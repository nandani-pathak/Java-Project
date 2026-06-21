package data;

import model.MetroEdge;
import model.MetroGraph;
import model.NetworkConfig;
import model.StationNode;
import util.SimpleJsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JsonMetroRepository implements MetroRepository {
    private final String jsonPath;
    private MetroGraph cachedGraph;
    private NetworkConfig cachedConfig;

    public JsonMetroRepository(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    @Override
    public MetroGraph loadGraph() {
        ensureLoaded();
        return cachedGraph;
    }

    @Override
    public NetworkConfig loadConfig() {
        ensureLoaded();
        return cachedConfig;
    }

    private void ensureLoaded() {
        if (cachedGraph != null && cachedConfig != null) {
            return;
        }

        try {
            String json = Files.readString(Path.of(jsonPath), StandardCharsets.UTF_8);
            @SuppressWarnings("unchecked")
            Map<String, Object> root = (Map<String, Object>) SimpleJsonParser.parse(json);

            cachedConfig = parseConfig(castMap(root.get("config")));
            cachedGraph = buildGraph(root, cachedConfig);
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to load metro network JSON from " + jsonPath, ex);
        }
    }

    private NetworkConfig parseConfig(Map<String, Object> configNode) {
        return new NetworkConfig(
            getDouble(configNode, "minutesPerKm"),
            getDouble(configNode, "stationDwellMinutes"),
            getDouble(configNode, "interchangePenaltyMinutes"),
            getInt(configNode, "baseFare"),
            getInt(configNode, "farePerFiveKm"),
            getInt(configNode, "fareCap")
        );
    }

    private MetroGraph buildGraph(Map<String, Object> root, NetworkConfig config) {
        MetroGraph graph = new MetroGraph();

        for (Object stationNode : castList(root.get("stations"))) {
            Map<String, Object> station = castMap(stationNode);
            graph.registerStationGroup((String) station.get("name"));
        }

        Map<String, String> lineColors = new HashMap<>();
        for (Object lineNode : castList(root.get("lines"))) {
            Map<String, Object> line = castMap(lineNode);
            String lineName = (String) line.get("name");
            String color = (String) line.get("color");
            lineColors.put(lineName, color);

            List<Object> stations = castList(line.get("stations"));
            String previousNodeId = null;
            for (int index = 0; index < stations.size(); index++) {
                Map<String, Object> station = castMap(stations.get(index));
                String stationName = (String) station.get("station");
                String nodeId = createNodeId(lineName, stationName);
                graph.addStation(new StationNode(nodeId, stationName, lineName, color));

                if (previousNodeId != null) {
                    Map<String, Object> previousStation = castMap(stations.get(index - 1));
                    double distanceKm = getDouble(previousStation, "distanceToNextKm");
                    double travelMinutes = distanceKm * config.minutesPerKm() + config.stationDwellMinutes();
                    graph.addEdge(previousNodeId, nodeId, new MetroEdge(distanceKm, travelMinutes, false));
                }
                previousNodeId = nodeId;
            }
        }

        for (Object interchangeNode : castList(root.get("interchanges"))) {
            Map<String, Object> interchange = castMap(interchangeNode);
            String stationName = (String) interchange.get("station");
            String fromLine = (String) interchange.get("fromLine");
            String toLine = (String) interchange.get("toLine");
            double walkingDistanceKm = getDouble(interchange, "walkingDistanceKm");
            double transferMinutes = walkingDistanceKm * config.minutesPerKm() + config.interchangePenaltyMinutes();
            graph.addEdge(
                createNodeId(fromLine, stationName),
                createNodeId(toLine, stationName),
                new MetroEdge(walkingDistanceKm, transferMinutes, true)
            );
        }

        graph.setLineColors(lineColors);
        return graph;
    }

    private static String createNodeId(String lineName, String stationName) {
        return lineName.toLowerCase(Locale.ROOT) + "::" + stationName.toLowerCase(Locale.ROOT)
            .replace("&", "and")
            .replaceAll("[^a-z0-9]+", "_")
            .replaceAll("^_|_$", "");
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> castMap(Object value) {
        return value == null ? new LinkedHashMap<>() : (Map<String, Object>) value;
    }

    @SuppressWarnings("unchecked")
    private static List<Object> castList(Object value) {
        return value == null ? new ArrayList<>() : (List<Object>) value;
    }

    private static double getDouble(Map<String, Object> node, String key) {
        Object value = node.get(key);
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        throw new IllegalArgumentException("Missing numeric key: " + key);
    }

    private static int getInt(Map<String, Object> node, String key) {
        return (int) Math.round(getDouble(node, key));
    }
}
