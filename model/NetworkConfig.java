package model;

public record NetworkConfig(
    double minutesPerKm,
    double stationDwellMinutes,
    double interchangePenaltyMinutes,
    int baseFare,
    int farePerFiveKm,
    int fareCap
) {
}
