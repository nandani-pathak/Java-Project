package controller;

import model.RouteDetails;
import model.RouteQuery;
import model.RouteStrategyType;
import service.MetroService;
import view.MetroNavigatorView;

import javax.swing.SwingWorker;

public class RouteController {
    private final MetroNavigatorView view;
    private final MetroService metroService;

    public RouteController(MetroNavigatorView view, MetroService metroService) {
        this.view = view;
        this.metroService = metroService;
        bind();
        initialize();
    }

    private void initialize() {
        view.loadStations(metroService.getStationNames(), metroService.getStationCount(), metroService.getLineColors());
        view.showIdleState();
    }

    private void bind() {
        view.setFindRouteAction(this::handleRouteSearch);
        view.setSwapAction(view::swapStations);
        view.setResetAction(view::resetSelections);
    }

    private void handleRouteSearch() {
        String source = view.getSelectedSource();
        String destination = view.getSelectedDestination();
        RouteStrategyType strategy = view.getSelectedStrategy();

        view.showLoadingState();
        SwingWorker<RouteDetails, Void> worker = new SwingWorker<>() {
            @Override
            protected RouteDetails doInBackground() {
                try {
                    Thread.sleep(550);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                }
                return metroService.findRoute(new RouteQuery(source, destination, strategy));
            }

            @Override
            protected void done() {
                try {
                    view.showRoute(get());
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    view.showError(cause.getMessage());
                }
            }
        };
        worker.execute();
    }
}
