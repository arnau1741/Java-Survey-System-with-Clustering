package prop.enquestes.presentacio;
import prop.enquestes.controladors.CtrlDomini;
import prop.enquestes.controladors.CtrlDominiMantUsuari;
import prop.enquestes.domini.Usuari;
import prop.enquestes.excepcions.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CtrlPresentacio {
    private CtrlDomini ctrlDomini;
    private VistaPrincipal vistaPrincipal;
    private VistaIniciarSessio vistaIniciarSessio;
    private VistaCrearUsuari vistaCrearUsuari;

    private VistaConvidat vistaConvidat;
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
    private VistaEnquestesExtresAdmin vistaEnquestesExtresAdmin;
    private VistaRespondreEnquesta vistaRespondreEnquesta;
    private VistaExportarRespostes vistaExportarRespostes;
    private VistaImportarRespostes vistaImportarRespostes;
    private VistaDonarPoders vistaDonarPoders;
    private VistaConsultarRespostes vistaConsultarRespostes;
    private VistaVetarDesvetar vistaVetarDesvetar;

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

    public void mostrarVistaConvidat() {
        int idUsuari = -1;
        vistaConvidat = new VistaConvidat(this, idUsuari);
        vistaConvidat.setVisible(true);
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

    public void mostrarVistaModificarEnquesta(int idUsuari) {
        vistaModificarEnquesta = new VistaModificarEnquesta(this, idUsuari);
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

    public void mostrarEnquestesExtra(String nomEnquesta, int idUsuari) {
        vistaEnquestesExtresAdmin = new VistaEnquestesExtresAdmin(this, nomEnquesta, idUsuari);
        vistaEnquestesExtresAdmin.setVisible(true);
    }

    public void mostrarRespondreEnquesta(int idUsuari) {
        vistaRespondreEnquesta = new VistaRespondreEnquesta(this, idUsuari);
        vistaRespondreEnquesta.setVisible(true);
    }

    public void mostrarExportarRespostes(int idUsuari) {
        vistaExportarRespostes = new VistaExportarRespostes(this, idUsuari);
        vistaExportarRespostes.setVisible(true);
    }

    public void mostrarImportarRespostes(int idUsuari) {
        vistaImportarRespostes = new VistaImportarRespostes(this, idUsuari);
        vistaImportarRespostes.setVisible(true);
    }

    public void mostrarDonarPoders(int idUsuari) {
        vistaDonarPoders = new VistaDonarPoders(this, idUsuari);
        vistaDonarPoders.setVisible(true);
    }

    public void mostrarConsultarRespostes(int idUsuari) {
        vistaConsultarRespostes = new VistaConsultarRespostes(this, idUsuari);
        vistaConsultarRespostes.setVisible(true);
    }

    public void mostrarVetarDesvetar(int idUsuari) {
        vistaVetarDesvetar = new VistaVetarDesvetar(this, idUsuari);
        vistaVetarDesvetar.setVisible(true);
    }

    // ======================
    // OPERACIONS
    // ======================

    public int iniciarSessio(String nomUsuari, String password) throws IllegalArgumentException {
        int id = ctrlDomini.iniciarSessio(nomUsuari, password);
        return id;
    }

    public int crearUsuari(String nom, String cont, String email, String rol) throws UsuariNoValid {
        if(rol.equals("ENQUESTADOR")){
            return ctrlDomini.crearUsuariEnquestador(nom,cont,email);
        }
        else return ctrlDomini.crearUsuariEnquestat(nom,cont,email);
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

    public List<String> obtenirEnquestesAdministrades(int idUsuari) throws EnquestaNoExisteixException {
        return ctrlDomini.obtenirEnquestesAdministrades(idUsuari);
    }

    public List<String> obtenirPreguntesEnquesta(int idEnquesta) throws EnquestaNoExisteixException {
        return ctrlDomini.consultarEnquestaAmbPreguntes(idEnquesta);
    }

    public List<String> obtenirPreguntes(int idEnquesta) throws EnquestaNoExisteixException {
        return ctrlDomini.consultarPreguntes(idEnquesta);
    }

    public void eliminarEnquesta(int idUsuari, int idEnquesta) throws EnquestaNoExisteixException, InvalidFormatEnquesta, UsuariNoValid {
        ctrlDomini.eliminarEnquesta(idUsuari, idEnquesta);
    }

    public void esborrarRespostaEnquesta(int idUsuari, Integer idEnquesta, int idEnquestat) throws UsuariNoHaResposEnquesta, EnquestaNoExisteixException, InvalidFormatEnquesta, UsuariNoValid {
        ctrlDomini.esborrarRespostaEnquesta(idUsuari, idEnquesta, idEnquestat);
    }

    public int modificarPreguntaEnquesta(int idUsuari, int idEnquesta, int indexPregunta, List<String> novaPreguntaText) throws InvalidFormatEnquesta, EnquestaNoExisteixException, UsuariNoValid {
        return ctrlDomini.modificarPreguntaEnquesta(idUsuari, idEnquesta, indexPregunta, novaPreguntaText);
    }

    public void crearEnquesta(String titol, String descripcio, int idCreador, List<String> preguntes) throws InvalidFormatEnquesta, UsuariNoValid {
        ctrlDomini.crearEnquesta(titol, descripcio, idCreador, preguntes);
    }

    public int importarEnquesta(String nombreArchivo, int idCreador)
            throws FileNotFound, InvalidFormatEnquesta, UsuariNoValid {
        String Base_path = "Pruebas";

        try {
            // Construir ruta completa
            String rutaCompleta = Base_path + File.separator + nombreArchivo + ".txt";
            File archivo = new File(rutaCompleta);

            if (!archivo.exists()) {
                throw new FileNotFound("No s'ha trobat el fitxer: " + rutaCompleta);
            }

            // Leer y procesar el archivo
            BufferedReader br = new BufferedReader(new FileReader(archivo));

            String titol = br.readLine();
            if (titol == null || titol.trim().isEmpty()) {
                br.close();
                throw new InvalidFormatEnquesta("Falta títol");
            }

            String descripcio = br.readLine();
            if (descripcio == null) {
                br.close();
                throw new InvalidFormatEnquesta("Falta descripció");
            }

            /*String idCreadorStr = br.readLine();
            if (idCreadorStr == null) {
                br.close();
                throw new InvalidFormatEnquesta("Falta ID creador");
            }

            int idCreador = Integer.parseInt(idCreadorStr.trim());*/

            String numPreguntesStr = br.readLine();
            if (numPreguntesStr == null) {
                br.close();
                throw new InvalidFormatEnquesta("Falta nombre de preguntes");
            }

            int numPreguntes = Integer.parseInt(numPreguntesStr.trim());

            List<String> preguntes = new ArrayList<>();

            for (int i = 0; i < numPreguntes; i++) {
                String liniaTipus = br.readLine();
                if (liniaTipus == null) {
                    br.close();
                    throw new InvalidFormatEnquesta("Falta tipus de la pregunta " + (i + 1));
                }

                String textPregunta = br.readLine();
                if (textPregunta == null) {
                    br.close();
                    throw new InvalidFormatEnquesta("Falta text de la pregunta " + (i + 1));
                }

                preguntes.add(liniaTipus.trim());
                preguntes.add(textPregunta.trim());

                int tipus = Integer.parseInt(liniaTipus.trim());
                if (tipus == 1 || tipus == 2 || tipus == 3) {
                    String liniaNumOpcions = br.readLine();
                    if (liniaNumOpcions == null) {
                        br.close();
                        throw new InvalidFormatEnquesta("Falta nombre d'opcions per la pregunta " + (i + 1));
                    }

                    int numOpcions = Integer.parseInt(liniaNumOpcions.trim());
                    preguntes.add(liniaNumOpcions.trim());

                    for (int j = 0; j < numOpcions; j++) {
                        String opcio = br.readLine();
                        if (opcio == null) {
                            br.close();
                            throw new InvalidFormatEnquesta("Falta l'opció " + (j + 1) + " per la pregunta " + (i + 1));
                        }
                        preguntes.add(opcio.trim());
                    }
                }
            }
            br.close();

            ctrlDomini.crearEnquesta(titol.trim(), descripcio.trim(), idCreador, preguntes);
            return numPreguntes;

        } catch (IOException e) {
            throw new FileNotFound("Error llegint el fitxer: " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new InvalidFormatEnquesta("Format numèric incorrecte: " + e.getMessage());
        } catch (UsuariNoValid e) {
            throw new UsuariNoValid("Error de l'usuari: " + e.getMessage());
        }
    }

    public int importarRespostes(String nombreArchivo, int idUsuari, int idEnquesta)
            throws FileNotFound, InvalidFormatEnquesta, EnquestaNoExisteixException, UsuariNoValid {
        String Base_path = "Pruebas";

        // Construir ruta completa
        String rutaCompleta = Base_path + File.separator + nombreArchivo + ".txt";
        File archivo = new File(rutaCompleta);

        if (!archivo.exists()) {
            throw new FileNotFound("No s'ha trobat el fitxer: " + rutaCompleta);
        }
        return ctrlDomini.importarRespostes(idUsuari, rutaCompleta, idEnquesta);

    }

    public List<String> exportarEnquesta(int idUsuari, int idEnquesta) throws EnquestaNoExisteixException, UsuariNoValid {
        return ctrlDomini.exportarEnquesta(idEnquesta, idUsuari);
    }

    public List<String> exportarRespostesEnquesta(int idEnquesta) throws EnquestaNoExisteixException {
        return ctrlDomini.exportarRespostesEnquesta(idEnquesta);
    }

    public void respondreEnquesta(int idEnquestat, int idEnquesta, List<String> respostes) throws EnquestaNoExisteixException, InvalidFormatEnquesta, InvalidFormatResposta, UsuariNoValid {
        ctrlDomini.respondreEnquesta(idEnquesta, idEnquestat, respostes);
    }

    ///////Persistencia
    public void exportarRespostesAFitxer(int idUsuari, int idEnquesta, String path) throws Exception {
        ctrlDomini.exportarRespostesAFitxer(idUsuari, idEnquesta, path);
    }

    public List<String> obtenirRespostesEnquesta(int idEnquesta) {
        return ctrlDomini.obtenirRespostesEnquesta(idEnquesta);
    }

    public List<String> obtenirInfoEnquesta(int idEnquesta) throws EnquestaNoExisteixException {
        return ctrlDomini.consultarEnquestaAmbPreguntes(idEnquesta);
    }

    public void donarPoders(int idExecutor, int idEnquesta, String nomTarget, String rol) throws InvalidFormatEnquesta, UsuariNoValid {
        if(rol.equals("Enquestador")) {
            ctrlDomini.donarPodersEnquestador(idExecutor,idEnquesta, nomTarget);
        }
        else {
            ctrlDomini.donarPodersAdmin(idExecutor,idEnquesta, nomTarget);
        }

    }

    public Map<Integer, Integer> aplicarClustering(int idUsuari, int idEnquesta, int k, int iter) throws EnquestaNoExisteixException, KmeansExcepcio, InvalidFormatEnquesta, UsuariNoValid {
        return ctrlDomini.clustering(idUsuari, idEnquesta, k, iter);
    }

    public void vetarUsuari(int idUsuari, String nomObjectiu) throws UsuariNoValid {
        ctrlDomini.vetarUsuari(idUsuari, nomObjectiu);
    }

    public void desvetarUsuari(int idUsuari, String nomObjectiu) throws UsuariNoValid {
        ctrlDomini.desvetarUsuari(idUsuari, nomObjectiu);
    }

    public List<String> obtenirUsuaris() {
        return ctrlDomini.obtenirNomUsuaris();
    }

    public int getIdUsuariPerNom(String nomUsuari) {
        return ctrlDomini.getIdUsuariPerNom(nomUsuari);
    }

    public String obtenirRolUsuari(int idUsuari) {
        return ctrlDomini.getCtrlDominiMantUsuari().getRolUsuari(idUsuari);
    }
}
