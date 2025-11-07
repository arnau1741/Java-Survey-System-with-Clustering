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
            x = enq.afegirFilaRespostes();
            //añadir usuario a la lista de participantes en la posicion x
            enq.afegirParticipant(u, x);
        }
        //Muestra las preguntas en orden para ser respondidas por el usuario
        enq.mostrarPreguntes();

        //lógica para añadir respuestas del usuario a la encuesta
        Pregunta[] preguntes = enq.getPreguntes(x);
        for(int i = 0; i < preguntes.length; i++){
            Pregunta pre = preguntes[i];
            //usuario da la respuesta
            Resposta res = null;
            pre.setResposta(res);

            //asignar la respuesta a la matriz de respuestas de la encuesta
            enq.setPreguntaResposta(x, i, pre);
        }
    }
}

