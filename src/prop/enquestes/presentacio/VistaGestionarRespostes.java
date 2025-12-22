package prop.enquestes.presentacio;

import prop.enquestes.excepcions.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VistaGestionarRespostes extends JDialog {
    private CtrlPresentacio ctrl;
    private int idEnquesta;
    private int idUsuariActual; // Admin/Owner ID

    private DefaultListModel<Integer> listModelUsuaris;
    private JList<Integer> listUsuaris;

    private DefaultListModel<String> listModelRespostes;
    private JList<String> listRespostes;

    public VistaGestionarRespostes(CtrlPresentacio ctrl, int idUsuariActual, int idEnquesta) {
        super((Frame) null, "Gestionar Respostes", true);
        this.ctrl = ctrl;
        this.idUsuariActual = idUsuariActual;
        this.idEnquesta = idEnquesta;

        UIHelper.configureDialog(this, "Gestionar Respostes - Enquesta " + idEnquesta);
        setSize(800, 600);
        setLocationRelativeTo(null);

        initUI();
        carregarUsuaris();
    }

    private void initUI() {
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBackground(UIHelper.COLOR_BACKGROUND);
        content.setBorder(UIHelper.PADDING_MAIN);
        setContentPane(content);

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);

        // Left: Users List
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Usuaris que han respost"));
        listModelUsuaris = new DefaultListModel<>();
        listUsuaris = new JList<>(listModelUsuaris);
        listUsuaris.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listUsuaris.addListSelectionListener(e -> onSelectUsuari());
        leftPanel.add(new JScrollPane(listUsuaris), BorderLayout.CENTER);

        JButton btnEliminar = UIHelper.createButton("Eliminar Respostes Usuari", e -> eliminarRespostes());
        btnEliminar.setForeground(Color.RED);
        leftPanel.add(btnEliminar, BorderLayout.SOUTH);

        splitPane.setLeftComponent(leftPanel);

        // Right: Responses List
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Respostes de l'Usuari"));
        listModelRespostes = new DefaultListModel<>();
        listRespostes = new JList<>(listModelRespostes);
        rightPanel.add(new JScrollPane(listRespostes), BorderLayout.CENTER);

        JButton btnModificar = UIHelper.createButton("Modificar Resposta Seleccionada", e -> modificarResposta());
        rightPanel.add(btnModificar, BorderLayout.SOUTH);

        splitPane.setRightComponent(rightPanel);

        content.add(splitPane, BorderLayout.CENTER);

        // Bottom: Close
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(UIHelper.createButton("Tancar", e -> dispose()));
        content.add(bottom, BorderLayout.SOUTH);
    }

    private void carregarUsuaris() {
        listModelUsuaris.clear();
        listModelRespostes.clear();
        try {
            List<Integer> usuaris = ctrl.obtenirUsuarisQueHanRespost(idEnquesta);
            for (Integer id : usuaris) {
                listModelUsuaris.addElement(id);
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Error carregant usuaris: " + e.getMessage());
        }
    }

    private void onSelectUsuari() {
        Integer idUser = listUsuaris.getSelectedValue();
        listModelRespostes.clear();
        if (idUser == null)
            return;

        try {
            List<String> respostes = ctrl.obtenirRespostesUsuari(idEnquesta, idUser);
            for (String s : respostes) {
                listModelRespostes.addElement(s);
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Error carregant respostes: " + e.getMessage());
        }
    }

    private void eliminarRespostes() {
        Integer idUser = listUsuaris.getSelectedValue();
        if (idUser == null) {
            UIHelper.showWarning(this, "Selecciona un usuari.");
            return;
        }
        if (UIHelper.showConfirm(this, "Segur que vols eliminar les respostes de l'usuari " + idUser + "?")) {
            try {
                ctrl.esborrarRespostaEnquesta(idUsuariActual, idEnquesta, idUser);
                UIHelper.showInfo(this, "Respostes eliminades.");
                carregarUsuaris(); // Refresh list
            } catch (Exception e) {
                UIHelper.showError(this, "Error eliminant: " + e.getMessage());
            }
        }
    }

    private void modificarResposta() {
        Integer idUser = listUsuaris.getSelectedValue();
        int idxSel = listRespostes.getSelectedIndex();
        if (idUser == null || idxSel == -1) {
            UIHelper.showWarning(this, "Selecciona una línia de resposta.");
            return;
        }

        // The list contains: "P1: Text", "R: Answer", "- - -"
        // We need to identify which question index corresponds to the selected line.
        // Pattern is 3 lines per question.
        // Index in list / 3 = Index of question.

        int idxPregunta = idxSel / 3;
        // Check if selected line is actually a response or question text.
        // Ideally we allow clicking anywhere in the block.

        // Safety check
        if (idxPregunta * 3 + 1 >= listModelRespostes.size())
            return;

        String currentVal = listModelRespostes.get(idxPregunta * 3 + 1); // "R: Answer"
        if (!currentVal.startsWith("R: ")) {
            // Maybe user clicked on question or separator. We can still infer.
            // But let's be strict or smart.
            // If user clicks P1, we assume they want to edit R1.
        }

        // Re-fetch correct line
        currentVal = listModelRespostes.get(idxPregunta * 3 + 1);
        String currentAns = currentVal.substring(3); // Remove "R: "

        String novaResposta = JOptionPane.showInputDialog(this, "Introdueix la nova resposta:", currentAns);
        if (novaResposta != null) {
            try {
                ctrl.modificarRespostaEnquesta(idUsuariActual, idEnquesta, idUser, idxPregunta, novaResposta);
                UIHelper.showInfo(this, "Resposta modificada.");
                onSelectUsuari(); // Refresh
            } catch (Exception e) {
                UIHelper.showError(this, "Error modificant: " + e.getMessage());
            }
        }
    }
}
