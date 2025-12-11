package prop.enquestes.presentacio;

import prop.enquestes.excepcions.UsuariNoValid;

import javax.swing.*;
import java.awt.*;

public class VistaVetarDesvetar extends JDialog {

    private final CtrlPresentacio ctrl;
    private final int idExecutor;

    private JPanel contentPane = new JPanel();
    private JTextField fieldNom = new JTextField();

    private JButton btnVetar = new JButton("Vetar Usuari");
    private JButton btnDesvetar = new JButton("Desvetar Usuari");
    private JButton btnSortir = new JButton("Tancar");

    private JButton buttonOK = new JButton("OK");

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

        btnSortir.addActionListener(e -> dispose());
    }
}
