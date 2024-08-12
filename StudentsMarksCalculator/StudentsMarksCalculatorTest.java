

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The test class StudentsMarksCalculatorTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class StudentsMarksCalculatorTest
{
        private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    // Test case 1: Check if the file reading works correctly
    @Test
    public void testReadInputFromFile() {
        StudentsMarksCalculator.Student[] students = StudentsMarksCalculator.readInputFromFile("test_file.csv");
        assertNotNull(students);
        assertEquals(15, students.length); // Check if 15 students were loaded
        assertEquals("Karki", students[0].lastName); // Validate data of first student
        assertEquals(27, students[0].marks[0]); // Validate A1 mark of the first student
    }

    // Test case 2: Check if the missing marks are handled correctly and set to 0.0
    @Test
    public void testMissingMarks() {
        StudentsMarksCalculator.Student[] students = StudentsMarksCalculator.readInputFromFile("test_file.csv");
        assertEquals(0.0, students[1].marks[1]); // A2 mark is missing, should be set to 0.0
        assertEquals(0.0, students[1].marks[2]); // A3 mark is missing, should be set to 0.0
    }

    // Test case 3: Check if the marks are printed correctly
    @Test
    public void testPrintMarks() {
        // Setup input
        StudentsMarksCalculator.Student[] students = StudentsMarksCalculator.readInputFromFile("test_file.csv");
    
        // Redirect the output to capture the printed content
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    
        // Call the method
        StudentsMarksCalculator.printMarks(students);
    
        // Reset System.out
        System.setOut(System.out);
    
        // Verify if 15 students have been printed
        String output = outContent.toString();
        int lineCount = output.split("\n").length - 1;
        assertEquals(17, lineCount, "17 lines have been printed");
    }

    // Test case 4: Check if the filtering by threshold works correctly
    @Test
    public void testFilterAndPrintMarksBelowThreshold() {
   // Setup input
    StudentsMarksCalculator.Student[] students = StudentsMarksCalculator.readInputFromFile("test_file.csv");

    // Define the threshold
    double thresholdMark = 70.0;  // Example threshold

    // Redirect the output to capture the printed content
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    // Call the method
    StudentsMarksCalculator.filterAndPrintMarksBelowThreshold(students, thresholdMark);

    // Reset System.out
    System.setOut(System.out);

    // Verify if the correct students have been printed
    String output = outContent.toString();
    String[] lines = output.split("\n");

    // Count student lines (excluding headers)
    int studentCount = 0;
    for (String line : lines) {
        if (line.trim().length() > 0 && !line.contains("Last Name") && !line.contains("First Name")) {
            studentCount++;
        }
    }

    // Expected number of students below the threshold
    int expectedCount = 8;  // Replace with the correct number of students below 70.0 total marks based on your data

    // Check if the correct number of students have been printed
    assertEquals(expectedCount, studentCount, "The number of students printed should match the expected count below the threshold.");
    }

    // Test case 5: Check if the sorting and filtering of top 5 and bottom 5 works correctly
    @Test
    public void testSortMarksAndFilterFiveTopBottomStudents() {
     // Setup input
    StudentsMarksCalculator.Student[] students = StudentsMarksCalculator.readInputFromFile("test_file.csv");

    // Redirect the output to capture the printed content
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    // Call the method
    StudentsMarksCalculator.sortMarksAndFilterFiveTopBottomStudents(students);

    // Reset System.out
    System.setOut(originalOut);

    // Parse the output
    String output = outContent.toString();
    String[] lines = output.split("\n");

    // Count the number of lines in the top 5 section
    int top5Start = -1, bottom5Start = -1;
    boolean inTop5 = false, inBottom5 = false;
    int top5Count = 0, bottom5Count = 0;

    for (String line : lines) {
        if (line.contains("Top 5 students with highest marks:")) {
            inTop5 = true;
            continue;
        }
        if (line.contains("Top 5 students with lowest marks:")) {
            inTop5 = false;
            inBottom5 = true;
            continue;
        }
        if (line.trim().isEmpty()) {
            continue;
        }

        if (inTop5) {
            top5Count++;
        } else if (inBottom5) {
            bottom5Count++;
        }
    }

    // Assert that exactly 5 students are printed in both sections
    assertEquals(6, top5Count, "There should be exactly 6 lines in the top 5 section.");
    assertEquals(6, bottom5Count, "There should be exactly 6 lines in the bottom 5 section.");
    }

    // Test case 6: Check if the program handles non-existent file
    @Test
    public void testNonExistentFile() {
        StudentsMarksCalculator.Student[] students = StudentsMarksCalculator.readInputFromFile("non_existent_file.csv");
        assertNull(students); // Should return null for non-existent file
    }

    // Test case 7: Check if the program lists CSV files correctly
    @Test
    public void testListFiles() {
       System.out.println("\nRunning testListFiles()...");
    
    // Capture the list of files before calling the method
    File folder = new File(".");
    File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

    // Call the method to test
    StudentsMarksCalculator.listFiles();

    // Check if "test_file.csv" is in the list
    String foundTestFile = "false";
    for (File file : listOfFiles) {
        if (file.getName().equals("test_file.csv")) {
            foundTestFile = "true";
            break;
        }
    }

    // Use assertTrue to assert that the test passes only if foundTestFile is true
        assertTrue(foundTestFile.contains("true"));
    }
    
    @Test
    public void testInvalidMenuInput() {
        // Setup the invalid input (e.g., a string or invalid number)
        String invalidInput = "abc";
        ByteArrayInputStream inContent = new ByteArrayInputStream(invalidInput.getBytes());
        System.setIn(inContent);

        // Capture the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Call the main method (or a method that includes the menu loop)
        StudentsMarksCalculator.main(new String[]{});

        // Reset System.out and System.in
        System.setOut(originalOut);
        System.setIn(System.in);

        // Get the output and split by lines
        String output = outContent.toString();
        String[] lines = output.split("\n");

        // Check for the presence of the invalid input message
        String invalidInputMessageFound = "";
        for (String line : lines) {
            if (line.contains("Invalid task. Please try again.")) {
                invalidInputMessageFound = "Invalid task. Please try again.";
                break;
            }
        }

        // Assert that the invalid input message is present
        assertTrue(invalidInputMessageFound.contains("Invalid task. Please try again."));
    }
}
