package prop.enquestes.presentacio;

import prop.enquestes.excepcions.InvalidFormatEnquesta;
import prop.enquestes.excepcions.UsuariNoValid;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Diàleg per a la creació de noves enquestes.
 */
public class VistaCrearEnquesta extends JDialog {
    private CtrlPresentacio ctrl;
    private int idCreador;

    private JPanel contentPane;
    private JTextField titolEnq;
    private JTextArea descripcioEnq;
    private DefaultListModel<String> preguntesModel;
    private JList<String> llistaPreguntes;

    // Stores questions: [tipusInt, text, List<String> opcions]
    private List<List<Object>> preguntes = new ArrayList<>();

    public VistaCrearEnquesta(CtrlPresentacio ctrl, int idUsuari) {
        this.ctrl = ctrl;
        this.idCreador = idUsuari;

        UIHelper.configureDialog(this, "Crear Enquesta");

        initUI();
        pack();
        setSize(600, 500);
        setLocationRelativeTo(null);
    }

    private void initUI() {
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBackground(UIHelper.COLOR_BACKGROUND);
        contentPane.setBorder(UIHelper.PADDING_MAIN);
        setContentPane(contentPane);

        // --- TOP: Info Enquesta ---
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(UIHelper.COLOR_BACKGROUND);
        topPanel.setBorder(BorderFactory.createTitledBorder("Informació General"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        topPanel.add(new JLabel("Títol:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        titolEnq = new JTextField(20);
        topPanel.add(titolEnq, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        topPanel.add(new JLabel("Descripció:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        descripcioEnq = new JTextArea(3, 20);
        descripcioEnq.setLineWrap(true);
        descripcioEnq.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        topPanel.add(new JScrollPane(descripcioEnq), gbc);

        contentPane.add(topPanel, BorderLayout.NORTH);

        // --- CENTER: Preguntes ---
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBackground(UIHelper.COLOR_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createTitledBorder("Preguntes"));

        preguntesModel = new DefaultListModel<>();
        llistaPreguntes = new JList<>(preguntesModel);
        llistaPreguntes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        centerPanel.add(new JScrollPane(llistaPreguntes), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(UIHelper.COLOR_BACKGROUND);

        JButton btnAdd = UIHelper.createButton("Afegir Pregunta", e -> showDialogAfegirPregunta());
        JButton btnDel = UIHelper.createButton("Eliminar Pregunta", e -> eliminarPregunta());

        btnPanel.add(btnAdd);
        btnPanel.add(btnDel);
        centerPanel.add(btnPanel, BorderLayout.SOUTH);

        contentPane.add(centerPanel, BorderLayout.CENTER);

        // --- BOTTOM: Actions ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(UIHelper.COLOR_BACKGROUND);

        JButton btnCreate = UIHelper.createButton("Crear Enquesta", e -> onOK());
        JButton btnCancel = UIHelper.createButton("Cancel·lar", e -> onCancel());

        bottomPanel.add(btnCreate);
        bottomPanel.add(btnCancel);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
    }

    // --- Logic ---

    private void showDialogAfegirPregunta() {
        DialogAfegirPregunta dialog = new DialogAfegirPregunta(this);
        dialog.setVisible(true);
        // Execution continues after dialog closes

        if (dialog.isConfirmed()) {
            List<Object> novaPregunta = new ArrayList<>();
            novaPregunta.add(dialog.getEncodedType());
            novaPregunta.add(dialog.getText());
            novaPregunta.add(dialog.getOptions());

            preguntes.add(novaPregunta);

            String display = dialog.getTypeName() + ": " + dialog.getText();
            if (!dialog.getOptions().isEmpty()) {
                display += " [" + dialog.getOptions().size() + " opcions]";
            }
            preguntesModel.addElement(display);
        }
    }

    private void eliminarPregunta() {
        int idx = llistaPreguntes.getSelectedIndex();
        if (idx == -1) {
            UIHelper.showWarning(this, "Selecciona una pregunta per eliminar.");
            return;
        }
        if (UIHelper.showConfirm(this, "Segur que vols eliminar aquesta pregunta?")) {
            preguntes.remove(idx);
            preguntesModel.remove(idx);
        }
    }

    private void onOK() {
        String titol = titolEnq.getText().trim();
        String desc = descripcioEnq.getText().trim();

        if (titol.isEmpty()) {
            UIHelper.showWarning(this, "El títol és obligatori.");
            return;
        }
        if (desc.isEmpty()) {
            UIHelper.showWarning(this, "La descripció és obligatòria.");
            return;
        }
        if (preguntes.isEmpty()) {
            UIHelper.showWarning(this, "Has d'afegir almenys una pregunta.");
            return;
        }

        try {
            ctrl.crearEnquesta(titol, desc, idCreador, buildPreguntesStrings());
            UIHelper.showInfo(this, "Enquesta creada correctament.");
            dispose();
            ctrl.mostrarVistaPrincipalComuna(idCreador);
        } catch (Exception e) {
            UIHelper.showError(this, "Error creant enquesta: " + e.getMessage());
        }
    }

    private void onCancel() {
        dispose();
        ctrl.mostrarVistaPrincipalComuna(idCreador);
    }

    private List<String> buildPreguntesStrings() {
        List<String> out = new ArrayList<>();
        for (List<Object> p : preguntes) {
            int tipus = (int) p.get(0);
            String text = (String) p.get(1);
            List<String> opcions = (List<String>) p.get(2);

            out.add(String.valueOf(tipus));
            out.add(text);

            if (tipus == 1 || tipus == 2 || tipus == 3) {
                out.add(String.valueOf(opcions.size()));
                out.addAll(opcions);
            }
        }
        return out;
    }

    // --- Inner Dialog Class for proper UX ---
    class DialogAfegirPregunta extends JDialog {
        private boolean confirmed = false;
        private JComboBox<String> comboTipus;
        private JTextField txtText;
        private DefaultListModel<String> optionsModel;
        private JTextField txtOption;

        // 0=Num, 1=Unique, 2=Multi, 3=Ord, 4=Free
        // Mapped to Combo: Numèrica, Única, Múltiple, Ordenada, Lliure
        // Note: The original code had mapping:
        // "Numèrica": 0, "Única": 1, "Múltiple": 2, "Ordenada": 3, "Lliure": 4
        // WAIT! Original Pregunta.java:
        // 0=Num, 1=Unica, 2=Ord, 3=Multi, 4=Lliure
        // Wait, let me check Enquesta logic in refactor.
        // Enquesta.java refactor:
        // TIPUS_NUMERICA = 0
        // TIPUS_UNICA = 1
        // TIPUS_ORDENADA = 2
        // TIPUS_MULTIPLE = 3
        // TIPUS_LLIURE = 4

        // So I must stick to this logic!

        public DialogAfegirPregunta(JDialog owner) {
            super(owner, "Afegir Pregunta", true);
            init();
            pack();
            setLocationRelativeTo(owner);
        }

        private void init() {
            JPanel p = new JPanel(new BorderLayout(5, 5));
            p.setBorder(new EmptyBorder(10, 10, 10, 10));
            setContentPane(p);

            // Form
            JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));

            form.add(new JLabel("Tipus:"));
            comboTipus = new JComboBox<>(new String[] { "Numèrica", "Única", "Ordenada", "Múltiple", "Lliure" });
            form.add(comboTipus);

            form.add(new JLabel("Text de la pregunta:"));
            txtText = new JTextField(30);
            form.add(txtText);

            p.add(form, BorderLayout.NORTH);

            // Options Panel (only for Unica, Ordenada, Multiple)
            JPanel optsPanel = new JPanel(new BorderLayout(5, 5));
            optsPanel.setBorder(BorderFactory.createTitledBorder("Opcions (Només per Única/Ordenada/Múltiple)"));

            optionsModel = new DefaultListModel<>();
            JList<String> listOpts = new JList<>(optionsModel);
            optsPanel.add(new JScrollPane(listOpts), BorderLayout.CENTER);

            JPanel inputOptPanel = new JPanel(new BorderLayout());
            txtOption = new JTextField();
            JButton btnAddOpt = new JButton("+");
            JButton btnDelOpt = new JButton("-");

            JPanel miniBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            miniBtns.add(btnAddOpt);
            miniBtns.add(btnDelOpt);

            inputOptPanel.add(txtOption, BorderLayout.CENTER);
            inputOptPanel.add(miniBtns, BorderLayout.EAST);

            optsPanel.add(inputOptPanel, BorderLayout.SOUTH);

            p.add(optsPanel, BorderLayout.CENTER);

            // Buttons
            JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnOk = new JButton("Afegir");
            JButton btnCancel = new JButton("Cancel·lar");
            btns.add(btnOk);
            btns.add(btnCancel);
            p.add(btns, BorderLayout.SOUTH);

            // Logic
            comboTipus.addActionListener(e -> updateOptionsState());
            updateOptionsState(); // init

            btnAddOpt.addActionListener(e -> addOption());
            btnDelOpt.addActionListener(e -> {
                int idx = listOpts.getSelectedIndex();
                if (idx != -1)
                    optionsModel.remove(idx);
            });

            btnOk.addActionListener(e -> {
                if (validateInput()) {
                    confirmed = true;
                    dispose();
                }
            });
            btnCancel.addActionListener(e -> dispose());
        }

        private void updateOptionsState() {
            String t = (String) comboTipus.getSelectedItem();
            boolean needsOptions = t.equals("Única") || t.equals("Ordenada") || t.equals("Múltiple");
            txtOption.setEnabled(needsOptions);
        }

        private void addOption() {
            String t = txtOption.getText().trim();
            if (!t.isEmpty()) {
                optionsModel.addElement(t);
                txtOption.setText("");
                txtOption.requestFocus();
            }
        }

        private boolean validateInput() {
            if (txtText.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Text obligatori.");
                return false;
            }
            String t = (String) comboTipus.getSelectedItem();
            boolean needsOptions = t.equals("Única") || t.equals("Ordenada") || t.equals("Múltiple");
            if (needsOptions && optionsModel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aquest tipus requereix opcions.");
                return false;
            }
            return true;
        }

        public boolean isConfirmed() {
            return confirmed;
        }

        public String getText() {
            return txtText.getText().trim();
        }

        public List<String> getOptions() {
            List<String> l = new ArrayList<>();
            for (int i = 0; i < optionsModel.size(); i++)
                l.add(optionsModel.get(i));
            return l;
        }

        public int getEncodedType() {
            String t = (String) comboTipus.getSelectedItem();
            if (t.equals("Numèrica"))
                return 0;
            if (t.equals("Única"))
                return 1;
            if (t.equals("Ordenada"))
                return 2;
            if (t.equals("Múltiple"))
                return 3;
            return 4; // Lliure
        }

        public String getTypeName() {
            return (String) comboTipus.getSelectedItem();
        }
    }
}