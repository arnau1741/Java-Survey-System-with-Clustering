package prop.enquestes.presentacio;
import javax.swing.SwingUtilities;
import prop.enquestes.controladors.CtrlDomini;

public class MainPresentacio {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CtrlPresentacio ctrlPresentacio = new CtrlPresentacio();
                ctrlPresentacio.inicializarPresentacio();
            }
        });
    }
}
