package prop.enquestes.domini;

import prop.enquestes.domini.Usuari;
import java.util.Collections;
import java.util.Map;

public abstract class UsuariState {
    public abstract String nombreRol();

    public abstract void cambiarARolEnquestador(Usuari u);
    public abstract void cambiarARolAdmin(Usuari u);
    public abstract void cambiarARolEnquestat(Usuari u);
    public abstract void cambiarARolModerador(Usuari u);

    public abstract Map<Integer, Enquesta> getEnquestesAssignades();
    public abstract void afegirEnquestaAssignada(Enquesta e);
    public abstract void eliminarEnquestaAssignada(int idEnquesta);
    public abstract boolean enquestaAssignada(int idEnquesta);

    public abstract Map<Integer, Enquesta> getEnquestesRealitzades();
    public abstract void afegirEnquestaRealitzada(Enquesta e);
    public abstract void eliminarEnquestaRealitzada(int idEnquesta);
    public abstract boolean enquestaRealitzada(int idEnquesta);

    public abstract Map<Integer, Enquesta> getEnquestesAdministrades();
    public abstract void afegirEnquestaAdministrada(Enquesta e);
    public abstract void eliminarEnquestaAdministrada(int idEnquesta);
    public abstract boolean enquestaAdministrada(int idEnquesta);

    public boolean esAdmin() { return false; }
    public boolean esEnquestador() { return false; }
    public boolean esEnquestat() { return false; }
    public boolean esModerador() { return false; }
}
