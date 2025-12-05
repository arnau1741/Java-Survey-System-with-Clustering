package prop.enquestes.domini;

import java.util.Collections;
import java.util.Map;

public class ModeradorState extends UsuariState {

    @Override public String nombreRol() { return "Moderador"; }

    @Override
    public void cambiarARolEnquestador(Usuari u) { u.setRol(new EnquestadorState()); }
    @Override
    public void cambiarARolAdmin(Usuari u) { u.setRol(new AdminState()); }
    @Override
    public void cambiarARolEnquestat(Usuari u) { u.setRol(new EnquestatState()); }
    @Override
    public void cambiarARolModerador(Usuari u) { }

    @Override
    public Map<Integer, Enquesta> getEnquestesAssignades() { return Collections.emptyMap(); }
    @Override
    public void afegirEnquestaAssignada(Enquesta e) {}
    @Override
    public void eliminarEnquestaAssignada(int idEnquesta) {}
    @Override
    public boolean enquestaAssignada(int idEnquesta) { return false; }

    @Override
    public Map<Integer, Enquesta> getEnquestesRealitzades() { return Collections.emptyMap(); }
    @Override
    public void afegirEnquestaRealitzada(Enquesta e) {}
    @Override
    public void eliminarEnquestaRealitzada(int idEnquesta) {}
    @Override
    public boolean enquestaRealitzada(int idEnquesta) { return false; }

    @Override
    public Map<Integer, Enquesta> getEnquestesAdministrades() { return Collections.emptyMap(); }
    @Override
    public void afegirEnquestaAdministrada(Enquesta e) {}
    @Override
    public void eliminarEnquestaAdministrada(int idEnquesta) {}
    @Override
    public boolean enquestaAdministrada(int idEnquesta) { return false; }

    @Override
    public boolean esModerador() { return true; }
}