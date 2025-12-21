package prop.enquestes.presentacio;

import prop.enquestes.excepcions.EnquestaNoExisteixException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Diàleg per consultar la informació detallada de les enquestes del sistema.
 * <p>
 * Aquesta vista permet a l'usuari seleccionar una enquesta d'una llista desplegable
 * i visualitzar-ne tot el contingut (preguntes, opcions, etc.) en una àrea de text.
 * </p>
 */
public class VistaConsultarEnquesta extends JDialog {

    private CtrlPresentacio ctrlPresentacio;
    private JPanel contentPane = new JPanel();
    private JEditorPane resultat;
    private JButton buttonOK =  new JButton("OK");
    private JButton buttonCancel =  new JButton("Cancel");
    private JComboBox<String> comboEnquestes;

    /**
     * Constructor del diàleg de consulta d'enquestes.
     * Configura la interfície, carrega les dades inicials i assigna els controladors d'esdeveniments.
     *
     * @param ctrlPresentacio Referència al controlador de presentació per obtenir les dades.
     */
    public VistaConsultarEnquesta(CtrlPresentacio ctrlPresentacio) {
        super((Frame) null, "Consultar Enquesta", true);
        this.ctrlPresentacio = ctrlPresentacio;

        UIHelper.configureDialog(this, "Consultar Enquesta");
        setSize(700, 600);
        setLocationRelativeTo(null);
        inicialitzarComponents();
        carregarEnquestes();
        configurarListeners();

    }

    /**
     * Inicialitza i distribueix els components gràfics del diàleg.
     * Crea el panell de selecció superior, l'àrea de text central i els botons inferiors.
     */
    private void inicialitzarComponents() {
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBackground(UIHelper.COLOR_BACKGROUND);
        contentPane.setBorder(UIHelper.PADDING_MAIN);
        setContentPane(contentPane);

        // TOP: Selector
        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelSeleccion.setBackground(UIHelper.COLOR_BACKGROUND);
        panelSeleccion.add(new JLabel("Enquesta:"));

        comboEnquestes = new JComboBox<>();
        comboEnquestes.setPreferredSize(new Dimension(300, 25));
        panelSeleccion.add(comboEnquestes);

        contentPane.add(panelSeleccion, BorderLayout.NORTH);

        // ========== RESULTAT (CENTER) ==========
        resultat = new JEditorPane();
        resultat.setContentType("text/html");
        resultat.setEditable(false);
        resultat.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        resultat.setFont(UIHelper.FONT_NORMAL);
        JScrollPane scroll = new JScrollPane(resultat);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        contentPane.add(scroll, BorderLayout.CENTER);

        // ========== BOTONS (BOTTOM) ==========
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        buttonOK = new JButton("Consultar");
        buttonCancel = new JButton("Tancar");

        bottom.add(buttonOK);
        bottom.add(buttonCancel);

        contentPane.add(bottom, BorderLayout.SOUTH);

        setContentPane(contentPane);
    }

    /**
     * Carrega la llista d'enquestes disponibles al desplegable (ComboBox).
     * Obté la informació del controlador i filtra només la línia identificativa.
     * Mostra un missatge d'error si no es poden carregar les dades.
     */
    private void carregarEnquestes() {

        comboEnquestes.removeAllItems();
        comboEnquestes.addItem("-- Selecciona --");

        try {
            List<String> enquestesInfo = ctrlPresentacio.obtenirLlistaEnquestes(-1);

            for (String info : enquestesInfo) {
                String[] linies = info.split("\n");
                for (String l : linies) {
                    if (l.trim().startsWith("ID:")) {
                        comboEnquestes.addItem(l); // afegim només la línia amb l’ID
                        break;
                    }
                }
            }

        } catch (EnquestaNoExisteixException e) {
            JOptionPane.showMessageDialog(this,
                    "Error carregant enquestes:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Configura els listeners (escoltadors) per gestionar les interaccions de l'usuari.
     * Defineix el comportament dels botons, la selecció del desplegable i la tecla ESC.
     */
    private void configurarListeners() {

        // Botó consultar
        buttonOK.addActionListener(e -> {
            try {
                consultar();
            } catch (EnquestaNoExisteixException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botó tancar
        buttonCancel.addActionListener(e -> dispose());

        // Quan l'usuari selecciona al combo, mostrem automàticament
        comboEnquestes.addActionListener(e -> {
            if (comboEnquestes.getSelectedIndex() > 0) {
                try {
                    consultar();
                } catch (Exception ignored) {}
            }
        });

        // ESCAPE per tancar
        contentPane.registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        // X per tancar
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Realitza la consulta de l'enquesta seleccionada i mostra el resultat.
     * Obté l'ID de l'enquesta seleccionada, demana els detalls al controlador
     * i els mostra a l'àrea de text.
     *
     * @throws EnquestaNoExisteixException Si l'enquesta sol·licitada no existeix.
     */
    private void consultar() throws EnquestaNoExisteixException {
        String sel = (String) comboEnquestes.getSelectedItem();
        if (sel == null || sel.equals("-- Selecciona --")) {
            resultat.setText("");
            return;
        }

        try {
            int id = extraerIdEnquesta(sel);
            List<String> info = ctrlPresentacio.consultarEnquesta(id);

            // Convert plain text list to HTML
            StringBuilder html = new StringBuilder();
            html.append("<html><body style='font-family:sans-serif; padding:10px;'>");
            html.append("<h2>Detalls de l'Enquesta ").append(id).append("</h2>");

            boolean inQuestions = false;
            for (String s : info) {
                if (s.trim().equals("Preguntes:")) {
                    html.append("<h3>Preguntes:</h3><ul>");
                    inQuestions = true;
                    continue;
                }

                if (inQuestions) {
                    if (s.startsWith("- ")) {
                        html.append("</ul><div style='background-color:#EFEFEF; padding:5px; margin-bottom:5px;'><b>")
                                .append(s.substring(2)).append("</b>"); // Type
                    } else if (s.equals("- - -")) {
                        html.append("</div>");
                    } else {
                        // Text or Options
                        // Simple heuristic: if follows Type, it's Text.
                        html.append("<br>").append(s);
                    }
                } else {
                    html.append("<p>").append(s).append("</p>");
                }
            }
            if (inQuestions)
                html.append("</div>"); // Close last div if needed

            html.append("</body></html>");
            resultat.setText(html.toString());

        } catch (Exception ex) {
            UIHelper.showError(this, "Error consultant: " + ex.getMessage());
        }
    }

    /**
     * Extreu l'ID numèric de l'enquesta a partir de la cadena de text mostrada al desplegable.
     * S'espera un format tipus "ID: X - Titol...".
     *
     * @param texto Cadena de text seleccionada al ComboBox.
     * @return L'ID de l'enquesta com a enter.
     * @throws RuntimeException Si el format del text no permet extreure l'ID.
     */
    private int extraerIdEnquesta(String texto) {
        try {
            // Formato esperado: "ID: X - ..."
            int idxID = texto.indexOf("ID:");
            if (idxID == -1) throw new RuntimeException("Format invalid");

            int idxDosPunts = texto.indexOf(':', idxID);
            int idxGuio = texto.indexOf('-', idxDosPunts);

            String idStr = texto.substring(idxDosPunts + 1, idxGuio).trim();
            return Integer.parseInt(idStr);

        } catch (Exception e) {
            throw new RuntimeException("No s'ha pogut extreure l'ID de: " + texto);
        }
    }

}