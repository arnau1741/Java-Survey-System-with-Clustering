package prop.enquestes.domini;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EnquestatState extends UsuariState {
    private Map<Integer, Enquesta> enquestesRealitzades;

    public EnquestatState() {
        this.enquestesRealitzades = new HashMap<>();
    }

    // Constructor per mantenir dades al fer downgrade d'Admin
    public EnquestatState(Map<Integer, Enquesta> realitzadesExistents) {
        this.enquestesRealitzades = (realitzadesExistents != null) ? realitzadesExistents : new HashMap<>();
    }

    @Override
    public String nombreRol() { return "Enquestat"; }

    @Override
    public void cambiarARolEnquestador(Usuari u) {
        u.setRol(new EnquestadorState());
    }

    @Override
    public void cambiarARolAdmin(Usuari u) {
        u.setRol(new AdminState(this.enquestesRealitzades));
    }

    @Override
    public void cambiarARolEnquestat(Usuari u) {}

    @Override
    public void cambiarARolModerador(Usuari u) {
        u.setRol(new ModeradorState());
    }

    @Override
    public Map<Integer, Enquesta> getEnquestesRealitzades() { return enquestesRealitzades; }
    @Override
    public void afegirEnquestaRealitzada(Enquesta e) { enquestesRealitzades.put(e.getId(), e); }
    @Override
    public void eliminarEnquestaRealitzada(int idEnquesta) { enquestesRealitzades.remove(idEnquesta); }
    @Override
    public boolean enquestaRealitzada(int idEnquesta) { return enquestesRealitzades.containsKey(idEnquesta); }

    @Override
    public Map<Integer, Enquesta> getEnquestesAssignades() { return Collections.emptyMap(); }
    @Override
    public void afegirEnquestaAssignada(Enquesta e) { throw new UnsupportedOperationException("Enquestat no té assignades"); }
    @Override
    public void eliminarEnquestaAssignada(int idEnquesta) {}
    @Override
    public boolean enquestaAssignada(int idEnquesta) { return false; }

    @Override
    public Map<Integer, Enquesta> getEnquestesAdministrades() { return Collections.emptyMap(); }
    @Override
    public void afegirEnquestaAdministrada(Enquesta e) { throw new UnsupportedOperationException("Enquestat no administra"); }
    @Override
    public void eliminarEnquestaAdministrada(int idEnquesta) {}
    @Override
    public boolean enquestaAdministrada(int idEnquesta) { return false; }

    @Override
    public boolean esEnquestat() { return true; }
}