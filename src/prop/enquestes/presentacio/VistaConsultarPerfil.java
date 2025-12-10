package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.List;

public class VistaConsultarPerfil extends JDialog {
    private CtrlPresentacio ctrl;

    private JPanel contentPane = new JPanel();
    // private JTextField campID = new JTextField(10);
    private JComboBox<String> comboUsuaris = new JComboBox<>();
    private JTextArea perfil = new JTextArea();
    private JButton buttonOK = new JButton("Consultar");
    private JButton buttonCancel = new JButton("Cancel·lar");

    public VistaConsultarPerfil(CtrlPresentacio ctrl) {
        super((Frame) null, "Consultar Perfil", true);
        this.ctrl = ctrl;

        setSize(450,350);
        setLocationRelativeTo(null);
        contentPane.setLayout(new BorderLayout(10, 10));

        // PANEL SUPERIOR
        JPanel top = new JPanel(new FlowLayout());
        // top.add(new JLabel("ID de l'usuari:"));
        // top.add(campID);
        top.add(new JLabel("Usuaris:"));
        top.add(comboUsuaris);
        contentPane.add(top, BorderLayout.NORTH);

        carregarUsuaris();

        // AREA DEL PERFIL
        perfil.setEditable(false);
        JScrollPane scroll = new JScrollPane(perfil);
        contentPane.add(scroll, BorderLayout.CENTER);

        // BOTONS INFERIORS
        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(buttonOK);
        bottom.add(buttonCancel);
        contentPane.add(bottom, BorderLayout.SOUTH);

        add(contentPane);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // LISTENERS
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    // CARREGAR USUARIS
    private void carregarUsuaris() {
        comboUsuaris.removeAllItems();
        comboUsuaris.addItem("-- Selecciona un usuari --");

        List<String> usuaris = ctrl.obtenirUsuaris();
        for (String nom : usuaris) {
            comboUsuaris.addItem(nom);
        }
    }

    // CONSULTAR PERFIL
    private void onOK() {
        // String txt = campID.getText().trim();
        String txt = (String) comboUsuaris.getSelectedItem();
        if (txt == null || txt.equals("-- Selecciona un usuari --")) {
            JOptionPane.showMessageDialog(this, "Has de seleccionar un usuari.");
            return;
        }

        int id = ctrl.getIdUsuariPerNom(txt);
        if (id == -1) {
            JOptionPane.showMessageDialog(this, "Usuari no trobat.");
            return;
        }

        List<String> dades = ctrl.consultarPerfil(id);
        StringBuilder sb = new StringBuilder();
        for (String s : dades) sb.append(s).append("\n");
        perfil.setText(sb.toString());
    }

    private void onCancel() {
        dispose();
    }
}
