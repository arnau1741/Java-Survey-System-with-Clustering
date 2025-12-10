package prop.enquestes.presentacio;

import prop.enquestes.excepcions.EnquestaNoExisteixException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class VistaConsultarEnquesta extends JDialog {

    private CtrlPresentacio ctrlPresentacio;
    private JPanel contentPane;
    private JTextArea resultat;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> comboEnquestes;

    public VistaConsultarEnquesta(CtrlPresentacio ctrlPresentacio) {
        super((Frame) null, "Consultar Enquesta", true);
        this.ctrlPresentacio = ctrlPresentacio;

        inicialitzarComponents();
        carregarEnquestes();
        configurarListeners();

        pack();
        setLocationRelativeTo(null);
    }

    private void inicialitzarComponents() {

        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ========== SELECCIÓ ENQUESTA (TOP) ==========
        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelSeleccion.setBorder(BorderFactory.createTitledBorder("Selecció d'Enquesta"));

        panelSeleccion.add(new JLabel("Enquesta:"));

        comboEnquestes = new JComboBox<>();
        comboEnquestes.setPreferredSize(new Dimension(250, 25));
        panelSeleccion.add(comboEnquestes);

        contentPane.add(panelSeleccion, BorderLayout.NORTH);

        // ========== RESULTAT (CENTER) ==========
        resultat = new JTextArea(15, 40);
        resultat.setEditable(false);
        resultat.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(resultat);

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

    private void carregarEnquestes() {

        comboEnquestes.removeAllItems();
        comboEnquestes.addItem("-- Selecciona --");

        try {
            List<String> enquestesInfo = ctrlPresentacio.obtenirLlistaEnquestes();

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

    private void consultar() throws EnquestaNoExisteixException {

        String sel = (String) comboEnquestes.getSelectedItem();

        if (sel == null || sel.equals("-- Selecciona --")) {
            JOptionPane.showMessageDialog(this,
                    "Has de seleccionar una enquesta.",
                    "Advertència",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = extraerIdEnquesta(sel);

        List<String> info = ctrlPresentacio.consultarEnquesta(id);

        StringBuilder sb = new StringBuilder();
        for (String s : info) sb.append(s).append("\n");

        resultat.setText(sb.toString());
    }

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
