import kliving.gui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * K-Living Solution — entry point.
 * Run from the project root so that the data/ folder is created there.
 */
public class Main {
    public static void main(String[] args) {
        // Apply system look-and-feel for native widget rendering
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(MainFrame::new);
    }
}
