import java.util.ArrayList;
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