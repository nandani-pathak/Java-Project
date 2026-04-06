// MetroGUI.java
// Java Swing GUI for Delhi Metro Route Finder

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MetroGUI extends JFrame implements ActionListener {

    private static final Color PAGE_BG = new Color(7, 16, 32);
    private static final Color PANEL_BG = new Color(15, 28, 52);
    private static final Color PANEL_ALT = new Color(20, 38, 68);
    private static final Color ACCENT = new Color(0, 196, 255);
    private static final Color TEXT_PRIMARY = new Color(241, 247, 255);
    private static final Color TEXT_MUTED = new Color(157, 177, 204);
    private static final Color SUCCESS = new Color(120, 238, 176);
    private static final Color WARNING = new Color(255, 107, 107);

    private JComboBox<String> sourceDropdown;
    private JComboBox<String> destinationDropdown;
    private JButton findRouteBtn;
    private JButton clearBtn;
    private JButton swapBtn;
    private JTextArea resultArea;
    private JLabel statusLabel;
    private JLabel totalStopsValue;
    private JLabel fareValue;
    private JLabel timeValue;
    private JLabel interchangeValue;

    private final Graph metroGraph;

    public MetroGUI() {
        metroGraph = new Graph();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Delhi Metro Navigator");
        setSize(980, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(PAGE_BG);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout(18, 18));
        contentPanel.setBackground(PAGE_BG);
        contentPanel.setBorder(new EmptyBorder(18, 18, 18, 18));

        contentPanel.add(buildHeroPanel(), BorderLayout.NORTH);
        contentPanel.add(buildCenterPanel(), BorderLayout.CENTER);

        statusLabel = new JLabel("Ready to plan a trip across " + metroGraph.getTotalStations() + " stations.");
        statusLabel.setForeground(TEXT_MUTED);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setBorder(new EmptyBorder(0, 20, 14, 20));

        add(contentPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        resetSummaryCards();
        showWelcomeMessage();
        setVisible(true);
    }

    private JPanel buildHeroPanel() {
        JPanel heroPanel = createPanel(PANEL_BG, new BorderLayout(10, 10), new EmptyBorder(20, 22, 20, 22));

        JLabel titleLabel = new JLabel("Delhi Metro Navigator");
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 30));

        JLabel subtitleLabel = new JLabel("A polished route planner with fare, travel time, and interchange insights.");
        subtitleLabel.setForeground(TEXT_MUTED);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        badgePanel.setOpaque(false);
        badgePanel.add(createBadge("Network Map Feel", new Color(255, 214, 102)));
        badgePanel.add(createBadge("Fastest Route Logic", SUCCESS));
        badgePanel.add(createBadge("Fare + Time Summary", ACCENT));

        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        textPanel.add(badgePanel);

        heroPanel.add(textPanel, BorderLayout.CENTER);
        heroPanel.add(buildLegendPanel(), BorderLayout.EAST);

        return heroPanel;
    }

    private JPanel buildCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 18, 0));
        centerPanel.setBackground(PAGE_BG);
        centerPanel.add(buildControlPanel());
        centerPanel.add(buildResultPanel());
        return centerPanel;
    }

    private JPanel buildControlPanel() {
        JPanel controlPanel = createPanel(PANEL_ALT, new BorderLayout(15, 15), new EmptyBorder(20, 20, 20, 20));

        JLabel panelHeading = new JLabel("Trip Controls");
        panelHeading.setForeground(TEXT_PRIMARY);
        panelHeading.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));

        JLabel panelCopy = new JLabel("Select your journey and compare route quality at a glance.");
        panelCopy.setForeground(TEXT_MUTED);
        panelCopy.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel headingPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        headingPanel.setOpaque(false);
        headingPanel.add(panelHeading);
        headingPanel.add(panelCopy);

        String[] stationNames = metroGraph.getAllStationNames();
        sourceDropdown = createStationDropdown(stationNames);
        destinationDropdown = createStationDropdown(stationNames);
        destinationDropdown.setSelectedIndex(Math.min(8, stationNames.length - 1));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 16, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        gbc.gridy = 0;
        formPanel.add(createSectionLabel("From"), gbc);
        gbc.gridy = 1;
        formPanel.add(sourceDropdown, gbc);

        gbc.gridy = 2;
        formPanel.add(createSectionLabel("To"), gbc);
        gbc.gridy = 3;
        formPanel.add(destinationDropdown, gbc);

        swapBtn = createButton("Swap Stations", new Color(255, 214, 102), new Color(36, 43, 62));
        findRouteBtn = createButton("Find Best Route", ACCENT, new Color(4, 18, 30));
        clearBtn = createButton("Reset", new Color(255, 107, 107), Color.WHITE);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(swapBtn);
        buttonPanel.add(findRouteBtn);
        buttonPanel.add(clearBtn);

        JPanel summaryGrid = new JPanel(new GridLayout(2, 2, 12, 12));
        summaryGrid.setOpaque(false);

        totalStopsValue = new JLabel();
        fareValue = new JLabel();
        timeValue = new JLabel();
        interchangeValue = new JLabel();

        summaryGrid.add(createMetricCard("Stops", totalStopsValue, new Color(109, 218, 255)));
        summaryGrid.add(createMetricCard("Fare", fareValue, new Color(255, 214, 102)));
        summaryGrid.add(createMetricCard("ETA", timeValue, SUCCESS));
        summaryGrid.add(createMetricCard("Interchanges", interchangeValue, new Color(255, 140, 105)));

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 16));
        bottomPanel.setOpaque(false);
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(summaryGrid, BorderLayout.CENTER);

        controlPanel.add(headingPanel, BorderLayout.NORTH);
        controlPanel.add(formPanel, BorderLayout.CENTER);
        controlPanel.add(bottomPanel, BorderLayout.SOUTH);

        return controlPanel;
    }

    private JPanel buildResultPanel() {
        JPanel resultPanel = createPanel(PANEL_BG, new BorderLayout(12, 12), new EmptyBorder(20, 20, 20, 20));

        JLabel resultTitle = new JLabel("Route Timeline");
        resultTitle.setForeground(TEXT_PRIMARY);
        resultTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));

        JLabel resultCopy = new JLabel("Every station, line, and interchange is called out clearly.");
        resultCopy.setForeground(TEXT_MUTED);
        resultCopy.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel headingPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        headingPanel.setOpaque(false);
        headingPanel.add(resultTitle);
        headingPanel.add(resultCopy);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setBackground(new Color(5, 12, 24));
        resultArea.setForeground(TEXT_PRIMARY);
        resultArea.setBorder(new CompoundBorder(
            new LineBorder(new Color(42, 69, 118), 1, true),
            new EmptyBorder(18, 18, 18, 18)
        ));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(5, 12, 24));

        resultPanel.add(headingPanel, BorderLayout.NORTH);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        return resultPanel;
    }

    private JPanel buildLegendPanel() {
        JPanel legendPanel = createPanel(new Color(11, 20, 37), new GridLayout(3, 1, 0, 8), new EmptyBorder(14, 16, 14, 16));
        legendPanel.setPreferredSize(new Dimension(220, 120));

        JLabel legendTitle = new JLabel("Live Line Legend");
        legendTitle.setForeground(TEXT_PRIMARY);
        legendTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        legendPanel.add(legendTitle);
        legendPanel.add(createLegendRow("Yellow Line", new Color(255, 214, 102)));
        legendPanel.add(createLegendRow("Blue Line", new Color(86, 166, 255)));

        return legendPanel;
    }

    private JComboBox<String> createStationDropdown(String[] stationNames) {
        JComboBox<String> dropdown = new JComboBox<>(stationNames);
        dropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dropdown.setBackground(new Color(244, 249, 255));
        dropdown.setBorder(new CompoundBorder(
            new LineBorder(new Color(72, 111, 173), 1, true),
            new EmptyBorder(6, 8, 6, 8)
        ));
        dropdown.setRenderer(new StationRenderer());
        return dropdown;
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_MUTED);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return label;
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 12, 10, 12));
        button.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(this);
        return button;
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = createPanel(new Color(10, 22, 42), new BorderLayout(0, 10), new EmptyBorder(14, 14, 14, 14));
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 140), 1, true),
            new EmptyBorder(0, 0, 0, 0)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(TEXT_MUTED);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        valueLabel.setForeground(accentColor);
        valueLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JLabel createBadge(String text, Color badgeColor) {
        JLabel badge = new JLabel(text);
        badge.setOpaque(true);
        badge.setBackground(new Color(badgeColor.getRed(), badgeColor.getGreen(), badgeColor.getBlue(), 30));
        badge.setForeground(badgeColor);
        badge.setBorder(new CompoundBorder(
            new LineBorder(new Color(badgeColor.getRed(), badgeColor.getGreen(), badgeColor.getBlue(), 120), 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return badge;
    }

    private JPanel createLegendRow(String lineName, Color lineColor) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row.setOpaque(false);

        JLabel swatch = new JLabel("  ");
        swatch.setOpaque(true);
        swatch.setBackground(lineColor);
        swatch.setPreferredSize(new Dimension(18, 18));

        JLabel nameLabel = new JLabel(lineName);
        nameLabel.setForeground(TEXT_MUTED);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        row.add(swatch);
        row.add(nameLabel);
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
            showErrorState("Select valid source and destination stations.");
            return;
        }

        if (sourceName.equals(destName)) {
            showErrorState("Source and destination cannot be the same station.");
            statusLabel.setText("Select two different stations to continue.");
            return;
        }

        try {
            Graph.RouteDetails details = metroGraph.findRouteDetails(sourceName, destName);
            updateSummaryCards(details);
            resultArea.setForeground(TEXT_PRIMARY);
            resultArea.setText(buildRouteNarrative(details));
            statusLabel.setText(
                "Route ready from " + details.getSourceName() + " to " + details.getDestinationName()
                + " | " + details.getStops() + " stops | Rs " + details.getFare()
            );
        } catch (InvalidStationException ex) {
            showErrorState(ex.getMessage());
            statusLabel.setText("Unable to calculate route right now.");
        }
    }

    private String buildRouteNarrative(Graph.RouteDetails details) {
        StringBuilder sb = new StringBuilder();
        sb.append("BEST ROUTE SELECTED\n");
        sb.append("--------------------------------------------------\n");
        sb.append("FROM        : ").append(details.getSourceName()).append("\n");
        sb.append("TO          : ").append(details.getDestinationName()).append("\n");
        sb.append("FARE        : Rs ").append(details.getFare()).append("\n");
        sb.append("ETA         : ").append(details.getEstimatedMinutes()).append(" min\n");
        sb.append("INTERCHANGE : ").append(details.getInterchanges().size()).append("\n");
        sb.append("--------------------------------------------------\n\n");
        sb.append("STATION-BY-STATION TIMELINE\n\n");

        List<Integer> path = details.getPath();
        for (int i = 0; i < path.size(); i++) {
            String stationName = metroGraph.getStationName(path.get(i));
            String lineName = metroGraph.getStationLine(path.get(i));
            String marker = "o";

            if (i == 0) {
                marker = "START";
            } else if (i == path.size() - 1) {
                marker = "END";
            } else {
                String previousLine = metroGraph.getStationLine(path.get(i - 1));
                if (!previousLine.equals(lineName)) {
                    marker = "CHANGE";
                }
            }

            sb.append(String.format("%-7s  %s [%s Line]%n", marker, stationName, lineName));
        }

        if (!details.getInterchanges().isEmpty()) {
            sb.append("\nINTERCHANGE NOTES\n");
            for (String interchange : details.getInterchanges()) {
                sb.append("- ").append(interchange).append("\n");
            }
        } else {
            sb.append("\nNo interchanges needed for this journey.\n");
        }

        sb.append("\nStandout idea: add a mini metro map screenshot or animated route drawing next if you want this to feel even more project-showcase ready.");
        return sb.toString();
    }

    private void updateSummaryCards(Graph.RouteDetails details) {
        totalStopsValue.setText(String.valueOf(details.getStops()));
        fareValue.setText("Rs " + details.getFare());
        timeValue.setText(details.getEstimatedMinutes() + " min");
        interchangeValue.setText(String.valueOf(details.getInterchanges().size()));
    }

    private void showErrorState(String message) {
        resultArea.setForeground(WARNING);
        resultArea.setText("ROUTE UNAVAILABLE\n-----------------\n" + message);
        resetSummaryCards();
    }

    private void clearResult() {
        sourceDropdown.setSelectedIndex(0);
        destinationDropdown.setSelectedIndex(Math.min(8, destinationDropdown.getItemCount() - 1));
        resetSummaryCards();
        showWelcomeMessage();
        statusLabel.setText("Selections reset. Ready for a new search.");
    }

    private void swapStations() {
        Object source = sourceDropdown.getSelectedItem();
        Object destination = destinationDropdown.getSelectedItem();
        sourceDropdown.setSelectedItem(destination);
        destinationDropdown.setSelectedItem(source);
        statusLabel.setText("Stations swapped. You can search the reverse trip now.");
    }

    private void resetSummaryCards() {
        totalStopsValue.setText("--");
        fareValue.setText("--");
        timeValue.setText("--");
        interchangeValue.setText("--");
    }

    private void showWelcomeMessage() {
        resultArea.setForeground(TEXT_MUTED);
        resultArea.setText(
            "Pick two stations and press 'Find Best Route' to see a cleaner metro dashboard.\n\n"
                + "What already makes this version stronger:\n"
                + "- premium layout instead of a plain form\n"
                + "- quick route metrics up front\n"
                + "- clearer interchange callouts\n"
                + "- reverse-trip swap button\n\n"
                + "If you want, we can go one level higher next with a visual metro map panel."
        );
    }

    private class StationRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof String stationName) {
                String line = metroGraph.getStationLineByName(stationName);
                label.setText(stationName + "  |  " + line + " Line");
                if (!isSelected) {
                    label.setBackground(Color.WHITE);
                    label.setForeground("Yellow".equals(line) ? new Color(135, 100, 0) : new Color(15, 72, 150));
                }
            }
            return label;
        }
    }
}
