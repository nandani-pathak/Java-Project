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
