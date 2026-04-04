// ========================================
// Main.java - Entry Point
// (Menu-driven college student database system)
// ========================================

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Student> students = new ArrayList<>();
        boolean running = true;

        printHeader();

        while (running) {
            printMenu();
            int choice = readInt(sc, "Please enter a valid menu choice.");

            switch (choice) {
                case 1:
                    addStudentRecord(sc, students);
                    break;
                case 2:
                    showAllRecords(students);
                    break;
                case 3:
                    searchByRollNo(sc, students);
                    break;
                case 4:
                    showDepartmentRecords(sc, students);
                    break;
                case 5:
                    showDepartmentToppers(students);
                    break;
                case 6:
                    showUniversityTopper(students);
                    break;
                case 7:
                    showAcademicSummary(students);
                    break;
                case 8:
                    running = false;
                    System.out.println("\nThank you for using the College Student Database System.");
                    break;
                default:
                    System.out.println("  !! Please choose a menu option from 1 to 8.");
            }
        }

        sc.close();
    }

    private static void printHeader() {
        System.out.println("==============================================================");
        System.out.println("               COLLEGE STUDENT DATABASE SYSTEM");
        System.out.println("            BTECH ACADEMIC RECORD MANAGEMENT PORTAL");
        System.out.println("==============================================================");
        System.out.println("Available Departments: CSE, IT, AI/ML, ECE, MECH, CIVIL, OTHER");
        System.out.println("Co-curricular Credits: Sports, Hackathons, Event Leadership, Culture");
        System.out.println("Profile Fields: Semester, Section, Email, Phone, Admission Year\n");
    }

    private static void printMenu() {
        System.out.println("\n==================== MAIN MENU ====================");
        System.out.println("1. Add Student Record");
        System.out.println("2. View All Student Records");
        System.out.println("3. Search Student By Roll Number");
        System.out.println("4. View Department Records");
        System.out.println("5. Show Department Toppers");
        System.out.println("6. Show University Rank 1");
        System.out.println("7. Show Academic Summary Dashboard");
        System.out.println("8. Exit");
        System.out.print("Enter choice : ");
    }

    private static void addStudentRecord(Scanner sc, List<Student> students) {
        System.out.println("\n========== ADD STUDENT RECORD ==========");

        System.out.print("Name           : ");
        String name = readNonEmpty(sc, "Name cannot be empty.");

        System.out.print("Roll No        : ");
        int rollNo = readUniqueRollNo(sc, students);

        System.out.print("Semester       : ");
        int semester = readPositiveIntInRange(sc, 1, 8, "Semester must be between 1 and 8.");

        System.out.print("Section        : ");
        String section = readNonEmpty(sc, "Section cannot be empty.").toUpperCase();

        System.out.print("Email          : ");
        String email = readEmail(sc);

        System.out.print("Phone          : ");
        String phone = readPhone(sc);

        System.out.print("Admission Year : ");
        int admissionYear = readPositiveIntInRange(sc, 2000, 2100, "Please enter a valid admission year.");

        String branch = readBranch(sc);
        String[] subjects = GradeManager.getSubjectsForBranch(branch);
        double[] marks = new double[subjects.length];

        System.out.println("Enter marks for " + branch + " subjects:");
        for (int j = 0; j < subjects.length; j++) {
            marks[j] = readValidatedMark(sc, subjects[j]);
        }

        Student student;
        if (readYesNo(sc, "Did the student earn co-curricular activity credit? (yes/no): ")) {
            String activityCategory = readActivityCategory(sc);
            double bonusMarks = ActivityStudent.getBonusMarksForCategory(activityCategory);
            student = new ActivityStudent(name, rollNo, semester, section, email, phone, admissionYear,
                    branch, subjects, marks, activityCategory, bonusMarks);
        } else {
            student = new Student(name, rollNo, semester, section, email, phone, admissionYear,
                    branch, subjects, marks);
        }

        students.add(student);
        System.out.println("\nStudent record added successfully for Roll No " + rollNo + ".");
    }

    private static void showAllRecords(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("\nNo student records found.");
            return;
        }

        System.out.println("\n========== STUDENT RECORDS ==========");
        for (Student student : students) {
            student.display();
        }
        System.out.println(GradeManager.generateReport(students));
    }

    private static void searchByRollNo(Scanner sc, List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("\nNo student records found.");
            return;
        }

        System.out.print("\nEnter Roll No to search: ");
        int rollNo = readInt(sc, "Please enter a valid roll number.");
        Student student = GradeManager.findByRollNo(students, rollNo);

        if (student == null) {
            System.out.println("No student found with Roll No " + rollNo + ".");
            return;
        }

        System.out.println("\n========== STUDENT SEARCH RESULT ==========");
        student.display();
    }

    private static void showDepartmentRecords(Scanner sc, List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("\nNo student records found.");
            return;
        }

        String branch = readBranch(sc);
        List<Student> branchStudents = GradeManager.filterByBranch(students, branch);
        if (branchStudents.isEmpty()) {
            System.out.println("\nNo records found for " + branch + " department.");
            return;
        }

        System.out.println("\n========== " + branch + " DEPARTMENT RECORDS ==========");
        for (Student student : branchStudents) {
            student.display();
        }
        System.out.println(GradeManager.generateReport(branchStudents));
    }

    private static void showDepartmentToppers(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("\nNo student records found.");
            return;
        }

        System.out.println("\n========== DEPARTMENT TOPPERS ==========");
        for (String branch : GradeManager.getAvailableBranches()) {
            Student topper = GradeManager.findTopperByBranch(students, branch);
            if (topper != null) {
                System.out.println(branch + " Topper : " + topper.getName()
                        + " | Roll No: " + topper.getRollNo()
                        + " | Semester: " + topper.getSemester()
                        + " | Percentage: " + String.format("%.1f", topper.getPercentage()) + "%");
            }
        }
    }

    private static void showUniversityTopper(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("\nNo student records found.");
            return;
        }

        Student topper = GradeManager.findTopper(students);
        System.out.println("\n========== UNIVERSITY RANK 1 ==========");
        topper.display();
    }

    private static void showAcademicSummary(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("\nNo student records found.");
            return;
        }

        System.out.println("\n========== ACADEMIC SUMMARY DASHBOARD ==========");
        System.out.println("Total Students      : " + students.size());
        System.out.println("Passed              : " + GradeManager.countPassed(students));
        System.out.println("Failed              : " + (students.size() - GradeManager.countPassed(students)));
        System.out.println("Average Percentage  : " + String.format("%.2f", GradeManager.calculateAveragePercentage(students)) + "%");
        System.out.println("Activity Credits    : " + GradeManager.countActivityCreditStudents(students));
        System.out.println();
        showDepartmentToppers(students);
        System.out.println();
        System.out.println(GradeManager.generateDepartmentStatistics(students));
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

    private static int readPositiveIntInRange(Scanner sc, int min, int max, String errorMessage) {
        while (true) {
            int value = readInt(sc, errorMessage);
            if (value < min || value > max) {
                System.out.println("  !! " + errorMessage);
                System.out.print("Try again   : ");
                continue;
            }
            return value;
        }
    }

    private static int readUniqueRollNo(Scanner sc, List<Student> students) {
        while (true) {
            int rollNo = readInt(sc, "Please enter a valid roll number.");
            if (GradeManager.findByRollNo(students, rollNo) != null) {
                System.out.println("  !! Roll number already exists.");
                System.out.print("Try again   : ");
                continue;
            }
            return rollNo;
        }
    }

    private static String readNonEmpty(Scanner sc, String errorMessage) {
        while (true) {
            String value = sc.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("  !! " + errorMessage);
            System.out.print("Try again   : ");
        }
    }

    private static String readEmail(Scanner sc) {
        while (true) {
            String email = readNonEmpty(sc, "Email cannot be empty.");
            if (email.contains("@") && email.contains(".")) {
                return email;
            }
            System.out.println("  !! Please enter a valid email address.");
            System.out.print("Try again   : ");
        }
    }

    private static String readPhone(Scanner sc) {
        while (true) {
            String phone = readNonEmpty(sc, "Phone cannot be empty.");
            if (phone.matches("\\d{10}")) {
                return phone;
            }
            System.out.println("  !! Please enter a 10-digit phone number.");
            System.out.print("Try again   : ");
        }
    }

    private static String readBranch(Scanner sc) {
        while (true) {
            System.out.println("\nSelect Department:");
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
            System.out.println("3. Good Projects");
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
                    return "Good Projects";
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
