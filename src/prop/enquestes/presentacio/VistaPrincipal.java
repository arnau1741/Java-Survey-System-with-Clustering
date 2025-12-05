package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;

public class VistaPrincipal {
    private CtrlPresentacio iCtrlPresentacio;
    private JFrame ventana = new JFrame("Sistema d'Enquestes");
    private JPanel panel = new JPanel();

    private JButton btnIniciarSessio = new JButton("Iniciar Sessio");
    private JButton btnCrearUsuari = new JButton("Crear Usuari");
    private JButton btnConvidat = new JButton("Convidat");
    private JButton btnSortir = new JButton("Sortir");


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
        panel.add(btnConvidat);
        panel.add(btnSortir);

        ventana.add(panel);

        // === LISTENERS ===

        btnIniciarSessio.addActionListener(e -> {
            iCtrlPresentacio.mostrarVistaIniciarSessio();
        });

        btnCrearUsuari.addActionListener(e -> {
            iCtrlPresentacio.mostrarVistaCrearUsuari();
        });

        btnConvidat.addActionListener(e -> {
            iCtrlPresentacio.mostrarVistaConvidat();
        });

        btnSortir.addActionListener(e -> {
            System.exit(0);
        });

    }

    public void hacerVisible(boolean b) {
        ventana.setVisible(b);
    }

    public void tancar() {
        ventana.dispose();
    }
}
