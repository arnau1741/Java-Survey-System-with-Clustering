package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VistaIniciarSessio extends JDialog {
    private JPanel contentPane;
    private CtrlPresentacio ctrl;

    private JTextField campNomUsuari = new JTextField();
    private JPasswordField campPassword = new JPasswordField();
    private JButton buttonOK;
    private JButton buttonCancel;


    public VistaIniciarSessio(CtrlPresentacio ctrlPre) {
        super((Frame) null, "Iniciar Sessio", true);
        ctrl = ctrlPre;

        contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(4, 1, 10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        contentPane.add(new JLabel("Nom Usuari:"));
        contentPane.add(campNomUsuari);
        contentPane.add(new JLabel("Contrasenya:"));
        contentPane.add(campPassword);
        contentPane.add(buttonOK);
        contentPane.add(buttonCancel);


        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2,1,10,10));
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
        // add your code here
        String nomUsuari = campNomUsuari.getText().trim();
        String pass = new String(campPassword.getPassword());

        if (nomUsuari.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Has d'introduir un nom d'usuari.");
            return;
        }
        int id = 0;
        try {
            id = ctrl.iniciarSessio(nomUsuari, pass);
            ctrl.mostrarVistaPrincipalComuna(id);
            dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Error de l'argument:\n" + e.getMessage(),
                    "Format invàlid",
                    JOptionPane.ERROR_MESSAGE);
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
