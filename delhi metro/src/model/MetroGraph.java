package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MetroGraph {
    public record EdgeRef(String targetNodeId, MetroEdge edge) {
    }

    private final Map<String, StationNode> stationsByNodeId = new LinkedHashMap<>();
    private final Map<String, List<EdgeRef>> adjacency = new HashMap<>();
    private final Map<String, Set<String>> nodesByStationName = new LinkedHashMap<>();
    private Map<String, String> lineColors = new HashMap<>();

    public void registerStationGroup(String stationName) {
        nodesByStationName.computeIfAbsent(stationName, key -> new LinkedHashSet<>());
    }

    public void addStation(StationNode station) {
        stationsByNodeId.put(station.nodeId(), station);
        adjacency.computeIfAbsent(station.nodeId(), key -> new ArrayList<>());
        nodesByStationName.computeIfAbsent(station.stationName(), key -> new LinkedHashSet<>()).add(station.nodeId());
    }

    public void addEdge(String fromNodeId, String toNodeId, MetroEdge edge) {
        adjacency.get(fromNodeId).add(new EdgeRef(toNodeId, edge));
        adjacency.get(toNodeId).add(new EdgeRef(fromNodeId, edge));
    }

    public StationNode getStation(String nodeId) {
        return stationsByNodeId.get(nodeId);
    }

    public List<EdgeRef> getNeighbors(String nodeId) {
        return adjacency.getOrDefault(nodeId, List.of());
    }

    public Set<String> getNodesForStation(String stationName) {
        return nodesByStationName.getOrDefault(stationName, Set.of());
    }

    public List<String> getStationNames() {
        return new ArrayList<>(new TreeSet<>(nodesByStationName.keySet()));
    }

    public int getStationCount() {
        return nodesByStationName.size();
    }

    public Collection<StationNode> getStations() {
        return Collections.unmodifiableCollection(stationsByNodeId.values());
    }

    public void setLineColors(Map<String, String> lineColors) {
        this.lineColors = new HashMap<>(lineColors);
    }

    public Map<String, String> getLineColors() {
        return Collections.unmodifiableMap(lineColors);
    }
}
