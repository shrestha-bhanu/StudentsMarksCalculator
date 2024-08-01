import java.util.Scanner;
/**
 * Write a description of class StudentsMarksCalculator here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class StudentsMarksCalculator
{

    /**
     * Constructor for objects of class StudentsMarksCalculator
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String fileName = "";
        
        while (true) {
            System.out.println("Student Marks Calculator");
            System.out.println("1. Read input from file");
            System.out.println("2. Print student marks");
            System.out.println("3. Print marks below threshold");
            System.out.println("4. Sort marks and filter students");
            System.out.println("5. Exit");
            System.out.println("Enter task (1-5)");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    System.out.println("Enter the file name: ");
                    fileName = scanner.nextLine();
                    break;
                case 2:
                    System.out.println("case 2");
                    break;
                case 3:
                    System.out.println("case 3");
                    break;
                case 4:
                    System.out.println("case 4");
                    break;
                case 5:
                    System.out.println("Exiting the program. Thank you");
                    System.exit(0);
                default:
                    System.out.println("Invalid task. Please try again.");
            }
        }
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
 
}
