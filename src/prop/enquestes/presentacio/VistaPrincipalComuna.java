package prop.enquestes.presentacio;

import prop.enquestes.domini.Usuari;
import prop.enquestes.domini.PerfilAdministrador;
import prop.enquestes.domini.PerfilEnquestador;
import prop.enquestes.domini.PerfilEnquestat;
import javax.swing.*;

public class VistaPrincipalComuna extends JFrame {

    private Usuari usuari;

    private JPanel contentPanel;
    private JButton importarEnquestaButton;
    private JButton crearEnquestaButton;
    private JButton consultarPerfilButton;
    private JButton respondreEnquestaButton;
    private JButton consultarRecomanacionsButton;
    private JButton sortirButton;

    public VistaPrincipalComuna(Usuari usuari) {
        this.usuari = usuari;

        initComponents();
        configureUsuari();    // Configura visibilitat segons tipus d'usuari
        // setupListeners();     // Configura els listeners dels botons

        setLocationRelativeTo(null); // Centrar pantalla
        setTitle("Pantalla Principal");
    }

    private void initComponents() {

    }

    private void configureUsuari() {
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
