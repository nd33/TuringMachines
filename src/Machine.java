import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//represents any TM given a description file
public class Machine {
    private List<State> states = new ArrayList<State>();
    private State initState;
    private State currState;
    private Tape tape;
    private boolean HALT = false;
    private int inputSize;
    private int transitionsMade;

    public Machine(TMDescriptionReader description){
        this.states = description.getStates();
        this.initState = description.getInitState();
        currState = initState;
        tape = new Tape();
    }

    public void runMachine(String inputFileName) throws InterruptedException{
        File inputFile = new File(inputFileName);
        if(inputFile.exists() && inputFile.isFile()){
            try {
                FileInputStream fs = new FileInputStream(inputFile);
                while(fs.available() > 0)
                    tape.getTapeList().add((char)fs.read());

                inputSize = tape.getTapeList().size();

                //if file is empty initialise our virtually infinite tape with a blank
                // because emtpy input results in only blank cells on tape
                if(inputFile.length() == 0)
                    tape.getTapeList().add('_');

                transitionsMade = 0;
                // make transitions until accepting or rejecting state reached
                // or loop forever :( (undesirable)
                while(!HALT){
                    makeStep(tape.getTapeList().get(tape.getIndex()));
                    transitionsMade++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // make a single transition from state to state if possible
    private void makeStep(char inputChar) throws InterruptedException{
        System.out.println("Current state is : " + currState.getName());
        // check if state is accepting and print out analysis information
        if(currState.isAccepting()){
            System.out.println("\nInput Accepted");
            System.out.println("Input size is: " + inputSize);
            System.out.println("Transitions made: " + transitionsMade);
            System.out.println("Space taken: " + tape.getTapeList().size());
            System.exit(0);
        }

        //check if input symbol exists in state transitions table for getting to s2
        for(Transition transition : currState.getTransitions()) {
            if (inputChar == transition.getTapeInput()) {
                tape.write(transition.getTapeOutput()); // output on tape
                if (transition.getMove() == 'L')
                    tape.moveLeft(); //move tape head left
                else if (transition.getMove() == 'R')
                    tape.moveRight(); //move tape head right
                for(State state : states)
                    if(state.getName().equals(transition.getState2())){
                        currState = state; // go to next state
//                        Thread.sleep(500); // show tape modification slowly on each step
                        return; // step finished
                }
            }
        }
        //if reached here then invalid input (reject)
        System.out.println("\nInput Rejected");
        System.exit(0);
    }
}
