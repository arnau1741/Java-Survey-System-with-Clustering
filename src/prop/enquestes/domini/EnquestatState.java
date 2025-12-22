package prop.enquestes.domini;

import java.util.*;

public class EnquestatState extends UsuariState {
    private Map<Integer, Enquesta> enquestesRealitzades;

    /**
     * Constructor per defecte de la classe EnquestatState
     */
    public EnquestatState() {
        this.enquestesRealitzades = new HashMap<>();
    }

    /**
     * Constructor amb paràmetres de la classe EnquestatState
     * @param realitzadesExistents
     */
    public EnquestatState(Map<Integer, Enquesta> realitzadesExistents) {
        this.enquestesRealitzades = (realitzadesExistents != null) ? realitzadesExistents : new HashMap<>();
    }

    /**
     * Getter del nom del rol
     * @return nom del rol
     */
    @Override
    public String nombreRol() { return "ENQUESTAT"; }

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
    public void cambiarARolAdmin(Usuari u) {
        u.setRol(new AdminState(this.enquestesRealitzades));
    }

    /**
     * Canvia el rol de l'usuari a Enquestat
     * @param u
     */
    @Override
    public void cambiarARolEnquestat(Usuari u) {}

    /**
     * Canvia el rol de l'usuari a Moderador
     * @param u
     */
    @Override
    public void cambiarARolModerador(Usuari u) {
        u.setRol(new ModeradorState());
    }

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
     * Comprova si una enquesta ha estat realitzada
     * @param idEnquesta
     * @return true si l'enquesta ha estat realitzada, false en cas contrari
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
    public void afegirEnquestaAssignada(Enquesta e) { throw new UnsupportedOperationException("Enquestat no té assignades"); }

    /**
     * Elimina una enquesta assignada
     * @param idEnquesta
     */
    @Override
    public void eliminarEnquestaAssignada(int idEnquesta) {}

    /**
     * Comprova si una enquesta està assignada
     * @param idEnquesta
     * @return true si l'enquesta està assignada, false en cas contrari
     */
    @Override
    public boolean enquestaAssignada(int idEnquesta) { return false; }

    /**
     * Getter de les enquestes administrades
     * @return mapa d'enquestes administrades
     */
    @Override
    public Map<Integer, Enquesta> getEnquestesAdministrades() { return Collections.emptyMap(); }

    /**
     * Afegeix una enquesta administrada
     * @param e
     */
    @Override
    public void afegirEnquestaAdministrada(Enquesta e) { throw new UnsupportedOperationException("Enquestat no administra"); }

    /**
     * Elimina una enquesta administrada
     * @param idEnquesta
     */
    @Override
    public void eliminarEnquestaAdministrada(int idEnquesta) {}

    /**
     * Comprova si una enquesta està administrada
     * @param idEnquesta
     * @return true si l'enquesta està administrada, false en cas contrari
     */
    @Override
    public boolean enquestaAdministrada(int idEnquesta) { return false; }

    /**
     * Comprova si l'usuari és admin
     * @return true si és admin, false en cas contrari
     */
    @Override
    public boolean esEnquestat() { return true; }

    /**
     * Obtenir les enquestes per rol
     * @return llista d'enquestes en format string
     */
    @Override
    public List<String> obtenirEnquestesPerRol() {
        List<String> resultat = new ArrayList<>();

        for (Enquesta e : enquestesRealitzades.values()) {
            resultat.add("ID: " + e.getId() + " - " + e.getTitol());
        }
        return resultat;
    }
}