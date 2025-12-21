package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Vista principal per als usuaris amb rol d'Enquestador.
 * <p>
 * Aquesta classe representa el menú principal per a un enquestador.
 * Proporciona accés a funcionalitats com consultar el perfil, gestionar respostes
 * (exportar/importar/consultar) i veure recomanacions.
 * </p>
 */
public class VistaEnquestador extends JFrame {

    private CtrlPresentacio ctrl;
    private int idUsuari;
    private String nomUsuari;
    private String nomRol;

    private JPanel contentPanel = new JPanel();
    private JButton consultarPerfilButton = new JButton("Consultar Perfil");
    private JButton consultarEnquestaButton = new JButton("Consultar Enquesta");
    private JButton exportarRespostesButton = new JButton("Exportar Respostes");
    private JButton exportarEnquestatButton = new JButton("Exportar Enquesta");
    private JButton importarRespostesButton = new JButton("Importar Respostes");
    private JButton consultarRespostesButton = new JButton("Consultar Respostes");
    private JButton sortirButton = new JButton("Sortir");

    /**
     * Constructor de la vista d'Enquestador.
     * Inicialitza la finestra amb les dades de l'usuari i configura els components.
     *
     * @param ctrl Referència al controlador de presentació.
     * @param idUsuari Identificador de l'enquestador.
     * @param nomUsuari Nom de l'usuari.
     * @param nomRol Rol de l'usuari ("ENQUESTADOR").
     */
    public VistaEnquestador(CtrlPresentacio ctrl, int idUsuari, String nomUsuari, String nomRol) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;
        this.nomUsuari = nomUsuari;
        this.nomRol = nomRol;

        initComponents();
        setupListeners();     // Configura els listeners dels botons

    }

    /**
     * Inicialitza i distribueix els components gràfics de la interfície.
     * Crea un menú de botons vertical per a les diferents funcionalitats disponibles.
     */
    private void initComponents() {
        setTitle("Menu Usuari - " + nomRol);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPanel.setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,1,5,5));

        buttonPanel.add(consultarPerfilButton);
        buttonPanel.add(consultarEnquestaButton);
        buttonPanel.add(exportarRespostesButton);
        buttonPanel.add(exportarEnquestatButton);
        buttonPanel.add(importarRespostesButton);
        buttonPanel.add(consultarRespostesButton);

        JPanel panelSortir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSortir.add(sortirButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panelSortir.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel.add(buttonPanel, BorderLayout.CENTER);
        contentPanel.add(panelSortir, BorderLayout.SOUTH);

        add(contentPanel);

    }

    /**
     * Canvia la visibilitat de la finestra.
     *
     * @param b True per mostrar la finestra, False per amagar-la.
     */
    public void hacerVisible(boolean b) {
        setVisible(b);
    }

    /**
     * Assigna els escoltadors (listeners) als botons del menú.
     * Defineix quina acció del controlador s'executa en prémer cada botó.
     */
    private void setupListeners() {
        consultarPerfilButton.addActionListener(e -> {
            ctrl.mostrarConsultarPerfil();
        });

        consultarEnquestaButton.addActionListener(e -> {
            ctrl.mostrarConsultarEnquesta();
        });

        exportarRespostesButton.addActionListener(e -> {
            ctrl.mostrarExportarRespostes(idUsuari, false);
        });

        exportarEnquestatButton.addActionListener(e -> {
            ctrl.mostrarExportarEnquesta(idUsuari);
        });

        importarRespostesButton.addActionListener(e -> {
            ctrl.mostrarImportarRespostes(idUsuari);
        });

        consultarRespostesButton.addActionListener(e -> {
            ctrl.mostrarConsultarRespostes(idUsuari);
        });
        sortirButton.addActionListener(e -> {
            dispose();
            ctrl.inicializarPresentacio();
        });

    }
}