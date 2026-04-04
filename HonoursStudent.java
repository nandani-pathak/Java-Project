// ========================================
// HonoursStudent.java — Subclass (Unit II: Inheritance, Method Overriding)
// ========================================

public class HonoursStudent extends Student {

    private double bonusMarks; // Extra credit for honours students

    public HonoursStudent(String name, int rollNo, double[] marks, double bonusMarks) {
        super(name, rollNo, "OTHER", new String[] { "Subject 1", "Subject 2", "Subject 3", "Subject 4" }, marks);
        this.bonusMarks = bonusMarks;
    }

    // Override getPercentage to include bonus
    @Override
    public double getPercentage() {
        return super.getPercentage() + bonusMarks;
    }

    // Override getGrade to show honours tag
    @Override
    public String getGrade() {
        String base = super.getGrade();
        return base + " (Honours)";
    }

    // Override display to show bonus info
    @Override
    public void display() {
        super.display();
        System.out.println("Bonus Marks: " + bonusMarks);
        System.out.println("Type       : Honours Student");
    }
}
