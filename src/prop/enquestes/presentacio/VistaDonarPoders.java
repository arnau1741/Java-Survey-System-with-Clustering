package prop.enquestes.presentacio;

import prop.enquestes.excepcions.EnquestaNoExisteixException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class VistaDonarPoders extends JDialog {

    private CtrlPresentacio ctrl;
    private int idExecutor;

    private JPanel contentPane = new JPanel();
    private JComboBox<String> comboEnquestes;
    private JTextField fieldNomTarget;
    private JComboBox<String> comboRol;
    private JButton buttonOK = new JButton("Assignar");
    private JButton buttonCancel = new JButton("Cancel·lar");

    private java.util.List<Integer> idsEnquestes;

    public VistaDonarPoders(CtrlPresentacio ctrl, int idExecutor) {
        this.ctrl = ctrl;
        this.idExecutor = idExecutor;

        setTitle("Assignar Poders");
        setModal(true);
        setSize(400, 250);
        setLocationRelativeTo(null);

        initComponents();
        carregarEnquestes();
        initActions();
    }

    private void initComponents() {
        contentPane.setLayout(new BorderLayout(10, 10));
        JPanel center = new JPanel(new GridLayout(4, 2, 8, 8));

        center.add(new JLabel("Enquesta:"));
        comboEnquestes = new JComboBox<>();
        center.add(comboEnquestes);

        center.add(new JLabel("Nom usuari target:"));
        fieldNomTarget = new JTextField();
        center.add(fieldNomTarget);

        center.add(new JLabel("Rol a assignar:"));
        comboRol = new JComboBox<>(new String[]{"Enquestador", "Administrador"});
        center.add(comboRol);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(buttonOK);
        bottom.add(buttonCancel);

        contentPane.add(center, BorderLayout.CENTER);
        contentPane.add(bottom, BorderLayout.SOUTH);

        add(contentPane);
        setContentPane(contentPane);
    }

    private void carregarEnquestes() {
        comboEnquestes.removeAllItems();
        idsEnquestes = new ArrayList<>();
        comboEnquestes.addItem("-- Selecciona --");
        idsEnquestes.add(-1);

        try {
            List<String> enquestasInfo = ctrl.obtenirEnquestesAdministrades(idExecutor);
            for (String info : enquestasInfo) {
                // Buscamos la línea exacta que empieza por "ID:"
                String[] lineas = info.split("\n");
                for (String linea : lineas) {
                    linea = linea.trim();
                    if (linea.startsWith("ID:")) {
                        String part = linea.substring(3).trim();
                        String idStr = part.split("-")[0].trim();
                        int id = Integer.parseInt(idStr);
                        comboEnquestes.addItem(linea); // SOLO esta línea
                        idsEnquestes.add(id);
                        break; // Muy importante
                    }
                }
            }
        } catch (EnquestaNoExisteixException e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initActions() {
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> dispose());
    }

    private void onOK() {
        int idx = comboEnquestes.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Has de seleccionar una enquesta.");
            return;
        }

        int idEnquesta = idsEnquestes.get(idx);
        String nomTarget = fieldNomTarget.getText().trim();
        String rol = (String) comboRol.getSelectedItem();

        try {
            int result;
            if (rol.equals("Enquestador")) {
                result = ctrl.donarPoders(idExecutor, idEnquesta, nomTarget, rol);
            } else {
                result = ctrl.donarPoders(idExecutor, idEnquesta, nomTarget, rol);
            }
            if(result == 1) {
                JOptionPane.showMessageDialog(this,
                        "Poder assignat correctament!",
                        "Èxit",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
