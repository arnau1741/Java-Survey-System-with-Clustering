package prop.enquestes.domini;

import prop.enquestes.domini.Usuari;

public abstract class UsuariState {
    public abstract String nombreRol();

    public abstract void cambiarARolEnquestador(Usuari u);

    public abstract void cambiarARolAdmin(Usuari u);

    public abstract void cambiarARolEnquestat(Usuari u);

    public abstract void eliminarEnquesta(Usuari contexto, int idEnquesta);

    public abstract void afegirEnquesta(Usuari contexto, Enquesta e);

    public boolean teEnquesta(Usuari usuari, int idEnquesta) {
        // False per defecte els estats sobreescriuen aixo
        return false;
    }

    public boolean esAdmin() {
        // False per defecte els estats sobreescriuen aixo
        return false;
    }

    public boolean esEnquestador() {
        // False per defecte els estats sobreescriuen aixo
        return false;
    }

    public boolean esEnquestat() {
        // False per defecte els estats sobreescriuen aixo
        return false;
    }

    public boolean esModerador() {
        // False per defecte els estats sobreescriuen aixo
        return false;
    }
}
