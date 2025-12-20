package prop.enquestes.presentacio;

import prop.enquestes.excepcions.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Map;

/**
 * Diàleg per a la consulta de respostes i l'anàlisi de dades mitjançant
 * Clustering.
 */
public class VistaConsultarRespostes extends JDialog {

    private CtrlPresentacio ctrl;
    private int idUsuari;

    private JComboBox<String> comboEnquestes;
    private List<Integer> idsEnquestes;

    // Respostes
    private DefaultListModel<String> respostesModel;
    private JList<String> listRespostes;

    // Clustering
    private JTextField fieldK;
    private JTextField fieldIter;
    private JComboBox<String> comboAlgorisme;
    private JLabel lblSilhouette;
    private JTable tableClusters;
    private DefaultTableModel modelClusters;

    public VistaConsultarRespostes(CtrlPresentacio ctrl, int idUsuari) {
        super((Frame) null, "Consultes i Clustering", true);
        this.ctrl = ctrl;
        this.idUsuari = idUsuari;

        UIHelper.configureDialog(this, "Consultes i Clustering");
        setSize(900, 700);
        setLocationRelativeTo(null);

        initUI();
        carregarEnquestes();
    }

    private void initUI() {
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBackground(UIHelper.COLOR_BACKGROUND);
        content.setBorder(UIHelper.PADDING_MAIN);
        setContentPane(content);

        // TOP: Selector
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(UIHelper.COLOR_BACKGROUND);
        top.add(new JLabel("Enquesta:"));
        comboEnquestes = new JComboBox<>();
        comboEnquestes.setPreferredSize(new Dimension(300, 25));
        comboEnquestes.addActionListener(e -> mostrarRespostes());
        top.add(comboEnquestes);
        content.add(top, BorderLayout.NORTH);

        // CENTER: Split (Respostes | Clustering)
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setResizeWeight(0.4);

        // -- Upper: Respostes --
        JPanel pResp = new JPanel(new BorderLayout());
        pResp.setBorder(BorderFactory.createTitledBorder("Respostes Rebudes"));
        respostesModel = new DefaultListModel<>();
        listRespostes = new JList<>(respostesModel);
        listRespostes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        pResp.add(new JScrollPane(listRespostes), BorderLayout.CENTER);

        split.setTopComponent(pResp);

        // -- Lower: Clustering --
        JPanel pCluster = new JPanel(new BorderLayout(10, 10));
        pCluster.setBorder(BorderFactory.createTitledBorder("Anàlisi de Dades (Clustering)"));

        // Params
        JPanel pParams = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        pParams.add(new JLabel("Clusters (k):"));
        fieldK = new JTextField("2", 3);
        pParams.add(fieldK);

        pParams.add(new JLabel("Max Iter:"));
        fieldIter = new JTextField("50", 4);
        pParams.add(fieldIter);

        pParams.add(new JLabel("Algorisme:"));
        comboAlgorisme = new JComboBox<>(new String[] { "KMeans", "kmeans++", "KMedoids" });
        pParams.add(comboAlgorisme);

        JButton btnRun = UIHelper.createButton("Executar Clustering", e -> aplicarClustering());
        pParams.add(btnRun);

        pCluster.add(pParams, BorderLayout.NORTH);

        // Results
        JPanel pResCluster = new JPanel(new BorderLayout());
        lblSilhouette = new JLabel("Silhouette Coefficient: -");
        lblSilhouette.setFont(UIHelper.FONT_SUBTITLE);
        lblSilhouette.setBorder(new EmptyBorder(0, 5, 5, 5));
        pResCluster.add(lblSilhouette, BorderLayout.NORTH);

        modelClusters = new DefaultTableModel(new String[] { "ID Usuari", "Cluster Assignat" }, 0);
        tableClusters = new JTable(modelClusters);
        pResCluster.add(new JScrollPane(tableClusters), BorderLayout.CENTER);

        pCluster.add(pResCluster, BorderLayout.CENTER);

        split.setBottomComponent(pCluster);
        content.add(split, BorderLayout.CENTER);

        // BOTTOM: Close
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(UIHelper.COLOR_BACKGROUND);
        bottom.add(UIHelper.createButton("Tancar", e -> dispose()));
        content.add(bottom, BorderLayout.SOUTH);
    }

    private void carregarEnquestes() {
        comboEnquestes.removeAllItems();
        idsEnquestes = new ArrayList<>();
        comboEnquestes.addItem("-- Selecciona --");
        idsEnquestes.add(-1);

        try {
            List<String> enquestasInfo = ctrl.obtenirLlistaEnquestes();
            for (String info : enquestasInfo) {
                String[] lineas = info.split("\n");
                for (String linea : lineas) {
                    if (linea.trim().startsWith("ID:")) {
                        // Parse ID
                        String part = linea.substring(3).trim();
                        // Usually format is "ID: 1 - Title"
                        String idStr = part.split("-")[0].trim();
                        try {
                            int id = Integer.parseInt(idStr);
                            comboEnquestes.addItem(linea);
                            idsEnquestes.add(id);
                        } catch (NumberFormatException ignored) {
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Error carregant enquestes: " + e.getMessage());
        }
    }

    private void mostrarRespostes() {
        int idx = comboEnquestes.getSelectedIndex();
        respostesModel.clear();
        if (idx <= 0)
            return;

        int idEnq = idsEnquestes.get(idx);
        try {
            List<String> llista = ctrl.obtenirRespostesEnquesta(idEnq);
            if (llista.isEmpty()) {
                respostesModel.addElement("No hi ha respostes.");
            } else {
                for (String s : llista)
                    respostesModel.addElement(s);
            }
        } catch (Exception ex) {
            UIHelper.showError(this, "Error obtenint respostes: " + ex.getMessage());
        }
    }

    private void aplicarClustering() {
        int idx = comboEnquestes.getSelectedIndex();
        if (idx <= 0) {
            UIHelper.showWarning(this, "Selecciona una enquesta primer.");
            return;
        }
        int idEnq = idsEnquestes.get(idx);

        try {
            int k = Integer.parseInt(fieldK.getText().trim());
            int iter = Integer.parseInt(fieldIter.getText().trim());
            if (k < 1 || iter < 1)
                throw new NumberFormatException();

            String tipus = (String) comboAlgorisme.getSelectedItem();

            var resultats = ctrl.aplicarClustering(idUsuari, idEnq, k, iter, tipus);
            Map<Integer, Integer> result = resultats.getKey();
            double coef = resultats.getValue();

            // Update UI
            lblSilhouette.setText(String.format("Silhouette Coefficient: %.4f", coef));

            modelClusters.setRowCount(0);
            for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
                modelClusters.addRow(new Object[] { entry.getKey(), "Cluster " + entry.getValue() });
            }

            UIHelper.showInfo(this, "Clustering finalitzat correctament.");

        } catch (NumberFormatException e) {
            UIHelper.showWarning(this, "Paràmetres invàlids (han de ser enters positius).");
        } catch (Exception e) {
            UIHelper.showError(this, "Error en clustering: " + e.getMessage());
        }
    }
}
