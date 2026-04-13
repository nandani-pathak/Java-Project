package routing;

import model.MetroEdge;
import model.RouteStrategyType;

public class MinimumInterchangeStrategy implements RouteStrategy {
    private static final double INTERCHANGE_PRIORITY = 1000.0;

    @Override
    public RouteStrategyType type() {
        return RouteStrategyType.MINIMUM_INTERCHANGE;
    }

    @Override
    public double edgeCost(MetroEdge edge) {
        return (edge.interchange() ? INTERCHANGE_PRIORITY : 0.0) + edge.distanceKm();
    }
}
