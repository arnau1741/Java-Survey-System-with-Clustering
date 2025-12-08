package prop.enquestes.domini;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EnquestadorState extends UsuariState {
    private Map<Integer, Enquesta> enquestesAssignades;

    /**
     * Constructor per defecte que inicialitza les enquestes assignades buides
     */
    public EnquestadorState() {
        this.enquestesAssignades = new HashMap<>();
    }

    /**
     * Getter del nom del rol
     * @return nom del rol
     */
    @Override
    public String nombreRol() { return "Enquestador"; }

    /**
     * Canvia el rol de l'usuari a Enquestador
     * @param u
     */
    @Override
    public void cambiarARolEnquestador(Usuari u) {}

    /**
     * Canvia el rol de l'usuari a Admin
     * @param u
     */
    @Override
    public void cambiarARolAdmin(Usuari u) {
        u.setRol(new AdminState(null));
    }

    /**
     * Canvia el rol de l'usuari a Enquestat
     * @param u
     */
    @Override
    public void cambiarARolEnquestat(Usuari u) {
        u.setRol(new EnquestatState());
    }

    /**
     * Canvia el rol de l'usuari a Moderador
     * @param u
     */
    @Override
    public void cambiarARolModerador(Usuari u) {
        u.setRol(new ModeradorState());
    }

    /**
     * Getter de les enquestes assignades
     * @return map d'enquestes assignades
     */
    @Override
    public Map<Integer, Enquesta> getEnquestesAssignades() { return enquestesAssignades; }

    /**
     * Afegeix una enquesta assignada
     * @param e
     */
    @Override
    public void afegirEnquestaAssignada(Enquesta e) { enquestesAssignades.put(e.getId(), e); }

    /**
     * Elimina una enquesta assignada
     * @param idEnquesta
     */
    @Override
    public void eliminarEnquestaAssignada(int idEnquesta) { enquestesAssignades.remove(idEnquesta); }

    /**
     * Comprova si una enquesta està assignada
     * @param idEnquesta
     * @return true si està assignada, false en cas contrari
     */
    @Override
    public boolean enquestaAssignada(int idEnquesta) { return enquestesAssignades.containsKey(idEnquesta); }

    /**
     * Getter de les enquestes realitzades
     * @return map d'enquestes realitzades
     */
    @Override
    public Map<Integer, Enquesta> getEnquestesRealitzades() { return Collections.emptyMap(); }

    /**
     * Afegeix una enquesta realitzada
     * @param e
     */
    @Override
    public void afegirEnquestaRealitzada(Enquesta e) { throw new UnsupportedOperationException("Enquestador no respon"); }

    /**
     * Elimina una enquesta realitzada
     * @param idEnquesta
     */
    @Override
    public void eliminarEnquestaRealitzada(int idEnquesta) {}

    /**
     * Comprova si una enquesta està realitzada
     * @param idEnquesta
     * @return true si està realitzada, false en cas contrari
     */
    @Override
    public boolean enquestaRealitzada(int idEnquesta) { return false; }

    /**
     * Getter de les enquestes administrades
     * @return map d'enquestes administrades
     */
    @Override
    public Map<Integer, Enquesta> getEnquestesAdministrades() { return Collections.emptyMap(); }

    /**
     * Afegeix una enquesta administrada
     * @param e
     */
    @Override
    public void afegirEnquestaAdministrada(Enquesta e) { throw new UnsupportedOperationException("Enquestador no administra"); }

    /**
     * Elimina una enquesta administrada
     * @param idEnquesta
     */
    @Override
    public void eliminarEnquestaAdministrada(int idEnquesta) {}

    /**
     * Comprova si una enquesta està administrada
     * @param idEnquesta
     * @return true si està administrada, false en cas contrari
     */
    @Override
    public boolean enquestaAdministrada(int idEnquesta) { return false; }

    /**
     * Comprova si l'usuari és enquestador
     * @return true si és enquestador, false en cas contrari
     */
    @Override public boolean esEnquestador() { return true; }
}