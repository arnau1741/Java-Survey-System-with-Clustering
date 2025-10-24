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

    public void afegirPregunta(Pregunta p) {
        p.setID(seguentPreguntaID++);
        preguntes.add(p);
    }

    public void eliminarPregunta(int preguntaID) {
        if (preguntaID >= 0 && preguntaID < preguntes.size()) {
            preguntes.removeIf(p -> p.getID() == preguntaID);
        }
    }

    public void modificarPregunta(Integer preguntaID, Pregunta novaPregunta) {
        for (int i = 0; i < preguntes.size(); i++) {
            Pregunta p = preguntes.get(i);
            if (p.getID() == preguntaID) {
                novaPregunta.setID(preguntaID);
                preguntes.set(i, novaPregunta);
                break;
            }
        }
    }

    public List<Pregunta> getPreguntes() {
        return Collections.unmodifiableList(preguntes);
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