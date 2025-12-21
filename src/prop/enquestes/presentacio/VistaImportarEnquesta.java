package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import prop.enquestes.excepcions.FileNotFound;
import prop.enquestes.excepcions.InvalidFormatEnquesta;
import prop.enquestes.excepcions.UsuariNoValid;

/**
 * Diàleg per a la importació d'enquestes des de fitxers externs.
 * <p>
 * Aquesta vista permet a l'usuari seleccionar un fitxer local mitjançant un explorador de fitxers.
 * El sistema llegeix el fitxer i intenta crear una nova enquesta associada a l'usuari actual,
 * validant el format de les dades.
 * </p>
 */
public class VistaImportarEnquesta extends JDialog {
    private CtrlPresentacio ctrl;
    private int idUsuari;

    private static final String BASE_DIR = "."; // carpeta base: directori actual

    private JPanel contentPane = new JPanel();
    private JTextField fieldPath = new JTextField();
    private JButton btnSeleccionar = new JButton("Seleccionar fitxer");
    private JButton buttonOK = new JButton("Importar");
    private JButton buttonCancel = new JButton("Cancel·lar");

    /**
     * Constructor de la vista d'importació.
     * Inicialitza la finestra modal i configura els components gràfics.
     *
     * @param ctrl Referència al controlador de presentació.
     * @param idUsuari Identificador de l'usuari que realitza la importació.
     */
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

    /**
     * Inicialitza i distribueix els components visuals.
     * Col·loca el camp de text i el botó de selecció al centre, i els botons d'acció a sota.
     */
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

    /**
     * Configura els escoltadors (listeners) per als botons.
     * Assigna la lògica de selecció de fitxer, confirmació d'importació i cancel·lació.
     */
    private void initActions() {
        btnSeleccionar.addActionListener(e -> seleccionarFitxer());
        buttonOK.addActionListener(e -> onImportar());
        buttonCancel.addActionListener(e -> {
            dispose();
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    /**
     * Obre un explorador de fitxers (JFileChooser) perquè l'usuari seleccioni l'arxiu a importar.
     * Processa la ruta seleccionada per obtenir el nom del fitxer (sovint necessari sense extensió
     * segons la lògica del controlador) i l'actualitza al camp de text.
     */
    private void seleccionarFitxer() {
        JFileChooser fc = new JFileChooser(BASE_DIR);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(true);


        int res = fc.showOpenDialog(this);

        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String pathSeleccionat = fc.getSelectedFile().getPath();

            String relativePath;
            if (pathSeleccionat.startsWith(BASE_DIR)) {
                relativePath = pathSeleccionat.substring(BASE_DIR.length());
                if (relativePath.startsWith("/") || relativePath.startsWith("\\")) {
                    relativePath = relativePath.substring(1); // eliminar separador inicial
                }
            } else {
                String nombreArchivo = file.getName();
                if (nombreArchivo.toLowerCase().endsWith(".txt")) {
                    nombreArchivo = nombreArchivo.substring(0, nombreArchivo.length() - 4);
                }
                relativePath = nombreArchivo;
            }

            fieldPath.setText(relativePath);
        }
    }

    /**
     * Executa la importació de l'enquesta utilitzant el nom del fitxer seleccionat.
     * Crida al controlador i gestiona les possibles excepcions (fitxer no trobat, format invàlid,
     * o usuari no vàlid), mostrant missatges d'error o d'èxit segons correspongui.
     */
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
            int numPreguntes = ctrl.importarEnquesta(relativePath, idUsuari);

            JOptionPane.showMessageDialog(this,
                    "Enquesta importada correctament!\nPreguntes importades: " + numPreguntes,
                    "Èxit",
                    JOptionPane.INFORMATION_MESSAGE);

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
        } catch (UsuariNoValid e) {
            JOptionPane.showMessageDialog(this,
                    "Error de l'usuari:\n" + e.getMessage(),
                    "Format invàlid",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}