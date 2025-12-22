package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.*;

/**
 * Vista de gestió avançada d'enquestes per a l'administrador.
 * <p>
 * Aquesta finestra actua com un submenú dinàmic. Depenent del context indicat
 * (si es consulten enquestes "Realitzades" o "Administrades"), la vista
 * renderitza
 * un conjunt diferent de botons i funcionalitats, com ara modificar, donar
 * poders,
 * exportar o importar respostes.
 * </p>
 */
public class VistaEnquestesExtresAdmin extends JFrame {

    private CtrlPresentacio ctrl;
    private int idUsuari;
    private String nomEnquesta;
    private JPanel contentPanel = new JPanel();
    private JButton exportarEnquestaButton = new JButton("Exportar enquesta");
    private JButton exportarRespostesButton = new JButton("Exportar respostes");
    private JButton respondreEnquestaButton = new JButton("Respondre enquesta");
    private JButton consultarRespostesButton = new JButton("Consultar resposta");
    private JButton importarRespostesButton = new JButton("Importar resposta");
    private JButton modificarEnquestaButton = new JButton("Modificar enquesta");
    private JButton donarPodersButton = new JButton("Donar poders");
    private JButton sortirButton = new JButton("Sortir");

    /**
     * Constructor de la vista d'enquestes extres.
     * Configura la finestra segons el tipus d'operativa seleccionada al menú
     * principal.
     *
     * @param ctrl        Referència al controlador de presentació.
     * @param nomEnquesta Cadena de text que determina el mode de visualització (ex:
     *                    "Realitzada").
     * @param idUsuari    Identificador de l'administrador actual.
     */
    public VistaEnquestesExtresAdmin(CtrlPresentacio ctrl, String nomEnquesta, int idUsuari) {
        this.ctrl = ctrl;
        this.nomEnquesta = nomEnquesta;
        this.idUsuari = idUsuari;
        initComponents();
        setupListeners(); // Configura els listeners dels botons
    }

    /**
     * Inicialitza i configura els components visuals.
     * Aquest mètode conté lògica condicional:
     *
     * <ul>
     * <li>Si {@code nomEnquesta} és "Realitzada": Mostra opcions d'exportació i
     * consulta bàsica.</li>
     * <li>En cas contrari (Administrada): Afegeix opcions de gestió avançada com
     * importar, modificar i donar poders.</li>
     * </ul>
     */
    public void initComponents() {
        setTitle("Menú enquesta" + nomEnquesta);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPanel.setLayout(new BorderLayout(10, 10));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(0, 1, 10, 10));

        if (nomEnquesta.equals("Realitzada")) {
            panelBotones.add(exportarEnquestaButton);
            panelBotones.add(exportarRespostesButton);
            panelBotones.add(consultarRespostesButton);
        } else {
            panelBotones.add(exportarEnquestaButton);
            panelBotones.add(exportarRespostesButton);
            panelBotones.add(importarRespostesButton);
            panelBotones.add(modificarEnquestaButton);
            panelBotones.add(consultarRespostesButton);
            panelBotones.add(donarPodersButton);
        }

        JPanel panelSortir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSortir.add(sortirButton);

        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panelSortir.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel.add(panelBotones, BorderLayout.CENTER);
        contentPanel.add(panelSortir, BorderLayout.SOUTH);

        add(contentPanel);

        setContentPane(contentPanel);

        sortirButton.addActionListener(e -> {
            dispose();
        });

    }

    /**
     * Assigna els escoltadors (listeners) als botons disponibles.
     * Vincula cada botó amb la funció corresponent del controlador de presentació.
     */
    public void setupListeners() {
        modificarEnquestaButton.addActionListener(e -> {
            ctrl.mostrarVistaModificarEnquesta(idUsuari);
        });
        importarRespostesButton.addActionListener(e -> {
            ctrl.mostrarImportarRespostes(idUsuari);
        });
        exportarRespostesButton.addActionListener(e -> {
            if ("Realitzada".equals(nomEnquesta)) {
                ctrl.mostrarExportarRespostes(idUsuari, false);
            } else {
                ctrl.mostrarExportarRespostes(idUsuari, true);
            }
        });
        exportarEnquestaButton.addActionListener(e -> {
            if ("Realitzada".equals(nomEnquesta)) {
                ctrl.mostrarExportarEnquesta(idUsuari, false);
            } else {
                ctrl.mostrarExportarEnquesta(idUsuari, true);
            }
        });
        donarPodersButton.addActionListener(e -> {
            ctrl.mostrarDonarPoders(idUsuari);
        });
        consultarRespostesButton.addActionListener(e -> {
            if ("Realitzada".equals(nomEnquesta)) {
                ctrl.mostrarConsultarRespostesPropies(idUsuari);
            } else {
                ctrl.mostrarConsultarRespostes(idUsuari);
            }
        });

    }
}