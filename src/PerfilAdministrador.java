import java.util.List;

public class PerfilAdministrador extends Usuari
{
    private List<Enquesta> enquestesAdministrades;
    private List<Enquesta> enquestesAssignades;
    private List<Enquesta> enquestesRealitzada;

    // Constructor
    /**
     * Constructor de PerfilAdministrador
     * @param idUsuari de l' usuari
     * @param nomUsuari de l'usuari
     * @param contrasenya de l'usuari
     * @param email de l'usuari
     */
    public PerfilAdministrador(int idUsuari, String nomUsuari, String contrasenya, String email)  {
        super(idUsuari, nomUsuari,contrasenya, email);
        enquestesAdministrades = new java.util.ArrayList<>();
        enquestesAssignades = new java.util.ArrayList<>();
        enquestesRealitzada = new java.util.ArrayList<>();
    }

    /**
     * Comprova si una enquesta pot ser administrada per aquest usuari
     * @param idEnquesta de l'enquesta
     * @return true si l'enquesta pot ser administrada per aquest usuari, false en cas contrari
     */
    public boolean enquestaAdministrada(int idEnquesta) {
        for (Enquesta e : enquestesAdministrades) {
            if (e.getId() == idEnquesta) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprova si una enquesta ha estat realitzada per aquest usuari
     * @param idEnquesta de l'enquesta
     * @return true si l'enquesta ha estat realitzada per aquest usuari, false en cas contrari
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
     * Comprova si una enquesta està assignada a aquest usuari
     * @param idEnquesta de l'enquesta
     * @return true si l'enquesta està assignada a aquest usuari, false en cas contrari
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
     * Getter de les enquestes administrades
     * @return la llista d'enquestes administrades
     */
    public List<Enquesta> getEnquestesAdministrades() {
        return enquestesAdministrades;
    }

    /**
     * Getter de les enquestes assignades
     * @return la llista d'enquestes assignades
     */
    public List<Enquesta> getEnquestesAssignades() {
        return enquestesAssignades;
    }

    /**
     * Getter de les enquestes realitzades
     * @return la llista d'enquestes realitzades
     */
    public List<Enquesta> getEnquestesRealitzada() {
        return enquestesRealitzada;
    }

    /**
     * Afegeix una enquesta a la llista d'enquestes assignades
     * @param enquesta a afegir
     */
    public void afegirEnquestaAssignada(Enquesta enquesta) {
        enquestesAssignades.add(enquesta);
    }

    /**
     * Afegeix una enquesta a la llista d'enquestes realitzades
     * @param enquesta a afegir
     */
    public void afegirEnquestaRealitzada(Enquesta enquesta) {
        enquestesRealitzada.add(enquesta);
    }

    /**
     * Afegeix una enquesta a la llista d'enquestes administrades
     * @param enquesta a afegir
     */
    public void afegirEnquestaAdministrada(Enquesta enquesta) {
        enquestesAdministrades.add(enquesta);
    }

    /**
     * Elimina una enquesta de la llista d'enquestes assignades
     * @param idEnquesta de l'enquesta a eliminar
     */
    public void eliminarEnquestaAssignada(int idEnquesta) {
        enquestesAssignades.removeIf(e -> e.getId() == idEnquesta);
    }

    /**
     * Elimina una enquesta de la llista d'enquestes realitzades
     * @param idEnquesta de l'enquesta a eliminar
     */
    public void eliminarEnquestaRealitzada(int idEnquesta) {
        enquestesRealitzada.removeIf(e -> e.getId() == idEnquesta);
    }

    /**
     * Elimina una enquesta de la llista d'enquestes administrades
     * @param idEnquesta de l'enquesta a eliminar
     */
    public void eliminarEnquestaAdministrada(int idEnquesta) {
        enquestesAdministrades.removeIf(e -> e.getId() == idEnquesta);
    }

    /**
     * Elimina una enquesta de totes les llistes d'enquestes
     * @param idEnquesta de l'enquesta a eliminar
     */
    public void eliminarEnquesta(int idEnquesta) {
        eliminarEnquestaAdministrada(idEnquesta);
        eliminarEnquestaAssignada(idEnquesta);
        eliminarEnquestaRealitzada(idEnquesta);
    }
}