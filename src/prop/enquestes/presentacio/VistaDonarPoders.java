package prop.enquestes.presentacio;

import prop.enquestes.excepcions.EnquestaNoExisteixException;
import prop.enquestes.excepcions.InvalidFormatEnquesta;
import prop.enquestes.excepcions.UsuariNoValid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Diàleg per assignar permisos (poders) sobre una enquesta a altres usuaris.
 * <p>
 * Aquesta vista permet a un usuari administrador seleccionar una enquesta que gestiona,
 * indicar el nom d'un usuari objectiu i assignar-li un rol específic (Enquestador o Administrador)
 * sobre aquella enquesta concreta.
 * </p>
 */
public class VistaDonarPoders extends JDialog {

    private CtrlPresentacio ctrl;
    private int idExecutor;

    private JPanel contentPane = new JPanel();
    private JComboBox<String> comboEnquestes = new JComboBox<>();
    private JTextField fieldNomTarget = new JTextField();
    private JComboBox<String> comboRol = new JComboBox<>();
    private JButton buttonOK = new JButton("Assignar");
    private JButton buttonCancel = new JButton("Cancel·lar");

    /** Llista auxiliar per emmagatzemar els IDs reals de les enquestes llistades al ComboBox. */
    private java.util.List<Integer> idsEnquestes;

    /**
     * Constructor de la vista d'assignació de poders.
     * Inicialitza la finestra modal, carrega les enquestes on l'usuari té permisos d'administració
     * i configura els components gràfics.
     *
     * @param ctrl Referència al controlador de presentació.
     * @param idExecutor ID de l'usuari que està executant l'acció (ha de ser administrador de l'enquesta).
     */
    public VistaDonarPoders(CtrlPresentacio ctrl, int idExecutor) {
        this.ctrl = ctrl;
        this.idExecutor = idExecutor;

        setTitle("Assignar Poders");
        setModal(true);
        setSize(400, 250);
        setLocationRelativeTo(null);

        initComponents();
        carregarEnquestes();
        initActions();
    }

    /**
     * Inicialitza i distribueix els components gràfics dins del diàleg.
     * Utilitza un GridLayout per al formulari (Enquesta, Usuari Target, Rol)
     * i un FlowLayout per als botons d'acció.
     */
    private void initComponents() {
        contentPane.setLayout(new BorderLayout(10, 10));
        JPanel center = new JPanel(new GridLayout(4, 2, 8, 8));

        center.add(new JLabel("Enquesta:"));
        comboEnquestes = new JComboBox<>();
        center.add(comboEnquestes);

        center.add(new JLabel("Nom usuari target:"));
        fieldNomTarget = new JTextField();
        center.add(fieldNomTarget);

        center.add(new JLabel("Rol a assignar:"));
        comboRol = new JComboBox<>(new String[]{"Enquestador", "Administrador"});
        center.add(comboRol);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(buttonOK);
        bottom.add(buttonCancel);

        contentPane.add(center, BorderLayout.CENTER);
        contentPane.add(bottom, BorderLayout.SOUTH);

        add(contentPane);
        setContentPane(contentPane);
    }

    /**
     * Carrega al desplegable les enquestes que l'usuari actual administra.
     * Analitza la informació textual rebuda del controlador per extreure l'ID de l'enquesta
     * i mantenir la referència a la llista {@code idsEnquestes}.
     */
    private void carregarEnquestes() {
        comboEnquestes.removeAllItems();
        idsEnquestes = new ArrayList<>();
        comboEnquestes.addItem("-- Selecciona --");
        idsEnquestes.add(-1);
        String rol = ctrl.obtenirRolUsuari(idExecutor);

        try {
            List<String> enquestasInfo = null;
            if(rol.equals("MODERADOR")) {
                enquestasInfo = ctrl.obtenirLlistaEnquestes();
            }
            else {
                enquestasInfo = ctrl.obtenirEnquestesAdministrades(idExecutor);
            }
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
     * Assigna els escoltadors (listeners) als botons d'Acceptar i Cancel·lar.
     */
    private void initActions() {
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> dispose());
    }

    /**
     * Executa l'acció d'assignar poders.
     * Valida les entrades (enquesta seleccionada, nom d'usuari) i crida al mètode
     * corresponent del controlador segons el rol escollit.
     * Gestiona les excepcions si l'usuari no existeix o l'enquesta té problemes.
     */
    private void onOK() {
        int idx = comboEnquestes.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Has de seleccionar una enquesta.");
            return;
        }

        int idEnquesta = idsEnquestes.get(idx);
        String nomTarget = fieldNomTarget.getText().trim();
        String rol = (String) comboRol.getSelectedItem();

        try {
            // Nota: Encara que el bloc if/else fa la mateixa crida, es manté per claredat
            // o per si en el futur es diferencien les crides al controlador.
            if (rol.equals("ENQUESTADOR")) {
                ctrl.donarPoders(idExecutor, idEnquesta, nomTarget, rol);
            } else {
                ctrl.donarPoders(idExecutor, idEnquesta, nomTarget, rol);
            }
            JOptionPane.showMessageDialog(this,
                    "Poder assignat correctament!",
                    "Èxit",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (InvalidFormatEnquesta ex) {
            JOptionPane.showMessageDialog(this,
                    "Error del format de l'enquesta: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (UsuariNoValid ex) {
            JOptionPane.showMessageDialog(this,
                    "Error de l'usuari: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}