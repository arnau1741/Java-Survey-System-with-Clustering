package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;

public class VistaConvidat extends JFrame {
    private CtrlPresentacio ctrl;
    private int idUsuari;

    private JPanel contentPanel = new JPanel();
    private JButton buttonRespondreEnquesta = new JButton("Respondre Enquesta");
    private JButton buttonSortir = new JButton("Sortir");

    public VistaConvidat(CtrlPresentacio ctrl, int idUsuari) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;

        initComponents();
        setupListeners();     // Configura els listeners dels botons
    }

    private void initComponents() {
        setTitle("Menu convidat");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPanel.setLayout(new BorderLayout(10, 10));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));

        panelBotones.add(buttonRespondreEnquesta);


        JPanel panelSortir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSortir.add(buttonSortir);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panelSortir.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel.add(panelBotones, BorderLayout.CENTER);
        contentPanel.add(panelSortir, BorderLayout.SOUTH);
        add(contentPanel);
        setContentPane(contentPanel);

        buttonSortir.addActionListener(e -> {
            dispose();
        });

    }

    private void setupListeners() {
        buttonRespondreEnquesta.addActionListener(e -> {
            ctrl.mostrarRespondreEnquesta(idUsuari);
        });
    }
}
