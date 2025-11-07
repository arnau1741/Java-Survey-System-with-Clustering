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
        else enquestes.add(enquesta);;
        //añadiria el añadir ya participante, respuestas, etc.
    }

    public Enquesta getEnquestaPerID(int id) {
        for (Enquesta e : enquestes) {
            if (e.getId().equals(id)) return e;
        }
        return null;
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