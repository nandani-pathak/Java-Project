package view;

import model.RouteDetails;
import model.RouteStep;
import model.RouteStrategyType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MetroNavigatorView extends JFrame {
    private static final Color PAGE_BG = new Color(8, 15, 27);
    private static final Color PANEL_BG = new Color(16, 29, 47);
    private static final Color PANEL_ALT = new Color(10, 21, 36);
    private static final Color CARD_BG = new Color(247, 249, 253);
    private static final Color TEXT_PRIMARY = new Color(244, 247, 251);
    private static final Color TEXT_MUTED = new Color(193, 207, 225);
    private static final Color TEXT_DARK = new Color(29, 43, 61);
    private static final Color ACCENT = new Color(47, 213, 196);
    private static final Color ACCENT_HOVER = new Color(25, 177, 161);
    private static final Color SUCCESS = new Color(94, 211, 124);
    private static final Color WARNING = new Color(243, 142, 82);
    private static final Color ERROR = new Color(211, 70, 70);

    private final DefaultComboBoxModel<String> sourceModel = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<String> destinationModel = new DefaultComboBoxModel<>();
    private final JComboBox<String> sourceDropdown = createSearchableDropdown(sourceModel);
    private final JComboBox<String> destinationDropdown = createSearchableDropdown(destinationModel);
    private final JComboBox<RouteStrategyType> strategyDropdown = new JComboBox<>(RouteStrategyType.values());
    private final JTextArea resultArea = new JTextArea();
    private final JLabel statusLabel = new JLabel();
    private final JLabel loadingLabel = new JLabel(" ");
    private final MetricCard stopsCard = new MetricCard("Stops", ACCENT);
    private final MetricCard distanceCard = new MetricCard("Distance", new Color(255, 199, 72));
    private final MetricCard fareCard = new MetricCard("Fare", SUCCESS);
    private final MetricCard etaCard = new MetricCard("ETA", WARNING);
    private final RouteMapPanel routeMapPanel = new RouteMapPanel();
    private final Map<String, Color> lineColors = new HashMap<>();
    private final Timer loadingTimer;

    private JButton findRouteButton;
    private JButton resetButton;
    private JButton swapButton;
    private JScrollPane pageScrollPane;
    private int loadingFrame;
    private List<String> stationNames = List.of();

    public MetroNavigatorView() {
        setTitle("Delhi Metro Navigator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1320, 930);
        setMinimumSize(new Dimension(1180, 860));
        setLocationRelativeTo(null);
        getContentPane().setBackground(PAGE_BG);
        setLayout(new BorderLayout());

        JPanel root = new JPanel();
        root.setBackground(PAGE_BG);
        root.setBorder(new EmptyBorder(20, 20, 18, 20));
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setPreferredSize(new Dimension(1520, 1500));
        root.add(buildHeroPanel());
        root.add(Box.createVerticalStrut(18));
        root.add(buildControlPanel());
        root.add(Box.createVerticalStrut(18));
        root.add(buildMetricPanel());
        root.add(Box.createVerticalStrut(18));
        root.add(buildBodyPanel());

        pageScrollPane = new JScrollPane(root);
        pageScrollPane.setBorder(null);
        pageScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pageScrollPane.getViewport().setBackground(PAGE_BG);
        pageScrollPane.getVerticalScrollBar().setUnitIncrement(18);
        pageScrollPane.getHorizontalScrollBar().setUnitIncrement(18);
        add(pageScrollPane, BorderLayout.CENTER);

        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(7, 12, 21));
        statusLabel.setForeground(TEXT_MUTED);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setBorder(new EmptyBorder(12, 24, 12, 24));
        add(statusLabel, BorderLayout.SOUTH);

        configureResultArea();
        installHover(findRouteButton, ACCENT, ACCENT_HOVER);
        installHover(swapButton, new Color(229, 238, 255), new Color(209, 223, 249));
        installHover(resetButton, new Color(251, 230, 230), new Color(240, 207, 207));

        loadingTimer = new Timer(250, event -> {
            loadingFrame = (loadingFrame + 1) % 4;
            loadingLabel.setText("Calculating route" + ".".repeat(loadingFrame));
        });
    }

    public void loadStations(List<String> stations, int stationCount, Map<String, String> colors) {
        stationNames = new ArrayList<>(stations);
        refillModel(sourceModel, stationNames);
        refillModel(destinationModel, stationNames);
        if (!stations.isEmpty()) {
            sourceDropdown.setSelectedIndex(0);
            destinationDropdown.setSelectedIndex(Math.min(8, stations.size() - 1));
        }
        lineColors.clear();
        colors.forEach((line, color) -> lineColors.put(line, Color.decode(color)));
        statusLabel.setText("Network loaded. " + stationCount + " stations are ready for route planning.");
    }

    public void setFindRouteAction(Runnable action) { findRouteButton.addActionListener(event -> action.run()); }
    public void setSwapAction(Runnable action) { swapButton.addActionListener(event -> action.run()); }
    public void setResetAction(Runnable action) { resetButton.addActionListener(event -> action.run()); }
    public String getSelectedSource() { return Objects.toString(sourceDropdown.getSelectedItem(), ""); }
    public String getSelectedDestination() { return Objects.toString(destinationDropdown.getSelectedItem(), ""); }
    public RouteStrategyType getSelectedStrategy() { return (RouteStrategyType) strategyDropdown.getSelectedItem(); }

    public void swapStations() {
        Object source = sourceDropdown.getSelectedItem();
        Object destination = destinationDropdown.getSelectedItem();
        sourceDropdown.setSelectedItem(destination);
        destinationDropdown.setSelectedItem(source);
        statusLabel.setForeground(TEXT_MUTED);
        statusLabel.setText("Stations swapped. You can now calculate the reverse trip.");
    }

    public void resetSelections() {
        if (!stationNames.isEmpty()) {
            sourceDropdown.setSelectedIndex(0);
            destinationDropdown.setSelectedIndex(Math.min(8, stationNames.size() - 1));
        }
        strategyDropdown.setSelectedItem(RouteStrategyType.SHORTEST_DISTANCE);
        showIdleState();
    }

    public void showIdleState() {
        stopLoading();
        setCards("--", "--", "--", "--");
        routeMapPanel.clearRoute();
        resultArea.setForeground(TEXT_DARK);
        resultArea.setText("Choose your source and destination, then select a routing strategy.\n\n"
            + "- Shortest Distance: minimizes total kilometers.\n"
            + "- Minimum Interchange: favors fewer line changes.\n"
            + "- Fastest Route: minimizes estimated travel time.\n\n"
            + "Tip: type inside the station fields to use autocomplete.");
        resultArea.setCaretPosition(0);
        statusLabel.setForeground(TEXT_MUTED);
    }

    public void showLoadingState() {
        findRouteButton.setEnabled(false);
        swapButton.setEnabled(false);
        resetButton.setEnabled(false);
        loadingFrame = 0;
        loadingLabel.setForeground(ACCENT);
        loadingLabel.setText("Calculating route");
        loadingTimer.start();
        statusLabel.setForeground(ACCENT);
        statusLabel.setText("Running " + getSelectedStrategy().label() + " optimization on the metro graph.");
    }

    public void showRoute(RouteDetails details) {
        stopLoading();
        setCards(String.valueOf(details.totalStops()), details.totalDistanceKm() + " km", "Rs " + details.fare(), details.estimatedMinutes() + " min");
        routeMapPanel.setRoute(details);
        resultArea.setForeground(TEXT_DARK);
        resultArea.setText(buildResultText(details));
        resultArea.setCaretPosition(0);
        statusLabel.setForeground(SUCCESS);
        statusLabel.setText(details.strategyLabel() + " route ready. " + details.totalStops() + " stops, Rs " + details.fare() + ", ETA " + details.estimatedMinutes() + " min.");
    }

    public void showError(String message) {
        stopLoading();
        setCards("--", "--", "--", "--");
        routeMapPanel.clearRoute();
        resultArea.setForeground(ERROR);
        resultArea.setText("ROUTE UNAVAILABLE\n-----------------\n" + message);
        resultArea.setCaretPosition(0);
        statusLabel.setForeground(ERROR);
        statusLabel.setText(message);
    }

    private void stopLoading() {
        loadingTimer.stop();
        loadingLabel.setText(" ");
        findRouteButton.setEnabled(true);
        swapButton.setEnabled(true);
        resetButton.setEnabled(true);
    }

    private void setCards(String stops, String distance, String fare, String eta) {
        stopsCard.setValue(stops);
        distanceCard.setValue(distance);
        fareCard.setValue(fare);
        etaCard.setValue(eta);
    }

    private JPanel buildHeroPanel() {
        GradientPanel hero = new GradientPanel(new BorderLayout(20, 0), new Color(20, 49, 78), new Color(11, 109, 136));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));
        hero.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 30), 1, true), new EmptyBorder(28, 28, 28, 28)));
        JPanel left = transparentPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Delhi Metro Navigator");
        title.setForeground(TEXT_PRIMARY);
        title.setFont(new Font("Georgia", Font.BOLD, 38));
        JLabel subtitle = new JLabel("<html>Plan routes across Blue, Yellow, Red, and Pink lines with distance, fare, ETA, and interchange details.</html>");
        subtitle.setForeground(new Color(237, 244, 251));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        left.add(title);
        left.add(Box.createVerticalStrut(12));
        left.add(subtitle);
        left.add(Box.createVerticalStrut(14));
        left.add(createChip("Choose stations, strategy, then click Find Route"));
        JPanel right = buildLegendPanel();
        hero.add(left, BorderLayout.CENTER);
        hero.add(right, BorderLayout.EAST);
        return hero;
    }

    private JPanel buildControlPanel() {
        JPanel panel = createPanel(PANEL_BG, new BorderLayout(), new EmptyBorder(24, 24, 24, 24));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 330));
        JPanel left = transparentPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Plan a Journey");
        title.setForeground(TEXT_PRIMARY);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
        JLabel subtitle = new JLabel("Choose source, destination, route strategy, and use the buttons below.");
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel fields = transparentPanel();
        fields.setLayout(new GridLayout(1, 3, 14, 14));
        strategyDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        strategyDropdown.setBackground(Color.WHITE);
        strategyDropdown.setMaximumRowCount(8);
        fields.add(createFieldBlock("From", sourceDropdown));
        fields.add(createFieldBlock("To", destinationDropdown));
        fields.add(createFieldBlock("Strategy", strategyDropdown));

        JPanel buttons = transparentPanel();
        buttons.setLayout(new GridLayout(1, 3, 12, 0));
        findRouteButton = createButton("Find Route", ACCENT, new Color(4, 24, 28));
        swapButton = createButton("Swap", new Color(229, 238, 255), TEXT_DARK);
        resetButton = createButton("Reset", new Color(251, 230, 230), new Color(137, 48, 48));
        buttons.add(findRouteButton);
        buttons.add(swapButton);
        buttons.add(resetButton);

        JPanel statusPanel = transparentPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        JLabel statusTitle = new JLabel("Status");
        statusTitle.setForeground(TEXT_MUTED);
        statusTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
        loadingLabel.setForeground(ACCENT);
        loadingLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        statusPanel.add(statusTitle);
        statusPanel.add(Box.createVerticalStrut(8));
        statusPanel.add(loadingLabel);

        left.add(title);
        left.add(Box.createVerticalStrut(8));
        left.add(subtitle);
        left.add(Box.createVerticalStrut(18));
        left.add(fields);
        left.add(Box.createVerticalStrut(18));
        left.add(buttons);
        left.add(Box.createVerticalStrut(14));
        left.add(statusPanel);

        panel.add(left, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildMetricPanel() {
        JPanel panel = createPanel(PANEL_BG, new BorderLayout(), new EmptyBorder(22, 22, 22, 22));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        JLabel heading = new JLabel("Trip Snapshot");
        heading.setForeground(TEXT_PRIMARY);
        heading.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));
        JPanel cards = transparentPanel();
        cards.setLayout(new GridLayout(1, 4, 14, 0));
        cards.add(stopsCard);
        cards.add(distanceCard);
        cards.add(fareCard);
        cards.add(etaCard);
        panel.add(heading, BorderLayout.NORTH);
        panel.add(cards, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildLegendPanel() {
        JPanel panel = createPanel(PANEL_ALT, new BorderLayout(0, 12), new EmptyBorder(18, 18, 18, 18));
        panel.setPreferredSize(new Dimension(360, 0));
        JLabel title = new JLabel("Line Legend");
        title.setForeground(TEXT_PRIMARY);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));

        JPanel rows = transparentPanel();
        rows.setLayout(new GridLayout(5, 1, 10, 10));
        rows.add(createLegendRow("Blue Line", new Color(63, 131, 230)));
        rows.add(createLegendRow("Yellow Line", new Color(225, 183, 24)));
        rows.add(createLegendRow("Red Line", new Color(224, 68, 68)));
        rows.add(createLegendRow("Pink Line", new Color(221, 90, 163)));
        rows.add(createLegendRow("Change", WARNING));

        panel.add(title, BorderLayout.NORTH);
        panel.add(rows, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildBodyPanel() {
        JPanel panel = createPanel(PANEL_BG, new BorderLayout(16, 16), new EmptyBorder(22, 22, 22, 22));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 900));
        JPanel content = transparentPanel();
        content.setLayout(new GridLayout(1, 2, 16, 0));
        content.setPreferredSize(new Dimension(1420, 880));
        JScrollPane routeScroll = new JScrollPane(routeMapPanel);
        routeScroll.setBorder(BorderFactory.createLineBorder(new Color(217, 225, 237), 1, true));
        routeScroll.getViewport().setBackground(CARD_BG);
        routeScroll.getVerticalScrollBar().setUnitIncrement(16);
        routeScroll.getHorizontalScrollBar().setUnitIncrement(16);
        routeMapPanel.attachScrollPane(routeScroll);
        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultScroll.setBorder(BorderFactory.createLineBorder(new Color(217, 225, 237), 1, true));
        resultScroll.getViewport().setBackground(CARD_BG);
        resultScroll.getVerticalScrollBar().setUnitIncrement(16);
        resultScroll.getHorizontalScrollBar().setUnitIncrement(16);
        content.add(routeScroll);
        content.add(resultScroll);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFieldBlock(String labelText, JComponent component) {
        JPanel panel = transparentPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        label.setForeground(TEXT_MUTED);
        label.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(component);
        return panel;
    }

    private JComboBox<String> createSearchableDropdown(DefaultComboBoxModel<String> model) {
        JComboBox<String> dropdown = new JComboBox<>(model);
        dropdown.setEditable(true);
        dropdown.setMaximumRowCount(20);
        dropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dropdown.setBackground(Color.WHITE);
        dropdown.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(191, 206, 227), 1, true), new EmptyBorder(10, 12, 10, 12)));
        dropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(new EmptyBorder(8, 10, 8, 10));
                return label;
            }
        });
        if (dropdown.getEditor().getEditorComponent() instanceof JTextField field) {
            field.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    refillModel(model, stationNames);
                    SwingUtilities.invokeLater(dropdown::showPopup);
                }
            });
            field.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    String current = field.getText();
                    String query = current.trim().toLowerCase();
                    List<String> filtered = stationNames;
                    if (!query.isEmpty()) {
                        filtered = stationNames.stream()
                            .filter(name -> name.toLowerCase().contains(query))
                            .toList();
                    }
                    refillModel(model, filtered.isEmpty() ? stationNames : filtered);
                    field.setText(current);
                    SwingUtilities.invokeLater(dropdown::showPopup);
                }
            });
        }
        dropdown.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
                if (!dropdown.isFocusOwner()) {
                    refillModel(model, stationNames);
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {
            }
        });
        return dropdown;
    }

    private JPanel createLegendRow(String text, Color color) {
        JPanel row = transparentPanel();
        row.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JPanel swatch = new JPanel();
        swatch.setBackground(color);
        swatch.setPreferredSize(new Dimension(18, 18));
        swatch.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 1, true));
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
        row.add(swatch);
        row.add(label);
        return row;
    }

    private JButton createButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 35), 1, true), new EmptyBorder(12, 16, 12, 16)));
        return button;
    }

    private void installHover(JButton button, Color base, Color hover) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                if (button.isEnabled()) {
                    button.setBackground(hover);
                }
            }

            @Override
            public void mouseExited(MouseEvent event) {
                button.setBackground(base);
            }
        });
    }

    private void configureResultArea() {
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setBackground(CARD_BG);
        resultArea.setForeground(TEXT_DARK);
        resultArea.setBorder(new EmptyBorder(18, 18, 18, 18));
    }

    private JPanel createChip(String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(true);
        panel.setBackground(new Color(255, 255, 255, 30));
        panel.setBorder(new EmptyBorder(8, 12, 8, 12));
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_PRIMARY);
        label.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        panel.add(label);
        return panel;
    }

    private void refillModel(DefaultComboBoxModel<String> model, List<String> items) {
        Object selected = model.getSelectedItem();
        model.removeAllElements();
        for (String item : items) {
            model.addElement(item);
        }
        if (selected != null && items.contains(selected.toString())) {
            model.setSelectedItem(selected);
        }
    }

    private JPanel createInfoBlock(String titleText, String bodyText) {
        JPanel panel = transparentPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel(titleText);
        title.setForeground(TEXT_PRIMARY);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
        JLabel body = new JLabel("<html>" + bodyText + "</html>");
        body.setForeground(TEXT_MUTED);
        body.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(title);
        panel.add(Box.createVerticalStrut(4));
        panel.add(body);
        return panel;
    }

    private JPanel createPanel(Color background, LayoutManager layout, EmptyBorder border) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(background);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 18), 1, true), border));
        return panel;
    }

    private JPanel transparentPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    private String buildResultText(RouteDetails details) {
        StringBuilder builder = new StringBuilder();
        builder.append("TRIP SUMMARY\n--------------------------------------------------\n");
        builder.append("From        : ").append(details.sourceName()).append('\n');
        builder.append("To          : ").append(details.destinationName()).append('\n');
        builder.append("Strategy    : ").append(details.strategyLabel()).append('\n');
        builder.append("Distance    : ").append(details.totalDistanceKm()).append(" km\n");
        builder.append("Fare        : Rs ").append(details.fare()).append('\n');
        builder.append("ETA         : ").append(details.estimatedMinutes()).append(" min\n");
        builder.append("Stops       : ").append(details.totalStops()).append('\n');
        builder.append("Interchange : ").append(details.totalInterchanges()).append("\n\n");
        builder.append("ROUTE NOTES\n--------------------------------------------------\n");
        if (details.interchanges().isEmpty()) {
            builder.append("Direct route. No line change required.\n\n");
        } else {
            for (String interchange : details.interchanges()) {
                builder.append("- ").append(interchange).append('\n');
            }
            builder.append('\n');
        }
        builder.append("STATION TRAIL\n--------------------------------------------------\n");
        builder.append(String.join(" -> ", details.stationTrail())).append("\n\n");
        builder.append("TIMELINE\n--------------------------------------------------\n");
        for (RouteStep step : details.steps()) {
            String lineText = step.lineName() == null || step.lineName().isBlank() ? "" : " [" + step.lineName() + " Line]";
            builder.append(String.format("%-8s %s%s%n", step.label(), step.stationName(), lineText));
        }
        return builder.toString();
    }

    private class MetricCard extends JPanel {
        private final JLabel valueLabel = new JLabel("--");

        MetricCard(String title, Color accentColor) {
            setBackground(new Color(9, 20, 36));
            setOpaque(true);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 145), 1, true), new EmptyBorder(16, 16, 16, 16)));
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_MUTED);
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            valueLabel.setForeground(accentColor);
            valueLabel.setFont(new Font("Georgia", Font.BOLD, 22));
            add(titleLabel);
            add(Box.createVerticalStrut(12));
            add(valueLabel);
        }

        void setValue(String value) {
            valueLabel.setText(value);
        }
    }

    private class RouteMapPanel extends JPanel {
        private RouteDetails details;
        private JScrollPane scrollPane;
        private Point dragAnchor;
        private Point viewportAnchor;
        private float animationProgress = 1.0f;

        RouteMapPanel() {
            setBackground(CARD_BG);
            setPreferredSize(new Dimension(640, 880));
            setBorder(new EmptyBorder(18, 18, 18, 18));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            MouseAdapter dragHandler = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent event) {
                    if (scrollPane == null) return;
                    dragAnchor = event.getPoint();
                    viewportAnchor = scrollPane.getViewport().getViewPosition();
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                }

                @Override
                public void mouseReleased(MouseEvent event) {
                    dragAnchor = null;
                    viewportAnchor = null;
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseDragged(MouseEvent event) {
                    if (scrollPane == null || dragAnchor == null || viewportAnchor == null) return;
                    int deltaX = dragAnchor.x - event.getX();
                    int deltaY = dragAnchor.y - event.getY();
                    int maxX = Math.max(getWidth() - scrollPane.getViewport().getWidth(), 0);
                    int maxY = Math.max(getHeight() - scrollPane.getViewport().getHeight(), 0);
                    scrollPane.getViewport().setViewPosition(new Point(Math.max(0, Math.min(viewportAnchor.x + deltaX, maxX)), Math.max(0, Math.min(viewportAnchor.y + deltaY, maxY))));
                }
            };
            addMouseListener(dragHandler);
            addMouseMotionListener(dragHandler);
        }

        void attachScrollPane(JScrollPane scrollPane) {
            this.scrollPane = scrollPane;
        }

        void setRoute(RouteDetails details) {
            this.details = details;
            this.animationProgress = 0.0f;
            updateCanvasSize();
            Timer timer = new Timer(22, null);
            timer.addActionListener(event -> {
                animationProgress += 0.06f;
                repaint();
                if (animationProgress >= 1.0f) {
                    animationProgress = 1.0f;
                    timer.stop();
                }
            });
            timer.start();
        }

        void clearRoute() {
            details = null;
            animationProgress = 1.0f;
            updateCanvasSize();
            repaint();
        }

        private void updateCanvasSize() {
            int height = details == null ? 880 : Math.max(880, 180 + details.steps().size() * 58);
            setPreferredSize(new Dimension(640, height));
            revalidate();
            if (scrollPane != null) {
                SwingUtilities.invokeLater(() -> scrollPane.getViewport().setViewPosition(new Point(0, 0)));
            }
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            if (details == null || details.steps().isEmpty()) {
                g2.setColor(new Color(236, 241, 248));
                g2.fillRoundRect(18, 18, getWidth() - 36, 100, 24, 24);
                g2.setColor(TEXT_DARK);
                g2.setFont(new Font("Georgia", Font.BOLD, 20));
                g2.drawString("Animated route map will appear here after search.", 34, 58);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                g2.drawString("Interchanges are highlighted clearly so screenshots stay easy to explain.", 34, 82);
                g2.dispose();
                return;
            }
            g2.setColor(new Color(238, 244, 250));
            g2.fillRoundRect(14, 14, getWidth() - 28, getHeight() - 28, 26, 26);
            g2.setColor(new Color(216, 224, 236));
            g2.drawRoundRect(14, 14, getWidth() - 29, getHeight() - 29, 26, 26);
            int lineX = 182;
            int startY = 100;
            int gapY = 56;
            int visibleSegments = Math.max(1, Math.round((details.steps().size() - 1) * animationProgress));
            for (int i = 0; i < details.steps().size() - 1 && i < visibleSegments; i++) {
                RouteStep current = details.steps().get(i);
                RouteStep next = details.steps().get(i + 1);
                int y1 = startY + i * gapY;
                int y2 = startY + (i + 1) * gapY;
                Color connector = current.interchange() || next.interchange() ? WARNING : lineColors.getOrDefault(current.lineName(), ACCENT);
                g2.setColor(connector);
                g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(lineX, y1, lineX, y2);
            }
            for (int i = 0; i < details.steps().size(); i++) {
                if (i > visibleSegments) break;
                RouteStep step = details.steps().get(i);
                int y = startY + i * gapY;
                Color nodeColor = step.interchange() ? WARNING : lineColors.getOrDefault(step.lineName(), ACCENT);
                g2.setColor(Color.WHITE);
                g2.fillOval(lineX - 11, y - 11, 22, 22);
                g2.setColor(nodeColor);
                g2.setStroke(new BasicStroke(5f));
                g2.drawOval(lineX - 11, y - 11, 22, 22);
                g2.setColor(TEXT_DARK);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.drawString(step.label().toUpperCase(), 32, y - 2);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                g2.drawString(step.stationName(), lineX + 24, y - 2);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g2.setColor(new Color(96, 110, 128));
                g2.drawString(step.interchange() ? "Transfer to " + step.lineName() + " Line" : step.lineName() + " Line", lineX + 24, y + 16);
            }
            g2.dispose();
        }
    }

    private static class GradientPanel extends JPanel {
        private final Color startColor;
        private final Color endColor;

        GradientPanel(LayoutManager layout, Color startColor, Color endColor) {
            super(layout);
            this.startColor = startColor;
            this.endColor = endColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
            g2.setColor(new Color(255, 255, 255, 16));
            g2.fillOval(getWidth() - 230, -30, 210, 210);
            g2.setColor(new Color(255, 255, 255, 12));
            g2.fillOval(getWidth() - 130, 82, 120, 120);
            g2.dispose();
            super.paintComponent(graphics);
        }
    }
}
