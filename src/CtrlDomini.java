import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CtrlDomini {
    //Tiene que estar todos los gestores creados
    private CtrlDominiMantEnquesta ctrlDominiMantEnquesta;
    private CtrlDominiMantUsuari ctrlDominiMantUsuari;

    /**
     * Crea una nova instancia de CtrlDomini
     *
     */
    public CtrlDomini() {
        //Crear los controladores de dominio
        ctrlDominiMantEnquesta = new CtrlDominiMantEnquesta();
        ctrlDominiMantUsuari = new CtrlDominiMantUsuari();
    }

    /**
     * Retorna les preguntes de l'enquesta amb id donat
     * @param idEnquesta Identificador de l'enquesta
     * @return Llista de preguntes en format text
     */
    ////////////////// Caso de uso 1 - Respondre enquesta //////////////////////
    public List<String> getPreguntes(int idEnquesta){ //Final
        System.out.println("entra a getPreguntes de CtrlDomini");
        return this.ctrlDominiMantEnquesta.getPreguntesEnquesta(idEnquesta);
    }

    /**
     *Funcio per a respondre una enquesta
     * @param idEnquesta Identificador de l'enquesta a respondre
     * @param idUsuari Identificador de l'usuari que respon l'enquesta
     * @param respostesUsuari Llista de respostes
     */
    public void respondreEnquesta(int idEnquesta, int idUsuari, List<String> respostesUsuari) { //Final
        Enquesta enq = this.ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        System.out.println("entra a respondreEnquesta de CtrlDomini");
        List<Resposta> respostesObj = enq.stringARespostes(respostesUsuari);
        enq.afegeixResposta(idUsuari, respostesObj);
        System.out.println("salta de respondreEnquesta de CtrlDomini");
    }

    /**
     * Funcio per a crear una enquesta
     * @param titol Titol de l'enquesta
     * @param descripcio Descripcio de l'enquesta
     * @param idCreador Identificador de l'usuari creador de l'enquesta
     * @param preguntes Llista de preguntes
     */
    /////////////////////// Caso de uso 2 - Crear enquesta //////////////////////
    public void crearEnquesta(String titol, String descripcio, int idCreador, List<String> preguntes) { //Final
        List<Pregunta> preguntesObj = transformaPreguntesAObj(preguntes);
        int id = ctrlDominiMantEnquesta.getIdEnquestaNova();
        Enquesta novaEnquesta = new Enquesta(id, titol, descripcio, idCreador, preguntesObj);
        this.ctrlDominiMantEnquesta.addEnquesta(novaEnquesta);

    }

    /////////////////////// Caso de uso - Importar enquesta //////////////////
    public int importarEnquesta(int idUsuari, String path){
        return 0;
    }

    /**
     * Funcio per a exportar una enquesta
     * @param id de l'enquesta a exportar
     * @return Llista de strings amb la informacio de l'enquesta
     */
    // Caso de uso 4: exportar enquesta
    public List<String> exportarEnquesta(int id) {
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(id);
        if (enq == null) {
            return null;
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
    /// importarRespostes funcio que llegeix les respostes d'un fitxer 
    /// leer: (numPreguntas, PREGUNTA1, PREGUNTA2, ...PREGUNTAn, RESPUESTA1, RESPUESTA2, ... RESPUESTAm) 
    ///RESPUESTAj = (resp1, resp2,... respn)
    /**
     * Funcio per a importar respostes d'un fitxer
     * @param idUsuari identificador de l'usuari que importa les respostes
     * @param path origen del fitxer
     * @param idEnquesta identificador de l'enquesta
     * @return nombre de respostes importades, -1 si hi ha un error llegint el fitxer, -2 si l'enquesta no existeix
     */
    public int importarRespostes(int idUsuari, String path, int idEnquesta){
        //llegir fitxer
        List<String> respostesTxt = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                respostesTxt.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // Error al leer el archivo
        }
        //afegir respostes a l'enquesta
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (enq == null) {
            return -2; // Enquesta no existeix
        }
        //llegim el nombre de respostes (primer linia)
        int numRespostes = Integer.parseInt(respostesTxt.get(0));
        int numPreguntes = enq.getNumPreguntes();
        System.out.println("NumRespostes llegides: " + numRespostes);
        System.out.println("NumPreguntes de l'enquesta: " + numPreguntes);

        //llegim les respostes (a partir de la linia 1, una resposta per linia)
        List<String> respostesUsuari = new ArrayList<>();
        for (int i = 1; i <= numRespostes*numPreguntes; i++) {
            respostesUsuari.add(respostesTxt.get(i));
            if (i%numPreguntes == 0) {
                //afegim la resposta a l'enquesta
                List<Resposta> respostesObj = enq.stringARespostes(respostesUsuari);
                enq.afegeixResposta(-1, respostesObj);
                //netegem la llista de respostes per al seguent usuari
                //mostra les respostes afegides
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
    public void eliminarEnquesta(int idUsuari, int idEnquesta){
        //borrar de ctrlDominiMantEnquesta
        ctrlDominiMantEnquesta.eliminarEnquesta(idEnquesta);
        //borrar de usuarios //si queremos hacer esto, implementar la logica en crear
        Usuari usuari = ctrlDominiMantUsuari.getUsuari(idUsuari);
        usuari.eliminarEnquesta(idEnquesta);
    }

    /**
     * Funcio per a modificar una pregunta d'una enquesta
     * @param idUsuari identificador de l'usuari que modifica la pregunta
     * @param idxPregunta index de la pregunta a modificar
     * @param novaPregunta llista de strings amb la nova pregunta
     * @return 1 si s'ha modificat correctament
     */
    //deberiamos hacer mas versiones en un futuro.
    public int modificarPreguntaEnquesta(int idUsuari, int idxPregunta, List<String> novaPregunta){
        //borrar todas las respuestas
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idxPregunta);
        List<Pregunta> preguntes = enq.getPreguntesObj();
        for (Pregunta p : preguntes) {
            p.eliminarTotesRespostes();
        }
        //crear la nueva pregunta
        List<Pregunta> preguntesObj = transformaPreguntesAObj(novaPregunta);
        //assignar la nueva pregunta a la enquesta
        enq.canviarPregunta(idxPregunta, preguntesObj.get(0));
        //setearla como nueva pregunta

        return 1; // Èxit
    }

    /**
     * Funcio per a esborrar les respostes d'una enquesta d'un enquestat
     * @param idUsuari identificador de l'usuari que esborra la resposta
     * @param idEnquesta identificador de l'enquesta
     * @param idEnquestat identificador de l'usuari que ha respost l'enquesta
     */
    public void esborrarRespostaEnquesta(int idUsuari, int idEnquesta, int idEnquestat){
        //pensar en como trabajar con idEnquestat.
            /*

            if (!ctrlDominiMantEnquesta.existeixEnquesta(idEnquesta)) {
                System.out.println("Error: Enquesta no existeix");
                return -1; // Codi error: Enquesta no existeix
            }

            boolean esCreador = ctrlDominiMantEnquesta.esCreadorEnquesta(idEnquesta, idUsuari);
            boolean esElMateixUsuari = (idUsuari == idEnquestat);

            if (!esCreador && !esElMateixUsuari) {
                System.out.println("Error: Sense permisos per esborrar resposta");
                return -2; // Codi error: Sense permisos
            }
            */

            // 3. Obtenir l'enquesta i eliminar la resposta
            Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
            int respostesEliminades = 0;
            for (Pregunta pregunta : enq.getPreguntesObj()) {
                Map<Integer, Resposta> respostes = pregunta.getRespostes();
                if (respostes.containsKey(idEnquestat)) {
                    respostes.remove(idEnquestat);
                    respostesEliminades++;
                }
            }
            if (respostesEliminades > 0) {
                System.out.println("Eliminades " + respostesEliminades + " respostes");
            } else {
                System.out.println("Error: No s'han trobat respostes per eliminar");
            }
    }



    /**
     * Funcio per a realitzar clustering K-means sobre les respostes d'una enquesta
     * @param idEnquesta identificador de l'enquesta
     * @param k nombre de clústers
     * @param maxIterations nombre màxim d'iteracions
     * @return Map amb l'identificador de la resposta i el clúster assignat
     */
    ////////////////////// Caso de uso  clustering //////////////////////
    public Map<Integer, Integer> clustering(int idEnquesta, int k, int maxIterations) {
        Enquesta enq = this.ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
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
    //mostrar enquestes per debug
    /**
     * Funcio per a mostrar les enquestes i les seves preguntes i respostes
     */
    public void mostrarEnquesta(int idEnquesta) {
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        System.out.println("Enquesta: " + idEnquesta);
        System.out.println("Titol: " + enq.getTitol());
        System.out.println("Descripció: " + enq.getDescripcio());
        System.out.println("Nº de preguntes: " + enq.getNumPreguntes());

    }

    //mostrar enquestes amb preguntes per debug
    /**
     * Funcio per a mostrar les enquestes amb preguntes
     */
    public void mostrarEnquestesAmbPreguntes() {
        int numEnquestes = ctrlDominiMantEnquesta.getNumEnquestes();
        System.out.println("Número d'enquestes: " + numEnquestes);
        for (int i = 0; i < numEnquestes; i++) {
            Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(i);
            System.out.println("ID enquesta: " + enq.getId() + ", Títol: " + enq.getTitol() + ", Descripció: " + enq.getDescripcio());
            List<String> preguntes = enq.getPreguntes();
            System.out.println("Preguntes:");
            for (String p : preguntes) {
                System.out.println("- " + p);
            }
        }
    }

    /**
     * Funcio per a mostrar les enquestes amb preguntes i respostes
     */
    //mostrar enquestes amb preguntes i respostes per debug
    public void mostrarEnquestesAmbPreguntesIRespostes() {
        int numEnquestes = ctrlDominiMantEnquesta.getNumEnquestes();
        System.out.println("Número d'enquestes: " + numEnquestes);
        for (int i = 0; i < numEnquestes; i++) {
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

    /**
     * Funcio per a transformar les preguntes en format string a objectes Pregunta
     * @param preguntes Llista de preguntes en format text
     * @return Llista de preguntes en format objecte Pregunta
     * @throws IllegalArgumentException
     */
    //////////////////////// Funcions auxiliars ///////////////////////////////
    //Transforma les preguntes en format text a objectes Pregunta
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
            if (tipus == 1 || tipus == 2 || tipus == 3) { //UNICA, MULTIPLE, ORDENADA
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
            else if (tipus == 0 || tipus == 4) { //NUMERICA, LLIURE
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
    //Cas d'us crear usuari
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
    public int afegirEnquestador(int idUsuariAdmin, int idEnquesta,String nomUsuariEnquestador){
        if (!ctrlDominiMantUsuari.existeixUsuari(nomUsuariEnquestador)) {
            return -1; // Codi error: Usuari no existeix
        }
        Usuari usuari = ctrlDominiMantUsuari.getUsuariPerNom(nomUsuariEnquestador);
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (usuari instanceof PerfilEnquestat){
            PerfilEnquestador nouEnquestador = new PerfilEnquestador(usuari.getId(), usuari.getUsuari(), usuari.getContrasenya(), usuari.getEmail());
            nouEnquestador.afegirEnquestaAssignada(enq);
            ctrlDominiMantUsuari.reemplacarUsuari(usuari, nouEnquestador);
            return 1; // Èxit 
        } else if(usuari instanceof PerfilEnquestador){
            PerfilEnquestador enquestador = (PerfilEnquestador) usuari;
            if (enquestador.enquestaAssignada(idEnquesta)) {
                return -2; // Codi error: Enquesta ja assignada
            }
            enquestador.afegirEnquestaAssignada(enq);
            return 1; // Èxit
        } else if (usuari instanceof PerfilAdministrador){
            // mirem si la administra a enquestesdministrades
            PerfilAdministrador admin = (PerfilAdministrador) usuari;
            if (admin.enquestaAdministrada(idEnquesta)) {
                return -3; // Codi error: Usuari ja administra aquesta enquesta
            }
            // mirem si la te a enquestesassignades
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
    public int afegirAdministrador(int idUsuariAdmin, int idEnquesta,String nomUsuariAdministrador){
        if (!ctrlDominiMantUsuari.existeixUsuari(nomUsuariAdministrador)) {
            return -1; // Codi error: Usuari no existeix
        }
        Usuari usuari = ctrlDominiMantUsuari.getUsuariPerNom(nomUsuariAdministrador);
        Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        if (usuari instanceof PerfilEnquestat){
            PerfilAdministrador nouAdministrador = new PerfilAdministrador(usuari.getId(), usuari.getUsuari(), usuari.getContrasenya(), usuari.getEmail());
            nouAdministrador.afegirEnquestaAdministrada(enq);
            ctrlDominiMantUsuari.reemplacarUsuari(usuari, nouAdministrador);
            return 1; // Èxit 
        } else if(usuari instanceof PerfilEnquestador){
            PerfilAdministrador nouAdministrador = new PerfilAdministrador(usuari.getId(), usuari.getUsuari(), usuari.getContrasenya(), usuari.getEmail());
            nouAdministrador.afegirEnquestaAdministrada(enq);
            ctrlDominiMantUsuari.reemplacarUsuari(usuari, nouAdministrador);
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
     * Funcio per a consultar el perfil d'un usuari
     * @param id identificador de l'usuari
     */
    //Caso de uso 9: consultar perfil usuari
    public void consultarPerfil(int id) {
        Usuari us = ctrlDominiMantUsuari.getUsuari(id);
        System.out.println("Id de l'usuari: " + us.getId());
        System.out.println("Nom de l'usuari: " + us.getUsuari());
        System.out.println("Email: " + us.getEmail());
    }



    /*


    //Caso de uso 3: importar enquesta
    public void importarEnquesta(String titol, String descripcio, int idCreador, List<Pregunta> preguntes) {
        gestorEnquesta.importarEnquesta(titol, descripcio, idCreador, preguntes);
    }



    // Caso de uso 5: importar respostes
    public void importarRespostes(int idEnquesta, int idUsuari, List<String> preguntesTxt, List<String> respostesTxt) {
        gestorEnquesta.importarRespostas(idEnquesta, idUsuari, preguntesTxt, respostesTxt);
    }

    //Caso de uso 6: exportar respostes
    public List<String> exportarRespostes(int id) {
        return gestorEnquesta.exportarEnquesta(id);
    }

    //Caso de uso 8: crear perfil usuari
    public void crearPerfil(Usuari usuari) {
        gestorUsuaris.crearPerfil(usuari);
    }

    //Caso de uso 9: consultar perfil usuari
    public void consultarPerfil(int id) {
        gestorUsuaris.consultarPerfil(id);
    }

    //Caso de uso 10: consultarEnquesta
    public void consultarEnquesta() {
        gestorEnquesta.llistarEnquestes();
    }

    //Caso de uso 11: consultarRespostes
    public List<Resposta> consultarRespostes(int idEnquesta, int idUsuari) {
        return gestorEnquesta.consultarRespostes(idEnquesta, idUsuari);
    }

    //Caso de uso 12: esborrar enquesta
    public void esborrarEnquesta(int idEnquesta) {
        gestorEnquesta.esborrarEnquesta(idEnquesta);
    }
    */

    /*
    public void consultarEstadistiques(int idEnquesta) {
        GestorEnquesta ge = getCtrlEnquesta();
        Enquesta enq = ge.getEnquestaPerID(idEnquesta);
        enq.mostrarEstadistiques();
    }
    */

}
