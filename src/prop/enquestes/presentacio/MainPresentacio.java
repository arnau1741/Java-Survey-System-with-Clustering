package prop.enquestes.presentacio;
import javax.swing.SwingUtilities;
import prop.enquestes.controladors.CtrlDomini;
import prop.enquestes.excepcions.EnquestaNoExisteixException;
import prop.enquestes.excepcions.InvalidFormatEnquesta;

public class MainPresentacio {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CtrlPresentacio ctrlPresentacio = null;
                try {
                    ctrlPresentacio = new CtrlPresentacio();
                } catch (InvalidFormatEnquesta e) {
                    throw new RuntimeException(e);
                } catch (EnquestaNoExisteixException e) {
                    throw new RuntimeException(e);
                }
                ctrlPresentacio.inicializarPresentacio();
            }
        });
    }
}
