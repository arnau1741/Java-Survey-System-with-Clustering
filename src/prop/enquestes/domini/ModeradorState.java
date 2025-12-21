package prop.enquestes.domini;

import prop.enquestes.controladors.CtrlDominiMantEnquesta;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ModeradorState extends UsuariState {
    //puntero a todas las encuestas del sistema

    /**
     * Nom del rol
     * @return String amb el nom del rol
     */
    @Override public String nombreRol() { return "MODERADOR"; }

    /**
     * Canvia el rol de l'usuari a Enquestador
     * @param u
     */
    @Override
    public void cambiarARolEnquestador(Usuari u) { u.setRol(new EnquestadorState()); }

    /**
     * Canvia el rol de l'usuari a Admin
     * @param u
     */
    @Override
    public void cambiarARolAdmin(Usuari u) { u.setRol(new AdminState()); }

    /**
     * Canvia el rol de l'usuari a Enquestat
     * @param u
     */
    @Override
    public void cambiarARolEnquestat(Usuari u) { u.setRol(new EnquestatState()); }

    /**
     * Canvia el rol de l'usuari a Moderador
     * @param u
     */
    @Override
    public void cambiarARolModerador(Usuari u) { }

    /**
     * Enquestes assignades a l'usuari
     * @return Map d'enquestes assignades
     */
    @Override
    public Map<Integer, Enquesta> getEnquestesAssignades() { return Collections.emptyMap(); }

    /**
     * Afegeix una enquesta assignada a l'usuari
     * @param e
     */
    @Override
    public void afegirEnquestaAssignada(Enquesta e) {}

    /**
     * Elimina una enquesta assignada a l'usuari
     * @param idEnquesta
     */
    @Override
    public void eliminarEnquestaAssignada(int idEnquesta) {}

    /**
     * Comprova si l'usuari té una enquesta assignada
     * @param idEnquesta
     * @return boolean indicant si té l'enquesta assignada
     */
    @Override
    public boolean enquestaAssignada(int idEnquesta) { return false; }

    /**
     * Enquestes realitzades per l'usuari
     * @return Map d'enquestes realitzades
     */
    @Override
    public Map<Integer, Enquesta> getEnquestesRealitzades() { return Collections.emptyMap(); }

    /**
     * Afegeix una enquesta realitzada per l'usuari
     * @param e
     */
    @Override
    public void afegirEnquestaRealitzada(Enquesta e) {}

    /**
     * Elimina una enquesta realitzada per l'usuari
     * @param idEnquesta
     */
    @Override
    public void eliminarEnquestaRealitzada(int idEnquesta) {}

    /**
     * Comprova si l'usuari ha realitzat una enquesta
     * @param idEnquesta
     * @return boolean indicant si ha realitzat l'enquesta
     */
    @Override
    public boolean enquestaRealitzada(int idEnquesta) { return false; }

    /**
     * Enquestes administrades per l'usuari
     * @return Map d'enquestes administrades
     */
    @Override
    public Map<Integer, Enquesta> getEnquestesAdministrades() { return Collections.emptyMap(); }

    /**
     * Afegeix una enquesta administrada per l'usuari
     * @param e
     */
    @Override
    public void afegirEnquestaAdministrada(Enquesta e) {}

    /**
     * Elimina una enquesta administrada per l'usuari
     * @param idEnquesta
     */
    @Override
    public void eliminarEnquestaAdministrada(int idEnquesta) {}

    /**
     * Comprova si l'usuari administra una enquesta
     * @param idEnquesta
     * @return boolean indicant si administra l'enquesta
     */
    @Override
    public boolean enquestaAdministrada(int idEnquesta) { return false; }

    /**
     * Comprova si l'usuari és administrador
     * @return boolean indicant si és administrador
     */
    @Override
    public boolean esModerador() { return true; }

    @Override
    public List<String> obtenirEnquestesPerRol() {
        return null;
    }
}