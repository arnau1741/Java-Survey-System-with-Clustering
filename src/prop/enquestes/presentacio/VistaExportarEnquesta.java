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

/**
 * Diàleg per a l'exportació d'enquestes a fitxers externs.
 * <p>
 * Aquesta vista permet a l'usuari seleccionar una enquesta existent del
 * sistema,
 * previsualitzar-ne el contingut i exportar-la a un fitxer de text dins de la
 * carpeta "Pruebas".
 * El format d'exportació és compatible amb el format d'importació del sistema.
 * </p>
 */
public class VistaExportarEnquesta extends JDialog {

    private final CtrlPresentacio ctrl;
    private final int idUsuari;
    private boolean isAdministeredMode = false;
    private int idEnquestaActual = -1;

    private JPanel contentPane = new JPanel();
    private JComboBox<String> comboEnquestes = new JComboBox<>();
    private JTextArea areaPreview = new JTextArea();
    private JLabel labelIdActual = new JLabel();

    private JButton btnExportar = new JButton("Exportar");
    private JButton btnCancel = new JButton("Cancel·lar");

    /**
     * Constructor de la vista d'exportació d'enquestes.
     * Inicialitza la finestra modal, carrega la llista d'enquestes disponibles
     * i configura els components de la interfície.
     *
     * @param ctrl     Referència al controlador de presentació.
     * @param idUsuari Identificador de l'usuari que realitza l'acció.
     */
    public VistaExportarEnquesta(CtrlPresentacio ctrl, int idUsuari) {
        this(ctrl, idUsuari, false);
    }

    public VistaExportarEnquesta(CtrlPresentacio ctrl, int idUsuari, boolean isAdministeredMode) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;
        this.isAdministeredMode = isAdministeredMode;

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

    /**
     * Inicialitza i distribueix els elements gràfics a la finestra.
     * Organitza el panell de selecció a la part superior, la previsualització al
     * centre
     * i els botons d'acció a la part inferior.
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

    /**
     * Carrega totes les enquestes disponibles al desplegable de selecció.
     * Filtra la informació rebuda del controlador per mostrar només la línia
     * identificativa.
     *
     * @throws EnquestaNoExisteixException Si no hi ha enquestes disponibles al
     *                                     sistema.
     */
    private void cargarEnquestes() throws EnquestaNoExisteixException {
        comboEnquestes.removeAllItems();
        comboEnquestes.addItem("-- Selecciona --");

        try {
            List<String> enquestasInfo;
            // Logical matrix:
            // Role Enquestat -> Always Own Answers
            // Role Admin + isAdministeredMode -> Administered
            // Role Admin + !isAdministeredMode -> Own Answers
            String rol = ctrl.obtenirRol(idUsuari);

            if ("ENQUESTAT".equals(rol) || !isAdministeredMode) {
                enquestasInfo = ctrl.obtenirEnquestesRespostesPerUsuari(idUsuari);
            } else {
                enquestasInfo = ctrl.obtenirEnquestesAdministrades(idUsuari);
            }

            for (String info : enquestasInfo) {
                if ("ENQUESTAT".equals(rol) || !isAdministeredMode) {
                    comboEnquestes.addItem(info);
                } else {
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
            // Handle exceptions if needed, but existing code throws
            // EnquestaNoExisteixException declared in signature
        }
    }

    /**
     * Assigna les accions (listeners) als components interactius.
     * Defineix el comportament en canviar la selecció del ComboBox i en prémer els
     * botons.
     */
    private void initActions() {
        comboEnquestes.addActionListener(e -> mostrarPreview());
        btnExportar.addActionListener(e -> onExportar());
        btnCancel.addActionListener(e -> dispose());
    }

    /**
     * Actualitza l'àrea de previsualització amb la informació de l'enquesta
     * seleccionada.
     * S'executa automàticament quan l'usuari canvia l'opció al desplegable.
     */
    private void mostrarPreview() {
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

    /**
     * Sol·licita al controlador la informació detallada de l'enquesta actual i la
     * mostra a l'àrea de text.
     */
    private void mostrarInfoEnquesta() {
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

    /**
     * Extreu l'ID numèric de l'enquesta a partir de la cadena de text del ComboBox.
     *
     * @param texto Cadena amb format "ID: X - Títol...".
     * @return L'ID de l'enquesta com a enter.
     * @throws RuntimeException Si el format del text no és l'esperat.
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
     * Executa el procés d'exportació.
     * Genera un fitxer de text amb el contingut de l'enquesta a la carpeta
     * predefinida ("Pruebas").
     * Mostra missatges d'èxit o error segons el resultat de l'operació.
     */
    private void onExportar() {
        if (idEnquestaActual == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una enquesta primer.", "Atenció",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Enquesta");
        fileChooser.setSelectedFile(new File("enquesta_" + idEnquestaActual + ".txt"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try {
                List<String> contingut = ctrl.exportarEnquestaSenseRespostes(idUsuari, idEnquestaActual);

                try (FileWriter fw = new FileWriter(fileToSave)) {
                    for (String s : contingut)
                        fw.write(s + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                JOptionPane.showMessageDialog(this,
                        "Enquesta exportada a:\n" + fileToSave.getAbsolutePath(),
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
}