package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class VistaConsultarRespostesUsuari extends JDialog {

    private CtrlPresentacio ctrl;
    private int idUsuari;
    private JPanel content;

    private JComboBox<String> comboEnquestes;
    private JTextArea areaPreview;
    private JButton btnModificar;
    private JButton btnTancar;

    // Store IDs corresponding to combo items
    private List<Integer> idsEnquestes;

    /**
     * Constructor del diàleg de consulta de respostes d'un usuari.
     * Configura la interfície, carrega les dades inicials i assigna els controladors d'esdeveniments.
     *
     * @param ctrl Referència al controlador de presentació per obtenir les dades.
     * @param idUsuari Identificador de l'usuari cuyas respuestas se van a consultar.
     */
    public VistaConsultarRespostesUsuari(CtrlPresentacio ctrl, int idUsuari) {
        super((Frame) null, "Les Meves Respostes", true);
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;

        UIHelper.configureDialog(this, "Les Meves Respostes");
        setSize(600, 500);
        setLocationRelativeTo(null);

        initUI();
        carregarEnquestes();
    }

    /**
     * Inicialitza i distribueix els components gràfics del diàleg.
     */
    private void initUI() {
        content = new JPanel(new BorderLayout(10, 10));
        content.setBackground(UIHelper.COLOR_BACKGROUND);
        content.setBorder(UIHelper.PADDING_MAIN);
        setContentPane(content);

        // TOP
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(UIHelper.COLOR_BACKGROUND);
        top.setBorder(BorderFactory.createTitledBorder("Selecciona Enquesta Resposta"));

        top.add(new JLabel("Enquesta:"));
        comboEnquestes = new JComboBox<>();
        comboEnquestes.setPreferredSize(new Dimension(300, 25));
        comboEnquestes.addActionListener(e -> mostrarPreview());
        top.add(comboEnquestes);

        content.add(top, BorderLayout.NORTH);

        // CENTER
        areaPreview = new JTextArea();
        areaPreview.setEditable(false);
        areaPreview.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(areaPreview);
        scroll.setBorder(BorderFactory.createTitledBorder("Detall Respostes"));
        content.add(scroll, BorderLayout.CENTER);

        // BOTTOM
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(UIHelper.COLOR_BACKGROUND);

        btnModificar = UIHelper.createButton("Modificar", e -> actionModificar());
        btnModificar.setEnabled(false); // Enable only when survey selected

        btnTancar = UIHelper.createButton("Tancar", e -> dispose());

        bottom.add(btnModificar);
        bottom.add(btnTancar);
        content.add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Carrega la llista d'enquestes a les quals l'usuari ha respost i les afegeix al combo.
     */
    private void carregarEnquestes() {
        comboEnquestes.removeAllItems();
        idsEnquestes = new ArrayList<>();
        comboEnquestes.addItem("-- Selecciona --");
        idsEnquestes.add(-1);

        try {
            List<String> rawList = ctrl.obtenirEnquestesRespostesPerUsuari(idUsuari);
            if (rawList.isEmpty()) {
                areaPreview.setText("No has respost cap enquesta encara.");
            }
            for (String s : rawList) {
                // s format: "ID: X - Titol"
                comboEnquestes.addItem(s);

                // Extract ID
                try {
                    String[] parts = s.split("-");
                    String idPart = parts[0].replace("ID:", "").trim();
                    idsEnquestes.add(Integer.parseInt(idPart));
                } catch (Exception ex) {
                    idsEnquestes.add(-1);
                }
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Error carregant llista: " + e.getMessage());
        }
    }

    /**
     * Mostra la vista prèvia de les respostes de l'usuari per a l'enquesta seleccionada.
     * Si no hi ha cap enquesta seleccionada, es neteja l'àrea de text.
     */
    private void mostrarPreview() {
        int idx = comboEnquestes.getSelectedIndex();
        if (idx <= 0) {
            areaPreview.setText("");
            btnModificar.setEnabled(false);
            return;
        }

        int idEnquesta = idsEnquestes.get(idx);
        btnModificar.setEnabled(true);

        try {
            // Reusing exportarRespostesUsuari logic for preview as it gives nice text
            List<String> lines = ctrl.exportarRespostesUsuari(idEnquesta, idUsuari);
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                sb.append(line).append("\n");
            }
            areaPreview.setText(sb.toString());
        } catch (Exception e) {
            areaPreview.setText("Error carregant respostes: " + e.getMessage());
        }
    }

    /**
     * Acció per modificar les respostes de l'enquesta seleccionada.
     * Tanca el diàleg actual i obre la vista per respondre l'enquesta.
     */
    private void actionModificar() {
        int idx = comboEnquestes.getSelectedIndex();
        if (idx <= 0)
            return;
        int idEnquesta = idsEnquestes.get(idx);

        dispose();
        ctrl.mostrarRespondreEnquesta(idUsuari, idEnquesta);
    }
}
