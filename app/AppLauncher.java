package app;

import controller.RouteController;
import data.JsonMetroRepository;
import data.MetroRepository;
import service.MetroService;
import view.MetroNavigatorView;

import javax.swing.UIManager;

public final class AppLauncher {
    private AppLauncher() {
    }

    public static void launch() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Fall back to the default look and feel when the system theme is unavailable.
            }

            MetroRepository repository = new JsonMetroRepository("resources/data/metro-network.json");
            MetroService metroService = new MetroService(repository);
            MetroNavigatorView view = new MetroNavigatorView();
            new RouteController(view, metroService);
            view.setVisible(true);
        });
    }
}
