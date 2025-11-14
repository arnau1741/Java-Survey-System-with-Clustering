import java.util.List;

public class PerfilEnquestat extends Usuari
{
    private List<Enquesta> enquestesRealitzades;

    // Constructor

    /**
     * Constructor de la classe PerfilEnquestat
     * @param idUsuari de l'uusuari
     * @param nomUsuari de l'usuari
     * @param contrasenya de l'usuari
     * @param email de l'usuari
     */
    public PerfilEnquestat(int idUsuari, String nomUsuari, String contrasenya, String email)  {
        super(idUsuari, nomUsuari,contrasenya, email);
        enquestesRealitzades = new java.util.ArrayList<>();
    }

    /**
     * Comprova si l'enquestat ha realitzat una enquesta concreta
     * @param idEnquesta de l'enquesta
     * @return true si l'ha realitzada, false en cas contrari
     */
    public boolean haRealitzatEnquesta(int idEnquesta) {
        for (Enquesta e : enquestesRealitzades) {
            if (e.getId() == idEnquesta) {
                return true;
            }
        }
        return false;
    }

    /**
     * Afegeix una enquesta a la llista d'enquestes realitzades
     * @param enquesta a afegir
     */
    public void afegirEnquestaRealitzada(Enquesta enquesta) {
        enquestesRealitzades.add(enquesta);
    }

    /**
     * Elimina una enquesta de la llista d'enquestes realitzades
     * @param idEnquesta de l'enquesta a eliminar
     */
    public void eliminarEnquestaRealitzada(int idEnquesta) {
        enquestesRealitzades.removeIf(e -> e.getId() == idEnquesta);
    }

    /**
     * Retorna la llista d'enquestes realitzades
     * @return llista d'enquestes realitzades
     */
    public List<Enquesta> getEnquestesRealitzades() {
        return enquestesRealitzades;
    }

    /**
     * Elimina una enquesta de la llista d'enquestes realitzades
     * @param idEnquesta de l'enquesta a eliminar
     */
    public void eliminarEnquesta(int idEnquesta) {
        eliminarEnquestaRealitzada(idEnquesta);
    }
}
