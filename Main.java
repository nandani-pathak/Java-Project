// ========================================
// Main.java - Entry Point
// (Branch-aware BTech result processing)
// ========================================

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        printHeader();

        int n = readStudentCount(sc);
        Student[] students = new Student[n];

        for (int i = 0; i < n; i++) {
            System.out.println("\n--- Student " + (i + 1) + " ---");

            System.out.print("Name       : ");
            String name = sc.nextLine().trim();

            System.out.print("Roll No    : ");
            int rollNo = readInt(sc, "Please enter a valid roll number.");

            String branch = readBranch(sc);
            String[] subjects = GradeManager.getSubjectsForBranch(branch);
            double[] marks = new double[subjects.length];

            System.out.println("Enter marks for " + branch + " subjects:");
            for (int j = 0; j < subjects.length; j++) {
                marks[j] = readValidatedMark(sc, subjects[j]);
            }

            students[i] = new Student(name, rollNo, branch, subjects, marks);
        }

        System.out.println("\n\n========== STUDENT RECORDS ==========");
        for (Student s : students) {
            s.display();
        }

        System.out.println("\n" + GradeManager.generateReport(students));

        if (n > 0) {
            System.out.println("\n========== DEPARTMENT TOPPERS ==========");
            String[] branches = GradeManager.getAvailableBranches();
            for (String branch : branches) {
                Student branchTopper = GradeManager.findTopperByBranch(students, branch);
                if (branchTopper != null) {
                    System.out.println(branch + " Topper : " + branchTopper.getName()
                            + " | Roll No: " + branchTopper.getRollNo()
                            + " | Percentage: " + String.format("%.1f", branchTopper.getPercentage()) + "%");
                }
            }

            Student topper = GradeManager.findTopper(students);
            System.out.println("\nUNIVERSITY RANK 1 : " + topper.getName()
                    + " | Branch: " + topper.getBranch()
                    + " | Roll No: " + topper.getRollNo()
                    + " | Percentage: " + String.format("%.1f", topper.getPercentage()) + "%");

            int passed = GradeManager.countPassed(students);
            System.out.println("Passed : " + passed + " / " + n);
            System.out.println("Failed : " + (n - passed) + " / " + n);
        } else {
            System.out.println("No students were entered.");
        }

        sc.close();
    }

    private static void printHeader() {
        System.out.println("==============================================================");
        System.out.println("               COLLEGE STUDENT DATABASE SYSTEM");
        System.out.println("            BTECH ACADEMIC RECORD MANAGEMENT PORTAL");
        System.out.println("==============================================================");
        System.out.println("Available Departments: CSE, IT, AI/ML, ECE, MECH, CIVIL, OTHER\n");
    }

    private static int readStudentCount(Scanner sc) {
        while (true) {
            System.out.print("Enter number of student records to create: ");
            try {
                int count = Integer.parseInt(sc.nextLine().trim());
                if (count < 0) {
                    System.out.println("  !! Number of students cannot be negative.");
                    continue;
                }
                return count;
            } catch (NumberFormatException e) {
                System.out.println("  !! Please enter a whole number.");
            }
        }
    }

    private static int readInt(Scanner sc, String errorMessage) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  !! " + errorMessage);
                System.out.print("Try again   : ");
            }
        }
    }

    private static String readBranch(Scanner sc) {
        while (true) {
            System.out.println("Select Department:");
            System.out.println("1. CSE");
            System.out.println("2. IT");
            System.out.println("3. AI/ML");
            System.out.println("4. ECE");
            System.out.println("5. MECH");
            System.out.println("6. CIVIL");
            System.out.println("7. OTHER");
            System.out.print("Enter choice : ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    return "CSE";
                case "2":
                    return "IT";
                case "3":
                    return "AI/ML";
                case "4":
                    return "ECE";
                case "5":
                    return "MECH";
                case "6":
                    return "CIVIL";
                case "7":
                    return "OTHER";
                default:
                    System.out.println("  !! Please choose a number from 1 to 7.");
            }
        }
    }

    private static double readValidatedMark(Scanner sc, String subjectName) {
        while (true) {
            System.out.print(subjectName + " : ");
            try {
                double value = Double.parseDouble(sc.nextLine().trim());
                GradeManager.validateMark(value);
                return value;
            } catch (InvalidMarksException e) {
                System.out.println("  !! Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("  !! Please enter a valid number.");
            }
        }
    }
}
