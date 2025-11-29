package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VistaModificarEnquesta extends JDialog {
    private CtrlPresentacio ctrl;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonEliminar;
    private JComboBox<String> comboEnquestes;
    private JTextArea areaPreguntes;
    private JButton buttonModificarPregunta;
    private JTextField campIndexPregunta;
    private JTextField campTipusPregunta;
    private JTextArea campTextPregunta;
    private JTextArea campOpcionsPregunta;

    public VistaModificarEnquesta(CtrlPresentacio ctrl) {
        super((Frame) null, "Modificar Enquesta", true);
        this.ctrl = ctrl;

        setSize(600, 500);
        setLocationRelativeTo(null);

        inicializarComponentes();
        cargarEnquestes();

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        buttonEliminar.addActionListener(e -> onEliminar());
        buttonModificarPregunta.addActionListener(e -> onModificarPregunta());

        comboEnquestes.addActionListener(e -> onEnquestaSeleccionada());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void inicializarComponentes() {
        contentPane = new JPanel(new BorderLayout(10, 10));

        // Panel superior - Selección de enquesta
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Selecciona enquesta:"));
        comboEnquestes = new JComboBox<>();
        comboEnquestes.setPreferredSize(new Dimension(300, 25));
        panelSuperior.add(comboEnquestes);

        // Panel central - Información y modificación
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 10));

        // Panel izquierdo - Preguntas actuales
        JPanel panelPreguntes = new JPanel(new BorderLayout());
        panelPreguntes.setBorder(BorderFactory.createTitledBorder("Preguntes actuals"));
        areaPreguntes = new JTextArea(20, 30);
        areaPreguntes.setEditable(false);
        JScrollPane scrollPreguntes = new JScrollPane(areaPreguntes);
        panelPreguntes.add(scrollPreguntes, BorderLayout.CENTER);

        // Panel derecho - Modificación
        JPanel panelModificacion = new JPanel(new BorderLayout());
        panelModificacion.setBorder(BorderFactory.createTitledBorder("Modificar pregunta"));

        JPanel panelInputs = new JPanel(new GridLayout(4, 2, 5, 5));
        panelInputs.add(new JLabel("Índex pregunta:"));
        campIndexPregunta = new JTextField();
        panelInputs.add(campIndexPregunta);

        panelInputs.add(new JLabel("Tipus (0-4):"));
        campTipusPregunta = new JTextField();
        panelInputs.add(campTipusPregunta);

        panelInputs.add(new JLabel("Text pregunta:"));
        panelInputs.add(new JLabel()); // Espacio vacío

        campTextPregunta = new JTextArea(3, 20);
        JScrollPane scrollTextPregunta = new JScrollPane(campTextPregunta);

        panelInputs.add(new JLabel("Opcions (separar por comas):"));
        panelInputs.add(new JLabel()); // Espacio vacío

        campOpcionsPregunta = new JTextArea(2, 20);
        JScrollPane scrollOpcions = new JScrollPane(campOpcionsPregunta);

        buttonModificarPregunta = new JButton("Modificar Pregunta");

        JPanel panelFormulario = new JPanel(new BorderLayout(5, 5));
        panelFormulario.add(panelInputs, BorderLayout.NORTH);
        panelFormulario.add(scrollTextPregunta, BorderLayout.CENTER);
        panelFormulario.add(new JLabel("Opcions:"), BorderLayout.AFTER_LINE_ENDS);
        panelFormulario.add(scrollOpcions, BorderLayout.SOUTH);

        panelModificacion.add(panelFormulario, BorderLayout.CENTER);
        panelModificacion.add(buttonModificarPregunta, BorderLayout.SOUTH);

        panelCentral.add(panelPreguntes);
        panelCentral.add(panelModificacion);

        // Panel inferior - Botones de acción
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonEliminar = new JButton("Eliminar Enquesta");
        buttonEliminar.setBackground(new Color(255, 100, 100));
        buttonOK = new JButton("Tancar");
        buttonCancel = new JButton("Cancel·lar");

        panelInferior.add(buttonEliminar);
        panelInferior.add(buttonOK);
        panelInferior.add(buttonCancel);

        // Añadir todos los paneles al contentPane
        contentPane.add(panelSuperior, BorderLayout.NORTH);
        contentPane.add(panelCentral, BorderLayout.CENTER);
        contentPane.add(panelInferior, BorderLayout.SOUTH);

        // Añadir bordes para mejor espaciado
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
    }

    private void cargarEnquestes() {
        comboEnquestes.removeAllItems();
        try {
            List<String> enquestasInfo = ctrl.obtenirLlistaEnquestes();
            for (String info : enquestasInfo) {
                comboEnquestes.addItem(info);
            }

            if (comboEnquestes.getItemCount() == 0) {
                comboEnquestes.addItem("No hi ha enquestes disponibles");
            }
        } catch (Exception e) {
            comboEnquestes.addItem("Error carregant enquestes");
            JOptionPane.showMessageDialog(this, "Error carregant enquestes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEnquestaSeleccionada() {
        String seleccionado = (String) comboEnquestes.getSelectedItem();
        if (seleccionado == null || seleccionado.startsWith("No hi ha") || seleccionado.startsWith("Error")) {
            areaPreguntes.setText("");
            return;
        }

        try {
            int idEnquesta = extraerIdEnquesta(seleccionado);
            List<String> preguntes = ctrl.obtenirPreguntesEnquesta(idEnquesta);

            StringBuilder sb = new StringBuilder();
            sb.append("Enquesta ID: ").append(idEnquesta).append("\n");
            sb.append("====================================\n\n");

            for (int i = 0; i < preguntes.size(); i++) {
                sb.append("[Pregunta ").append(i).append("]\n");
                sb.append(preguntes.get(i)).append("\n");
                sb.append("------------------------------------\n");
            }

            areaPreguntes.setText(sb.toString());

        } catch (Exception e) {
            areaPreguntes.setText("Error carregant preguntes: " + e.getMessage());
        }
    }

    private void onModificarPregunta() {
        String seleccionado = (String) comboEnquestes.getSelectedItem();
        if (seleccionado == null || seleccionado.startsWith("No hi ha") || seleccionado.startsWith("Error")) {
            JOptionPane.showMessageDialog(this, "Selecciona una enquesta vàlida",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idEnquesta = extraerIdEnquesta(seleccionado);
            int indexPregunta;

            try {
                indexPregunta = Integer.parseInt(campIndexPregunta.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "L'índex de pregunta ha de ser un número",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<String> novaPregunta = new ArrayList<>();

            String tipus = campTipusPregunta.getText().trim();
            if (tipus.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Introdueix el tipus de pregunta (0-4)",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            novaPregunta.add(tipus);

            String textPregunta = campTextPregunta.getText().trim();
            if (textPregunta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Introdueix el text de la pregunta",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            novaPregunta.add(textPregunta);

            int tipusNum = Integer.parseInt(tipus);
            if (tipusNum == 1 || tipusNum == 2 || tipusNum == 3) {
                String opcionsText = campOpcionsPregunta.getText().trim();
                if (opcionsText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Aquest tipus de pregunta requereix opcions",
                            "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String[] opcions = opcionsText.split(",");
                novaPregunta.add(String.valueOf(opcions.length));
                for (String opcio : opcions) {
                    novaPregunta.add(opcio.trim());
                }
            }

            ctrl.modificarPreguntaEnquesta(idEnquesta, indexPregunta, novaPregunta);
            //JOptionPane.showMessageDialog(this, "Pregunta modificada correctament", "Èxit", JOptionPane.INFORMATION_MESSAGE);
            onEnquestaSeleccionada();
            campIndexPregunta.setText("");
            campTipusPregunta.setText("");
            campTextPregunta.setText("");
            campOpcionsPregunta.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error modificant pregunta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEliminar() {
        String seleccionado = (String) comboEnquestes.getSelectedItem();
        if (seleccionado == null || seleccionado.startsWith("No hi ha") || seleccionado.startsWith("Error")) {
            JOptionPane.showMessageDialog(this, "Selecciona una enquesta vàlida",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacio = JOptionPane.showConfirmDialog(this,
                "Estàs segur que vols eliminar aquesta enquesta?\nAquesta acció no es pot desfer.",
                "Confirmar eliminació", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacio == JOptionPane.YES_OPTION) {
            try {
                int idEnquesta = extraerIdEnquesta(seleccionado);

                ctrl.eliminarEnquesta(idEnquesta);

                JOptionPane.showMessageDialog(this,
                        "Enquesta eliminada correctament", "Èxit", JOptionPane.INFORMATION_MESSAGE);

                cargarEnquestes();
                areaPreguntes.setText("");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error eliminant enquesta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private int extraerIdEnquesta(String texto) {
        try {
            String[] partes = texto.split(" - ")[0].split(": ");
            return Integer.parseInt(partes[1]);
        } catch (Exception e) {
            throw new RuntimeException("Format d'enquesta invàlid: " + texto);
        }
    }
}
