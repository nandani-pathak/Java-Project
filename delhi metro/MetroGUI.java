// MetroGUI.java
// Java Swing GUI for Delhi Metro Route Finder

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MetroGUI extends JFrame implements ActionListener {

    private static final Color PAGE_BG = new Color(8, 15, 29);
    private static final Color HERO_BG = new Color(18, 31, 56);
    private static final Color PANEL_BG = new Color(23, 39, 69);
    private static final Color PANEL_BG_ALT = new Color(13, 26, 48);
    private static final Color CARD_BG = new Color(247, 250, 255);
    private static final Color TEXT_PRIMARY = new Color(244, 247, 252);
    private static final Color TEXT_MUTED = new Color(171, 188, 213);
    private static final Color TEXT_DARK = new Color(33, 47, 72);
    private static final Color ACCENT = new Color(49, 201, 255);
    private static final Color SUCCESS = new Color(107, 225, 148);
    private static final Color WARNING = new Color(255, 139, 104);
    private static final Color ERROR = new Color(210, 57, 57);
    private static final Color PINK = new Color(221, 90, 163);
    private static final Color GREEN = new Color(68, 186, 106);
    private static final Color YELLOW = new Color(225, 183, 24);
    private static final Color BLUE = new Color(63, 131, 230);
    private static final Color RED = new Color(224, 68, 68);

    private JComboBox<String> sourceDropdown;
    private JComboBox<String> destinationDropdown;
    private JButton findRouteBtn;
    private JButton clearBtn;
    private JButton swapBtn;
    private JTextArea resultArea;
    private JLabel statusLabel;

    private MetricCard stopsCard;
    private MetricCard fareCard;
    private MetricCard timeCard;
    private MetricCard interchangeCard;
    private RouteMapPanel routeMapPanel;
    private JScrollPane routeMapScroll;

    private final Graph metroGraph;

    public MetroGUI() {
        metroGraph = new Graph();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Delhi Metro Navigator");
        setSize(1280, 940);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(PAGE_BG);
        setLayout(new BorderLayout());

        JPanel root = new JPanel();
        root.setBackground(PAGE_BG);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(22, 22, 18, 22));

        JPanel hero = buildHeroPanel();
        hero.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel controls = buildControlsPanel();
        controls.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel analytics = buildAnalyticsPanel();
        analytics.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel details = buildDetailsPanel();
        details.setAlignmentX(Component.LEFT_ALIGNMENT);

        root.add(hero);
        root.add(Box.createVerticalStrut(18));
        root.add(controls);
        root.add(Box.createVerticalStrut(18));
        root.add(analytics);
        root.add(Box.createVerticalStrut(18));
        root.add(details);

        statusLabel = new JLabel("Ready to plan your route across " + metroGraph.getTotalStations() + " stations.");
        statusLabel.setForeground(TEXT_MUTED);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(7, 15, 29));
        statusLabel.setBorder(new EmptyBorder(12, 28, 14, 28));

        JScrollPane pageScroll = new JScrollPane(root);
        pageScroll.setBorder(null);
        pageScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pageScroll.getVerticalScrollBar().setUnitIncrement(18);
        pageScroll.getViewport().setBackground(PAGE_BG);

        add(pageScroll, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        resetRouteView();
        setVisible(true);
    }

    private JPanel buildHeroPanel() {
        JPanel heroPanel = new GradientPanel(new BorderLayout(24, 0), new EmptyBorder(28, 30, 28, 30),
            new Color(18, 35, 67), new Color(8, 77, 122));
        heroPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 292));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel badge = createHeroBadge("PRODUCT DEMO UI");
        JLabel title = new JLabel("Delhi Metro Navigator");
        title.setForeground(TEXT_PRIMARY);
        title.setFont(new Font("Georgia", Font.BOLD, 40));

        JLabel subtitle = new JLabel("<html>Plan Delhi Metro journeys with a full route summary, visual station trail, richer trip metrics, and screenshot-ready outputs for your project file.</html>");
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 17));

        JPanel chips = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        chips.setOpaque(false);
        chips.add(createInfoPill("5 Lines Active", new Color(255, 255, 255, 26), TEXT_PRIMARY));
        chips.add(createInfoPill(metroGraph.getTotalStations() + " Stations", new Color(255, 255, 255, 22), TEXT_PRIMARY));
        chips.add(createInfoPill("Route + Fare + ETA", new Color(49, 201, 255, 34), ACCENT));

        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        chips.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(badge);
        left.add(Box.createVerticalStrut(16));
        left.add(title);
        left.add(Box.createVerticalStrut(12));
        left.add(subtitle);
        left.add(Box.createVerticalStrut(18));
        left.add(chips);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        JPanel legend = buildLegendPanel();
        legend.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel spotlight = buildSpotlightPanel();
        spotlight.setAlignmentX(Component.LEFT_ALIGNMENT);
        right.add(legend);
        right.add(Box.createVerticalStrut(14));
        right.add(spotlight);

        heroPanel.add(left, BorderLayout.CENTER);
        heroPanel.add(right, BorderLayout.EAST);
        return heroPanel;
    }

    private JPanel buildControlsPanel() {
        JPanel controlsPanel = createPanel(PANEL_BG, new BorderLayout(20, 0), new EmptyBorder(26, 26, 26, 26));
        controlsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));

        JLabel heading = new JLabel("Choose Stations");
        heading.setForeground(TEXT_PRIMARY);
        heading.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));

        JLabel copy = new JLabel("Select your source and destination, then generate a route with timeline, cost, and travel estimate.");
        copy.setForeground(TEXT_MUTED);
        copy.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        String[] stationNames = metroGraph.getAllStationNames();
        sourceDropdown = createStationDropdown(stationNames);
        destinationDropdown = createStationDropdown(stationNames);
        destinationDropdown.setSelectedIndex(Math.min(8, stationNames.length - 1));

        JPanel fields = new JPanel(new GridLayout(1, 2, 18, 0));
        fields.setOpaque(false);
        fields.setMaximumSize(new Dimension(Integer.MAX_VALUE, 86));
        fields.add(createFieldBlock("From", sourceDropdown));
        fields.add(createFieldBlock("To", destinationDropdown));

        JPanel buttons = new JPanel(new GridLayout(1, 3, 12, 0));
        buttons.setOpaque(false);
        buttons.setPreferredSize(new Dimension(0, 52));
        buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        swapBtn = createButton("Swap", new Color(250, 240, 211), TEXT_DARK);
        findRouteBtn = createButton("Find Route", ACCENT, new Color(7, 22, 36));
        clearBtn = createButton("Reset", new Color(255, 232, 228), new Color(136, 42, 36));
        buttons.add(swapBtn);
        buttons.add(findRouteBtn);
        buttons.add(clearBtn);

        JPanel stack = new JPanel();
        stack.setOpaque(false);
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        copy.setAlignmentX(Component.LEFT_ALIGNMENT);
        fields.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
        stack.add(heading);
        stack.add(Box.createVerticalStrut(8));
        stack.add(copy);
        stack.add(Box.createVerticalStrut(18));
        stack.add(fields);
        stack.add(Box.createVerticalStrut(18));
        stack.add(buttons);

        JPanel helperCard = createPanel(PANEL_BG_ALT, new BorderLayout(0, 12), new EmptyBorder(18, 18, 18, 18));
        helperCard.setPreferredSize(new Dimension(220, 0));
        helperCard.add(createSideLabel("Quick Capture Guide"), BorderLayout.NORTH);

        JLabel routeHint = new JLabel("<html>Report-friendly routes:<br><br>Samaypur Badli to Rajiv Chowk<br>Hauz Khas to Vaishali<br>Green Park to Hauz Khas<br>Dwarka Sector 21 to Vaishali</html>");
        routeHint.setForeground(TEXT_MUTED);
        routeHint.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        helperCard.add(routeHint, BorderLayout.CENTER);

        controlsPanel.add(stack, BorderLayout.CENTER);
        controlsPanel.add(helperCard, BorderLayout.EAST);
        return controlsPanel;
    }

    private JPanel buildAnalyticsPanel() {
        JPanel analyticsPanel = createPanel(PANEL_BG, new BorderLayout(), new EmptyBorder(24, 24, 24, 24));
        analyticsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        JLabel heading = new JLabel("Trip Snapshot");
        heading.setForeground(TEXT_PRIMARY);
        heading.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));

        JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
        cards.setOpaque(false);
        stopsCard = new MetricCard("Stops", ACCENT);
        fareCard = new MetricCard("Fare", YELLOW);
        timeCard = new MetricCard("ETA", SUCCESS);
        interchangeCard = new MetricCard("Interchanges", WARNING);
        cards.add(stopsCard);
        cards.add(fareCard);
        cards.add(timeCard);
        cards.add(interchangeCard);

        analyticsPanel.add(heading, BorderLayout.NORTH);
        analyticsPanel.add(cards, BorderLayout.CENTER);
        return analyticsPanel;
    }

    private JPanel buildDetailsPanel() {
        JPanel detailsPanel = createPanel(PANEL_BG, new BorderLayout(18, 18), new EmptyBorder(24, 24, 24, 24));
        detailsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 880));

        JPanel titles = new JPanel(new GridLayout(1, 2, 18, 18));
        titles.setOpaque(false);

        JPanel mapTitlePanel = new JPanel();
        mapTitlePanel.setOpaque(false);
        mapTitlePanel.setLayout(new BoxLayout(mapTitlePanel, BoxLayout.Y_AXIS));
        JLabel mapTitle = new JLabel("Visual Route Map");
        mapTitle.setForeground(TEXT_PRIMARY);
        mapTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        JLabel mapCopy = new JLabel("A stylized station trail with line colors and transfer highlights. Drag inside the map to pan.");
        mapCopy.setForeground(TEXT_MUTED);
        mapCopy.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mapTitlePanel.add(mapTitle);
        mapTitlePanel.add(Box.createVerticalStrut(8));
        mapTitlePanel.add(mapCopy);

        JPanel infoTitlePanel = new JPanel();
        infoTitlePanel.setOpaque(false);
        infoTitlePanel.setLayout(new BoxLayout(infoTitlePanel, BoxLayout.Y_AXIS));
        JLabel infoTitle = new JLabel("Route Notes");
        infoTitle.setForeground(TEXT_PRIMARY);
        infoTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        JLabel infoCopy = new JLabel("Full route text, station-by-station timeline, and interchange notes.");
        infoCopy.setForeground(TEXT_MUTED);
        infoCopy.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoTitlePanel.add(infoTitle);
        infoTitlePanel.add(Box.createVerticalStrut(8));
        infoTitlePanel.add(infoCopy);

        titles.add(mapTitlePanel);
        titles.add(infoTitlePanel);

        JPanel content = new JPanel(new GridLayout(1, 2, 18, 0));
        content.setOpaque(false);
        content.setMaximumSize(new Dimension(Integer.MAX_VALUE, 780));

        routeMapPanel = new RouteMapPanel();
        routeMapPanel.setPreferredSize(new Dimension(720, 920));
        routeMapPanel.setMinimumSize(new Dimension(720, 920));

        routeMapScroll = new JScrollPane(routeMapPanel);
        routeMapScroll.setBorder(new CompoundBorder(
            new LineBorder(new Color(219, 228, 240), 1, true),
            new EmptyBorder(0, 0, 0, 0)
        ));
        routeMapScroll.getViewport().setBackground(CARD_BG);
        routeMapScroll.getVerticalScrollBar().setUnitIncrement(18);
        routeMapScroll.getHorizontalScrollBar().setUnitIncrement(18);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setBackground(CARD_BG);
        resultArea.setForeground(TEXT_DARK);
        resultArea.setBorder(new EmptyBorder(16, 16, 16, 16));

        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultScroll.setBorder(new CompoundBorder(
            new LineBorder(new Color(219, 228, 240), 1, true),
            new EmptyBorder(0, 0, 0, 0)
        ));
        resultScroll.getViewport().setBackground(CARD_BG);
        resultScroll.getVerticalScrollBar().setUnitIncrement(16);

        content.add(routeMapScroll);
        content.add(resultScroll);

        detailsPanel.add(titles, BorderLayout.NORTH);
        detailsPanel.add(content, BorderLayout.CENTER);
        return detailsPanel;
    }

    private JPanel buildLegendPanel() {
        JPanel legendPanel = createPanel(new Color(10, 22, 42), new BorderLayout(0, 14), new EmptyBorder(18, 20, 18, 20));
        legendPanel.setPreferredSize(new Dimension(226, 190));

        JLabel title = new JLabel("Line Legend");
        title.setForeground(TEXT_PRIMARY);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));

        JPanel rows = new JPanel(new GridLayout(3, 2, 8, 10));
        rows.setOpaque(false);
        rows.add(createLegendRow("Yellow", YELLOW));
        rows.add(createLegendRow("Blue", BLUE));
        rows.add(createLegendRow("Pink", PINK));
        rows.add(createLegendRow("Green", GREEN));
        rows.add(createLegendRow("Red", RED));
        rows.add(createLegendRow("Transfer", WARNING));

        legendPanel.add(title, BorderLayout.NORTH);
        legendPanel.add(rows, BorderLayout.CENTER);
        return legendPanel;
    }

    private JPanel buildSpotlightPanel() {
        JPanel panel = createPanel(new Color(8, 19, 35), new BorderLayout(0, 10), new EmptyBorder(16, 18, 16, 18));
        panel.setPreferredSize(new Dimension(226, 104));

        JLabel title = createSideLabel("System Snapshot");
        JLabel copy = new JLabel("<html>Looks cleaner for demos, shows all required metrics, and keeps screenshots readable for Chapter 5.</html>");
        copy.setForeground(TEXT_MUTED);
        copy.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        panel.add(title, BorderLayout.NORTH);
        panel.add(copy, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFieldBlock(String title, JComboBox<String> dropdown) {
        JPanel field = new JPanel();
        field.setOpaque(false);
        field.setLayout(new BoxLayout(field, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(title);
        label.setForeground(TEXT_MUTED);
        label.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        dropdown.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.add(label);
        field.add(Box.createVerticalStrut(10));
        field.add(dropdown);
        return field;
    }

    private JComboBox<String> createStationDropdown(String[] stationNames) {
        JComboBox<String> dropdown = new JComboBox<>(stationNames);
        dropdown.setPreferredSize(new Dimension(300, 54));
        dropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        dropdown.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        dropdown.setBackground(new Color(250, 252, 255));
        dropdown.setBorder(new CompoundBorder(
            new LineBorder(new Color(182, 202, 235), 1, true),
            new EmptyBorder(11, 14, 11, 14)
        ));
        dropdown.setRenderer(new StationRenderer());
        return dropdown;
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
            new LineBorder(new Color(255, 255, 255, 38), 1, true),
            new EmptyBorder(12, 14, 12, 14)
        ));
        button.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 48));
        button.addActionListener(this);
        return button;
    }

    private JPanel createLegendRow(String text, Color swatchColor) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row.setOpaque(false);

        JPanel swatch = new JPanel();
        swatch.setOpaque(true);
        swatch.setBackground(swatchColor);
        swatch.setPreferredSize(new Dimension(18, 18));
        swatch.setBorder(new LineBorder(new Color(255, 255, 255, 35), 1, true));

        JLabel label = new JLabel(text);
        label.setForeground(TEXT_PRIMARY);
        label.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));

        row.add(swatch);
        row.add(label);
        return row;
    }

    private JPanel createPanel(Color bgColor, LayoutManager layout, Border border) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(bgColor);
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(255, 255, 255, 18), 1, true),
            border
        ));
        return panel;
    }

    private JLabel createHeroBadge(String text) {
        JLabel badge = new JLabel(text);
        badge.setOpaque(true);
        badge.setBackground(new Color(255, 255, 255, 26));
        badge.setForeground(TEXT_PRIMARY);
        badge.setBorder(new EmptyBorder(7, 12, 7, 12));
        badge.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        return badge;
    }

    private JPanel createInfoPill(String text, Color bg, Color fg) {
        JPanel pill = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pill.setOpaque(true);
        pill.setBackground(bg);
        pill.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel label = new JLabel(text);
        label.setForeground(fg);
        label.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        pill.add(label);
        return pill;
    }

    private JLabel createSideLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_PRIMARY);
        label.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == findRouteBtn) {
            findRoute();
        } else if (source == clearBtn) {
            clearResult();
        } else if (source == swapBtn) {
            swapStations();
        }
    }

    private void findRoute() {
        String sourceName = (String) sourceDropdown.getSelectedItem();
        String destName = (String) destinationDropdown.getSelectedItem();

        if (sourceName == null || destName == null) {
            showError("Select valid source and destination stations.");
            return;
        }

        if (sourceName.equals(destName)) {
            showError("Source and destination cannot be the same station.");
            statusLabel.setForeground(ERROR);
            statusLabel.setText("Select two different stations to continue.");
            return;
        }

        try {
            Graph.RouteDetails details = metroGraph.findRouteDetails(sourceName, destName);
            applyResultStyle(false);
            stopsCard.setValue(String.valueOf(details.getStops()));
            fareCard.setValue("\u20B9" + details.getFare());
            timeCard.setValue(details.getEstimatedMinutes() + " min");
            interchangeCard.setValue(String.valueOf(details.getInterchanges().size()));
            routeMapPanel.setRoute(details.getPath());
            resultArea.setText(buildResultText(details));
            resultArea.setCaretPosition(0);
            statusLabel.setForeground(SUCCESS);
            statusLabel.setText("Route found! Stops: " + details.getStops()
                + " | Fare: \u20B9" + details.getFare()
                + " | ETA: " + details.getEstimatedMinutes() + " min");
        } catch (InvalidStationException ex) {
            showError(ex.getMessage());
            statusLabel.setForeground(ERROR);
            statusLabel.setText("Route unavailable. " + ex.getMessage());
        }
    }

    private String buildResultText(Graph.RouteDetails details) {
        StringBuilder sb = new StringBuilder();
        sb.append("TRIP SUMMARY\n");
        sb.append("--------------------------------------------------\n");
        sb.append("From        : ").append(details.getSourceName()).append("\n");
        sb.append("To          : ").append(details.getDestinationName()).append("\n");
        sb.append("Lines Used  : ").append(buildLineSummary(details.getPath())).append("\n");
        sb.append("Fare        : \u20B9").append(details.getFare()).append("\n");
        sb.append("ETA         : ").append(details.getEstimatedMinutes()).append(" min\n");
        sb.append("Stops       : ").append(details.getStops()).append("\n");
        sb.append("Interchange : ").append(details.getInterchanges().size()).append("\n\n");

        sb.append("ROUTE NOTES\n");
        sb.append("--------------------------------------------------\n");
        sb.append(buildTransferSummary(details)).append("\n");
        sb.append("First stop  : ").append(details.getSourceName()).append("\n");
        sb.append("Final stop  : ").append(details.getDestinationName()).append("\n");
        sb.append("Quick view  : ").append(buildStopsPreview(details.getPath())).append("\n\n");

        sb.append("FULL ROUTE\n");
        sb.append("--------------------------------------------------\n");
        sb.append(buildFullRouteLine(details.getPath())).append("\n\n");

        sb.append("STATION TIMELINE\n");
        sb.append("--------------------------------------------------\n");
        List<Integer> path = details.getPath();
        for (int i = 0; i < path.size(); i++) {
            String stationName = metroGraph.getStationName(path.get(i));
            String lineName = metroGraph.getStationLine(path.get(i));
            String label = "Stop";
            if (i == 0) {
                label = "Start";
            } else if (i == path.size() - 1) {
                label = "End";
            } else {
                String previousLine = metroGraph.getStationLine(path.get(i - 1));
                if (!previousLine.equals(lineName)) {
                    label = "Change";
                }
            }
            sb.append(String.format("%-8s %s [%s Line]%n", label, stationName, lineName));
        }

        return sb.toString();
    }

    private String buildLineSummary(List<Integer> path) {
        List<String> lines = new ArrayList<>();
        for (int stationId : path) {
            String line = metroGraph.getStationLine(stationId);
            if (lines.isEmpty() || !lines.get(lines.size() - 1).equals(line)) {
                lines.add(line);
            }
        }
        return String.join(" -> ", lines);
    }

    private String buildTransferSummary(Graph.RouteDetails details) {
        if (details.getInterchanges().isEmpty()) {
            return "Direct trip. No line change needed.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Change at ");
        for (int i = 0; i < details.getInterchanges().size(); i++) {
            if (i > 0) {
                sb.append(i == details.getInterchanges().size() - 1 ? " and " : ", ");
            }
            sb.append(details.getInterchanges().get(i));
        }
        sb.append('.');
        return sb.toString();
    }

    private String buildStopsPreview(List<Integer> path) {
        if (path.isEmpty()) {
            return "--";
        }
        if (path.size() <= 6) {
            return buildFullRouteLine(path);
        }
        return metroGraph.getStationName(path.get(0))
            + " -> "
            + metroGraph.getStationName(path.get(1))
            + " -> ... -> "
            + metroGraph.getStationName(path.get(path.size() - 2))
            + " -> "
            + metroGraph.getStationName(path.get(path.size() - 1));
    }

    private String buildFullRouteLine(List<Integer> path) {
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) {
                route.append(" -> ");
            }
            route.append(metroGraph.getStationName(path.get(i)));
        }
        return route.toString();
    }

    private void applyResultStyle(boolean isError) {
        resultArea.setForeground(isError ? ERROR : TEXT_DARK);
    }

    private void showError(String message) {
        resetCards();
        routeMapPanel.clearRoute();
        applyResultStyle(true);
        resultArea.setText("ROUTE UNAVAILABLE\n-----------------\n" + message);
        resultArea.setCaretPosition(0);
    }

    private void clearResult() {
        sourceDropdown.setSelectedIndex(0);
        destinationDropdown.setSelectedIndex(Math.min(8, destinationDropdown.getItemCount() - 1));
        resetRouteView();
        statusLabel.setForeground(TEXT_MUTED);
        statusLabel.setText("Selections reset. Ready for a new search.");
    }

    private void swapStations() {
        Object source = sourceDropdown.getSelectedItem();
        Object destination = destinationDropdown.getSelectedItem();
        sourceDropdown.setSelectedItem(destination);
        destinationDropdown.setSelectedItem(source);
        statusLabel.setForeground(TEXT_MUTED);
        statusLabel.setText("Stations swapped. You can search the reverse trip now.");
    }

    private void resetRouteView() {
        applyResultStyle(false);
        resetCards();
        routeMapPanel.clearRoute();
        resultArea.setText(
            "Select source and destination stations, then click 'Find Route'.\n\n"
                + "The full route, stop count, fare, ETA, and interchange details will appear here.\n"
        );
        resultArea.setCaretPosition(0);
    }

    private void resetCards() {
        stopsCard.setValue("--");
        fareCard.setValue("--");
        timeCard.setValue("--");
        interchangeCard.setValue("--");
    }

    private Color getLineColor(String line) {
        switch (line) {
            case "Yellow":
                return YELLOW;
            case "Blue":
                return BLUE;
            case "Pink":
                return PINK;
            case "Green":
                return GREEN;
            case "Red":
                return RED;
            default:
                return new Color(106, 124, 150);
        }
    }

    private class StationRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof String stationName) {
                String line = metroGraph.getStationLineByName(stationName);
                label.setText(stationName + "  |  " + line + " Line");
                label.setBorder(new EmptyBorder(8, 10, 8, 10));
                if (!isSelected) {
                    label.setBackground(Color.WHITE);
                    label.setForeground(getLineColor(line).darker());
                }
            }
            return label;
        }
    }

    private class MetricCard extends JPanel {
        private final JLabel valueLabel;

        MetricCard(String title, Color accentColor) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(new Color(11, 24, 44));
            setBorder(new CompoundBorder(
                new LineBorder(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 140), 1, true),
                new EmptyBorder(16, 16, 16, 16)
            ));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_MUTED);
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            valueLabel = new JLabel("--");
            valueLabel.setForeground(accentColor);
            valueLabel.setFont(new Font("Georgia", Font.BOLD, 21));
            valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            add(titleLabel);
            add(Box.createVerticalStrut(12));
            add(valueLabel);
            add(Box.createVerticalGlue());
        }

        void setValue(String value) {
            valueLabel.setText(value);
        }
    }

    private class RouteMapPanel extends JPanel {
        private List<Integer> routePath = new ArrayList<>();
        private Point dragAnchor;
        private Point viewAnchor;

        RouteMapPanel() {
            setBackground(CARD_BG);
            setBorder(new CompoundBorder(
                new LineBorder(new Color(219, 228, 240), 1, true),
                new EmptyBorder(16, 16, 16, 16)
            ));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            MouseAdapter dragHandler = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, RouteMapPanel.this);
                    if (viewport == null) {
                        return;
                    }
                    dragAnchor = e.getPoint();
                    viewAnchor = viewport.getViewPosition();
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    dragAnchor = null;
                    viewAnchor = null;
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, RouteMapPanel.this);
                    if (viewport == null || dragAnchor == null || viewAnchor == null) {
                        return;
                    }

                    int deltaX = dragAnchor.x - e.getX();
                    int deltaY = dragAnchor.y - e.getY();
                    int maxX = Math.max(getWidth() - viewport.getWidth(), 0);
                    int maxY = Math.max(getHeight() - viewport.getHeight(), 0);
                    int nextX = Math.max(0, Math.min(viewAnchor.x + deltaX, maxX));
                    int nextY = Math.max(0, Math.min(viewAnchor.y + deltaY, maxY));
                    viewport.setViewPosition(new Point(nextX, nextY));
                }
            };

            addMouseListener(dragHandler);
            addMouseMotionListener(dragHandler);
        }

        void setRoute(List<Integer> path) {
            routePath = new ArrayList<>(path);
            updateCanvasSize();
            repaint();
        }

        void clearRoute() {
            routePath.clear();
            updateCanvasSize();
            repaint();
        }

        private void updateCanvasSize() {
            int height = routePath.isEmpty() ? 920 : Math.max(920, 180 + (routePath.size() * 36));
            setPreferredSize(new Dimension(720, height));
            revalidate();
            if (routeMapScroll != null) {
                SwingUtilities.invokeLater(() -> routeMapScroll.getViewport().setViewPosition(new Point(0, 0)));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (routePath.isEmpty()) {
                g2.setColor(new Color(234, 241, 252));
                g2.fillRoundRect(16, 16, getWidth() - 32, 92, 24, 24);
                g2.setColor(new Color(100, 118, 145));
                g2.setFont(new Font("Georgia", Font.BOLD, 20));
                g2.drawString("Route map will appear here after you search.", 34, 54);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                g2.drawString("This panel is drawn directly using Java Swing graphics.", 34, 80);
                g2.dispose();
                return;
            }

            g2.setColor(new Color(238, 244, 252));
            g2.fillRoundRect(16, 16, getWidth() - 32, getHeight() - 32, 28, 28);
            g2.setColor(new Color(221, 229, 241));
            g2.drawRoundRect(16, 16, getWidth() - 33, getHeight() - 33, 28, 28);
            g2.setColor(TEXT_DARK);
            g2.setFont(new Font("Georgia", Font.BOLD, 18));
            g2.drawString("Journey Map", 34, 48);
            g2.setColor(new Color(105, 121, 145));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString("Colored connectors show line continuity and transfer points.", 34, 68);

            int lineX = 182;
            int startY = 112;
            int bottomPadding = 72;
            int availableHeight = Math.max(getHeight() - startY - bottomPadding, 120);
            int stepY = routePath.size() == 1 ? 0 : availableHeight / (routePath.size() - 1);

            List<Point> points = new ArrayList<>();
            for (int i = 0; i < routePath.size(); i++) {
                int y = startY + (i * stepY);
                points.add(new Point(lineX, y));
            }

            for (int i = 0; i < points.size() - 1; i++) {
                Point p1 = points.get(i);
                Point p2 = points.get(i + 1);
                String currentLine = metroGraph.getStationLine(routePath.get(i));
                String nextLine = metroGraph.getStationLine(routePath.get(i + 1));
                Color connectorColor = getLineColor(currentLine);
                if (!currentLine.equals(nextLine)) {
                    connectorColor = WARNING;
                }
                g2.setColor(connectorColor);
                g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

            for (int i = 0; i < points.size(); i++) {
                Point point = points.get(i);
                String stationName = metroGraph.getStationName(routePath.get(i));
                String lineName = metroGraph.getStationLine(routePath.get(i));
                Color lineColor = getLineColor(lineName);

                g2.setColor(Color.WHITE);
                g2.fillOval(point.x - 11, point.y - 11, 22, 22);
                g2.setColor(isInterchange(i) ? WARNING : lineColor);
                g2.setStroke(new BasicStroke(5f));
                g2.drawOval(point.x - 11, point.y - 11, 22, 22);

                if (i == 0 || i == points.size() - 1) {
                    g2.setColor(new Color(255, 255, 255, 228));
                    g2.fillRoundRect(point.x - 30, point.y - 42, 64, 20, 10, 10);
                    g2.setColor(i == 0 ? SUCCESS.darker() : WARNING.darker());
                    String tag = i == 0 ? "START" : "END";
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    FontMetrics tagMetrics = g2.getFontMetrics();
                    int tagWidth = tagMetrics.stringWidth(tag);
                    g2.drawString(tag, point.x + 2 - tagWidth / 2, point.y - 28);
                }

                g2.setColor(TEXT_DARK);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                drawWrappedLeftText(g2, stationName, point.x + 18, point.y - 10, 220, 2);
            }

            g2.dispose();
        }

        private boolean isInterchange(int index) {
            if (index <= 0 || index >= routePath.size() - 1) {
                return false;
            }
            String previous = metroGraph.getStationLine(routePath.get(index - 1));
            String current = metroGraph.getStationLine(routePath.get(index));
            String next = metroGraph.getStationLine(routePath.get(index + 1));
            return !previous.equals(current) || !current.equals(next);
        }

        private void drawWrappedLeftText(Graphics2D g2, String text, int x, int y, int maxWidth, int maxLines) {
            FontMetrics fm = g2.getFontMetrics();
            String[] words = text.split(" ");
            List<String> lines = new ArrayList<>();
            StringBuilder current = new StringBuilder();

            for (String word : words) {
                String candidate = current.length() == 0 ? word : current + " " + word;
                if (fm.stringWidth(candidate) > maxWidth && current.length() > 0) {
                    lines.add(current.toString());
                    current = new StringBuilder(word);
                } else {
                    current = new StringBuilder(candidate);
                }
            }
            if (current.length() > 0) {
                lines.add(current.toString());
            }

            if (lines.size() > maxLines) {
                List<String> trimmed = new ArrayList<>();
                for (int i = 0; i < maxLines; i++) {
                    trimmed.add(lines.get(i));
                }
                String last = trimmed.get(maxLines - 1);
                while (fm.stringWidth(last + "...") > maxWidth && last.length() > 0) {
                    last = last.substring(0, last.length() - 1);
                }
                trimmed.set(maxLines - 1, last + "...");
                lines = trimmed;
            }

            int lineHeight = fm.getHeight();
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                g2.drawString(line, x, y + (i * lineHeight));
            }
        }
    }

    private class GradientPanel extends JPanel {
        private final Color startColor;
        private final Color endColor;

        GradientPanel(LayoutManager layout, Border border, Color startColor, Color endColor) {
            super(layout);
            this.startColor = startColor;
            this.endColor = endColor;
            setOpaque(false);
            setBorder(new CompoundBorder(
                new LineBorder(new Color(255, 255, 255, 20), 1, true),
                border
            ));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint paint = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
            g2.setPaint(paint);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
            g2.setColor(new Color(255, 255, 255, 18));
            g2.fillOval(getWidth() - 210, -40, 240, 240);
            g2.setColor(new Color(49, 201, 255, 26));
            g2.fillOval(getWidth() - 320, 46, 170, 170);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
