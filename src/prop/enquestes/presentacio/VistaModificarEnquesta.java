package prop.enquestes.presentacio;

import prop.enquestes.excepcions.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Diàleg per a la gestió i modificació d'enquestes.
 * <p>
 * Aquesta vista ofereix funcionalitats per a:
 * <ul>
 * <li>Consultar informació detallada d'una enquesta existent.</li>
 * <li>Modificar preguntes específiques d'una enquesta.</li>
 * <li>Eliminar completament una enquesta.</li>
 * <li>Eliminar respostes d'un usuari concret en una enquesta.</li>
 * </ul>
 * </p>
 */
public class VistaModificarEnquesta extends JDialog {
    private CtrlPresentacio ctrl;
    private int idUsuariActual;

    private int idEnquestaActual = -1;

    // Components
    private JComboBox<String> comboEnquestes;
    private JTextArea areaInfo; // Para mostrar descripción general

    // Master-Detail
    private DefaultListModel<String> listModelPreguntes;
    private JList<String> listPreguntes;

    // Editor
    private JTextField txtIndex; // Readonly or hidden
    private JComboBox<String> comboTipus;
    private JTextArea txtPregunta;
    private DefaultListModel<String> listModelOpcions;
    private JList<String> listOpcions;
    private JTextField txtNewOpcio;
    private JButton btnAddOpt, btnDelOpt;
    private JButton btnGuardarCanvis;

    // Actions
    private JButton btnEliminarEnquesta;
    private JButton btnEliminarRespostes;

    /**
     * Constructor de la vista de modificació d'enquestes.
     * Inicialitza els components, carrega la llista d'enquestes i configura els controladors.
     *
     * @param ctrl Referència al controlador de presentació.
     * @param idUsuariActual Identificador de l'usuari que realitza la gestió.
     */
    public VistaModificarEnquesta(CtrlPresentacio ctrl, int idUsuariActual) {
        super((Frame) null, "Gestió d'Enquestes", true);
        this.ctrl = ctrl;
        this.idUsuariActual = idUsuariActual;

        UIHelper.configureDialog(this, "Gestió d'Enquestes");
        setSize(900, 700);
        setLocationRelativeTo(null);

        initUI();
        cargarEnquestes();
    }
    /**
     * Inicialitza i organitza tots els components gràfics de la finestra.
     * Divideix la interfície en seccions clares: Selecció, Modificació, Eliminació d'enquesta i Eliminació de respostes.
     */
    private void initUI() {
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBackground(UIHelper.COLOR_BACKGROUND);
        content.setBorder(UIHelper.PADDING_MAIN);
        setContentPane(content);

        // --- TOP: Selector ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(UIHelper.COLOR_BACKGROUND);

        topPanel.add(new JLabel("Enquesta:"));
        comboEnquestes = new JComboBox<>();
        comboEnquestes.setPreferredSize(new Dimension(300, 25));
        comboEnquestes.addActionListener(e -> onSelectEnquesta());
        topPanel.add(comboEnquestes);

        content.add(topPanel, BorderLayout.NORTH);

        // --- CENTER: Split Pane (Questions List | Editor) ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.3);

        // LEFT: List
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Preguntes"));
        listModelPreguntes = new DefaultListModel<>();
        listPreguntes = new JList<>(listModelPreguntes);
        listPreguntes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPreguntes.addListSelectionListener(e -> onSelectPregunta());
        leftPanel.add(new JScrollPane(listPreguntes), BorderLayout.CENTER);

        splitPane.setLeftComponent(leftPanel);

        // RIGHT: Editor
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Editor de Pregunta"));

        // Editor Form
        JPanel form = new JPanel(new GridBagLayout());
        // ... (Layout code for form) ...
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tipus
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        form.add(new JLabel("Tipus:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        comboTipus = new JComboBox<>(new String[] { "Numèrica", "Única", "Ordenada", "Múltiple", "Lliure" });
        comboTipus.addActionListener(e -> updateEditorState());
        form.add(comboTipus, gbc);

        // Text
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        form.add(new JLabel("Text:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        txtPregunta = new JTextArea(3, 20);
        txtPregunta.setLineWrap(true);
        form.add(new JScrollPane(txtPregunta), gbc);

        // Opcions
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        form.add(new JLabel("Opcions:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        JPanel pOpt = new JPanel(new BorderLayout());
        listModelOpcions = new DefaultListModel<>();
        listOpcions = new JList<>(listModelOpcions);
        pOpt.add(new JScrollPane(listOpcions), BorderLayout.CENTER);

        JPanel pOptInput = new JPanel(new BorderLayout());
        txtNewOpcio = new JTextField();
        JPanel pOptBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnAddOpt = new JButton("+");
        btnDelOpt = new JButton("-");
        pOptBtns.add(btnAddOpt);
        pOptBtns.add(btnDelOpt);
        pOptInput.add(txtNewOpcio, BorderLayout.CENTER);
        pOptInput.add(pOptBtns, BorderLayout.EAST);

        pOpt.add(pOptInput, BorderLayout.SOUTH);

        form.add(pOpt, gbc);

        // Save Button
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        btnGuardarCanvis = UIHelper.createButton("Guardar Canvis a la Pregunta", e -> guardarPregunta());
        form.add(btnGuardarCanvis, gbc);

        rightPanel.add(form, BorderLayout.CENTER);
        splitPane.setRightComponent(rightPanel);

        content.add(splitPane, BorderLayout.CENTER);

        // --- BOTTOM: Global Actions ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Accions Globals"));

        btnEliminarEnquesta = UIHelper.createButton("Eliminar Enquesta", e -> eliminarEnquesta());
        btnEliminarEnquesta.setForeground(Color.RED);

        btnEliminarRespostes = UIHelper.createButton("Eliminar Respostes d'Usuari...", e -> eliminarRespostes());

        JButton btnClose = UIHelper.createButton("Tancar", e -> dispose());

        bottomPanel.add(btnEliminarEnquesta);
        bottomPanel.add(btnEliminarRespostes);
        bottomPanel.add(Box.createHorizontalStrut(50));
        bottomPanel.add(btnClose);

        content.add(bottomPanel, BorderLayout.SOUTH);

        // Listener logic
        btnAddOpt.addActionListener(e -> {
            String t = txtNewOpcio.getText().trim();
            if (!t.isEmpty()) {
                listModelOpcions.addElement(t);
                txtNewOpcio.setText("");
            }
        });
        btnDelOpt.addActionListener(e -> {
            int idx = listOpcions.getSelectedIndex();
            if (idx != -1)
                listModelOpcions.remove(idx);
        });

        // Initial state
        setEditorEnabled(false);
    }

    private void setEditorEnabled(boolean b) {
        comboTipus.setEnabled(b);
        txtPregunta.setEnabled(b);
        listOpcions.setEnabled(b);
        txtNewOpcio.setEnabled(b);
        btnAddOpt.setEnabled(b);
        btnDelOpt.setEnabled(b);
        btnGuardarCanvis.setEnabled(b);
    }

    private void updateEditorState() {
        if (!comboTipus.isEnabled())
            return;
        String t = (String) comboTipus.getSelectedItem();
        boolean hasOpts = t.equals("Única") || t.equals("Ordenada") || t.equals("Múltiple");
        listOpcions.setEnabled(hasOpts);
        txtNewOpcio.setEnabled(hasOpts);
        btnAddOpt.setEnabled(hasOpts);
        btnDelOpt.setEnabled(hasOpts);
        if (!hasOpts) {
            listModelOpcions.clear();
            txtNewOpcio.setText("");
        }
    }

    // --- Loading Logic ---

    private void cargarEnquestes() {
        comboEnquestes.removeAllItems();
        comboEnquestes.addItem("-- Selecciona --");
        try {
            List<String> enquestasInfo = ctrl.obtenirEnquestesAdministrades(idUsuariActual);
            for (String info : enquestasInfo) {
                String[] lineas = info.split("\n");
                for (String linea : lineas) {
                    if (linea.trim().startsWith("ID:")) {
                        comboEnquestes.addItem(linea);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Error carregant enquestes: " + e.getMessage());
        }
    }

    private void onSelectEnquesta() {
        String sel = (String) comboEnquestes.getSelectedItem();
        listModelPreguntes.clear();
        setEditorEnabled(false);

        if (sel == null || sel.equals("-- Selecciona --")) {
            idEnquestaActual = -1;
            return;
        }

        try {
            idEnquestaActual = extraerIdEnquesta(sel);
            List<String> raw = ctrl.obtenirPreguntesEnquesta(idEnquestaActual);
            // Parse to fill list
            // We need a custom parser that keeps the structure to populate the list
            // "1. Text de la pregunta [TIPUS]"
            List<String> texts = parseQuestionTitles(raw);
            for (String t : texts)
                listModelPreguntes.addElement(t);

        } catch (Exception e) {
            UIHelper.showError(this, "Error carregant preguntes: " + e.getMessage());
        }
    }

    // Helper to get titles for the list
    private List<String> parseQuestionTitles(List<String> rawLines) {
        List<String> titles = new ArrayList<>();
        // Simple state machine again...
        // Format of rawLines is from Enquesta.getPreguntes() which returns [Type, Text,
        // (NumOpts, Opts...)?]
        // Actually Enquesta.getPreguntes() returns a flattened list.
        // I need to parse it robustly.

        List<String> clean = new ArrayList<>();
        boolean start = false;
        for (String s : rawLines) {
            if (s.trim().equals("Preguntes:")) {
                start = true;
                continue;
            }
            if (!start)
                continue;
            if (s.startsWith("- "))
                clean.add(s.substring(2));
            else
                clean.add(s);
        }

        int i = 0;
        while (i < clean.size()) {
            String type = clean.get(i++);
            if (type == null || type.trim().equals("- - -") || type.trim().isEmpty())
                continue;
            // Validate type
            if (!type.equals("NUMERICA") && !type.equals("UNICA") && !type.equals("ORDENADA") &&
                    !type.equals("MULTIPLE") && !type.equals("LLIURE"))
                continue;

            if (i >= clean.size())
                break;
            String text = clean.get(i++);
            titles.add(text + " [" + type + "]");

            // Consume options
            if (type.equals("UNICA") || type.equals("ORDENADA") || type.equals("MULTIPLE")) {
                if (i < clean.size()) {
                    try {
                        int n = Integer.parseInt(clean.get(i++));
                        for (int k = 0; k < n; k++)
                            i++;
                    } catch (Exception ignored) {
                    }
                }
            }
            if (i < clean.size() && clean.get(i).equals("- - -"))
                i++;
        }
        return titles;
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

    // --- Editor Logic ---

    private void onSelectPregunta() {
        int idx = listPreguntes.getSelectedIndex();
        if (idx == -1) {
            setEditorEnabled(false);
            return;
        }
        setEditorEnabled(true);
        loadPreguntaToEditor(idx);
    }

    private void loadPreguntaToEditor(int idx) {
        // Need to parse again to get full data... this is inefficient (O(N^2) if done
        // poorly),
        // but N is small.
        // Better: caching parsed questions. But for "cleaning views", I will re-parse.
        try {
            List<String> raw = ctrl.obtenirPreguntesEnquesta(idEnquestaActual);
            // Re-use logic to find specific question
            // I will copy-paste parser logic but return the object for index `idx`
            // Ideally I should have had a `Ctrl.getPregunta(idEnquesta, idx)` but I have
            // `modificarPregunta`...
            // I have to interpret "obtenirPreguntesEnquesta" output.

            // ... (Parser logic similar to VistaRespondreEnquesta) ...
            List<PreguntaInfo> allParams = parsearPreguntesFull(raw);
            if (idx < allParams.size()) {
                PreguntaInfo p = allParams.get(idx);
                // Map TIPUS string to Combo
                // NUMERICA -> Numèrica
                // UNICA -> Única
                // ORDENADA -> Ordenada
                // MULTIPLE -> Múltiple
                // LLIURE -> Lliure
                String comboVal = "Numèrica";
                if (p.tipus.equals("UNICA"))
                    comboVal = "Única";
                else if (p.tipus.equals("ORDENADA"))
                    comboVal = "Ordenada";
                else if (p.tipus.equals("MULTIPLE"))
                    comboVal = "Múltiple";
                else if (p.tipus.equals("LLIURE"))
                    comboVal = "Lliure";

                comboTipus.setSelectedItem(comboVal);
                txtPregunta.setText(p.text);

                listModelOpcions.clear();
                if (p.opcions != null) {
                    for (String op : p.opcions)
                        listModelOpcions.addElement(op);
                }
                updateEditorState();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper class
    private static class PreguntaInfo {
        String tipus;
        String text;
        List<String> opcions;

        PreguntaInfo(String t, String x, List<String> o) {
            tipus = t;
            text = x;
            opcions = o;
        }
    }

    private List<PreguntaInfo> parsearPreguntesFull(List<String> rawLines) {
        // ... (Same parser as VistaRespondreEnquesta) ...
        // I should have put this in a helper class or Ctrl, but I can't touch Ctrl
        // easily.
        // Copying here is "minimal changes" to architecture vs "clean code"...

        // Copied logic:
        List<PreguntaInfo> llista = new ArrayList<>();
        List<String> clean = new ArrayList<>();
        boolean start = false;
        for (String s : rawLines) {
            if (s.trim().equals("Preguntes:")) {
                start = true;
                continue;
            }
            if (!start)
                continue;
            if (s.startsWith("- "))
                clean.add(s.substring(2));
            else
                clean.add(s);
        }
        int i = 0;
        while (i < clean.size()) {
            String type = clean.get(i++);
            if (type == null || type.trim().equals("- - -") || type.trim().isEmpty())
                continue;
            if (!type.equals("NUMERICA") && !type.equals("UNICA") && !type.equals("ORDENADA") &&
                    !type.equals("MULTIPLE") && !type.equals("LLIURE"))
                continue;
            if (i >= clean.size())
                break;
            String text = clean.get(i++);
            List<String> opcions = new ArrayList<>();
            if (type.equals("UNICA") || type.equals("ORDENADA") || type.equals("MULTIPLE")) {
                if (i < clean.size()) {
                    try {
                        int n = Integer.parseInt(clean.get(i++));
                        for (int k = 0; k < n; k++)
                            opcions.add(clean.get(i++));
                    } catch (Exception ignored) {
                    }
                }
            }
            llista.add(new PreguntaInfo(type, text, opcions));
            if (i < clean.size() && clean.get(i).equals("- - -"))
                i++;
        }
        return llista;
    }

    private void guardarPregunta() {
        int idx = listPreguntes.getSelectedIndex();
        if (idx == -1)
            return;

        try {
            List<String> novaPregunta = new ArrayList<>();
            // Map Combo back to UPPERCASE ID
            String comboVal = (String) comboTipus.getSelectedItem();
            int tipoInt = 0; // NUMERICA
            if (comboVal.equals("Única"))
                tipoInt = 1;
            else if (comboVal.equals("Ordenada"))
                tipoInt = 2;
            else if (comboVal.equals("Múltiple"))
                tipoInt = 3;
            else if (comboVal.equals("Lliure"))
                tipoInt = 4;

            novaPregunta.add(String.valueOf(tipoInt));

            String text = txtPregunta.getText().trim();
            if (text.isEmpty()) {
                UIHelper.showWarning(this, "El text és obligatori.");
                return;
            }
            novaPregunta.add(text);

            if (tipoInt == 1 || tipoInt == 2 || tipoInt == 3) {
                if (listModelOpcions.isEmpty()) {
                    UIHelper.showWarning(this, "Afegeix opcions.");
                    return;
                }
                novaPregunta.add(String.valueOf(listModelOpcions.size()));
                for (int i = 0; i < listModelOpcions.size(); i++)
                    novaPregunta.add(listModelOpcions.get(i));
            }

            ctrl.modificarPreguntaEnquesta(idUsuariActual, idEnquestaActual, idx, novaPregunta);
            UIHelper.showInfo(this, "Pregunta modificada!");
            onSelectEnquesta(); // Refresh

        } catch (Exception e) {
            UIHelper.showError(this, "Error modificant: " + e.getMessage());
        }
    }

    private void eliminarEnquesta() {
        if (idEnquestaActual == -1)
            return;
        if (UIHelper.showConfirm(this, "Segur que vols eliminar aquesta enquesta? Acció irreversible.")) {
            try {
                ctrl.eliminarEnquesta(idUsuariActual, idEnquestaActual);
                UIHelper.showInfo(this, "Enquesta eliminada.");
                cargarEnquestes();
                onSelectEnquesta();
            } catch (Exception e) {
                UIHelper.showError(this, "Error: " + e.getMessage());
            }
        }
    }

    private void eliminarRespostes() {
        if (idEnquestaActual == -1)
            return;
        String idUserStr = JOptionPane.showInputDialog(this,
                "Introdueix l'ID de l'usuari del qual vols esborrar les respostes:");
        if (idUserStr == null || idUserStr.trim().isEmpty())
            return;

        try {
            int idUserTarget = Integer.parseInt(idUserStr);
            if (UIHelper.showConfirm(this, "Eliminar respostes de l'usuari " + idUserTarget + "?")) {
                ctrl.esborrarRespostaEnquesta(idUsuariActual, idEnquestaActual, idUserTarget);
                UIHelper.showInfo(this, "Respostes eliminades.");
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Error: " + e.getMessage());
        }
    }
}