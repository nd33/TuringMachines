public class Transition {
    private String state1;
    private char tapeInput;
    private String state2;
    private char tapeOutput;
    private char move;

    public Transition(String state1, char tapeinput, String state2, char tapeoutput, char move) {
        this.state1 = state1;
        this.tapeInput = tapeinput;
        this.state2 = state2;
        this.tapeOutput = tapeoutput;
        this.move = move;
    }

    public String getState1() {
        return state1;
    }

    public void setState1(String state1) {
        this.state1 = state1;
    }

    public char getTapeInput() {
        return tapeInput;
    }

    public void setTapeInput(char tapeInput) {
        this.tapeInput = tapeInput;
    }

    public String getState2() {
        return state2;
    }

    public void setState2(String state2) {
        this.state2 = state2;
    }

    public char getTapeOutput() {
        return tapeOutput;
    }

    public void setTapeOutput(char tapeOutput) {
        this.tapeOutput = tapeOutput;
    }

    public char getMove() {
        return move;
    }

    public void setMove(char move) {
        this.move = move;
    }

    @Override
    public String toString() {
        return "Transition{" +
                "state1='" + state1 + '\'' +
                ", tapeInput=" + tapeInput +
                ", state2='" + state2 + '\'' +
                ", tapeOutput=" + tapeOutput +
                ", move=" + move +
                '}';
    }
}
