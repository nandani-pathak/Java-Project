package model;

import java.util.List;

public record RouteDetails(
    String sourceName,
    String destinationName,
    String strategyLabel,
    List<String> nodePath,
    List<String> stationTrail,
    List<RouteStep> steps,
    List<String> interchanges,
    double totalDistanceKm,
    int totalStops,
    int totalInterchanges,
    int estimatedMinutes,
    int fare
) {
}
