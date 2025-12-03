package prop.enquestes.presentacio;
import prop.enquestes.controladors.CtrlDomini;
import prop.enquestes.controladors.CtrlDominiMantUsuari;
import prop.enquestes.domini.Usuari;
import prop.enquestes.excepcions.EnquestaNoExisteixException;
import prop.enquestes.excepcions.InvalidFormatEnquesta;
import prop.enquestes.excepcions.FileNotFound;

import java.util.List;

public class CtrlPresentacio {
    private CtrlDomini ctrlDomini;
    private VistaPrincipal vistaPrincipal;
    private VistaIniciarSessio vistaIniciarSessio;
    private VistaCrearUsuari vistaCrearUsuari;
    // afegir vista Convidat?
    private VistaAdmin vistaAdmin;
    private VistaEnquestador vistaEnquestador;
    private VistaEnquestat vistaEnquestat;
    private VistaModerador vistaModerador;
    
    private VistaConsultarPerfil vistaConsultarPerfil;
    private VistaConsultarRecomanacions vistaConsultarRecomanacions;
    private VistaModificarEnquesta vistaModificarEnquesta;
    private VistaConsultarEnquesta vistaConsultarEnquesta;
    private VistaCrearEnquesta vistaCrearEnquesta;
    private VistaImportarEnquesta vistaImportarEnquesta;
    private VistaExportarEnquesta vistaExportarEnquesta;

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
        ///Des d'aqui obtenim el rol i mostrem qualsevol de les vistes decidides
        String nomUsuari = obtenirNomUsuari(idUsuari);
        String rol = obtenirRol(idUsuari); ////S'hauria d'obtenir el rol en forma de string
        if(rol.equals("ADMIN")){
            vistaAdmin = new VistaAdmin(this, idUsuari, nomUsuari, rol);
            vistaAdmin.setVisible(true);
        }
        else if(rol.equals("MODERADOR")){
            vistaModerador = new VistaModerador(this, idUsuari, nomUsuari, rol);
            vistaModerador.setVisible(true);
        }
        else if(rol.equals("ENQUESTADOR")) {
            vistaEnquestador = new VistaEnquestador(this, idUsuari, nomUsuari, rol);
            vistaEnquestador.setVisible(true);
        }
        else {
            vistaEnquestat = new VistaEnquestat(this, idUsuari, nomUsuari, rol);
            vistaEnquestat.setVisible(true);
        }

    }

    public void mostrarConsultarPerfil() {
        vistaConsultarPerfil = new VistaConsultarPerfil(this);
        vistaConsultarPerfil.setVisible(true);
    }

    public void mostrarConsultarRecomanacions() {
        vistaConsultarRecomanacions = new VistaConsultarRecomanacions(this);
        vistaConsultarRecomanacions.setVisible(true);
    }

    public void mostrarVistaModificarEnquesta() {
        vistaModificarEnquesta = new VistaModificarEnquesta(this);
        vistaModificarEnquesta.setVisible(true);

    }

    public void mostrarConsultarEnquesta() {
        vistaConsultarEnquesta = new VistaConsultarEnquesta(this);
        vistaConsultarEnquesta.setVisible(true);
    }

    public void mostrarCrearEnquesta(int idUsuari) {
        vistaCrearEnquesta = new VistaCrearEnquesta(this, idUsuari);
        vistaCrearEnquesta.setVisible(true);
    }

    public void mostrarImportarEnquesta(int idUsuari) {
        vistaImportarEnquesta = new VistaImportarEnquesta(this, idUsuari);
        vistaImportarEnquesta.setVisible(true);
    }

    public void mostrarExportarEnquesta(int idUsuari) {
        vistaExportarEnquesta = new VistaExportarEnquesta(this, idUsuari);
        vistaExportarEnquesta.setVisible(true);
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

    public String obtenirNomUsuari(int id) {
       CtrlDominiMantUsuari mantUsuari = ctrlDomini.getCtrlDominiMantUsuari();
       Usuari us = mantUsuari.getUsuari(id);
       String nomUsuari = us.getUsuari();
       return nomUsuari;
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

    public void crearEnquesta(String titol, String descripcio, int idCreador, List<String> preguntes) throws InvalidFormatEnquesta {
        ctrlDomini.crearEnquesta(titol, descripcio, idCreador, preguntes);
    }

    public int importarEnquesta(int idUsuari, String path) throws InvalidFormatEnquesta, FileNotFound {
        return ctrlDomini.importarEnquesta(idUsuari, path);
    }

    public List<String> exportarEnquesta(int idEnquesta) throws EnquestaNoExisteixException {
        return ctrlDomini.exportarEnquesta(idEnquesta);
    }

    // funcions pel desplegable de la vistaExportarEnquesta
    public List<Integer> getIdsEnquestes() {
        return ctrlDomini.getIdsEnquestes();
    }

    public List<String> getTitolsEnquestes() {
        return ctrlDomini.getTitolsEnquestes();
    }
}
