package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class VistaExportarEnquesta extends JDialog {

    private final CtrlPresentacio ctrl;
    private final int idUsuari;

    private JPanel contentPane = new JPanel();
    private JComboBox<String> comboEnquestes = new JComboBox<>();
    private JButton btnExportar = new JButton("Exportar");
    private JButton btnCancel = new JButton("Cancel·lar");

    private List<Integer> idsEnquestes = new ArrayList<>();

    public VistaExportarEnquesta(CtrlPresentacio ctrl, int idUsuari) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;

        setTitle("Exportar Enquesta");
        setModal(true);
        setContentPane(contentPane);

        initLayout();
        carregarEnquestesDesDelControlador();
        initActions();

        pack();
        setLocationRelativeTo(null);
    }

    private void initLayout() {
        contentPane.setLayout(new BorderLayout(10,10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel top = new JPanel(new BorderLayout(6,6));
        top.add(new JLabel("Selecciona una enquesta:"), BorderLayout.WEST);
        top.add(comboEnquestes, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnExportar);
        bottom.add(btnCancel);

        contentPane.add(top, BorderLayout.NORTH);
        contentPane.add(bottom, BorderLayout.SOUTH);
    }

    private void carregarEnquestesDesDelControlador() {
        try {
            List<Integer> ids = ctrl.getIdsEnquestes();
            List<String> titols = ctrl.getTitolsEnquestes();

            if (ids.size() != titols.size()) {
                JOptionPane.showMessageDialog(this,
                        "Error: Llistes d'IDs i títols no coincideixen.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            idsEnquestes.clear();
            comboEnquestes.removeAllItems();

            for (int i = 0; i < ids.size(); i++) {
                idsEnquestes.add(ids.get(i));
                comboEnquestes.addItem(titols.get(i));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No s'han pogut carregar les enquestes.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initActions() {
        btnExportar.addActionListener(e -> onExportar());
        btnCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    private void onExportar() {
        int idx = comboEnquestes.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this,
                    "Has de seleccionar una enquesta.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idEnquesta = idsEnquestes.get(idx);

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar enquesta com...");
        chooser.setSelectedFile(new java.io.File(comboEnquestes.getSelectedItem() + ".txt"));

        int res = chooser.showSaveDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) return;

        String path = chooser.getSelectedFile().getAbsolutePath(); // mirar importar (hacer lo mismo)

        try {
            List<String> contingut = ctrl.exportarEnquesta(idUsuari, idEnquesta);

            try (FileWriter fw = new FileWriter(path)) {
                for (String line : contingut) fw.write(line + "\n");
            }

            JOptionPane.showMessageDialog(this,
                    "Enquesta exportada correctament!",
                    "Èxit",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error en exportar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }
}