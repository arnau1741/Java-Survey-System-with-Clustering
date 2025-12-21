package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;

/**
 * Vista principal de l'aplicació (Punt d'entrada gràfic).
 * <p>
 * Aquesta finestra es mostra a l'inici de l'execució i ofereix les opcions bàsiques d'accés:
 * iniciar sessió, registrar un nou usuari o accedir com a convidat. Actua com a menú d'arrel
 * abans que l'usuari s'autentiqui.
 * </p>
 */
public class VistaPrincipal {
    private CtrlPresentacio iCtrlPresentacio;
    private JFrame ventana = new JFrame("Sistema d'Enquestes");
    private JPanel panel = new JPanel();

    private JButton btnIniciarSessio = new JButton("Iniciar Sessió");
    private JButton btnCrearUsuari = new JButton("Crear Usuari");
    private JButton btnConvidat = new JButton("Convidat");
    private JButton btnSortir = new JButton("Sortir");


    /**
     * Constructor de la Vista Principal.
     * Inicialitza la referència al controlador i construeix la interfície gràfica.
     *
     * @param ctrlPre Referència al controlador de presentació per gestionar la navegació.
     */
    public VistaPrincipal(CtrlPresentacio ctrlPre) {
        iCtrlPresentacio = ctrlPre;
        inicializarComponentes();
    }

    /**
     * Configura els components visuals de la finestra.
     * Defineix la mida, la disposició (GridLayout) i afegeix els botons.
     * També assigna els escoltadors (listeners) per redirigir a les vistes de
     * Login, Registre o Convidat segons l'acció de l'usuari.
     */
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
            iCtrlPresentacio.actualizaDades();
        });

    }

    /**
     * Modifica la visibilitat de la finestra principal.
     *
     * @param b True per fer-la visible, False per amagar-la.
     */
    public void hacerVisible(boolean b) {
        ventana.setVisible(b);
    }

    /**
     * Tanca la finestra i allibera els recursos gràfics associats.
     */
    public void tancar() {
        ventana.dispose();
    }
}