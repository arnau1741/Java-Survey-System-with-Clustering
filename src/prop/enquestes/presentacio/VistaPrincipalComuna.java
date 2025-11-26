package prop.enquestes.presentacio;

import prop.enquestes.domini.Usuari;
import prop.enquestes.domini.PerfilAdministrador;
import prop.enquestes.domini.PerfilEnquestador;
import prop.enquestes.domini.PerfilEnquestat;
import javax.swing.*;

public class VistaPrincipalComuna extends JFrame {

    private CtrlPresentacio ctrl;
    private int idUsuari;
    private String nomUsuari; ///Nomes pot ser "ADMIN", "ENQUESTADOR", "ENQUESTAT"

    private JPanel contentPanel = new JPanel();
    private JButton importarEnquestaButton;
    private JButton crearEnquestaButton = new JButton("Crear Enquesta");
    private JButton consultarPerfilButton;
    private JButton respondreEnquestaButton = new JButton("Responder Enquesta");
    private JButton consultarRecomanacionsButton =  new JButton("Consultar Recomanacions");
    private JButton btnrolExtra = new  JButton("Rol Extra");
    private JButton sortirButton =  new JButton("Sortir");

    public VistaPrincipalComuna(CtrlPresentacio ctrl, int idUsuario, String nomUsuari) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuario;
        this.nomUsuari = nomUsuari;
        initComponents();
        configureUsuari();    // Configura visibilitat segons tipus d'usuari
        // setupListeners();     // Configura els listeners dels botons
        contentPanel.add(crearEnquestaButton);
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
                btnrolExtra.setText("ADMIN");
                break;
            case "ENQUESTADOR" :
                btnrolExtra.setText("ENQUESTADOR");
                break;
            case "ENQUESTAT":
                btnrolExtra.setText("ENQUESTAT");
                break;
        }
        /*
        if (usuari instanceof PerfilAdministrador) {
            crearEnquestaButton.setVisible(true);
            importarEnquestaButton.setVisible(true);
            respondreEnquestaButton.setVisible(true);
            consultarPerfilButton.setVisible(true);
            consultarRecomanacionsButton.setVisible(true);
        }
        else if (usuari instanceof PerfilEnquestador) {
            crearEnquestaButton.setVisible(false);   // No pot crear
            importarEnquestaButton.setVisible(true);         // Pot importar respostes
            respondreEnquestaButton.setVisible(true);
            consultarPerfilButton.setVisible(true);
            consultarRecomanacionsButton.setVisible(false);
        }
        else if (usuari instanceof PerfilEnquestat) {

            PerfilEnquestat pe = (PerfilEnquestat) usuari;

            crearEnquestaButton.setVisible(false);
            importarEnquestaButton.setVisible(false);
            respondreEnquestaButton.setVisible(true);

            if (pe.getId() != -1) {
                // registrat
                consultarPerfilButton.setVisible(true);          // Té perfil
                consultarRecomanacionsButton.setVisible(true);   // Rep recomanacions
            }
            else {
                // no registrat
                consultarPerfilButton.setVisible(false);         // No hi ha perfil
                consultarRecomanacionsButton.setVisible(false);
            }
        }
        */

    }

    public void hacerVisible(boolean b) {
        setVisible(b);
    }

    // Pots posar aquí els listeners si vols
    /*private void setupListeners() {
        btnCrearEnquesta.addActionListener(e -> {
            CrearEnquestaView v = new CrearEnquestaView();
            v.setVisible(true);
        });

        btnImportar.addActionListener(e -> {
            ImportarRespostesView v = new ImportarRespostesView();
            v.setVisible(true);
        });

        btnRespondre.addActionListener(e -> {
            RespondreEnquestaView v = new RespondreEnquestaView();
            v.setVisible(true);
        });

        btnPerfil.addActionListener(e -> {
            PerfilView v = new PerfilView();
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
    }*/
}
