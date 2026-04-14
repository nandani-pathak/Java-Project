package model;

public class StationNode {
    private final String nodeId;
    private final String stationName;
    private final String lineName;
    private final String colorHex;

    public StationNode(String nodeId, String stationName, String lineName, String colorHex) {
        this.nodeId = nodeId;
        this.stationName = stationName;
        this.lineName = lineName;
        this.colorHex = colorHex;
    }

    public String nodeId() {
        return nodeId;
    }

    public String stationName() {
        return stationName;
    }

    public String lineName() {
        return lineName;
    }

    public String colorHex() {
        return colorHex;
    }
}
