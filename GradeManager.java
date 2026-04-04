// ========================================
// GradeManager.java — Core Logic
// (Unit II: Wrapper Classes, StringBuffer, throw/throws, Logical Operators)
// ========================================

public class GradeManager {

    // Validate marks using Wrapper class Double (Unit II: Wrapper Classes)
    public static double[] validateAndParse(String[] rawMarks) throws InvalidMarksException {
        double[] marks = new double[rawMarks.length];

        for (int i = 0; i < rawMarks.length; i++) {
            // Wrapper class: String → double conversion
            double val = Double.parseDouble(rawMarks[i]);

            // Logical operator check (Unit II: Logical Operators)
            if (val < 0 || val > 100) {
                throw new InvalidMarksException(val); // throw clause (Unit II)
            }
            marks[i] = val;
        }
        return marks;
    }

    // Generate a summary report using StringBuffer (Unit II: StringBuffer)
    public static String generateReport(Student[] students) {
        StringBuffer sb = new StringBuffer();
        sb.append("========== GRADE REPORT ==========\n");

        for (Student s : students) {
            sb.append("Roll No: ").append(s.getRollNo());
            sb.append(" | Name: ").append(s.getName());
            sb.append(" | Grade: ").append(s.getGrade());
            sb.append(" | %: ").append(String.format("%.1f", s.getPercentage()));
            sb.append("\n");
        }

        sb.append("==================================\n");
        sb.append("Total Students: ").append(students.length);
        return sb.toString();
    }

    // Find topper using Control of Flow (Unit II: Control of Flow)
    public static Student findTopper(Student[] students) {
        Student topper = students[0];
        for (int i = 1; i < students.length; i++) {
            if (students[i].getPercentage() > topper.getPercentage()) {
                topper = students[i];
            }
        }
        return topper;
    }

    // Count students who passed (marks >= 50)
    public static int countPassed(Student[] students) {
        int count = 0;
        for (Student s : students) {
            if (s.getPercentage() >= 50) count++;
        }
        return count;
    }
}
