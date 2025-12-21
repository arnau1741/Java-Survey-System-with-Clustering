package prop.enquestes.domini;

import prop.enquestes.domini.Usuari;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class UsuariState {
    /**
     * Getter del nom del rol
     * @return nomRol
     */
    public abstract String nombreRol();

    /**
     * Canvia el rol de l'usuari a Enquestador
     * @param u
     */
    public abstract void cambiarARolEnquestador(Usuari u);

    /**
     * Canvia el rol de l'usuari a Admin
     * @param u
     */
    public abstract void cambiarARolAdmin(Usuari u);

    /**
     * Canvia el rol de l'usuari a Enquestat
     * @param u
     */
    public abstract void cambiarARolEnquestat(Usuari u);

    /**
     * Canvia el rol de l'usuari a Moderador
     * @param u
     */
    public abstract void cambiarARolModerador(Usuari u);

    /**
     * Getter de les enquestes assignades a l'usuari
     * @return Map de les enquestes assignades a l'usuari
     */
    public abstract Map<Integer, Enquesta> getEnquestesAssignades();

    /**
     * Afegir una enquesta assignada a l'usuari
     * @param e
     */
    public abstract void afegirEnquestaAssignada(Enquesta e);

    /**
     * Eliminar una enquesta assignada a l'usuari
     * @param idEnquesta
     */
    public abstract void eliminarEnquestaAssignada(int idEnquesta);

    /**
     * Comprova si l'usuari te una enquesta assignada
     * @param idEnquesta
     * @return true si l'usuari te l'enquesta assignada, false en cas contrari
     */
    public abstract boolean enquestaAssignada(int idEnquesta);

    /**
     * Getter de les enquestes realitzades per l'usuari
     * @return Map de les enquestes realitzades per l'usuari
     */
    public abstract Map<Integer, Enquesta> getEnquestesRealitzades();

    /**
     * Afegir una enquesta realitzada a l'usuari
     * @param e
     */
    public abstract void afegirEnquestaRealitzada(Enquesta e);

    /**
     * Eliminar una enquesta realitzada a l'usuari
     * @param idEnquesta
     */
    public abstract void eliminarEnquestaRealitzada(int idEnquesta);

    /**
     * Comprova si l'usuari ha realitzat una enquesta
     * @param idEnquesta
     * @return true si l'usuari ha realitzat l'enquesta, false en cas contrari
     */
    public abstract boolean enquestaRealitzada(int idEnquesta);

    /**
     * Getter de les enquestes administrades per l'usuari
     * @return Map de les enquestes administrades per l'usuari
     */
    public abstract Map<Integer, Enquesta> getEnquestesAdministrades();

    /**
     * Afegir una enquesta administrada a l'usuari
     * @param e
     */
    public abstract void afegirEnquestaAdministrada(Enquesta e);

    /**
     * Eliminar una enquesta administrada a l'usuari
     * @param idEnquesta
     */
    public abstract void eliminarEnquestaAdministrada(int idEnquesta);

    /**
     * Comprova si l'usuari administra una enquesta
     * @param idEnquesta
     * @return true si l'usuari administra l'enquesta, false en cas contrari
     */
    public abstract boolean enquestaAdministrada(int idEnquesta);

    /**
     * Comprova si l'usuari es Admin
     * @return true si es Admin, false en cas contrari
     */
    public boolean esAdmin() { return false; }

    /**
     * Comprova si l'usuari es Enquestador
     * @return true si es Enquestador, false en cas contrari
     */
    public boolean esEnquestador() { return false; }

    /**
     * Comprova si l'usuari es Enquestat
     * @return true si es Enquestat, false en cas contrari
     */
    public boolean esEnquestat() { return false; }

    /**
     * Comprova si l'usuari es Moderador
     * @return true si es Moderador, false en cas contrari
     */
    public boolean esModerador() { return false; }

    public abstract List<String> obtenirEnquestesPerRol();
}
