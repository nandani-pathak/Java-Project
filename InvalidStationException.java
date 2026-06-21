// InvalidStationException.java
// Custom Exception for handling invalid station names

public class InvalidStationException extends Exception {
    public InvalidStationException(String message) {
        super(message);
    }
}
