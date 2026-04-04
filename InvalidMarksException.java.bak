// ========================================
// InvalidMarksException.java — Custom Exception (Unit II: User Defined Exceptions)
// ========================================

public class InvalidMarksException extends Exception {

    private double invalidValue;

    public InvalidMarksException(double value) {
        super("Invalid marks entered: " + value + ". Marks must be between 0 and 100.");
        this.invalidValue = value;
    }

    public double getInvalidValue() {
        return invalidValue;
    }
}
