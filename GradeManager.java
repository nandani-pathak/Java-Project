// ========================================
// GradeManager.java - Core Logic
// (Branch-specific subjects and topper logic)
// ========================================

public class GradeManager {

    private static final String[] CSE_SUBJECTS = { "DSA", "Java", "OOPs", "CPP" };
    private static final String[] IT_SUBJECTS = { "DOC", "Computational Methods", "Probability", "DBMS" };
    private static final String[] AIML_SUBJECTS = { "Python", "Probability", "Artificial Intelligence", "Machine Learning" };
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

    public static String generateReport(Student[] students) {
        StringBuilder sb = new StringBuilder();
        sb.append("========== CENTRAL STUDENT DATABASE REPORT ==========\n");
        sb.append(String.format("%-10s %-18s %-10s %-12s %-8s%n",
                "Roll No", "Name", "Branch", "Grade", "Percent"));
        sb.append("----------------------------------------------------\n");

        for (Student s : students) {
            sb.append(String.format("%-10d %-18s %-10s %-12s %-8.1f%n",
                    s.getRollNo(),
                    truncate(s.getName(), 18),
                    s.getBranch(),
                    s.getGrade(),
                    s.getPercentage()));
        }

        sb.append("----------------------------------------------------\n");
        sb.append("Total Students: ").append(students.length);
        return sb.toString();
    }

    private static String truncate(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength - 3) + "...";
    }

    // Find topper using Control of Flow (Unit II: Control of Flow)
    public static Student findTopper(Student[] students) {
        if (students.length == 0) {
            return null;
        }

        Student topper = students[0];
        for (int i = 1; i < students.length; i++) {
            if (students[i].getPercentage() > topper.getPercentage()) {
                topper = students[i];
            }
        }
        return topper;
    }

    public static Student findTopperByBranch(Student[] students, String branch) {
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

    // Count students who passed (marks >= 50)
    public static int countPassed(Student[] students) {
        int count = 0;
        for (Student s : students) {
            if (s.getPercentage() >= 50) {
                count++;
            }
        }
        return count;
    }
}
