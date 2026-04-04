// ========================================
// Student.java — Base Class (Unit II: Classes, Inheritance, Encapsulation)
// ========================================

public class Student {

    // Encapsulation: private fields
    private String name;
    private int rollNo;
    private double[] marks;  // Array of marks (Unit II: Arrays)

    // Constructor
    public Student(String name, int rollNo, double[] marks) {
        this.name = name;
        this.rollNo = rollNo;
        this.marks = marks;
    }

    // Getters
    public String getName()    { return name; }
    public int getRollNo()     { return rollNo; }
    public double[] getMarks() { return marks; }

    // Calculate total marks
    public double getTotal() {
        double total = 0;
        for (double m : marks) total += m;
        return total;
    }

    // Calculate percentage
    public double getPercentage() {
        return getTotal() / marks.length;
    }

    // Grade logic — can be overridden by subclasses (Inheritance)
    public String getGrade() {
        double pct = getPercentage();
        if (pct >= 90) return "A+";
        else if (pct >= 80) return "A";
        else if (pct >= 70) return "B";
        else if (pct >= 60) return "C";
        else if (pct >= 50) return "D";
        else return "F";
    }

    // Display student info
    public void display() {
        System.out.println("------------------------------");
        System.out.println("Name       : " + name);
        System.out.println("Roll No    : " + rollNo);
        System.out.printf ("Total      : %.2f%n", getTotal());
        System.out.printf ("Percentage : %.2f%%%n", getPercentage());
        System.out.println("Grade      : " + getGrade());
    }
}
