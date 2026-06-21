package data;

import model.MetroGraph;
import model.NetworkConfig;

public interface MetroRepository {
    MetroGraph loadGraph();

    NetworkConfig loadConfig();
}
