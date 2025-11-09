import java.util.*;

public class CtrlDominiMantEnquesta {
    private Map<Integer, Enquesta> enquestes;
    public CtrlDominiMantEnquesta() {
        enquestes = new HashMap<>();
    }

    public void afegirEnquesta(Enquesta enquesta) {
        enquestes.put(enquesta.getId(), enquesta);
    }

    public int getNumEnquestes() {
        return enquestes.size();
    }

    public Enquesta getEnquesta(int idEnquesta) {
        return enquestes.get(idEnquesta);
    }

    public List<Pregunta> getPreguntesEnquesta(int idEnquesta) {
        Enquesta enq = enquestes.get(idEnquesta);
        if (enq != null) {
            return enq.getPreguntes();
        }
        return null;
    }
    
}
