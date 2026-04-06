// Station.java
// Represents a single Metro Station

public class Station {
    private int id;
    private String name;
    private String line;

    public Station(int id, String name, String line) {
        this.id = id;
        this.name = name;
        this.line = line;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLine() {
        return line;
    }

    @Override
    public String toString() {
        return name + " (" + line + " Line)";
    }
}
