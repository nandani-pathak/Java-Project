// ========================================
// ActivityStudent.java - Co-curricular credit model
// ========================================

public class ActivityStudent extends Student {

    private final String activityCategory;
    private final double bonusMarks;

    public ActivityStudent(String name, int rollNo, int semester, String section,
            int admissionYear, String branch, String[] subjects,
            double[] marks, String activityCategory, double bonusMarks) {
        super(name, rollNo, semester, section, admissionYear, branch, subjects, marks);
        this.activityCategory = activityCategory;
        this.bonusMarks = bonusMarks;
    }

    public static double getBonusMarksForCategory(String category) {
        switch (category) {
            case "Sports Participation":
                return 2.0;
            case "Sports Achievement":
                return 10.0;
            case "Good Projects":
                return 2.0;
            case "Hackathon Winner":
                return 10.0;
            case "Event Organizer":
                return 5.0;
            case "Cultural Activity":
                return 5.0;
            default:
                return 0.0;
        }
    }

    @Override
    public double getTotal() {
        return super.getTotal() + bonusMarks;
    }

    @Override
    public double getPercentage() {
        return Math.min(100.0, getTotal() / getSubjects().length);
    }

    @Override
    public String getActivityInfo() {
        return activityCategory;
    }

    @Override
    public double getBonusMarks() {
        return bonusMarks;
    }

    @Override
    public String getGrade() {
        return super.getGrade() + " (Activity Credit)";
    }
}
