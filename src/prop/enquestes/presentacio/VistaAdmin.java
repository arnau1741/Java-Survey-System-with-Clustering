package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;

public class VistaAdmin extends JFrame {
    private CtrlPresentacio ctrl;
    private JPanel contentPanel = new JPanel();
    private JButton clustering = new JButton("Clustering");
    private JButton modificarEnquesta = new JButton("Modificar Enquesta");
    private JButton analisiEstadistica = new JButton("Analisi de Estadistica");
    private JButton exportarRespostes = new JButton("Exportar Respostes");
    private JButton sortirButton = new JButton("Sortir");

    public VistaAdmin(CtrlPresentacio ctrl) {
        this.ctrl = ctrl;

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

        panelBotones.add(clustering);
        panelBotones.add(modificarEnquesta);
        panelBotones.add(analisiEstadistica);
        panelBotones.add(exportarRespostes);

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
        clustering.addActionListener(e -> {

        });
        modificarEnquesta.addActionListener(e -> {
            ctrl.mostrarVistaModificarEnquesta();
        });
        analisiEstadistica.addActionListener(e -> {

        });
        exportarRespostes.addActionListener(e -> {

        });


    }
}
