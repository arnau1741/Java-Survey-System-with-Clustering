package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VistaCrearUsuari extends JDialog {
    private JPanel contentPane = new JPanel();
    private CtrlPresentacio ctrl;

    private JTextField campNom = new JTextField();
    private JPasswordField campcont = new JPasswordField();
    private JTextField campEmail = new JTextField();

    private JComboBox<String> comboRol = new JComboBox<>(new String[] {
            "ENQUESTAT",
            "ENQUESTADOR",
            "ADMIN"
    });

    private JButton buttonOK;
    private JButton buttonCancel;

    public VistaCrearUsuari(CtrlPresentacio c) {
        super((Frame) null, "Crear Usuari", true);
        ctrl = c;

        setSize(350, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        contentPane.setLayout(new BorderLayout(10, 10));

        // Panel para los campos de formulario
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        panelForm.add(new JLabel("Nom:"));
        panelForm.add(campNom);
        panelForm.add(new JLabel("Contrasenya:"));
        panelForm.add(campcont);
        panelForm.add(new JLabel("Email:"));
        panelForm.add(campEmail);
        panelForm.add(new JLabel("Rol"));
        panelForm.add(comboRol);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(buttonOK);
        panelBotones.add(buttonCancel);

        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPane.add(panelForm, BorderLayout.CENTER);
        contentPane.add(panelBotones, BorderLayout.SOUTH);

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
        String nom = campNom.getText();
        String cont = campcont.getText();
        String email = campEmail.getText();
        String rol = (String) comboRol.getSelectedItem();
        int id = ctrl.crearUsuari(nom,cont,email,rol);
        if(id == -1) {
            JOptionPane.showMessageDialog(this, "Nom usuari repetit.");
        }
        else if(id == -2) {
            JOptionPane.showMessageDialog(this, "El password no compleix els requisits");
        }
        else if(id == -3) {
            JOptionPane.showMessageDialog(this, "Email ja esta en us");
        }
        else {
            ctrl.mostrarVistaPrincipalComuna(id);
            dispose();
        }

    }

    private void onCancel() {
        ctrl.inicializarPresentacio();
        dispose();
    }

    public void tancar() {
        dispose();
    }
}

