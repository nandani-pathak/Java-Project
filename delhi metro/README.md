# Delhi Metro Navigator

A Java Swing desktop app for finding Delhi Metro routes, fares, estimated travel time, and interchange details.

## Requirements

- Java JDK 8 or newer

## How to Run

Open PowerShell in:

```powershell
D:\Java Project\delhi metro
```

Compile the project:

```powershell
javac Main.java Graph.java MetroGUI.java Station.java InvalidStationException.java
```

Run the app:

```powershell
java Main
```

## Check Java Installation

If the commands fail, verify Java is installed:

```powershell
java -version
javac -version
```

## Project Files

- `Main.java` - starts the app
- `MetroGUI.java` - user interface
- `Graph.java` - metro network and route logic
- `Station.java` - station model
- `InvalidStationException.java` - custom exception handling

## What This App Shows

- Best route between two stations
- Total stops
- Fare
- Estimated travel time
- Interchange count and notes

## Limitations

- Static network: the metro graph is hardcoded in Java and does not update from a live database.
- Simplified fare structure: fare is estimated from stop count, while real DMRC pricing is more detailed.
- No real-time data: delays, service disruptions, and peak-hour conditions are not considered.
- No visual metro map: the route is shown as a text path and Swing route panel, not as a full geographic network map.
- Limited line coverage: this project currently includes Yellow, Blue, Pink, and Green lines only.
- No accessibility-aware routing: elevator or disabled-friendly routing is not included.
- No first or last train timing data: the app does not show operating schedule information.
