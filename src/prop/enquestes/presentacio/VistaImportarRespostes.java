package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import prop.enquestes.excepcions.EnquestaNoExisteixException;
import prop.enquestes.excepcions.FileNotFound;
import prop.enquestes.excepcions.InvalidFormatEnquesta;
import prop.enquestes.excepcions.UsuariNoValid;

/**
 * Diàleg per a la importació de respostes a una enquesta des d'un fitxer extern.
 * <p>
 * Aquesta vista permet a l'usuari (generalment un Administrador o Moderador) seleccionar
 * una enquesta existent i carregar un fitxer de text que contingui respostes per a ser
 * processades i emmagatzemades pel sistema.
 * </p>
 */
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

    /**
     * Constructor de la vista d'importació de respostes.
     * Inicialitza la finestra, configura la interfície i carrega la llista d'enquestes
     * disponibles segons el rol de l'usuari.
     *
     * @param ctrl Referència al controlador de presentació.
     * @param idUsuari Identificador de l'usuari que realitza la importació.
     */
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

    /**
     * Inicialitza i distribueix els components visuals de la finestra.
     * Organitza els panells per a la selecció d'enquesta, selecció de fitxer i botons d'acció.
     */
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

        // Panel inferior: botons
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(buttonOK);
        bottom.add(buttonCancel);
        contentPane.add(bottom, BorderLayout.SOUTH);

        fieldPath.setPreferredSize(new Dimension(250, 28));
    }

    /**
     * Configura els escoltadors (listeners) per als components interactius.
     * Defineix el comportament dels botons de selecció de fitxer, importació i cancel·lació,
     * així com del desplegable d'enquestes per actualitzar l'ID seleccionat.
     */
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

    /**
     * Obre un explorador de fitxers per permetre a l'usuari seleccionar l'arxiu de respostes.
     * Processa la ruta seleccionada per adaptar-la al format esperat pel sistema
     * (nom de fitxer net o ruta relativa).
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
                    relativePath = relativePath.substring(1);
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
     * Carrega les enquestes disponibles al desplegable segons el rol de l'usuari.
     * Si l'usuari és ADMIN, mostra només les enquestes que administra.
     * Si és MODERADOR, mostra totes les enquestes del sistema.
     */
    private void carregarEnquestes() {
        comboEnquestes.removeAllItems();
        comboEnquestes.addItem("-- Selecciona --");
        try {
            List<String> enquestes = new ArrayList<>();
            String rol = ctrl.obtenirRolUsuari(idUsuari);
            if (rol.equals("ADMIN")) {
                // si l'usuari es admin, mostra les enquestes que administra
                enquestes = ctrl.obtenirEnquestesAdministrades(idUsuari);
            }
            else if (rol.equals("MODERADOR")) {
                // si l'usuari es moderador, mostra totes les enquestes
                enquestes = ctrl.obtenirLlistaEnquestes();
            }
            else {
                // per l'enquestador
                enquestes = ctrl.obtenirLlistaEnquestes();
            }

            for (String info : enquestes) {
                String[] linies = info.split("\n");
                for (String linia : linies) {
                    if (linia.startsWith("ID:")) {
                        comboEnquestes.addItem(linia);
                        break;
                    }
                }
            }
        } catch (EnquestaNoExisteixException e) {
            JOptionPane.showMessageDialog(this,
                    "Error carregant enquestes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Executa el procés d'importació de les respostes.
     * Valida que s'hagi seleccionat una enquesta i un fitxer, i crida al controlador per processar la importació.
     * Gestiona i mostra els errors possibles (fitxer no trobat, format incorrecte, errors d'usuari, etc.).
     */
    private void onImportar() {
        if (idEnquestaActual == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una enquesta",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String relativePath = fieldPath.getText().trim();
        if (relativePath.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Has de seleccionar un fitxer.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int numRespostes = ctrl.importarRespostes(relativePath, idUsuari, idEnquestaActual);

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

        } catch (EnquestaNoExisteixException e) {
            JOptionPane.showMessageDialog(this,
                    "Error de l'enquesta:\n" + e.getMessage(),
                    "Format invàlid",
                    JOptionPane.ERROR_MESSAGE);
        } catch (UsuariNoValid e) {
            JOptionPane.showMessageDialog(this,
                    "Error de l'usuari:\n" + e.getMessage(),
                    "Format invàlid",
                    JOptionPane.ERROR_MESSAGE);
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Error format de les respostes incorrecta:\n",
                    "Format invàlid",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Extreu l'ID numèric de l'enquesta a partir de la cadena de text seleccionada al desplegable.
     * S'espera el format "ID: X - Títol".
     *
     * @param texto Cadena de text del ComboBox.
     * @return L'ID de l'enquesta, o -1 si no es pot extreure.
     */
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