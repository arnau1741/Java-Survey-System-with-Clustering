package prop.enquestes.presentacio;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Classe auxiliar per a la gestió i estilització de la interfície d'usuari.
 * <p>
 * Aquesta classe proporciona constants i mètodes estàtics per a la configuració
 * estètica dels components Swing de l'aplicació. Inclou definicions de fonts,
 * colors, borders i mètodes factory per crear components estilitzats.
 * </p>
 */
public class UIHelper {

    // Fonts
    /**
     * Font per als títols principals de les finestres.
     */
    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 18);
    /**
     * Font per als subtítols i encapçalaments de secció.
     */
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.BOLD, 14);
    /**
     * Font per al text normal i contingut principal.
     */
    public static final Font FONT_NORMAL = new Font("SansSerif", Font.PLAIN, 12);
    /**
     * Font per al text petit, notes o informació secundària.
     */
    public static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 10);

    // Colors
    /**
     * Color primari de l'aplicació (blau acer).
     */
    private static final Color COLOR_PRIMARY = new Color(60, 130, 200); // Steel Blue
    /**
     * Color de fons predeterminat per a les finestres i diàlegs.
     */
    public static final Color COLOR_BACKGROUND = new Color(245, 245, 245); // Light Gray
    /**
     * Color de text per als encapçalaments i textos destacats.
     */
    public static final Color COLOR_TEXT_HEADER = new Color(50, 50, 50);

    // Borders
    /**
     * Border amb marge principal (20 píxels per tots els costats).
     */
    public static final Border PADDING_MAIN = BorderFactory.createEmptyBorder(20, 20, 20, 20);
    /**
     * Border amb marge petit (5 píxels per tots els costats).
     */
    public static final Border PADDING_SMALL = BorderFactory.createEmptyBorder(5, 5, 5, 5);

    /**
     * Constructor privat per evitar la instanciació d'aquesta classe d'utilitat.
     */
    private UIHelper() {
        // Utility class
    }

    /**
     * Configura un diàleg amb els valors predeterminats de l'aplicació.
     *
     * @param dialog El diàleg a configurar.
     * @param title  El títol que es mostrarà al diàleg.
     */
    public static void configureDialog(JDialog dialog, String title) {
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(COLOR_BACKGROUND);
    }

    /**
     * Configura una finestra principal amb els valors predeterminats de l'aplicació.
     *
     * @param frame La finestra a configurar.
     * @param title El títol que es mostrarà a la finestra.
     */
    public static void configureFrame(JFrame frame, String title) {
        frame.setTitle(title);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(COLOR_BACKGROUND);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Or dispose
    }

    /**
     * Mostra un diàleg d'error amb un missatge específic.
     *
     * @param parent  El component pare sobre el qual es mostrarà el diàleg.
     * @param message El missatge d'error a mostrar.
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Mostra un diàleg d'informació amb un missatge específic.
     *
     * @param parent  El component pare sobre el qual es mostrarà el diàleg.
     * @param message El missatge d'informació a mostrar.
     */
    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Informació", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostra un diàleg d'advertència amb un missatge específic.
     *
     * @param parent  El component pare sobre el qual es mostrarà el diàleg.
     * @param message El missatge d'advertència a mostrar.
     */
    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Atenció", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Mostra un diàleg de confirmació amb opcions Sí/No.
     *
     * @param parent  El component pare sobre el qual es mostrarà el diàleg.
     * @param message El missatge de confirmació a mostrar.
     * @return {@code true} si l'usuari ha seleccionat "Sí", {@code false} si ha seleccionat "No".
     */
    public static boolean showConfirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Confirmació", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    // Components Factory
    /**
     * Crea una etiqueta de títol estilitzada amb la font i color corresponents.
     *
     * @param text El text que es mostrarà a l'etiqueta.
     * @return Una instància de JLabel configurada com a títol.
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(COLOR_TEXT_HEADER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    /**
     * Crea una etiqueta de subtítol estilitzada amb la font i color corresponents.
     *
     * @param text El text que es mostrarà a l'etiqueta.
     * @return Una instància de JLabel configurada com a subtítol.
     */
    public static JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SUBTITLE);
        label.setForeground(COLOR_TEXT_HEADER);
        return label;
    }

    /**
     * Crea un botó estilitzat amb l'alineació centrada i l'actionListener proporcionat.
     *
     * @param text   El text que es mostrarà al botó.
     * @param action L'actionListener que gestionarà l'esdeveniment de clic al botó.
     * @return Una instància de JButton configurada.
     */
    public static JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(action);
        // Helper to make buttons look slightly better could be added here
        return btn;
    }

    /**
     * Aplica estils addicionals a un botó existent.
     * <p>
     * Actualment aplica la font normal al botó. Aquest mètode pot ser ampliat
     * per a afegir més personalització.
     * </p>
     *
     * @param btn El botó al qual aplicar els estils.
     */
    public static void styleButton(JButton btn) {
        // Optional styling
        btn.setFont(FONT_NORMAL);
    }
}
