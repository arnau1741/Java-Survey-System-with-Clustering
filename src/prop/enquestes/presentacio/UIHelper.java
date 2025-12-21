package prop.enquestes.presentacio;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UIHelper {

    // Fonts
    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_NORMAL = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 10);

    // Colors
    private static final Color COLOR_PRIMARY = new Color(60, 130, 200); // Steel Blue
    public static final Color COLOR_BACKGROUND = new Color(245, 245, 245); // Light Gray
    public static final Color COLOR_TEXT_HEADER = new Color(50, 50, 50);

    // Borders
    public static final Border PADDING_MAIN = BorderFactory.createEmptyBorder(20, 20, 20, 20);
    public static final Border PADDING_SMALL = BorderFactory.createEmptyBorder(5, 5, 5, 5);

    private UIHelper() {
        // Utility class
    }

    public static void configureDialog(JDialog dialog, String title) {
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(COLOR_BACKGROUND);
    }

    public static void configureFrame(JFrame frame, String title) {
        frame.setTitle(title);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(COLOR_BACKGROUND);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Or dispose
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Informació", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Atenció", JOptionPane.WARNING_MESSAGE);
    }

    public static boolean showConfirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Confirmació", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    // Components Factory
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(COLOR_TEXT_HEADER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public static JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SUBTITLE);
        label.setForeground(COLOR_TEXT_HEADER);
        return label;
    }

    public static JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(action);
        // Helper to make buttons look slightly better could be added here
        return btn;
    }

    public static void styleButton(JButton btn) {
        // Optional styling
        btn.setFont(FONT_NORMAL);
    }
}
