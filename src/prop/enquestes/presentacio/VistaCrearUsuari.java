package prop.enquestes.presentacio;

import prop.enquestes.excepcions.UsuariNoValid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Diàleg per al registre de nous usuaris al sistema.
 * <p>
 * Aquesta vista presenta un formulari perquè l'usuari introdueixi les seves dades
 * (nom, contrasenya, email) i seleccioni el rol desitjat (Enquestat, Enquestador o Admin).
 * </p>
 */
public class VistaCrearUsuari extends JDialog {
    private JPanel contentPane = new JPanel();
    private CtrlPresentacio ctrl;

    private JTextField campNom = new JTextField();
    private JPasswordField campcont = new JPasswordField();
    private JTextField campEmail = new JTextField();

    private JComboBox<String> comboRol = new JComboBox<>(new String[] {
            "ENQUESTAT",
            "ENQUESTADOR"
    });

    private JButton buttonOK = new JButton("OK");
    private JButton buttonCancel = new JButton("Cancelar");

    /**
     * Constructor de la vista de creació d'usuari.
     * Inicialitza els camps del formulari, configura la disposició (Layout)
     * i assigna els escoltadors d'esdeveniments als botons.
     *
     * @param c Referència al controlador de presentació.
     */
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

    /**
     * Gestiona l'acció de confirmar el registre.
     * Recull les dades introduïdes, intenta crear l'usuari a través del controlador
     * i, si té èxit, redirigeix a la pantalla principal comuna.
     * En cas d'error (usuari no vàlid), mostra un missatge d'alerta.
     */
    private void onOK() {
        String nom = campNom.getText();
        String cont = campcont.getText();
        String email = campEmail.getText();
        String rol = (String) comboRol.getSelectedItem();
        int id = 0;
        try {
            id = ctrl.crearUsuari(nom,cont,email,rol);
            ctrl.mostrarVistaPrincipalComuna(id);
            dispose();
        } catch (UsuariNoValid e) {
            JOptionPane.showMessageDialog(this, "Error d'usuari: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error de parametres: " + e.getMessage());
        }

    }

    /**
     * Gestiona l'acció de cancel·lar el registre.
     * Torna a la pantalla inicial de l'aplicació i tanca el diàleg actual.
     */
    private void onCancel() {
        ctrl.inicializarPresentacio();
        dispose();
    }

    /**
     * Tanca la finestra de diàleg i allibera els recursos gràfics.
     */
    public void tancar() {
        dispose();
    }
}