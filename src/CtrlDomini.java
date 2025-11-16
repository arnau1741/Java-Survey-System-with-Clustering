import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CtrlDomini {
    // crear els controladors de domini
    private CtrlDominiMantEnquesta ctrlDominiMantEnquesta;
    private CtrlDominiMantUsuari ctrlDominiMantUsuari;

    /**
     * Funcio constructora de CtrlDomini
     *
     */
    public CtrlDomini() {
        ctrlDominiMantEnquesta = new CtrlDominiMantEnquesta();
        ctrlDominiMantUsuari = new CtrlDominiMantUsuari();
    }

    /**
     * Retorna les preguntes de l'enquesta amb id donat
     * @param idEnquesta Identificador de l'enquesta
     * @return Llista de preguntes en format text
     */
    ////////////////// Cas d'us - Respondre enquesta //////////////////////
    public List<String> getPreguntes(Integer idEnquesta) throws EnquestaNoExisteixException { //Final
        System.out.println("entra a getPreguntes de CtrlDomini");
        return this.ctrlDominiMantEnquesta.getPreguntesEnquesta(idEnquesta);
    }

    /**
     * Funcio per a respondre una enquesta
     * @param idEnquesta Identificador de l'enquesta a respondre
     * @param idUsuari Identificador de l'usuari que respon l'enquesta
     * @param respostesUsuari Llista de respostes
     */
    public void respondreEnquesta(Integer idEnquesta, int idUsuari, List<String> respostesUsuari) throws EnquestaNoExisteixException, InvalidFormatEnquesta {
        System.out.println("entra a respondreEnquesta de CtrlDomini");
        Enquesta enq = this.ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }
        enq.afegeixResposta(idUsuari,respostesUsuari);
        System.out.println("salta de respondreEnquesta de CtrlDomini");
    }

    /**
     * Funcio per a crear una enquesta
     * @param titol Titol de l'enquesta
     * @param descripcio Descripcio de l'enquesta
     * @param idCreador Identificador de l'usuari creador de l'enquesta
     * @param preguntes Llista de preguntes
     */
    /////////////////////// Cas d'us - Crear enquesta //////////////////////
    public void crearEnquesta(String titol, String descripcio, int idCreador, List<String> preguntes) throws InvalidFormatEnquesta { //Final
        this.ctrlDominiMantEnquesta.novaEnquesta(titol, descripcio, idCreador, preguntes);
    }

    /**
     * Funcio per a importar una enquesta d'un fitxer
     * @param idUsuari identificador de l'usuari que importa les respostes
     * @param path origen del fitxer
     * @return 1 si s'ha importat correctament, -1 si hi ha un error llegint el fitxer
     */
    /////////////////////// Cas d'us - Importar enquesta //////////////////
    public int importarEnquesta(int idUsuari, String path) throws InvalidFormatEnquesta, FileNotFound {
        int numPreguntes = this.ctrlDominiMantEnquesta.importarEnquesta(idUsuari, path);
        return numPreguntes;
    }

    /**
     * Funcio per a exportar una enquesta
     * @param id de l'enquesta a exportar
     * @return Llista de strings amb la informacio de l'enquesta
     */
    /////////////////////// Cas d'us - Exportar enquesta //////////////////
    public List<String> exportarEnquesta(Integer id) throws EnquestaNoExisteixException {
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(id);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + id + " no existeix.");
        }
        List <String> exportat = new ArrayList<>();
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
            exportat.add("Pregunta: " + (i+1) + ": " + p.getText());

            List<String> opcions = p.getOpcions();
            if (opcions != null && !opcions.isEmpty()){
                exportat.add("Opcions:");
                for (int j = 0; j < opcions.size(); j++) {
                    String opcio = opcions.get(j);
                    exportat.add("  * Opció " + (j+1) + ": " + opcio);
                }
            }
            Map<Integer, Resposta> respostes = p.getRespostes();
            if(!respostes.isEmpty()) {
                exportat.add("Respostes:");
                for (Map.Entry<Integer, Resposta> entry : respostes.entrySet()) {
                    exportat.add("  - Usuari ID: " + entry.getKey() + ", Resposta: " + entry.getValue().getText(opcions));
                }
            }
            else {
                exportat.add("No hi ha respostes.");
            }
            exportat.add("");
        }
        return exportat;
    }

    /**
     * Funcio per a importar respostes d'un fitxer
     * @param idUsuari identificador de l'usuari que importa les respostes
     * @param path origen del fitxer
     * @param idEnquesta identificador de l'enquesta
     * @return nombre de respostes importades, -1 si hi ha un error llegint el fitxer, -2 si l'enquesta no existeix
     */
    ////////////////////// Cas d'us - Importar respostes ////////////////////
    /// leer: (numPreguntas, PREGUNTA1, PREGUNTA2, ...PREGUNTAn, RESPUESTA1, RESPUESTA2, ... RESPUESTAm)
    /// RESPUESTAj = (resp1, resp2,... respn)
    public int importarRespostes(int idUsuari, String path, Integer idEnquesta) throws EnquestaNoExisteixException, InvalidFormatEnquesta {
        // llegir fitxer
        List<String> respostesTxt = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                respostesTxt.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // Error al llegir l'arxiu
        }

        // afegir respostes a l'enquesta
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            return -2; // Enquesta no existeix
        }

        // llegim el nombre de respostes (primer linia)
        int numRespostes = Integer.parseInt(respostesTxt.get(0));
        int numPreguntes = enq.getNumPreguntes();
        System.out.println("NumRespostes llegides: " + numRespostes);
        System.out.println("NumPreguntes de l'enquesta: " + numPreguntes);

        // llegim les respostes (a partir de la linia 1, una resposta per linia)
        List<String> respostesUsuari = new ArrayList<>();
        for (int i = 1; i <= numRespostes*numPreguntes; i++) {
            respostesUsuari.add(respostesTxt.get(i));
            if (i%numPreguntes == 0) {
                // afegim la resposta a l'enquesta
                enq.afegeixResposta(-1, respostesUsuari);
                // netegem la llista de respostes per al següent usuari
                // mostra les respostes afegides
                System.out.println("Respostes afegides per usuari " + (i/numPreguntes) + ": " + respostesUsuari);

                respostesUsuari.clear();
            }
        }
        return numRespostes; // Èxit
    }

    /**
     * Funcio per a eliminar una enquesta
     * @param idUsuari identificador de l'usuari que elimina l'enquesta
     * @param idEnquesta identificador de l'enquesta a eliminar
     */
    public void eliminarEnquesta(int idUsuari, Integer idEnquesta) throws EnquestaNoExisteixException {
        // esborrar de ctrlDominiMantEnquesta
        ctrlDominiMantEnquesta.eliminarEnquesta(idEnquesta);
        // esborrar d'usuaris
        //Usuari usuari = ctrlDominiMantUsuari.getUsuari(idUsuari);
        //usuari.eliminarEnquesta(idEnquesta);
    }

    /**
     * Funcio per a modificar una pregunta d'una enquesta
     * @param idEnquesta identificador de l'enquesta per la pregunta que vol modificar
     * @param idxPregunta index de la pregunta a modificar
     * @param novaPregunta llista de strings amb la nova pregunta
     * @return 1 si s'ha modificat correctament
     */
    //deberiamos hacer mas versiones en un futuro.
    public int modificarPreguntaEnquesta(int idEnquesta, int idxPregunta, List<String> novaPregunta) throws InvalidFormatEnquesta, EnquestaNoExisteixException {
        return ctrlDominiMantEnquesta.modificarPreguntaEnquesta(idEnquesta, idxPregunta, novaPregunta);
    }

    /**
     * Funcio per a esborrar les respostes d'una enquesta d'un enquestat
     * @param idUsuari identificador de l'usuari que esborra la resposta
     * @param idEnquesta identificador de l'enquesta
     * @param idEnquestat identificador de l'usuari que ha respost l'enquesta
     */
    ////////////////////// Cas d'us - Esborrar resposta ////////////////////
    public void esborrarRespostaEnquesta(Integer idEnquesta, int idEnquestat) throws EnquestaNoExisteixException, UsuariNoHaResposEnquesta {
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }
        if (!enq.participa(idEnquestat)) {
            throw new UsuariNoHaResposEnquesta("L'usuari amb id " + idEnquestat + " no ha respost l'enquesta amb id " + idEnquesta + ".");
        }
        for (Pregunta pregunta : enq.getPreguntesObj()) {
            Map<Integer, Resposta> respostes = pregunta.getRespostes();
            if (respostes.containsKey(idEnquestat)) {
                respostes.remove(idEnquestat);
            }
        }
    }

    /**
     * Funcio per a realitzar clustering K-means sobre les respostes d'una enquesta
     * @param idEnquesta identificador de l'enquesta
     * @param k nombre de clústers
     * @param maxIterations nombre màxim d'iteracions
     * @return resultat, map amb l'identificador de la resposta i el clúster assignat
     */
    ////////////////////// Cas d'us - clustering //////////////////////
    public Map<Integer, Integer> clustering(Integer idEnquesta, int k, int maxIterations) throws EnquestaNoExisteixException, KmeansExcepcio {
        Enquesta enq = this.ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }
        KMeans kmeans = new KMeans(k, maxIterations);
        kmeans.fit(enq);
        int[] labels = kmeans.getLabels();
        Map<Integer, Integer> resultat = new HashMap<>();
        int n = enq.getNumRespostes();
        for (int i = 0; i < n; i++) {
            resultat.put(i, labels[i]);
        }
        return resultat;
    }


    //////////////////// Funciones para debug ///////////////////////////////
    /**
     * Funcio per a mostrar les enquestes i les seves preguntes i respostes
     * @param idEnquesta identificador de l'enquesta
     * @return result, llista de strings amb la informacio de l'enquesta
     */
    ////////////////////// Cas d'us - Consultar enquesta ////////////////////
    public List<String> consultarEnquesta(Integer idEnquesta) throws EnquestaNoExisteixException{
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
     * @param idEnquesta identificador de l'enquesta
     * @return result, llista de strings amb la informacio de l'enquesta i les seves preguntes
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
     * Funcio per a consultar les enquestes amb preguntes i respostes
     * @param idEnquesta identificador de l'enquesta
     * @return result, llista de strings amb la informacio de l'enquesta, les seves preguntes i respostes
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
            if (opcions != null){
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
     * Funcio per a mostrar les enquestes amb preguntes i respostes
     */
    public void mostrarEnquestesAmbPreguntesIRespostes() {
        int numEnquestes = ctrlDominiMantEnquesta.getNumEnquestes();
        System.out.println("Número d'enquestes: " + numEnquestes);
        Map<Integer, Enquesta> enquestes = ctrlDominiMantEnquesta.getEnquestesObj();
        for (Integer i : enquestes.keySet()) {
            Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(i);

            System.out.println("ID creador: " + enq.getId() + ", Títol: " + enq.getTitol() + ", Descripció: " + enq.getDescripcio());
            List<Pregunta> preguntes = enq.getPreguntesObj();
            System.out.println("Preguntes i respostes:");
            for (Pregunta p : preguntes) {
                System.out.println("- Pregunta: " + p.getText());
                Map<Integer, Resposta> respostes = p.getRespostes();
                List<String> opcions = p.getOpcions();
                if (opcions != null){
                    for (String opcio : opcions) {
                        System.out.println("  * Opció: " + opcio);
                    }
                }
                for (Map.Entry<Integer, Resposta> entry : respostes.entrySet()) {
                    System.out.println("  - Usuari ID: " + entry.getKey() + ", Resposta: " + entry.getValue().getText(opcions));
                }
            }
        }
    }
    public List<String> consultarEnquestaAmbPreguntesIRespostes(int idEnquesta) throws EnquestaNoExisteixException {
        List<String> result = new ArrayList<>();
        result = consultarEnquestaAmbPreguntes(idEnquesta);

        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }

        List<Pregunta> preguntes = enq.getPreguntesObj();
        for (Pregunta p : preguntes) {
            result.add("Pregunta: " + p.getText());
            List<String> opcions = p.getOpcions();
            if (opcions != null){
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
     * Funcio per a transformar les preguntes en format string a objectes Pregunta
     * @param preguntes Llista de preguntes en format text
     * @return Llista de preguntes en format objecte Pregunta
     * @throws IllegalArgumentException
     */
    //////////////////////// Funcions auxiliars ///////////////////////////////
    private List<Pregunta> transformaPreguntesAObj (List<String> preguntes) throws IllegalArgumentException {
        List <Pregunta> preguntesObj = new ArrayList<>();
        int size = preguntes.size();
        int idx = 0;
        while (idx < size) {
            String enunciat = preguntes.get(idx);
            int tipus = Integer.parseInt(enunciat);
            idx++;
            enunciat = preguntes.get(idx);
            idx++;
            if (tipus == 1 || tipus == 2 || tipus == 3) { // UNICA, ORDENADA, MULTIPLE
                int numOpcions = Integer.parseInt(preguntes.get(idx));
                idx++;
                List<String> opcions = new ArrayList<>();
                for (int i = 0; i < numOpcions; i++) {
                    String opcio = preguntes.get(idx);
                    opcions.add(opcio);
                    idx++;
                }
                Pregunta p = new Pregunta(enunciat, tipus, opcions);
                preguntesObj.add(p);
            }
            else if (tipus == 0 || tipus == 4) { // NUMERICA, LLIURE
                Pregunta p = new Pregunta(enunciat, tipus, null);
                preguntesObj.add(p);
            }
        }
        return preguntesObj;
    }

    /**
     * Funcio per a crear un usuari enquestat
     * @param nomUsuari Nom de l'usuari
     * @param contrasenya Contrasenya de l'usuari
     * @param email Email de l'usuari
     * @return Identificador de l'usuari creat, 0 si l'usuari ja existeix
     */
    ////////////////////// Cas d'us - Crear usuari ////////////////////
    public int crearUsuariEnquestat(String nomUsuari, String contrasenya, String email) {
        if (ctrlDominiMantUsuari.existeixUsuari(nomUsuari)) {
            return 0;
        }
        int id = ctrlDominiMantUsuari.getNumUsuaris();
        PerfilEnquestador nouEnquestador = new PerfilEnquestador(id, nomUsuari, contrasenya, email);
        ctrlDominiMantUsuari.afegirUsuari(nouEnquestador);
        return id;
    }

    /**
     * Funcio per a afegir un enquestador
     * Pendent a ser canviat segons el patro estat en entregues futures
     * @param idUsuariAdmin identificador de l'usuari administrador que afegeix l'enquestador
     * @param idEnquesta identificador de l'enquesta
     * @param nomUsuariEnquestador nom de l'usuari enquestador
     * @return 1 si s'ha afegit correctament, -1 si l'usuari no existeix, -2 si l'enquesta ja està assignada, -3 si l'usuari ja administra aquesta enquesta, -4 tipus d'usuari desconegut
     */
    public int afegirEnquestador(int idUsuariAdmin, Integer idEnquesta, String nomUsuariEnquestador){
        if (!ctrlDominiMantUsuari.existeixUsuari(nomUsuariEnquestador)) {
            return -1; // Codi error: Usuari no existeix
        }
        Usuari usuari = ctrlDominiMantUsuari.getUsuariPerNom(nomUsuariEnquestador);
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (usuari instanceof PerfilEnquestat){
            PerfilEnquestador nouEnquestador = new PerfilEnquestador(usuari.getId(), usuari.getUsuari(), usuari.getContrasenya(), usuari.getEmail());
            nouEnquestador.afegirEnquestaAssignada(enq);
            ctrlDominiMantUsuari.substituirUsuari(usuari, nouEnquestador);
            return 1; // Èxit
        } else if(usuari instanceof PerfilEnquestador){
            PerfilEnquestador enquestador = (PerfilEnquestador) usuari;
            if (enquestador.enquestaAssignada(idEnquesta)) {
                return -2; // Codi error: Enquesta ja assignada
            }
            enquestador.afegirEnquestaAssignada(enq);
            return 1; // Èxit
        } else if (usuari instanceof PerfilAdministrador){
            // mirem si l'administra a enquestesAdministrades
            PerfilAdministrador admin = (PerfilAdministrador) usuari;
            if (admin.enquestaAdministrada(idEnquesta)) {
                return -3; // Codi error: Usuari ja administra aquesta enquesta
            }
            // mirem si la te a enquestesAssignades
            if (admin.enquestaAssignada(idEnquesta)) {
                return -2; // Codi error: Enquesta ja assignada
            }
            admin.afegirEnquestaAssignada(enq);
            return 1; // Èxit
        }
        return -4; // Codi error: Tipus d'usuari desconegut
    }

    /**
     * Funcio per a afegir un administrador
     * @param idUsuariAdmin identificador de l'usuari administrador que afegeix l'administrador
     * @param idEnquesta identificador de l'enquesta
     * @param nomUsuariAdministrador nom de l'usuari administrador
     * @return 1 si s'ha afegit correctament, -1 si l'usuari no existeix, -2 si l'enquesta ja és administrada, -3 tipus d'usuari desconegut
     */
    public int afegirAdministrador(int idUsuariAdmin, Integer idEnquesta,String nomUsuariAdministrador){
        if (!ctrlDominiMantUsuari.existeixUsuari(nomUsuariAdministrador)) {
            return -1; // Codi error: Usuari no existeix
        }
        Usuari usuari = ctrlDominiMantUsuari.getUsuariPerNom(nomUsuariAdministrador);
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (usuari instanceof PerfilEnquestat){
            PerfilAdministrador nouAdministrador = new PerfilAdministrador(usuari.getId(), usuari.getUsuari(), usuari.getContrasenya(), usuari.getEmail());
            nouAdministrador.afegirEnquestaAdministrada(enq);
            ctrlDominiMantUsuari.substituirUsuari(usuari, nouAdministrador);
            return 1; // Èxit
        } else if(usuari instanceof PerfilEnquestador){
            PerfilAdministrador nouAdministrador = new PerfilAdministrador(usuari.getId(), usuari.getUsuari(), usuari.getContrasenya(), usuari.getEmail());
            nouAdministrador.afegirEnquestaAdministrada(enq);
            ctrlDominiMantUsuari.substituirUsuari(usuari, nouAdministrador);
            return 1; // Èxit
        } else if (usuari instanceof PerfilAdministrador){
            PerfilAdministrador admin = (PerfilAdministrador) usuari;
            if (admin.enquestaAdministrada(idEnquesta)) {
                return -2; // Codi error: Enquesta ja administrada
            }
            admin.afegirEnquestaAdministrada(enq);
            return 1; // Èxit
        }
        return -3; // Codi error: Tipus d'usuari desconegut
    }

    /**
     * Funcio per a consultar les respostes d'una enquesta per un usuari concret
     * @param idEnquesta identificador de l'enquesta
     * @param idUsuari identificador de l'usuari
     * @return respostesStr, llista de strings amb les preguntes i respostes de l'usuari
     */
    public List<String> getRespostesEnquestaPerUsuari(Integer idEnquesta, int idUsuari) throws EnquestaNoExisteixException, UsuariNoHaResposEnquesta{
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        List<String> respostesStr = new ArrayList<>();
        List<Resposta> respostes = enq.getRespostesUsuari(idUsuari);
        if (respostes == null) {
            throw new UsuariNoHaResposEnquesta("L'usuari amb id " + idUsuari + " no ha respost l'enquesta amb id " + idEnquesta + ".");
        }
        int idx = 0;
        for (Pregunta p : enq.getPreguntesObj()) {
            respostesStr.add("Pregunta: " + p.getText());
            List<String> opcions = p.getOpcions();
            if (opcions != null){
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
     * @param p Pregunta
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
            case 2: // ORDENADA
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
     * @param enq Enquesta
     * @param idUsuari identificador de l'usuari
     * @return true si l'usuari ha respost l'enquesta, false en cas contrari
     */
    private boolean enquestaTeRespostaUsuari(Enquesta enq, int idUsuari) {
        return enq.participa(idUsuari);
    }

    /**
     * Funcio per a obtenir una enquesta
     * @param idEnquesta identificador de l'enquesta
     * @return enquesta, l'enquesta amb l'identificador donat, null si no existeix
     */
    private Enquesta getEnquesta(Integer idEnquesta) {
        try {
            return ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Funcio per a modificar la resposta d'una pregunta d'una enquesta per un usuari concret
     * @param idEnquesta identificador de l'enquesta
     * @param idUsuari identificador de l'usuari
     * @param idxPregunta index de la pregunta a modificar
     * @param novaResposta nova resposta en format text
     * @return 1 si s'ha modificat correctament, -1 si l'enquesta no existeix, -2 si l'usuari no ha respost l'enquesta, -3 si l'índex de la pregunta és invàlid, -4 si la nova resposta no és vàlida
     */
    public int modificarRespostaEnquesta(Integer idEnquesta, int idUsuari, int idxPregunta, String novaResposta) throws EnquestaNoExisteixException, UsuariNoHaResposEnquesta, InvalidFormatResposta {
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");

        if (!enquestaTeRespostaUsuari(enq, idUsuari)) {
            throw new UsuariNoHaResposEnquesta("L'usuari amb id " + idUsuari + " no ha respost l'enquesta amb id " + idEnquesta + ".");
        }

        List<Resposta> respostes = enq.getRespostesUsuari(idUsuari);
        if (idxPregunta < 0 || idxPregunta >= respostes.size()) {
            throw new InvalidFormatResposta("L'índex de la pregunta " + idxPregunta + " és invàlid.");
        }

        Pregunta p = enq.getPreguntesObj().get(idxPregunta);
        if (!comprovarRespostaValid(p, novaResposta)) {
            throw new InvalidFormatResposta("La nova resposta '" + novaResposta + "' no és vàlida per a la pregunta: " + p.getText());
        }

        int tipus = p.getTipus();
        if (tipus == 0) {
            // numerica: convertir a double
            double valor = Double.parseDouble(novaResposta);
            Resposta r = new RespostaNumerica(valor);
            respostes.set(idxPregunta, r);
        }
        else if (tipus == 1) {
            // unica: convertir a int
            int idxOpcio = p.getOpcions().indexOf(novaResposta);
            RespostaUnica r = new RespostaUnica(idxOpcio);
            respostes.set(idxPregunta, r);
        }
        else if (tipus == 2) {
            // multiple: convertir a llista d'int
            String[] parts = novaResposta.split(",");
            List<Integer> idxOpcions = new ArrayList<>();
            for (String part : parts) {
                int idx = p.getOpcions().indexOf(part.trim());
                idxOpcions.add(idx);
            }
            RespostaMultiple r = new RespostaMultiple(p.getNumOpcions());
            r.selecciona(idxOpcions);
            respostes.set(idxPregunta, r);
        }
        else if (tipus == 3) {
            // ordenada: convertir a int
            int idxOpcio = p.getOpcions().indexOf(novaResposta);
            RespostaOrdenada r = new RespostaOrdenada(idxOpcio);
            respostes.set(idxPregunta, r);
        }
        else if (tipus == 4) {
            // lliure: text
            RespostaLliure r = new RespostaLliure(novaResposta);
            respostes.set(idxPregunta, r);
        }

        return 1; // Èxit
    }

    /**
     * Funcio per a consultar el perfil d'un usuari
     * @param id identificador de l'usuari
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
}