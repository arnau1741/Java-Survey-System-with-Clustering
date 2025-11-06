import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestorEnquesta {
    // gestiona totes les enquestes creades
    private List<Enquesta> enquestes;

    public GestorEnquesta() {
        enquestes = new ArrayList<>();
    }

    public void afegirEnquesta(Enquesta enquesta) {
        if (getEnquestaPerID(enquesta.getId()) != null) {
            System.out.println("Ja existeix una enquesta amb aquest ID: " + enquesta.getId());
        }
        else enquestes.add(enquesta);
    }

    public Enquesta getEnquestaPerID(int id) {
        for (Enquesta e : enquestes) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    public void eliminarEnquesta(int id) {
        enquestes.removeIf(e -> e.getId().equals(id));
    }

    public void afegirPregunta(Enquesta e, Pregunta p) {
        e.afegirPregunta(p);
    }

    public void eliminarPregunta(Enquesta e, int preguntaID) {
        e.eliminarPregunta(preguntaID);
    }

    public void modificarPregunta(Enquesta e, Integer preguntaID, Pregunta novaPregunta) {
        e.modificarPregunta(preguntaID, novaPregunta);
    }

    public List<Pregunta> getPreguntes(Enquesta e) {
        return e.getPreguntes();
    }

    public void afegirParticipant(Enquesta e, Usuari u) {
        e.afegirParticipant(u);
    }

    public void afegirResposta(Enquesta e, Usuari u, Pregunta p, Resposta r) {
        e.afegirResposta(u, p, r);
    }

    public void getResposta(Enquesta e, Usuari u, Pregunta p) {
        e.getResposta(u, p);
    }

    public void llistarParticipants(Enquesta e) {
        e.getParticipants();
    }

    public void llistarEnquestes() {
        for (Enquesta enq : enquestes) {
            System.out.println(
                    "Enquesta ID: " + enq.getId() +
                    ", Títol: " + enq.getTitol() +
                    ", Descripció: " + enq.getDescripcio() +
                    ", Creador: " + enq.getCreador().getUsuari()
            );
        }
    }
}