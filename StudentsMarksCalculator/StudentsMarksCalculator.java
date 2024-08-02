import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
        Student[] students = null;
        
        while (true) {
            System.out.println("----------------------------------------------------------------------------------------");
            System.out.println("Student Marks Calculator");
            System.out.println("1. Read input from file");
            System.out.println("2. Print student marks");
            System.out.println("3. Print marks below threshold");
            System.out.println("4. Sort marks and filter students");
            System.out.println("5. Exit");
            System.out.println("Enter task (1-5)");
            System.out.println("-----------------------------------------------------------------------------------------\n");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    System.out.println("Enter the file name: ");
                    fileName = scanner.nextLine();
                    students = readInputFromFile(fileName);
                    break;
                case 2:
                    if (students == null) {
                        System.out.println("Please select a file first.");
                    } else {
                        printMarks(students);
                    }
                    break;
                case 3:
                    if (students == null) {
                        System.out.println("Please select a file first.");
                    } else {
                        System.out.println("Please enter the threshold: ");
                        double thresholdMark = scanner.nextDouble();
                        filterAndPrintMarks(students, thresholdMark);
                    }
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
    
    static class Student {
        String lastName;
        String firstName;
        String studentId;
        double[] marks;
        double totalMark;
        
        public Student(String lastName, String firstName, String studentId, double[] marks) {
            this.lastName = lastName;
            this.firstName = firstName;
            this.studentId = studentId;
            this.marks = marks;
            this.totalMark = marks[0] + marks[1] + marks[2];
        }
    }
    
    private static Student[] readInputFromFile(String fileName) {
        ArrayList<Student> studentList = new ArrayList<>();
        String unitName = "";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean firstLine = true;
            boolean isHeaderSkipped = false;
    
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue; // Skip comments.
                }
                
                // Skip first line
                if (firstLine) {
                    unitName = line.trim();
                    firstLine = false;
                    continue;
                }
    
                // Skip heading row
                if (!isHeaderSkipped) {
                    isHeaderSkipped = true;
                    continue;
                }
    
                String[] column = line.split(",");
                
                if (column.length >= 3) { // Ensure there are at least 3 fields: lastName, firstName, studentId
                    String lastName = column[0].trim();
                    String firstName = column[1].trim();
                    String studentId = column[2].trim();
                    double[] marks = new double[3];
    
                    for (int i = 0; i < 3; i++) {
            
                        if (i + 3 < column.length) { // Ensure index exists.
                            String mark = column[i + 3].trim();
                            if (!mark.isEmpty()) {
                                try {
                                    marks[i] = Double.parseDouble(mark);
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: Invalid mark '" + mark + "' for " + firstName + " " + lastName + " (ID: " + studentId + ")");
                                    marks[i] = 0.0; // Assign default value for invalid numbers
                                }
                            } else {
                                //System.out.println("Warning: Missing mark for " + firstName + " " + lastName + " (ID: " + studentId + ")");
                                System.out.printf("Warning: Missing mark for %s %s (ID: %s) in Assignment %d\n", firstName, lastName, studentId, i + 1);                                
                                marks[i] = 0.0; // Assign default value for empty marks
                            }
                        } else {
                            //System.out.println("Warning: Missing mark for " + firstName + " " + lastName + " (ID: " + studentId + ")");
                            System.out.printf("Warning: Missing mark for %s %s (ID: %s) in Assignment %d\n", firstName, lastName, studentId, i + 1);                                

                            marks[i] = 0.0; // Assign default value for missing marks
                        }
                        
                    }
    
                    studentList.add(new Student(lastName, firstName, studentId, marks));
                } else {
                    System.out.println("Error: Insufficient data on line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    
        System.out.println("\n" + unitName);
        System.out.println("Number of students: " + studentList.size() + "\n");
        return studentList.toArray(new Student[0]);
    }

    private static void printMarks(Student[] students) {
        System.out.println("\nStudent-wise marks:");
        System.out.printf("%-30s %-20s\t%s\t%s\t\t%s\t\t%s\t\t%s\n","Last Name", "First Name","Student ID","Assignment 1","Assignment 2","Assignment 3","Total Mark");
        for (Student student : students) {
            System.out.printf("%-30s %-20s\t%s\t%.2f\t\t\t%.2f\t\t\t%.2f\t\t\t%.2f\n",
                    student.lastName, student.firstName, student.studentId,
                    student.marks[0], student.marks[1], student.marks[2], student.totalMark);
        }
    }

    private static void filterAndPrintMarks(Student[] students, double thresholdMark) {
        boolean entriesFound = false;
        for (Student student : students) {
            if (student.totalMark < thresholdMark) {
                if (!entriesFound) {
                    System.out.println("\nStudents with total marks below " + thresholdMark + ":");
                    System.out.printf("%-30s %-20s\t%s\t%s\t\t%s\t\t%s\t\t%s\n","Last Name", "First Name","Student ID","Assignment 1","Assignment 2","Assignment 3","Total Mark");
                    entriesFound = true;
                }
                System.out.printf("%-30s %-20s\t%s\t%.2f\t\t\t%.2f\t\t\t%.2f\t\t\t%.2f\n",
                student.lastName, student.firstName, student.studentId,
                student.marks[0], student.marks[1], student.marks[2], student.totalMark);
            }
        }
        
        if (!entriesFound) {
            System.out.println("\nNo students have total marks below " + thresholdMark);
        }
    }

}

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */

