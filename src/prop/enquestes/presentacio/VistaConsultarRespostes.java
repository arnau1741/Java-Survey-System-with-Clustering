package prop.enquestes.presentacio;

import prop.enquestes.excepcions.EnquestaNoExisteixException;
import prop.enquestes.excepcions.InvalidFormatEnquesta;
import prop.enquestes.excepcions.KmeansExcepcio;
import prop.enquestes.excepcions.UsuariNoValid;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Diàleg per a la consulta de respostes i l'anàlisi de dades mitjançant Clustering.
 * <p>
 * Aquesta vista permet seleccionar una enquesta per visualitzar totes les respostes rebudes.
 * A més, ofereix la funcionalitat d'executar l'algorisme K-Means (Clustering) sobre aquestes
 * respostes, permetent a l'usuari configurar els paràmetres 'k' (nombre de clústers) i
 * 'maxIter' (iteracions màximes).
 * </p>
 */
public class VistaConsultarRespostes extends JDialog {

    private CtrlPresentacio ctrl;
    private int idUsuari;

    private JPanel contentPane = new JPanel();

    private JComboBox<String> comboEnquestes = new JComboBox<>();
    private JTextArea areaRespostes = new  JTextArea();
    private JTextArea areaClustering = new  JTextArea();

    private JTextField fieldK = new  JTextField();
    private JTextField fieldIter = new  JTextField();
    private JComboBox<String> comboKmeans = new JComboBox<>();

    private JButton btnAplicarCluster = new JButton("Aplicar Clustering");
    private JButton btnTancar = new JButton("Tancar");

    /** Llista auxiliar per mapejar l'índex del ComboBox amb l'ID real de l'enquesta. */
    private List<Integer> idsEnquestes;

    /**
     * Constructor de la vista de consulta de respostes.
     * Inicialitza la finestra, carrega les enquestes disponibles i configura els escoltadors.
     *
     * @param ctrl Referència al controlador de presentació.
     * @param idUsuari Identificador de l'usuari actual (necessari per a permisos d'execució).
     */
    public VistaConsultarRespostes(CtrlPresentacio ctrl, int idUsuari) {
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;

        setTitle("Consultes i Clustering");
        setSize(700, 550);
        setModal(true);
        setLocationRelativeTo(null);

        initComponents();
        carregarEnquestes();
        initActions();
    }

    /**
     * Inicialitza els components gràfics de la interfície.
     * Divideix la pantalla en dues àrees (respostes i clustering) mitjançant un JSplitPane
     * i afegeix els controls de paràmetres a la part inferior.
     */
    private void initComponents() {
        contentPane.setLayout(new BorderLayout(10, 10));
        JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        north.add(new JLabel("Enquesta: "));
        comboEnquestes = new JComboBox<>();
        comboEnquestes.setPreferredSize(new Dimension(300, 25));
        north.add(comboEnquestes);

        contentPane.add(north, BorderLayout.NORTH);

        areaRespostes = new JTextArea();
        areaRespostes.setEditable(false);
        areaRespostes.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollResp = new JScrollPane(areaRespostes);
        scrollResp.setBorder(BorderFactory.createTitledBorder("Respostes"));

        areaClustering = new JTextArea();
        areaClustering.setEditable(false);
        areaClustering.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollCluster = new JScrollPane(areaClustering);
        scrollCluster.setBorder(BorderFactory.createTitledBorder("Resultat Clustering"));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollResp, scrollCluster);
        split.setResizeWeight(0.6);
        contentPane.add(split, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        south.add(new JLabel("k:"));
        fieldK = new JTextField("2", 5);
        south.add(fieldK);

        south.add(new JLabel("maxIter:"));
        fieldIter = new JTextField("50", 5);
        south.add(fieldIter);

        south.add(new JLabel("Algorime a usar:"));
        comboKmeans = new JComboBox<>(new String[] {
                "KMeans",
                "kmeans++",
                "KMedoids"
        });

        south.add(comboKmeans);
        south.add(btnAplicarCluster);
        south.add(btnTancar);

        contentPane.add(south, BorderLayout.SOUTH);

        add(contentPane);

        setContentPane(contentPane);
    }

    /**
     * Carrega les enquestes disponibles al desplegable.
     * Analitza les cadenes de text rebudes del controlador per extreure l'ID numèric
     * i l'emmagatzema a la llista auxiliar {@code idsEnquestes} per a la seva posterior referència.
     */
    private void carregarEnquestes() {
        comboEnquestes.removeAllItems();
        idsEnquestes = new ArrayList<>();
        comboEnquestes.addItem("-- Selecciona --");
        idsEnquestes.add(-1);

        try {
            List<String> enquestasInfo = ctrl.obtenirLlistaEnquestes();
            for (String info : enquestasInfo) {
                // Buscamos la línea exacta que empieza por "ID:"
                String[] lineas = info.split("\n");
                for (String linea : lineas) {
                    linea = linea.trim();
                    if (linea.startsWith("ID:")) {
                        String part = linea.substring(3).trim();
                        String idStr = part.split("-")[0].trim();
                        int id = Integer.parseInt(idStr);
                        comboEnquestes.addItem(linea); // SOLO esta línea
                        idsEnquestes.add(id);
                        break; // Muy importante
                    }
                }
            }
        } catch (EnquestaNoExisteixException e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Assigna les accions als components (botons i desplegable).
     */
    private void initActions() {
        comboEnquestes.addActionListener(e -> mostrarRespostes());

        btnAplicarCluster.addActionListener(e -> aplicarClustering());

        btnTancar.addActionListener(e -> dispose());
    }

    /**
     * Mostra les respostes de l'enquesta seleccionada.
     * S'executa automàticament en canviar la selecció del ComboBox.
     * Recupera l'ID de la llista auxiliar i demana les dades al controlador.
     */
    private void mostrarRespostes() {
        int idx = comboEnquestes.getSelectedIndex();
        if (idx < 0) return;

        int idEnq = idsEnquestes.get(idx);

        try {
            List<String> llista = ctrl.obtenirRespostesEnquesta(idEnq);
            StringBuilder sb = new StringBuilder();
            for (String s : llista) sb.append(s).append("\n");

            areaRespostes.setText(sb.toString());

        } catch (Exception ex) {
            areaRespostes.setText("Error carregant respostes.");
        }
    }

    /**
     * Executa l'algorisme de Clustering amb els paràmetres introduïts per l'usuari.
     * Llegeix els valors dels camps de text (K i iteracions), crida al controlador
     * i mostra el resultat (associació Usuari -> Grup) a l'àrea inferior.
     * Gestiona possibles errors de format o lògica de negoci.
     */
    private void aplicarClustering() {
        int idx = comboEnquestes.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una enquesta.");
            return;
        }

        int idEnq = idsEnquestes.get(idx);

        int k = Integer.parseInt(fieldK.getText().trim());
        int iter = Integer.parseInt(fieldIter.getText().trim());
        String tipus = (String) comboKmeans.getSelectedItem();

        try {
            var resultats = ctrl.aplicarClustering(idUsuari, idEnq, k, iter, tipus);
			Map<Integer, Integer> result = resultats.getKey();
			double coefSilhouete = resultats.getValue();

            StringBuilder sb = new StringBuilder();

            sb.append("Resultat clustering (clúster id = {id usuaris}):\n\n");
            sb.append("Algorisme: ").append(tipus).append("\n\n");

            for (var entry : result.entrySet()) {
                sb.append("Usuari ").append(entry.getKey())
                        .append(" → Grup ").append(entry.getValue()).append("\n");
            }

            sb.append("Resultat clustering (usuari -> clúster):\n\n");
            sb.append("Algorisme: ").append(tipus).append("\n\n");
            for (var entry : result.entrySet()) {
                sb.append("Usuari ").append(entry.getKey())
                        .append(" → Grup ").append(entry.getValue()).append("\n");
            }

            areaClustering.setText(sb.toString());

        } catch (KmeansExcepcio ex) {
            JOptionPane.showMessageDialog(this, "Error en clustering: " + ex.getMessage());
        } catch (EnquestaNoExisteixException ex) {
            JOptionPane.showMessageDialog(this, "Error de l'enquesta: " + ex.getMessage());
        } catch (InvalidFormatEnquesta ex) {
            JOptionPane.showMessageDialog(this, "Error del format: " + ex.getMessage());
        } catch (UsuariNoValid ex) {
            JOptionPane.showMessageDialog(this, "Error de l'usuari: " + ex.getMessage());
        }
    }
}
