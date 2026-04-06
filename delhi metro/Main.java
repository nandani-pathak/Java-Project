// Main.java
// Entry point for Delhi Metro Route Finder

import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Falls back to the default theme if the system look and feel is unavailable.
            }
            new MetroGUI();
        });
    }
}
