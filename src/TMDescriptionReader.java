import javax.sql.rowset.spi.TransactionalWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TMDescriptionReader {
    private static final int NAME = 0;
    private static final int ACCEPTING = 1;
    private static final int ALPHABETLENGTH = 1;
    private static final int STATE1 = 0;
    private static final int STATE2 = 2;
    private static final int TAPEINPUT = 1;
    private static final int TAPEOUTPUT = 3;
    private static final int MOVE = 4;
    private File description;
    private List<State> states = new ArrayList<State>();
    private State initState;
    private char[] alphabet;

    public TMDescriptionReader(String description) {
        this.description = new File(description);
        if(!checkDescription(this.description)){
            System.out.println("Description file does not match TM pattern !");
            System.exit(0);
        }
    }

    //Thorough check for any malicious description is performed
    private boolean checkDescription(File description){
        int numOfStates;
        if(description.exists() && description.isFile()){
            try {
                BufferedReader r = new BufferedReader(new FileReader(description.getName()));
                String line = r.readLine();
                // index out of bounds if less than 7 chars
                if(line.length() < 8){
                    System.out.println("Invalid first line of description file !");
                    System.exit(0);
                }
                if(!line.substring(0,7).equals("states ")){
                    System.out.println("First line of Description file should be: \"states n\" where n is number of states");
                    return false;
                }
                try {
                    numOfStates = Integer.parseInt(line.substring(7,line.length()));
                    if(numOfStates <= 0){
                        System.out.println("Number of states should be > 0");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Number of states should be a valid integer");
                    e.printStackTrace();
                    return false;
                }
                line = r.readLine();

                //add state to list of states
                for(int i = 0 ; i < numOfStates && line != null; i++){
                    State state;
                    String[] tokens = line.split(" ");
                    if(tokens.length == 1)
                        state = new State(false, tokens[NAME]);
                    else if(tokens.length == 2 && tokens[ACCEPTING].equals("+"))
                        state = new State(true, tokens[NAME]);
                    else{
                        System.out.println("format for state lines is: \"state_name\" or \"state_name +\" for accepting states ");
                        return false;
                    }
                    states.add(state);

                    //assign start state
                    if(i == 0)
                        initState = state;

                    //read next state
                    line = r.readLine();
                }
                if(line == null) return false;

                //alphabet line
                String[] tokens = line.split(" ");
                if(tokens.length < 3 || !tokens[0].equals("alphabet")){
                    System.out.println("Format of alphabet line is: the word alphabet" +
                            "followed by the number of letters in the tape alphabet," +
                            "followed by those letters, separated by spaces ");
                    return false;
                }
                try {
                    int numOfLetters = Integer.parseInt(tokens[ALPHABETLENGTH]);
                    if(numOfLetters <= 0){
                        System.out.println("Number of letters in the alphabet should be > 0");
                        return false;
                    }
                    if(numOfLetters != tokens.length - 2){
                        System.out.println("Number of letters after alphabet and actual amount of letters in alphabet do not match");
                        return false;
                    }
                    alphabet = new char[numOfLetters + 1]; // + 1 for blank char (tape alphabet)
                } catch (NumberFormatException e) {
                    System.err.println("Number of letters in the alphabet should be a valid integer");
                    e.printStackTrace();
                    return false;
                }


                for(int i = 2 ; i < tokens.length ; i++){
                    if(tokens[i].length() != 1){
                        System.out.println("Each alphabet symbol should be of length 1");
                        return false;
                    }
                    //get the only char from each string token from alphabet
                    alphabet[i - 2] = tokens[i].charAt(0);
                }
                //add blank character "_" at the end of tape alphabet
                alphabet[alphabet.length - 1] = '_';

                //read transition lines in description
                line = r.readLine();
                if(line == null){
                    System.out.println("The TM description should include at least one transition.");
                    return false;
                }
                while(line != null){
                    String[] transitionTokens = line.split(" ");
                    // check if input,output and move are one char long
                    if(transitionTokens[TAPEOUTPUT].length() > 1 ||
                       transitionTokens[TAPEINPUT].length() > 1 ||
                       transitionTokens[MOVE].length() > 1) return false;

                    // booleans to validate all components of the transition
                    boolean validS1 = false;
                    boolean validInputCh = false;
                    boolean validS2 = false;
                    boolean validOutputCh = false;
                    boolean validMove = false;
                    // check states
                    for(State state : states){
                        if(transitionTokens[STATE1].equals(state.getName()))
                            validS1 = true;
                        if(transitionTokens[STATE2].equals(state.getName()))
                            validS2 = true;
                    }

                    //check tape alphabet characters
                    for(char ch : alphabet){
                        if(transitionTokens[TAPEINPUT].charAt(0) == ch)
                            validInputCh = true;
                        if(transitionTokens[TAPEOUTPUT].charAt(0) == ch)
                            validOutputCh = true;
                    }

                    //check move
                    if(transitionTokens[MOVE].charAt(0) == 'L' ||
                       transitionTokens[MOVE].charAt(0) == 'R' ||
                       transitionTokens[MOVE].charAt(0) == 'S') validMove = true;

                    //validate components
                    if(!validS1 || !validInputCh || !validS2 || !validOutputCh || !validMove)
                        return false;
                    else{
                        // create new transaction
                        Transition transition = new Transition(transitionTokens[STATE1], transitionTokens[TAPEINPUT].charAt(0),
                                transitionTokens[STATE2], transitionTokens[TAPEOUTPUT].charAt(0), transitionTokens[MOVE].charAt(0));
                        for(State state : states)
                            if(state.getName().equals(transition.getState1()))
                                state.getTransitions().add(transition);
                    }
                    line = r.readLine();
                }
//                displayDescription();  // line used for debugging purposes

                return true;
            } catch (IOException e) {
                System.err.println("Problem occured when trying to read from file.");
                e.printStackTrace();
            }
        }
        else{
            System.out.println(description.getName() + " does not exist or is not a file");
            System.exit(0);
        }
        return false;
    }

    //method for display purposes
    public void displayDescription(){
        System.out.println("initial state is: " + initState.getName());
        for(State state : states){
            for(Transition transition : state.getTransitions()){
                System.out.println(transition.toString());
            }
        }
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public State getInitState() {
        return initState;
    }

    public void setInitState(State initState) {
        this.initState = initState;
    }

    public char[] getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(char[] alphabet) {
        this.alphabet = alphabet;
    }
}
