package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Vista principal per a l'usuari amb rol d'Administrador.
 * <p>
 * Aquesta classe representa el menú principal que veu un administrador en
 * iniciar sessió.
 * Proporciona accés a totes les funcionalitats del sistema: gestió d'enquestes
 * (crear, importar, exportar),
 * gestió d'usuaris (vetar/desvetar) i funcionalitats comunes (respondre,
 * consultar perfil).
 * </p>
 */
public class VistaAdmin extends JFrame {
    private CtrlPresentacio ctrl;
    private int idUsuari;
    private String nomUsuari;
    private String nomRol;
    private JPanel contentPanel = new JPanel();

    private JButton crearEnquestaButton = new JButton("Crear Enquesta");
    private JButton consultarEnquestaButton = new JButton("Consultar Enquesta");
    private JButton importarEnquestaButton = new JButton("Importar Enquesta");
    private JButton respondreEnquestaButton = new JButton("Respondre Enquesta");
    private JButton consultarUsuariButton = new JButton("Consultar Perfil");
    private JButton enquestesRealitzadesButton = new JButton("Enquestes Realitzades");
    private JButton enquestesAdministradesButton = new JButton("Enquestes Administrades");
    /// Faltaria decidir aquesta vista

    private JButton sortirButton = new JButton("Sortir");

    /**
     * Constructor de la vista d'administrador.
     * Inicialitza la finestra, guarda les dades de la sessió i configura els
     * components gràfics.
     *
     * @param ctrl      Referència al controlador de presentació per comunicar-se
     *                  amb la lògica.
     * @param idUsuari  Identificador únic de l'administrador actual.
     * @param nomUsuari Nom de l'administrador.
     * @param nomRol    Rol de l'usuari (en aquest cas, "ADMIN").
     */
    public VistaAdmin(CtrlPresentacio ctrl, int idUsuari, String nomUsuari, String nomRol) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;
        this.nomUsuari = nomUsuari;
        this.nomRol = nomRol;

        initComponents();
        setupListeners(); // Configura els listeners dels botons
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                ctrl.inicializarPresentacio();
            }
        });
    }

    /**
     * Inicialitza i configura els components visuals de la interfície.
     * Defineix el títol, la mida, la disposició dels botons (layout) i afegeix els
     * panells al frame.
     */
    private void initComponents() {
        setTitle("Menú Usuari - " + nomRol);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPanel.setLayout(new BorderLayout(10, 10));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(0, 1, 10, 10));

        panelBotones.add(crearEnquestaButton);
        panelBotones.add(consultarEnquestaButton);
        panelBotones.add(importarEnquestaButton);
        panelBotones.add(respondreEnquestaButton);
        panelBotones.add(consultarUsuariButton);
        panelBotones.add(enquestesRealitzadesButton);
        panelBotones.add(enquestesAdministradesButton);

        JPanel panelSortir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSortir.add(sortirButton);

        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panelSortir.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel.add(panelBotones, BorderLayout.CENTER);
        contentPanel.add(panelSortir, BorderLayout.SOUTH);

        add(contentPanel);

        setContentPane(contentPanel);

    }

    /**
     * Configura els listeners (escoltadors) per als botons.
     * Assigna a cada botó la funció corresponent del controlador de presentació.
     */
    private void setupListeners() {
        crearEnquestaButton.addActionListener(e -> {
            dispose();
            ctrl.mostrarCrearEnquesta(idUsuari);
        });
        consultarEnquestaButton.addActionListener(e -> {
            ctrl.mostrarConsultarEnquesta();
        });
        importarEnquestaButton.addActionListener(e -> {
            ctrl.mostrarImportarEnquesta(idUsuari);
        });
        respondreEnquestaButton.addActionListener(e -> {
            ctrl.mostrarRespondreEnquesta(idUsuari);
        });
        consultarUsuariButton.addActionListener(e -> {
            ctrl.mostrarConsultarPerfil(idUsuari);
        });
        enquestesRealitzadesButton.addActionListener(e -> {
            String nomEnquesta = "Realitzada";
            ctrl.mostrarEnquestesExtra(nomEnquesta, idUsuari);
        });
        enquestesAdministradesButton.addActionListener(e -> {
            String nomEnquesta = "Administrada";
            ctrl.mostrarEnquestesExtra(nomEnquesta, idUsuari);
        });
        sortirButton.addActionListener(e -> {
            dispose();
            ctrl.inicializarPresentacio();
        });

    }

    public void hacerVisible(boolean b) {
        setVisible(b);
    }
}