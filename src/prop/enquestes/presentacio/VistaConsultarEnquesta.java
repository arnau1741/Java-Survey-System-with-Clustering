package prop.enquestes.presentacio;

import prop.enquestes.excepcions.EnquestaNoExisteixException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class VistaConsultarEnquesta extends JDialog {
    private CtrlPresentacio ctrlPresentacio;
    private JPanel contentPane = new JPanel();
    private JTextField campID = new JTextField(10);
    private JTextArea resultat = new JTextArea(20,20);
    private JButton buttonOK;
    private JButton buttonCancel;

    public VistaConsultarEnquesta(CtrlPresentacio ctrlPresentacio) {
        super((Frame) null, "Consultar Enquesta", null);
        this.ctrlPresentacio = ctrlPresentacio;
        setSize(450,350);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3,1,10,10));


        contentPane.setLayout(new BorderLayout(10, 10));
        JPanel top = new JPanel(new FlowLayout());
        top.add(new JLabel("ID de l'enquesta:"));
        top.add(campID);
        contentPane.add(top, BorderLayout.NORTH);

        resultat.setEditable(false);
        JScrollPane scroll = new JScrollPane(resultat);
        contentPane.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(buttonOK);
        bottom.add(buttonCancel);
        contentPane.add(bottom, BorderLayout.SOUTH);

        add(contentPane);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (EnquestaNoExisteixException ex) {
                    throw new RuntimeException(ex);
                }
            }
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

    private void onOK() throws EnquestaNoExisteixException {
        String txt = campID.getText().trim();
        if (txt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Has d'introduir un ID.");
            return;
        }

        int id;
        try { id = Integer.parseInt(txt); }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Introdueix un número vàlid.");
            return;
        }

        List<String> info = ctrlPresentacio.consultarEnquesta(id);

        StringBuilder sb = new StringBuilder();
        for (String s : info) sb.append(s).append("\n");

        resultat.setText(sb.toString());
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
