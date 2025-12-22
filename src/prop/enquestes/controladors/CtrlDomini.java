package prop.enquestes.controladors;

import prop.enquestes.domini.*;
import prop.enquestes.excepcions.*;
import prop.enquestes.persistencia.GestorPersistencia;

import static prop.enquestes.domini.KMeans.InitializationMethod.KMEANS_PLUS_PLUS;
import static prop.enquestes.domini.KMeans.InitializationMethod.RANDOM;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CtrlDomini {
    private CtrlDominiMantEnquesta ctrlDominiMantEnquesta;
    private CtrlDominiMantUsuari ctrlDominiMantUsuari;
    private GestorPersistencia gestorPersistencia;

    /**
     * Funcio constructora de CtrlDomini
     *
     */
    public CtrlDomini() {
        ctrlDominiMantEnquesta = new CtrlDominiMantEnquesta();
        ctrlDominiMantUsuari = new CtrlDominiMantUsuari();
        gestorPersistencia = new GestorPersistencia();

        // Load data on startup
        ctrlDominiMantEnquesta.setEnquestes(gestorPersistencia.carregarEnquestes());
        Map<Integer, Enquesta> totesEnquestes = ctrlDominiMantEnquesta.getEnquestesObj();
        ctrlDominiMantUsuari.setUsuaris(gestorPersistencia.carregarUsuaris(totesEnquestes));
    }

    /**
     * Retorna el controlador de domini de manteniment d'usuaris
     * 
     * @return CtrlDominiMantUsuari
     */
    public CtrlDominiMantUsuari getCtrlDominiMantUsuari() {
        return ctrlDominiMantUsuari;
    }

    /**
     * Retorna el controlador de domini de manteniment d'enquestes
     * 
     * @return CtrlDominiMantEnquesta
     */
    public CtrlDominiMantEnquesta getCtrlDominiMantEnquesta() {
        return ctrlDominiMantEnquesta;
    }

    /**
     * Funcio per a guardar les dades
     */
    public void guardarDades() {
        gestorPersistencia.guardarEnquestes(ctrlDominiMantEnquesta.getEnquestesObj());
        gestorPersistencia.guardarUsuaris(ctrlDominiMantUsuari.getUsuaris());
    }

    // ==============================================================
    // Gestió d'enquestes
    // ==============================================================

    /**
     * Retorna les preguntes de l'enquesta amb id donat
     * 
     * @param idEnquesta Identificador de l'enquesta
     * @return Llista de preguntes en format text
     * @throws EnquestaNoExisteixException si l'enquesta no existeix
     */
    public List<String> getPreguntes(Integer idEnquesta) throws EnquestaNoExisteixException {
        return this.ctrlDominiMantEnquesta.getPreguntesEnquesta(idEnquesta);
    }


    /**
     * Funcio per a respondre una enquesta
     * 
     * @param idEnquesta      Identificador de l'enquesta a respondre
     * @param idUsuari        Identificador de l'usuari que respon l'enquesta
     * @param respostesUsuari Llista de respostes
     */
    private void respondreEnquestaPrivate(Integer idEnquesta, int idUsuari, List<String> respostesUsuari)
            throws EnquestaNoExisteixException {
        Enquesta enq = this.ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }
        enq.afegeixResposta(idUsuari, respostesUsuari);
    }

    /**
     * Funcio per a respondre una enquesta
     * 
     * @param idEnquesta
     * @param idUsuari
     * @param respostesUsuari
     * @throws InvalidFormatResposta
     * @throws InvalidFormatEnquesta
     * @throws EnquestaNoExisteixException
     * @throws UsuariNoValid
     */
    public void respondreEnquesta(Integer idEnquesta, int idUsuari, List<String> respostesUsuari)
            throws InvalidFormatResposta, EnquestaNoExisteixException, UsuariNoValid {
        if (respostesUsuari == null || respostesUsuari.isEmpty())
            throw new InvalidFormatResposta("Les respostes de l'usuari són nul·les");
        if (idUsuari == -1) {
            respondreEnquestaPrivate(idEnquesta, -1, respostesUsuari);
            return; // Èxit
        }

        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if (u == null)
            throw new UsuariNoValid("L'usuari no existeix"); // Usuari no existeix
        if (u.isBlocked())
            throw new UsuariNoValid("L'usuari està vetat"); // Usuari vetat

        if (!u.esAdmin() && !u.esEnquestat()) {
            throw new UsuariNoValid("Credencials insuficients"); // Credencials insuficients
        }

        Enquesta enq;
        enq = this.ctrlDominiMantEnquesta.getEnquesta(idEnquesta);

        if (u.teEnquestaRealitzada(idEnquesta))
            throw new UsuariNoValid("L'enquesta ja ha estat realitzada per l'usuari"); // Enquesta ja realitzada

        respondreEnquestaPrivate(idEnquesta, idUsuari, respostesUsuari);

        u.demanarAfegirEnquestaRealitzada(enq);
    }

    /**
     * Funcio per a crear una enquesta
     * 
     * @param titol      Titol de l'enquesta
     * @param descripcio Descripcio de l'enquesta
     * @param idCreador  Identificador de l'usuari creador de l'enquesta
     * @param preguntes  Llista de preguntes
     */
    private void crearEnquestaPrivate(String titol, String descripcio, int idCreador, List<String> preguntes)
            throws InvalidFormatEnquesta {
        this.ctrlDominiMantEnquesta.novaEnquesta(titol, descripcio, idCreador, preguntes);
    }

    /**
     * Funcio per a crear una enquesta
     * 
     * @param titol
     * @param descripcio
     * @param idCreador
     * @param preguntes
     * @throws InvalidFormatEnquesta
     * @throws UsuariNoValid
     */
    public void crearEnquesta(String titol, String descripcio, int idCreador, List<String> preguntes)
            throws InvalidFormatEnquesta, UsuariNoValid {
        if (titol == null || titol.isEmpty() || descripcio == null || descripcio.isEmpty() || preguntes.isEmpty()
                || preguntes == null) {
            throw new IllegalArgumentException("Paràmetres invàlids per crear enquesta");
        }

        Usuari u = ctrlDominiMantUsuari.getUsuari(idCreador);
        if (u == null)
            throw new UsuariNoValid("L'usuari no existeix"); // L'usuari no existeix

        if (u.getId() < 0)
            throw new UsuariNoValid("L'usuari és anònim"); // L'usuari és anònim

        if (u.isBlocked())
            throw new UsuariNoValid("L'usuari està vetat"); // Usuari vetat

        if (u.esEnquestador())
            throw new UsuariNoValid("Credencials insuficients"); // Credencials insuficients

        crearEnquestaPrivate(titol, descripcio, idCreador, preguntes);

        Enquesta enq = ctrlDominiMantEnquesta.getUltimaEnquestaCreada();
        if (u.esEnquestat()) {
            u.cambiarARolAdmin();
        }
        u.demanarAfegirEnquestaAdministrada(enq);
    }

    /**
     * Funcio per a exportar una enquesta
     * 
     * @param idEnquesta de l'enquesta a exportar
     * @return Llista de strings amb la informacio de l'enquesta
     * @throws EnquestaNoExisteixException si l'enquesta no existeix
     */
    protected List<String> exportarEnquestaPrivate(Integer idEnquesta) throws EnquestaNoExisteixException {
        if (idEnquesta == -1)
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }
        List<String> exportat = new ArrayList<>();
        exportat.add("==== Informacio enquesta ====");
        exportat.add("ID: " + enq.getId());
        exportat.add("Titol: " + enq.getTitol());
        exportat.add("Descripcio: " + enq.getDescripcio());
        exportat.add("Creador ID: " + enq.getCreador());
        exportat.add("Numero de preguntes: " + enq.getNumPreguntes());
        exportat.add("");

        List<Pregunta> preguntes = enq.getPreguntesObj();
        exportat.add("==== Preguntes i respostes ====");
        for (int i = 0; i < preguntes.size(); i++) {
            Pregunta p = preguntes.get(i);
            exportat.add("Pregunta: " + (i + 1) + ": " + p.getText());

            List<String> opcions = p.getOpcions();
            if (opcions != null && !opcions.isEmpty()) {
                exportat.add("Opcions:");
                for (int j = 0; j < opcions.size(); j++) {
                    String opcio = opcions.get(j);
                    exportat.add("  * Opció " + (j + 1) + ": " + opcio);
                }
            }
            Map<Integer, Resposta> respostes = p.getRespostes();
            if (!respostes.isEmpty()) {
                exportat.add("Respostes:");
                for (Map.Entry<Integer, Resposta> entry : respostes.entrySet()) {
                    exportat.add(
                            "  - Usuari ID: " + entry.getKey() + ", Resposta: " + entry.getValue().getText(opcions));
                }
            } else {
                exportat.add("No hi ha respostes.");
            }
            exportat.add("");
        }
        return exportat;
    }

    /**
     * Funcio per a exportar una enquesta
     * 
     * @param idEnquesta
     * @param idUsuari
     * @return Llista de strings amb la informacio de l'enquesta, null si hi ha un
     *         error
     */
    public List<String> exportarEnquesta(Integer idEnquesta, int idUsuari)
            throws UsuariNoValid, EnquestaNoExisteixException {
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if (u == null || u.getId() < 0)
            throw new UsuariNoValid("L'usuari no existeix o és anònim"); // L'usuari no existeix o és anònim
        if (u.isBlocked())
            throw new UsuariNoValid("L'usuari està vetat"); // Usuari vetat

        return exportarEnquestaPrivate(idEnquesta);
    }

    ////////
    /**
     * Funcio per a importar respostes d'un fitxer
     *
     * @param path       origen del fitxer
     * @param idEnquesta identificador de l'enquesta
     * @return nombre de respostes importades, -1 si hi ha un error llegint el
     *         fitxer, -2 si l'enquesta no existeix
     */
    protected int importarRespostesPrivate(String path, Integer idEnquesta)
            throws EnquestaNoExisteixException, InvalidFormatEnquesta {
        // llegir fitxer
        List<String> respostesTxt = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                respostesTxt.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // afegir respostes a l'enquesta
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }

        // llegim el nombre de respostes (primer linia)
        int numRespostes = Integer.parseInt(respostesTxt.get(0));
        int numPreguntes = enq.getNumPreguntes();

        // llegim les respostes (a partir de la linia 1, una resposta per linia)
        List<String> respostesUsuari = new ArrayList<>();
        for (int i = 1; i <= numRespostes * numPreguntes; i++) {
            respostesUsuari.add(respostesTxt.get(i));
            if (i % numPreguntes == 0) {
                // afegim la resposta a l'enquesta
                enq.afegeixResposta(-1, respostesUsuari);
                respostesUsuari.clear();
            }
        }
        return numRespostes; // Èxit
    }

    /**
     * Funcio per a importar respostes d'un fitxer
     * 
     * @param idUsuari
     * @param path
     * @param idEnquesta
     * @return nombre de respostes importades
     * @throws InvalidFormatEnquesta
     * @throws EnquestaNoExisteixException
     * @throws UsuariNoValid
     * @throws IllegalArgumentException
     */
    public int importarRespostes(int idUsuari, String path, Integer idEnquesta)
            throws InvalidFormatEnquesta, EnquestaNoExisteixException, UsuariNoValid, IllegalArgumentException {
        if (path == null || path.isEmpty())
            throw new IllegalArgumentException("Path invàlid");
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if (u == null) {
            throw new UsuariNoValid("L'usuari no existeix");
        }

        if (u.isBlocked()) {
            throw new UsuariNoValid("L'usuari està vetat");
        }

        if (u.esEnquestat()) {
            throw new UsuariNoValid("Credencials insuficients");
        }

        if (u.getId() < 0) {
            throw new UsuariNoValid("L'usuari és anònim");
        }

        if (u.esAdmin()) {
            if (!u.teEnquestaAdministrada(idEnquesta)) {
                throw new InvalidFormatEnquesta("L'usuari no administra l'enquesta");
            }
        } else if (u.esEnquestador()) {
            if (!u.teEnquestaAssignada(idEnquesta)) {
                throw new InvalidFormatEnquesta("L'usuari no té assignada l'enquesta");
            }
        }
        return importarRespostesPrivate(path, idEnquesta);
    }

    /**
     * Funcio per a exportar les respostes d'una enquesta a un fitxer
     *
     * @param idEnquesta identificador de l'enquesta
     * @param path       desti del fitxer
     * @throws EnquestaNoExisteixException
     * @throws UsuariNoValid
     * @throws IllegalArgumentException
     */
    public void exportarRespostesAFitxer(int idUsuari, int idEnquesta, String path)
            throws UsuariNoValid, EnquestaNoExisteixException, IllegalArgumentException {
        if (path == null || path.isEmpty())
            throw new IllegalArgumentException("Path invàlid");
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if (u == null) {
            throw new UsuariNoValid("L'usuari no existeix");
        }

        if (u.isBlocked()) {
            throw new UsuariNoValid("L'usuari està vetat");
        }

        if (u.getId() < 0) {
            throw new UsuariNoValid("L'usuari és anònim");
        }

        List<String> data = exportarRespostesEnquesta(idEnquesta);
    }

    /**
     * Funcio per a exportar les respostes d'una enquesta
     * 
     * @param idEnquesta
     * @return Llista de strings amb la informacio de les respostes de l'enquesta
     * @throws EnquestaNoExisteixException
     */
    public List<String> exportarRespostesEnquesta(int idEnquesta) throws EnquestaNoExisteixException {
        if (idEnquesta == -1)
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }

        List<String> result = new ArrayList<>();
        result.add("=== RESPOSTES DE L'ENQUESTA " + idEnquesta + " ===");
        result.add("Títol: " + enq.getTitol());
        result.add("");

        List<Pregunta> preguntes = enq.getPreguntesObj();

        int idxPregunta = 1;
        for (Pregunta p : preguntes) {
            result.add("Pregunta " + idxPregunta + ": " + p.getText());

            List<String> opcions = p.getOpcions();
            if (opcions != null) {
                result.add("Opcions:");
                for (String op : opcions) {
                    result.add(" * " + op);
                }
            }

            Map<Integer, Resposta> respostes = p.getRespostes();
            if (respostes.isEmpty()) {
                result.add("   (No hi ha respostes)");
            } else {
                for (Integer idUsuari : respostes.keySet()) {
                    result.add(" - Usuari " + idUsuari + ": " +
                            respostes.get(idUsuari).getText(opcions));
                }
            }
            result.add("");
            idxPregunta++;
        }

        return result;
    }

    /**
     * Funcio per a eliminar una enquesta
     * 
     * @param idEnquesta identificador de l'enquesta a eliminar
     */
    protected void eliminarEnquestaPrivate(Integer idEnquesta) throws EnquestaNoExisteixException {
        ctrlDominiMantEnquesta.eliminarEnquesta(idEnquesta);
        //// ctrlPersistencia.guardarEnquestes(ctrlDominiMantEnquesta.getEnquestesObj());
    }

    /**
     * Funcio per a eliminar una enquesta
     * 
     * @param idUsuari
     * @param idEnquesta
     * @throws EnquestaNoExisteixException
     * @throws UsuariNoValid
     * @throws InvalidFormatEnquesta
     */
    public void eliminarEnquesta(int idUsuari, Integer idEnquesta)
            throws EnquestaNoExisteixException, UsuariNoValid, InvalidFormatEnquesta {
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if (u == null) {
            throw new UsuariNoValid("L'usuari no existeix");
        }

        if (u.isBlocked()) {
            throw new UsuariNoValid("L'usuari està vetat");
        }

        if (!u.esAdmin() && !u.esModerador()) {
            throw new UsuariNoValid("Credencials insuficients");
        }

        if (u.getId() < 0) {
            throw new UsuariNoValid("L'usuari és anònim");
        }

        if (u.esAdmin()) {
            if (!u.teEnquestaAdministrada(idEnquesta)) {
                throw new InvalidFormatEnquesta("L'usuari no administra l'enquesta");
            }
        }

        eliminarEnquestaPrivate(idEnquesta);

        Map<Integer, Usuari> totsElsUsuaris = ctrlDominiMantUsuari.getUsuaris();
        for (Usuari afectarEsborrat : totsElsUsuaris.values()) {
            afectarEsborrat.demanarEliminarAdministrada(idEnquesta);
            afectarEsborrat.demanarEliminarAssignada(idEnquesta);
            afectarEsborrat.demanarEliminarRealitzada(idEnquesta);
        }
    }

    /**
     * Funcio per a modificar una pregunta d'una enquesta
     * 
     * @param idEnquesta   identificador de l'enquesta per la pregunta que vol
     *                     modificar
     * @param idxPregunta  index de la pregunta a modificar
     * @param novaPregunta llista de strings amb la nova pregunta
     * @return 1 si s'ha modificat correctament
     */
    protected int modificarPreguntaEnquestaPrivate(int idEnquesta, int idxPregunta, List<String> novaPregunta)
            throws InvalidFormatEnquesta, EnquestaNoExisteixException {
        int r = ctrlDominiMantEnquesta.modificarPreguntaEnquesta(idEnquesta, idxPregunta, novaPregunta);
        ///// ctrlPersistencia.guardarEnquestes(ctrlDominiMantEnquesta.getEnquestesObj());
        return r;
    }

    /**
     * Funcio per a modificar una pregunta d'una enquesta
     * 
     * @param idUsuari
     * @param idEnquesta
     * @param idxPregunta
     * @param novaPregunta
     * @return 1 si s'ha modificat correctament
     * @throws InvalidFormatEnquesta
     * @throws EnquestaNoExisteixException
     * @throws UsuariNoValid
     * @throws IllegalArgumentException
     */
    public int modificarPreguntaEnquesta(int idUsuari, int idEnquesta, int idxPregunta, List<String> novaPregunta)
            throws InvalidFormatEnquesta, EnquestaNoExisteixException, UsuariNoValid, IllegalArgumentException {
        if (novaPregunta == null)
            throw new IllegalArgumentException("La nova pregunta és nul·la");

        Enquesta enq;
        enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);

        if (idxPregunta < 0 || idxPregunta >= enq.getNumPreguntes())
            throw new IllegalArgumentException("L'índex de la pregunta és invàlid");

        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);

        if (u == null) {
            throw new UsuariNoValid("L'usuari no existeix");
        }

        if (u.isBlocked()) {
            throw new UsuariNoValid("L'usuari està vetat");
        }

        if (!u.esAdmin() && !u.esModerador()) {
            throw new UsuariNoValid("Credencials insuficients");
        }
        if (u.getId() < 0) {
            throw new UsuariNoValid("L'usuari és anònim");
        }

        if (u.esAdmin()) {
            if (!u.teEnquestaAdministrada(idEnquesta)) {
                throw new InvalidFormatEnquesta("L'usuari no administra l'enquesta");
            }
        }
        return modificarPreguntaEnquestaPrivate(idEnquesta, idxPregunta, novaPregunta);
    }

    /**
     * Funcio per a esborrar les respostes d'una enquesta d'un enquestat
     * 
     * @param idEnquesta  identificador de l'usuari que esborra la resposta
     * @param idEnquesta  identificador de l'enquesta
     * @param idEnquestat identificador de l'usuari que ha respost l'enquesta
     * @throws EnquestaNoExisteixException si l'enquesta no existeix
     * @throws UsuariNoHaResposEnquesta    si l'usuari no ha respost l'enquesta amb
     *                                     id donat
     */
    protected void esborrarRespostaEnquestaPrivate(Integer idEnquesta, int idEnquestat)
            throws EnquestaNoExisteixException, UsuariNoHaResposEnquesta {
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }
        for (Pregunta pregunta : enq.getPreguntesObj()) {
            Map<Integer, Resposta> respostes = pregunta.getRespostes();
            if (respostes.containsKey(idEnquestat)) {
                respostes.remove(idEnquestat);
                //// ctrlPersistencia.guardarEnquestes(ctrlDominiMantEnquesta.getEnquestesObj());
            }
        }
    }

    /**
     * Funcio per a esborrar les respostes d'una enquesta d'un enquestat
     * 
     * @param idUsuari
     * @param idEnquesta
     * @param idEnquestat
     * @throws UsuariNoValid
     * @throws InvalidFormatEnquesta
     * @throws UsuariNoHaResposEnquesta
     * @throws EnquestaNoExisteixException
     */
    public void esborrarRespostaEnquesta(int idUsuari, Integer idEnquesta, int idEnquestat)
            throws UsuariNoValid, InvalidFormatEnquesta, UsuariNoHaResposEnquesta, EnquestaNoExisteixException {
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        Usuari afectat = ctrlDominiMantUsuari.getUsuari(idEnquestat);
        if (u == null) {
            throw new UsuariNoValid("L'usuari no existeix");
        }

        if (u.getId() < 0) {
            throw new UsuariNoValid("L'usuari és anònim");
        }

        if (afectat == null) {
            throw new UsuariNoValid("L'usuari afectat no existeix");
        }

        if (u.isBlocked()) {
            throw new UsuariNoValid("L'usuari està vetat");
        }

        if (afectat.isBlocked()) {
            throw new UsuariNoValid("L'usuari afectat està vetat");
        }

        if (!u.esAdmin() && !u.esModerador()) {
            throw new UsuariNoValid("Credencials insuficients");
        }

        if (!afectat.teEnquestaRealitzada(idEnquesta)) {
            throw new InvalidFormatEnquesta("L'usuari enquestat no ha realitzat l'enquesta");
        }

        if (u.esAdmin()) {
            if (!u.teEnquestaAdministrada(idEnquesta)) {
                throw new UsuariNoValid("L'usuari no administra l'enquesta");
            }
        }

        esborrarRespostaEnquestaPrivate(idEnquesta, idEnquestat);
        afectat.demanarEliminarRealitzada(idEnquesta);
    }

     /**
     * Funcio per a modificar la resposta d'una pregunta d'una enquesta per un
     * usuari concret
     * 
     * @param idEnquesta   identificador de l'enquesta
     * @param idUsuari     identificador de l'usuari
     * @param idxPregunta  index de la pregunta a modificar
     * @param novaResposta nova resposta en format text
     * @return 1 si s'ha modificat correctament, -1 si l'enquesta no existeix, -2 si
     *         l'usuari no ha respost l'enquesta, -3 si l'índex de la pregunta és
     *         invàlid, -4 si la nova resposta no és vàlida
     * @throws EnquestaNoExisteixException si l'enquesta no existeix
     * @throws UsuariNoHaResposEnquesta    si l'usuari no ha respost l'enquesta amb
     *                                     id donat
     * @throws InvalidFormatResposta       si la nova resposta no és vàlida per a la
     *                                     pregunta
     * @throws InvalidFormatResposta       si l'índex de la pregunta és invàlid
     */
    protected int modificarRespostaEnquestaPrivate(Integer idEnquesta, int idUsuari, int idxPregunta,
            String novaResposta)
            throws EnquestaNoExisteixException, UsuariNoHaResposEnquesta, InvalidFormatResposta {
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null)
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");

        if (!enquestaTeRespostaUsuari(enq, idUsuari)) {
            throw new UsuariNoHaResposEnquesta(
                    "L'usuari amb id " + idUsuari + " no ha respost l'enquesta amb id " + idEnquesta + ".");
        }

        List<Resposta> respostes = enq.getRespostesUsuari(idUsuari);
        if (idxPregunta < 0 || idxPregunta >= respostes.size()) {
            throw new InvalidFormatResposta("L'índex de la pregunta " + idxPregunta + " és invàlid.");
        }

        Pregunta p = enq.getPreguntesObj().get(idxPregunta);
        int tipus = p.getTipus();
        if (!comprovarRespostaValid(p, novaResposta)) {
            throw new InvalidFormatResposta(
                    "La nova resposta '" + novaResposta + "' no és vàlida per a la pregunta: " + p.getText());
        }

        if (tipus == 0) {
            // numerica: convertir a double
            double valor = Double.parseDouble(novaResposta);
            Resposta r = new RespostaNumerica(valor);
            enq.modificarRespostaUsuari(idUsuari, idxPregunta, r);
        } else if (tipus == 1) {
            // unica: convertir a int
            int idxOpcio = p.getOpcions().indexOf(novaResposta);
            RespostaUnica r = new RespostaUnica();
            r.setResposta(idxOpcio);
            enq.modificarRespostaUsuari(idUsuari, idxPregunta, r);
        } else if (tipus == 2) {
            // ordenada: convertir a int
            int idxOpcio = p.getOpcions().indexOf(novaResposta);
            RespostaOrdenada r = new RespostaOrdenada();
            r.setResposta(idxOpcio);
            enq.modificarRespostaUsuari(idUsuari, idxPregunta, r);
        } else if (tipus == 3) {
            // multiple: convertir a llista d'int
            String[] parts = novaResposta.split(",");
            List<Integer> idxOpcions = new ArrayList<>();
            for (String part : parts) {
                int idx = p.getOpcions().indexOf(part.trim());
                idxOpcions.add(idx);
            }
            RespostaMultiple r = new RespostaMultiple();
            r.selecciona(idxOpcions);
            enq.modificarRespostaUsuari(idUsuari, idxPregunta, r);
        } else if (tipus == 4) {
            // lliure: text
            RespostaLliure r = new RespostaLliure(novaResposta);
            enq.modificarRespostaUsuari(idUsuari, idxPregunta, r);
        }
        return 1; // Èxit
    }

    /**
     * Funcio per a modificar la resposta d'una pregunta d'una enquesta
     * 
     * @param idExecutor
     * @param idEnquesta
     * @param idUsuari
     * @param idxPregunta
     * @param novaResposta
     * @return codi d'error
     * @throws UsuariNoHaResposEnquesta
     * @throws EnquestaNoExisteixException
     * @throws InvalidFormatResposta
     * @throws InvalidFormatEnquesta
     * @throws UsuariNoValid
     * @throws IllegalArgumentException
     */
    public int modificarRespostaEnquesta(int idExecutor, Integer idEnquesta, int idUsuari, int idxPregunta,
            String novaResposta)
            throws UsuariNoHaResposEnquesta, EnquestaNoExisteixException, InvalidFormatResposta, InvalidFormatEnquesta,
            UsuariNoValid, IllegalArgumentException {
        if (novaResposta == null || novaResposta.isEmpty())
            return -11;

        Usuari u = ctrlDominiMantUsuari.getUsuari(idExecutor);
        if (u == null) {
            throw new UsuariNoValid("L'usuari no existeix");
        }

        if (u.isBlocked()) {
            throw new UsuariNoValid("L'usuari està vetat");
        }

        if (u.getId() < 0) {
            throw new UsuariNoValid("L'usuari és anònim");
        }

        if (u.esEnquestador()) {
            throw new UsuariNoValid("Els enquestadors no poden modificar respostes");
        }
        if (u.esEnquestat()) {
            if (idExecutor != idUsuari) {
                throw new IllegalArgumentException(
                        "L'usuari enquestat només pot modificar les seves pròpies respostes");
            }
            if (!u.teEnquestaRealitzada(idEnquesta)) {
                throw new InvalidFormatEnquesta("L'usuari no ha realitzat l'enquesta");
            }
        }
        if (u.esAdmin()) {
            if (!u.teEnquestaAdministrada(idEnquesta) && !u.teEnquestaRealitzada(idEnquesta)) {
                throw new InvalidFormatEnquesta("L'usuari no administra ni ha realitzat l'enquesta");
            }
        }

        return modificarRespostaEnquestaPrivate(idEnquesta, idUsuari, idxPregunta, novaResposta);
    }

    // ================== Clustering ==================
    /**
     * Funcio per a realitzar clustering K-means sobre les respostes d'una enquesta
     * 
     * @param idUsuari
     * @param idEnquesta
     * @param k
     * @param maxIterations
     * @param algorisme
     * @return map amb l'identificador de la resposta i el clúster assignat
     * @throws EnquestaNoExisteixException
     * @throws KmeansExcepcio
     * @throws InvalidFormatEnquesta
     * @throws UsuariNoValid
     */
    public AbstractMap.SimpleEntry<Map<Integer, Integer>, Double> clustering(int idUsuari, Integer idEnquesta, int k,
            int maxIterations, String algorisme)
            throws EnquestaNoExisteixException, KmeansExcepcio, InvalidFormatEnquesta, UsuariNoValid {
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if (u == null) {
            throw new UsuariNoValid("L'usuari no existeix");
        }

        if (u.isBlocked()) {
            throw new UsuariNoValid("L'usuari està vetat");
        }

        if (u.getId() < 0) {
            throw new UsuariNoValid("L'usuari és anònim");
        }

        if (u.esEnquestat()) {
            if (!u.teEnquestaRealitzada(idEnquesta)) {
                throw new InvalidFormatEnquesta("L'usuari no ha realitzat l'enquesta");
            }
        } else if (u.esEnquestador()) {
            if (!u.teEnquestaAssignada(idEnquesta)) {
                throw new InvalidFormatEnquesta("L'usuari no té assignada l'enquesta");
            }
        }

        else if (u.esAdmin()) {
            if (!u.teEnquestaAdministrada(idEnquesta) && !u.teEnquestaRealitzada(idEnquesta)) {
                throw new InvalidFormatEnquesta("L'usuari no administra ni ha realitzat l'enquesta");
            }
        }

        ClusteringStrategy strategy;
        if (algorisme == "KMeans") {
            strategy = new KMeansStrategy(k, maxIterations, RANDOM);
        } else if (algorisme == "KMedoids") {
            strategy = new KMedoidsStrategy(k, maxIterations);
        } else {
            strategy = new KMeansStrategy(k, maxIterations, KMEANS_PLUS_PLUS);
        }

        try {
            Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
            if (enq == null) {
                throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
            }
            return strategy.executar(enq);
        } catch (Exception e) {
            throw new KmeansExcepcio("Error en l'execució de l'algorisme de clustering");
        }
    }

    // ================== Consultes Enquestes ==================

    /**
     * Funcio per a mostrar les enquestes i les seves preguntes i respostes
     * 
     * @param idEnquesta identificador de l'enquesta
     * @return result, llista de strings amb la informacio de l'enquesta
     * @throws EnquestaNoExisteixException si l'enquesta no existeix
     */
    public List<String> consultarEnquesta(Integer idEnquesta) throws EnquestaNoExisteixException {
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }
        List<String> result = new ArrayList<>();
        result.add("Enquesta: " + idEnquesta);
        result.add("Titol: " + enq.getTitol());
        result.add("Descripció: " + enq.getDescripcio());
        result.add("Nº de preguntes: " + enq.getNumPreguntes());
        return result;
    }

    /**
     * Funcio per a mostrar les enquestes amb preguntes
     * 
     * @param idEnquesta identificador de l'enquesta
     * @return result, llista de strings amb la informacio de l'enquesta i les seves
     *         preguntes
     * @throws EnquestaNoExisteixException si l'enquesta no existeix
     */
    public List<String> consultarEnquestaAmbPreguntes(Integer idEnquesta) throws EnquestaNoExisteixException {
        List<String> result = new ArrayList<>();
        result = consultarEnquesta(idEnquesta);

        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }
        List<String> preguntes = enq.getPreguntes();
        result.add("Preguntes:");
        for (String p : preguntes) {
            result.add("- " + p);
        }
        return result;
    }

    /**
     * Funcio per a consultar les preguntes d'una enquesta
     * 
     * @param idEnquesta
     * @return llista de strings amb les preguntes de l'enquesta
     * @throws EnquestaNoExisteixException
     */
    public List<String> consultarPreguntes(Integer idEnquesta) throws EnquestaNoExisteixException {
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);

        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }

        return enq.getTextPreguntes();
    }

    /**
     * Funcio per a consultar les enquestes amb preguntes i respostes
     * 
     * @param idEnquesta identificador de l'enquesta
     * @return result, llista de strings amb la informacio de l'enquesta, les seves
     *         preguntes i respostes
     * @throws EnquestaNoExisteixException si l'enquesta no existeix
     */
    public List<String> consultarRespostesEnquesta(Integer idEnquesta) throws EnquestaNoExisteixException {
        List<String> result = new ArrayList<>();
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);

        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }

        List<Pregunta> preguntes = enq.getPreguntesObj();
        for (Pregunta p : preguntes) {
            result.add("Pregunta: " + p.getText());
            List<String> opcions = p.getOpcions();
            if (opcions != null) {
                result.add("Opcions:");
                for (String opcio : opcions) {
                    result.add("  * Opció: " + opcio);
                }
            }
            Map<Integer, Resposta> respostes = p.getRespostes();
            for (Map.Entry<Integer, Resposta> entry : respostes.entrySet()) {
                result.add("  - Usuari ID: " + entry.getKey() + ", Resposta: " + entry.getValue().getText(opcions));
            }
        }
        return result;
    }

   /**
     * Funcio que consulta i retorna una llista d'una esquesta idEnquesta amb les
     * seves preguntes i respostes
     * 
     * @return una llista d'una esquesta idEnquesta amb les seves preguntes i
     *         respostes
     * @throws EnquestaNoExisteixException si l'enquesta no existeix
     */
    public List<String> consultarEnquestesAmbPreguntesIRespostes(int idUsuari) throws EnquestaNoExisteixException {
        int numEnquestes = ctrlDominiMantEnquesta.getNumEnquestes();
        List<String> result = new ArrayList<>();
        Map<Integer, Enquesta> enquestes = ctrlDominiMantEnquesta.getEnquestesObj();
        result.add("Numero d'enquesta: " + numEnquestes);
        for (Integer idEnquesta : enquestes.keySet()) {
            Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
            if (enq == null) {
                throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
            }

            // FILTER: If user is registered (idUsuari != -1) and has answered, skip this
            // survey.
            if (idUsuari != -1 && enquestaTeRespostaUsuari(enq, idUsuari)) {
                continue;
            }

            result.add("ID: " + idEnquesta + " - " + enq.getTitol());

            List<Pregunta> preguntes = enq.getPreguntesObj();
            for (Pregunta p : preguntes) {
                result.add("Pregunta: " + p.getText());
                List<String> opcions = p.getOpcions();
                if (opcions != null) {
                    result.add("Opcions:");
                    for (String opcio : opcions) {
                        result.add("  * Opció: " + opcio);
                    }
                }
                Map<Integer, Resposta> respostes = p.getRespostes();
                for (Map.Entry<Integer, Resposta> entry : respostes.entrySet()) {
                    result.add("  - Usuari ID: " + entry.getKey() + ", Resposta: " + entry.getValue().getText(opcions));
                }
            }
        }
        return result;
    }

    /**
     * Funcio per a obtenir les respostes d'una enquesta
     * 
     * @param idEnquesta
     * @return llista de strings amb les respostes de l'enquesta
     */
    public List<String> obtenirRespostesEnquesta(int idEnquesta) {
        Enquesta e = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);

        List<String> out = new ArrayList<>();
        out.add("=== RESPOSTES ENQUESTA " + idEnquesta + " ===");

        for (Pregunta p : e.getPreguntesObj()) {
            out.add("");
            out.add("Pregunta: " + p.getText());

            if (p.getRespostes().isEmpty()) {
                out.add("  (sense respostes)");
            } else {
                for (var entry : p.getRespostes().entrySet()) {
                    int idUsuari = entry.getKey();
                    Resposta r = entry.getValue();
                    out.add("  • Usuari " + idUsuari + ": " + r.getText(p.getOpcions()));
                }
            }
        }

        return out;
    }

    /**
     * Funcio per a obtenir les enquestes administrades per un usuari
     * 
     * @param idUsuari
     * @return llista de strings amb les enquestes administrades
     */
    public List<String> obtenirEnquestesAdministrades(int idUsuari) {
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);

        Map<Integer, Enquesta> map = new HashMap<>();
        if (u.esModerador()){
            map = ctrlDominiMantEnquesta.getEnquestesObj();
        }
        else{
            map = u.getRol().getEnquestesAdministrades();
        }

        List<String> resultat = new ArrayList<>();

        for (Enquesta e : map.values()) {
            resultat.add("ID: " + e.getId() + " - " + e.getTitol());
        }
        return resultat;
    }

    /**
     * Funcio per a comprovar si una enquesta té resposta d'un usuari
     * 
     * @param enq      Enquesta
     * @param idUsuari identificador de l'usuari
     * @return true si l'usuari ha respost l'enquesta, false en cas contrari
     */
    private boolean enquestaTeRespostaUsuari(Enquesta enq, int idUsuari) {
        return enq.participa(idUsuari);
    }

    //==============================================================
    // Gestió d'usuaris
    //==============================================================

    /**
     * Funcio per a crear un usuari enquestat
     * 
     * @param nomUsuari Nom de l'usuari
     * @param password  Contrasenya de l'usuari
     * @param email     Email de l'usuari
     * @return id de l'usuari creat
     * @throws UsuariNoValid
     * @throws IllegalArgumentException
     */
    public int crearUsuariEnquestat(String nomUsuari, String password, String email)
            throws UsuariNoValid, IllegalArgumentException {
        if (nomUsuari == null || nomUsuari.isEmpty() || password == null || password.isEmpty() || email.isEmpty()
                || email == null) {
            throw new IllegalArgumentException("Dades invàlides");
        }

        if (ctrlDominiMantUsuari.existeixUsuari(nomUsuari)) {
            throw new UsuariNoValid("El nom d'usuari ja existeix");
        }

        if (!checkRequerimentsPassword(password))
            throw new IllegalArgumentException("Contrasenya no compleix els requisits");

        if (ctrlDominiMantUsuari.emailUsat(email))
            throw new IllegalArgumentException("Email ja està en ús");

        int id = ctrlDominiMantUsuari.getNouID();
        // D'alguna forma s'ha de decidir el rol per enviar-lo, es a dir rol es Admin,
        // esnquestat o enquestador, es fa amb un if
        UsuariState rol = new EnquestatState();
        Usuari nouEnquestador = new Usuari(id, nomUsuari, password, email, rol);
        ctrlDominiMantUsuari.afegirUsuari(nouEnquestador);
        return id;
    }

    /**
     * Funcio per a crear un usuari enquestador
     * 
     * @param nomUsuari
     * @param password
     * @param email
     * @return id de l'usuari creat
     * @throws UsuariNoValid
     * @throws IllegalArgumentException
     */
    public int crearUsuariEnquestador(String nomUsuari, String password, String email)
            throws UsuariNoValid, IllegalArgumentException {
        if (nomUsuari == null || nomUsuari.isEmpty() || password == null || password.isEmpty() || email.isEmpty()
                || email == null) {
            throw new IllegalArgumentException("Dades invàlides");
        }

        if (ctrlDominiMantUsuari.existeixUsuari(nomUsuari)) {
            throw new UsuariNoValid("El nom d'usuari ja existeix");
        }

        if (!checkRequerimentsPassword(password))
            throw new IllegalArgumentException("Contrasenya no compleix els requisits");

        if (ctrlDominiMantUsuari.emailUsat(email))
            throw new IllegalArgumentException("Email ja està en ús");

        int id = ctrlDominiMantUsuari.getNouID();
        // D'alguna forma s'ha de decidir el rol per enviar-lo, es a dir rol es Admin,
        // esnquestat o enquestador, es fa amb un if
        UsuariState rol = new EnquestadorState();
        /////////////////////////
        Usuari nouEnquestador = new Usuari(id, nomUsuari, password, email, rol);
        ctrlDominiMantUsuari.afegirUsuari(nouEnquestador);
        return id;
    }

    /**
     * Funcio per a donar poders d'enquestador a un usuari
     * 
     * @param idExecutor
     * @param idEnquesta
     * @param nomTarget
     * @throws UsuariNoValid
     * @throws InvalidFormatEnquesta
     * @throws IllegalArgumentException
     */
    public void donarPodersEnquestador(int idExecutor, Integer idEnquesta, String nomTarget)
            throws UsuariNoValid, InvalidFormatEnquesta, IllegalArgumentException {
        if (nomTarget == null || nomTarget.isEmpty())
            throw new IllegalArgumentException("Dades invàlides");

        Usuari executor = ctrlDominiMantUsuari.getUsuari(idExecutor);
        if (executor == null || executor.getId() < 0) {
            throw new UsuariNoValid("L'executor no és vàlid.");
        }

        if (executor.isBlocked()) {
            throw new UsuariNoValid("L'usuari amb id " + idExecutor + " està vetat i no pot assignar enquestadors.");
        }

        if (!executor.esAdmin() && !executor.esModerador()) {
            throw new UsuariNoValid("L'usuari amb id " + idExecutor + " no té permisos per assignar enquestadors.");
        }

        Enquesta enq;
        enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);

        if (executor.esAdmin()) {
            if (!executor.teEnquestaAdministrada(idEnquesta)) {
                throw new InvalidFormatEnquesta(
                        "L'admin no administra aquesta enquesta i no pot assignar enquestadors.");
            }
        }

        Usuari target = ctrlDominiMantUsuari.getUsuariPerNom(nomTarget);

        if (target == null || target.getId() < 0) {
            throw new UsuariNoValid("El target no és vàlid.");
        }

        if (target.isBlocked()) {
            throw new UsuariNoValid("L'usuari destinatari '" + nomTarget + "' està vetat i no se li pot donar poders.");
        }

        if (target.esAdmin() || target.esModerador()) {
            throw new UsuariNoValid("No es pot canviar el rol d'un Admin o Moderador.");
        }

        if (target.esEnquestat()) {
            target.cambiarARolEnquestador();
        }

        target.demanarAfegirEnquestaAssignada(enq);
    }

    /**
     * Funcio per a donar poders d'administrador a un usuari
     * 
     * @param idExecutor
     * @param idEnquesta
     * @param nomTarget
     * @throws UsuariNoValid
     * @throws InvalidFormatEnquesta
     * @throws IllegalArgumentException
     */
    public void donarPodersAdmin(int idExecutor, Integer idEnquesta, String nomTarget)
            throws UsuariNoValid, InvalidFormatEnquesta, IllegalArgumentException {
        if (nomTarget == null || nomTarget.isEmpty())
            throw new IllegalArgumentException("Dades invàlides");

        Usuari executor = ctrlDominiMantUsuari.getUsuari(idExecutor);
        if (executor == null || executor.getId() < 0) {
            throw new UsuariNoValid("L'executor no és vàlid.");
        }

        if (executor.isBlocked()) {
            throw new UsuariNoValid("L'usuari amb id " + idExecutor + " està vetat i no pot nomenar administradors.");
        }

        if (!executor.esAdmin() && !executor.esModerador()) {
            throw new UsuariNoValid("L'usuari amb id " + idExecutor + " no té permisos per nomenar administradors.");
        }

        Enquesta enq;
        enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);

        if (executor.esAdmin()) {
            if (!executor.teEnquestaAdministrada(idEnquesta)) {
                throw new InvalidFormatEnquesta(
                        "L'admin no administra aquesta enquesta i no pot nomenar administradors.");
            }
        }

        Usuari target = ctrlDominiMantUsuari.getUsuariPerNom(nomTarget);

        if (target == null || target.getId() < 0) {
            throw new UsuariNoValid("El target no és vàlid.");
        }

        if (target.isBlocked()) {
            throw new UsuariNoValid("L'usuari destinatari '" + nomTarget + "' està vetat i no se li pot donar poders.");
        }

        if (target.esModerador()) {
            throw new UsuariNoValid("No es pot canviar el rol d'un Moderador.");
        }

        if (target.teEnquestaAdministrada(idEnquesta)) {
            throw new UsuariNoValid("L'usuari ja és administrador d'aquesta enquesta.");
        }

        if (!target.esAdmin()) {
            target.cambiarARolAdmin();
        }

        target.demanarAfegirEnquestaAdministrada(enq);
    }

    /**
     * Funcio per a vetar un usuari
     * 
     * @param idExecutor
     * @param nomObjectiu
     * @throws UsuariNoValid
     */
    public void vetarUsuari(int idExecutor, String nomObjectiu)
            throws UsuariNoValid {
        if (nomObjectiu == null || nomObjectiu.isEmpty())
            throw new UsuariNoValid("Nom d'usuari objectiu invàlid");
        Usuari executor = ctrlDominiMantUsuari.getUsuari(idExecutor);
        if (executor == null || executor.getId() < 0) {
            throw new UsuariNoValid("L'usuari executor no existeix o es anònim");
        }

        if (executor.isBlocked()) {
            throw new UsuariNoValid("L'usuari executor està vetat");
        }

        if (!executor.esModerador()) {
            throw new UsuariNoValid("Credencials insuficients");
        }
        Usuari objectiu = ctrlDominiMantUsuari.getUsuariPerNom(nomObjectiu);
        if (objectiu == null)
            throw new UsuariNoValid("L'usuari objectiu no existeix"); // l'usuari objectiu no existeix
        if (objectiu.esModerador())
            throw new UsuariNoValid("No es pot vetar a un moderador"); // no es pot vetar un moderador
        ctrlDominiMantUsuari.vetarUsuari(objectiu.getId());
    }

    /**
     * Funcio per a desvetar un usuari
     * 
     * @param idExecutor
     * @param nomObjectiu
     * @throws UsuariNoValid
     */
    public void desvetarUsuari(int idExecutor, String nomObjectiu) throws UsuariNoValid {
        if (nomObjectiu == null || nomObjectiu.isEmpty())
            throw new UsuariNoValid("Nom d'usuari objectiu invàlid");
        Usuari executor = ctrlDominiMantUsuari.getUsuari(idExecutor);
        if (executor == null || executor.getId() < 0) {
            throw new UsuariNoValid("L'usuari executor no existeix o es anònim");
        }

        if (executor.isBlocked()) {
            throw new UsuariNoValid("L'usuari executor està vetat");
        }

        if (!executor.esModerador()) {
            throw new UsuariNoValid("Credencials insuficients");
        }
        Usuari objectiu = ctrlDominiMantUsuari.getUsuariPerNom(nomObjectiu);
        if (objectiu == null)
            throw new UsuariNoValid("L'usuari objectiu no existeix"); // l'usuari objectiu no existeix
        ctrlDominiMantUsuari.desvetarUsuari(objectiu.getId());
    }

    // ================== Consultes ==================

    /**
     * Funcio per a obtenir els noms d'usuari
     * 
     * @return llista de strings amb els noms d'usuari
     */
    public List<String> obtenirNomUsuaris() {
        return ctrlDominiMantUsuari.obtenirNomsUsuaris();
    }

    /**
     * Funcio per a obtenir l'id d'un usuari a partir del seu nom
     * 
     * @param nomUsuari
     * @return id de l'usuari, o -1 si no existeix
     */
    public int getIdUsuariPerNom(String nomUsuari) {
        Usuari u = ctrlDominiMantUsuari.getUsuariPerNom(nomUsuari);
        if (u == null)
            return -1;
        return u.getId();
    }

    /**
     * Funcio per a consultar les respostes d'una enquesta per un usuari concret
     * 
     * @param idEnquesta identificador de l'enquesta
     * @param idUsuari   identificador de l'usuari
     * @return respostesStr, llista de strings amb les preguntes i respostes de
     *         l'usuari
     * @throws EnquestaNoExisteixException si l'enquesta no existeix
     * @throws UsuariNoHaResposEnquesta    si l'usuari no ha respost l'enquesta amb
     *                                     id donat
     */
    public List<String> getRespostesEnquestaPerUsuari(Integer idEnquesta, int idUsuari)
            throws NoSuchElementException, UsuariNoHaResposEnquesta {
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        List<String> respostesStr = new ArrayList<>();
        List<Resposta> respostes = enq.getRespostesUsuari(idUsuari);
        if (respostes == null) {
            throw new UsuariNoHaResposEnquesta(
                    "L'usuari amb id " + idUsuari + " no ha respost l'enquesta amb id " + idEnquesta + ".");
        }
        int idx = 0;
        for (Pregunta p : enq.getPreguntesObj()) {
            respostesStr.add("Pregunta: " + p.getText());
            List<String> opcions = p.getOpcions();
            if (opcions != null) {
                respostesStr.add("Opcions:");
                for (String opcio : opcions) {
                    respostesStr.add("  * Opció: " + opcio);
                }
            }
            Resposta r = respostes.get(idx);
            respostesStr.add("Resposta: " + r.getText(opcions));
            idx++;
        }
        return respostesStr;
    }   

    /**
     * Funcio per a consultar el perfil d'un usuari
     * 
     * @param id
     * @return llista de strings amb la informacio del perfil de l'usuari
     */
    public List<String> consultarPerfil(int id) {
        Usuari us = ctrlDominiMantUsuari.getUsuari(id);
        List<String> perfil = new ArrayList<>();
        perfil.add("Id de l'usuari: " + us.getId());
        perfil.add("Nom de l'usuari: " + us.getUsuari());
        perfil.add("Email: " + us.getEmail());
        return perfil;
    }


    //==============================================================
    // Funcions per comprovar valideses
    //==============================================================

    /**
     * Funcio per a comprovar si una resposta és vàlida per a una pregunta
     * 
     * @param p        Pregunta
     * @param resposta Resposta en format text
     * @return true si la resposta es valida, false en cas contrari
     */
    private boolean comprovarRespostaValid(Pregunta p, String resposta) {
        int tipus = p.getTipus();
        List<String> opcions = p.getOpcions();
        switch (tipus) {
            case 0: // NUMERICA
                try {
                    Double.parseDouble(resposta);
                    return true; // És vàlida
                } catch (NumberFormatException e) {
                    return false; // No és vàlida
                }
            case 1: // UNICA
                try {
                    int idx = Integer.parseInt(resposta);
                    if (idx >= 0 && idx < opcions.size()) {
                        return true; // És vàlida
                    } else {
                        return false; // No és vàlida
                    }
                } catch (NumberFormatException e) {
                    return false; // No és vàlida
                }
            case 2: // ORDENADA
                try {
                    int idx = Integer.parseInt(resposta);
                    if (idx >= 0 && idx < opcions.size()) {
                        return true; // És vàlida
                    } else {
                        return false; // No és vàlida
                    }
                } catch (NumberFormatException e) {
                    return false; // No és vàlida
                }
            case 3: // MULTIPLE
                if (opcions.contains(resposta)) {
                    return true; // És vàlida
                } else {
                    return false; // No és vàlida
                }
            case 4: // LLIURE
                return true; // Sempre és vàlida
            default:
                return false; // Tipus desconegut
        }
    }

    /**
     * Funcio per a comprovar els requeriments d'una contrasenya
     * 
     * @param password
     * @return true si compleix els requeriments, false en cas contrari
     */
    private boolean checkRequerimentsPassword(String password) {
        return password.length() > 5;
    }

    /**
     * Funcio per a iniciar sessio
     * 
     * @param nomUsuari
     * @param password
     * @return codi d'error
     */
    public int iniciarSessio(String nomUsuari, String password) throws IllegalArgumentException {
        if (nomUsuari == null || nomUsuari.isEmpty())
            throw new IllegalArgumentException("Has d'introduir un nom d'usuari");

        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Has d'introduir una password");

        return ctrlDominiMantUsuari.iniciarSessio(nomUsuari, password);
    }

    // ===================================
    // NOVES FUNCIONALITATS (USER REQ)
    // ===================================

    /**
     * Exporta l'estructura de l'enquesta (Preguntes i opcions) sense les respostes
     * dels usuaris.
     * 
     * @param idEnquesta identificador de l'enquesta
     * @return llista de strings amb la informació de l'enquesta i les preguntes
     * @throws EnquestaNoExisteixException
     */
    public List<String> exportarEnquestaSenseRespostes(Integer idEnquesta) throws EnquestaNoExisteixException {
        if (idEnquesta == -1)
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }
        List<String> exportat = new ArrayList<>();
        exportat.add("==== Informacio enquesta ====");
        exportat.add("ID: " + enq.getId());
        exportat.add("Titol: " + enq.getTitol());
        exportat.add("Descripcio: " + enq.getDescripcio());
        exportat.add("Creador ID: " + enq.getCreador());
        exportat.add("Numero de preguntes: " + enq.getNumPreguntes());
        exportat.add("");

        List<Pregunta> preguntes = enq.getPreguntesObj();
        exportat.add("==== Preguntes ====");
        for (int i = 0; i < preguntes.size(); i++) {
            Pregunta p = preguntes.get(i);
            exportat.add("Pregunta " + (i + 1) + ": " + p.getText());

            List<String> opcions = p.getOpcions();
            if (opcions != null && !opcions.isEmpty()) {
                exportat.add("Opcions:");
                for (int j = 0; j < opcions.size(); j++) {
                    exportat.add("  * Opció " + (j + 1) + ": " + opcions.get(j));
                }
            }
            exportat.add("");
        }
        return exportat;
    }

    /**
     * Exporta les preguntes i les respostes d'un usuari específic a una enquesta.
     * 
     * @param idEnquesta identificador de l'enquesta
     * @param idUsuari   identificador de l'usuari
     * @return llista de strings amb les preguntes i les respostes de l'usuari
     * @throws EnquestaNoExisteixException
     * @throws UsuariNoValid
     */
    public List<String> exportarRespostesUsuari(Integer idEnquesta, int idUsuari)
            throws EnquestaNoExisteixException, UsuariNoValid {
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null)
            throw new EnquestaNoExisteixException("L'enquesta no existeix");

        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if (u == null)
            throw new UsuariNoValid("L'usuari no existeix");

        List<String> result = new ArrayList<>();
        result.add("=== RESPOSTES DE L'USUARI " + u.getUsuari() + " (ID: " + idUsuari + ") ===");
        result.add("Enquesta: " + enq.getTitol());
        result.add("");

        List<Pregunta> preguntes = enq.getPreguntesObj();
        int idx = 1;

        for (Pregunta p : preguntes) {
            result.add("Pregunta " + idx + ": " + p.getText());
            List<String> opcions = p.getOpcions();
            if (opcions != null && !opcions.isEmpty()) {
                result.add("Opcions:");
                for (String op : opcions)
                    result.add(" * " + op);
            }

            Map<Integer, Resposta> respostes = p.getRespostes();
            if (respostes.containsKey(idUsuari)) {
                String rText = respostes.get(idUsuari).getText(opcions);
                result.add("-> RESPOSTA: " + rText);
            } else {
                result.add("-> (Sense resposta)");
            }
            result.add("");
            idx++;
        }
        return result;
    }

    /**
     * Obté les respostes d'un usuari en format cru (raw) per a ser processades (ex:
     * pre-emplenar formularis).
     * 
     * @param idEnquesta identificador de l'enquesta
     * @param idUsuari   identificador de l'usuari
     * @return llista de strings amb els valors de les respostes
     * @throws EnquestaNoExisteixException
     */
    public List<String> getRespostesUsuariList(Integer idEnquesta, int idUsuari) throws EnquestaNoExisteixException {
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null)
            throw new EnquestaNoExisteixException("Enquesta no trobada");

        List<Pregunta> preguntes = enq.getPreguntesObj();
        List<String> result = new ArrayList<>();

        for (Pregunta p : preguntes) {
            Map<Integer, Resposta> map = p.getRespostes();
            if (!map.containsKey(idUsuari)) {
                result.add("");
                continue;
            }

            Resposta r = map.get(idUsuari);
            if (r instanceof RespostaUnica) {
                Integer v = ((RespostaUnica) r).getResposta();
                result.add(v == null ? "" : v.toString());
            } else if (r instanceof RespostaMultiple) {
                List<Integer> v = ((RespostaMultiple) r).getRespostes();
                if (v == null || v.isEmpty())
                    result.add("");
                else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < v.size(); i++) {
                        sb.append(v.get(i));
                        if (i < v.size() - 1)
                            sb.append(",");
                    }
                    result.add(sb.toString());
                }
            } else if (r instanceof RespostaNumerica) {
                Double v = ((RespostaNumerica) r).getValor();
                result.add(v == null ? "" : v.toString());
            } else if (r instanceof RespostaLliure) {
                String s = ((RespostaLliure) r).getResposta();
                result.add(s == null ? "" : s);
            } else if (r instanceof RespostaOrdenada) {
                Integer v = ((RespostaOrdenada) r).getResposta();
                result.add(v == null ? "" : v.toString());
            } else {
                result.add("");
            }
        }
        return result;
    }

    /**
     * Retorna les enquestes que un usuari ha respost.
     * 
     * @param idUsuari identificador de l'usuari
     * @return llista de strings amb la informacio de les enquestes
     * @throws UsuariNoValid
     */
    public List<String> obtenirEnquestesRespostesPerUsuari(int idUsuari) throws UsuariNoValid {
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if (u == null)
            throw new UsuariNoValid("Usuari no valid");

        List<String> result = new ArrayList<>();
        Map<Integer, Enquesta> enquestes = ctrlDominiMantEnquesta.getEnquestesObj();
        for (Enquesta enq : enquestes.values()) {
            if (u.teEnquestaRealitzada(enq.getId())) {
                result.add("ID: " + enq.getId() + " - " + enq.getTitol());
            }
        }
        return result;
    }

    /**
     * Modifica les respostes d'un usuari a una enquesta existent (esborra les
     * anteriors i posa les noves).
     * 
     * @param idUsuari   identificador de l'usuari
     * @param idEnquesta identificador de l'enquesta
     * @param respostes  llista amb les noves respostes
     * @throws EnquestaNoExisteixException
     * @throws UsuariNoValid
     * @throws InvalidFormatEnquesta
     * @throws InvalidFormatResposta
     * @throws UsuariNoHaResposEnquesta
     */
    public void modificarRespostaEnquesta(int idUsuari, int idEnquesta, List<String> respostes)
            throws EnquestaNoExisteixException, UsuariNoValid, InvalidFormatEnquesta, InvalidFormatResposta,
            UsuariNoHaResposEnquesta {

        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if (u == null || u.isBlocked())
            throw new UsuariNoValid("Usuari invalid");

        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null)
            throw new EnquestaNoExisteixException("Enquesta inexistente");

        esborrarRespostaEnquestaPrivate(idEnquesta, idUsuari);
        respondreEnquestaPrivate(idEnquesta, idUsuari, respostes);
    }

    /**
     * Funcio per a obtenir les enquestes segons el rol de l'usuari
     * @param idUsuari
     * @return llista de strings amb les enquestes segons el rol de l'usuari
     */
    public List<String> obtenirEnquestesPerRol(int idUsuari){
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if(u.esModerador()){
            Map<Integer, Enquesta> totes = ctrlDominiMantEnquesta.getEnquestesObj();

            List<String> resultat = new ArrayList<>();

            for (Enquesta e : totes.values()) {
                resultat.add("ID: " + e.getId() + " - " + e.getTitol());
            }
            return resultat;
        }
        return u.obtenirEnquestesPerRol();
    }
}


