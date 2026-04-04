// ========================================
// Main.java — Entry Point
// (Ties together: Classes, Inheritance, Exceptions, Arrays, StringBuffer, Wrapper Classes)
// ========================================

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║        STUDENT GRADE MANAGER     ║");
        System.out.println("║     Unit II — Java Fundamentals  ║");
        System.out.println("╚══════════════════════════════════╝\n");

        System.out.print("How many students do you want to enter? ");
        int n = Integer.parseInt(sc.nextLine().trim()); // Wrapper: String → int

        Student[] students = new Student[n]; // Array of Student objects

        for (int i = 0; i < n; i++) {
            System.out.println("\n--- Student " + (i + 1) + " ---");

            System.out.print("Name       : ");
            String name = sc.nextLine().trim();

            System.out.print("Roll No    : ");
            int rollNo = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Is Honours student? (yes/no): ");
            boolean isHonours = sc.nextLine().trim().equalsIgnoreCase("yes");

            // Input 3 subject marks
            double[] marks = null;
            while (marks == null) {
                try {
                    System.out.print("Enter marks for 3 subjects (comma separated, e.g. 78,85,90): ");
                    String[] raw = sc.nextLine().trim().split(",");

                    if (raw.length != 3) {
                        System.out.println("  !! Please enter exactly 3 marks.");
                        continue;
                    }

                    marks = GradeManager.validateAndParse(raw); // throws clause

                } catch (InvalidMarksException e) {
                    // Catching user-defined exception (Unit II)
                    System.out.println("  !! Error: " + e.getMessage());
                } catch (NumberFormatException e) {
                    // Catching built-in exception (Unit II)
                    System.out.println("  !! Error: Please enter valid numbers only.");
                }
            }

            if (isHonours) {
                System.out.print("Enter bonus marks (e.g. 5): ");
                double bonus = Double.parseDouble(sc.nextLine().trim());
                students[i] = new HonoursStudent(name, rollNo, marks, bonus);
            } else {
                students[i] = new Student(name, rollNo, marks);
            }
        }

        // Display all students
        System.out.println("\n\n========== INDIVIDUAL RESULTS ==========");
        for (Student s : students) {
            s.display(); // Polymorphism: works for both Student and HonoursStudent
        }

        // Summary report using StringBuffer
        System.out.println("\n" + GradeManager.generateReport(students));

        // Topper
        Student topper = GradeManager.findTopper(students);
        System.out.println("🏆 Topper: " + topper.getName() + " (" + String.format("%.1f", topper.getPercentage()) + "%)");

        // Pass count
        int passed = GradeManager.countPassed(students);
        System.out.println("Passed : " + passed + " / " + n);
        System.out.println("Failed : " + (n - passed) + " / " + n);

        sc.close();
    }
}
