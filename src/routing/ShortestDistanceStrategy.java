package routing;

import model.MetroEdge;
import model.RouteStrategyType;

public class ShortestDistanceStrategy implements RouteStrategy {
    @Override
    public RouteStrategyType type() {
        return RouteStrategyType.SHORTEST_DISTANCE;
    }

    @Override
    public double edgeCost(MetroEdge edge) {
        return edge.distanceKm();
    }
}
