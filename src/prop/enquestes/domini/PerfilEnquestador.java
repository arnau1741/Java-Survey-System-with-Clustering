package prop.enquestes.domini;
import java.util.List;

public class PerfilEnquestador extends Usuari
{
    private List<Enquesta> enquestesAssignades;
    private List<Enquesta> enquestesRealitzada;

    // Constructor
    /**
     * Constructor de la classe PerfilEnquestador
     * @param idUsuari de l'usuari
     * @param nomUsuari de l'usuari
     * @param contrasenya de l'usuari
     * @param email de l'usuari
     */
    public PerfilEnquestador(int idUsuari, String nomUsuari, String contrasenya, String email)  {
        super(idUsuari, nomUsuari,contrasenya, email);
        enquestesAssignades = new java.util.ArrayList<>();
        enquestesRealitzada = new java.util.ArrayList<>();
    }

    /**
     * Comprova si una enquesta està assignada a l'enquestador
     * @param idEnquesta Identificador de l'enquesta
     * @return true si l'enquesta està assignada, false en cas contrari
     */
    public boolean enquestaAssignada(int idEnquesta) {
        for (Enquesta e : enquestesAssignades) {
            if (e.getId() == idEnquesta) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprova si l'enquestador ha realitzat una enquesta
     * @param idEnquesta Identificador de l'enquesta
     * @return true si l'enquesta ha estat realitzada, false en cas contrari
     */
    public boolean haRealitzatEnquesta(int idEnquesta) {
        for (Enquesta e : enquestesRealitzada) {
            if (e.getId() == idEnquesta) {
                return true;
            }
        }
        return false;
    }

    /**
     * Afegeix una enquesta a la llista d'enquestes assignades
     * @param enquesta Enquesta a assignar
     */
    public void afegirEnquestaAssignada(Enquesta enquesta) {
        enquestesAssignades.add(enquesta);
    }

    /**
     * Afegeix una enquesta a la llista d'enquestes realitzades
     * @param enquesta realitzada
     */
    public void afegirEnquestaRealitzada(Enquesta enquesta) {
        enquestesRealitzada.add(enquesta);
    }

    /**
     * Elimina una enquesta de la llista d'enquestes assignades
     * @param idEnquesta Identificador de l'enquesta a eliminar
     */
    public void eliminarEnquestaAssignada(int idEnquesta) {
        enquestesAssignades.removeIf(e -> e.getId() == idEnquesta);
    }

    /**
     * Elimina una enquesta de la llista d'enquestes realitzades
     * @param idEnquesta Identificador de l'enquesta a eliminar
     */
    public void eliminarEnquestaRealitzada(int idEnquesta) {
        enquestesRealitzada.removeIf(e -> e.getId() == idEnquesta);
    }

    /**
     * Retorna la llista d'enquestes assignades
     * @return Llista d'enquestes assignades
     */
    public List<Enquesta> getEnquestesAssignades() {
        return enquestesAssignades;
    }

    /**
     * Retorna la llista d'enquestes realitzades
     * @return Llista d'enquestes realitzades
     */
    public List<Enquesta> getEnquestesRealitzada() {
        return enquestesRealitzada;
    }

    /**
     * Elimina una enquesta tant de les assignades com de les realitzades
     * @param idEnquesta Identificador de l'enquesta a eliminar
     */
    public void eliminarEnquesta(int idEnquesta) {
        eliminarEnquestaAssignada(idEnquesta);
        eliminarEnquestaRealitzada(idEnquesta);
    }
}