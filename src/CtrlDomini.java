import java.util.*;


public class CtrlDomini {
    //Tiene que estar todos los gestores creados
    private GestorEnquesta gestorEnquesta;
    private GestorUsuaris gestorUsuaris;

    public CtrlDomini() {
        //Crea todas las instancias de los gestores
        gestorEnquesta = new GestorEnquesta();
        gestorUsuaris = new GestorUsuaris();
    }

    //Getters
    public GestorEnquesta getCtrlEnquesta() {
        return gestorEnquesta;
    }
    public GestorUsuaris getCtrlUsuari() {
        return gestorUsuaris;
    }

    //Setters
    public void setCtrlEnquesta(GestorEnquesta gestorEnquesta) {
        this.gestorEnquesta = gestorEnquesta;
    }
    public void setCtrlUsuari(GestorUsuaris gestorUsuaris) {
        this.gestorUsuaris = gestorUsuaris;
    }

    //Caso de uso 1 - Respondre enquesta
    public List<Pregunta> getPreguntes(int idEnquesta){ //Final
        return gestorEnquesta.getEnquestaPerID(idEnquesta).getPreguntes();
    }

    public void respondreEnquesta(int idEnquesta, int idUsuari, List<Resposta> respostesUsuari) { //Final
        GestorEnquesta ge = getCtrlEnquesta();
        Enquesta enq = ge.getEnquestaPerID(idEnquesta);
        if (enq == null) {
            throw new IllegalArgumentException("L'enquesta amb ID " + idEnquesta + " no existeix.");
        }
        enq.setResposta(idUsuari, respostesUsuari);
    }

    //Caso de uso 2 - Crear enquesta
    public void crearEnquesta(String titol, String descripcio, int idCreador, List<Pregunta> preguntes) { //Final
        int id = gestorEnquesta.returnSize();
        Enquesta novaEnquesta = new Enquesta(id, titol, descripcio, idCreador, preguntes);
        this.gestorEnquesta.afegirEnquesta(novaEnquesta);
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

