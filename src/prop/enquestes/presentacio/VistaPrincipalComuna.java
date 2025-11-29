package prop.enquestes.presentacio;

import prop.enquestes.domini.Usuari;
import javax.swing.*;
import java.awt.*;

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

    public VistaPrincipalComuna(CtrlPresentacio ctrl, int idUsuario, String nomUsuari) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuario;
        this.nomUsuari = nomUsuari;
        initComponents();
        configureUsuari();    // Configura visibilitat segons tipus d'usuari
        setupListeners();     // Configura els listeners dels botons
    }

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

    public void hacerVisible(boolean b) {
        setVisible(b);
    }

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
            String rol = ctrl.obtenirRol(idUsuari);
            if (rol.equals("ADMIN")) {
                ctrl.mostrarFuncionsAvançades();
            } else if (rol.equals("ENQUESTADOR")) {
                // ctrl.mostrarImportarRespostes();
            }
        });
        /*
        btnLogout.addActionListener(e -> {
            dispose();  // Tancar finestra
            new LoginView().setVisible(true);
        });
        */
    }
}
