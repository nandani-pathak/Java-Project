// ========================================
// Student.java - BTech Student Model
// ========================================

public class Student {

    // Encapsulation: private fields
    private final String name;
    private final int rollNo;
    private final String branch;
    private final String[] subjects;
    private final double[] marks;

    public Student(String name, int rollNo, String branch, String[] subjects, double[] marks) {
        this.name = name;
        this.rollNo = rollNo;
        this.branch = branch;
        this.subjects = subjects.clone();
        this.marks = marks.clone();
    }

    public String getName()    { return name; }
    public int getRollNo()     { return rollNo; }
    public String getBranch()  { return branch; }
    public String[] getSubjects() { return subjects.clone(); }
    public double[] getMarks() { return marks.clone(); }
    public String getActivityInfo() { return "No activity credit"; }
    public double getBonusMarks() { return 0.0; }

    // Calculate total marks
    public double getTotal() {
        double total = 0;
        for (double m : marks) {
            total += m;
        }
        return total;
    }

    // Calculate percentage
    public double getPercentage() {
        return getTotal() / marks.length;
    }

    // Grade logic - can be overridden by subclasses (Inheritance)
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
