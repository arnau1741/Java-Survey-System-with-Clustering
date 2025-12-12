package prop.enquestes.presentacio;

import prop.enquestes.domini.Usuari;
import javax.swing.*;
import java.awt.*;

/**
 * Vista comuna que serveix com a base per als menús d'usuaris registrats.
 * <p>
 * Aquesta classe proporciona una estructura unificada per als menús principals dels diferents rols.
 * Configura dinàmicament la visibilitat de certs botons (com {@code btnrolExtra})
 * en funció del rol de l'usuari (ADMIN, ENQUESTADOR, ENQUESTAT), oferint accés a funcionalitats
 * comunes com crear enquestes, respondre, consultar perfil i recomanacions.
 * </p>
 */
public class VistaPrincipalComuna extends JFrame {

    private CtrlPresentacio ctrl;
    private int idUsuari;
    private String nomUsuari; // Nomes pot ser "ADMIN", "ENQUESTADOR", "ENQUESTAT"

    private JPanel contentPanel = new JPanel();
    private JButton crearEnquestaButton = new JButton("Crear Enquesta");
    private JButton consultarPerfilButton = new JButton("Consultar Perfil");
    private JButton respondreEnquestaButton = new JButton("Responder Enquesta");
    private JButton consultarRecomanacionsButton =  new JButton("Consultar Recomanacions");
    private JButton btnrolExtra = new JButton();
    private JButton sortirButton = new JButton("Sortir");

    /**
     * Constructor de la Vista Principal Comuna.
     * Inicialitza la finestra, guarda les dades de l'usuari, configura els components
     * i adapta la interfície segons el rol.
     *
     * @param ctrl Referència al controlador de presentació.
     * @param idUsuario Identificador de l'usuari que ha iniciat sessió.
     * @param nomUsuari Nom de l'usuari (utilitzat per al títol de la finestra).
     */
    public VistaPrincipalComuna(CtrlPresentacio ctrl, int idUsuario, String nomUsuari) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuario;
        this.nomUsuari = nomUsuari;
        initComponents();
        configureUsuari();    // Configura visibilitat segons tipus d'usuari
        setupListeners();     // Configura els listeners dels botons
    }

    /**
     * Inicialitza i distribueix els components visuals de la interfície.
     * Crea un menú vertical amb les opcions comunes i un botó de sortida.
     */
    private void initComponents() {
        setTitle("Menu Usuari - " + nomUsuari);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPanel.setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,1,5,5));

        buttonPanel.add(crearEnquestaButton);
        buttonPanel.add(consultarPerfilButton);
        buttonPanel.add(respondreEnquestaButton);
        buttonPanel.add(consultarRecomanacionsButton);
        buttonPanel.add(btnrolExtra);

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
     * Configura la visibilitat i el text del botó extra segons el rol de l'usuari.
     * <ul>
     * <li><b>ADMIN:</b> Mostra "Funcions avançades".</li>
     * <li><b>ENQUESTADOR:</b> Mostra "Importar respostes".</li>
     * <li><b>Altres (ENQUESTAT):</b> Oculta el botó extra.</li>
     * </ul>
     */
    private void configureUsuari() {
        String rol = ctrl.obtenirRol(idUsuari);
        switch(rol){
            case "ADMIN":
                btnrolExtra.setVisible(true);
                btnrolExtra.setText("Funcions avançades");
                break;
            case "ENQUESTADOR" :
                btnrolExtra.setVisible(true);
                btnrolExtra.setText("Importar respostes");
                break;
            default:
                btnrolExtra.setVisible(false);
                break;
        }
    }

    /**
     * Modifica la visibilitat de la finestra.
     *
     * @param b True per mostrar la finestra, False per amagar-la.
     */
    public void hacerVisible(boolean b) {
        setVisible(b);
    }

    /**
     * Assigna els escoltadors (listeners) als botons.
     * Defineix les accions a realitzar quan es premen els botons (consultar perfil, recomanacions, etc.).
     * <p>
     * Nota: Algunes funcionalitats com 'Crear Enquesta', 'Respondre Enquesta' i el botó extra
     * tenen la lògica comentada o pendent d'implementació completa en aquesta classe base.
     * </p>
     */
    private void setupListeners() {
        consultarPerfilButton.addActionListener(e -> {
            ctrl.mostrarConsultarPerfil();
        });
        consultarRecomanacionsButton.addActionListener(e -> {
            ctrl.mostrarConsultarRecomanacions();
        });
        crearEnquestaButton.addActionListener(e -> {
            // ctrl.mostrarCrearEnquesta();
        });

        respondreEnquestaButton.addActionListener(e -> {
            // ctrl.mostrarRespondreEnquesta();
        });

        btnrolExtra.addActionListener(e -> {
            //String rol = ctrl.obtenirRol(idUsuari);
            //if (rol.equals("ADMIN")) {
            //ctrl.mostrarFuncionsAvançades();
            //} else if (rol.equals("ENQUESTADOR")) {
            // ctrl.mostrarImportarRespostes();
            //}
        });
        /*
        btnLogout.addActionListener(e -> {
            dispose();  // Tancar finestra
            new LoginView().setVisible(true);
        });
        */
    }
}