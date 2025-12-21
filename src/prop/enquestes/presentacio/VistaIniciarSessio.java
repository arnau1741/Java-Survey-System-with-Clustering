package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Diàleg per a l'autenticació d'usuaris al sistema.
 * <p>
 * Aquesta vista presenta un formulari senzill on l'usuari ha d'introduir
 * el seu nom i contrasenya per accedir a les funcionalitats de l'aplicació.
 * </p>
 */
public class VistaIniciarSessio extends JDialog {
    private JPanel contentPane;
    private CtrlPresentacio ctrl;

    private JTextField campNomUsuari = new JTextField();
    private JPasswordField campPassword = new JPasswordField();
    private JButton buttonOK = new JButton("OK");
    private JButton buttonCancel = new JButton("Cancel");

    /**
     * Constructor de la vista d'inici de sessió.
     * Configura la interfície gràfica (camps de text i botons), defineix la mida
     * i assigna els escoltadors d'esdeveniments per a la interacció.
     *
     * @param ctrlPre Referència al controlador de presentació.
     */
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


        setSize(900, 700);
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

    /**
     * Gestiona l'intent d'inici de sessió.
     * Recupera les credencials introduïdes, valida que no estiguin buides i
     * sol·licita al controlador que verifiqui l'usuari. Si és correcte,
     * redirigeix a la vista principal corresponent; si no, mostra un error.
     */
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

    /**
     * Cancel·la el procés d'autenticació.
     * Tanca el diàleg i retorna el control a la pantalla inicial de l'aplicació.
     */
    private void onCancel() {
        //ctrl.inicializarPresentacio();
        dispose();
        ctrl.inicializarPresentacio();
    }

    /**
     * Tanca la finestra de diàleg i allibera els recursos.
     */
    public void tancar() {
        dispose();
    }
}