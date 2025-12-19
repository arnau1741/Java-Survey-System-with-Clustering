package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import prop.enquestes.excepcions.InvalidFormatEnquesta;
import prop.enquestes.excepcions.UsuariNoValid;

/**
 * Diàleg per a la creació de noves enquestes.
 * <p>
 * Aquesta vista permet a l'usuari (normalment amb rol Enquestador o Administrador)
 * definir el títol, la descripció i les preguntes d'una nova enquesta.
 * Admet diferents tipus de preguntes (Lliure, Múltiple, Numèrica, Ordenada, Única)
 * i gestiona la introducció de les seves opcions corresponents.
 * </p>
 */
public class VistaCrearEnquesta extends JDialog {
    private CtrlPresentacio ctrl;
    private int idCreador;

    private JPanel contentPane = new JPanel();
    private JTextField titolEnq = new JTextField();
    private JTextField descripcioEnq = new JTextField();
    private JButton afegirPreguntaButton = new JButton("Afegir Pregunta");
    private JButton eliminarPreguntaButton = new JButton("Eliminar Pregunta");

    private DefaultListModel<String> preguntesModel = new DefaultListModel<>();
    private JList<String> llistaPreguntes = new JList<>(preguntesModel);

    // Aquí guardarem les preguntes per construir el format final
    // Cada pregunta és: [ tipusInt , text , List<String> opcions ]
    private List<List<Object>> preguntes = new ArrayList<>();

    private JButton buttonOK = new JButton("OK");
    private JButton buttonCancel = new JButton("Cancelar");

    /**
     * Constructor de la vista de creació d'enquestes.
     * Inicialitza la finestra, configura el layout i assigna els escoltadors d'esdeveniments.
     *
     * @param ctrl Referència al controlador de presentació.
     * @param idUsuari Identificador de l'usuari que crea l'enquesta.
     */
    public VistaCrearEnquesta(CtrlPresentacio ctrl, int idUsuari) {
        this.ctrl = ctrl;
        this.idCreador = idUsuari;

        setTitle("Crear Enquesta");
        setModal(true);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        initLayout();
        initActions();

        pack();
        setLocationRelativeTo(null);

        buttonOK.addActionListener(e -> {
            try {
                onOK();
            } catch (InvalidFormatEnquesta ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { onCancel(); }
        });

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Configura la distribució dels components gràfics (Layout).
     * Organitza els camps de text per al títol i descripció a la part superior,
     * la llista de preguntes al centre i els botons d'acció a la part inferior.
     */
    private void initLayout() {
        contentPane.setLayout(new BorderLayout(8, 8));

        JPanel topPanel = new JPanel(new GridLayout(2, 2, 6, 6));
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
        topPanel.add(new JLabel("Títol:"));
        topPanel.add(titolEnq);
        topPanel.add(new JLabel("Descripció:"));
        topPanel.add(descripcioEnq);
        contentPane.add(topPanel, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(6, 6));
        center.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        llistaPreguntes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(llistaPreguntes);
        center.add(scroll, BorderLayout.CENTER);

        JPanel listButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        listButtons.add(afegirPreguntaButton);
        listButtons.add(eliminarPreguntaButton);
        center.add(listButtons, BorderLayout.SOUTH);

        contentPane.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        bottom.add(buttonOK);
        bottom.add(buttonCancel);
        contentPane.add(bottom, BorderLayout.SOUTH);

        titolEnq.setColumns(30);
        descripcioEnq.setColumns(30);
        llistaPreguntes.setVisibleRowCount(8);
    }

    /**
     * Converteix el nom del tipus de pregunta en el seu codi enter corresponent.
     *
     * @param tipus Nom del tipus de pregunta (ex: "Numèrica", "Única").
     * @return Enter que representa el tipus (0=Numèrica, 1=Única, 2=Múltiple, 3=Ordenada, 4=Lliure) o -1 si no és vàlid.
     */
    private int tipusPreguntaAInt(String tipus) {
        switch (tipus) {
            case "Numèrica": return 0;
            case "Única": return 1;
            case "Múltiple": return 2;
            case "Ordenada": return 3;
            case "Lliure": return 4;
            default: return -1;
        }
    }

    /**
     * Defineix la lògica dels botons "Afegir Pregunta" i "Eliminar Pregunta".
     * Gestiona els diàlegs d'entrada de dades per a cada tipus de pregunta i les seves opcions.
     */
    private void initActions() {
        afegirPreguntaButton.addActionListener(e -> {
            String[] tipusValids = {"Lliure", "Múltiple", "Numèrica", "Ordenada", "Única"};
            String tipus = (String) JOptionPane.showInputDialog(
                    this, "Selecciona el tipus de pregunta:",
                    "Tipus de pregunta",
                    JOptionPane.PLAIN_MESSAGE,
                    null, tipusValids, tipusValids[0]
            );

            if (tipus == null) return;

            int tipusInt = tipusPreguntaAInt(tipus);

            String text = JOptionPane.showInputDialog(this, "Text de la pregunta:");
            if (text == null || text.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La pregunta no pot ser buida.");
                return;
            }
            text = text.trim();

            List<String> opcions = new ArrayList<>();

            if (tipusInt != 4 && tipusInt != 0) {
                boolean afegint = true;
                while (afegint) {
                    Object[] opts = {"Afegir", "Finalitzar"};
                    int r = JOptionPane.showOptionDialog(
                            this,
                            "Introdueix una nova opció:",
                            "Opcions",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null, opts, opts[0]
                    );

                    if (r == 1 || r == JOptionPane.CLOSED_OPTION) break;

                    String op = JOptionPane.showInputDialog(this, "Text de l'opció:");
                    if (op == null || op.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "L'opció no pot ser buida.");
                    } else {
                        opcions.add(op.trim());
                    }
                }

                if (opcions.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Aquest tipus de pregunta necessita opcions.");
                    return;
                }
            }

            List<Object> pregunta = new ArrayList<>();
            pregunta.add(tipusInt);
            pregunta.add(text);
            pregunta.add(opcions);
            preguntes.add(pregunta);

            String mostrable = tipus + " — " + text;
            if (!opcions.isEmpty()) mostrable += " (" + opcions.size() + " opcions)";
            preguntesModel.addElement(mostrable);
        });

        eliminarPreguntaButton.addActionListener(e -> {
            int idx = llistaPreguntes.getSelectedIndex();
            if (idx >= 0) {
                int resp = JOptionPane.showConfirmDialog(this,
                        "Eliminar la pregunta seleccionada?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    preguntesModel.remove(idx);
                    preguntes.remove(idx);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una pregunta per eliminar.");
            }
        });
    }

    /**
     * Valida les dades introduïdes i envia la sol·licitud de creació d'enquesta al controlador.
     *
     * @throws InvalidFormatEnquesta Si el format de les preguntes no és correcte.
     */
    private void onOK() throws InvalidFormatEnquesta {
        String titol = titolEnq.getText().trim();
        if (titol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Has d'introduir un títol.");
            return;
        }

        String descripcio = descripcioEnq.getText().trim();
        if (descripcio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Has d'introduir una descripció.");
            return;
        }

        List<String> preguntesTxt = getPreguntes();

        try {
            ctrl.crearEnquesta(titol, descripcio, idCreador, preguntesTxt);
            JOptionPane.showMessageDialog(this, "Enquesta creada correctament!");
            dispose();
            ctrl.mostrarVistaPrincipalComuna(idCreador);
        } catch (UsuariNoValid e) {
            JOptionPane.showMessageDialog(this,
                    "Error de l'usuari: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);

        } catch (InvalidFormatEnquesta e) {
            JOptionPane.showMessageDialog(this,
                    "Error del format: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cancel·la l'operació, tanca la finestra i torna a la presentació inicial.
     */
    private void onCancel() {
        dispose();
        ctrl.mostrarVistaPrincipalComuna(idCreador);
    }

    /**
     * Construeix la llista plana d'Strings que defineix l'estructura de les preguntes.
     * Aquest format és l'esperat pel controlador de domini per crear l'enquesta.
     *
     * @return Llista d'Strings amb el format [tipus, text, (numOpcions, opcio1...)?] per a cada pregunta.
     */
    public List<String> getPreguntes() {
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
}