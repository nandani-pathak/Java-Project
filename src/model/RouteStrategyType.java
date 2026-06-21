package model;

public enum RouteStrategyType {
    SHORTEST_DISTANCE("Shortest Distance"),
    MINIMUM_INTERCHANGE("Minimum Interchange"),
    FASTEST_ROUTE("Fastest Route");

    private final String label;

    RouteStrategyType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
