import java.util.*;

public class CtrlDominiMantEnquesta {
    private Map<Integer, Enquesta> enquestes;

    // Constructor que inicializa la colección de encuestas
    public CtrlDominiMantEnquesta() {
        enquestes = new HashMap<>();
    }

    // Método para agregar una encuesta
    public void addEnquesta(Enquesta enquesta) {
        if (enquesta != null) {
            enquestes.put(enquesta.getId(), enquesta);
        } else {
            throw new IllegalArgumentException("La encuesta no puede ser nula.");
        }
    }

    // Método para obtener el número total de encuestas
    public int getNumEnquestes() {
        return enquestes.size();
    }

    // Método para obtener una encuesta por su ID
    public Enquesta getEnquesta(int idEnquesta) {
        Enquesta enq = enquestes.get(idEnquesta);
        if (enq == null) {
            throw new NoSuchElementException("No se encontró la encuesta con el ID " + idEnquesta);
        }
        return enq;
    }

    // Método para obtener las preguntas de una encuesta
    public List<String> getPreguntesEnquesta(int idEnquesta) {
        Enquesta enq = enquestes.get(idEnquesta);
        if (enq != null) {
            return enq.getPreguntes();
        }
        return Collections.emptyList();  // Devuelve una lista vacía si no se encuentra la encuesta
    }

    // Método para mostrar todas las encuestas
    public void mostrarEnquestes() {
        if (enquestes.isEmpty()) {
            System.out.println("No hay encuestas disponibles.");
        } else {
            for (Enquesta e : enquestes.values()) {
                System.out.println(e);
            }
        }
    }
}
