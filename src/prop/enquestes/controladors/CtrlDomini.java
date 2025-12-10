package prop.enquestes.controladors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prop.enquestes.domini.*;
import prop.enquestes.excepcions.EnquestaNoExisteixException;
import prop.enquestes.excepcions.FileNotFound;
import prop.enquestes.excepcions.InvalidFormatEnquesta;
import prop.enquestes.excepcions.InvalidFormatResposta;
import prop.enquestes.excepcions.KmeansExcepcio;
import prop.enquestes.excepcions.UsuariNoHaResposEnquesta;
//import prop.enquestes.persistencia.CtrlPersddistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CtrlDomini {
    private CtrlDominiMantEnquesta ctrlDominiMantEnquesta;
    private CtrlDominiMantUsuari ctrlDominiMantUsuari;
    private prop.enquestes.persistencia.GestorPersistencia gestorPersistencia;

    /**
     * Funcio constructora de CtrlDomini
     *
     */
    public CtrlDomini() {
        ctrlDominiMantEnquesta = new CtrlDominiMantEnquesta();
        ctrlDominiMantUsuari = new CtrlDominiMantUsuari();
        gestorPersistencia = new prop.enquestes.persistencia.GestorPersistencia();

        // Load data on startup
        ctrlDominiMantUsuari.setUsuaris(gestorPersistencia.carregarUsuaris());
        ctrlDominiMantEnquesta.setEnquestes(gestorPersistencia.carregarEnquestes());
    }

    /**
     * Retorna el controlador de domini de manteniment d'usuaris
     * @return CtrlDominiMantUsuari
     */
    public CtrlDominiMantUsuari getCtrlDominiMantUsuari() {
        return ctrlDominiMantUsuari;
    }

    /**
     * Retorna el controlador de domini de manteniment d'enquestes
     * @return CtrlDominiMantEnquesta
     */
    public CtrlDominiMantEnquesta getCtrlDominiMantEnquesta() {
        return ctrlDominiMantEnquesta;
    }

    /**
     * Funcio per a guardar les dades
     */
    public void guardarDades() {
        gestorPersistencia.guardarUsuaris(ctrlDominiMantUsuari.getUsuaris());
        gestorPersistencia.guardarEnquestes(ctrlDominiMantEnquesta.getEnquestesObj());
    }

    /**
     * Funcio per a vetar un usuari
     * @param idExecutor
     * @param nomObjectiu
     * @return 1 si s'ha vetat correctament, -1 si l'usuari executor o objectiu no existeix, -2 si l'usuari executor no és moderador
     */
    public int vetarUsuari(int idExecutor, String nomObjectiu) {
        if (nomObjectiu == null || nomObjectiu.isEmpty()) return -11;
        Usuari executor = ctrlDominiMantUsuari.getUsuari(idExecutor);
        if(executor == null || executor.getId() < 0){
            return -1; // l'usuari executor no existeix o es anònim
        }

        if(executor.isBlocked()){
            return -10; //usuari vetat
        }

        if(!executor.esModerador()){
            return -2; //credencials insuficients
        }
        Usuari objectiu = ctrlDominiMantUsuari.getUsuariPerNom(nomObjectiu);
        if(objectiu == null) return -1; // l'usuari objectiu no existeix
        if(objectiu.esModerador()) return -9; // no es pot vetar un moderador
        ctrlDominiMantUsuari.vetarUsuari(objectiu.getId());
        return 1; // èxit
    }

    /**
     * Funcio per a desvetar un usuari
     * @param idExecutor
     * @param nomObjectiu
     * @return 1 si s'ha desvetat correctament, -1 si l'usuari executor o objectiu no existeix, -2 si l'usuari executor no és moderador
     */
    public int desvetarUsuari(int idExecutor, String nomObjectiu) {
        if (nomObjectiu == null || nomObjectiu.isEmpty()) return -11;
        Usuari executor = ctrlDominiMantUsuari.getUsuari(idExecutor);
        if(executor == null || executor.getId() < 0){
            return -1; // l'usuari executor no existeix o es anònim
        }

        if(executor.isBlocked()){
            return -10; //usuari vetat
        }

        if(!executor.esModerador()){
            return -3; //credencials insuficients
        }
        Usuari objectiu = ctrlDominiMantUsuari.getUsuariPerNom(nomObjectiu);
        if(objectiu == null) return -1; // l'usuari objectiu no existeix
        ctrlDominiMantUsuari.desvetarUsuari(objectiu.getId());
        return 1; // èxit
    }

    /**
     * Retorna les preguntes de l'enquesta amb id donat
     * 
     * @param idEnquesta Identificador de l'enquesta
     * @return Llista de preguntes en format text
     * @throws EnquestaNoExisteixException si l'enquesta no existeix
     */
    ////////////////// Cas d'us - Respondre enquesta //////////////////////
    public List<String> getPreguntes(Integer idEnquesta) throws EnquestaNoExisteixException { // Final
        return this.ctrlDominiMantEnquesta.getPreguntesEnquesta(idEnquesta);
    }

    /**
     * Funcio per a respondre una enquesta
     * 
     * @param idEnquesta      Identificador de l'enquesta a respondre
     * @param idUsuari        Identificador de l'usuari que respon l'enquesta
     * @param respostesUsuari Llista de respostes
     */
    protected void respondreEnquestaPrivate(Integer idEnquesta, int idUsuari, List<String> respostesUsuari)
            throws EnquestaNoExisteixException, InvalidFormatEnquesta {
        Enquesta enq = this.ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }
        enq.afegeixResposta(idUsuari, respostesUsuari);
        gestorPersistencia.guardarEnquestes(ctrlDominiMantEnquesta.getEnquestesObj());

    }

    /**
     * Funcio per a respondre una enquesta
     * @param idEnquesta
     * @param idUsuari
     * @param respostesUsuari
     * @throws EnquestaNoExisteixException
     * @throws InvalidFormatEnquesta
     */
    public int respondreEnquesta(Integer idEnquesta, int idUsuari, List<String> respostesUsuari) {
        if (respostesUsuari == null) return -11;
        if (idUsuari == -1) {
            try {
                respondreEnquestaPrivate(idEnquesta, -1, respostesUsuari);
                return 1; // Èxit
            } catch (InvalidFormatEnquesta e) { return -7; } // Format invàlid
            catch (EnquestaNoExisteixException e) { return -4; } // Enquesta no existeix
        }

        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if (u == null) return -1; // Usuari no existeix
        if (u.isBlocked()) return -10; // Usuari vetat

        if (!u.esAdmin() && !u.esEnquestat()) return -3; // Credencials insuficients

        Enquesta enq;
        try { enq = this.ctrlDominiMantEnquesta.getEnquesta(idEnquesta); }
        catch (Exception e) { return -4; /*Enquesta no existeix*/}

        if (u.teEnquestaRealitzada(idEnquesta)) return -5; // Enquesta ja realitzada

        try {
            respondreEnquestaPrivate(idEnquesta, idUsuari, respostesUsuari);
        } catch (InvalidFormatEnquesta e) { return -7; } // Format invàlid
        catch (EnquestaNoExisteixException e) { return -4; } // Enquesta no existeix

        u.demanarAfegirEnquestaRealitzada(enq);
        return 1; // Èxit
    }

    /**
     * Funcio per a crear una enquesta
     * 
     * @param titol      Titol de l'enquesta
     * @param descripcio Descripcio de l'enquesta
     * @param idCreador  Identificador de l'usuari creador de l'enquesta
     * @param preguntes  Llista de preguntes
     */
    /////////////////////// Cas d'us - Crear enquesta //////////////////////
    protected void crearEnquestaPrivate(String titol, String descripcio, int idCreador, List<String> preguntes)
            throws InvalidFormatEnquesta { // Final
        this.ctrlDominiMantEnquesta.novaEnquesta(titol, descripcio, idCreador, preguntes);
        gestorPersistencia.guardarEnquestes(ctrlDominiMantEnquesta.getEnquestesObj());
    }

    /**
     * Funcio per a crear una enquesta
     * @param titol
     * @param descripcio
     * @param idCreador
     * @param preguntes
     * @return 1 si s'ha creat correctament, 0 si hi ha un error en el format de l'enquesta
     */
    public int crearEnquesta(String titol, String descripcio, int idCreador, List<String> preguntes) {
        if (titol == null || titol.isEmpty() || descripcio == null || preguntes == null) return -11;

        Usuari u = ctrlDominiMantUsuari.getUsuari(idCreador);
        if (u == null) return -1; // Usuari no existeix

        if (u.getId() < 0) return -1; // Usuari anònim no pot crear enquestes

        if (u.isBlocked()) return -10; // Usuari vetat

        if (u.esEnquestador()) return -3; // Credencials insuficients

        try {
            crearEnquestaPrivate(titol, descripcio, idCreador, preguntes);
        } catch (InvalidFormatEnquesta e) {
            return -7; // Format invàlid
        }

        Enquesta enq = ctrlDominiMantEnquesta.getUltimaEnquestaCreada();
        if(u.esEnquestat()){
            u.cambiarARolAdmin();
        }
        u.demanarAfegirEnquestaAdministrada(enq);
        return 1; // Èxit
    }

    /**
     * Funcio per a exportar una enquesta
     * 
     * @param idEnquesta de l'enquesta a exportar
     * @return Llista de strings amb la informacio de l'enquesta
     * @throws EnquestaNoExisteixException si l'enquesta no existeix
     */
    /////////////////////// Cas d'us - Exportar enquesta //////////////////
    protected List<String> exportarEnquestaPrivate(Integer idEnquesta) throws EnquestaNoExisteixException {
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
     * @param idEnquesta
     * @param idUsuari
     * @return Llista de strings amb la informacio de l'enquesta
     * @throws EnquestaNoExisteixException
     */
    public List<String> exportarEnquesta(Integer idEnquesta, int idUsuari) {
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if (u == null || u.getId() < 0) return null; // Error
        if (u.isBlocked()) return null; // Error

        try {
            return exportarEnquestaPrivate(idEnquesta);
        } catch (EnquestaNoExisteixException e) {
            return null; // Error
        }
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
    ////////////////////// Cas d'us - Importar respostes ////////////////////
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
            return -8; // Error llegint el fitxer
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
     * @param idUsuari   identificador de l'usuari que importa les respostes
     * @param path       origen del fitxer
     * @param idEnquesta identificador de l'enquesta
     * @return llista amb el resultat
     */
    public int importarRespostes(int idUsuari, String path, Integer idEnquesta) throws EnquestaNoExisteixException, InvalidFormatEnquesta {
        if (path == null || path.isEmpty()) return -11;
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if(u == null){
            return -1; // L'usuari no existeix
        }

        if(u.isBlocked()){
            return -10; // Usuari vetat
        }

        if (u.esEnquestat()) {
            return -3; // Credencials insuficients
        }

        if (u.getId() < 0) {
            return -1; // L'usuari és anònim
        }

        if (u.esAdmin()) {
            if (!u.teEnquestaAdministrada(idEnquesta)) {
                return -6; // L'usuari no administra l'enquesta
            }
        } else if (u.esEnquestador()) {
            if (!u.teEnquestaAssignada(idEnquesta)) {
                return -6; // L'usuari no té assignada l'enquesta
            }
        }
        //no hay codigo de exito como tal, el codigo de exito es numRespostes > 0
        try {
            return importarRespostesPrivate(path, idEnquesta);
        } catch (EnquestaNoExisteixException e) { return -4; }
        catch (InvalidFormatEnquesta e) { return -7; }
    }

    //////////////// Persistencia
    /**
     * Funcio per a exportar les respostes d'una enquesta a un fitxer
     * @param idUsuari
     * @param idEnquesta
     * @param path
     * @throws Exception
     */
    public int exportarRespostesAFitxer(int idUsuari, int idEnquesta, String path) throws Exception {
        if (path == null || path.isEmpty()) return -11;
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if(u == null){
            return -1; // L'usuari no existeix
        }

        if(u.isBlocked()){
            return -10; // Usuari vetat
        }

        if (u.getId() < 0) {
            return -1; // L'usuari és anònim
        }

        try {
            List<String> data = exportarRespostesEnquesta(idEnquesta);
            // ctrlPersistencia.guardarFitxerText(path, data);
            return 1; // Èxit
        } catch (Exception e) { return -4; } // Error general o enquesta no existeix
    }

    /**
     * Funcio per a exportar les respostes d'una enquesta
     * @param idEnquesta
     * @return Llista de strings amb la informacio de les respostes de l'enquesta
     * @throws EnquestaNoExisteixException
     */
    public List<String> exportarRespostesEnquesta(int idEnquesta) throws EnquestaNoExisteixException {
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
     * @param idUsuari
     * @param idEnquesta
     * @throws EnquestaNoExisteixException
     */
    public int eliminarEnquesta(int idUsuari, Integer idEnquesta) throws EnquestaNoExisteixException {
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if (u == null) {
            return -1; // L'usuari no existeix
        }

        if(u.isBlocked()){
            return -10; // Usuari vetat
        }

        if (!u.esAdmin() && !u.esModerador()) {
            return -3; // Credencials insuficients
        }

        if (u.getId() < 0) {
            return -1; // L'usuari és anònim
        }

        if (u.esAdmin()) {
            if (!u.teEnquestaAdministrada(idEnquesta)) {
                return -6; // L'usuari no administra l'enquesta
            }
        }

        try {
            eliminarEnquestaPrivate(idEnquesta);
        } catch (EnquestaNoExisteixException e) { return -4; }

        Map<Integer, Usuari> totsElsUsuaris = ctrlDominiMantUsuari.getUsuaris();
        for (Usuari afectarEsborrat : totsElsUsuaris.values()) {
            afectarEsborrat.demanarEliminarAdministrada(idEnquesta);
            afectarEsborrat.demanarEliminarAssignada(idEnquesta);
            afectarEsborrat.demanarEliminarRealitzada(idEnquesta);
        }

        return 1; // Èxit
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
     * @param idUsuari
     * @param idEnquesta
     * @param idxPregunta
     * @param novaPregunta
     * @return
     * @throws InvalidFormatEnquesta
     * @throws EnquestaNoExisteixException
     */
    public int modificarPreguntaEnquesta(int idUsuari, int idEnquesta, int idxPregunta, List<String> novaPregunta)
            throws InvalidFormatEnquesta, EnquestaNoExisteixException {
        if(novaPregunta == null) return -11;

        Enquesta enq;
        try {
            enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        } catch (Exception e) { return -4; } // Si falla getEnquesta, retorna -4

        if(idxPregunta < 0 || idxPregunta >= enq.getNumPreguntes()) return -11;

        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);

        if(u == null){
            return -1; // L'usuari no existeix
        }

        if(u.isBlocked()){
            return -10; // Usuari vetat
        }

        if (!u.esAdmin() && !u.esModerador()) {
            return -3; // Credencials insuficients
        }
        if (u.getId() < 0) {
            return -1; // L'usuari és anònim
        }

        if (u.esAdmin()) {
            if (!u.teEnquestaAdministrada(idEnquesta)) {
                return -6; // L'usuari no administra l'enquesta
            }
        }
        //ya devuelve 1 si es exit
        try {
            return modificarPreguntaEnquestaPrivate(idEnquesta, idxPregunta, novaPregunta);
        } catch (InvalidFormatEnquesta e) { return -7; }
        catch (EnquestaNoExisteixException e) { return -4; }
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
    ////////////////////// Cas d'us - Esborrar resposta ////////////////////
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
     * @param idUsuari
     * @param idEnquesta
     * @param idEnquestat
     * @throws EnquestaNoExisteixException
     * @throws UsuariNoHaResposEnquesta
     */
    public int esborrarRespostaEnquesta(int idUsuari, Integer idEnquesta, int idEnquestat)
            throws EnquestaNoExisteixException, UsuariNoHaResposEnquesta {
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        Usuari afectat = ctrlDominiMantUsuari.getUsuari(idEnquestat);
        if(u == null){
            return -1; // L'usuari que vol esborrar la resposta no existeix
        }

        if(afectat == null){
            return -1; // L'usuari enquestat no existeix
        }

        if(u.isBlocked()){
            return -10; // Usuari vetat
        }

        if(afectat.isBlocked()){
            return -10; // Usuari vetat
        }

        if (!u.esAdmin() && !u.esModerador()) {
            return -6; // Credencials insuficients
        }

        if (!afectat.teEnquestaRealitzada(idEnquesta)) {
            return -5; // L'usuari enquestat no ha realitzat l'enquesta
        }

        if (u.getId() < 0) {
            return -1; // L'usuari és anònim
        }

        if (u.esAdmin()) {
            if (!u.teEnquestaAdministrada(idEnquesta)) {
                return -6; // L'usuari no administra l'enquesta
            }
        }

        try {
            esborrarRespostaEnquestaPrivate(idEnquesta, idEnquestat);
        } catch (Exception e) { return -4; }

        afectat.demanarEliminarRealitzada(idEnquesta);
        return 1;
    }

    /**
     * Funcio per a realitzar clustering K-means sobre les respostes d'una enquesta
     * 
     * @param idEnquesta    identificador de l'enquesta
     * @param k             nombre de clústers
     * @param maxIterations nombre màxim d'iteracions
     * @return resultat, map amb l'identificador de la resposta i el clúster
     *         assignat
     * @throws EnquestaNoExisteixException si l'enquesta no existeix
     * @throws KmeansExcepcio              si hi ha un error en l'algoritme K-means
     */
    ////////////////////// Cas d'us - clustering //////////////////////
    protected Map<Integer, Integer> clusteringPrivate(Integer idEnquesta, int k, int maxIterations)
            throws EnquestaNoExisteixException, KmeansExcepcio {
        Enquesta enq = this.ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }
        KMeans kmeans = new KMeans(k, maxIterations);
        kmeans.fit(enq);
        int[] labels = kmeans.getLabels();

        List<Pregunta> preguntes = enq.getPreguntesObj();
        Map<Integer, Resposta> respostesPregunta0 = preguntes.get(0).getRespostes();
        Map<Integer, Integer> resultat = new HashMap<>();
        int n = enq.getNumRespostes();

        Object[] idArray = respostesPregunta0.keySet().toArray();
        for (int i = 0; i < n; i++) {
            int idUsuari = (int) idArray[i];
            resultat.put(idUsuari, labels[i]);
        }
        return resultat;
    }

    /**
     * Funcio per a realitzar clustering K-means sobre les respostes d'una enquesta
     * @param idUsuari
     * @param idEnquesta
     * @param k
     * @param maxIterations
     * @return resultat, map amb l'identificador de la resposta i el clúster assignat
     * @throws EnquestaNoExisteixException
     * @throws KmeansExcepcio
     */
    public Map<Integer, Integer> clustering(int idUsuari, Integer idEnquesta, int k, int maxIterations)
            throws EnquestaNoExisteixException, KmeansExcepcio {
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        if(u == null){
            return null;
        }

        if(u.isBlocked()){
            return null;
        }

        if (u.getId() < 0) {
            return null;
        }

        if (u.esEnquestat()) {
            if (!u.teEnquestaRealitzada(idEnquesta)) {
                return null;
            }
        } else if (u.esEnquestador()) {
            if (!u.teEnquestaAssignada(idEnquesta)) {
                return null;
            }
        }

        else if (u.esAdmin()) {
            if (!u.teEnquestaAdministrada(idEnquesta) && !u.teEnquestaRealitzada(idEnquesta)) {
                return null;
            }
        }

        try {
            return clusteringPrivate(idEnquesta, k, maxIterations);
        } catch (Exception e) { return null; }
    }

    //////////////////// Funciones para debug ///////////////////////////////
    /**
     * Funcio per a mostrar les enquestes i les seves preguntes i respostes
     * 
     * @param idEnquesta identificador de l'enquesta
     * @return result, llista de strings amb la informacio de l'enquesta
     * @throws EnquestaNoExisteixException si l'enquesta no existeix
     */
    ////////////////////// Cas d'us - Consultar enquesta ////////////////////
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
    public List<String> consultarEnquestesAmbPreguntesIRespostes() throws EnquestaNoExisteixException {
        int numEnquestes = ctrlDominiMantEnquesta.getNumEnquestes();
        List<String> result = new ArrayList<>();
        Map<Integer, Enquesta> enquestes = ctrlDominiMantEnquesta.getEnquestesObj();
        result.add("Numero d'enquesta: " + numEnquestes);
        for (Integer idEnquesta : enquestes.keySet()) {
            Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
            if (enq == null) {
                throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
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

    /*
     public List<String> obtenirInfoEnquesta(int idEnquesta) {
     Enquesta e = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);

     List<String> out = new ArrayList<>();
     out.add("=== ENQUESTA " + idEnquesta + " ===");

     for (Pregunta p : e.getPreguntesObj()) {
     out.add("");
     out.add("Pregunta: " + p.getText());
     out.add("Opcions: " + p.getOpcions().toString());
     }

     return out;
     }
     */

    /**
     * Funcio per a obtenir les respostes d'una enquesta
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

    // programar el borrado de esta funcion en un futuro
    public int crearUsuariAdmin(String nomUsuari, String password, String email) {
        int id = ctrlDominiMantUsuari.getNouID();
        // D'alguna forma s'ha de decidir el rol per enviar-lo, es a dir rol es Admin,
        // esnquestat o enquestador, es fa amb un if
        UsuariState rol = new AdminState();
        /////////////////////////
        Usuari nouEnquestador = new Usuari(id, nomUsuari, password, email, rol);
        ctrlDominiMantUsuari.afegirUsuari(nouEnquestador);
        //// Funcio per la Persistencia
        /// ctrlPersistencia.guardarUsuaris(ctrlDominiMantUsuari.getUsuaris());
        return id;
    }

    /**
     * Funcio per a crear un usuari enquestat
     * 
     * @param nomUsuari Nom de l'usuari
     * @param password  Contrasenya de l'usuari
     * @param email     Email de l'usuari
     * @return codi d'error
     */
    ////////////////////// Cas d'us - Crear usuari ////////////////////
    public int crearUsuariEnquestat(String nomUsuari, String password, String email) {
        if (nomUsuari == null || nomUsuari.isEmpty() || password == null || email == null) return -11;

        if (ctrlDominiMantUsuari.existeixUsuari(nomUsuari)) {
            return -2;
        }

        if (!checkRequerimentsPassword(password))
            return -2;

        if (ctrlDominiMantUsuari.emailUsat(email))
            return -2;

        int id = ctrlDominiMantUsuari.getNouID();
        // D'alguna forma s'ha de decidir el rol per enviar-lo, es a dir rol es Admin,
        // esnquestat o enquestador, es fa amb un if
        UsuariState rol = new EnquestatState();
        /////////////////////////
        Usuari nouEnquestador = new Usuari(id, nomUsuari, password, email, rol);
        ctrlDominiMantUsuari.afegirUsuari(nouEnquestador);
        //// Funcio per la Persistencia
        /// ctrlPersistencia.guardarUsuaris(ctrlDominiMantUsuari.getUsuaris());
        return id;
    }

    /**
     * Funcio per a crear un usuari enquestador
     * @param nomUsuari
     * @param password
     * @param email
     * @return codi d'error
     */
    public int crearUsuariEnquestador(String nomUsuari, String password, String email) {
        if (nomUsuari == null || nomUsuari.isEmpty() || password == null || email == null) return -11;

        if (ctrlDominiMantUsuari.existeixUsuari(nomUsuari)) {
            return -2;
        }

        if (!checkRequerimentsPassword(password))
            return -2;

        if (ctrlDominiMantUsuari.emailUsat(email))
            return -2;

        int id = ctrlDominiMantUsuari.getNouID();
        // D'alguna forma s'ha de decidir el rol per enviar-lo, es a dir rol es Admin,
        // esnquestat o enquestador, es fa amb un if
        UsuariState rol = new EnquestadorState();
        /////////////////////////
        Usuari nouEnquestador = new Usuari(id, nomUsuari, password, email, rol);
        ctrlDominiMantUsuari.afegirUsuari(nouEnquestador);
        //// Funcio per la Persistencia
        /// ctrlPersistencia.guardarUsuaris(ctrlDominiMantUsuari.getUsuaris());
        return id;
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
            throws EnquestaNoExisteixException, UsuariNoHaResposEnquesta {
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
     * Funcio per a comprovar si una enquesta té resposta d'un usuari
     * 
     * @param enq      Enquesta
     * @param idUsuari identificador de l'usuari
     * @return true si l'usuari ha respost l'enquesta, false en cas contrari
     */
    private boolean enquestaTeRespostaUsuari(Enquesta enq, int idUsuari) {
        return enq.participa(idUsuari);
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
    protected int modificarRespostaEnquestaPrivate(Integer idEnquesta, int idUsuari, int idxPregunta, String novaResposta)
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
        //// ctrlPersistencia.guardarEnquestes(ctrlDominiMantEnquesta.getEnquestesObj());
        return 1; // Èxit
    }

    /**
     * Funcio per a modificar la resposta d'una pregunta d'una enquesta per un
     * @param idExecutor
     * @param idEnquesta
     * @param idUsuari
     * @param idxPregunta
     * @param novaResposta
     * @return codi d'error
     * @throws UsuariNoHaResposEnquesta
     * @throws EnquestaNoExisteixException
     * @throws InvalidFormatResposta
     */
    public int modificarRespostaEnquesta(int idExecutor, Integer idEnquesta, int idUsuari, int idxPregunta, String novaResposta)
            throws UsuariNoHaResposEnquesta, EnquestaNoExisteixException, InvalidFormatResposta {
        if (novaResposta == null) return -11;

        Usuari u = ctrlDominiMantUsuari.getUsuari(idExecutor);
        if(u == null){
            return -1; // L'usuari no existeix
        }

        if(u.isBlocked()){
            return -10; // Usuari vetat
        }

        if(u.getId() < 0) {
            return -1; // L'usuari és anònim
        }

        if(u.esEnquestador()){
            return -3; // Un enquestador no pot modificar respostes
        }
        if(u.esEnquestat()){
            if(idExecutor != idUsuari){
                return -5; // Un enquestat només pot modificar les seves pròpies respostes
            }
            if(!u.teEnquestaRealitzada(idEnquesta)){
                return -6; // L'usuari no ha realitzat l'enquesta
            }
        }
        if(u.esAdmin()){
            if(!u.teEnquestaAdministrada(idEnquesta) && !u.teEnquestaRealitzada(idEnquesta)){
                return -6; // L'usuari no administra ni ha realitzat l'enquesta
            }
        }
        //ja ha passat totes les comprovacions
        return modificarRespostaEnquestaPrivate(idEnquesta, idUsuari, idxPregunta, novaResposta);
    }

    /**
     * Funcio per a consultar el perfil d'un usuari
     * @param id
     * @return llista de strings amb la informacio del perfil de l'usuari
     */
    ////////////////////// Cas d'us - Consultar usuari ////////////////////
    public List<String> consultarPerfil(int id) {
        Usuari us = ctrlDominiMantUsuari.getUsuari(id);
        List<String> perfil = new ArrayList<>();
        perfil.add("Id de l'usuari: " + us.getId());
        perfil.add("Nom de l'usuari: " + us.getUsuari());
        perfil.add("Email: " + us.getEmail());
        return perfil;
    }

    /**
     * Funcio per a iniciar sessio
     * @param nomUsuari
     * @param password
     * @return codi d'error
     */
    public int iniciarSessio(String nomUsuari, String password) {
        if (nomUsuari == null || password == null || nomUsuari.isEmpty() || password.isEmpty()) return -11;
        return ctrlDominiMantUsuari.iniciarSessio(nomUsuari, password);
    }

    /**
     * Funcio per a comprovar els requeriments d'una contrasenya
     * @param password
     * @return true si compleix els requeriments, false en cas contrari
     */
    private boolean checkRequerimentsPassword(String password) {
        return password.length() > 5;
    }

    /**
     * Funcio per a donar poders d'enquestador a un usuari
     * @param idExecutor
     * @param idEnquesta
     * @param nomTarget
     * @return codi d'error
     */
    public int donarPodersEnquestador(int idExecutor, Integer idEnquesta, String nomTarget) {
        if (nomTarget == null || nomTarget.isEmpty()) return -11;

        Usuari executor = ctrlDominiMantUsuari.getUsuari(idExecutor);
        if (executor == null || executor.getId() < 0) {
            return -1; // L'executor no és vàlid.
        }

        if(executor.isBlocked()){
            return -10; // L'usuari amb id " + idExecutor + " està vetat i no pot assignar enquestadors.
        }

        if (!executor.esAdmin() && !executor.esModerador()) {
            return -3; // L'usuari amb id " + idExecutor + " no té permisos per assignar enquestadors.
        }

        Enquesta enq;
        try {
            enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        } catch (Exception e) {
            return -4; // Enquesta no existeix
        }

        if (executor.esAdmin()) {
            if (!executor.teEnquestaAdministrada(idEnquesta)) {
                return -6; // L'admin no administra aquesta enquesta i no pot assignar enquestadors.
            }
        }

        Usuari target = ctrlDominiMantUsuari.getUsuariPerNom(nomTarget);

        if (target == null || target.getId() < 0) {
            return -1; // El target no és vàlid.
        }

        if (target.isBlocked()) {
            return -10; // L'usuari destinatari '" + nomTarget + "' està vetat i no se li pot donar poders.
        }

        if (target.esAdmin() || target.esModerador()) {
            return -9; // No es pot canviar el rol d'un Admin o Moderador.
        }

        if (target.esEnquestat()) {
            target.cambiarARolEnquestador();
        }

        target.demanarAfegirEnquestaAssignada(enq);

        return 1; // Èxit
    }

    /**
     * Funcio per a donar poders d'administrador a un usuari
     * @param idExecutor
     * @param idEnquesta
     * @param nomTarget
     * @return codi d'error
     */
    public int donarPodersAdmin(int idExecutor, Integer idEnquesta, String nomTarget) {
        if (nomTarget == null || nomTarget.isEmpty()) return -11;

        Usuari executor = ctrlDominiMantUsuari.getUsuari(idExecutor);
        if (executor == null || executor.getId() < 0) {
            return -1; // L'executor no és vàlid.
        }

        if(executor.isBlocked()){
            return -10; // L'usuari amb id " + idExecutor + " està vetat i no pot nomenar administradors.
        }

        if (!executor.esAdmin() && !executor.esModerador()) {
            return -3; // L'usuari amb id " + idExecutor + " no té permisos per nomenar administradors.
        }

        Enquesta enq;
        try {
            enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        } catch (Exception e) {
            return -4; // Enquesta no existeix
        }

        if (executor.esAdmin()) {
            if (!executor.teEnquestaAdministrada(idEnquesta)) {
                return -6; // L'admin no administra aquesta enquesta i no pot nomenar administradors.
            }
        }

        Usuari target = ctrlDominiMantUsuari.getUsuariPerNom(nomTarget);

        if (target == null || target.getId() < 0) {
            return -1; // El target no és vàlid.
        }

        if(target.isBlocked()){
            return -10; // L'usuari destinatari '" + nomTarget + "' està vetat i no se li pot donar poders.
        }

        if (target.esModerador()) {
            return -9; // No es pot canviar el rol d'un Moderador.
        }

        if (target.teEnquestaAdministrada(idEnquesta)) {
            return -5; // L'usuari ja administra aquesta enquesta.
        }

        if (!target.esAdmin()) {
            target.cambiarARolAdmin();
        }

        target.demanarAfegirEnquestaAdministrada(enq);

        return 1; // Èxit
    }

    public List<String> obtenirNomUsuaris() {
        return ctrlDominiMantUsuari.obtenirNomsUsuaris();
    }

    public int getIdUsuariPerNom(String nomUsuari) {
        Usuari u = ctrlDominiMantUsuari.getUsuariPerNom(nomUsuari);
        if (u == null) return -1; // mirar si volem retornar -1 o que retorna exactament
        return u.getId();
    }

    public List<String> obtenirEnquestesAdministrades(int idUsuari) {
        Usuari u = ctrlDominiMantUsuari.getUsuari(idUsuari);
        Map<Integer, Enquesta> map = u.getRol().getEnquestesAdministrades();

        List<String> resultat = new ArrayList<>();

        for (Enquesta e : map.values()) {
            resultat.add("ID: " + e.getId() + " - " + e.getTitol());
        }
        return resultat;
    }
}