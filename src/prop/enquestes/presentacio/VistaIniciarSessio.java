package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VistaIniciarSessio extends JDialog {
    private JPanel contentPane;
    private CtrlPresentacio ctrl;

    private JTextField campID = new JTextField();
    private JPasswordField campPassword = new JPasswordField();
    private JButton buttonOK;
    private JButton buttonCancel;


    public VistaIniciarSessio(CtrlPresentacio ctrlPre) {
        super((Frame) null, "Iniciar Sessio", true);
        ctrl = ctrlPre;

        contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(4, 1, 10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        contentPane.add(new JLabel("ID:"));
        contentPane.add(campID);
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

        if (ctrl.iniciarSessio(id)) {
            ctrl.mostrarVistaPrincipalComuna(id);  // Obrir la següent vista
            dispose(); // Tancar el diàleg
        }
        else {
            JOptionPane.showMessageDialog(this, "Usuari no trobat.");
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
