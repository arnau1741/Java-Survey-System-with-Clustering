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
        //setupListeners();     // Configura els listeners dels botons
        contentPanel.add(clustering);
        contentPanel.add(modificarEnquesta);
        contentPanel.add(analisiEstadistica);
        contentPanel.add(exportarRespostes);
        contentPanel.add(sortirButton);
        add(contentPanel);
    }

    private void initComponents() {
        setTitle("Menu ADMIN");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        sortirButton.addActionListener(e -> {
            dispose();
        });

    }
}
