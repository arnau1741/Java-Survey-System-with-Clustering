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

public class VistaConsultarRespostes extends JDialog {

    private CtrlPresentacio ctrl;
    private int idUsuari;

    private JPanel contentPane = new JPanel();

    private JComboBox<String> comboEnquestes;
    private JTextArea areaRespostes;
    private JTextArea areaClustering;

    private JTextField fieldK;
    private JTextField fieldIter;

    private JButton btnAplicarCluster = new JButton("Aplicar Clustering");
    private JButton btnTancar = new JButton("Tancar");

    private List<Integer> idsEnquestes;

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

        south.add(btnAplicarCluster);
        south.add(btnTancar);

        contentPane.add(south, BorderLayout.SOUTH);

        add(contentPane);

        setContentPane(contentPane);
    }

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

    private void initActions() {
        comboEnquestes.addActionListener(e -> mostrarRespostes());

        btnAplicarCluster.addActionListener(e -> aplicarClustering());

        btnTancar.addActionListener(e -> dispose());
    }

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

    private void aplicarClustering() {
        int idx = comboEnquestes.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una enquesta.");
            return;
        }

        int idEnq = idsEnquestes.get(idx);

        int k = Integer.parseInt(fieldK.getText().trim());
        int iter = Integer.parseInt(fieldIter.getText().trim());

        try {
            Map<Integer, Integer> result = ctrl.aplicarClustering(idUsuari, idEnq, k, iter);

            StringBuilder sb = new StringBuilder();
            sb.append("Resultat clustering (usuari -> clúster):\n\n");
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
