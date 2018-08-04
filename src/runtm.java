import java.util.Scanner;

public class runtm {

    // simulate one-tape deterministic Turing machine on given input string

    //run TM on the input string

    public static void main(String[] args) throws InterruptedException{
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter TM description file name: ");
        String descName = scan.next();
        System.out.print("Enter TM input file name: ");
        String inputName = scan.next();
        TMDescriptionReader reader = new TMDescriptionReader(descName);
        Machine TM = new Machine(reader);
        TM.runMachine(inputName);
    }
}
