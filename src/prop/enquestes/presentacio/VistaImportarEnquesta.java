package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import prop.enquestes.excepcions.FileNotFound;
import prop.enquestes.excepcions.InvalidFormatEnquesta;

public class VistaImportarEnquesta extends JDialog {
    private CtrlPresentacio ctrl;
    private int idUsuari;

    private static final String BASE_DIR = "."; // carpeta base: directori actual

    private JPanel contentPane = new JPanel();
    private JTextField fieldPath = new JTextField();
    private JButton btnSeleccionar = new JButton("Seleccionar fitxer");
    private JButton buttonOK = new JButton("Importar");
    private JButton buttonCancel = new JButton("Cancel·lar");

    public VistaImportarEnquesta(CtrlPresentacio ctrl, int idUsuari) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;

        setTitle("Importar Enquesta");
        setModal(true);
        setContentPane(contentPane);
        initLayout();
        initActions();

        pack();
        setLocationRelativeTo(null);
    }

    private void initLayout() {
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel center = new JPanel(new BorderLayout(6, 6));
        fieldPath.setEditable(false);
        center.add(fieldPath, BorderLayout.CENTER);
        center.add(btnSeleccionar, BorderLayout.EAST);

        contentPane.add(center, BorderLayout.NORTH);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(buttonOK);
        bottom.add(buttonCancel);
        contentPane.add(bottom, BorderLayout.SOUTH);

        fieldPath.setPreferredSize(new Dimension(250, 28));
    }

    private void initActions() {
        btnSeleccionar.addActionListener(e -> seleccionarFitxer());
        buttonOK.addActionListener(e -> onImportar());
        buttonCancel.addActionListener(e -> {
            ctrl.inicializarPresentacio();
            dispose();
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
        int res = fc.showOpenDialog(this);

        if (res == JFileChooser.APPROVE_OPTION) {
            String pathSeleccionat = fc.getSelectedFile().getPath();

            String relativePath;
            if (pathSeleccionat.startsWith(BASE_DIR)) {
                relativePath = pathSeleccionat.substring(BASE_DIR.length());
                if (relativePath.startsWith("/") || relativePath.startsWith("\\")) {
                    relativePath = relativePath.substring(1); // eliminar separador inicial
                }
            } else {
                relativePath = pathSeleccionat; // si està fora, deixem absolut
            }

            fieldPath.setText(relativePath);
        }
    }

    private void onImportar() {
        String relativePath = fieldPath.getText().trim();

        if (relativePath.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Has de seleccionar un fitxer.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int numPreguntes = ctrl.importarEnquesta(idUsuari, relativePath);

            JOptionPane.showMessageDialog(this,
                    "Enquesta importada correctament!\nPreguntes importades: " + numPreguntes,
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
                    "El fitxer té un format d'enquesta incorrecte:\n" + e.getMessage(),
                    "Format invàlid",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
