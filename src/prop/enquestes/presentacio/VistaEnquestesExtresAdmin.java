package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;

public class VistaEnquestesExtresAdmin extends JFrame {

    private CtrlPresentacio ctrl;
    private int idUsuari;
    private String nomEnquesta;
    private JPanel contentPanel = new JPanel();
    private JButton exportarEnquestaButton = new  JButton("Exportar enquesta");
    private JButton exportarRespostesButton = new  JButton("Exportar resposta");
    private JButton respondreEnquestaButton = new  JButton("Respondre enquesta");
    private JButton consultarRespostesButton = new  JButton("Consultar resposta");
    private JButton importarRespostesButton = new  JButton("Importar resposta");
    private JButton modificarEnquestaButton = new JButton("Modificar enquesta");
    private JButton donarPodersButton = new JButton("Donar poders");
    private JButton consultarRecomanacionsButton = new JButton("Consultar Recomanacions");
    private JButton sortirButton = new JButton("Sortir");
    public VistaEnquestesExtresAdmin(CtrlPresentacio ctrl,  String nomEnquesta, int idUsuari) {
        this.ctrl = ctrl;
        this.nomEnquesta = nomEnquesta;
        this.idUsuari = idUsuari;
        initComponents();
        setupListeners();     // Configura els listeners dels botons
    }

    public void initComponents() {
        setTitle("Menu enquesta" + nomEnquesta);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPanel.setLayout(new BorderLayout(10, 10));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(0, 1, 10, 10));

        if(nomEnquesta.equals("Realitzada")) {
            panelBotones.add(exportarEnquestaButton);
            panelBotones.add(exportarRespostesButton);
            panelBotones.add(consultarRespostesButton);
        }
        else {
            panelBotones.add(exportarEnquestaButton);
            panelBotones.add(exportarRespostesButton);
            panelBotones.add(importarRespostesButton);
            panelBotones.add(modificarEnquestaButton);
            panelBotones.add(consultarRespostesButton);
            panelBotones.add(donarPodersButton);
        }
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

    public void setupListeners() {
        modificarEnquestaButton.addActionListener(e -> {
            ctrl.mostrarVistaModificarEnquesta(idUsuari);
        });
        consultarRecomanacionsButton.addActionListener(e -> {
            ctrl.mostrarConsultarRecomanacions();
        });
        importarRespostesButton.addActionListener(e -> {
            ctrl.mostrarImportarRespostes(idUsuari);
        });
        exportarRespostesButton.addActionListener(e -> {
            ctrl.mostrarExportarRespostes(idUsuari);
        });
        exportarEnquestaButton.addActionListener(e -> {
            ctrl.mostrarExportarEnquesta(idUsuari);
        });
        donarPodersButton.addActionListener(e -> {
            ctrl.mostrarDonarPoders(idUsuari);
        });
        consultarRespostesButton.addActionListener(e -> {
            ctrl.mostrarConsultarRespostes(idUsuari);
        });

    }
}
