import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CtrlDomini {
    //Tiene que estar todos los gestores creados
    private CtrlDominiMantEnquesta ctrlDominiMantEnquesta;
    private CtrlDominiMantUsuari ctrlDominiMantUsuari;

    public CtrlDomini() {
        //Crear los controladores de dominio
        ctrlDominiMantEnquesta = new CtrlDominiMantEnquesta();
        ctrlDominiMantUsuari = new CtrlDominiMantUsuari();
    }

    ////////////////// Caso de uso 1 - Respondre enquesta //////////////////////
    public List<String> getPreguntes(int idEnquesta){ //Final
        System.out.println("entra a getPreguntes de CtrlDomini");
        return this.ctrlDominiMantEnquesta.getPreguntesEnquesta(idEnquesta);
    }

    public void respondreEnquesta(int idEnquesta, int idUsuari, List<String> respostesUsuari) { //Final
        Enquesta enq = this.ctrlDominiMantEnquesta.getEnquesta(idEnquesta);
        System.out.println("entra a respondreEnquesta de CtrlDomini");
        List<Resposta> respostesObj = enq.stringARespostes(respostesUsuari);
        enq.afegeixResposta(idUsuari, respostesObj);
        System.out.println("salta de respondreEnquesta de CtrlDomini");
    }


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
    /// 
    /// 
    public int importarRespostes(int idUsuari, String path, int idEnquesta){
        return 0;
    }

    public void eliminarEnquesta(int idUsuari, int idEnquesta){
        //borrar de ctrlDominiMantEnquesta
        ctrlDominiMantEnquesta.eliminarEnquesta(idEnquesta);
        //borrar de usuarios //si queremos hacer esto, implementar la logica en crear
        Usuari us = ctrlDominiMantUsuari.getUsuari(idUsuari);
        //us.eliminarEnquestaCreada(idEnquesta);
    }

    //deberiamos hacer mas versiones en un futuro.
    public int modificarPreguntaEnquesta(int idUsuari, int idxPregunta, List<String> novaPregunta){
        //borrar todas las respuestas
        //get enquesta
        //crear nueva pregunta
        //setearla como nueva pregunta
        return 0;
    }

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
    public void mostrarEnquestes() {
        int numEnquestes = ctrlDominiMantEnquesta.getNumEnquestes();
        System.out.println("Número d'enquestes: " + numEnquestes);
        for (int i = 0; i < numEnquestes; i++) {
            Enquesta enq = ctrlDominiMantEnquesta.getEnquesta(i);
            System.out.println("ID enquesta: " + enq.getId() + ", Títol: " + enq.getTitol() + ", Descripció: " + enq.getDescripcio());
        }
    }

    //mostrar enquestes amb preguntes per debug
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

    /*

    //Cas d'us crear usuari
    public int crearEnquestador(String nomUsuari, String contrasenya, String email) {
        if (ctrlDominiMantUsuari.existeixUsuari(nomUsuari)) {
            return 0;
        }
        int id = ctrlDominiMantUsuari.getNumUsuaris();
        PerfilEnquestador nouEnquestador = new PerfilEnquestador(id, nomUsuari, contrasenya, email);
        ctrlDominiMantUsuari.afegirUsuari(nouEnquestador);
        return id;
    }

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
