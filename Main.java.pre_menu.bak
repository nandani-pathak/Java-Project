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

            if (readYesNo(sc, "Did the student earn co-curricular activity credit? (yes/no): ")) {
                String activityCategory = readActivityCategory(sc);
                double bonusMarks = ActivityStudent.getBonusMarksForCategory(activityCategory);
                students[i] = new ActivityStudent(name, rollNo, branch, subjects, marks, activityCategory, bonusMarks);
            } else {
                students[i] = new Student(name, rollNo, branch, subjects, marks);
            }
        }

        System.out.println("\n\n========== STUDENT RECORDS ==========");
        for (Student s : students) {
            s.display();
        }

        System.out.println("\n" + GradeManager.generateReport(students));

        if (n > 0) {
            System.out.println("\n\n========== DEPARTMENT TOPPERS ==========");
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
        System.out.println("Available Departments: CSE, IT, AI/ML, ECE, MECH, CIVIL, OTHER");
        System.out.println("Co-curricular Credits: Sports, Hackathons, Event Leadership, Culture\n");
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
            System.out.println("\n\nSelect Department:\n");
            System.out.println("1. CSE");
            System.out.println("2. IT");
            System.out.println("3. AI/ML");
            System.out.println("4. ECE");
            System.out.println("5. MECH");
            System.out.println("6. CIVIL");
            System.out.println("7. OTHER\n");

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
                    System.out.println("\nPlease choose a number from 1 to 7.\n");
            }
        }
    }

    private static boolean readYesNo(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("y")) {
                return true;
            }
            if (input.equals("no") || input.equals("n")) {
                return false;
            }
            System.out.println("  !! Please enter yes or no.");
        }
    }

    private static String readActivityCategory(Scanner sc) {
        while (true) {
            System.out.println("Select Activity Credit Category:");
            System.out.println("1. Sports Participation");
            System.out.println("2. Sports Achievement");
            System.out.println("3. Hackathon Participation");
            System.out.println("4. Hackathon Winner");
            System.out.println("5. Event Organizer");
            System.out.println("6. Cultural Activity");
            System.out.print("Enter choice : ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    return "Sports Participation";
                case "2":
                    return "Sports Achievement";
                case "3":
                    return "Hackathon Participation";
                case "4":
                    return "Hackathon Winner";
                case "5":
                    return "Event Organizer";
                case "6":
                    return "Cultural Activity";
                default:
                    System.out.println("  !! Please choose a number from 1 to 6.");
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
