package prop.enquestes.presentacio;

import prop.enquestes.excepcions.UsuariNoValid;

import javax.swing.*;
import java.awt.*;

/**
 * Diàleg per a la gestió de bloquejos (vetos) d'usuaris.
 * <p>
 * Aquesta vista permet a un moderador o administrador introduir el nom d'un usuari
 * per bloquejar-li l'accés al sistema (vetar) o restaurar-li els permisos (desvetar).
 * </p>
 */
public class VistaVetarDesvetar extends JDialog {

    private final CtrlPresentacio ctrl;
    private final int idExecutor;

    private JPanel contentPane = new JPanel();
    private JTextField fieldNom = new JTextField();

    private JButton btnVetar = new JButton("Vetar Usuari");
    private JButton btnDesvetar = new JButton("Desvetar Usuari");
    private JButton btnSortir = new JButton("Tancar");

    private JButton buttonOK = new JButton("OK");

    /**
     * Constructor de la vista de gestió de vetos.
     * Configura la finestra modal, defineix la mida i inicialitza els components.
     *
     * @param ctrl Referència al controlador de presentació.
     * @param idExecutor Identificador de l'usuari (moderador/admin) que executa l'acció.
     */
    public VistaVetarDesvetar(CtrlPresentacio ctrl, int idExecutor) {
        this.ctrl = ctrl;
        this.idExecutor = idExecutor;

        setTitle("Gestionar Vetos d'Usuaris");
        setModal(true);
        setSize(380, 200);
        setLocationRelativeTo(null);

        initLayout();
        initActions();
    }

    /**
     * Inicialitza la distribució gràfica de la finestra.
     * Organitza el camp d'entrada del nom d'usuari i els botons d'acció en un panell central.
     */
    private void initLayout() {
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel center = new JPanel(new GridLayout(2, 2, 8, 8));
        center.add(new JLabel("Nom de l'usuari:"));
        center.add(fieldNom);

        center.add(btnVetar);
        center.add(btnDesvetar);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnSortir);

        contentPane.add(center, BorderLayout.CENTER);
        contentPane.add(bottom, BorderLayout.SOUTH);

        setContentPane(contentPane);
    }

    /**
     * Configura els esdeveniments (listeners) dels botons.
     * <ul>
     * <li><b>Vetar:</b> Llegeix el nom i sol·licita al controlador bloquejar l'usuari.</li>
     * <li><b>Desvetar:</b> Llegeix el nom i sol·licita al controlador desbloquejar l'usuari.</li>
     * <li><b>Tancar:</b> Tanca la finestra.</li>
     * </ul>
     * També gestiona la captura d'excepcions en cas que l'usuari no sigui vàlid.
     */
    private void initActions() {

        btnVetar.addActionListener(e -> {
            String nom = fieldNom.getText().trim();
            if (nom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Escriu un nom d'usuari.");
                return;
            }

            try {
                ctrl.vetarUsuari(idExecutor, nom);
                JOptionPane.showMessageDialog(this, "Usuari vetat correctament!",
                        "Èxit",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (UsuariNoValid ex) {
                JOptionPane.showMessageDialog(this,
                        "Error de l'usuari:" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDesvetar.addActionListener(e -> {
            String nom = fieldNom.getText().trim();
            if (nom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Escriu un nom d'usuari.");
                return;
            }

            try {
                ctrl.desvetarUsuari(idExecutor, nom);
                JOptionPane.showMessageDialog(this,
                        "Usuari desvetat correctament!",
                        "Èxit",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (UsuariNoValid ex) {
                JOptionPane.showMessageDialog(this,
                        "Error de l'usuari:" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSortir.addActionListener(e -> {
            dispose();
            ctrl.mostrarVistaPrincipalComuna(idExecutor);
        });
    }
}