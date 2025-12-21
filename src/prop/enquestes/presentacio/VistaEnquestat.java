package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Vista principal per als usuaris amb rol d'Enquestat.
 * <p>
 * Aquesta classe representa el menú principal per a un usuari estàndard (enquestat).
 * Proporciona accés a les funcionalitats permeses per a aquest rol, com respondre enquestes,
 * crear-ne de noves (si el sistema ho permet), consultar el perfil i gestionar les seves dades.
 * </p>
 */
public class VistaEnquestat extends JFrame {
    private CtrlPresentacio ctrl;
    private int idUsuari;
    private String nomUsuari;
    private String nomRol;


    private JPanel contentPanel = new JPanel();
    private JButton crearEnquestaButton = new JButton("Crear Enquesta");
    private JButton consultarEnquestaButton = new JButton("Consultar Enquesta");
    private JButton consultarPerfilButton = new JButton("Consultar Perfil");
    private JButton respondreEnquestaButton = new JButton("Respondre Enquesta");
    private JButton exportarRespostesButton = new JButton("Exportar Respostes");
    private JButton exportarEnquestatButton = new JButton("Exportar Enquesta");
    private JButton consultarRespostesButton = new JButton("Consultar Respostes");
    private JButton sortirButton = new JButton("Sortir");

    /**
     * Constructor de la vista d'Enquestat.
     * Inicialitza la finestra, guarda la informació de la sessió i configura els components gràfics.
     *
     * @param ctrl Referència al controlador de presentació.
     * @param idUsuari Identificador de l'usuari actual.
     * @param nomUsuari Nom de l'usuari.
     * @param nomRol Rol de l'usuari ("ENQUESTAT").
     */
    public VistaEnquestat (CtrlPresentacio ctrl, int idUsuari, String nomUsuari, String nomRol) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;
        this.nomUsuari = nomUsuari;
        this.nomRol = nomRol;

        initComponents();
        setupListeners();     // Configura els listeners dels botons
    }

    /**
     * Inicialitza i distribueix els components visuals de la interfície.
     * Crea un menú vertical amb botons per a cada funcionalitat disponible
     * i un botó de sortida a la part inferior.
     */
    private void initComponents() {
        setTitle("Menú Usuari - " + nomRol);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPanel.setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,1,5,5));

        buttonPanel.add(crearEnquestaButton);
        buttonPanel.add(consultarEnquestaButton);
        buttonPanel.add(consultarPerfilButton);
        buttonPanel.add(respondreEnquestaButton);
        buttonPanel.add(exportarRespostesButton);
        buttonPanel.add(exportarEnquestatButton);
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
     * Modifica la visibilitat de la finestra principal.
     *
     * @param b True per mostrar la finestra, False per amagar-la.
     */
    public void hacerVisible(boolean b) {
        setVisible(b);
    }

    /**
     * Configura els escoltadors (listeners) per als botons del menú.
     * Assigna a cada botó la funció corresponent del controlador de presentació.
     */
    private void setupListeners() {
        consultarPerfilButton.addActionListener(e -> {
            ctrl.mostrarConsultarPerfil(idUsuari);
        });
        crearEnquestaButton.addActionListener(e -> {
            dispose();
            ctrl.mostrarCrearEnquesta(idUsuari);
        });

        consultarEnquestaButton.addActionListener(e -> {
            ctrl.mostrarConsultarEnquesta();
        });

        respondreEnquestaButton.addActionListener(e -> {
            ctrl.mostrarRespondreEnquesta(idUsuari);
        });

        exportarRespostesButton.addActionListener(e -> {
            ctrl.mostrarExportarRespostes(idUsuari, false);
        });

        exportarEnquestatButton.addActionListener(e -> {
            ctrl.mostrarExportarEnquesta(idUsuari);
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