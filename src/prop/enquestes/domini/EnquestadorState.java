package prop.enquestes.domini;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EnquestadorState extends UsuariState {
    private Map<Integer, Enquesta> enquestesAssignades;

    public EnquestadorState() {
        this.enquestesAssignades = new HashMap<>();
    }

    @Override
    public String nombreRol() { return "Enquestador"; }

    @Override
    public void cambiarARolEnquestador(Usuari u) {}

    @Override
    public void cambiarARolAdmin(Usuari u) {
        u.setRol(new AdminState(null));
    }

    @Override
    public void cambiarARolEnquestat(Usuari u) {
        u.setRol(new EnquestatState());
    }

    @Override
    public void cambiarARolModerador(Usuari u) {
        u.setRol(new ModeradorState());
    }

    @Override
    public Map<Integer, Enquesta> getEnquestesAssignades() { return enquestesAssignades; }
    @Override
    public void afegirEnquestaAssignada(Enquesta e) { enquestesAssignades.put(e.getId(), e); }
    @Override
    public void eliminarEnquestaAssignada(int idEnquesta) { enquestesAssignades.remove(idEnquesta); }
    @Override
    public boolean enquestaAssignada(int idEnquesta) { return enquestesAssignades.containsKey(idEnquesta); }

    @Override
    public Map<Integer, Enquesta> getEnquestesRealitzades() { return Collections.emptyMap(); }
    @Override
    public void afegirEnquestaRealitzada(Enquesta e) { throw new UnsupportedOperationException("Enquestador no respon"); }
    @Override
    public void eliminarEnquestaRealitzada(int idEnquesta) {}
    @Override
    public boolean enquestaRealitzada(int idEnquesta) { return false; }

    @Override
    public Map<Integer, Enquesta> getEnquestesAdministrades() { return Collections.emptyMap(); }
    @Override
    public void afegirEnquestaAdministrada(Enquesta e) { throw new UnsupportedOperationException("Enquestador no administra"); }
    @Override
    public void eliminarEnquestaAdministrada(int idEnquesta) {}
    @Override
    public boolean enquestaAdministrada(int idEnquesta) { return false; }

    @Override public boolean esEnquestador() { return true; }
}