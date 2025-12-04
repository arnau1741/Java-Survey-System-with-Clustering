package prop.enquestes.domini;

import prop.enquestes.domini.Usuari;

public abstract class UsuariState {
    public abstract String nombreRol();

    public abstract void cambiarARolEnquestador(Usuari u);

    public abstract void cambiarARolAdmin(Usuari u);

    public abstract void cambiarARolEnquestat(Usuari u);

    public abstract void eliminarEnquestaAssignada(Usuari usuari, int idEnquesta);

    public abstract void eliminarEnquestaRealitzada(Usuari usuari, int idEnquesta);

    public abstract void eliminarEnquestaAdministrada(Usuari usuari, int idEnquesta);

    public abstract void eliminarEnquestaModerada(Usuari usuari, int idEnquesta);

    public abstract void afegirEnquestaRealitzada(Usuari usuari, Enquesta e);

    public abstract void afegirEnquestaAssignada(Usuari usuari, Enquesta e);

    public abstract void afegirEnquestaAdministrada(Usuari usuari, Enquesta e);

    public abstract void afegirEnquestaModerada(Usuari usuari, Enquesta e);

    public boolean esAdmin() {
        return false;
    }

    public boolean esEnquestador() {
        return false;
    }

    public boolean esEnquestat() {
        return false;
    }

    public boolean esModerador() {
        return false;
    }

    public boolean enquestaAssignada(Usuari usuari, int idEnquesta) {
        return false;
    }

    public boolean enquestaAdministrada(Usuari usuari, int idEnquesta) {
        return false;
    }

    public boolean enquestaRealitzada(Usuari usuari, int idEnquesta) {
        return false;
    }

    public boolean enquestaModerada(Usuari usuari, int idEnquesta) {
        return false;
    }
}
