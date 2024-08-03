import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
/**
 * StudentsMarksCalculator class
 * This class handles various calculations on student marks such as reading data from a file, printing marks,
 * filtering marks based on a given threshold, and sorting the top 5 highest and lowest marks.
 *
 * @author Bhanu Pratap Shrestha
 * @version (3 August 2024)
 */
public class StudentsMarksCalculator
{

    /**
     * Main method of the program
     * Implements a menu interface for user interaction
     */
    public static void main(String[] args) {
        // Initialize scanner for user input and store in a var to easily access when neeeded.
        Scanner scanner = new Scanner(System.in);
        String fileName = "";
        Student[] students = null;
        
        // Menu loop        
        while (true) {
            // Print menu options on display
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Student Marks Calculator");
            System.out.println("1. Read input from file");
            System.out.println("2. Print student marks");
            System.out.println("3. Print marks below threshold");
            System.out.println("4. Sort marks and filter students");
            System.out.println("5. Exit");
            System.out.println("Enter task (1-5)");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            
            // Store user choice
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            // Handle user choice based on the each menu option
            switch (choice) {
                case 1:
                    listFiles();
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
                    if (students == null) {
                        System.out.println("Please select a file first.");
                    } else {
                        sortMarksAndFilterStudents(students);
                    }
                    break;
                case 5:
                    System.out.println("Exiting the program. Thank you");
                    System.exit(0);
                default:
                    System.out.println("Invalid task. Please try again.");
            }
        }
    }
    
    private static void listFiles() {
        // List available CSV files in the directory
        File folder = new File(".");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
        
        if (listOfFiles == null || listOfFiles.length == 0) {
            System.out.println("No files found. The directory is empty.");
        } else {
            System.out.println("List of CSV files in the directory:");
            for (File file : listOfFiles) {
                System.out.println(file.getName());
            }
        }
        

    }
    
    /**
     * static Student class
     * Contains all student details and marks
     */
    static class Student {
        // student attributes
        String lastName;
        String firstName;
        String studentId;
        double[] marks;
        double totalMark;
        
        // constructor
        public Student(String lastName, String firstName, String studentId, double[] marks) {
            // initialize student details
            this.lastName = lastName;
            this.firstName = firstName;
            this.studentId = studentId;
            this.marks = marks;
            this.totalMark = marks[0] + marks[1] + marks[2];
        }
    }
    
    
    /**
     * Read student's datas from a file that already exists in the root folder.
     *
     * @param  filename  Name of the file to read the data from
     * @return    Array of Student objects
     */
    private static Student[] readInputFromFile(String fileName) {
        // Creating an ArrayList of Student objects
        ArrayList<Student> studentList = new ArrayList<>();
        String unitName = "";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean firstLine = true;
            boolean isHeaderSkipped = false;
    
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue; // Skip comments starting with #
                }
                
                // Get unit name from the first line
                if (firstLine) {
                    unitName = line.trim();
                    firstLine = false;
                    continue;
                }
    
                // Skip the header line (2nd line) after the unit name line
                if (!isHeaderSkipped) {
                    isHeaderSkipped = true;
                    continue;
                }
                
                // Split the line and store them in columns array
                String[] column = line.split(",");
                
                // Check at least 3 fields: lastName, firstName, studentId exist
                if (column.length >= 3) {
                    String lastName = column[0].trim();
                    String firstName = column[1].trim();
                    String studentId = column[2].trim();
                    double[] marks = new double[3];
                    
                    //Loop the three assignment marks
                    for (int i = 0; i < 3; i++) {
                        // Check if the mark column exists (to resolve the ArrayOutofBoundsException error)
                        if (i + 3 < column.length) {
                            String mark = column[i + 3].trim();
                            if (!mark.isEmpty()) {
                                try {
                                    marks[i] = Double.parseDouble(mark);
                                } catch (NumberFormatException e) {
                                    // Handle invalid mark
                                    System.out.println("Error: Invalid mark '" + mark + "' for " + firstName + " " + lastName + " (ID: " + studentId + ")");
                                    marks[i] = 0.0; // Assign default value for invalid numbers
                                }
                            } else {
                                // Handle missing mark column
                                System.out.printf("Warning: Missing mark for %s %s (ID: %s) in Assignment %d\n", firstName, lastName, studentId, i + 1);                                
                                marks[i] = 0.0; // Assign default value for missing marks
                            }
                        } else {
                            //Handle missing mark column
                            System.out.printf("Warning: Missing mark for %s %s (ID: %s) in Assignment %d\n", firstName, lastName, studentId, i + 1);                                

                            marks[i] = 0.0; // Assign default value for missing marks
                        }
                        
                    }
                    // Create and add new Student object to the studentList
                    studentList.add(new Student(lastName, firstName, studentId, marks));
                } else {
                    // Handle insufficient data 
                    System.out.println("Error: Insufficient data on line: " + line);
                }
            }
        } catch (IOException e) {
            // Handle file reading errors
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        
        // Print unit name and number of students from the file
        System.out.println("\n" + unitName);
        System.out.println("Number of students: " + studentList.size() + "\n");
        
        // Convert ArrayList to array and return
        return studentList.toArray(new Student[0]);
    }
    
    /**
     * After reading the file, this method prints marks for all students
     *
     * @param  students  Array of Student objects
     */
    private static void printMarks(Student[] students) {
        // Print a title for the student marks list
        System.out.println("\nStudent-wise marks:");
        
        // print column headers with formatted string
        // %-30s: Left-aligned string, 30 characters wide
        // %-20s: Left-aligned string, 20 characters wide
        // %s: String
        // \t: Tab character for additional spacing
        System.out.printf("%-30s %-20s\t%s\t%s\t\t%s\t\t%s\t\t%s\n",
            "Last Name", 
            "First Name",
            "Student ID",
            "Assignment 1",
            "Assignment 2",
            "Assignment 3",
            "Total Mark");
        
        // Iterate through each student in the array
        for (Student student : students) {
            // Print each student details and marks
            System.out.printf("%-30s %-20s\t%s\t%.2f\t\t\t%.2f\t\t\t%.2f\t\t\t%.2f\n",
                student.lastName, 
                student.firstName, 
                student.studentId,
                student.marks[0], 
                student.marks[1], 
                student.marks[2], 
                student.totalMark);
        }
    }
    
    /**
     * Filters and prints students with marks below a threshold value
     *
     * @param  students  Array of Student objects
     * @param    thresholdMark the threshold mark input provided by the user
     */
    private static void filterAndPrintMarks(Student[] students, double thresholdMark) {
        // flag to check if students are found below the threshold
        boolean entriesFound = false;
        
        // Iterate through each student in the array
        for (Student student : students) {
            // Check if the student's mark is below threshold
            if (student.totalMark < thresholdMark) {
                // If this is the first entry below threshold print headers
                if (!entriesFound) {
                    System.out.println("\nStudents with total marks below " + thresholdMark + ":");
                    System.out.printf("%-30s %-20s\t%s\t%s\t\t%s\t\t%s\t\t%s\n",
                        "Last Name", 
                        "First Name",
                        "Student ID",
                        "Assignment 1",
                        "Assignment 2",
                        "Assignment 3",
                        "Total Mark");
                    entriesFound = true;
                }
                System.out.printf("%-30s %-20s\t%s\t%.2f\t\t\t%.2f\t\t\t%.2f\t\t\t%.2f\n",
                    student.lastName, 
                    student.firstName, 
                    student.studentId,
                    student.marks[0], 
                    student.marks[1], 
                    student.marks[2], 
                    student.totalMark);
            }
        }
        
        // If no students were found below the given threshold print a message
        if (!entriesFound) {
            System.out.println("\nSorry, no entries found! No students have total marks below " + thresholdMark);
        }
    }
    
    /**
     * Sort students by total marks and print the top 5 and bottom 5 student data
     *
     * @param  students  Array of Student objects
     */
    private static void sortMarksAndFilterStudents(Student[] students) {
        
        // Using bubble sort algorithm to sort students by total mark in descending order
        for (int i =0; i < students.length -1; i++) {
            for (int j=0; j < students.length - i - 1; j++) {
                if (students[j].totalMark < students[j+1].totalMark) {
                    // Swap students if they are in the wrong order
                    Student temp = students[j];
                    students[j] = students[j+1];
                    students[j+1] = temp;
                }
            }
        }
        
        // Print top 5 students with highest mark
        System.out.println("\nTop 5 students with highest marks:");
        
        // Print header
        System.out.printf("%-30s %-20s\t%s\t%s\t\t%s\t\t%s\t\t%s\n","Last Name", "First Name","Student ID","Assignment 1","Assignment 2","Assignment 3","Total Mark");
        
        // Itereate through students and print details of top 5 students
        for (int i=0; i < Math.min(5, students.length); i++) {
            System.out.printf("%-30s %-20s\t%s\t%.2f\t\t\t%.2f\t\t\t%.2f\t\t\t%.2f\n",
                students[i].lastName, students[i].firstName, students[i].studentId, students[i].marks[0], students[i].marks[1], students[i].marks[2], students[i].totalMark);

        }
        
        // Print top 5 students with lowest mark
        System.out.println("\nTop 5 students with lowest marks:");
        
        // Print header
        System.out.printf("%-30s %-20s\t%s\t%s\t\t%s\t\t%s\t\t%s\n","Last Name", "First Name","Student ID","Assignment 1","Assignment 2","Assignment 3","Total Mark");
        
        // Iterate through students and print details of bottom 5 students
        for (int i=students.length - 1; i >= Math.max(0, students.length - 5); i--) {
            System.out.printf("%-30s %-20s\t%s\t%.2f\t\t\t%.2f\t\t\t%.2f\t\t\t%.2f\n",
                students[i].lastName, students[i].firstName, students[i].studentId, students[i].marks[0], students[i].marks[1], students[i].marks[2], students[i].totalMark);

        }
    }

}