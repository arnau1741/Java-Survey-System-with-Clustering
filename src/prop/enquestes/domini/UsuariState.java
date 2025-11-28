package prop.enquestes.domini;

import prop.enquestes.domini.Usuari;

public abstract class UsuariState {
    public abstract String nombreRol();

    public abstract void cambiarARolEnquestador(Usuari u);

    public abstract void cambiarARolAdmin(Usuari u);

    public abstract void cambiarARolEnquestat(Usuari u);
}
