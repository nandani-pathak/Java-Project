package service;

import data.MetroRepository;
import model.MetroEdge;
import model.MetroGraph;
import model.NetworkConfig;
import model.RouteDetails;
import model.RouteQuery;
import model.RouteStep;
import model.RouteStrategyType;
import model.StationNode;
import routing.FastestRouteStrategy;
import routing.MinimumInterchangeStrategy;
import routing.RouteStrategy;
import routing.ShortestDistanceStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class MetroService {
    private final MetroGraph graph;
    private final NetworkConfig config;
    private final Map<RouteStrategyType, RouteStrategy> strategies = new HashMap<>();

    public MetroService(MetroRepository repository) {
        this.graph = repository.loadGraph();
        this.config = repository.loadConfig();
        register(new ShortestDistanceStrategy());
        register(new MinimumInterchangeStrategy());
        register(new FastestRouteStrategy());
    }

    public List<String> getStationNames() {
        return graph.getStationNames();
    }

    public int getStationCount() {
        return graph.getStationCount();
    }

    public Map<String, String> getLineColors() {
        return graph.getLineColors();
    }

    public NetworkConfig getConfig() {
        return config;
    }

    public RouteDetails findRoute(RouteQuery query) {
        validateQuery(query);
        RouteStrategy strategy = strategies.get(query.strategyType());
        PathResult bestPath = findBestPath(query.sourceName(), query.destinationName(), strategy);
        if (bestPath == null) {
            throw new IllegalArgumentException("No route found between the selected stations.");
        }
        return buildRouteDetails(query, bestPath);
    }

    private void validateQuery(RouteQuery query) {
        if (!graph.getStationNames().contains(query.sourceName()) || !graph.getStationNames().contains(query.destinationName())) {
            throw new IllegalArgumentException("Select valid source and destination stations.");
        }
        if (query.sourceName().equals(query.destinationName())) {
            throw new IllegalArgumentException("Source and destination cannot be the same station.");
        }
    }

    private PathResult findBestPath(String sourceName, String destinationName, RouteStrategy strategy) {
        Set<String> sourceNodes = graph.getNodesForStation(sourceName);
        Set<String> destinationNodes = graph.getNodesForStation(destinationName);
        PathResult bestResult = null;

        for (String sourceNode : sourceNodes) {
            PathResult candidate = dijkstra(sourceNode, destinationNodes, strategy);
            if (candidate != null && (bestResult == null || candidate.totalCost < bestResult.totalCost)) {
                bestResult = candidate;
            }
        }
        return bestResult;
    }

    private PathResult dijkstra(String sourceNode, Set<String> destinationNodes, RouteStrategy strategy) {
        List<StationNode> stationList = new ArrayList<>(graph.getStations());
        Map<String, Integer> indices = new HashMap<>();
        for (int index = 0; index < stationList.size(); index++) {
            indices.put(stationList.get(index).nodeId(), index);
        }

        double[] distances = new double[stationList.size()];
        String[] previous = new String[stationList.size()];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        distances[indices.get(sourceNode)] = 0.0;

        PriorityQueue<PathState> queue = new PriorityQueue<>(Comparator.comparingDouble(PathState::cost));
        queue.offer(new PathState(sourceNode, 0.0));

        while (!queue.isEmpty()) {
            PathState current = queue.poll();
            int currentIndex = indices.get(current.nodeId());
            if (current.cost() > distances[currentIndex]) {
                continue;
            }
            if (destinationNodes.contains(current.nodeId())) {
                return rebuildPath(current.nodeId(), current.cost(), previous, indices);
            }

            for (MetroGraph.EdgeRef edgeRef : graph.getNeighbors(current.nodeId())) {
                int neighborIndex = indices.get(edgeRef.targetNodeId());
                double nextCost = distances[currentIndex] + strategy.edgeCost(edgeRef.edge());
                if (nextCost < distances[neighborIndex]) {
                    distances[neighborIndex] = nextCost;
                    previous[neighborIndex] = current.nodeId();
                    queue.offer(new PathState(edgeRef.targetNodeId(), nextCost));
                }
            }
        }
        return null;
    }

    private PathResult rebuildPath(String destinationNode, double totalCost, String[] previous, Map<String, Integer> indices) {
        List<String> path = new ArrayList<>();
        String current = destinationNode;
        while (current != null) {
            path.add(0, current);
            current = previous[indices.get(current)];
        }
        return new PathResult(path, totalCost);
    }

    private RouteDetails buildRouteDetails(RouteQuery query, PathResult pathResult) {
        List<String> path = pathResult.nodePath();
        List<RouteStep> steps = new ArrayList<>();
        List<String> stationTrail = new ArrayList<>();
        List<String> interchanges = new ArrayList<>();
        double totalDistanceKm = 0.0;
        double totalMinutes = 0.0;
        int interchangeCount = 0;

        if (!path.isEmpty()) {
            StationNode start = graph.getStation(path.get(0));
            steps.add(new RouteStep("Start", start.stationName(), start.lineName(), false));
            stationTrail.add(start.stationName());
        }

        for (int index = 1; index < path.size(); index++) {
            StationNode previousNode = graph.getStation(path.get(index - 1));
            StationNode currentNode = graph.getStation(path.get(index));
            MetroEdge edge = findEdge(path.get(index - 1), path.get(index));

            totalDistanceKm += edge.distanceKm();
            totalMinutes += edge.travelMinutes();

            if (edge.interchange()) {
                interchangeCount++;
                String note = "Change at " + previousNode.stationName() + " from "
                    + previousNode.lineName() + " Line to " + currentNode.lineName() + " Line";
                interchanges.add(note);
                steps.add(new RouteStep("Change", previousNode.stationName(), currentNode.lineName(), true));
            } else {
                String label = index == path.size() - 1 ? "End" : "Stop";
                steps.add(new RouteStep(label, currentNode.stationName(), currentNode.lineName(), false));
                if (!stationTrail.get(stationTrail.size() - 1).equals(currentNode.stationName())) {
                    stationTrail.add(currentNode.stationName());
                }
            }
        }

        return new RouteDetails(
            query.sourceName(),
            query.destinationName(),
            query.strategyType().label(),
            path,
            stationTrail,
            steps,
            interchanges,
            round(totalDistanceKm),
            Math.max(stationTrail.size() - 1, 0),
            interchangeCount,
            (int) Math.ceil(totalMinutes),
            calculateFare(totalDistanceKm)
        );
    }

    private MetroEdge findEdge(String fromNodeId, String toNodeId) {
        return graph.getNeighbors(fromNodeId).stream()
            .filter(edgeRef -> edgeRef.targetNodeId().equals(toNodeId))
            .map(MetroGraph.EdgeRef::edge)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Broken path between " + fromNodeId + " and " + toNodeId));
    }

    private int calculateFare(double totalDistanceKm) {
        int slabs = (int) Math.ceil(totalDistanceKm / 5.0);
        int fare = config.baseFare() + Math.max(0, slabs - 1) * config.farePerFiveKm();
        return Math.min(fare, config.fareCap());
    }

    private void register(RouteStrategy strategy) {
        strategies.put(strategy.type(), strategy);
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private record PathState(String nodeId, double cost) {
    }

    private record PathResult(List<String> nodePath, double totalCost) {
    }
}
