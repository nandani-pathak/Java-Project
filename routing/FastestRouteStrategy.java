package routing;

import model.MetroEdge;
import model.RouteStrategyType;

public class FastestRouteStrategy implements RouteStrategy {
    @Override
    public RouteStrategyType type() {
        return RouteStrategyType.FASTEST_ROUTE;
    }

    @Override
    public double edgeCost(MetroEdge edge) {
        return edge.travelMinutes();
    }
}
