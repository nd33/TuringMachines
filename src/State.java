import java.util.ArrayList;
import java.util.List;
// represents a state in the turing machine
public class State {
    private boolean accepting;
    private String name;
    private List<Transition> transitions;

    public State(boolean accepting, String name) {
        this.accepting = accepting;
        this.name = name;
        transitions = new ArrayList<Transition>();
    }

    public boolean isAccepting() {
        return accepting;
    }

    public void setAccepting(boolean accepting) {
        this.accepting = accepting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }
}
