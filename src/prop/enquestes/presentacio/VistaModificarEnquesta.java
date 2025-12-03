package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class VistaModificarEnquesta extends JDialog {
    private CtrlPresentacio ctrl;
    private int idUsuariActual;
    private int idEnquestaActual = -1;

    private JButton buttonOK = new JButton("OK");
    private JButton buttonCancel = new JButton("Cancelar");

    private JPanel contentPane;
    private JComboBox<String> comboEnquestes;
    private JTextArea areaInfo;
    private JButton buttonMostrarInfo;
    private JLabel labelIdActual;

    private JTextField campIndexPregunta;
    private JComboBox<String> comboTipusPregunta;
    private JTextArea campTextPregunta;
    private JTextField campNumOpcions;
    private JTextArea campOpcionsPregunta;
    private JButton buttonModificarPregunta;

    private JButton buttonEliminarEnquesta;
    private JTextField campIdEnquestat;
    private JButton buttonEliminarResposta;

    public VistaModificarEnquesta(CtrlPresentacio ctrl, int idUsuariActual) {
        super((Frame) null, "Gestió d'Enquestes", true);
        this.ctrl = ctrl;
        this.idUsuariActual = idUsuariActual;

        setSize(700, 650); // Más compacto
        setLocationRelativeTo(null);

        inicializarComponentes();
        cargarEnquestes();

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonMostrarInfo);

        configurarListeners();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        contentPane.registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void inicializarComponentes() {
        contentPane = new JPanel(new BorderLayout(5, 5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel principal con scroll
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setBorder(null);

        // ========== SECCIÓN 1: Selección de Enquesta ==========
        JPanel panelSeleccion = new JPanel(new BorderLayout(5, 5));
        panelSeleccion.setBorder(BorderFactory.createTitledBorder("Selecció d'Enquesta"));

        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelSuperior.add(new JLabel("Enquesta:"));

        comboEnquestes = new JComboBox<>();
        comboEnquestes.setPreferredSize(new Dimension(250, 25));
        panelSuperior.add(comboEnquestes);

        labelIdActual = new JLabel("ID: --");
        labelIdActual.setFont(labelIdActual.getFont().deriveFont(Font.BOLD));
        labelIdActual.setForeground(Color.BLUE);
        panelSuperior.add(labelIdActual);

        panelSuperior.add(Box.createHorizontalStrut(10));

        buttonMostrarInfo = new JButton("Mostrar");
        buttonMostrarInfo.setPreferredSize(new Dimension(100, 25));
        panelSuperior.add(buttonMostrarInfo);

        panelSeleccion.add(panelSuperior, BorderLayout.NORTH);

        // Área de información
        areaInfo = new JTextArea(6, 50);
        areaInfo.setEditable(false);
        areaInfo.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollInfo = new JScrollPane(areaInfo);
        scrollInfo.setPreferredSize(new Dimension(650, 120));
        panelSeleccion.add(scrollInfo, BorderLayout.CENTER);

        panelPrincipal.add(panelSeleccion);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));

        // ========== SECCIÓN 2: Modificar Pregunta ==========
        JPanel panelModificar = new JPanel();
        panelModificar.setLayout(new BoxLayout(panelModificar, BoxLayout.Y_AXIS));
        panelModificar.setBorder(BorderFactory.createTitledBorder("Modificar Pregunta"));

        // Inputs básicos en grid 2x2
        JPanel panelInputs = new JPanel(new GridLayout(2, 2, 5, 5));
        panelInputs.add(new JLabel("Índex pregunta (0-X):"));
        campIndexPregunta = new JTextField();
        panelInputs.add(campIndexPregunta);

        panelInputs.add(new JLabel("Nou tipus:"));
        comboTipusPregunta = new JComboBox<>(new String[]{
                "0: NUMÈRICA", "1: ÚNICA", "2: ORDENADA", "3: MÚLTIPLE", "4: LLIURE"
        });
        panelInputs.add(comboTipusPregunta);

        panelModificar.add(panelInputs);
        panelModificar.add(Box.createRigidArea(new Dimension(0, 5)));

        // Texto pregunta
        JPanel panelTexto = new JPanel(new BorderLayout(2, 2));
        panelTexto.add(new JLabel("Text:"), BorderLayout.NORTH);
        campTextPregunta = new JTextArea(2, 40);
        campTextPregunta.setLineWrap(true);
        campTextPregunta.setWrapStyleWord(true);
        JScrollPane scrollTexto = new JScrollPane(campTextPregunta);
        scrollTexto.setPreferredSize(new Dimension(600, 60));
        panelTexto.add(scrollTexto, BorderLayout.CENTER);
        panelModificar.add(panelTexto);
        panelModificar.add(Box.createRigidArea(new Dimension(0, 5)));

        // Opciones
        JPanel panelOpciones = new JPanel(new BorderLayout(2, 2));
        panelOpciones.add(new JLabel("Opcions (una per línea):"), BorderLayout.NORTH);
        campOpcionsPregunta = new JTextArea(2, 40);
        campOpcionsPregunta.setEnabled(false);
        campOpcionsPregunta.setLineWrap(true);
        JScrollPane scrollOpciones = new JScrollPane(campOpcionsPregunta);
        scrollOpciones.setPreferredSize(new Dimension(600, 60));
        panelOpciones.add(scrollOpciones, BorderLayout.CENTER);
        panelModificar.add(panelOpciones);
        panelModificar.add(Box.createRigidArea(new Dimension(0, 5)));

        // Número de opciones
        JPanel panelNumOpciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNumOpciones.add(new JLabel("Nº opcions:"));
        campNumOpcions = new JTextField(5);
        campNumOpcions.setEnabled(false);
        panelNumOpciones.add(campNumOpcions);
        panelModificar.add(panelNumOpciones);
        panelModificar.add(Box.createRigidArea(new Dimension(0, 10)));

        // Botón
        buttonModificarPregunta = new JButton("Modificar Pregunta");
        buttonModificarPregunta.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelModificar.add(buttonModificarPregunta);

        panelPrincipal.add(panelModificar);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));

        // ========== SECCIÓN 3: Eliminar Enquesta ==========
        JPanel panelEliminar = new JPanel(new BorderLayout(5, 5));
        panelEliminar.setBorder(BorderFactory.createTitledBorder("Eliminar Enquesta"));

        JLabel labelWarning = new JLabel("<html><font color='red'><b>ATENCIÓ:</b> Acció irreversible</font></html>");
        panelEliminar.add(labelWarning, BorderLayout.NORTH);

        buttonEliminarEnquesta = new JButton("ELIMINAR ENQUESTA");
        buttonEliminarEnquesta.setBackground(new Color(220, 50, 50));
        buttonEliminarEnquesta.setForeground(Color.WHITE);
        buttonEliminarEnquesta.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEliminar.add(buttonEliminarEnquesta, BorderLayout.CENTER);

        panelPrincipal.add(panelEliminar);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));

        // ========== SECCIÓN 4: Eliminar Resposta ==========
        JPanel panelEliminarResposta = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelEliminarResposta.setBorder(BorderFactory.createTitledBorder("Eliminar Resposta"));

        panelEliminarResposta.add(new JLabel("ID Enquestat:"));
        campIdEnquestat = new JTextField(8);
        panelEliminarResposta.add(campIdEnquestat);

        buttonEliminarResposta = new JButton("Eliminar");
        buttonEliminarResposta.setBackground(new Color(255, 150, 150));
        panelEliminarResposta.add(buttonEliminarResposta);

        JLabel labelInfo = new JLabel("<html><i>Elimina totes les respostes d'aquest enquestat</i></html>");
        labelInfo.setFont(labelInfo.getFont().deriveFont(Font.ITALIC, 11f));
        panelEliminarResposta.add(labelInfo);

        panelPrincipal.add(panelEliminarResposta);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));

        // ========== BOTÓN CERRAR ==========
        JPanel panelCerrar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton buttonCerrar = new JButton("Tancar");
        buttonCerrar.addActionListener(e -> dispose());
        panelCerrar.add(buttonCerrar);

        panelPrincipal.add(panelCerrar);

        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarListeners() {
        buttonMostrarInfo.addActionListener(e -> mostrarInfoEnquesta());

        comboEnquestes.addActionListener(e -> {
            String seleccionado = (String) comboEnquestes.getSelectedItem();
            if (seleccionado != null && !seleccionado.equals("-- Selecciona --")) {
                try {
                    idEnquestaActual = extraerIdEnquesta(seleccionado);
                    labelIdActual.setText("ID: " + idEnquestaActual);
                    mostrarInfoEnquesta();
                } catch (Exception ex) {
                    idEnquestaActual = -1;
                    labelIdActual.setText("ID: --");
                    areaInfo.setText("");
                }
            } else {
                idEnquestaActual = -1;
                labelIdActual.setText("ID: --");
                areaInfo.setText("");
            }
        });

        comboTipusPregunta.addActionListener(e -> {
            String seleccionado = (String) comboTipusPregunta.getSelectedItem();
            if (seleccionado != null) {
                String tipoNumero = seleccionado.substring(0, 1);
                int tipus = Integer.parseInt(tipoNumero);
                boolean requiereOpciones = (tipus == 1 || tipus == 2 || tipus == 3);
                campNumOpcions.setEnabled(requiereOpciones);
                campOpcionsPregunta.setEnabled(requiereOpciones);
                if (!requiereOpciones) {
                    campNumOpcions.setText("");
                    campOpcionsPregunta.setText("");
                }
            }
        });

        // Botones de acción
        buttonModificarPregunta.addActionListener(e -> modificarPregunta());
        buttonEliminarEnquesta.addActionListener(e -> eliminarEnquesta());
        buttonEliminarResposta.addActionListener(e -> eliminarResposta());
    }

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
        } catch (Exception e) {
            // Puedes ignorarlo
        }
    }

    private void mostrarInfoEnquesta() {
        if (idEnquestaActual == -1) {
            areaInfo.setText("");
            JOptionPane.showMessageDialog(this,
                    "Selecciona una enquesta",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<String> infoCompleta = ctrl.obtenirPreguntesEnquesta(idEnquestaActual);

            StringBuilder sb = new StringBuilder();
            for (String linea : infoCompleta) {
                sb.append(linea).append("\n");
            }

            areaInfo.setText(sb.toString());

        } catch (Exception e) {
            areaInfo.setText("Error: " + e.getMessage());
        }
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

    private void modificarPregunta() {
        if (idEnquestaActual == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una enquesta",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Validar índice
            String indexText = campIndexPregunta.getText().trim();
            if (indexText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Introdueix l'índex de la pregunta",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int idxPregunta = Integer.parseInt(indexText);

            // Construir nueva pregunta
            List<String> novaPregunta = new ArrayList<>();

            // Tipo
            String tipoSeleccionado = (String) comboTipusPregunta.getSelectedItem();
            String tipus = tipoSeleccionado.substring(0, 1);
            novaPregunta.add(tipus);

            // Texto
            String textPregunta = campTextPregunta.getText().trim();
            if (textPregunta.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Introdueix el text de la pregunta",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            novaPregunta.add(textPregunta);

            // Opciones si aplica
            int tipusNum = Integer.parseInt(tipus);
            if (tipusNum == 1 || tipusNum == 2 || tipusNum == 3) {
                String numOpcionsText = campNumOpcions.getText().trim();
                if (numOpcionsText.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Introdueix el nombre d'opcions",
                            "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int numOpcions = Integer.parseInt(numOpcionsText);
                if (numOpcions <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "El nombre d'opcions ha de ser positiu",
                            "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                novaPregunta.add(String.valueOf(numOpcions));

                String opcionsText = campOpcionsPregunta.getText().trim();
                if (opcionsText.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Introdueix les opcions",
                            "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String[] opcions = opcionsText.split("\n");
                if (opcions.length < numOpcions) {
                    JOptionPane.showMessageDialog(this,
                            String.format("Falten opcions. Has introduït %d de %d",
                                    opcions.length, numOpcions),
                            "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                for (int i = 0; i < numOpcions; i++) {
                    novaPregunta.add(opcions[i].trim());
                }
            }

            // Llamar al controlador
            ctrl.modificarPreguntaEnquesta(idEnquestaActual, idxPregunta, novaPregunta);

            JOptionPane.showMessageDialog(this,
                    "Pregunta modificada correctament",
                    "Èxit", JOptionPane.INFORMATION_MESSAGE);

            // Refrescar
            mostrarInfoEnquesta();
            campIndexPregunta.setText("");
            campTextPregunta.setText("");
            campNumOpcions.setText("");
            campOpcionsPregunta.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Valors numèrics incorrectes",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEnquesta() {
        if (idEnquestaActual == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una enquesta",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "<html>Eliminar enquesta <b>" + idEnquestaActual + "</b>?<br>" +
                        "<font color='red'>Acció irreversible</font></html>",
                "Confirmar eliminació",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                ctrl.eliminarEnquesta(idUsuariActual, idEnquestaActual);

                JOptionPane.showMessageDialog(this,
                        "Enquesta " + idEnquestaActual + " eliminada",
                        "Èxit", JOptionPane.INFORMATION_MESSAGE);

                // Recargar
                cargarEnquestes();
                idEnquestaActual = -1;
                labelIdActual.setText("ID: --");
                areaInfo.setText("");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarResposta() {
        if (idEnquestaActual == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una enquesta",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idEnquestatText = campIdEnquestat.getText().trim();
        if (idEnquestatText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Introdueix l'ID de l'enquestat",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idEnquestat = Integer.parseInt(idEnquestatText);

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "Eliminar respostes de l'enquestat " + idEnquestat +
                            " a l'enquesta " + idEnquestaActual + "?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                ctrl.esborrarRespostaEnquesta(idEnquestaActual, idEnquestat);

                JOptionPane.showMessageDialog(this,
                        "Respostes eliminades",
                        "Èxit", JOptionPane.INFORMATION_MESSAGE);

                campIdEnquestat.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "ID invàlid",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}