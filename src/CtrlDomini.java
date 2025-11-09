import java.util.*;


public class CtrlDomini {
    //Tiene que estar todos los gestores creados
    private CtrlDominiMantEnquesta ctrlDominiMantEnquesta;
    private CtrlDominiMantUsuari ctrlDominiMantUsuari;

    public CtrlDomini() {
        //Crear los controladores de dominio
        ctrlDominiMantEnquesta = new CtrlDominiMantEnquesta();
        ctrlDominiMantUsuari = new CtrlDominiMantUsuari();
    }

    /*
    //Getters
    public CtrlDominiMantEnquesta getCtrlEnquesta() {
        return ctrlDominiMantEnquesta;
    }
    public CtrlDominiMantUsuari getCtrlUsuari() {
        return ctrlDominiMantUsuari;
    }

    //Setters
    public void setCtrlEnquesta(GestorEnquesta gestorEnquesta) {
        this.gestorEnquesta = gestorEnquesta;
    }
    public void setCtrlUsuari(GestorUsuaris gestorUsuaris) {
        this.gestorUsuaris = gestorUsuaris;
    }
    */

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


    //Caso de uso 2 - Crear enquesta
    public void crearEnquesta(String titol, String descripcio, int idCreador, List<String> preguntes, List<String>tipus) { //Final
        List<Pregunta> preguntesObj = new ArrayList<>();
        for (String p : preguntes) {
            int index = preguntes.indexOf(p);
            Pregunta.Tipus t = Pregunta.Tipus.valueOf(tipus.get(index));
            Pregunta novaPregunta = new Pregunta(p, t);
            preguntesObj.add(novaPregunta);
        }
        int id = ctrlDominiMantEnquesta.getNumEnquestes();
        Enquesta novaEnquesta = new Enquesta(id, titol, descripcio, idCreador, preguntesObj);
        this.ctrlDominiMantEnquesta.afegirEnquesta(novaEnquesta);
    }

    //Caso de uso 1 - Respondre enquesta
    public List<Pregunta> getPreguntes(int idEnquesta){ //Final
        return this.ctrlDominiMantEnquesta.getPreguntesEnquesta(idEnquesta);
    }

    public void respondreEnquesta(int idEnquesta, int idUsuari, List<Resposta> respostesUsuari) { //Final
        GestorEnquesta ge = getCtrlEnquesta();
        Enquesta enq = ge.getEnquestaPerID(idEnquesta);
        if (enq == null) {
            throw new IllegalArgumentException("L'enquesta amb ID " + idEnquesta + " no existeix.");
        }
        enq.setResposta(idUsuari, respostesUsuari);
    }


    //Caso de uso 3: importar enquesta
    public void importarEnquesta(String titol, String descripcio, int idCreador, List<Pregunta> preguntes) {
        gestorEnquesta.importarEnquesta(titol, descripcio, idCreador, preguntes);
    }

    // Caso de uso 4: exportar enquesta
    public List<String> exportarEnquesta(int id) {
        return gestorEnquesta.exportarEnquesta(id);
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

    /*
    public void consultarEstadistiques(int idEnquesta) {
        GestorEnquesta ge = getCtrlEnquesta();
        Enquesta enq = ge.getEnquestaPerID(idEnquesta);
        enq.mostrarEstadistiques();
    }
    */

}

