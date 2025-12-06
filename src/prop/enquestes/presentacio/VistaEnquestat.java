package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VistaEnquestat extends JFrame {
    private CtrlPresentacio ctrl;
    private int idUsuari;
    private String nomUsuari;
    private String nomRol;


    private JPanel contentPanel = new JPanel();
    private JButton crearEnquestaButton = new JButton("Crear Enquesta");
    private JButton consultarPerfilButton = new JButton("Consultar Perfil");
    private JButton respondreEnquestaButton = new JButton("Responder Enquesta");
    private JButton exportarRespostesButton = new JButton("Exportar Respostes");
    private JButton exportarEnquestatButton = new JButton("Exportar Enquesta");
    private JButton consultarRespostesButton = new JButton("Consultar Respostes");
    private JButton consultarRecomanacionsButton =  new JButton("Consultar Recomanacions");
    private JButton sortirButton = new JButton("Sortir");

    public VistaEnquestat (CtrlPresentacio ctrl, int idUsuari, String nomUsuari, String nomRol) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;
        this.nomUsuari = nomUsuari;
        this.nomRol = nomRol;

        initComponents();
        setupListeners();     // Configura els listeners dels botons
    }

    private void initComponents() {
        setTitle("Menu Usuari - " + nomRol);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPanel.setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,1,5,5));

        buttonPanel.add(crearEnquestaButton);
        buttonPanel.add(consultarPerfilButton);
        buttonPanel.add(respondreEnquestaButton);
        buttonPanel.add(exportarRespostesButton);
        buttonPanel.add(exportarEnquestatButton);
        buttonPanel.add(consultarRespostesButton);
        buttonPanel.add(consultarRecomanacionsButton);

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
            ctrl.mostrarCrearEnquesta(idUsuari);
        });

        respondreEnquestaButton.addActionListener(e -> {
            ctrl.mostrarRespondreEnquesta(idUsuari);
        });

        exportarRespostesButton.addActionListener(e -> {
            ctrl.mostrarExportarRespostes(idUsuari);
        });

        exportarEnquestatButton.addActionListener(e -> {
            ctrl.mostrarExportarEnquesta(idUsuari);
        });

        consultarRespostesButton.addActionListener(e -> {
            //ctrl.mostrarConsultarRespostes();
        });

    }

}
