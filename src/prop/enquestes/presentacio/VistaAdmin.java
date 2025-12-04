package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;

public class VistaAdmin extends JFrame {
    private CtrlPresentacio ctrl;
    private int idUsuari;
    private String nomUsuari;
    private String nomRol;
    private JPanel contentPanel = new JPanel();

    private JButton crearEnquestaButton = new JButton("Crear Enquesta");
    private JButton consultarEnquestaButton = new JButton("Consultar Enquesta");
    private JButton importarEnquestaButton = new JButton("Importar Enquesta");
    private JButton exportarEnquestaButton = new JButton("Exportar Enquesta");
    private JButton consultarUsuariButton  = new JButton("Consultar Usuari");
    private JButton enquestesRealitzadesButton = new JButton("Enquestes Realitzades");
    private JButton enquestesAdministradesButton = new JButton("Enquestes Administrades");
    private JButton consultarRecomanacionsButton = new JButton("Consultar Recomanacions");
    private JButton sortirButton = new JButton("Sortir");

    public VistaAdmin(CtrlPresentacio ctrl, int idUsuari, String nomUsuari, String nomRol) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;
        this.nomUsuari = nomUsuari;
        this.nomRol = nomRol;

        initComponents();
        setupListeners();     // Configura els listeners dels botons
    }

    private void initComponents() {
        setTitle("Menu ADMIN");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPanel.setLayout(new BorderLayout(10, 10));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(0, 1, 10, 10));

        panelBotones.add(crearEnquestaButton);
        panelBotones.add(consultarEnquestaButton);
        panelBotones.add(importarEnquestaButton);
        panelBotones.add(exportarEnquestaButton);
        panelBotones.add(consultarUsuariButton);
        panelBotones.add(enquestesRealitzadesButton);
        panelBotones.add(enquestesAdministradesButton);
        panelBotones.add(consultarRecomanacionsButton);


        JPanel panelSortir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSortir.add(sortirButton);

        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panelSortir.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel.add(panelBotones, BorderLayout.CENTER);
        contentPanel.add(panelSortir, BorderLayout.SOUTH);

        add(contentPanel);

        setContentPane(contentPanel);

        sortirButton.addActionListener(e -> {
            dispose();
        });

    }

    private void setupListeners() {
        crearEnquestaButton.addActionListener(e -> {
            ctrl.mostrarCrearEnquesta(idUsuari);
        });
        consultarEnquestaButton.addActionListener(e -> {
            ctrl.mostrarConsultarEnquesta();
        });
        importarEnquestaButton.addActionListener(e -> {
            ctrl.mostrarImportarEnquesta(idUsuari);
        });
        exportarEnquestaButton.addActionListener(e -> {
            ctrl.mostrarExportarEnquesta(idUsuari);
        });
        consultarUsuariButton.addActionListener(e -> {
            ctrl.mostrarConsultarPerfil();
        });
        enquestesRealitzadesButton.addActionListener(e -> {
            String nomEnquesta = "Realitzada";
            ctrl.mostrarEnquestesExtra(nomEnquesta, idUsuari);
        });
        enquestesAdministradesButton.addActionListener(e -> {
            String nomEnquesta = "Administrada";
            ctrl.mostrarEnquestesExtra(nomEnquesta, idUsuari);
        });
        consultarRecomanacionsButton.addActionListener(e -> {
            ctrl.mostrarConsultarRecomanacions();
        });

    }
}
