package prop.enquestes.presentacio;

import prop.enquestes.excepcions.EnquestaNoExisteixException;
import prop.enquestes.excepcions.UsuariNoValid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VistaExportarEnquesta extends JDialog {

    private final CtrlPresentacio ctrl;
    private final int idUsuari;
    private int idEnquestaActual = -1;

    private JPanel contentPane = new JPanel();
    private JComboBox<String> comboEnquestes = new JComboBox<>();
    private JTextArea areaPreview = new JTextArea();
    private JLabel labelIdActual;

    private JButton btnExportar = new JButton("Exportar");
    private JButton btnCancel = new JButton("Cancel·lar");

    public VistaExportarEnquesta(CtrlPresentacio ctrl, int idUsuari) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;

        setTitle("Exportar Enquesta");
        setModal(true);
        setContentPane(contentPane);

        initLayout();
        try {
            cargarEnquestes();
        } catch (EnquestaNoExisteixException e) {
            throw new RuntimeException(e);
        }
        initActions();

        setSize(600, 500);
        setLocationRelativeTo(null);
    }

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

        // PANEL CENTRAL
        areaPreview = new JTextArea(12, 60);
        areaPreview.setEditable(false);
        areaPreview.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane srollInfo = new JScrollPane(areaPreview);
        srollInfo.setBorder(BorderFactory.createTitledBorder("Preview de l'Enquesta"));
        contentPane.add(srollInfo, BorderLayout.CENTER);

        // PANEL BOTONES
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnExportar);
        bottom.add(btnCancel);

        contentPane.add(bottom, BorderLayout.SOUTH);
    }

    private void cargarEnquestes() throws EnquestaNoExisteixException {
        comboEnquestes.removeAllItems();
        comboEnquestes.addItem("-- Selecciona --");


        List<String> enquestasInfo = ctrl.obtenirLlistaEnquestes();
        for (String info : enquestasInfo) {
            String[] lineas = info.split("\n");
            for (String linea : lineas) {
                if (linea.trim().startsWith("ID:")) {
                    comboEnquestes.addItem(linea);
                    break;
                }
            }
        }

    }

    private void initActions () {
        comboEnquestes.addActionListener(e -> mostrarPreview());
        btnExportar.addActionListener(e -> onExportar());
        btnCancel.addActionListener(e -> dispose());
    }

    private void mostrarPreview () {
        String seleccionado = (String) comboEnquestes.getSelectedItem();
        if (seleccionado != null && !seleccionado.equals("-- Selecciona --")) {
            try {
                idEnquestaActual = extraerIdEnquesta(seleccionado);
                labelIdActual.setText("ID: " + idEnquestaActual);
                mostrarInfoEnquesta();
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

    private void mostrarInfoEnquesta () {
        try {
            List<String> infoEnquesta = ctrl.obtenirInfoEnquesta(idEnquestaActual);
            StringBuilder sb = new StringBuilder();
            for (String line : infoEnquesta) {
                sb.append(line).append("\n");
            }
            areaPreview.setText(sb.toString());
        } catch (EnquestaNoExisteixException ex) {
            areaPreview.setText("Error al carregar la informació de l'enquesta. Info: " + ex.getMessage());
        }
    }

    private int extraerIdEnquesta (String texto){
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

    private void onExportar () {
        String outputPath = "Pruebas" + File.separator + "enquesta_" + idEnquestaActual + ".txt";

        try {
            List<String> contingut = ctrl.exportarEnquesta(idUsuari, idEnquestaActual);

            try (FileWriter fw = new FileWriter(outputPath)) {
                for (String s : contingut)
                    fw.write(s + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JOptionPane.showMessageDialog(this,
                    "Enquesta exportada a:\n" + outputPath,
                    "Èxit",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (EnquestaNoExisteixException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error exportant: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (UsuariNoValid ex) {
            JOptionPane.showMessageDialog(this,
                    "Error de l'usuari: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}