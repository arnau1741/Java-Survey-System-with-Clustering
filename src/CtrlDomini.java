import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;

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
    public void respondreEnquesta(int idEnquesta, int idUsuari) {
        //empezamos seleccionando el usuario que responde
        GestorUsuaris gu = getCtrlUsuari();
        gu.llistarUsuaris();
        //idUsuari deberia ser seleccionado por el scanner en la capa de presentacion
        Usuari u = gu.seleccionarUsuari(idUsuari);

        //empezamos mostrando id's de las encuestas
        GestorEnquesta ge = getCtrlEnquesta();
        ge.llistarEnquestes();
        //idEnquesta deberia ser seleccionado por el scanner en la capa de presentacion
        Enquesta enq = ge.getEnquestaPerID(idEnquesta);

        boolean participa = enq.participa(u);
        int x = -1;
        if(!participa){
            //crear nueva fila en la matriz que devolverá la fila x
            x = enq.afegirFilaRespostesAmbPreguntesNoRespostes();
            //añadir usuario a la lista de participantes en la posicion x
            enq.afegirParticipant(u, x);
        }
        //Muestra las preguntas en orden para ser respondidas por el usuario
        enq.mostrarPreguntes();

        //asignar la respuesta a la matriz de respuestas de la encuesta
        //vector que se recibe de la capa de presentacion con las respuestas
        List<Pregunta> preguntes;
        enq.afegirPreguntesJaRespostes(x, preguntes);
    }

    //Caso de uso 2 - Crear enquesta
    public void crearEnquesta(int idUsuari, List<Pregunta> pregunta) {
        //seleccionamos el usuario creador
        GestorUsuaris gu = getCtrlUsuari();
        gu.llistarUsuaris();
        Usuari u = gu.seleccionarUsuari(idUsuari);

        GestorEnquesta ges = getCtrlEnquesta();
        //creamos la enquesta
        //introducimos los datos en la capa de prentacion (id, titol, descripcio, creador)
        int id = ges.returnSize();
        String titol = "me la pela";
        String descripcio = "una enquesta que me la pela";
        Enquesta enq = new Enquesta(id, titol, descripcio, u);
        ges.afegirEnquesta(enq);

        //enq.afegirParticipant(u, enq.afegirFilaRespostes());
        enq.participants.addFirst(new Pair<>(u, 0)); //afegim el creador com a participant per defecte

        //crear la fila de preguntas con respuestas respuestas
        enq.afegirFilaRespostesBuit();

        //añadir preguntas a la encuesta
        for(Pregunta p: pregunta) enq.respostes.get(id).add(p);

        enq.preguntes = enq.respostes.get(id);
    }


    /*
    public void consultarEstadistiques(int idEnquesta) {
        GestorEnquesta ge = getCtrlEnquesta();
        Enquesta enq = ge.getEnquestaPerID(idEnquesta);
        enq.mostrarEstadistiques();
    }
    */

}

