import java.util.List;


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
    public void crearEnquesta(String titol, String descripcio, int idCreador, List<Pregunta> pregunta) { //Final
        int id = gestorEnquesta.returnSize();
        Enquesta novaEnquesta = new Enquesta(id, titol, descripcio, idCreador);
        this.gestorEnquesta.afegirEnquesta(novaEnquesta);
    }



    /*
    public void consultarEstadistiques(int idEnquesta) {
        GestorEnquesta ge = getCtrlEnquesta();
        Enquesta enq = ge.getEnquestaPerID(idEnquesta);
        enq.mostrarEstadistiques();
    }
    */

}

