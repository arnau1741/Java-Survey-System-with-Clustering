package prop.enquestes.presentacio;

import prop.enquestes.excepcions.EnquestaNoExisteixException;
import prop.enquestes.excepcions.InvalidFormatEnquesta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class VistaRespondreEnquesta extends JDialog {
    private CtrlPresentacio ctrl;
    private int idUsuari;
    private int idEnquestaActual = -1;
    private List<String> preguntes;
    private List<String> respostes;
    private int preguntaActual = 0;

    private JPanel contentPane;
    private JComboBox<String> comboEnquestes;
    private JLabel labelIdActual;
    private JButton buttonMostrarInfo;
    private JTextArea areaInfo;
    private JButton buttonOK = new JButton("OK");
    private JButton buttonFinalitzar = new JButton("Finalitzar");

    private JTextArea areaPreguntas;
    private JTextField respuesta;
    private JButton buttonAfegirResposta;
    private JButton buttonCerrar;

    public VistaRespondreEnquesta(CtrlPresentacio ctrl, int idUsuari) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;

        inicializarComponentes();
        cargarEnquestes();
        setTitle("Respondre Enquesta");
        setModal(true);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        configurarListeners();

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setLocationRelativeTo(null);
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


        // ========== SECCIÓN 2: Responder Encuesta ==========
        JPanel panelPreguntas = new JPanel();
        panelPreguntas.setLayout(new BoxLayout(panelPreguntas, BoxLayout.Y_AXIS));
        panelPreguntas.setBorder(BorderFactory.createTitledBorder("Respon la pregunta"));

        // Texto pregunta
        JPanel panelTexto = new JPanel(new BorderLayout(2, 2));
        panelTexto.add(new JLabel("Pregunta de l'enquesta:"), BorderLayout.NORTH);
        areaPreguntas = new JTextArea(2, 40);
        areaPreguntas.setEditable(false);
        JScrollPane scrollTexto = new JScrollPane(areaPreguntas);
        scrollTexto.setPreferredSize(new Dimension(600, 60));
        panelTexto.add(scrollTexto, BorderLayout.CENTER);
        panelPreguntas.add(panelTexto);
        panelPreguntas.add(Box.createRigidArea(new Dimension(0, 5)));

        // Respuesta
        JPanel panelResposta = new JPanel(new BorderLayout(2, 2));
        panelResposta.add(new JLabel("Respon:"), BorderLayout.NORTH);
        respuesta = new JTextField();
        JScrollPane scrollOpciones = new JScrollPane(respuesta);
        scrollOpciones.setPreferredSize(new Dimension(600, 60));
        panelResposta.add(scrollOpciones, BorderLayout.CENTER);
        panelPreguntas.add(panelResposta);
        panelPreguntas.add(Box.createRigidArea(new Dimension(0, 5)));

        // Botón
        buttonAfegirResposta = new JButton("Afegir Resposta");
        buttonAfegirResposta.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPreguntas.add(buttonAfegirResposta);

        panelPrincipal.add(panelPreguntas);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));

        buttonFinalitzar = new JButton("Finalitzar");
        buttonFinalitzar.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonFinalitzar.setVisible(false); // Amagat inicialment
        panelPreguntas.add(buttonFinalitzar);

        // ========== BOTÓN CERRAR ==========
        JPanel panelCerrar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonCerrar = new JButton("Tancar");
        buttonCerrar.addActionListener(e -> dispose());
        panelCerrar.add(buttonCerrar);

        panelPrincipal.add(panelCerrar);

        contentPane.add(scrollPane, BorderLayout.CENTER);
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
        } catch (EnquestaNoExisteixException e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarListeners() {
        buttonMostrarInfo.addActionListener(e -> {
            if (idEnquestaActual == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecciona una enquesta",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            mostrarInfoEnquesta();

            try {
                preguntes = ctrl.obtenirPreguntes(idEnquestaActual);
                respostes = new java.util.ArrayList<>();
                preguntaActual = 0;

                mostrarPregunta();

            } catch (EnquestaNoExisteixException ex) {
                JOptionPane.showMessageDialog(this,
                        "No s'han pogut carregar les preguntes:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

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

        buttonAfegirResposta.addActionListener(e -> {
            String resp = respuesta.getText().trim();
            if (resp.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Escriu una resposta",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Guardar resposta
            respostes.add(resp);
            // Passar a la següent
            preguntaActual++;
            // Mostrar-la
            mostrarPregunta();
        });

        buttonFinalitzar.addActionListener(e -> {
            enviarRespostes();
        });

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

        } catch (EnquestaNoExisteixException e) {
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

    private void mostrarPregunta() {
        if (preguntes == null || preguntes.isEmpty()) {
            areaPreguntas.setText("No hi ha preguntes en aquesta enquesta.");
            return;
        }

        if (preguntaActual < preguntes.size()) {
            areaPreguntas.setText(preguntes.get(preguntaActual));
            respuesta.setText("");
        }
        else {
            // Hem acabat totes les preguntes
            areaPreguntas.setText("Has completat l’enquesta!");
            respuesta.setEnabled(false);
            buttonAfegirResposta.setVisible(false);
            buttonFinalitzar.setVisible(true);
        }
    }

    private void enviarRespostes() {
        try {
            ctrl.respondreEnquesta(idUsuari, idEnquestaActual, respostes);

            JOptionPane.showMessageDialog(this,
                    "Respostes enviades correctament!",
                    "Èxit",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        } catch (InvalidFormatEnquesta ex) {
            JOptionPane.showMessageDialog(this,
                    "Error enviant respostes: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (EnquestaNoExisteixException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
