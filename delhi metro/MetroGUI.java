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
    private static final Color CARD_BG = new Color(247, 250, 255);
    private static final Color TEXT_PRIMARY = new Color(244, 247, 252);
    private static final Color TEXT_MUTED = new Color(171, 188, 213);
    private static final Color TEXT_DARK = new Color(33, 47, 72);
    private static final Color ACCENT = new Color(49, 201, 255);
    private static final Color SUCCESS = new Color(107, 225, 148);
    private static final Color WARNING = new Color(255, 139, 104);
    private static final Color PINK = new Color(221, 90, 163);
    private static final Color GREEN = new Color(68, 186, 106);
    private static final Color YELLOW = new Color(225, 183, 24);
    private static final Color BLUE = new Color(63, 131, 230);

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

    private final Graph metroGraph;

    public MetroGUI() {
        metroGraph = new Graph();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Delhi Metro Navigator TEST123");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(PAGE_BG);
        setLayout(new BorderLayout());

        JPanel root = new JPanel();
        root.setBackground(PAGE_BG);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(18, 18, 16, 18));

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
        statusLabel.setBorder(new EmptyBorder(0, 24, 14, 24));

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
        JPanel heroPanel = createPanel(HERO_BG, new BorderLayout(20, 0), new EmptyBorder(26, 28, 26, 28));
        heroPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Delhi Metro Navigator");
        title.setForeground(TEXT_PRIMARY);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 36));

        JLabel subtitle = new JLabel("Route drawing, fare, ETA, and station timeline in one clean Java project.");
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 17));

        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(title);
        left.add(Box.createVerticalStrut(12));
        left.add(subtitle);

        heroPanel.add(left, BorderLayout.CENTER);
        heroPanel.add(buildLegendPanel(), BorderLayout.EAST);
        return heroPanel;
    }

    private JPanel buildControlsPanel() {
        JPanel controlsPanel = createPanel(PANEL_BG, new BorderLayout(), new EmptyBorder(24, 24, 24, 24));
        controlsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JLabel heading = new JLabel("Choose Stations");
        heading.setForeground(TEXT_PRIMARY);
        heading.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));

        JLabel copy = new JLabel("Select source and destination, then the route map and trip details update below.");
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

        controlsPanel.add(stack, BorderLayout.CENTER);
        return controlsPanel;
    }

    private JPanel buildAnalyticsPanel() {
        JPanel analyticsPanel = createPanel(PANEL_BG, new BorderLayout(), new EmptyBorder(24, 24, 24, 24));
        analyticsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel heading = new JLabel("Trip Snapshot");
        heading.setForeground(TEXT_PRIMARY);
        heading.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));

        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
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
        detailsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 860));

        JPanel titles = new JPanel(new GridLayout(1, 2, 18, 18));
        titles.setOpaque(false);

        JPanel mapTitlePanel = new JPanel();
        mapTitlePanel.setOpaque(false);
        mapTitlePanel.setLayout(new BoxLayout(mapTitlePanel, BoxLayout.Y_AXIS));
        JLabel mapTitle = new JLabel("Visual Route Map");
        mapTitle.setForeground(TEXT_PRIMARY);
        mapTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        JLabel mapCopy = new JLabel("Actual route drawing with station nodes and interchange markers.");
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
        JLabel infoCopy = new JLabel("Simple station-by-station details for the selected route.");
        infoCopy.setForeground(TEXT_MUTED);
        infoCopy.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoTitlePanel.add(infoTitle);
        infoTitlePanel.add(Box.createVerticalStrut(8));
        infoTitlePanel.add(infoCopy);

        titles.add(mapTitlePanel);
        titles.add(infoTitlePanel);

        JPanel content = new JPanel(new GridLayout(1, 2, 18, 0));
        content.setOpaque(false);
        content.setMaximumSize(new Dimension(Integer.MAX_VALUE, 760));

        routeMapPanel = new RouteMapPanel();
        routeMapPanel.setPreferredSize(new Dimension(560, 760));
        routeMapPanel.setMinimumSize(new Dimension(560, 760));

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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

        content.add(routeMapPanel);
        content.add(resultScroll);

        detailsPanel.add(titles, BorderLayout.NORTH);
        detailsPanel.add(content, BorderLayout.CENTER);
        return detailsPanel;
    }

    private JPanel buildLegendPanel() {
        JPanel legendPanel = createPanel(new Color(12, 23, 42), new GridLayout(5, 1, 0, 10), new EmptyBorder(18, 20, 18, 20));
        legendPanel.setPreferredSize(new Dimension(240, 170));

        JLabel title = new JLabel("Line Legend");
        title.setForeground(TEXT_PRIMARY);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
        legendPanel.add(title);
        legendPanel.add(createLegendRow("Yellow Line", YELLOW));
        legendPanel.add(createLegendRow("Blue Line", BLUE));
        legendPanel.add(createLegendRow("Pink Line", PINK));
        legendPanel.add(createLegendRow("Green Line", GREEN));
        return legendPanel;
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
        dropdown.setBackground(Color.WHITE);
        dropdown.setBorder(new CompoundBorder(
            new LineBorder(new Color(214, 222, 235), 1, true),
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
            new LineBorder(new Color(220, 228, 238), 1, true),
            new EmptyBorder(12, 14, 12, 14)
        ));
        button.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 48));
        button.addActionListener(this);
        return button;
    }

    private JPanel createLegendRow(String text, Color swatchColor) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);

        JLabel swatch = new JLabel(" ");
        swatch.setOpaque(true);
        swatch.setBackground(swatchColor);
        swatch.setPreferredSize(new Dimension(24, 24));

        JLabel label = new JLabel(text);
        label.setForeground(TEXT_MUTED);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        row.add(swatch);
        row.add(label);
        return row;
    }

    private JPanel createPanel(Color bgColor, LayoutManager layout, Border border) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(bgColor);
        panel.setBorder(border);
        return panel;
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
            statusLabel.setText("Select two different stations to continue.");
            return;
        }

        try {
            Graph.RouteDetails details = metroGraph.findRouteDetails(sourceName, destName);
            stopsCard.setValue(String.valueOf(details.getStops()));
            fareCard.setValue("Rs " + details.getFare());
            timeCard.setValue(details.getEstimatedMinutes() + " min");
            interchangeCard.setValue(String.valueOf(details.getInterchanges().size()));
            routeMapPanel.setRoute(details.getPath());
            resultArea.setText(buildResultText(details));
            resultArea.setCaretPosition(0);
            statusLabel.setText(
                "Route ready from " + details.getSourceName() + " to " + details.getDestinationName()
                    + " | " + details.getStops() + " stops | Rs " + details.getFare()
            );
        } catch (InvalidStationException ex) {
            showError(ex.getMessage());
            statusLabel.setText("Unable to calculate route right now.");
        }
    }

    private String buildResultText(Graph.RouteDetails details) {
        StringBuilder sb = new StringBuilder();
        sb.append("BEST ROUTE\n");
        sb.append("--------------------------------------------------\n");
        sb.append("From        : ").append(details.getSourceName()).append("\n");
        sb.append("To          : ").append(details.getDestinationName()).append("\n");
        sb.append("Fare        : Rs ").append(details.getFare()).append("\n");
        sb.append("ETA         : ").append(details.getEstimatedMinutes()).append(" min\n");
        sb.append("Stops       : ").append(details.getStops()).append("\n");
        sb.append("Interchange : ").append(details.getInterchanges().size()).append("\n\n");

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

        sb.append("\nINTERCHANGE NOTES\n");
        sb.append("--------------------------------------------------\n");
        if (details.getInterchanges().isEmpty()) {
            sb.append("No interchanges needed for this journey.\n");
        } else {
            for (String interchange : details.getInterchanges()) {
                sb.append("- ").append(interchange).append("\n");
            }
        }

        return sb.toString();
    }

    private void showError(String message) {
        resetCards();
        routeMapPanel.clearRoute();
        resultArea.setText("ROUTE UNAVAILABLE\n-----------------\n" + message);
        resultArea.setCaretPosition(0);
    }

    private void clearResult() {
        sourceDropdown.setSelectedIndex(0);
        destinationDropdown.setSelectedIndex(Math.min(8, destinationDropdown.getItemCount() - 1));
        resetRouteView();
        statusLabel.setText("Selections reset. Ready for a new search.");
    }

    private void swapStations() {
        Object source = sourceDropdown.getSelectedItem();
        Object destination = destinationDropdown.getSelectedItem();
        sourceDropdown.setSelectedItem(destination);
        destinationDropdown.setSelectedItem(source);
        statusLabel.setText("Stations swapped. You can search the reverse trip now.");
    }

    private void resetRouteView() {
        resetCards();
        routeMapPanel.clearRoute();
        resultArea.setText(
            "Select source and destination stations, then click 'Find Route'.\n\n"
                + "The selected route details will appear here.\n"
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
                new LineBorder(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 115), 1, true),
                new EmptyBorder(14, 14, 14, 14)
            ));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(TEXT_MUTED);
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            valueLabel = new JLabel("--");
            valueLabel.setForeground(accentColor);
            valueLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 18));
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

        RouteMapPanel() {
            setBackground(CARD_BG);
            setBorder(new CompoundBorder(
                new LineBorder(new Color(219, 228, 240), 1, true),
                new EmptyBorder(12, 12, 12, 12)
            ));
        }

        void setRoute(List<Integer> path) {
            routePath = new ArrayList<>(path);
            repaint();
        }

        void clearRoute() {
            routePath.clear();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (routePath.isEmpty()) {
                g2.setColor(new Color(100, 118, 145));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                g2.drawString("Route map will appear here after you search.", 24, 44);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                g2.drawString("This panel is drawn directly using Java Swing graphics.", 24, 68);
                g2.dispose();
                return;
            }

            int lineX = 176;
            int startY = 74;
            int bottomPadding = 56;
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
                g2.setColor(lineColor);
                g2.setStroke(new BasicStroke(5f));
                g2.drawOval(point.x - 11, point.y - 11, 22, 22);

                if (i == 0 || i == points.size() - 1) {
                    g2.setColor(new Color(255, 255, 255, 210));
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
}
