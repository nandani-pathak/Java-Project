// Graph.java
// Builds the Delhi Metro Network as a Graph (Adjacency List)
// Implements Dijkstra's Algorithm for shortest path

import java.util.*;

public class Graph {

    public static class RouteDetails {
        private final String sourceName;
        private final String destinationName;
        private final List<Integer> path;
        private final int stops;
        private final int fare;
        private final int estimatedMinutes;
        private final List<String> interchanges;

        public RouteDetails(String sourceName, String destinationName, List<Integer> path,
                            int stops, int fare, int estimatedMinutes, List<String> interchanges) {
            this.sourceName = sourceName;
            this.destinationName = destinationName;
            this.path = path;
            this.stops = stops;
            this.fare = fare;
            this.estimatedMinutes = estimatedMinutes;
            this.interchanges = interchanges;
        }

        public String getSourceName() {
            return sourceName;
        }

        public String getDestinationName() {
            return destinationName;
        }

        public List<Integer> getPath() {
            return path;
        }

        public int getStops() {
            return stops;
        }

        public int getFare() {
            return fare;
        }

        public int getEstimatedMinutes() {
            return estimatedMinutes;
        }

        public List<String> getInterchanges() {
            return interchanges;
        }
    }

    private int totalStations;
    private List<List<int[]>> adjList; // adjList[i] = list of {neighborId, weight}
    private Map<String, Integer> stationIndex; // station name -> id
    private List<Station> stations; // all stations

    public Graph() {
        stationIndex = new HashMap<>();
        stations = new ArrayList<>();
        adjList = new ArrayList<>();
        buildMetroNetwork();
    }

    // Add a station to the graph
    private void addStation(int id, String name, String line) {
        Station s = new Station(id, name, line);
        stations.add(s);
        stationIndex.put(name, id);
        adjList.add(new ArrayList<>());
    }

    // Add a bidirectional edge between two stations with a distance weight
    private void addEdge(int from, int to, int distance) {
        adjList.get(from).add(new int[]{to, distance});
        adjList.get(to).add(new int[]{from, distance});
    }

    // Build the Delhi Metro Network
    private void buildMetroNetwork() {

        // --- YELLOW LINE (Samaypur Badli to HUDA City Centre) ---
        addStation(0,  "Samaypur Badli",     "Yellow");
        addStation(1,  "Rohini Sector 18",   "Yellow");
        addStation(2,  "Haiderpur Badli Mor","Yellow");
        addStation(3,  "Jahangirpuri",        "Yellow");
        addStation(4,  "Adarsh Nagar",        "Yellow");
        addStation(5,  "Azadpur",             "Yellow");
        addStation(6,  "Model Town",          "Yellow");
        addStation(7,  "GTB Nagar",           "Yellow");
        addStation(8,  "Vishwa Vidyalaya",    "Yellow");
        addStation(9,  "Vidhan Sabha",        "Yellow");
        addStation(10, "Civil Lines",         "Yellow");
        addStation(11, "Kashmere Gate",       "Yellow");
        addStation(12, "Chandni Chowk",       "Yellow");
        addStation(13, "Chawri Bazar",        "Yellow");
        addStation(14, "New Delhi",           "Yellow");
        addStation(15, "Rajiv Chowk",         "Yellow");
        addStation(16, "Patel Chowk",         "Yellow");
        addStation(17, "Central Secretariat", "Yellow");
        addStation(18, "Udyog Bhawan",        "Yellow");
        addStation(19, "Lok Kalyan Marg",     "Yellow");
        addStation(20, "Jor Bagh",            "Yellow");
        addStation(21, "INA",                 "Yellow");
        addStation(22, "AIIMS",               "Yellow");
        addStation(23, "Green Park",          "Yellow");
        addStation(24, "Hauz Khas",           "Yellow");
        addStation(25, "Malviya Nagar",       "Yellow");
        addStation(26, "Saket",               "Yellow");
        addStation(27, "Qutab Minar",         "Yellow");
        addStation(28, "Chhatarpur",          "Yellow");
        addStation(29, "Sultanpur",           "Yellow");
        addStation(30, "Ghitorni",            "Yellow");
        addStation(31, "Arjan Garh",          "Yellow");
        addStation(32, "Guru Dronacharya",    "Yellow");
        addStation(33, "Sikanderpur",         "Yellow");
        addStation(34, "MG Road",             "Yellow");
        addStation(35, "IFFCO Chowk",         "Yellow");
        addStation(36, "HUDA City Centre",    "Yellow");

        // Yellow Line edges
        addEdge(0,1,2); addEdge(1,2,2); addEdge(2,3,2); addEdge(3,4,2);
        addEdge(4,5,2); addEdge(5,6,2); addEdge(6,7,2); addEdge(7,8,2);
        addEdge(8,9,2); addEdge(9,10,2); addEdge(10,11,2); addEdge(11,12,2);
        addEdge(12,13,2); addEdge(13,14,2); addEdge(14,15,2); addEdge(15,16,2);
        addEdge(16,17,2); addEdge(17,18,2); addEdge(18,19,2); addEdge(19,20,2);
        addEdge(20,21,2); addEdge(21,22,2); addEdge(22,23,2); addEdge(23,24,2);
        addEdge(24,25,2); addEdge(25,26,2); addEdge(26,27,2); addEdge(27,28,2);
        addEdge(28,29,2); addEdge(29,30,2); addEdge(30,31,2); addEdge(31,32,2);
        addEdge(32,33,2); addEdge(33,34,2); addEdge(34,35,2); addEdge(35,36,2);

        // --- BLUE LINE (Dwarka Sector 21 to Vaishali/Noida) ---
        addStation(37, "Dwarka Sector 21",   "Blue");
        addStation(38, "Dwarka Sector 8",    "Blue");
        addStation(39, "Dwarka Sector 9",    "Blue");
        addStation(40, "Dwarka Sector 10",   "Blue");
        addStation(41, "Dwarka Sector 11",   "Blue");
        addStation(42, "Dwarka Sector 12",   "Blue");
        addStation(43, "Dwarka Sector 13",   "Blue");
        addStation(44, "Dwarka Sector 14",   "Blue");
        addStation(45, "Dwarka",             "Blue");
        addStation(46, "Dwarka Mor",         "Blue");
        addStation(47, "Nawada",             "Blue");
        addStation(48, "Uttam Nagar West",   "Blue");
        addStation(49, "Uttam Nagar East",   "Blue");
        addStation(50, "Janakpuri West",     "Blue");
        addStation(51, "Janakpuri East",     "Blue");
        addStation(52, "Tilak Nagar",        "Blue");
        addStation(53, "Subhash Nagar",      "Blue");
        addStation(54, "Tagore Garden",      "Blue");
        addStation(55, "Rajouri Garden",     "Blue");
        addStation(56, "Ramesh Nagar",       "Blue");
        addStation(57, "Moti Nagar",         "Blue");
        addStation(58, "Kirti Nagar",        "Blue");
        addStation(59, "Shadipur",           "Blue");
        addStation(60, "Patel Nagar",        "Blue");
        addStation(61, "Rajendra Place",     "Blue");
        addStation(62, "Karol Bagh",         "Blue");
        addStation(63, "Jhandewalan",        "Blue");
        addStation(64, "Ramakrishna Ashram", "Blue");
        addStation(65, "Rajiv Chowk (Blue)", "Blue");
        addStation(66, "Barakhamba Road",    "Blue");
        addStation(67, "Mandi House",        "Blue");
        addStation(68, "Yamuna Bank",        "Blue");
        addStation(69, "Laxmi Nagar",        "Blue");
        addStation(70, "Nirman Vihar",       "Blue");
        addStation(71, "Preet Vihar",        "Blue");
        addStation(72, "Karkarduma",         "Blue");
        addStation(73, "Anand Vihar",        "Blue");
        addStation(74, "Vaishali",           "Blue");

        // Blue Line edges
        addEdge(37,38,2); addEdge(38,39,2); addEdge(39,40,2); addEdge(40,41,2);
        addEdge(41,42,2); addEdge(42,43,2); addEdge(43,44,2); addEdge(44,45,2);
        addEdge(45,46,2); addEdge(46,47,2); addEdge(47,48,2); addEdge(48,49,2);
        addEdge(49,50,2); addEdge(50,51,2); addEdge(51,52,2); addEdge(52,53,2);
        addEdge(53,54,2); addEdge(54,55,2); addEdge(55,56,2); addEdge(56,57,2);
        addEdge(57,58,2); addEdge(58,59,2); addEdge(59,60,2); addEdge(60,61,2);
        addEdge(61,62,2); addEdge(62,63,2); addEdge(63,64,2); addEdge(64,65,2);
        addEdge(65,66,2); addEdge(66,67,2); addEdge(67,68,2); addEdge(68,69,2);
        addEdge(69,70,2); addEdge(70,71,2); addEdge(71,72,2); addEdge(72,73,2);
        addEdge(73,74,2);

        // Interchange: Rajiv Chowk Yellow <-> Rajiv Chowk Blue
        addEdge(15, 65, 0);

        // Interchange: Kashmere Gate Yellow <-> Blue (connecting point)
        addEdge(11, 64, 3);

        // --- PINK LINE (Majlis Park to Lajpat Nagar segment) ---
        addStation(75, "Majlis Park", "Pink");
        addStation(76, "Azadpur (Pink)", "Pink");
        addStation(77, "Netaji Subhash Place", "Pink");
        addStation(78, "Rajouri Garden (Pink)", "Pink");
        addStation(79, "Maya Puri", "Pink");
        addStation(80, "Durgabai Deshmukh South Campus", "Pink");
        addStation(81, "Lajpat Nagar", "Pink");

        addEdge(75, 76, 2); addEdge(76, 77, 2); addEdge(77, 78, 3);
        addEdge(78, 79, 2); addEdge(79, 80, 2); addEdge(80, 81, 3);

        // Pink line interchanges
        addEdge(5, 76, 0);   // Azadpur Yellow <-> Azadpur Pink
        addEdge(55, 78, 0);  // Rajouri Garden Blue <-> Rajouri Garden Pink

        // --- GREEN LINE (Kirti Nagar to Brigadier Hoshiar Singh segment) ---
        addStation(82, "Satguru Ram Singh Marg", "Green");
        addStation(83, "Ashok Park Main", "Green");
        addStation(84, "Punjabi Bagh", "Green");
        addStation(85, "Peeragarhi", "Green");
        addStation(86, "Udyog Nagar", "Green");
        addStation(87, "Maharaja Surajmal Stadium", "Green");
        addStation(88, "Brigadier Hoshiar Singh", "Green");

        addEdge(58, 82, 2); addEdge(82, 83, 2); addEdge(83, 84, 2);
        addEdge(84, 85, 2); addEdge(85, 86, 2); addEdge(86, 87, 2);
        addEdge(87, 88, 2);

        totalStations = stations.size();
    }

    // Dijkstra's Algorithm - returns {distance[], previous[]}
    public int[][] dijkstra(int source) {
        int[] dist = new int[totalStations];
        int[] prev = new int[totalStations];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[source] = 0;

        // PriorityQueue: {distance, stationId}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, source});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int currDist = curr[0];
            int currNode = curr[1];

            if (currDist > dist[currNode]) continue;

            for (int[] neighbor : adjList.get(currNode)) {
                int nextNode = neighbor[0];
                int weight   = neighbor[1];
                if (dist[currNode] + weight < dist[nextNode]) {
                    dist[nextNode] = dist[currNode] + weight;
                    prev[nextNode] = currNode;
                    pq.offer(new int[]{dist[nextNode], nextNode});
                }
            }
        }
        return new int[][]{dist, prev};
    }

    // Reconstruct path from source to destination
    public List<Integer> getPath(int[] prev, int destination) {
        List<Integer> path = new ArrayList<>();
        for (int at = destination; at != -1; at = prev[at]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    // Calculate fare based on number of stops
    public int calculateFare(int stops) {
        if (stops <= 2)  return 10;
        if (stops <= 5)  return 20;
        if (stops <= 10) return 30;
        if (stops <= 20) return 40;
        return 50;
    }

    // Get station id by name
    public int getStationId(String name) {
        return stationIndex.getOrDefault(name, -1);
    }

    // Get station name by id
    public String getStationName(int id) {
        return stations.get(id).getName();
    }

    // Get station line by id
    public String getStationLine(int id) {
        return stations.get(id).getLine();
    }

    // Get all station names for dropdown
    public String[] getAllStationNames() {
        String[] names = new String[stations.size()];
        for (int i = 0; i < stations.size(); i++) {
            names[i] = stations.get(i).getName();
        }
        Arrays.sort(names);
        return names;
    }

    public int getTotalStations() {
        return totalStations;
    }

    public String getStationLineByName(String stationName) {
        int stationId = getStationId(stationName);
        if (stationId == -1) {
            return "Unknown";
        }
        return getStationLine(stationId);
    }

    public RouteDetails findRouteDetails(String sourceName, String destName) throws InvalidStationException {
        int sourceId = getStationId(sourceName);
        int destId = getStationId(destName);

        if (sourceId == -1 || destId == -1) {
            throw new InvalidStationException("Station not found in the network.");
        }

        int[][] result = dijkstra(sourceId);
        int[] dist = result[0];
        int[] prev = result[1];

        if (dist[destId] == Integer.MAX_VALUE) {
            throw new InvalidStationException("No route found between selected stations.");
        }

        List<Integer> path = getPath(prev, destId);
        int stops = Math.max(path.size() - 1, 0);
        int fare = calculateFare(stops);
        List<String> interchanges = new ArrayList<>();

        for (int i = 1; i < path.size(); i++) {
            String previousLine = getStationLine(path.get(i - 1));
            String currentLine = getStationLine(path.get(i));
            if (!previousLine.equals(currentLine)) {
                String stationName = getStationName(path.get(i - 1));
                interchanges.add(stationName + ": " + previousLine + " -> " + currentLine);
            }
        }

        int estimatedMinutes = stops * 2 + (interchanges.size() * 4);

        return new RouteDetails(
            sourceName,
            destName,
            path,
            stops,
            fare,
            estimatedMinutes,
            interchanges
        );
    }
}
