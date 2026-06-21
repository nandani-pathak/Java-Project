package model;

public class MetroEdge {
    private final double distanceKm;
    private final double travelMinutes;
    private final boolean interchange;

    public MetroEdge(double distanceKm, double travelMinutes, boolean interchange) {
        this.distanceKm = distanceKm;
        this.travelMinutes = travelMinutes;
        this.interchange = interchange;
    }

    public double distanceKm() {
        return distanceKm;
    }

    public double travelMinutes() {
        return travelMinutes;
    }

    public boolean interchange() {
        return interchange;
    }
}
