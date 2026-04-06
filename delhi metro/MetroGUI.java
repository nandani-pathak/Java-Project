// MetroGUI.java
// Java Swing GUI for Delhi Metro Route Finder

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MetroGUI extends JFrame implements ActionListener {

    // GUI Components
    private JComboBox<String> sourceDropdown;
    private JComboBox<String> destinationDropdown;
    private JButton findRouteBtn;
    private JButton clearBtn;
    private JTextArea resultArea;
    private JLabel titleLabel;
    private JLabel statusLabel;

    // Graph object
    private Graph metroGraph;

    public MetroGUI() {
        metroGraph = new Graph();
        initializeGUI();
    }

    private void initializeGUI() {
        // Frame settings
        setTitle("Delhi Metro Route Finder");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(15, 32, 65));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- TITLE PANEL ---
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(15, 32, 65));

        titleLabel = new JLabel("🚇 Delhi Metro Route Finder");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 200, 255));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Find shortest route & fare between any two stations");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(180, 180, 180));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titlePanel.setLayout(new GridLayout(2, 1, 0, 5));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        // --- INPUT PANEL ---
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(25, 50, 100));
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 200, 255), 1),
            "Select Stations",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 13),
            new Color(0, 200, 255)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Source label
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel sourceLabel = new JLabel("Source Station:");
        sourceLabel.setForeground(Color.WHITE);
        sourceLabel.setFont(new Font("Arial", Font.BOLD, 13));
        inputPanel.add(sourceLabel, gbc);

        // Source dropdown
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        String[] stationNames = metroGraph.getAllStationNames();
        sourceDropdown = new JComboBox<>(stationNames);
        sourceDropdown.setFont(new Font("Arial", Font.PLAIN, 12));
        sourceDropdown.setBackground(Color.WHITE);
        inputPanel.add(sourceDropdown, gbc);

        // Destination label
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel destLabel = new JLabel("Destination Station:");
        destLabel.setForeground(Color.WHITE);
        destLabel.setFont(new Font("Arial", Font.BOLD, 13));
        inputPanel.add(destLabel, gbc);

        // Destination dropdown
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        destinationDropdown = new JComboBox<>(stationNames);
        destinationDropdown.setFont(new Font("Arial", Font.PLAIN, 12));
        destinationDropdown.setBackground(Color.WHITE);
        destinationDropdown.setSelectedIndex(5);
        inputPanel.add(destinationDropdown, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBackground(new Color(25, 50, 100));

        findRouteBtn = new JButton("🔍 Find Route");
        findRouteBtn.setFont(new Font("Arial", Font.BOLD, 14));
        findRouteBtn.setBackground(new Color(0, 150, 255));
        findRouteBtn.setForeground(Color.WHITE);
        findRouteBtn.setFocusPainted(false);
        findRouteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        findRouteBtn.setPreferredSize(new Dimension(160, 40));
        findRouteBtn.addActionListener(this);

        clearBtn = new JButton("🗑 Clear");
        clearBtn.setFont(new Font("Arial", Font.BOLD, 14));
        clearBtn.setBackground(new Color(200, 50, 50));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFocusPainted(false);
        clearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearBtn.setPreferredSize(new Dimension(120, 40));
        clearBtn.addActionListener(this);

        btnPanel.add(findRouteBtn);
        btnPanel.add(clearBtn);
        inputPanel.add(btnPanel, gbc);

        // --- RESULT PANEL ---
        JPanel resultPanel = new JPanel(new BorderLayout(5, 5));
        resultPanel.setBackground(new Color(15, 32, 65));

        JLabel resultLabel = new JLabel("Route Result:");
        resultLabel.setForeground(new Color(0, 200, 255));
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));

        resultArea = new JTextArea();
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultArea.setEditable(false);
        resultArea.setBackground(new Color(10, 20, 45));
        resultArea.setForeground(new Color(200, 255, 200));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        resultArea.setText("Select source and destination stations, then click 'Find Route'.");

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 255), 1));
        scrollPane.setPreferredSize(new Dimension(650, 200));

        resultPanel.add(resultLabel, BorderLayout.NORTH);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        // --- STATUS BAR ---
        statusLabel = new JLabel("Ready | Total Stations: " + metroGraph.getTotalStations());
        statusLabel.setForeground(new Color(150, 150, 150));
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        statusLabel.setBorder(new EmptyBorder(5, 0, 0, 0));

        // Add all panels to main
        mainPanel.add(titlePanel,  BorderLayout.NORTH);
        mainPanel.add(inputPanel,  BorderLayout.CENTER);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);

        add(mainPanel);
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == findRouteBtn) {
            findRoute();
        } else if (e.getSource() == clearBtn) {
            clearResult();
        }
    }

    private void findRoute() {
        String sourceName = (String) sourceDropdown.getSelectedItem();
        String destName   = (String) destinationDropdown.getSelectedItem();

        // Validation
        if (sourceName.equals(destName)) {
            resultArea.setForeground(new Color(255, 100, 100));
            resultArea.setText("⚠️ Source and destination cannot be the same station!");
            statusLabel.setText("Error: Same station selected.");
            return;
        }

        try {
            int sourceId = metroGraph.getStationId(sourceName);
            int destId   = metroGraph.getStationId(destName);

            if (sourceId == -1 || destId == -1) {
                throw new InvalidStationException("Station not found in the network.");
            }

            // Run Dijkstra
            int[][] result = metroGraph.dijkstra(sourceId);
            int[] dist = result[0];
            int[] prev = result[1];

            // Check if reachable
            if (dist[destId] == Integer.MAX_VALUE) {
                resultArea.setForeground(new Color(255, 100, 100));
                resultArea.setText("❌ No route found between selected stations.");
                return;
            }

            // Get path
            List<Integer> path = metroGraph.getPath(prev, destId);
            int stops = path.size() - 1;
            int fare  = metroGraph.calculateFare(stops);

            // Build result string
            StringBuilder sb = new StringBuilder();
            sb.append("✅ ROUTE FOUND!\n");
            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
            sb.append("📍 From  : ").append(sourceName).append("\n");
            sb.append("🏁 To    : ").append(destName).append("\n\n");
            sb.append("🛤️  ROUTE :\n");

            for (int i = 0; i < path.size(); i++) {
                String stName = metroGraph.getStationName(path.get(i));
                String line   = metroGraph.getStationLine(path.get(i));
                if (i == 0) {
                    sb.append("   🟢 ").append(stName).append(" [").append(line).append(" Line] (START)\n");
                } else if (i == path.size() - 1) {
                    sb.append("   🔴 ").append(stName).append(" [").append(line).append(" Line] (END)\n");
                } else {
                    sb.append("   🔵 ").append(stName).append(" [").append(line).append(" Line]\n");
                }
            }

            sb.append("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            sb.append("🚉 Total Stops  : ").append(stops).append("\n");
            sb.append("💰 Fare         : ₹").append(fare).append("\n");
            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

            resultArea.setForeground(new Color(200, 255, 200));
            resultArea.setText(sb.toString());
            statusLabel.setText("Route found! Stops: " + stops + " | Fare: ₹" + fare);

        } catch (InvalidStationException ex) {
            resultArea.setForeground(new Color(255, 100, 100));
            resultArea.setText("⚠️ Error: " + ex.getMessage());
            statusLabel.setText("Error: Invalid station.");
        }
    }

    private void clearResult() {
        resultArea.setForeground(new Color(200, 255, 200));
        resultArea.setText("Select source and destination stations, then click 'Find Route'.");
        sourceDropdown.setSelectedIndex(0);
        destinationDropdown.setSelectedIndex(5);
        statusLabel.setText("Ready | Total Stations: " + metroGraph.getTotalStations());
    }
}
