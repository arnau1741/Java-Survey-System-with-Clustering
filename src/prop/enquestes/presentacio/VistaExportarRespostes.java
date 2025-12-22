package prop.enquestes.presentacio;

import prop.enquestes.excepcions.EnquestaNoExisteixException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Diàleg per a l'exportació de les respostes d'una enquesta.
 * <p>
 * Aquesta vista permet a l'usuari seleccionar una enquesta existent,
 * visualitzar
 * una previsualització de les respostes rebudes i exportar-les a un fitxer de
 * text
 * localitzat a la carpeta "Pruebas".
 * </p>
 */
public class VistaExportarRespostes extends JDialog {

    private final CtrlPresentacio ctrl;
    private int idUsuari;
    private int idEnquestaActual = -1;
    private boolean isAdministeredMode;

    private JPanel contentPane = new JPanel();
    private JComboBox<String> comboEnquestes = new JComboBox<>();
    private JTextArea areaPreview = new JTextArea();
    private JLabel labelIdActual = new JLabel();

    private JButton btnExportar = new JButton("Exportar");
    private JButton btnSortir = new JButton("Tancar");

    private JButton buttonOK = new JButton("OK");
    private JButton buttonCancel = new JButton("Cancel");

    /**
     * Constructor de la vista d'exportació de respostes.
     * Inicialitza la finestra modal, carrega les enquestes disponibles i configura
     * els components.
     *
     * @param ctrl     Referència al controlador de presentació.
     * @param idUsuari Identificador de l'usuari que realitza l'acció.
     */

    public VistaExportarRespostes(CtrlPresentacio ctrl, int idUsuari, boolean isAdministeredMode) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;
        this.isAdministeredMode = isAdministeredMode;

        setTitle("Exportar Respostes");
        setModal(true);
        setContentPane(contentPane);

        initLayout();
        cargarEnquestes();
        initActions();

        setSize(600, 500);
        setLocationRelativeTo(null);
    }

    /**
     * Inicialitza i organitza els components gràfics (Layout).
     * Crea el panell de selecció superior, l'àrea de previsualització central i els
     * botons inferiors.
     */
    private void initLayout() {
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // PANEL SUPERIOR
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Selecció d'Enquesta"));

        panelSuperior.add(new JLabel("Enquesta:"));

        comboEnquestes = new JComboBox<>();
        comboEnquestes.setPreferredSize(new Dimension(250, 25));
        panelSuperior.add(comboEnquestes);

        labelIdActual = new JLabel("ID: --");
        labelIdActual.setFont(labelIdActual.getFont().deriveFont(Font.BOLD));
        labelIdActual.setForeground(Color.BLUE);
        panelSuperior.add(labelIdActual);

        contentPane.add(panelSuperior, BorderLayout.NORTH);

        // PANEL CENTRAL (PREVIEW)
        areaPreview = new JTextArea(12, 60);
        areaPreview.setEditable(false);
        areaPreview.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollInfo = new JScrollPane(areaPreview);
        scrollInfo.setBorder(BorderFactory.createTitledBorder("Preview de Respostes"));

        contentPane.add(scrollInfo, BorderLayout.CENTER);

        // PANEL BOTONES
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnExportar);
        bottom.add(btnSortir);

        contentPane.add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Carrega la llista d'enquestes disponibles al desplegable (ComboBox).
     * Filtra la informació rebuda del controlador per mostrar la línia
     * identificativa.
     */
    private void cargarEnquestes() {
        comboEnquestes.removeAllItems();
        comboEnquestes.addItem("-- Selecciona --");
        try {
            List<String> enquestasInfo;
            String rol = ctrl.obtenirRol(idUsuari);

            if ("ENQUESTAT".equals(rol) || ("ADMINISTRADOR".equals(rol) && !isAdministeredMode)) {
                enquestasInfo = ctrl.obtenirEnquestesRespostesPerUsuari(idUsuari);
            }
            else if (rol.equals("ENQUESTADOR")) {
                enquestasInfo = ctrl.obtenirEnquestesAdministrades(idUsuari);
            } else {
                enquestasInfo = ctrl.obtenirEnquestesAdministrades(idUsuari);
            }

            for (String info : enquestasInfo) {
                if ("ENQUESTAT".equals(rol) || ("ADMINISTRADOR".equals(rol) && !isAdministeredMode)) {
                    comboEnquestes.addItem(info);
                } else {
                    // Administered/List format usually needs ID extraction if formatted weirdly
                    // My implemented obtenirEnquestesAdministrades returns "ID: X - Title"
                    // VistaEnquestesExtresAdmin handling suggests checking line start.
                    // But here we can simpler if consistency exists.
                    // Let's assume consistent "ID:" start logic just in case.
                    String[] lineas = info.split("\n");
                    for (String linea : lineas) {
                        if (linea.trim().startsWith("ID:")) {
                            comboEnquestes.addItem(linea);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error carregar llista: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Assigna els escoltadors (listeners) als components interactius.
     */
    private void initActions() {

        comboEnquestes.addActionListener(e -> mostrarPreview());

        btnExportar.addActionListener(e -> onExportar());

        btnSortir.addActionListener(e -> dispose());
    }

    /**
     * Actualitza l'àrea de previsualització quan es selecciona una enquesta.
     * Extreu l'ID de l'enquesta seleccionada i crida al mètode de visualització de
     * dades.
     */
    private void mostrarPreview() {
        String seleccionado = (String) comboEnquestes.getSelectedItem();
        if (seleccionado != null && !seleccionado.equals("-- Selecciona --")) {
            try {
                idEnquestaActual = extraerIdEnquesta(seleccionado);
                labelIdActual.setText("ID: " + idEnquestaActual);
                mostrarInfoRespostes();
            } catch (Exception ex) {
                idEnquestaActual = -1;
                labelIdActual.setText("ID: --");
                areaPreview.setText("");
            }
        } else {
            idEnquestaActual = -1;
            labelIdActual.setText("ID: --");
            areaPreview.setText("");
        }

    }

    /**
     * Obté les respostes de l'enquesta actual des del controlador i les mostra a
     * l'àrea de text.
     */
    private void mostrarInfoRespostes() {
        try {
            List<String> res;
            String rol = ctrl.obtenirRol(idUsuari);
            if ("ENQUESTAT".equals(rol) || ("ADMINISTRADOR".equals(rol) && !isAdministeredMode)) {
                res = ctrl.exportarRespostesUsuari(idEnquestaActual, idUsuari);
            } else {
                res = ctrl.obtenirRespostesEnquesta(idEnquestaActual);
            }

            StringBuilder sb = new StringBuilder();
            for (String s : res)
                sb.append(s).append("\n");
            areaPreview.setText(sb.toString());
        } catch (Exception e) {
            areaPreview.setText("Error: " + e.getMessage());
        }
    }

    /**
     * Extreu l'ID numèric de l'enquesta a partir del text seleccionat al ComboBox.
     *
     * @param texto Cadena amb format "ID: X - Títol...".
     * @return L'ID de l'enquesta.
     * @throws RuntimeException Si el format no és vàlid.
     */
    private int extraerIdEnquesta(String texto) {
        try {
            // Formato esperado: "ID: X - ..."
            int idxID = texto.indexOf("ID:");
            if (idxID == -1)
                throw new RuntimeException("Format invalid");

            int idxDosPunts = texto.indexOf(':', idxID);
            int idxGuio = texto.indexOf('-', idxDosPunts);

            String idStr = texto.substring(idxDosPunts + 1, idxGuio).trim();
            return Integer.parseInt(idStr);

        } catch (Exception e) {
            throw new RuntimeException("No s'ha pogut extreure l'ID de: " + texto);
        }
    }

    /**
     * Executa l'exportació de les respostes a un fitxer de text.
     * El fitxer es guarda a la carpeta "Pruebas" amb el nom
     * "respostes_enquesta_ID.txt".
     */
    private void onExportar() {
        if (idEnquestaActual == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una enquesta primer.", "Atenció",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Respostes");
        fileChooser.setSelectedFile(new File("respostes_enquesta_" + idEnquestaActual + ".txt"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try {
                List<String> contingut;
                // Determine which export method to use based on Role/Mode
                String rol = ctrl.obtenirRol(idUsuari);
                if ("ENQUESTAT".equals(rol) || ("ADMIN".equals(rol) && !isAdministeredMode)) {
                    contingut = ctrl.exportarRespostesUsuari(idEnquestaActual, idUsuari);
                } else {
                    contingut = ctrl.exportarRespostesEnquesta(idEnquestaActual);
                }

                try (FileWriter fw = new FileWriter(fileToSave)) {
                    for (String s : contingut)
                        fw.write(s + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                JOptionPane.showMessageDialog(this,
                        "Respostes exportades correctament a:\n" + fileToSave.getAbsolutePath(),
                        "Èxit",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error exportant: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}