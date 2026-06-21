package routing;

import model.MetroEdge;
import model.RouteStrategyType;

public interface RouteStrategy {
    RouteStrategyType type();

    double edgeCost(MetroEdge edge);
}
