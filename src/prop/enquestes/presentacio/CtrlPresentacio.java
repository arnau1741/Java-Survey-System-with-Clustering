package prop.enquestes.presentacio;

import prop.enquestes.controladors.CtrlDomini;
import prop.enquestes.controladors.CtrlDominiMantUsuari;
import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractMap;
import prop.enquestes.domini.Usuari;
import prop.enquestes.excepcions.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controlador principal de la capa de Presentació.
 * <p>
 * Aquesta classe actua com a punt d'entrada per a la interfície gràfica i
 * gestiona la navegació entre
 * les diferents vistes de l'aplicació. També fa d'intermediari entre les vistes
 * i el Controlador de Domini.
 * </p>
 */
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

    /**
     * Constructor del Controlador de Presentació.
     * Inicialitza el controlador de domini i la vista principal de l'aplicació.
     *
     * @throws InvalidFormatEnquesta       Si hi ha un error de format intern en
     *                                     inicialitzar dades.
     * @throws EnquestaNoExisteixException Si s'intenten carregar enquestes que no
     *                                     existeixen inicialment.
     */
    public CtrlPresentacio() throws InvalidFormatEnquesta, EnquestaNoExisteixException {
        ctrlDomini = new CtrlDomini();
        vistaPrincipal = new VistaPrincipal(this);
    }

    /**
     * Mètode per arrencar la capa de presentació.
     * Fa visible la finestra principal.
     */
    public void inicializarPresentacio() {
        vistaPrincipal.hacerVisible(true);
    }

    // ==========================================
    // GESTIÓ DE VISTES (NAVEGACIÓ)
    // ==========================================

    /**
     * Mostra la vista per iniciar sessió i amaga la vista principal.
     */
    public void mostrarVistaIniciarSessio() {
        vistaPrincipal.hacerVisible(false);
        vistaIniciarSessio = new VistaIniciarSessio(this);
        vistaIniciarSessio.setVisible(true);
    }

    /**
     * Mostra la vista per registrar un nou usuari.
     */
    public void mostrarVistaCrearUsuari() {
        vistaPrincipal.hacerVisible(false);
        vistaCrearUsuari = new VistaCrearUsuari(this);
        vistaCrearUsuari.setVisible(true);
    }

    /**
     * Mostra la vista per a usuaris convidats (sense registrar).
     */
    public void mostrarVistaConvidat() {
        int idUsuari = -1;
        vistaPrincipal.hacerVisible(false);
        vistaConvidat = new VistaConvidat(this, idUsuari);
        vistaConvidat.setVisible(true);
    }

    /**
     * Redirigeix l'usuari a la vista principal corresponent segons el seu rol.
     *
     * @param idUsuari Identificador únic de l'usuari que ha iniciat sessió.
     */
    public void mostrarVistaPrincipalComuna(int idUsuari) {
        /// Des d'aqui obtenim el rol i mostrem qualsevol de les vistes decidides
        String nomUsuari = obtenirNomUsuari(idUsuari);
        String rol = obtenirRol(idUsuari);

        if (rol.equals("ENQUESTADOR")) {
            vistaEnquestador = new VistaEnquestador(this, idUsuari, nomUsuari, rol);
            vistaEnquestador.setVisible(true);
        } else if (rol.equals("ADMIN")) {
            vistaAdmin = new VistaAdmin(this, idUsuari, nomUsuari, rol);
            vistaAdmin.setVisible(true);
        } else if (rol.equals("MODERADOR")) {
            vistaModerador = new VistaModerador(this, idUsuari, nomUsuari, rol);
            vistaModerador.setVisible(true);
        } else {
            vistaEnquestat = new VistaEnquestat(this, idUsuari, nomUsuari, rol);
            vistaEnquestat.setVisible(true);
        }

    }

    /**
     * Obre la finestra per consultar el perfil d'usuari.
     */
    public void mostrarConsultarPerfil(int idUsuari) {
        String rol = obtenirRol(idUsuari);
        if (rol.equals("ENQUESTADOR")) {
            vistaEnquestador.setVisible(false);
        } else if (rol.equals("ADMIN")) {
            vistaAdmin.setVisible(false);
        } else if (rol.equals("MODERADOR")) {
            vistaModerador.setVisible(false);
        } else vistaEnquestat.setVisible(false);

        vistaConsultarPerfil = new VistaConsultarPerfil(this, idUsuari);
        vistaConsultarPerfil.setVisible(true);
    }

    /**
     * Obre la finestra per modificar una enquesta existent.
     *
     * @param idUsuari ID de l'usuari que vol modificar l'enquesta.
     */
    public void mostrarVistaModificarEnquesta(int idUsuari) {
        vistaModificarEnquesta = new VistaModificarEnquesta(this, idUsuari);
        vistaModificarEnquesta.setVisible(true);

    }

    /**
     * Obre la finestra per consultar/cercar enquestes.
     */
    public void mostrarConsultarEnquesta() {
        vistaConsultarEnquesta = new VistaConsultarEnquesta(this);
        vistaConsultarEnquesta.setVisible(true);
    }

    /**
     * Obre la finestra per crear una nova enquesta.
     *
     * @param idUsuari ID de l'usuari creador.
     */
    public void mostrarCrearEnquesta(int idUsuari) {
        vistaCrearEnquesta = new VistaCrearEnquesta(this, idUsuari);
        vistaCrearEnquesta.setVisible(true);
    }

    /**
     * Obre la finestra per importar una enquesta des d'un fitxer.
     *
     * @param idUsuari ID de l'usuari que realitza la importació.
     */
    public void mostrarImportarEnquesta(int idUsuari) {
        vistaImportarEnquesta = new VistaImportarEnquesta(this, idUsuari);
        vistaImportarEnquesta.setVisible(true);
    }

    /**
     * Obre la finestra per exportar una enquesta.
     *
     * @param idUsuari ID de l'usuari que vol exportar.
     */
    public void mostrarExportarEnquesta(int idUsuari) {
        mostrarExportarEnquesta(idUsuari, false);
    }

    public void mostrarExportarEnquesta(int idUsuari, boolean isAdministeredMode) {
        vistaExportarEnquesta = new VistaExportarEnquesta(this, idUsuari, isAdministeredMode);
        vistaExportarEnquesta.setVisible(true);
    }

    /**
     * Obre la vista de gestió extra d'enquestes per a administradors.
     *
     * @param nomEnquesta Títol de l'enquesta a gestionar.
     * @param idUsuari    ID de l'administrador.
     */
    public void mostrarEnquestesExtra(String nomEnquesta, int idUsuari) {
        vistaEnquestesExtresAdmin = new VistaEnquestesExtresAdmin(this, nomEnquesta, idUsuari);
        vistaEnquestesExtresAdmin.setVisible(true);
    }

    /**
     * Obre la finestra per respondre una enquesta.
     *
     * @param idUsuari ID de l'usuari que respondrà.
     */
    public void mostrarRespondreEnquesta(int idUsuari) {
        mostrarRespondreEnquesta(idUsuari, -1);
    }

    public void mostrarRespondreEnquesta(int idUsuari, int idEnquesta) {
        vistaRespondreEnquesta = new VistaRespondreEnquesta(this, idUsuari, idEnquesta);
        vistaRespondreEnquesta.setVisible(true);
    }

    /**
     * Obre la finestra per exportar les respostes d'una enquesta.
     *
     * @param idUsuari ID de l'usuari que exporta.
     */
    public void mostrarExportarRespostes(int idUsuari) {
        mostrarExportarRespostes(idUsuari, false);
    }

    public void mostrarExportarRespostes(int idUsuari, boolean isAdministeredMode) {
        vistaExportarRespostes = new VistaExportarRespostes(this, idUsuari, isAdministeredMode);
        vistaExportarRespostes.setVisible(true);
    }

    /**
     * Obre la finestra per importar respostes des d'un fitxer extern.
     *
     * @param idUsuari ID de l'usuari que importa.
     */
    public void mostrarImportarRespostes(int idUsuari) {
        vistaImportarRespostes = new VistaImportarRespostes(this, idUsuari, false);
        vistaImportarRespostes.setVisible(true);
    }

    /**
     * Obre la finestra per atorgar permisos sobre enquestes.
     *
     * @param idUsuari ID de l'usuari administrador o propietari.
     */
    public void mostrarDonarPoders(int idUsuari) {
        vistaDonarPoders = new VistaDonarPoders(this, idUsuari);
        vistaDonarPoders.setVisible(true);
    }

    /**
     * Obre la finestra per consultar les respostes rebudes a les enquestes.
     *
     * @param idUsuari ID de l'usuari que consulta.
     */
    public void mostrarConsultarRespostes(int idUsuari) {
        vistaConsultarRespostes = new VistaConsultarRespostes(this, idUsuari);
        vistaConsultarRespostes.setVisible(true);
    }

    /**
     * Obre la finestra de moderació per vetar o desvetar usuaris.
     *
     * @param idUsuari ID del moderador.
     */
    public void mostrarVetarDesvetar(int idUsuari) {
        vistaVetarDesvetar = new VistaVetarDesvetar(this, idUsuari);
        vistaVetarDesvetar.setVisible(true);
    }

    // ======================
    // OPERACIONS AMB DOMINI
    // ======================

    /**
     * Gestiona l'inici de sessió d'un usuari.
     *
     * @param nomUsuari Nom d'usuari.
     * @param password  Contrasenya.
     * @return L'identificador (ID) de l'usuari si l'autenticació és correcta.
     * @throws IllegalArgumentException Si la contrasenya o l'usuari són
     *                                  incorrectes.
     */
    public int iniciarSessio(String nomUsuari, String password) throws IllegalArgumentException {
        int id = ctrlDomini.iniciarSessio(nomUsuari, password);
        return id;
    }

    /**
     * Crea un nou usuari al sistema.
     *
     * @param nom   Nom de l'usuari.
     * @param cont  Contrasenya.
     * @param email Correu electrònic.
     * @param rol   Rol de l'usuari (ENQUESTADOR o ENQUESTAT).
     * @return L'ID del nou usuari creat.
     * @throws UsuariNoValid Si l'usuari ja existeix o les dades no són vàlides.
     */
    public int crearUsuari(String nom, String cont, String email, String rol) throws UsuariNoValid {
        if (rol.equals("ENQUESTADOR")) {
            return ctrlDomini.crearUsuariEnquestador(nom, cont, email);
        } else
            return ctrlDomini.crearUsuariEnquestat(nom, cont, email);
    }

    /**
     * Obté el rol d'un usuari a partir del seu ID.
     *
     * @param id Identificador de l'usuari.
     * @return El rol de l'usuari (ex: "ADMIN", "ENQUESTADOR", etc.).
     */
    public String obtenirRol(int id) {
        CtrlDominiMantUsuari mantUsuari = ctrlDomini.getCtrlDominiMantUsuari();
        String rol = mantUsuari.getRolUsuari(id);
        return rol;
    }

    /**
     * Obté el nom d'usuari a partir del seu ID.
     *
     * @param id Identificador de l'usuari.
     * @return El nom d'usuari (String).
     */
    public String obtenirNomUsuari(int id) {
        CtrlDominiMantUsuari mantUsuari = ctrlDomini.getCtrlDominiMantUsuari();
        Usuari us = mantUsuari.getUsuari(id);
        String nomUsuari = us.getUsuari();
        return nomUsuari;
    }

    /**
     * Obté la informació bàsica d'una enquesta.
     *
     * @param id Identificador de l'enquesta.
     * @return Llista d'Strings amb la informació de l'enquesta.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix.
     */
    public List<String> consultarEnquesta(int id) throws EnquestaNoExisteixException {
        List<String> Enquesta = ctrlDomini.consultarEnquesta(id);
        return Enquesta;
    }

    /**
     * Obté la informació del perfil d'un usuari.
     *
     * @param id Identificador de l'usuari.
     * @return Llista d'Strings amb les dades de l'usuari.
     */
    public List<String> consultarPerfil(int id) {
        List<String> Perfil = ctrlDomini.consultarPerfil(id);
        return Perfil;
    }


    /**
     * Obté la llista d'enquestes disponibles per a un usuari.
     * Si l'usuari està registrat, filtra aquelles que ja ha respost.
     *
     * @param idUsuari ID de l'usuari.
     * @return Llista d'enquestes disponibles.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix
     */
    public List<String> obtenirLlistaEnquestes(int idUsuari) throws EnquestaNoExisteixException {
        return ctrlDomini.consultarEnquestesAmbPreguntesIRespostes(idUsuari);
    }

    /**
     * Obté la llista d'enquestes que administra un usuari específic.
     *
     * @param idUsuari ID de l'administrador o propietari.
     * @return Llista d'Strings amb les enquestes administrades.
     * @throws EnquestaNoExisteixException Si l'usuari no administra cap enquesta o
     *                                     aquestes no existeixen.
     */
    public List<String> obtenirEnquestesAdministrades(int idUsuari) throws EnquestaNoExisteixException {
        return ctrlDomini.obtenirEnquestesPerRol(idUsuari);
    }

    /**
     * Consulta una enquesta específica incloent les seves preguntes.
     *
     * @param idEnquesta ID de l'enquesta.
     * @return Llista d'Strings amb les dades de l'enquesta i les preguntes.
     * @throws EnquestaNoExisteixException Si l'enquesta no es troba.
     */
    public List<String> obtenirPreguntesEnquesta(int idEnquesta) throws EnquestaNoExisteixException {
        return ctrlDomini.consultarEnquestaAmbPreguntes(idEnquesta);
    }

    /**
     * Obté només les preguntes d'una enquesta.
     *
     * @param idEnquesta ID de l'enquesta.
     * @return Llista de preguntes en format text.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix.
     */
    public List<String> obtenirPreguntes(int idEnquesta) throws EnquestaNoExisteixException {
        return ctrlDomini.consultarPreguntes(idEnquesta);
    }

    /**
     * Elimina una enquesta del sistema.
     *
     * @param idUsuari   ID de l'usuari que vol eliminar l'enquesta (ha de tenir
     *                   permisos).
     * @param idEnquesta ID de l'enquesta a eliminar.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix.
     * @throws InvalidFormatEnquesta       Si hi ha un error de format.
     * @throws UsuariNoValid               Si l'usuari no té permisos.
     */
    public void eliminarEnquesta(int idUsuari, int idEnquesta)
            throws EnquestaNoExisteixException, InvalidFormatEnquesta, UsuariNoValid {
        ctrlDomini.eliminarEnquesta(idUsuari, idEnquesta);
    }

    /*
     * Esborra una resposta concreta d'un usuari a una enquesta.
     *
     * @param idUsuari    ID de l'usuari administrador/executor.
     * @param idEnquesta  ID de l'enquesta.
     * @param idEnquestat ID de l'usuari que va respondre l'enquesta.
     * @throws UsuariNoHaResposEnquesta    Si l'enquestat no havia respost
     *                                     l'enquesta.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix.
     * @throws InvalidFormatEnquesta       Si hi ha un error intern de format.
     * @throws UsuariNoValid               Si l'usuari executor no té permisos.
     */
    public void esborrarRespostaEnquesta(int idUsuari, Integer idEnquesta, int idEnquestat)
            throws UsuariNoHaResposEnquesta, EnquestaNoExisteixException, InvalidFormatEnquesta, UsuariNoValid {
        ctrlDomini.esborrarRespostaEnquesta(idUsuari, idEnquesta, idEnquestat);
    }

    /**
     * Modifica el text d'una pregunta dins d'una enquesta.
     *
     * @param idUsuari         ID de l'usuari que modifica.
     * @param idEnquesta       ID de l'enquesta.
     * @param indexPregunta    Índex de la pregunta dins l'enquesta.
     * @param novaPreguntaText Llista amb el nou text i opcions de la pregunta.
     * @return Codi d'operació o resultat.
     * @throws InvalidFormatEnquesta       Si el format de la nova pregunta no és
     *                                     vàlid.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix.
     * @throws UsuariNoValid               Si l'usuari no té permisos.
     */
    public int modificarPreguntaEnquesta(int idUsuari, int idEnquesta, int indexPregunta, List<String> novaPreguntaText)
            throws InvalidFormatEnquesta, EnquestaNoExisteixException, UsuariNoValid {
        return ctrlDomini.modificarPreguntaEnquesta(idUsuari, idEnquesta, indexPregunta, novaPreguntaText);
    }

    /**
     * Crea una nova enquesta al sistema.
     *
     * @param titol      Títol de l'enquesta.
     * @param descripcio Descripció de l'enquesta.
     * @param idCreador  ID de l'usuari creador.
     * @param preguntes  Llista d'Strings definint les preguntes.
     * @throws InvalidFormatEnquesta Si el format de les preguntes o dades és
     *                               incorrecte.
     * @throws UsuariNoValid         Si l'usuari creador no és vàlid.
     */
    public void crearEnquesta(String titol, String descripcio, int idCreador, List<String> preguntes)
            throws InvalidFormatEnquesta, UsuariNoValid {
        ctrlDomini.crearEnquesta(titol, descripcio, idCreador, preguntes);
    }

    /**
     * Importa una enquesta des d'un fitxer de text localitzat a la carpeta
     * "Pruebas".
     *
     * @param nombreArchivo Nom del fitxer sense extensió.
     * @param idCreador     ID de l'usuari que importa l'enquesta.
     * @return El nombre de preguntes importades.
     * @throws FileNotFound          Si el fitxer no es troba.
     * @throws InvalidFormatEnquesta Si el fitxer no segueix el format esperat.
     * @throws UsuariNoValid         Si l'usuari no és vàlid.
     */
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

    /**
     * Importa respostes a una enquesta des d'un fitxer extern.
     *
     * @param nombreArchivo Nom del fitxer.
     * @param idUsuari      ID de l'usuari que realitza la importació.
     * @param idEnquesta    ID de l'enquesta on s'associen les respostes.
     * @return Nombre de respostes importades.
     * @throws FileNotFound                Si no es troba el fitxer.
     * @throws InvalidFormatEnquesta       Si el format no és correcte.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix.
     * @throws UsuariNoValid               Si l'usuari no és vàlid.
     */
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

    /**
     * Obté les dades d'una enquesta per a ser exportada.
     *
     * @param idUsuari   ID de l'usuari sol·licitant.
     * @param idEnquesta ID de l'enquesta.
     * @return Llista d'Strings amb el format d'exportació de l'enquesta.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix.
     * @throws UsuariNoValid               Si l'usuari no té permisos.
     */
    public List<String> exportarEnquesta(int idUsuari, int idEnquesta)
            throws EnquestaNoExisteixException, UsuariNoValid {
        return ctrlDomini.exportarEnquesta(idEnquesta, idUsuari);
    }

    /**
     * Obté les respostes d'una enquesta per a ser exportades.
     *
     * @param idEnquesta ID de l'enquesta.
     * @return Llista d'Strings amb les respostes.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix.
     */
    public List<String> exportarRespostesEnquesta(int idEnquesta) throws EnquestaNoExisteixException {
        return ctrlDomini.exportarRespostesEnquesta(idEnquesta);
    }

    /**
     * Envia les respostes d'un usuari a una enquesta per ser emmagatzemades.
     *
     * @param idEnquestat ID de l'usuari que respon.
     * @param idEnquesta  ID de l'enquesta.
     * @param respostes   Llista de respostes en format text.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix.
     * @throws InvalidFormatEnquesta       Si l'enquesta té un format incorrecte.
     * @throws InvalidFormatResposta       Si les respostes no compleixen el format
     *                                     esperat.
     * @throws UsuariNoValid               Si l'usuari no és vàlid.
     */
    public void respondreEnquesta(int idEnquestat, int idEnquesta, List<String> respostes)
            throws EnquestaNoExisteixException, InvalidFormatEnquesta, InvalidFormatResposta, UsuariNoValid {
        ctrlDomini.respondreEnquesta(idEnquesta, idEnquestat, respostes);
    }

    /////// Persistencia

    /**
     * Guarda les respostes d'una enquesta directament a un fitxer.
     *
     * @param idUsuari   ID de l'usuari que sol·licita l'exportació.
     * @param idEnquesta ID de l'enquesta.
     * @param path       Ruta on guardar el fitxer.
     * @throws Exception Si hi ha errors d'escriptura o lògica.
     */
    public void exportarRespostesAFitxer(int idUsuari, int idEnquesta, String path) throws Exception {
        ctrlDomini.exportarRespostesAFitxer(idUsuari, idEnquesta, path);
    }

    /**
     * Recupera les respostes emmagatzemades per a una enquesta.
     *
     * @param idEnquesta ID de l'enquesta.
     * @return Llista d'Strings amb les respostes.
     */
    public List<String> obtenirRespostesEnquesta(int idEnquesta) {
        return ctrlDomini.obtenirRespostesEnquesta(idEnquesta);
    }

    /**
     * Obté la informació detallada d'una enquesta i les seves preguntes.
     *
     * @param idEnquesta ID de l'enquesta.
     * @return Llista d'Strings amb la informació.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix.
     */
    public List<String> obtenirInfoEnquesta(int idEnquesta) throws EnquestaNoExisteixException {
        return ctrlDomini.consultarEnquestaAmbPreguntes(idEnquesta);
    }

    /**
     * Assigna permisos d'administració sobre una enquesta a un altre usuari.
     *
     * @param idExecutor ID de l'usuari que atorga els poders.
     * @param idEnquesta ID de l'enquesta.
     * @param nomTarget  Nom de l'usuari que rebrà els poders.
     * @param rol        Rol que se li assignarà (o rol de l'executor per
     *                   verificació).
     * @throws InvalidFormatEnquesta Si hi ha errors en l'estructura de dades.
     * @throws UsuariNoValid         Si els usuaris no són vàlids o no tenen
     *                               permisos.
     */
    public void donarPoders(int idExecutor, int idEnquesta, String nomTarget, String rol)
            throws InvalidFormatEnquesta, UsuariNoValid {
        if (rol.equals("Enquestador")) {
            ctrlDomini.donarPodersEnquestador(idExecutor, idEnquesta, nomTarget);
        } else {
            ctrlDomini.donarPodersAdmin(idExecutor, idEnquesta, nomTarget);
        }

    }

    /**
     * Executa l'algorisme de clustering K-Means sobre les respostes d'una enquesta.
     *
     * @param idUsuari   ID de l'usuari que demana l'operació.
     * @param idEnquesta ID de l'enquesta objectiu.
     * @param k          Nombre de clústers.
     * @param iter       Nombre màxim d'iteracions.
     * @return Un mapa relacionant clústers amb usuaris/respostes.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix.
     * @throws KmeansExcepcio              Si falla l'algorisme K-means.
     * @throws InvalidFormatEnquesta       Si el format de dades és incorrecte.
     * @throws UsuariNoValid               Si l'usuari no és vàlid.
     */
    public SimpleEntry<Map<Integer, Integer>, Double> aplicarClustering(int idUsuari, int idEnquesta, int k, int iter,
                                                                        String tipus) throws EnquestaNoExisteixException, KmeansExcepcio, InvalidFormatEnquesta, UsuariNoValid {
        return ctrlDomini.clustering(idUsuari, idEnquesta, k, iter, tipus);
    }

    /**
     * Veta (bloqueja) un usuari al sistema.
     *
     * @param idUsuari    ID del moderador.
     * @param nomObjectiu Nom de l'usuari a vetar.
     * @throws UsuariNoValid Si l'usuari no existeix o el moderador no té permisos.
     */
    public void vetarUsuari(int idUsuari, String nomObjectiu) throws UsuariNoValid {
        ctrlDomini.vetarUsuari(idUsuari, nomObjectiu);
    }

    /**
     * Retira el vet (desbloqueja) a un usuari del sistema.
     *
     * @param idUsuari    ID del moderador.
     * @param nomObjectiu Nom de l'usuari a desvetar.
     * @throws UsuariNoValid Si l'usuari no existeix o el moderador no té permisos.
     */
    public void desvetarUsuari(int idUsuari, String nomObjectiu) throws UsuariNoValid {
        ctrlDomini.desvetarUsuari(idUsuari, nomObjectiu);
    }

    /**
     * Obté una llista amb els noms de tots els usuaris del sistema.
     *
     * @return Llista de noms d'usuari.
     */
    public List<String> obtenirUsuaris() {
        return ctrlDomini.obtenirNomUsuaris();
    }

    /**
     * Recupera l'ID d'un usuari donat el seu nom.
     *
     * @param nomUsuari Nom de l'usuari.
     * @return ID de l'usuari.
     */
    public int getIdUsuariPerNom(String nomUsuari) {
        return ctrlDomini.getIdUsuariPerNom(nomUsuari);
    }

    /**
     * Recupera el rol d'un usuari donat el seu ID.
     *
     * @param idUsuari ID de l'usuari.
     * @return El rol de l'usuari.
     */
    public String obtenirRolUsuari(int idUsuari) {
        return ctrlDomini.getCtrlDominiMantUsuari().getRolUsuari(idUsuari);
    }

    /**
     * Actualitza i guarda les dades abans de tancar l'aplicació.
     */
    public void actualizaDades() {
        ctrlDomini.guardarDades();
        System.exit(0);
    }

    // ==========================================
    // NOVES FUNCIONALITATS (USER REQ)
    // ==========================================

    /**
     * Exporta una enquesta sense incloure les respostes.
     *
     * @param idUsuari   ID de l'usuari que sol·licita l'exportació.
     * @param idEnquesta ID de l'enquesta a exportar.
     * @return Llista d'Strings amb el format d'exportació de l'enquesta.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix.
     * @throws UsuariNoValid               Si l'usuari no té permisos.
     */
    public List<String> exportarEnquestaSenseRespostes(int idUsuari, int idEnquesta)
            throws EnquestaNoExisteixException, UsuariNoValid {
        // Validación básica
        return ctrlDomini.exportarEnquestaSenseRespostes(idEnquesta);
    }

    /**
     * Exporta les respostes d'un usuari a una enquesta específica.
     *
     * @param idEnquesta ID de l'enquesta.
     * @param idUsuari   ID de l'usuari.
     * @return Llista d'Strings amb les respostes de l'usuari.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix.
     * @throws UsuariNoValid               Si l'usuari no és vàlid.
     */
    public List<String> exportarRespostesUsuari(int idEnquesta, int idUsuari)
            throws EnquestaNoExisteixException, UsuariNoValid {
        return ctrlDomini.exportarRespostesUsuari(idEnquesta, idUsuari);
    }

    /**
     * Obté les respostes d'un usuari a una enquesta específica.
     *
     * @param idEnquesta ID de l'enquesta.
     * @param idUsuari   ID de l'usuari.
     * @return Llista d'Strings amb les respostes de l'usuari.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix.
     */
    public List<String> getRespostesUsuariList(int idEnquesta, int idUsuari) throws EnquestaNoExisteixException {
        return ctrlDomini.getRespostesUsuariList(idEnquesta, idUsuari);
    }

    /**
     * Obté la llista d'enquestes que un usuari ha respost.
     *
     * @param idUsuari ID de l'usuari.
     * @return Llista d'Strings amb les enquestes respostes.
     * @throws UsuariNoValid Si l'usuari no és vàlid.
     */
    public List<String> obtenirEnquestesRespostesPerUsuari(int idUsuari) throws UsuariNoValid {
        return ctrlDomini.obtenirEnquestesRespostesPerUsuari(idUsuari);
    }

    /**
     * Modifica les respostes d'un usuari a una enquesta específica.
     *
     * @param idUsuari   ID de l'usuari que vol modificar les respostes.
     * @param idEnquesta ID de l'enquesta.
     * @param respostes  Llista de noves respostes en format text.
     * @throws EnquestaNoExisteixException Si l'enquesta no existeix.
     * @throws UsuariNoValid               Si l'usuari no és vàlid.
     * @throws InvalidFormatEnquesta       Si l'enquesta té un format incorrecte.
     * @throws InvalidFormatResposta       Si les noves respostes no compleixen
     *                                     el format esperat.
     * @throws UsuariNoHaResposEnquesta    Si l'usuari no havia respost
     *                                     prèviament l'enquesta.
     */
    public void modificarRespostaEnquesta(int idUsuari, int idEnquesta, List<String> respostes)
            throws EnquestaNoExisteixException, UsuariNoValid, InvalidFormatEnquesta, InvalidFormatResposta,
            UsuariNoHaResposEnquesta {
        ctrlDomini.modificarRespostaEnquesta(idUsuari, idEnquesta, respostes);
    }

    // Mètodes per a les noves vistes o modificades

    /**
     * Obre la nova vista per consultar i modificar les respostes de l'usuari.
     * @param idUsuari   ID de l'usuari que vol modificar les respostes.
     */
    public void mostrarConsultarRespostesPropies(int idUsuari) {
        VistaConsultarRespostesUsuari v = new VistaConsultarRespostesUsuari(this, idUsuari);
        v.setVisible(true);
    }

}
