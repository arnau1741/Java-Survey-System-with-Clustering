package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VistaCrearUsuari extends JDialog {
    private JPanel contentPane;
    private CtrlPresentacio ctrl;

    private JTextField campNom = new JTextField();
    private JTextField campcont = new JTextField();
    private JTextField campEmail = new JTextField();

    private JButton buttonOK;
    private JButton buttonCancel;

    public VistaCrearUsuari(CtrlPresentacio c) {
        super((Frame) null, "Crear Usuari", true);
        ctrl = c;

        setSize(350, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Nom:"));
        add(campNom);

        add(new JLabel("Contrasenya:"));
        add(campcont);

        add(new JLabel("Email:"));
        add(campEmail);

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
        String nom = campNom.getText();
        String cont = campcont.getText();
        String email = campEmail.getText();
        int id = ctrl.crearUsuari(nom,cont,email);
        ctrl.mostrarVistaPrincipalComuna(id);
        dispose();
    }

    private void onCancel() {
        ctrl.inicializarPresentacio();
        dispose();
    }

    public void tancar() {
        dispose();
    }
}

