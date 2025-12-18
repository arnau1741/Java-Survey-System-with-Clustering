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
 * Aquesta vista permet a l'usuari seleccionar una enquesta existent, visualitzar
 * una previsualització de les respostes rebudes i exportar-les a un fitxer de text
 * localitzat a la carpeta "Pruebas".
 * </p>
 */
public class VistaExportarRespostes extends JDialog {

    private final CtrlPresentacio ctrl;
    private int idUsuari;
    private int idEnquestaActual = -1;

    private JPanel contentPane = new JPanel();
    private JComboBox<String> comboEnquestes = new JComboBox<>();
    private JTextArea areaPreview = new JTextArea();
    private JLabel labelIdActual = new JLabel();

    private JButton btnExportar = new JButton("Exportar");
    private JButton btnSortir = new JButton("Tancar");


    private JButton buttonOK = new  JButton("OK");
    private JButton buttonCancel = new  JButton("Cancel");

    /**
     * Constructor de la vista d'exportació de respostes.
     * Inicialitza la finestra modal, carrega les enquestes disponibles i configura els components.
     *
     * @param ctrl Referència al controlador de presentació.
     * @param idUsuari Identificador de l'usuari que realitza l'acció.
     */
    public VistaExportarRespostes(CtrlPresentacio ctrl, int idUsuari) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;

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
     * Crea el panell de selecció superior, l'àrea de previsualització central i els botons inferiors.
     */
    private void initLayout() {
        contentPane.setLayout(new BorderLayout(10,10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

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
     * Filtra la informació rebuda del controlador per mostrar la línia identificativa.
     */
    private void cargarEnquestes() {
        comboEnquestes.removeAllItems();
        comboEnquestes.addItem("-- Selecciona --");
        try {
            List<String> enquestasInfo = ctrl.obtenirLlistaEnquestes();
            for (String info : enquestasInfo) {
                // Buscamos la línea exacta que empieza por "ID:"
                String[] lineas = info.split("\n");
                for (String linea : lineas) {
                    if (linea.trim().startsWith("ID:")) {
                        comboEnquestes.addItem(linea); // SOLO esta línea
                        break; // Muy importante
                    }
                }
            }
        } catch (EnquestaNoExisteixException e) {
            JOptionPane.showMessageDialog(this,
                    "Error exportant: " + e.getMessage(),
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
     * Extreu l'ID de l'enquesta seleccionada i crida al mètode de visualització de dades.
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
     * Obté les respostes de l'enquesta actual des del controlador i les mostra a l'àrea de text.
     */
    private void mostrarInfoRespostes() {
        List<String> res = ctrl.obtenirRespostesEnquesta(idEnquestaActual);
        StringBuilder sb = new StringBuilder();

        for (String s : res) sb.append(s).append("\n");
        areaPreview.setText(sb.toString());
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
            if (idxID == -1) throw new RuntimeException("Format invalid");

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
     * El fitxer es guarda a la carpeta "Pruebas" amb el nom "respostes_enquesta_ID.txt".
     */
    private void onExportar() {
        String outputPath = "Pruebas" + File.separator + "respostes_enquesta_" + idEnquestaActual + ".txt";

        try {
            List<String> contingut = ctrl.exportarRespostesEnquesta(idEnquestaActual);

            try (FileWriter fw = new FileWriter(outputPath)) {
                for (String s : contingut)
                    fw.write(s + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            JOptionPane.showMessageDialog(this,
                    "Respostes exportades a:\n" + outputPath,
                    "Èxit",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (EnquestaNoExisteixException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error exportant: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}