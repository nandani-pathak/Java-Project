// Main.java
// Entry point for Delhi Metro Route Finder

public class Main {
    public static void main(String[] args) {
        // Launch GUI on Event Dispatch Thread (good Swing practice)
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MetroGUI();
        });
    }
}
