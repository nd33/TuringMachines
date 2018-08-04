import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

// In my implementation the tape has a start (the leftmost cell), which the head cannot exceed going left (is blocked)
// and is infinite on the right side
// tape is really not infinite but as we can move only one step and on each step
// a new cell is added if necessary it virtually is infinite
public class Tape {
    private List<Character> tapeList;
    private int index;
    private static final char BLANK = '_';
    private static final int TAPESTART = 0;

    public Tape() {
        this.tapeList = new ArrayList<Character>();
        this.index = TAPESTART;
    }

    //move right func if array element not added add a blank symbol, inc index
    public void moveRight(){
        if(index + 1 == tapeList.size())
            tapeList.add(BLANK);
        index++;
    }

    //move left func if at start, stay put, otherwise decrement index
    public void moveLeft(){
        if(index != TAPESTART)
            index--;
    }

    //write in tape cell
    public void write(char ch){
        tapeList.set(index, ch);
        for(Character character : tapeList)
            System.out.print(character);
        System.out.println();
    }

    public List<Character> getTapeList() {
        return tapeList;
    }

    public void setTapeList(List<Character> tapeList) {
        this.tapeList = tapeList;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
