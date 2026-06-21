# Delhi Metro Navigator

Delhi Metro Navigator is a Java Swing route-planning application that models the metro network as a weighted graph and computes optimal journeys using Dijkstra-based routing strategies. The project was redesigned with a centered, minimalist interface that focuses on four major metro lines (Blue, Yellow, Red, Pink) and supports dynamic JSON data loading, layered architecture, and multiple routing strategies.

## 1. Introduction

Metro navigation is a classic graph problem. Stations can be modeled as nodes, rail links as weighted edges, and interchanges as special transfer edges. This project demonstrates how graph theory, software architecture, and user interface design can be combined in a practical desktop application.

The final system supports multiple routing goals, realistic travel assumptions, and a polished Swing UI that is suitable for final-year project demonstration and screenshots.

## 2. Problem Statement

The earlier version of the project worked functionally, but it suffered from common student-project problems:

- metro data was hardcoded directly inside Java source code,
- the GUI class handled routing, formatting, and event coordination together,
- only one route optimization strategy existed,
- travel modeling relied on fixed magic numbers,
- the UI looked static and offered limited interaction feedback.

This redesigned version solves those issues through architecture refactoring, Strategy pattern usage, external JSON data, and improved UI behavior.

## 3. System Architecture

The project uses a layered architecture with manual dependency injection.

```text
Main
  -> AppLauncher
      -> JsonMetroRepository
      -> MetroService
      -> RouteController
      -> MetroNavigatorView
```

### Responsibilities

- `view/`:
  renders the Swing interface, route cards, status bar, autocomplete, hover effects, and animated route map.
- `controller/`:
  listens to user actions, triggers route calculation, and updates view state.
- `service/`:
  contains business logic, route validation, Dijkstra execution, and result summarization.
- `data/`:
  loads JSON network data and builds the graph dynamically.
- `routing/`:
  contains route strategies for different optimization goals.
- `model/`:
  defines immutable domain objects used across layers.

### Folder Structure

```text
.
|-- Main.java
|-- app/
|-- controller/
|-- data/
|-- model/
|-- resources/
|   `-- data/
|       `-- metro-network.json
|-- routing/
|-- service/
|-- util/
`-- view/
```


### Why This Is Better

- new stations or lines can be added without changing the routing code,
- transfer links are explicit,
- travel assumptions are configurable,
- the data layer is easier to explain in a viva and easier to maintain.

## 5. Algorithm Explanation

### Why Dijkstra's Algorithm

Dijkstra's algorithm is appropriate because the metro network is a weighted graph with non-negative edge costs. The system needs optimal point-to-point route computation, and Dijkstra guarantees the shortest valid path for the chosen cost function.

### Time Complexity

Using a priority queue, the complexity is:

`O(E log V)`

where:

- `V` = number of graph nodes,
- `E` = number of track and interchange edges.

This is efficient for an academic metro-scale network and is significantly more appropriate than unweighted traversal methods.

### Shortest Distance

- minimizes total kilometers,
- suitable when physical distance is the primary objective.

### Minimum Interchange

- assigns a very high cost to interchange edges,
- prioritizes fewer line changes,
- useful for simpler passenger journeys.

### Fastest Route

- minimizes estimated travel time,
- uses distance, station dwell time, and interchange penalty,
- produces the most realistic academic approximation of travel time.


### Current UI Design

- **Hero Panel**: Center-aligned title "Delhi Metro Navigator" with description and instruction text
- **Control Panel**: Centered "PLAN A JOURNEY!" heading, focused station selection, and action buttons
- **Metric Cards**: Trip Snapshot showing stops, distance, fare, and ETA in real-time
- **Route Visualization**: Animated map panel showing the journey with color-coded metro lines


### Implemented Features

- center-aligned hero section for cleaner visual hierarchy,
- button hover effects with cursor feedback,
- autocomplete in station selection fields,
- async loading state using `SwingWorker`,
- animated route drawing with color-coded lines,
- improved text contrast and readability,
- cleaner interchange notes such as `Change at Rajiv Chowk from Yellow Line to Blue Line`,
- cleaner status messages for real-time feedback.

### Metro Lines Supported

- **Blue Line**: Dwarka Sector 21 to Vaishali
- **Yellow Line**: Samaypur Badli to HUDA City Centre
- **Red Line**: Rithala to Shaheed Sthal
- **Pink Line**: Majlis Park to Lajpat Nagar (with interchanges)

### WOW Feature

Autocomplete search allows users to type station names directly instead of scrolling through long lists.

## 9. Design Decisions

### Why Swing

- included in standard Java,
- easy to run in classroom or lab environments,
- supports custom painting for route visualization,
- avoids setup complexity that comes with heavier UI frameworks.

### Why a Graph Model

- metro systems naturally map to nodes and edges,
- interchange behavior can be represented explicitly,
- multiple routing strategies can reuse the same graph.

### Why Not BFS

BFS assumes equal edge cost. This project uses different distances, times, and transfer penalties, so BFS would not guarantee optimal answers.

### Why Not a Database

For this academic scope, JSON is more practical than a database because:

- it is lightweight,
- easy to review and edit,
- simple to version-control,
- and avoids deployment overhead.

## 10. Running the Project

Open PowerShell in the project folder and compile:

```powershell
javac Main.java app\*.java controller\*.java data\*.java model\*.java routing\*.java service\*.java util\*.java view\*.java
```

Run:

```powershell
java Main
```

## 11. Suggested Demonstration Cases

### Same-Line Route

- Source: `Samaypur Badli`
- Destination: `Rajiv Chowk`
- Strategy: `Shortest Distance`

### Cross-Line Route

- Source: `Hauz Khas`
- Destination: `Vaishali`
- Strategy: `Fastest Route`

### Minimum Interchange Example

- Source: `Azadpur`
- Destination: `Lajpat Nagar`
- Strategy: `Minimum Interchange`

### Validation Example

- Source: `Rajiv Chowk`
- Destination: `Rajiv Chowk`
- Expected: validation error

## 12. Limitations

- This is still a curated academic dataset, not the full live DMRC system.
- The fare model is approximate and not an official DMRC fare engine.
- The route map is schematic, not geographically accurate.
- Real-time service disruptions and congestion are not included.
- Accessibility-aware routing and schedule planning are not yet supported.

## 13. Future Scope

- integrate real-time data when available,
- support full Delhi Metro line coverage,
- compare all routing strategies side by side,
- export route summaries as PDF or image,
- add accessibility-aware and schedule-aware planning.

## 14. Academic Value

This project demonstrates:

- graph modeling,
- shortest-path optimization,
- layered software architecture,
- Strategy pattern,
- JSON-based configuration,
- and desktop UI engineering.

