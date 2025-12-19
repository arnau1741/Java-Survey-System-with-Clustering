package prop.enquestes.domini;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AdminState extends UsuariState {
    private Map<Integer, Enquesta> enquestesAdministrades;
    private Map<Integer, Enquesta> enquestesRealitzades;

    /**
     * Constructor per defecte d'AdminState
     */
    public AdminState() {
        this.enquestesAdministrades = new HashMap<>();
        this.enquestesRealitzades = new HashMap<>();
    }

    /**
     * Constructor per mantenir dades al fer downgrade d'Admin
     * @param realitzadesHeretades Enquestes realitzades a heretar
     */
    public AdminState(Map<Integer, Enquesta> realitzadesHeretades) {
        this.enquestesAdministrades = new HashMap<>();
        if(realitzadesHeretades != null) {
            this.enquestesRealitzades = realitzadesHeretades;
        } else {
            this.enquestesRealitzades = new HashMap<>();
        }
    }

    /**
     * Getter del nom del rol
     * @return nom del rol
     */
    @Override
    public String nombreRol() { return "ADMIN"; }

    /**
     * Canvia el rol de l'usuari a Enquestador
     * @param u
     */
    @Override
    public void cambiarARolEnquestador(Usuari u) {
        u.setRol(new EnquestadorState());
    }

    /**
     * Canvia el rol de l'usuari a Admin
     * @param u
     */
    @Override
    public void cambiarARolAdmin(Usuari u) {}

    /**
     * Canvia el rol de l'usuari a Enquestat
     * @param u
     */
    @Override
    public void cambiarARolEnquestat(Usuari u) {
        u.setRol(new EnquestatState(this.enquestesRealitzades));
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
     * Getter de les enquestes administrades
     * @return mapa d'enquestes administrades
     */
    @Override
    public Map<Integer, Enquesta> getEnquestesAdministrades() { return enquestesAdministrades; }

    /**
     * Afegeix una enquesta administrada
     * @param e
     */
    @Override
    public void afegirEnquestaAdministrada(Enquesta e) { enquestesAdministrades.put(e.getId(), e); }

    /**
     * Elimina una enquesta administrada
     * @param idEnquesta
     */
    @Override
    public void eliminarEnquestaAdministrada(int idEnquesta) { enquestesAdministrades.remove(idEnquesta); }

    /**
     * Comprova si una enquesta està administrada
     * @param idEnquesta
     * @return true si està administrada, false en cas contrari
     */
    @Override
    public boolean enquestaAdministrada(int idEnquesta) { return enquestesAdministrades.containsKey(idEnquesta); }


    /**
     * Getter de les enquestes realitzades
     * @return mapa d'enquestes realitzades
     */
    @Override
    public Map<Integer, Enquesta> getEnquestesRealitzades() { return enquestesRealitzades; }

    /**
     * Afegeix una enquesta realitzada
     * @param e
     */
    @Override
    public void afegirEnquestaRealitzada(Enquesta e) { enquestesRealitzades.put(e.getId(), e); }

    /**
     * Elimina una enquesta realitzada
     * @param idEnquesta
     */
    @Override
    public void eliminarEnquestaRealitzada(int idEnquesta) { enquestesRealitzades.remove(idEnquesta); }

    /**
     * Comprova si una enquesta està realitzada
     * @param idEnquesta
     * @return true si està realitzada, false en cas contrari
     */
    @Override
    public boolean enquestaRealitzada(int idEnquesta) { return enquestesRealitzades.containsKey(idEnquesta); }


    /**
     * Getter de les enquestes assignades
     * @return mapa d'enquestes assignades
     */
    @Override
    public Map<Integer, Enquesta> getEnquestesAssignades() { return Collections.emptyMap(); }

    /**
     * Afegeix una enquesta assignada
     * @param e
     */
    @Override
    public void afegirEnquestaAssignada(Enquesta e) { throw new UnsupportedOperationException("Admin no té assignades"); }

    /**
     * Elimina una enquesta assignada
     * @param idEnquesta
     */
    @Override
    public void eliminarEnquestaAssignada(int idEnquesta) {}

    /**
     * Comprova si una enquesta està assignada
     * @param idEnquesta
     * @return true si està assignada, false en cas contrari
     */
    @Override
    public boolean enquestaAssignada(int idEnquesta) { return false; }

    /**
     * Comprova si l'usuari és admin
     * @return true si és admin, false en cas contrari
     */
    @Override public boolean esAdmin() { return true; }
}