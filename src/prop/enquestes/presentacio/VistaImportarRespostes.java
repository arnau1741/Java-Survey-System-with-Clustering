package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

import prop.enquestes.excepcions.FileNotFound;
import prop.enquestes.excepcions.InvalidFormatEnquesta;

public class VistaImportarRespostes extends JDialog {
    private CtrlPresentacio ctrl;
    private int idUsuari;
    private int idEnquestaActual = -1;

    private static final String BASE_DIR = ".";

    private JPanel contentPane = new JPanel();
    private JTextField fieldPath = new JTextField();
    private JButton btnSeleccionar = new JButton("Seleccionar fitxer");
    private JButton buttonOK = new JButton("Importar");
    private JButton buttonCancel = new JButton("Cancel·lar");
    private JComboBox<String> comboEnquestes = new JComboBox<>();

    public VistaImportarRespostes(CtrlPresentacio ctrl, int idUsuari) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;

        setTitle("Importar Respostes");
        setModal(true);
        setContentPane(contentPane);
        initLayout();
        initActions();
        carregarEnquestes();

        pack();
        setLocationRelativeTo(null);
    }

    private void initLayout() {
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior: selección de enquesta
        JPanel panelEnquesta = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelEnquesta.add(new JLabel("Enquesta:"));
        comboEnquestes.setPreferredSize(new Dimension(250, 28));
        panelEnquesta.add(comboEnquestes);

        contentPane.add(panelEnquesta, BorderLayout.NORTH);

        // Panel central: path del fitxer
        JPanel center = new JPanel(new BorderLayout(6, 6));
        fieldPath.setEditable(false);
        center.add(fieldPath, BorderLayout.CENTER);
        center.add(btnSeleccionar, BorderLayout.EAST);
        contentPane.add(center, BorderLayout.CENTER);
        fieldPath.setPreferredSize(new Dimension(250, 28));

        // Panel inferior: botons
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(buttonOK);
        bottom.add(buttonCancel);
        contentPane.add(bottom, BorderLayout.SOUTH);
    }

    private void initActions() {
        btnSeleccionar.addActionListener(e -> seleccionarFitxer());
        buttonOK.addActionListener(e -> onImportar());
        buttonCancel.addActionListener(e -> {
            ctrl.inicializarPresentacio();
            dispose();
        });

        comboEnquestes.addActionListener(e -> {
            String seleccion = (String) comboEnquestes.getSelectedItem();
            if (seleccion != null && !seleccion.equals("-- Selecciona --")) {
                idEnquestaActual = extraerIdEnquesta(seleccion);
            } else {
                idEnquestaActual = -1;
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                ctrl.inicializarPresentacio();
                dispose();
            }
        });
    }

    private void seleccionarFitxer() {
        JFileChooser fc = new JFileChooser(BASE_DIR);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int res = fc.showOpenDialog(this);

        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            fieldPath.setText(file.getAbsolutePath());
        }
    }

    private void carregarEnquestes() {
        comboEnquestes.removeAllItems();
        comboEnquestes.addItem("-- Selecciona --");
        try {
            List<String> enquestes = ctrl.obtenirLlistaEnquestes();
            for (String info : enquestes) {
                String[] linies = info.split("\n");
                for (String linia : linies) {
                    if (linia.startsWith("ID:")) {
                        comboEnquestes.addItem(linia);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error carregant enquestes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onImportar() {
        if (idEnquestaActual == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una enquesta",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String path = fieldPath.getText().trim();
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Has de seleccionar un fitxer.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int numRespostes = ctrl.importarRespostes(path, idUsuari, idEnquestaActual);

            JOptionPane.showMessageDialog(this,
                    "Respostes importades correctament!\nRespostes importades: " + numRespostes,
                    "Èxit",
                    JOptionPane.INFORMATION_MESSAGE);

            ctrl.inicializarPresentacio();
            dispose();

        } catch (FileNotFound e) {
            JOptionPane.showMessageDialog(this,
                    "Fitxer no trobat:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

        } catch (InvalidFormatEnquesta e) {
            JOptionPane.showMessageDialog(this,
                    "El fitxer té un format de resposta incorrecte:\n" + e.getMessage(),
                    "Format invàlid",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private int extraerIdEnquesta(String texto) {
        // "ID: X - Títol"
        try {
            int idxDosPunts = texto.indexOf(':');
            int idxGuio = texto.indexOf('-');
            String idStr = texto.substring(idxDosPunts + 1, idxGuio).trim();
            return Integer.parseInt(idStr);
        } catch (Exception e) {
            return -1;
        }
    }
}
