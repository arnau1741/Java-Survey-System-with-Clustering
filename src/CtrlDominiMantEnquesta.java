import java.util.*;

public class CtrlDominiMantEnquesta {
    private Map<Integer, Enquesta> enquestes;
    private int ultimIdEnquesta = 0;

    // Constructor que inicializa la colección de encuestas

    /**
     * Constructor de la classe CtrlDominiMantEnquesta
     */
    public CtrlDominiMantEnquesta() {
        enquestes = new HashMap<>();
    }


    /**
     * Afegeix una enquesta a la col·lecció d'enquestes
     * @param enquesta a afegir
     * @throws IllegalArgumentException si l'enquesta és nul·la
     */
    public void addEnquesta(Enquesta enquesta) {
        if (enquesta != null) {
            enquestes.put(enquesta.getId(), enquesta);
        } else {
            throw new IllegalArgumentException("La encuesta no puede ser nula.");
        }
    }

    /**
     * Elimina una enquesta de la col·lecció d'enquestes
     * @param idEnquesta de l'enquesta a eliminar
     */
    public void eliminarEnquesta(int idEnquesta) {
        if (enquestes.containsKey(idEnquesta)) {
            enquestes.remove(idEnquesta);
        }
    }

    /**
     * Obtén el número total de encuestas
     * @return número d'enquestes
     */
    public int getNumEnquestes() {
        return enquestes.size();
    }

    /**
     * Obtenir un id nou per a una enquesta
     * @return id nou
     */
    public int getIdEnquestaNova() {
        int tmp = ultimIdEnquesta;
        ultimIdEnquesta++;
        return tmp;
    }

    /**
     * Obtenir una enquesta donat el seu id
     * @param idEnquesta de l'enquesta a obtenir
     * @return enquesta amb l'id donat
     * @throws NoSuchElementException si no existeix l'enquesta amb l'id donat
     */
    public Enquesta getEnquesta(int idEnquesta) {
        Enquesta enq = enquestes.get(idEnquesta);
        return enq;
    }

    /**
     * Obtenir les preguntes d'una enquesta donat el seu id
     * @param idEnquesta de l'enquesta
     * @return llista de preguntes de l'enquesta, llista buida si no es troba l'enquesta
     */
    public List<String> getPreguntesEnquesta(int idEnquesta) {
        Enquesta enq = enquestes.get(idEnquesta);
        if (enq != null) {
            return enq.getPreguntes();
        }
        return Collections.emptyList();  // Devuelve una lista vacía si no se encuentra la encuesta
    }

    /**
     * Funcio per mostrar totes les enquestes
     */
    public void mostrarEnquestes() {
        if (enquestes.isEmpty()) {
            System.out.println("No hay encuestas disponibles.");
        } else {
            for (Enquesta e : enquestes.values()) {
                System.out.println(e);
            }
        }
    }


    public Map<Integer,Enquesta> getEnquestesObj() {
        return enquestes;
    }
}
