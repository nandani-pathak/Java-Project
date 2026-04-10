// Graph.java
// Builds the Delhi Metro Network as a Graph (Adjacency List)
// Implements Dijkstra's Algorithm for shortest path

import java.util.*;

public class Graph {

    private static final int STATION_HOP = 2;
    private static final int INTERCHANGE_HOP = 4;

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
        addEdge(0,1,STATION_HOP); addEdge(1,2,STATION_HOP); addEdge(2,3,STATION_HOP); addEdge(3,4,STATION_HOP);
        addEdge(4,5,STATION_HOP); addEdge(5,6,STATION_HOP); addEdge(6,7,STATION_HOP); addEdge(7,8,STATION_HOP);
        addEdge(8,9,STATION_HOP); addEdge(9,10,STATION_HOP); addEdge(10,11,STATION_HOP); addEdge(11,12,STATION_HOP);
        addEdge(12,13,STATION_HOP); addEdge(13,14,STATION_HOP); addEdge(14,15,STATION_HOP); addEdge(15,16,STATION_HOP);
        addEdge(16,17,STATION_HOP); addEdge(17,18,STATION_HOP); addEdge(18,19,STATION_HOP); addEdge(19,20,STATION_HOP);
        addEdge(20,21,STATION_HOP); addEdge(21,22,STATION_HOP); addEdge(22,23,STATION_HOP); addEdge(23,24,STATION_HOP);
        addEdge(24,25,STATION_HOP); addEdge(25,26,STATION_HOP); addEdge(26,27,STATION_HOP); addEdge(27,28,STATION_HOP);
        addEdge(28,29,STATION_HOP); addEdge(29,30,STATION_HOP); addEdge(30,31,STATION_HOP); addEdge(31,32,STATION_HOP);
        addEdge(32,33,STATION_HOP); addEdge(33,34,STATION_HOP); addEdge(34,35,STATION_HOP); addEdge(35,36,STATION_HOP);

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
        addEdge(37,38,STATION_HOP); addEdge(38,39,STATION_HOP); addEdge(39,40,STATION_HOP); addEdge(40,41,STATION_HOP);
        addEdge(41,42,STATION_HOP); addEdge(42,43,STATION_HOP); addEdge(43,44,STATION_HOP); addEdge(44,45,STATION_HOP);
        addEdge(45,46,STATION_HOP); addEdge(46,47,STATION_HOP); addEdge(47,48,STATION_HOP); addEdge(48,49,STATION_HOP);
        addEdge(49,50,STATION_HOP); addEdge(50,51,STATION_HOP); addEdge(51,52,STATION_HOP); addEdge(52,53,STATION_HOP);
        addEdge(53,54,STATION_HOP); addEdge(54,55,STATION_HOP); addEdge(55,56,STATION_HOP); addEdge(56,57,STATION_HOP);
        addEdge(57,58,STATION_HOP); addEdge(58,59,STATION_HOP); addEdge(59,60,STATION_HOP); addEdge(60,61,STATION_HOP);
        addEdge(61,62,STATION_HOP); addEdge(62,63,STATION_HOP); addEdge(63,64,STATION_HOP); addEdge(64,65,STATION_HOP);
        addEdge(65,66,STATION_HOP); addEdge(66,67,STATION_HOP); addEdge(67,68,STATION_HOP); addEdge(68,69,STATION_HOP);
        addEdge(69,70,STATION_HOP); addEdge(70,71,STATION_HOP); addEdge(71,72,STATION_HOP); addEdge(72,73,STATION_HOP);
        addEdge(73,74,STATION_HOP);

        // Interchange: Rajiv Chowk Yellow <-> Rajiv Chowk Blue
        addEdge(15, 65, INTERCHANGE_HOP);

        // Interchange: Kashmere Gate Yellow <-> Blue (connecting point)
        addEdge(11, 64, INTERCHANGE_HOP + 2);

        // --- PINK LINE (Majlis Park to Lajpat Nagar segment) ---
        addStation(75, "Majlis Park", "Pink");
        addStation(76, "Azadpur (Pink)", "Pink");
        addStation(77, "Netaji Subhash Place", "Pink");
        addStation(78, "Rajouri Garden (Pink)", "Pink");
        addStation(79, "Maya Puri", "Pink");
        addStation(80, "Durgabai Deshmukh South Campus", "Pink");
        addStation(81, "Lajpat Nagar", "Pink");

        addEdge(75, 76, STATION_HOP); addEdge(76, 77, STATION_HOP); addEdge(77, 78, 3);
        addEdge(78, 79, STATION_HOP); addEdge(79, 80, STATION_HOP); addEdge(80, 81, 3);

        // Pink line interchanges
        addEdge(5, 76, INTERCHANGE_HOP);   // Azadpur Yellow <-> Azadpur Pink
        addEdge(55, 78, INTERCHANGE_HOP);  // Rajouri Garden Blue <-> Rajouri Garden Pink

        // --- GREEN LINE (Kirti Nagar to Brigadier Hoshiar Singh segment) ---
        addStation(82, "Satguru Ram Singh Marg", "Green");
        addStation(83, "Ashok Park Main", "Green");
        addStation(84, "Punjabi Bagh", "Green");
        addStation(85, "Peeragarhi", "Green");
        addStation(86, "Udyog Nagar", "Green");
        addStation(87, "Maharaja Surajmal Stadium", "Green");
        addStation(88, "Brigadier Hoshiar Singh", "Green");

        addEdge(58, 82, STATION_HOP); addEdge(82, 83, STATION_HOP); addEdge(83, 84, STATION_HOP);
        addEdge(84, 85, STATION_HOP); addEdge(85, 86, STATION_HOP); addEdge(86, 87, STATION_HOP);
        addEdge(87, 88, STATION_HOP);

        // --- RED LINE (Rithala to Shaheed Sthal) ---
        addStation(89,  "Rithala",                 "Red");
        addStation(90,  "Rohini West",             "Red");
        addStation(91,  "Rohini East",             "Red");
        addStation(92,  "Pitampura",               "Red");
        addStation(93,  "Kohat Enclave",           "Red");
        addStation(94,  "Netaji Subhash Place (Red)", "Red");
        addStation(95,  "Keshav Puram",            "Red");
        addStation(96,  "Kanhaiya Nagar",          "Red");
        addStation(97,  "Inderlok",                "Red");
        addStation(98,  "Shastri Nagar",           "Red");
        addStation(99,  "Pratap Nagar",            "Red");
        addStation(100, "Pulbangash",              "Red");
        addStation(101, "Tis Hazari",              "Red");
        addStation(102, "Kashmere Gate (Red)",     "Red");
        addStation(103, "Shastri Park",            "Red");
        addStation(104, "Seelampur",               "Red");
        addStation(105, "Welcome",                 "Red");
        addStation(106, "Shahdara",                "Red");
        addStation(107, "Mansarovar Park",         "Red");
        addStation(108, "Jhilmil",                 "Red");
        addStation(109, "Dilshad Garden",          "Red");
        addStation(110, "Shaheed Nagar",           "Red");
        addStation(111, "Raj Bagh",                "Red");
        addStation(112, "Rajendra Nagar",          "Red");
        addStation(113, "Shyam Park",              "Red");
        addStation(114, "Mohan Nagar",             "Red");
        addStation(115, "Arthala",                 "Red");
        addStation(116, "Hindon River",            "Red");
        addStation(117, "Shaheed Sthal",           "Red");

        addEdge(89, 90, STATION_HOP);   addEdge(90, 91, STATION_HOP);   addEdge(91, 92, STATION_HOP);   addEdge(92, 93, STATION_HOP);
        addEdge(93, 94, STATION_HOP);   addEdge(94, 95, STATION_HOP);   addEdge(95, 96, STATION_HOP);   addEdge(96, 97, STATION_HOP);
        addEdge(97, 98, STATION_HOP);   addEdge(98, 99, STATION_HOP);   addEdge(99, 100, STATION_HOP);  addEdge(100, 101, STATION_HOP);
        addEdge(101, 102, STATION_HOP); addEdge(102, 103, STATION_HOP); addEdge(103, 104, STATION_HOP); addEdge(104, 105, STATION_HOP);
        addEdge(105, 106, STATION_HOP); addEdge(106, 107, STATION_HOP); addEdge(107, 108, STATION_HOP); addEdge(108, 109, STATION_HOP);
        addEdge(109, 110, STATION_HOP); addEdge(110, 111, STATION_HOP); addEdge(111, 112, STATION_HOP); addEdge(112, 113, STATION_HOP);
        addEdge(113, 114, STATION_HOP); addEdge(114, 115, STATION_HOP); addEdge(115, 116, STATION_HOP); addEdge(116, 117, STATION_HOP);

        // Red line interchanges
        addEdge(77, 94, INTERCHANGE_HOP);   // Netaji Subhash Place Pink <-> Red
        addEdge(11, 102, INTERCHANGE_HOP);  // Kashmere Gate Yellow <-> Red

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
        if (stops <= 15) return 40;
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
