package net.daifo.services;

import net.daifo.models.ClassroomModel;
import net.daifo.models.RegularStudentModel;
import net.daifo.models.ScholarshipStudentModel;
import net.daifo.models.StudentModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Service class that handles all menu-driven operations for the student system.
 * Each public method corresponds to a menu option in the main loop.
 */
public class MenuService {
    private ClassroomModel cm;
    private final StatsService stats = new StatsService();

    /**
     * Creates a new MenuService bound to the given classroom.
     *
     * @param cm the classroom this service will operate on
     */
    public MenuService(ClassroomModel cm) {
        this.cm = cm;
    }

    /**
     * Displays a list of existing classrooms and lets the user pick one,
     * or create a new one. If no classrooms exist, a new one is created automatically.
     * Newly created classrooms are added to the provided list.
     *
     * @param cms the list of available classrooms (may be mutated by adding new ones)
     * @return the selected or newly created ClassroomModel
     */
    public static ClassroomModel pickClassroom(ArrayList<ClassroomModel> cms) {
        if (cms.isEmpty()) {
            System.out.println("No classrooms found. Creating new classroom...");
            ClassroomModel newCm = new ClassroomModel();
            cms.add(newCm);
            return newCm;
        }

        System.out.println("Choose a classroom: ");
        System.out.printf("%-5s %-36s%n", "#", "Classroom ID");
        System.out.println("-------------------------------------------");
        for (int i = 0; i < cms.size(); i++) {
            System.out.printf("%-5d %-36s%n", i + 1, cms.get(i).getId());
        }
        System.out.printf("%-5d %-36s%n", cms.size() + 1, "Create new classroom");

        Scanner scanner = new Scanner(System.in);
        int maxChoice = cms.size() + 1;
        int choice = 0;
        while (choice < 1 || choice > maxChoice) {
            System.out.print("Enter classroom number: ");

            boolean isNumber = scanner.hasNextInt();

            if (!isNumber) {
                System.out.println("Invalid input. Enter a number.");
                scanner.next();
                continue;
            }

            choice = scanner.nextInt();
            boolean isOutOfRange = choice < 1 || choice > maxChoice;

            if (isOutOfRange) {
                System.out.println("Invalid choice. Pick between 1 and " + maxChoice + ".");
            }
        }

        // Last option creates a new classroom
        if (choice == maxChoice) {
            ClassroomModel newCm = new ClassroomModel();
            cms.add(newCm);
            return newCm;
        }

        return cms.get(choice - 1);
    }

    /**
     * Switches the current classroom by prompting the user to pick a different one.
     *
     * @param cms the list of available classrooms
     */
    public void switchClassroom(ArrayList<ClassroomModel> cms) {
        this.cm = pickClassroom(cms);
    }

    /**
     * Registers one or more students into the current classroom.
     * Prompts the user for the amount of students (1-20), then for each student
     * collects their first name, last name, age (16-40), and type (regular/scholarship).
     */
    public void registerStudents() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the amount of students to register: ");
        int amount = 0;
        while (amount < 1 || amount > 20) {
            if (scanner.hasNextInt()) {
                amount = scanner.nextInt();
                if (amount < 1 || amount > 20) {
                    System.out.println("Invalid amount. Must be between 1 and 20. Try again.");
                }
            } else {
                System.out.println("Invalid input. Enter a number.");
                scanner.next();
            }
        }

        for (int i = 0; i < amount; i++) {
            // LOG the ith student, so we can keep track of how many students are left to register.
            System.out.println("\nStudent " + (i + 1) + "/" + amount);

            // ASK FirstName
            System.out.println("Enter student first name: ");
            final String studentFirstName = scanner.next();

            // ASK LastName
            System.out.println("Enter student last name: ");
            final String studentLastName = scanner.next();

            // ASK Age
            int studentAge = 0;
            System.out.println("Enter student age: ");
            int lowerLimit = 16;
            int higherLimit = 40;
            while (studentAge < lowerLimit || studentAge > higherLimit) {
                boolean isNumber = scanner.hasNextInt();

                if (!isNumber) {
                    System.out.println("Invalid input. Enter a number.");
                    scanner.next();
                    continue;
                }

                studentAge = scanner.nextInt();
                boolean isOutOfRange = studentAge < lowerLimit || studentAge > higherLimit;

                if (isOutOfRange) {
                    System.out.println("Invalid student age. Has to be between (16-40). Try again.");
                }
            }

            // ASK Type
            String studentType = askStudentType(); // This function ensures the string is "s" or "r".

            // CREATE Student and add to classroom
            StudentModel student = (studentType.equalsIgnoreCase("s"))
                ? new ScholarshipStudentModel(studentFirstName, studentLastName, studentAge)
                : new RegularStudentModel(studentFirstName, studentLastName, studentAge);

            cm.getStudents().add(student);
        }

        System.out.println("\nStudents registered successfully!");
    }

    /**
     * Displays a formatted table of all students in the current classroom
     * along with their grades and pass/fail status.
     * Shows "N/A" for students who have not been graded yet.
     */
    public void viewGrades() {
        ArrayList<StudentModel> students = cm.getStudents();
        HashMap<String, Double> grades = cm.getGrades();

        if (students == null || students.isEmpty()) {
            System.out.println("No students registered yet.");
            return;
        }

        System.out.println("\nGrades for class: " + cm.getId());
        System.out.printf("%-5s %-15s %-15s %-8s %-8s%n", "#", "First Name", "Last Name", "Grade", "Status");
        System.out.println("-------------------------------------------------------------");
        for (int i = 0; i < students.size(); i++) {
            StudentModel s = students.get(i);
            Double grade = grades != null ? grades.get(s.getId()) : null;
            String gradeStr = grade != null ? String.format("%.2f", grade) : "N/A";
            String statusStr = grade != null ? stats.getStatus(grade) : "N/A";
            System.out.printf("%-5d %-15s %-15s %-8s %-8s%n",
                    i + 1, s.getFirstName(), s.getLastName(), gradeStr, statusStr);
        }
    }

    /**
     * Prompts the user to enter a grade (0-10) for each student in the current classroom.
     * Grades are stored in the classroom's grades HashMap keyed by student ID.
     */
    public void registerGrades() {
        ArrayList<StudentModel> students = cm.getStudents();

        if (students.isEmpty()) {
            System.out.println("No students registered yet. Register students first.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        HashMap<String, Double> grades = cm.getGrades();

        for (int i = 0; i < students.size(); i++) {
            StudentModel student = students.get(i);
            int current = i + 1;
            int total = students.size();
            String fullName = student.getFirstName() + " " + student.getLastName();

            System.out.printf("\n(%d/%d) %s%n", current, total, fullName);

            double grade = askGrade(scanner);
            grades.put(student.getId(), grade);
        }

        System.out.println("\nGrades registered successfully!");
    }

    /**
     * Displays statistics for the current classroom including:
     * group average, highest grade, lowest grade, and count of passing students.
     * Requires students and grades to be registered first.
     */
    public void viewStatistics() {
        ArrayList<StudentModel> students = cm.getStudents();
        HashMap<String, Double> grades = cm.getGrades();

        if (students.isEmpty() || grades.isEmpty()) {
            System.out.println("No grades registered yet.");
            return;
        }

        double[] gradesArray = cm.getGradesArray();
        double groupAvg = stats.calculateAverage(gradesArray);
        double highestGrade = stats.highestGrade(gradesArray);
        double lowestGrade = stats.lowestGrade(gradesArray);
        int countPassed = stats.countPassed(gradesArray);

        System.out.println("Statistic for class: " + cm.getId());
        System.out.println("Group Avg: " + groupAvg);
        System.out.println("Highest Grade: " + highestGrade);
        System.out.println("Lowest Grade: " + lowestGrade);
        System.out.println("Count Passed: " + countPassed);
    }

    /**
     * Displays a table of all registered students and lets the user pick one
     * to view their full info: ID, name, age, type, grade, and pass/fail status.
     */
    public void searchStudents() {
        ArrayList<StudentModel> students = cm.getStudents();
        HashMap<String, Double> grades = cm.getGrades();

        if (students == null || students.isEmpty()) {
            System.out.println("No students registered yet.");
            return;
        }

        System.out.println("\nRegistered students:");
        System.out.printf("%-5s %-15s %-15s %-36s%n", "#", "First Name", "Last Name", "Student ID");
        System.out.println("-------------------------------------------------------------------------");
        for (int i = 0; i < students.size(); i++) {
            StudentModel s = students.get(i);
            System.out.printf("%-5d %-15s %-15s %-36s%n",
                    i + 1, s.getFirstName(), s.getLastName(), s.getId());
        }

        int choice = 0;
        Scanner scanner = new Scanner(System.in);
        while (choice < 1 || choice > students.size()) {
            System.out.print("\nEnter student number to look up: ");
            boolean isNumber = scanner.hasNextInt();

            if (!isNumber) {
                System.out.println("Invalid input. Enter a number.");
                scanner.next(); // Discard invalid input
                continue;
            }

            choice = scanner.nextInt();
            boolean isOutOfRange = choice < 1 || choice > students.size();

            if (isOutOfRange) {
                System.out.println("Invalid choice. Pick between 1 and " + students.size() + ".");
            }
        }

        StudentModel student = students.get(choice - 1);
        Double grade = grades != null ? grades.get(student.getId()) : null;
        String gradeStr = grade != null ? String.format("%.2f", grade) : "N/A";
        String statusStr = grade != null ? stats.getStatus(grade) : "N/A";

        System.out.println("\n--- Student Info ---");
        System.out.println("ID:         " + student.getId());
        System.out.println("Name:       " + student.getFirstName() + " " + student.getLastName());
        System.out.println("Age:        " + student.getAge());
        System.out.println("Type:       " + student.getType());
        System.out.println("Grade:      " + gradeStr);
        System.out.println("Status:     " + statusStr);
    }

    /**
     * Prompts the user for a grade between 0 and 10 with input validation.
     * Loops until a valid number within range is entered.
     *
     * @param scanner the Scanner instance to read input from
     * @return a valid grade between 0 and 10
     */
    private double askGrade(Scanner scanner) {
        double grade = -1;

        while (grade < 0 || grade > 10) {
            System.out.print("Enter grade (0-10): ");

            boolean isNumber = scanner.hasNextDouble();

            if (!isNumber) {
                System.out.println("Invalid input. Enter a number.");
                scanner.next();
                continue;
            }

            grade = scanner.nextDouble();
            boolean isOutOfRange = grade < 0 || grade > 10;

            if (isOutOfRange) {
                System.out.println("Invalid grade. Must be between 0 and 10.");
            }
        }

        return grade;
    }

    /**
     * Recursively prompts the user for a student type until a valid input ("r" or "s") is given.
     *
     * @return "r" for regular or "s" for scholarship (lowercase)
     */
    private String askStudentType() {
        System.out.println("r: regular s: scholarship");
        System.out.println("Enter student type (r/s): ");
        String input = new Scanner(System.in).next();

        if (input.equalsIgnoreCase("r") || input.equalsIgnoreCase("s")) {
            return input.toLowerCase();
        } else {
            System.out.println("Invalid student type. Try again.");
            return askStudentType();
        }
    }
}
