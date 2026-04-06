// MetroGUI.java
// Java Swing GUI for Delhi Metro Route Finder

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MetroGUI extends JFrame implements ActionListener {

    private static final Color PAGE_BG = new Color(8, 15, 29);
    private static final Color PANEL_BG = new Color(18, 31, 56);
    private static final Color PANEL_SOFT = new Color(23, 39, 69);
    private static final Color SURFACE = new Color(244, 247, 252);
    private static final Color ACCENT = new Color(43, 199, 255);
    private static final Color ACCENT_WARM = new Color(255, 205, 94);
    private static final Color TEXT_PRIMARY = new Color(244, 247, 252);
    private static final Color TEXT_MUTED = new Color(171, 188, 213);
    private static final Color TEXT_DARK = new Color(30, 43, 64);
    private static final Color SUCCESS = new Color(120, 238, 176);
    private static final Color WARNING = new Color(255, 124, 107);

    private JComboBox<String> sourceDropdown;
    private JComboBox<String> destinationDropdown;
    private JButton findRouteBtn;
    private JButton clearBtn;
    private JButton swapBtn;
    private JEditorPane resultPane;
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
        setSize(1120, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(PAGE_BG);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout(22, 24));
        contentPanel.setBackground(PAGE_BG);
        contentPanel.setBorder(new EmptyBorder(22, 22, 18, 22));

        contentPanel.add(buildHeroPanel(), BorderLayout.NORTH);
        contentPanel.add(buildBodyPanel(), BorderLayout.CENTER);

        statusLabel = new JLabel("Ready to plan a smoother trip across " + metroGraph.getTotalStations() + " stations.");
        statusLabel.setForeground(TEXT_MUTED);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setBorder(new EmptyBorder(0, 24, 16, 24));

        add(contentPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        resetSummaryCards();
        showWelcomeMessage();
        setVisible(true);
    }

    private JPanel buildHeroPanel() {
        JPanel heroPanel = createPanel(PANEL_BG, new BorderLayout(20, 0), new EmptyBorder(26, 28, 26, 28));

        JLabel titleLabel = new JLabel("Delhi Metro Navigator");
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 34));

        JLabel subtitleLabel = new JLabel("Find routes with more breathing room, clearer hierarchy, and smarter trip context.");
        subtitleLabel.setForeground(TEXT_MUTED);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        badgePanel.setOpaque(false);
        badgePanel.add(createBadge("Smooth Layout", ACCENT_WARM));
        badgePanel.add(createBadge("Live Fare + ETA", SUCCESS));
        badgePanel.add(createBadge("Interchange Guidance", ACCENT));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        badgePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(14));
        textPanel.add(subtitleLabel);
        textPanel.add(Box.createVerticalStrut(22));
        textPanel.add(badgePanel);

        heroPanel.add(textPanel, BorderLayout.CENTER);
        heroPanel.add(buildLegendPanel(), BorderLayout.EAST);
        return heroPanel;
    }

    private JPanel buildBodyPanel() {
        JPanel bodyPanel = new JPanel(new GridLayout(1, 2, 22, 0));
        bodyPanel.setBackground(PAGE_BG);
        bodyPanel.add(buildControlPanel());
        bodyPanel.add(buildResultPanel());
        return bodyPanel;
    }

    private JPanel buildControlPanel() {
        JPanel controlPanel = createPanel(PANEL_SOFT, new BorderLayout(), new EmptyBorder(28, 26, 26, 26));

        JPanel stack = new JPanel();
        stack.setOpaque(false);
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));

        JLabel panelHeading = new JLabel("Trip Controls");
        panelHeading.setForeground(TEXT_PRIMARY);
        panelHeading.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));

        JLabel panelCopy = new JLabel("Choose a start and destination, then compare the trip summary before you travel.");
        panelCopy.setForeground(TEXT_MUTED);
        panelCopy.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        String[] stationNames = metroGraph.getAllStationNames();
        sourceDropdown = createStationDropdown(stationNames);
        destinationDropdown = createStationDropdown(stationNames);
        destinationDropdown.setSelectedIndex(Math.min(8, stationNames.length - 1));

        JPanel buttons = new JPanel(new GridLayout(1, 3, 12, 0));
        buttons.setOpaque(false);
        swapBtn = createButton("Swap", new Color(250, 240, 211), TEXT_DARK);
        findRouteBtn = createButton("Find Route", ACCENT, new Color(7, 22, 36));
        clearBtn = createButton("Reset", new Color(255, 232, 228), new Color(136, 42, 36));
        buttons.add(swapBtn);
        buttons.add(findRouteBtn);
        buttons.add(clearBtn);

        JPanel summaryGrid = new JPanel(new GridLayout(2, 2, 14, 14));
        summaryGrid.setOpaque(false);
        totalStopsValue = new JLabel();
        fareValue = new JLabel();
        timeValue = new JLabel();
        interchangeValue = new JLabel();
        summaryGrid.add(createMetricCard("Stops", totalStopsValue, new Color(103, 214, 255)));
        summaryGrid.add(createMetricCard("Fare", fareValue, ACCENT_WARM));
        summaryGrid.add(createMetricCard("ETA", timeValue, SUCCESS));
        summaryGrid.add(createMetricCard("Interchanges", interchangeValue, WARNING));

        stack.add(panelHeading);
        stack.add(Box.createVerticalStrut(10));
        stack.add(panelCopy);
        stack.add(Box.createVerticalStrut(28));
        stack.add(createFieldBlock("From", sourceDropdown));
        stack.add(Box.createVerticalStrut(18));
        stack.add(createFieldBlock("To", destinationDropdown));
        stack.add(Box.createVerticalStrut(24));
        stack.add(buttons);
        stack.add(Box.createVerticalStrut(26));
        stack.add(summaryGrid);

        controlPanel.add(stack, BorderLayout.NORTH);
        return controlPanel;
    }

    private JPanel buildResultPanel() {
        JPanel resultPanel = createPanel(PANEL_BG, new BorderLayout(0, 18), new EmptyBorder(28, 26, 26, 26));

        JLabel resultTitle = new JLabel("Journey Overview");
        resultTitle.setForeground(TEXT_PRIMARY);
        resultTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));

        JLabel resultCopy = new JLabel("A softer, card-based route view that highlights the important parts first.");
        resultCopy.setForeground(TEXT_MUTED);
        resultCopy.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel headingPanel = new JPanel();
        headingPanel.setOpaque(false);
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.Y_AXIS));
        resultTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultCopy.setAlignmentX(Component.LEFT_ALIGNMENT);
        headingPanel.add(resultTitle);
        headingPanel.add(Box.createVerticalStrut(10));
        headingPanel.add(resultCopy);

        resultPane = new JEditorPane();
        resultPane.setEditable(false);
        resultPane.setContentType("text/html");
        resultPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        resultPane.setBackground(SURFACE);
        resultPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(resultPane);
        scrollPane.setBorder(new CompoundBorder(
            new LineBorder(new Color(221, 229, 240), 1, true),
            new EmptyBorder(0, 0, 0, 0)
        ));
        scrollPane.getViewport().setBackground(SURFACE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        resultPanel.add(headingPanel, BorderLayout.NORTH);
        resultPanel.add(scrollPane, BorderLayout.CENTER);
        return resultPanel;
    }

    private JPanel buildLegendPanel() {
        JPanel legendPanel = createPanel(new Color(12, 23, 42), new GridLayout(5, 1, 0, 10), new EmptyBorder(18, 20, 18, 20));
        legendPanel.setPreferredSize(new Dimension(240, 190));

        JLabel legendTitle = new JLabel("Line Legend");
        legendTitle.setForeground(TEXT_PRIMARY);
        legendTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
        legendPanel.add(legendTitle);
        legendPanel.add(createLegendRow("Yellow Line", new Color(255, 214, 102)));
        legendPanel.add(createLegendRow("Blue Line", new Color(86, 166, 255)));
        legendPanel.add(createLegendRow("Pink Line", new Color(255, 120, 182)));
        legendPanel.add(createLegendRow("Green Line", new Color(80, 201, 120)));
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
        dropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        dropdown.setPreferredSize(new Dimension(300, 54));
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

    private JPanel createMetricCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = createPanel(new Color(12, 26, 47), new BorderLayout(0, 14), new EmptyBorder(16, 16, 18, 16));
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 110), 1, true),
            new EmptyBorder(0, 0, 0, 0)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(TEXT_MUTED);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        valueLabel.setForeground(accentColor);
        valueLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));

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
            new LineBorder(new Color(badgeColor.getRed(), badgeColor.getGreen(), badgeColor.getBlue(), 100), 1, true),
            new EmptyBorder(8, 14, 8, 14)
        ));
        badge.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        return badge;
    }

    private JPanel createLegendRow(String lineName, Color lineColor) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);

        JLabel swatch = new JLabel(" ");
        swatch.setOpaque(true);
        swatch.setBackground(lineColor);
        swatch.setPreferredSize(new Dimension(24, 24));

        JLabel nameLabel = new JLabel(lineName);
        nameLabel.setForeground(TEXT_MUTED);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

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
            resultPane.setText(buildRouteHtml(details));
            resultPane.setCaretPosition(0);
            statusLabel.setText(
                "Route ready from " + details.getSourceName() + " to " + details.getDestinationName()
                    + " | " + details.getStops() + " stops | Rs " + details.getFare()
            );
        } catch (InvalidStationException ex) {
            showErrorState(ex.getMessage());
            statusLabel.setText("Unable to calculate route right now.");
        }
    }

    private String buildRouteHtml(Graph.RouteDetails details) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='margin:0; font-family:Segoe UI; background:#f4f7fc; color:#1f2b3d;'>");
        sb.append("<div style='padding:24px 24px 14px 24px;'>");
        sb.append("<div style='font-size:12px; letter-spacing:1.2px; color:#6a7a96; font-weight:700;'>BEST ROUTE SELECTED</div>");
        sb.append("<div style='margin-top:10px; font-size:26px; font-weight:700;'>")
            .append(escapeHtml(details.getSourceName())).append(" to ")
            .append(escapeHtml(details.getDestinationName())).append("</div>");
        sb.append("<div style='margin-top:8px; font-size:14px; color:#5f6f89;'>A clearer trip summary with route depth, time estimate, and interchange guidance.</div>");
        sb.append("</div>");

        sb.append("<div style='padding:0 24px 20px 24px;'>");
        sb.append("<table style='width:100%; border-collapse:separate; border-spacing:0 12px;'>");
        appendSummaryRow(sb, "Fare", "Rs " + details.getFare());
        appendSummaryRow(sb, "Estimated Time", details.getEstimatedMinutes() + " min");
        appendSummaryRow(sb, "Stops", String.valueOf(details.getStops()));
        appendSummaryRow(sb, "Interchanges", String.valueOf(details.getInterchanges().size()));
        sb.append("</table></div>");

        sb.append("<div style='margin:0 24px; height:1px; background:#dde5f0;'></div>");
        sb.append("<div style='padding:20px 24px 26px 24px;'>");
        sb.append("<div style='font-size:18px; font-weight:700; margin-bottom:16px;'>Station Timeline</div>");

        List<Integer> path = details.getPath();
        for (int i = 0; i < path.size(); i++) {
            String stationName = metroGraph.getStationName(path.get(i));
            String lineName = metroGraph.getStationLine(path.get(i));
            String tagText = "Stop";
            String tagBg = "#e8f2ff";
            String tagFg = "#2f75d6";

            if (i == 0) {
                tagText = "Start";
                tagBg = "#e7fff2";
                tagFg = "#1d9c5f";
            } else if (i == path.size() - 1) {
                tagText = "End";
                tagBg = "#fff0ec";
                tagFg = "#d6594b";
            } else {
                String previousLine = metroGraph.getStationLine(path.get(i - 1));
                if (!previousLine.equals(lineName)) {
                    tagText = "Change";
                    tagBg = "#fff8e3";
                    tagFg = "#a97900";
                }
            }

            sb.append("<div style='display:flex; align-items:flex-start; gap:14px; padding:12px 0;'>");
            sb.append("<div style='min-width:70px; text-align:center; padding:6px 10px; border-radius:999px; background:")
                .append(tagBg).append("; color:").append(tagFg).append("; font-size:12px; font-weight:700;'>")
                .append(tagText).append("</div>");
            sb.append("<div>");
            sb.append("<div style='font-size:16px; font-weight:600;'>").append(escapeHtml(stationName)).append("</div>");
            sb.append("<div style='margin-top:4px; font-size:13px; color:#697a96;'>").append(escapeHtml(lineName)).append(" Line</div>");
            sb.append("</div></div>");
        }

        if (!details.getInterchanges().isEmpty()) {
            sb.append("<div style='margin-top:18px; padding:16px 18px; background:#ffffff; border:1px solid #e1e8f2; border-radius:12px;'>");
            sb.append("<div style='font-size:15px; font-weight:700; margin-bottom:10px;'>Interchange Notes</div>");
            for (String interchange : details.getInterchanges()) {
                sb.append("<div style='font-size:13px; color:#596985; margin-bottom:6px;'>&bull; ")
                    .append(escapeHtml(interchange)).append("</div>");
            }
            sb.append("</div>");
        }

        sb.append("</div></body></html>");
        return sb.toString();
    }

    private void appendSummaryRow(StringBuilder sb, String label, String value) {
        sb.append("<tr>");
        sb.append("<td style='width:48%; background:#ffffff; border:1px solid #e1e8f2; border-radius:12px; padding:14px 16px;'>");
        sb.append("<div style='font-size:12px; color:#71819d;'>").append(label).append("</div>");
        sb.append("<div style='margin-top:5px; font-size:20px; font-weight:700; color:#1f2b3d;'>").append(value).append("</div>");
        sb.append("</td>");
        sb.append("</tr>");
    }

    private void updateSummaryCards(Graph.RouteDetails details) {
        totalStopsValue.setText(String.valueOf(details.getStops()));
        fareValue.setText("Rs " + details.getFare());
        timeValue.setText(details.getEstimatedMinutes() + " min");
        interchangeValue.setText(String.valueOf(details.getInterchanges().size()));
    }

    private void showErrorState(String message) {
        resultPane.setText(
            "<html><body style='margin:0; font-family:Segoe UI; background:#fdf5f3; color:#7b3029;'>"
                + "<div style='padding:24px;'>"
                + "<div style='font-size:12px; font-weight:700; letter-spacing:1px;'>ROUTE UNAVAILABLE</div>"
                + "<div style='margin-top:12px; font-size:22px; font-weight:700;'>We could not build that trip</div>"
                + "<div style='margin-top:8px; font-size:14px; color:#91534c;'>"
                + escapeHtml(message)
                + "</div></div></body></html>"
        );
        resultPane.setCaretPosition(0);
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
        resultPane.setText(
            "<html><body style='margin:0; font-family:Segoe UI; background:#f4f7fc; color:#1f2b3d;'>"
                + "<div style='padding:24px 24px 10px 24px;'>"
                + "<div style='font-size:12px; letter-spacing:1.2px; color:#6a7a96; font-weight:700;'>WELCOME</div>"
                + "<div style='margin-top:10px; font-size:26px; font-weight:700;'>Plan a cleaner metro trip</div>"
                + "<div style='margin-top:10px; font-size:14px; color:#5f6f89;'>This version uses softer spacing, larger controls, and a more readable route card.</div>"
                + "</div>"
                + "<div style='padding:0 24px 24px 24px;'>"
                + "<div style='padding:16px 18px; background:#ffffff; border:1px solid #e1e8f2; border-radius:12px;'>"
                + "<div style='font-size:15px; font-weight:700; margin-bottom:10px;'>What improved</div>"
                + "<div style='font-size:13px; color:#596985; margin-bottom:6px;'>&bull; more breathing room between sections</div>"
                + "<div style='font-size:13px; color:#596985; margin-bottom:6px;'>&bull; less cramped buttons and dropdowns</div>"
                + "<div style='font-size:13px; color:#596985; margin-bottom:6px;'>&bull; route details shown as a designed card instead of console text</div>"
                + "<div style='font-size:13px; color:#596985;'>&bull; better visual hierarchy for the important trip facts</div>"
                + "</div></div></body></html>"
        );
        resultPane.setCaretPosition(0);
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
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
                    label.setForeground(getLineColor(line));
                }
            }
            return label;
        }
    }

    private Color getLineColor(String line) {
        switch (line) {
            case "Yellow":
                return new Color(135, 100, 0);
            case "Blue":
                return new Color(15, 72, 150);
            case "Pink":
                return new Color(186, 45, 120);
            case "Green":
                return new Color(31, 133, 66);
            default:
                return TEXT_DARK;
        }
    }
}
