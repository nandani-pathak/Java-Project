// ========================================
// Main.java - Entry Point
// (Ties together: Classes, Inheritance, Exceptions, Arrays, StringBuffer, Wrapper Classes)
// ========================================

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("==================================");
        System.out.println("      STUDENT GRADE MANAGER");
        System.out.println("     Unit II - Java Fundamentals");
        System.out.println("==================================\n");

        int n = readStudentCount(sc);
        Student[] students = new Student[n];

        for (int i = 0; i < n; i++) {
            System.out.println("\n--- Student " + (i + 1) + " ---");

            System.out.print("Name       : ");
            String name = sc.nextLine().trim();

            System.out.print("Roll No    : ");
            int rollNo = readInt(sc, "Please enter a valid roll number.");

            System.out.print("Is Honours student? (yes/no): ");
            boolean isHonours = sc.nextLine().trim().equalsIgnoreCase("yes");

            double[] marks = null;
            while (marks == null) {
                try {
                    System.out.print("Enter marks for 3 subjects (comma separated, e.g. 78,85,90): ");
                    String[] raw = sc.nextLine().trim().split(",");

                    if (raw.length != 3) {
                        System.out.println("  !! Please enter exactly 3 marks.");
                        continue;
                    }

                    marks = GradeManager.validateAndParse(raw);

                } catch (InvalidMarksException e) {
                    System.out.println("  !! Error: " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("  !! Error: Please enter valid numbers only.");
                }
            }

            if (isHonours) {
                double bonus = readDoubleInRange(sc, "Enter bonus marks (e.g. 5): ", 0, 10);
                students[i] = new HonoursStudent(name, rollNo, marks, bonus);
            } else {
                students[i] = new Student(name, rollNo, marks);
            }
        }

        System.out.println("\n\n========== INDIVIDUAL RESULTS ==========");
        for (Student s : students) {
            s.display();
        }

        System.out.println("\n" + GradeManager.generateReport(students));

        if (n > 0) {
            Student topper = GradeManager.findTopper(students);
            System.out.println("Topper : " + topper.getName() + " (" + String.format("%.1f", topper.getPercentage()) + "%)");

            int passed = GradeManager.countPassed(students);
            System.out.println("Passed : " + passed + " / " + n);
            System.out.println("Failed : " + (n - passed) + " / " + n);
        } else {
            System.out.println("No students were entered.");
        }

        sc.close();
    }

    private static int readStudentCount(Scanner sc) {
        while (true) {
            System.out.print("How many students do you want to enter? ");
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

    private static double readDoubleInRange(Scanner sc, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(sc.nextLine().trim());
                if (value < min || value > max) {
                    System.out.println("  !! Please enter a value between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("  !! Please enter a valid number.");
            }
        }
    }
}
