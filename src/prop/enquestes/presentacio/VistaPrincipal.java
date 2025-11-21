package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;

public class VistaPrincipal {
    private CtrlPresentacio iCtrlPresentacio;
    private JFrame ventana = new JFrame("Sistema d'Enquestes");
    private JPanel panel = new JPanel();

    private JButton btnIniciarSessio = new JButton("Iniciar Sessio");
    private JButton btnCrearUsuari = new JButton("Crear Usuari");
    private JButton btnSortir = new JButton("Sortir");
    private JButton iniciarSessioButton;
    private JPanel panel1;
    private JButton crearUsuariButton;
    private JButton sortirButton;

    public VistaPrincipal(CtrlPresentacio ctrlPre) {
        iCtrlPresentacio = ctrlPre;
        inicializarComponentes();
    }
    private void inicializarComponentes() {

        ventana.setSize(400, 300);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);

        panel.setLayout(new GridLayout(3, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(btnIniciarSessio);
        panel.add(btnCrearUsuari);
        panel.add(btnSortir);

        ventana.add(panel);

        // === LISTENERS ===

        btnIniciarSessio.addActionListener(e -> {
            //iCtrlPresentacio.mostrarVistaIniciarSessio();
        });

        btnCrearUsuari.addActionListener(e -> {
            //iCtrlPresentacio.mostrarVistaCrearUsuari();
        });

        btnSortir.addActionListener(e -> {
            System.exit(0);
        });

    }

    public void hacerVisible(boolean b) {
        ventana.setVisible(b);
    }
}
