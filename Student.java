// ========================================
// Student.java - BTech Student Model
// ========================================

public class Student {

    private final String name;
    private final int rollNo;
    private final int semester;
    private final String section;
    private final int admissionYear;
    private final String branch;
    private final String[] subjects;
    private final double[] marks;

    public Student(String name, int rollNo, int semester, String section, int admissionYear, String branch, String[] subjects, double[] marks) {
        this.name = name;
        this.rollNo = rollNo;
        this.semester = semester;
        this.section = section;
        this.admissionYear = admissionYear;
        this.branch = branch;
        this.subjects = subjects.clone();
        this.marks = marks.clone();
    }

    public String getName() { return name; }
    public int getRollNo() { return rollNo; }
    public int getSemester() { return semester; }
    public String getSection() { return section; }
    public int getAdmissionYear() { return admissionYear; }
    public String getBranch() { return branch; }
    public String[] getSubjects() { return subjects.clone(); }
    public double[] getMarks() { return marks.clone(); }
    public String getActivityInfo() { return "No activity credit"; }
    public double getBonusMarks() { return 0.0; }

    public double getTotal() {
        double total = 0;
        for (double m : marks) {
            total += m;
        }
        return total;
    }

    public double getPercentage() {
        return getTotal() / marks.length;
    }

    public String getGrade() {
        double pct = getPercentage();
        if (pct >= 90) {
            return "A+";
        } else if (pct >= 80) {
            return "A";
        } else if (pct >= 70) {
            return "B";
        } else if (pct >= 60) {
            return "C";
        } else if (pct >= 50) {
            return "D";
        } else {
            return "F";
        }
    }

    public void display() {
        System.out.println("--------------------------------------------------------------");
        System.out.println("STUDENT PROFILE");
        System.out.println("--------------------------------------------------------------");
        System.out.println("Name       : " + name);
        System.out.println("Roll No    : " + rollNo);
        System.out.println("Semester   : " + semester);
        System.out.println("Section    : " + section);
        System.out.println("Admission  : " + admissionYear);
        System.out.println("Department : " + branch);
        System.out.println("Subject-wise Marks");
        for (int i = 0; i < subjects.length; i++) {
            System.out.printf("%-24s: %.2f%n", subjects[i], marks[i]);
        }
        System.out.println("--------------------------------------------------------------");
        System.out.println("Activity   : " + getActivityInfo());
        System.out.printf("Bonus Marks: %.2f%n", getBonusMarks());
        System.out.printf("Total      : %.2f%n", getTotal());
        System.out.printf("Percentage : %.2f%%%n", getPercentage());
        System.out.println("Grade      : " + getGrade());
    }
}
