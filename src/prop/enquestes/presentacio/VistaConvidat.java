package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;

/**
 * Vista principal per a l'usuari convidat.
 * <p>
 * Aquesta classe representa el menú limitat que veu un usuari que accedeix al sistema
 * sense registrar-se (o com a convidat). La seva única funcionalitat principal és
 * respondre enquestes públiques.
 * </p>
 */
public class VistaConvidat extends JFrame {
    private CtrlPresentacio ctrl;
    private int idUsuari;

    private JPanel contentPanel = new JPanel();
    private JButton buttonRespondreEnquesta = new JButton("Respondre Enquesta");
    private JButton buttonSortir = new JButton("Sortir");

    /**
     * Constructor de la vista de convidat.
     * Inicialitza la finestra i configura els elements gràfics per a la interacció.
     *
     * @param ctrl Referència al controlador de presentació.
     * @param idUsuari Identificador assignat a l'usuari convidat (habitualment un identificador temporal o específic).
     */
    public VistaConvidat(CtrlPresentacio ctrl, int idUsuari) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;

        initComponents();
        setupListeners();     // Configura els listeners dels botons
    }

    /**
     * Inicialitza i configura els components visuals de la interfície.
     * Defineix el títol, la mida i la distribució dels botons al panell.
     */
    private void initComponents() {
        setTitle("Menú convidat");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPanel.setLayout(new BorderLayout(10, 10));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));

        panelBotones.add(buttonRespondreEnquesta);


        JPanel panelSortir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSortir.add(buttonSortir);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panelSortir.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel.add(panelBotones, BorderLayout.CENTER);
        contentPanel.add(panelSortir, BorderLayout.SOUTH);
        add(contentPanel);
        setContentPane(contentPanel);

    }

    /**
     * Configura els listeners (escoltadors) per als botons.
     * Assigna l'acció de respondre enquestes al botó corresponent, delegant la tasca al controlador.
     */
    private void setupListeners() {
        buttonRespondreEnquesta.addActionListener(e -> {
            ctrl.mostrarRespondreEnquesta(idUsuari);
        });
        buttonSortir.addActionListener(e -> {
            dispose();
            ctrl.inicializarPresentacio();
        });
    }
}