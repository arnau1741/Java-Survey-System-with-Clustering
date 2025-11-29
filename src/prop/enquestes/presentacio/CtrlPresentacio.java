package prop.enquestes.presentacio;
import prop.enquestes.controladors.CtrlDomini;
import prop.enquestes.controladors.CtrlDominiMantUsuari;
import prop.enquestes.domini.Usuari;
import prop.enquestes.excepcions.EnquestaNoExisteixException;
import prop.enquestes.excepcions.InvalidFormatEnquesta;

import java.util.List;

public class CtrlPresentacio {
    private CtrlDomini ctrlDomini;
    private VistaPrincipal vistaPrincipal;
    private VistaIniciarSessio vistaIniciarSessio;
    private VistaCrearUsuari vistaCrearUsuari;
    private VistaPrincipalComuna  vistaPrincipalComuna;
    private VistaConsultarPerfil vistaConsultarPerfil;
    private VistaConsultarRecomanacions vistaConsultarRecomanacions;
    private VistaAdmin vistaAdmin;
    private VistaModificarEnquesta vistaModificarEnquesta;

    public CtrlPresentacio() throws InvalidFormatEnquesta, EnquestaNoExisteixException {
        ctrlDomini = new CtrlDomini();
        vistaPrincipal = new VistaPrincipal(this);
    }

    public void inicializarPresentacio() {
        //ctrlDomini.inicializarCtrlDomini();
        vistaPrincipal.hacerVisible(true);
    }

    //Metodes que crida la vistaPrincipal, falta implementar amb la logica


    public void mostrarVistaIniciarSessio() {
        vistaIniciarSessio = new VistaIniciarSessio(this);
        vistaIniciarSessio.setVisible(true);
        vistaPrincipal.hacerVisible(false);
        //vistaPrincipal.tancar(); Faltaria implementar com es pot continuar o tornar enrera si cal, si no es tanca
    }

    public void mostrarVistaCrearUsuari() {
        vistaCrearUsuari = new VistaCrearUsuari(this);
        vistaCrearUsuari.setVisible(true);
        //vistaPrincipal.hacerVisible(false);
        //vistaCrearUsuari.tancar(); significa fer un dispose() al frame
    }

    public void mostrarVistaPrincipalComuna(int idUsuari) {
        String rol = obtenirRol(idUsuari); ////S'hauria d'obtenir el rol en forma de string
        vistaPrincipalComuna = new VistaPrincipalComuna(this, idUsuari, rol);
        vistaPrincipalComuna.hacerVisible(true);

    }

    public void mostrarConsultarPerfil() {
        vistaConsultarPerfil = new VistaConsultarPerfil(this);
        vistaConsultarPerfil.setVisible(true);
    }

    public void mostrarConsultarRecomanacions() {
        vistaConsultarRecomanacions = new VistaConsultarRecomanacions(this);
        vistaConsultarRecomanacions.setVisible(true);
    }

    public void mostrarFuncionsAvançades() {
        vistaAdmin = new VistaAdmin(this);
        vistaAdmin.setVisible(true);
    }

    public void mostrarVistaModificarEnquesta() {
        vistaModificarEnquesta = new VistaModificarEnquesta(this);
        vistaModificarEnquesta.setVisible(true);

    }

    // ======================
    // OPERACIONS
    // ======================

    public boolean iniciarSessio(int id) {
        //String nom = ctrlDomini.getNomUsuari(idUsuari);
        //String rol = ctrlDomini.getRolUsuari(idUsuari);

        //VistaMenuUsuari menu = new VistaMenuUsuari(this, nom, rol);
        //menu.setVisible(true);
        return true;
    }

    public int crearUsuari(String nom, String cont, String email) {
        return ctrlDomini.crearUsuariEnquestat(nom, cont, email);
    }

    public String obtenirRol(int id) {
        CtrlDominiMantUsuari mantUsuari = ctrlDomini.getCtrlDominiMantUsuari();
        String rol = mantUsuari.getRolUsuari(id);
        return rol;
    }

    public List<String> consultarEnquesta(int id) throws EnquestaNoExisteixException {
        List<String> Enquesta = ctrlDomini.consultarEnquesta(id);
        return Enquesta;
    }

    public List<String> consultarPerfil(int id) {
        List<String> Perfil = ctrlDomini.consultarPerfil(id);
        return Perfil;
    }

    public List<String> obtenirLlistaEnquestes() throws EnquestaNoExisteixException {
        return ctrlDomini.consultarEnquestesAmbPreguntesIRespostes();
    }

    public List<String> obtenirPreguntesEnquesta(int idEnquesta) throws EnquestaNoExisteixException {
        return ctrlDomini.consultarEnquestaAmbPreguntes(idEnquesta);
    }

    public void eliminarEnquesta(int idEnquesta) throws EnquestaNoExisteixException {
        ctrlDomini.eliminarEnquesta(0, idEnquesta);
    }

    public void modificarPreguntaEnquesta(int idEnquesta, int indexPregunta, List<String> novaPreguntaText) throws InvalidFormatEnquesta, EnquestaNoExisteixException {
        ctrlDomini.modificarPreguntaEnquesta(idEnquesta, indexPregunta, novaPreguntaText);
    }

}
