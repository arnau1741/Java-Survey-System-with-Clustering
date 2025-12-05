package prop.enquestes.domini;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AdminState extends UsuariState {
    private Map<Integer, Enquesta> enquestesAdministrades;
    private Map<Integer, Enquesta> enquestesRealitzades;

    public AdminState() {
        this.enquestesAdministrades = new HashMap<>();
        this.enquestesRealitzades = new HashMap<>();
    }

    public AdminState(Map<Integer, Enquesta> realitzadesHeretades) {
        this.enquestesAdministrades = new HashMap<>();
        this.enquestesRealitzades = (realitzadesHeretades != null) ? realitzadesHeretades : new HashMap<>();
    }

    @Override
    public String nombreRol() { return "Admin"; }

    @Override
    public void cambiarARolEnquestador(Usuari u) {
        u.setRol(new EnquestadorState());
    }

    @Override
    public void cambiarARolAdmin(Usuari u) {}

    @Override
    public void cambiarARolEnquestat(Usuari u) {
        u.setRol(new EnquestatState(this.enquestesRealitzades));
    }

    @Override
    public void cambiarARolModerador(Usuari u) {
        u.setRol(new ModeradorState());
    }

    @Override
    public Map<Integer, Enquesta> getEnquestesAdministrades() { return enquestesAdministrades; }
    @Override
    public void afegirEnquestaAdministrada(Enquesta e) { enquestesAdministrades.put(e.getId(), e); }
    @Override
    public void eliminarEnquestaAdministrada(int idEnquesta) { enquestesAdministrades.remove(idEnquesta); }
    @Override
    public boolean enquestaAdministrada(int idEnquesta) { return enquestesAdministrades.containsKey(idEnquesta); }

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
    public void afegirEnquestaAssignada(Enquesta e) { throw new UnsupportedOperationException("Admin no té assignades"); }
    @Override
    public void eliminarEnquestaAssignada(int idEnquesta) {}
    @Override
    public boolean enquestaAssignada(int idEnquesta) { return false; }

    @Override public boolean esAdmin() { return true; }
}