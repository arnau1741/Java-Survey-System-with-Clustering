package prop.enquestes.presentacio;

import prop.enquestes.domini.Usuari;
import prop.enquestes.domini.PerfilAdministrador;
import prop.enquestes.domini.PerfilEnquestador;
import prop.enquestes.domini.PerfilEnquestat;
import javax.swing.*;

public class VistaPrincipalComuna extends JFrame {

    private CtrlPresentacio ctrl;
    private int idUsuari;
    private String nomUsuari; // Nomes pot ser "ADMIN", "ENQUESTADOR", "ENQUESTAT"

    private JPanel contentPanel = new JPanel();
    private JButton crearEnquestaButton = new JButton("Crear Enquesta");
    private JButton consultarPerfilButton = new JButton("Consultar Perfil");
    private JButton respondreEnquestaButton = new JButton("Responder Enquesta");
    private JButton consultarRecomanacionsButton =  new JButton("Consultar Recomanacions");
    private JButton btnrolExtra = new JButton("Funcions avançades");
    private JButton sortirButton = new JButton("Sortir");

    public VistaPrincipalComuna(CtrlPresentacio ctrl, int idUsuario, String nomUsuari) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuario;
        this.nomUsuari = nomUsuari;
        initComponents();
        configureUsuari();    // Configura visibilitat segons tipus d'usuari
        setupListeners();     // Configura els listeners dels botons
        contentPanel.add(consultarRecomanacionsButton);
        contentPanel.add(crearEnquestaButton);
        contentPanel.add(consultarPerfilButton);
        contentPanel.add(respondreEnquestaButton);
        contentPanel.add(consultarRecomanacionsButton);
        contentPanel.add(btnrolExtra);
        contentPanel.add(sortirButton);
        add(contentPanel);
    }

    private void initComponents() {
        setTitle("Menu Usuari - " + nomUsuari);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        sortirButton.addActionListener(e -> {
            dispose();
        });

    }

    private void configureUsuari() {
        String rol = ctrl.obtenirRol(idUsuari);
        switch(rol){
            case "ADMINISTRADOR":
                btnrolExtra.setVisible(true);
                btnrolExtra.setText("ADMIN");
                break;
            case "ENQUESTADOR" :
                btnrolExtra.setVisible(true);
                btnrolExtra.setText("ENQUESTADOR");
                break;
            case "ENQUESTAT":
                btnrolExtra.setVisible(false);
                btnrolExtra.setText("ENQUESTAT");
                break;
        }
    }

    public void hacerVisible(boolean b) {
        setVisible(b);
    }

    private void setupListeners() {
        consultarPerfilButton.addActionListener(e -> {
            ctrl.mostrarConsultarPerfil();
        });
        /*
        btnCrearEnquesta.addActionListener(e -> {
            CrearEnquestaView v = new CrearEnquestaView();
            v.setVisible(true);
        });

        btnRespondre.addActionListener(e -> {
            RespondreEnquestaView v = new RespondreEnquestaView();
            v.setVisible(true);
        });



        btnRecomanacions.addActionListener(e -> {
            RecomanacionsView v = new RecomanacionsView();
            v.setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            dispose();  // Tancar finestra
            new LoginView().setVisible(true);
        });
        */
    }
}
