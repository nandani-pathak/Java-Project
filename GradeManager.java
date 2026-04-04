// ========================================
// GradeManager.java - Core Logic
// (Branch-specific subjects, reports, and analytics)
// ========================================

import java.util.ArrayList;
import java.util.List;

public class GradeManager {

    private static final String[] CSE_SUBJECTS = { "Data Structure & Algorithm", "Programming in Java", "Theory of Computation", "Database Management System" };
    private static final String[] IT_SUBJECTS = { "Object Oriented CPP", "Computational Methods", "Discrete Mathematics", "Programming in C" };
    private static final String[] AIML_SUBJECTS = { "Python", "Probability & Statistics", "Artificial Intelligence", "Machine Learning" };
    private static final String[] ECE_SUBJECTS = { "Digital Electronics", "Signals and Systems", "Network Theory", "Microprocessors" };
    private static final String[] MECH_SUBJECTS = { "Thermodynamics", "Fluid Mechanics", "Machine Design", "Manufacturing" };
    private static final String[] CIVIL_SUBJECTS = { "Structural Analysis", "Surveying", "Geotechnical Engineering", "Transportation Engineering" };
    private static final String[] OTHER_SUBJECTS = { "Engineering Mathematics", "Communication Skills", "Core Subject 1", "Core Subject 2" };
    private static final String[] BRANCHES = { "CSE", "IT", "AI/ML", "ECE", "MECH", "CIVIL", "OTHER" };

    public static void validateMark(double value) throws InvalidMarksException {
        if (value < 0 || value > 100) {
            throw new InvalidMarksException(value);
        }
    }

    public static String[] getSubjectsForBranch(String branch) {
        switch (branch) {
            case "CSE":
                return CSE_SUBJECTS.clone();
            case "IT":
                return IT_SUBJECTS.clone();
            case "AI/ML":
                return AIML_SUBJECTS.clone();
            case "ECE":
                return ECE_SUBJECTS.clone();
            case "MECH":
                return MECH_SUBJECTS.clone();
            case "CIVIL":
                return CIVIL_SUBJECTS.clone();
            default:
                return OTHER_SUBJECTS.clone();
        }
    }

    public static String[] getAvailableBranches() {
        return BRANCHES.clone();
    }

    public static String generateReport(List<Student> students) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== CENTRAL STUDENT DATABASE REPORT ==========\n");
        sb.append(String.format("%-10s %-18s %-10s %-9s %-8s %-20s %-8s%n",
                "Roll No", "Name", "Branch", "Sem/Sec", "Percent", "Grade", "Bonus"));
        sb.append("--------------------------------------------------------------------------------------\n");

        for (Student s : students) {
            sb.append(String.format("%-10d %-18s %-10s %-9s %-8.1f %-20s %-8.1f%n",
                    s.getRollNo(),
                    truncate(s.getName(), 18),
                    s.getBranch(),
                    s.getSemester() + "/" + s.getSection(),
                    s.getPercentage(),
                    truncate(s.getGrade(), 20),
                    s.getBonusMarks()));
        }

        sb.append("--------------------------------------------------------------------------------------\n");
        sb.append("Total Students: ").append(students.size());
        return sb.toString();
    }

    public static Student findTopper(List<Student> students) {
        if (students.isEmpty()) {
            return null;
        }

        Student topper = students.get(0);
        for (int i = 1; i < students.size(); i++) {
            if (students.get(i).getPercentage() > topper.getPercentage()) {
                topper = students.get(i);
            }
        }
        return topper;
    }

    public static Student findTopperByBranch(List<Student> students, String branch) {
        Student topper = null;
        for (Student student : students) {
            if (!student.getBranch().equals(branch)) {
                continue;
            }
            if (topper == null || student.getPercentage() > topper.getPercentage()) {
                topper = student;
            }
        }
        return topper;
    }

    public static int countPassed(List<Student> students) {
        int count = 0;
        for (Student s : students) {
            if (s.getPercentage() >= 50) {
                count++;
            }
        }
        return count;
    }

    public static Student findByRollNo(List<Student> students, int rollNo) {
        for (Student student : students) {
            if (student.getRollNo() == rollNo) {
                return student;
            }
        }
        return null;
    }

    public static List<Student> filterByBranch(List<Student> students, String branch) {
        List<Student> filtered = new ArrayList<>();
        for (Student student : students) {
            if (student.getBranch().equals(branch)) {
                filtered.add(student);
            }
        }
        return filtered;
    }

    public static double calculateAveragePercentage(List<Student> students) {
        if (students.isEmpty()) {
            return 0.0;
        }
        double total = 0;
        for (Student student : students) {
            total += student.getPercentage();
        }
        return total / students.size();
    }

    public static int countActivityCreditStudents(List<Student> students) {
        int count = 0;
        for (Student student : students) {
            if (student.getBonusMarks() > 0) {
                count++;
            }
        }
        return count;
    }

    public static String generateDepartmentStatistics(List<Student> students) {
        StringBuilder sb = new StringBuilder();
        sb.append("========== DEPARTMENT STATISTICS ==========\n");
        for (String branch : BRANCHES) {
            List<Student> branchStudents = filterByBranch(students, branch);
            if (branchStudents.isEmpty()) {
                continue;
            }
            sb.append(branch)
                    .append(" -> Students: ").append(branchStudents.size())
                    .append(", Avg %: ").append(String.format("%.2f", calculateAveragePercentage(branchStudents)))
                    .append(", Passed: ").append(countPassed(branchStudents))
                    .append(", Activity Credits: ").append(countActivityCreditStudents(branchStudents))
                    .append("\n");
        }
        return sb.toString();
    }

    private static String truncate(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength - 3) + "...";
    }
}
