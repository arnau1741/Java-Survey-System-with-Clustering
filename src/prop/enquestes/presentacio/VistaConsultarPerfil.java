package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.List;

public class VistaConsultarPerfil extends JDialog {
    private CtrlPresentacio ctrl;
    private JPanel contentPane = new JPanel();
    private JComboBox<String> comboUsuaris = new JComboBox<>();
    private JTextArea perfil = new JTextArea();
    private JButton buttonOK = new JButton("Consultar");
    private JButton buttonCancel = new JButton("Cancel·lar");
    private JLabel labelImagen = new JLabel();

    public VistaConsultarPerfil(CtrlPresentacio ctrl) {
        super((Frame) null, "Consultar Perfil", true);
        this.ctrl = ctrl;

        setSize(550, 350);
        setLocationRelativeTo(null);
        contentPane.setLayout(new BorderLayout(10, 10));

        // PANEL SUPERIOR - Fijo en su posición
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Usuaris:"));
        // Limitar el ancho del ComboBox para que no empuje todo
        comboUsuaris.setPreferredSize(new Dimension(200, 25));
        comboUsuaris.setMaximumSize(new Dimension(200, 25));
        top.add(comboUsuaris);
        contentPane.add(top, BorderLayout.NORTH);

        carregarUsuaris();

        // PANEL CENTRAL - Usar GridBagLayout que es más estable
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();

        // Panel de la imagen (IZQUIERDA) - tamaño fijo
        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.setPreferredSize(new Dimension(120, 200));

        crearAvatar("-");
        labelImagen.setHorizontalAlignment(SwingConstants.CENTER);
        panelImagen.add(labelImagen, BorderLayout.CENTER);

        JLabel etiquetaImagen = new JLabel("Avatar", SwingConstants.CENTER);
        panelImagen.add(etiquetaImagen, BorderLayout.SOUTH);

        // Configurar posición imagen
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // No se expande horizontalmente
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST; // Fijar a la izquierda
        gbc.insets = new Insets(0, 0, 0, 15); // Margen derecho
        centerPanel.add(panelImagen, gbc);

        // Panel del perfil (DERECHA) - se expande
        JPanel panelPerfil = new JPanel(new BorderLayout());
        perfil.setEditable(false);
        perfil.setLineWrap(true);
        perfil.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(perfil);
        panelPerfil.add(scroll, BorderLayout.CENTER);

        // Configurar posición perfil
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1; // Se expande para ocupar el espacio restante
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(panelPerfil, gbc);

        contentPane.add(centerPanel, BorderLayout.CENTER);

        // BOTONES INFERIORES
        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(buttonOK);
        bottom.add(buttonCancel);
        contentPane.add(bottom, BorderLayout.SOUTH);

        add(contentPane);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // LISTENERS
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // Actualitzar imatge
        comboUsuaris.addActionListener(e -> {
            String seleccionado = (String) comboUsuaris.getSelectedItem();
            if (seleccionado.equals("-- Selecciona un usuari --")) {
                actualizarAvatar("-");
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    // CARREGAR USUARIS
    private void carregarUsuaris() {
        comboUsuaris.removeAllItems();
        comboUsuaris.addItem("-- Selecciona un usuari --");

        List<String> usuaris = ctrl.obtenirUsuaris();
        for (String nom : usuaris) {
            comboUsuaris.addItem(nom);
        }
    }

    // CONSULTAR PERFIL
    private void onOK() {
        String txt = (String) comboUsuaris.getSelectedItem();
        if (txt == null || txt.equals("-- Selecciona un usuari --")) {
            JOptionPane.showMessageDialog(this, "Has de seleccionar un usuari.");
            return;
        }

        int id = ctrl.getIdUsuariPerNom(txt);
        if (id == -1) {
            JOptionPane.showMessageDialog(this, "Usuari no trobat.");
            return;
        }

        List<String> dades = ctrl.consultarPerfil(id);
        StringBuilder sb = new StringBuilder();
        for (String s : dades) sb.append(s).append("\n");
        perfil.setText(sb.toString());
        actualizarAvatar(txt);
    }

    private void onCancel() {
        dispose();
    }

    // Métodos para la imagen
    private void crearAvatar(String inicial) {
        int size = 100;
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
                size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = img.createGraphics();

        // Círculo azul
        g2d.setColor(new Color(0, 120, 212));
        g2d.fillOval(0, 0, size, size);

        // Texto blanco en el centro
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));

        // Centrar la letra
        FontMetrics fm = g2d.getFontMetrics();
        int x = (size - fm.stringWidth(inicial)) / 2;
        int y = (size + fm.getAscent()) / 2 - 5;

        g2d.drawString(inicial, x, y);

        g2d.dispose();

        labelImagen.setIcon(new ImageIcon(img));
        labelImagen.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void actualizarAvatar(String nombreUsuario) {
        // Obtener primera letra del nombre
        String inicial = nombreUsuario.substring(0, 1).toUpperCase();
        crearAvatar(inicial);
    }
}