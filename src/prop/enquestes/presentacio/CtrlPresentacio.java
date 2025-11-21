package prop.enquestes.presentacio;
import prop.enquestes.controladors.CtrlDomini;

public class CtrlPresentacio {
    private CtrlDomini ctrlDomini;
    private VistaPrincipal vistaPrincipal;

    public CtrlPresentacio() {
        ctrlDomini = new CtrlDomini();
        vistaPrincipal = new VistaPrincipal(this);
    }

    public void inicializarPresentacio() {
        //ctrlDomini.inicializarCtrlDomini();
        vistaPrincipal.hacerVisible(true);
    }

    //Metodes que crida la vistaPrincipal, falta implementar amb la logica


    public void mostrarVistaIniciarSessio() {
        VistaIniciarSessio v = new VistaIniciarSessio(this);
        v.setVisible(true);
    }

    public void mostrarVistaCrearUsuari() {
        VistaCrearUsuari v = new VistaCrearUsuari(this);
        v.setVisible(true);
    }

    // ======================
    // OPERACIONS
    // ======================

    public void iniciarSessio() {
        //String nom = ctrlDomini.getNomUsuari(idUsuari);
        //String rol = ctrlDomini.getRolUsuari(idUsuari);

        //VistaMenuUsuari menu = new VistaMenuUsuari(this, nom, rol);
        //menu.setVisible(true);
    }

    public void crearUsuari() {
        //ctrlDomini.crearUsuari(nom, rol);
    }

}
