package prop.enquestes.presentacio;

import prop.enquestes.excepcions.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class VistaRespondreEnquesta extends JDialog {

    private CtrlPresentacio ctrl;
    private int idUsuari;

    // State
    private int idEnquestaActual = -1;
    private List<PreguntaInfo> preguntes;
    private List<String> respostes; // Stores answers as strings
    private int preguntaActual = 0;

    // UI Components
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // -- Selection Panel --
    private JPanel panelSeleccio;
    private JComboBox<String> comboEnquestes;
    private JTextArea areaInfoEnquesta;
    private JButton btnComencar;

    // -- Question Panel --
    private JPanel panelPregunta;
    private JProgressBar progressBar;
    private JLabel labelIndexPregunta;
    private JTextArea labelTextPregunta; // JTextArea for wrapping
    private JPanel panelInputsContainer;
    private JButton btnAnterior;
    private JButton btnSeguent;
    private JButton btnFinalitzar;

    // Dynamic Inputs
    private ButtonGroup buttonGroupActual;
    private List<JCheckBox> checkBoxesActuals;
    private JTextField textFieldActual;
    private JTextArea textAreaActual;

    // Helper Class for Question Data
    private static class PreguntaInfo {
        String tipus; // "NUMERICA", "UNICA", "ORDENADA", "MULTIPLE", "LLIURE"
        String text;
        List<String> opcions;

        PreguntaInfo(String tipus, String text, List<String> opcions) {
            this.tipus = tipus;
            this.text = text;
            this.opcions = opcions;
        }
    }

    public VistaRespondreEnquesta(CtrlPresentacio ctrl, int idUsuari) {
        this(ctrl, idUsuari, -1);
    }

    public VistaRespondreEnquesta(CtrlPresentacio ctrl, int idUsuari, int idEnquestaPre) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;
        this.idEnquestaActual = idEnquestaPre;

        UIHelper.configureDialog(this, "Respondre Enquesta");

        // Setup Main Layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        setContentPane(mainPanel);

        // Build Panels
        createPanelSeleccio();
        createPanelPregunta();

        mainPanel.add(panelSeleccio, "SELECCIO");
        mainPanel.add(panelPregunta, "PREGUNTA");

        // Initial State
        cargarEnquestes();

        if (idEnquestaActual != -1) {
            // Auto-select if exists in combo (optional but good for consistency)
            // But main goal is to START.
            // actionComencar uses idEnquestaActual.
            // We just need to check if valid.
            // Let's hide selection panel and go straight to question?
            // Or just simulate button click.
            try {
                // Verify it exists in our loaded list or directly load it
                // Since cargarEnquestes relies on string parsing, we might not pass the right
                // string to combo.
                // But actionComencar relies on idEnquestaActual.
                // So we can just call actionComencar!
                actionComencar();
            } catch (Exception e) {
                // Fallback
            }
        }
        pack();
        setSize(800, 600); // Reasonable default size
        setLocationRelativeTo(null);
    }

    private void createPanelSeleccio() {
        panelSeleccio = new JPanel(new BorderLayout());
        panelSeleccio.setBorder(UIHelper.PADDING_MAIN);
        panelSeleccio.setBackground(UIHelper.COLOR_BACKGROUND);

        JLabel lblTitle = UIHelper.createTitleLabel("Selecciona una Enquesta");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panelSeleccio.add(lblTitle, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(UIHelper.COLOR_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        comboEnquestes = new JComboBox<>();
        comboEnquestes.setFont(UIHelper.FONT_NORMAL);

        areaInfoEnquesta = new JTextArea(10, 40);
        areaInfoEnquesta.setEditable(false);
        areaInfoEnquesta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaInfoEnquesta.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        centerPanel.add(comboEnquestes);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(new JScrollPane(areaInfoEnquesta));

        panelSeleccio.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(UIHelper.COLOR_BACKGROUND);

        btnComencar = UIHelper.createButton("Començar Enquesta", e -> actionComencar());
        btnComencar.setEnabled(false);
        JButton btnCancel = UIHelper.createButton("Cancel·lar", e -> dispose());

        bottomPanel.add(btnComencar);
        bottomPanel.add(btnCancel);
        panelSeleccio.add(bottomPanel, BorderLayout.SOUTH);

        // Listener for Combo
        comboEnquestes.addActionListener(e -> {
            String selected = (String) comboEnquestes.getSelectedItem();
            if (selected != null && !selected.equals("-- Selecciona --")) {
                try {
                    idEnquestaActual = extraerIdEnquesta(selected);
                    mostrarInfoEnquesta();
                    btnComencar.setEnabled(true);
                } catch (Exception ex) {
                    areaInfoEnquesta.setText("");
                    btnComencar.setEnabled(false);
                }
            } else {
                areaInfoEnquesta.setText("");
                btnComencar.setEnabled(false);
            }
        });
    }

    private void createPanelPregunta() {
        panelPregunta = new JPanel(new BorderLayout());
        panelPregunta.setBackground(UIHelper.COLOR_BACKGROUND);
        panelPregunta.setBorder(UIHelper.PADDING_MAIN);

        // Top: Progress
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIHelper.COLOR_BACKGROUND);
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        labelIndexPregunta = UIHelper.createSubtitleLabel("Pregunta 1 de ?");

        topPanel.add(labelIndexPregunta, BorderLayout.NORTH);
        topPanel.add(progressBar, BorderLayout.SOUTH);
        panelPregunta.add(topPanel, BorderLayout.NORTH);

        // Center: Question + Inputs
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(UIHelper.COLOR_BACKGROUND);
        centerPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        labelTextPregunta = new JTextArea("Text de la pregunta...");
        labelTextPregunta.setWrapStyleWord(true);
        labelTextPregunta.setLineWrap(true);
        labelTextPregunta.setEditable(false);
        labelTextPregunta.setOpaque(false);
        labelTextPregunta.setFont(UIHelper.FONT_TITLE);
        labelTextPregunta.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelInputsContainer = new JPanel();
        panelInputsContainer.setLayout(new BoxLayout(panelInputsContainer, BoxLayout.Y_AXIS));
        panelInputsContainer.setBackground(UIHelper.COLOR_BACKGROUND);
        panelInputsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerPanel.add(labelTextPregunta);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(panelInputsContainer); // Scrollable if needed?
        // Add scroll just in case
        JScrollPane scrollCenter = new JScrollPane(centerPanel);
        scrollCenter.setBorder(null);
        panelPregunta.add(scrollCenter, BorderLayout.CENTER);

        // Bottom: Navigation
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(UIHelper.COLOR_BACKGROUND);

        btnAnterior = UIHelper.createButton("Anterior", e -> actionAnterior());
        btnSeguent = UIHelper.createButton("Següent", e -> actionSeguent());
        btnFinalitzar = UIHelper.createButton("Finalitzar", e -> actionFinalitzar());

        bottomPanel.add(btnAnterior);
        bottomPanel.add(btnSeguent);
        bottomPanel.add(btnFinalitzar);

        panelPregunta.add(bottomPanel, BorderLayout.SOUTH);
    }

    // --- Actions ---

    private void actionComencar() {
        try {
            // Load questions
            List<String> rawInfo = ctrl.obtenirPreguntesEnquesta(idEnquestaActual);
            preguntes = parsearPreguntes(rawInfo);

            if (preguntes.isEmpty()) {
                UIHelper.showError(this, "Aquesta enquesta no té preguntes.");
                return;
            }

            // Check if user has answered before
            List<String> prevAnswers = ctrl.getRespostesUsuariList(idEnquestaActual, idUsuari);

            boolean hasData = false;
            if (prevAnswers != null && prevAnswers.size() == preguntes.size()) {
                for (String s : prevAnswers) {
                    if (s != null && !s.isEmpty()) {
                        hasData = true;
                        break;
                    }
                }
            }

            if (hasData) {
                respostes = prevAnswers;
                UIHelper.showInfo(this, "S'han recuperat les teves respostes anteriors.");
            } else {
                respostes = new ArrayList<>(Collections.nCopies(preguntes.size(), ""));
            }

            preguntaActual = 0;
            mostrarPregunta();
            cardLayout.show(mainPanel, "PREGUNTA");

        } catch (Exception e) {
            UIHelper.showError(this, "Error carregant l'enquesta: " + e.getMessage());
        }
    }

    private void actionSeguent() {
        String resp = extraerRespostaUI();
        if (resp == null || resp.trim().isEmpty()) {
            UIHelper.showWarning(this, "Siusplau, respon la pregunta per continuar.");
            return;
        }
        respostes.set(preguntaActual, resp);

        preguntaActual++;
        mostrarPregunta();
    }

    private void actionAnterior() {
        if (preguntaActual > 0) {
            // Save current (optional?) No, if they go back they might want to discard?
            // Better to save just in case
            String resp = extraerRespostaUI();
            if (resp != null && !resp.trim().isEmpty()) {
                respostes.set(preguntaActual, resp);
            }

            preguntaActual--;
            mostrarPregunta();
        }
    }

    private void actionFinalitzar() {
        // Save last answer
        String resp = extraerRespostaUI();
        if (resp == null || resp.trim().isEmpty()) {
            UIHelper.showWarning(this, "Siusplau, respon la pregunta per finalitzar.");
            return;
        }
        respostes.set(preguntaActual, resp);

        // Submit
        try {
            ctrl.respondreEnquesta(idUsuari, idEnquestaActual, respostes);
            UIHelper.showInfo(this, "Respostes enviades correctament!");
            dispose();

        } catch (InvalidFormatEnquesta ex) {
            if (ex.getMessage() != null && ex.getMessage().contains("ja ha estat realitzada")) {
                try {
                    ctrl.modificarRespostaEnquesta(idUsuari, idEnquestaActual, respostes);
                    UIHelper.showInfo(this, "Respostes modificades correctament!");
                    dispose();
                } catch (Exception e2) {
                    UIHelper.showError(this, "Error modificant respostes: " + e2.getMessage());
                }
            } else {
                UIHelper.showError(this, "Error enviant respostes: " + ex.getMessage());
            }
        } catch (Exception ex) {
            UIHelper.showError(this, "Error enviant respostes: " + ex.getMessage());
        }
    }

    // --- Helpers ---

    private void mostrarPregunta() {
        PreguntaInfo p = preguntes.get(preguntaActual);

        // Update Header
        labelIndexPregunta.setText("Pregunta " + (preguntaActual + 1) + " de " + preguntes.size());
        progressBar.setMaximum(preguntes.size());
        progressBar.setValue(preguntaActual + 1);

        // Update Text
        labelTextPregunta.setText(p.text);

        // Build Inputs
        panelInputsContainer.removeAll();
        buttonGroupActual = null;
        checkBoxesActuals = null;
        textFieldActual = null;
        textAreaActual = null;

        String savedAnswer = respostes.get(preguntaActual); // Get saved answer if any

        if (p.tipus.equals("NUMERICA")) {
            textFieldActual = new JTextField(20);
            textFieldActual.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            if (!savedAnswer.isEmpty())
                textFieldActual.setText(savedAnswer);
            panelInputsContainer.add(textFieldActual);
        } else if (p.tipus.equals("LLIURE")) {
            textAreaActual = new JTextArea(6, 40);
            textAreaActual.setLineWrap(true);
            textAreaActual.setWrapStyleWord(true);
            if (!savedAnswer.isEmpty())
                textAreaActual.setText(savedAnswer);
            JScrollPane scroll = new JScrollPane(textAreaActual);
            scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelInputsContainer.add(scroll);
        } else if (p.tipus.equals("UNICA") || p.tipus.equals("ORDENADA")) {
            buttonGroupActual = new ButtonGroup();
            int index = 0;
            // savedAnswer stores the index "0", "1", etc.
            int savedIdx = -1;
            try {
                if (!savedAnswer.isEmpty())
                    savedIdx = Integer.parseInt(savedAnswer);
            } catch (NumberFormatException ignored) {
            }

            for (String op : p.opcions) {
                JRadioButton rb = new JRadioButton(op);
                rb.setBackground(UIHelper.COLOR_BACKGROUND);
                buttonGroupActual.add(rb);
                panelInputsContainer.add(rb);
                if (index == savedIdx)
                    rb.setSelected(true);
                index++;
            }
        } else if (p.tipus.equals("MULTIPLE")) {
            checkBoxesActuals = new ArrayList<>();
            // savedAnswer stores "0,2,3"
            List<String> selectedIndices = new ArrayList<>();
            if (!savedAnswer.isEmpty()) {
                Collections.addAll(selectedIndices, savedAnswer.split(","));
            }

            int index = 0;
            for (String op : p.opcions) {
                JCheckBox cb = new JCheckBox(op);
                cb.setBackground(UIHelper.COLOR_BACKGROUND);
                checkBoxesActuals.add(cb);
                panelInputsContainer.add(cb);
                if (selectedIndices.contains(String.valueOf(index)))
                    cb.setSelected(true);
                index++;
            }
        }

        // Navigation State
        btnAnterior.setEnabled(preguntaActual > 0);

        if (preguntaActual == preguntes.size() - 1) {
            btnSeguent.setVisible(false);
            btnFinalitzar.setVisible(true);
        } else {
            btnSeguent.setVisible(true);
            btnFinalitzar.setVisible(false);
        }

        panelInputsContainer.revalidate();
        panelInputsContainer.repaint();
    }

    // --- Parsing / Extraction (Same logic, cleaner) ---

    private void cargarEnquestes() {
        comboEnquestes.removeAllItems();
        comboEnquestes.addItem("-- Selecciona --");
        try {
            List<String> enquestasInfo = ctrl.obtenirLlistaEnquestes(idUsuari);
            for (String info : enquestasInfo) {
                String[] lineas = info.split("\n");
                for (String linea : lineas) {
                    if (linea.trim().startsWith("ID:")) {
                        comboEnquestes.addItem(linea);
                        break;
                    }
                }
            }
            if (comboEnquestes.getItemCount() <= 1) {
                areaInfoEnquesta.setText("No hi ha enquestes disponibles.");
            }
        } catch (EnquestaNoExisteixException e) {
            areaInfoEnquesta.setText("Error: " + e.getMessage());
        }
    }

    private void mostrarInfoEnquesta() {
        if (idEnquestaActual == -1) {
            areaInfoEnquesta.setText("");
            return;
        }
        try {
            List<String> info = ctrl.obtenirPreguntesEnquesta(idEnquestaActual);
            StringBuilder sb = new StringBuilder();
            int lines = 0;
            for (String s : info) {
                if (lines++ > 5)
                    break;
                sb.append(s).append("\n");
            }
            areaInfoEnquesta.setText(sb.toString());
        } catch (Exception e) {
            areaInfoEnquesta.setText("Error: " + e.getMessage());
        }
    }

    private int extraerIdEnquesta(String texto) {
        int idxID = texto.indexOf("ID:");
        if (idxID == -1)
            throw new RuntimeException("Format invalid");
        int idxDosPunts = texto.indexOf(':', idxID);
        int idxGuio = texto.indexOf('-', idxDosPunts);
        String idStr = texto.substring(idxDosPunts + 1, idxGuio).trim();
        return Integer.parseInt(idStr);
    }

    private List<PreguntaInfo> parsearPreguntes(List<String> rawLines) {
        List<PreguntaInfo> llista = new ArrayList<>();
        List<String> cleanLines = new ArrayList<>();
        boolean startFound = false;

        for (String line : rawLines) {
            if (line != null && line.trim().equals("Preguntes:")) {
                startFound = true;
                continue;
            }
            if (!startFound)
                continue;
            if (line != null && line.startsWith("- "))
                cleanLines.add(line.substring(2));
            else if (line != null)
                cleanLines.add(line);
        }

        int i = 0;
        while (i < cleanLines.size()) {
            String tipus = cleanLines.get(i++);
            if (tipus == null || tipus.trim().equals("- - -") || tipus.trim().isEmpty())
                continue;

            // Basic validation
            if (!tipus.equals("NUMERICA") && !tipus.equals("UNICA") && !tipus.equals("ORDENADA") &&
                    !tipus.equals("MULTIPLE") && !tipus.equals("LLIURE"))
                continue;

            if (i >= cleanLines.size())
                break;
            String text = cleanLines.get(i++);
            List<String> opcions = new ArrayList<>();

            if (tipus.equals("UNICA") || tipus.equals("ORDENADA") || tipus.equals("MULTIPLE")) {
                if (i < cleanLines.size()) {
                    try {
                        int numOpcions = Integer.parseInt(cleanLines.get(i++));
                        for (int k = 0; k < numOpcions && i < cleanLines.size(); k++) {
                            opcions.add(cleanLines.get(i++));
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            llista.add(new PreguntaInfo(tipus, text, opcions));
            if (i < cleanLines.size() && cleanLines.get(i).equals("- - -"))
                i++;
        }
        return llista;
    }

    private String extraerRespostaUI() {
        if (preguntes == null || preguntaActual >= preguntes.size())
            return "";
        PreguntaInfo p = preguntes.get(preguntaActual);

        if (p.tipus.equals("NUMERICA")) {
            return textFieldActual != null ? textFieldActual.getText().trim() : "";
        } else if (p.tipus.equals("LLIURE")) {
            return textAreaActual != null ? textAreaActual.getText().trim() : "";
        } else if (p.tipus.equals("UNICA") || p.tipus.equals("ORDENADA")) {
            if (buttonGroupActual == null)
                return "";
            int index = 0;
            for (Enumeration<AbstractButton> buttons = buttonGroupActual.getElements(); buttons.hasMoreElements();) {
                AbstractButton button = buttons.nextElement();
                if (button.isSelected())
                    return String.valueOf(index);
                index++;
            }
            return "";
        } else if (p.tipus.equals("MULTIPLE")) {
            if (checkBoxesActuals == null)
                return "";
            List<String> indices = new ArrayList<>();
            for (int i = 0; i < checkBoxesActuals.size(); i++) {
                if (checkBoxesActuals.get(i).isSelected())
                    indices.add(String.valueOf(i));
            }
            return String.join(",", indices);
        }
        return "";
    }
}
