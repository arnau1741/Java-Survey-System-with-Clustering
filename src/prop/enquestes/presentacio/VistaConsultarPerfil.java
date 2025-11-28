package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.List;

public class VistaConsultarPerfil extends JDialog {
    private CtrlPresentacio ctrl;

    private JPanel contentPane = new JPanel();
    private JTextField campID = new JTextField(10);
    private JTextArea perfil = new JTextArea();
    private JButton buttonOK = new JButton("OK");
    private JButton buttonCancel = new JButton("Cancelar");

    public VistaConsultarPerfil(CtrlPresentacio ctrl) {
        super((Frame) null, "Consultar Perfil", true);
        this.ctrl = ctrl;

        setSize(450,350);
        setLocationRelativeTo(null);
        contentPane.setLayout(new BorderLayout(10, 10));
        JPanel top = new JPanel(new FlowLayout());
        top.add(new JLabel("ID de l'usuari:"));
        top.add(campID);
        contentPane.add(top, BorderLayout.NORTH);

        perfil.setEditable(false);
        JScrollPane scroll = new JScrollPane(perfil);
        contentPane.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(buttonOK);
        bottom.add(buttonCancel);
        contentPane.add(bottom, BorderLayout.SOUTH);

        add(contentPane);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

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

    private void onOK() {
        String txt = campID.getText().trim();
        if (txt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Has d'introduir un ID.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(txt);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "L'ID ha de ser un número.");
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
