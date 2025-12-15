package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Vista principal per als usuaris amb rol de Moderador.
 * <p>
 * Aquesta classe representa el menú principal per a un usuari amb privilegis de moderació.
 * Proporciona accés a funcionalitats específiques com la modificació d'enquestes existents,
 * la consulta de perfils, respostes i recomanacions, però sense tots els privilegis d'un administrador.
 * </p>
 */
public class VistaModerador extends JFrame {
    private CtrlPresentacio ctrl;
    private int idUsuari;
    private String nomUsuari;
    private String nomRol;

    private JPanel contentPanel = new JPanel();
    private JButton consultarPerfilButton = new JButton("Consultar Perfil");
    private JButton modificarEnquestaButton = new JButton("Modificar Enquesta");
    private JButton consultarRespostesButton = new JButton("Consultar Respostes");
    private JButton consultarRecomanacionsButton =  new JButton("Consultar Recomanacions");
    private JButton vetarDesvetarButton = new JButton("Vetar / Desvetar");
    private JButton sortirButton = new JButton("Sortir");

    /**
     * Constructor de la vista de Moderador.
     * Inicialitza la finestra amb les dades de l'usuari i configura els components gràfics.
     *
     * @param ctrl Referència al controlador de presentació.
     * @param idUsuari Identificador del moderador actual.
     * @param nomUsuari Nom de l'usuari.
     * @param nomRol Rol de l'usuari ("MODERADOR").
     */
    public VistaModerador(CtrlPresentacio ctrl, int idUsuari, String nomUsuari, String nomRol) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;
        this.nomUsuari = nomUsuari;
        this.nomRol = nomRol;

        initComponents();
        setupListeners();     // Configura els listeners dels botons
    }

    /**
     * Inicialitza i distribueix els components visuals de la interfície.
     * Crea un menú vertical amb botons per a les funcionalitats de moderació
     * i un botó de sortida a la part inferior.
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
        buttonPanel.add(modificarEnquestaButton);
        buttonPanel.add(consultarRespostesButton);
        buttonPanel.add(consultarRecomanacionsButton);
        buttonPanel.add(vetarDesvetarButton);

        JPanel panelSortir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSortir.add(sortirButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panelSortir.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel.add(buttonPanel, BorderLayout.CENTER);
        contentPanel.add(panelSortir, BorderLayout.SOUTH);

        add(contentPanel);

        sortirButton.addActionListener(e -> {
            dispose();
        });

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
     * Assigna a cada botó la funció corresponent del controlador de presentació,
     * permetent la navegació cap a les vistes de modificació, consulta, etc.
     */
    private void setupListeners() {
        consultarPerfilButton.addActionListener(e -> {
            ctrl.mostrarConsultarPerfil();
        });
        consultarRecomanacionsButton.addActionListener(e -> {
            ctrl.mostrarConsultarRecomanacions();
        });

        modificarEnquestaButton.addActionListener(e -> {
            ctrl.mostrarVistaModificarEnquesta(idUsuari);
        });

        consultarRespostesButton.addActionListener(e -> {
            ctrl.mostrarConsultarRespostes(idUsuari);
        });
        vetarDesvetarButton.addActionListener(e -> {
            ctrl.mostrarVetarDesvetar(idUsuari);
        });

    }
}