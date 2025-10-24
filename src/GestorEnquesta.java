import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestorEnquesta {
    private List<Enquesta> enquestes;

    public GestorEnquesta() {
        enquestes = new ArrayList<>();
    }

    public void afegirEnquesta(Enquesta enquesta) {
        enquestes.add(enquesta);
    }

    // public List<Enquesta> getEnquestes() { return enquestes;} fem això també?

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