package prop.enquestes.presentacio;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * Diàleg per visualitzar diferents tipus de recomanacions i estadístiques del sistema.
 * <p>
 * Aquesta vista ofereix un menú lateral amb opcions (Enquesta, Resposta, Clustering, Consultes)
 * que actualitzen dinàmicament un panell de text central amb la informació sol·licitada.
 * </p>
 */
public class VistaConsultarRecomanacions extends JDialog {
    private CtrlPresentacio ctrl;
    private JPanel contentPane = new JPanel();
    private JButton enquesta = new JButton("Enquesta");
    private JButton resposta = new JButton("Resposta");
    private JButton clustering = new JButton("Clustering");
    private JButton consultes = new JButton("Consultes");
    private JTextArea info = new JTextArea();

    private JButton buttonCancel = new JButton("Tancar");

    /**
     * Constructor de la vista de recomanacions.
     * Configura la interfície gràfica, defineix el layout (disseny) i assigna la lògica
     * als botons per mostrar informació textual al panell central.
     *
     * @param ctrl Referència al controlador de presentació per gestionar la navegació.
     */
    public VistaConsultarRecomanacions(CtrlPresentacio ctrl) {
        super((Frame) null, "Consultar Recomanacions", true);
        this.ctrl = ctrl;

        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);
        setModal(true);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new GridLayout(4,1,5,5));
        buttonPane.add(enquesta);
        buttonPane.add(resposta);
        buttonPane.add(clustering);
        buttonPane.add(consultes);

        JScrollPane scrollPane = new JScrollPane(info);
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);

        JPanel panelCancel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelCancel.add(buttonCancel);

        contentPane.add(buttonPane, BorderLayout.WEST);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(panelCancel, BorderLayout.SOUTH);

        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCancel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        enquesta.addActionListener(e -> {
            String informacion = "Informacion sobre enquestes: \n\n";
            informacion += "- Recomanacio 1: ...";
            informacion += "- Recomanacio 2: ...";
            info.setText(informacion);
        });

        resposta.addActionListener(e -> {
            String informacion = "Información sobre Respostes:\n\n";
            informacion += "- Anàlisi de respostes...\n";
            informacion += "- Estadístiques...\n";
            info.setText(informacion);
        });

        clustering.addActionListener(e -> {
            String informacion = "Información sobre Clustering:\n\n";
            informacion += "- Grups identificats...\n";
            informacion += "- Patrons trobats...\n";
            info.setText(informacion);
        });

        consultes.addActionListener(e -> {
            String informacion = "Información sobre Consultes:\n\n";
            informacion += "- Consultes freqüents...\n";
            informacion += "- Resultats...\n";
            info.setText(informacion);
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    /**
     * Tanca la finestra de diàleg i allibera els recursos associats.
     */
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}