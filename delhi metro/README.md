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

## Chapter 5 Test Cases

### 5.1 Test Case 1 - Same Line Route (Yellow Line)

Source: Samaypur Badli | Destination: Rajiv Chowk

Expected output: 15 stops, Rs 40 fare, no interchange, estimated about 30 minutes. All stations in the route timeline should show Yellow Line.

Screenshot tip: Capture the full application window so the route list, trip snapshot cards, and bottom status bar are visible.

### 5.2 Test Case 2 - Cross-Line Route (Yellow to Blue)

Source: Hauz Khas | Destination: Vaishali

Expected output: Route travels on Yellow Line to Rajiv Chowk, then shifts to Blue Line toward Vaishali. The interchange note at Rajiv Chowk should be clearly visible. Fare should be Rs 50.

Screenshot tip: Make sure the full route line and interchange notes are both visible in the result area.

### 5.3 Test Case 3 - Same Station Error

Source: Rajiv Chowk | Destination: Rajiv Chowk

Expected output: Error message in red stating that source and destination cannot be the same station.

Screenshot tip: Capture the red error text in the result area together with the red status bar message at the bottom.

### 5.4 Test Case 4 - Short Route (Adjacent Stations)

Source: Green Park | Destination: Hauz Khas

Expected output: 1 stop, Rs 10 fare, estimated 2 minutes, and a minimal direct route.

Screenshot tip: Keep the full route line and trip snapshot cards in view.

### 5.5 Test Case 5 - Blue Line End to End

Source: Dwarka Sector 21 | Destination: Vaishali

Expected output: Full Blue Line route, 37 stops, Rs 50 fare, estimated about 74 minutes.

Screenshot tip: Maximize the app window so the long route output and full station timeline remain readable.

## Limitations

- Static network: the metro graph is hardcoded in Java and does not update from a live database.
- Simplified fare structure: fare is estimated from stop count, while real DMRC pricing is more detailed.
- No real-time data: delays, service disruptions, and peak-hour conditions are not considered.
- No visual metro map: the route is shown as a text path and Swing route panel, not as a full geographic network map.
- Limited line coverage: this project currently includes Yellow, Blue, Pink, Green, and Red lines only.
- No accessibility-aware routing: elevator or disabled-friendly routing is not included.
- No first or last train timing data: the app does not show operating schedule information.
