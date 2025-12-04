package prop.enquestes.domini;

public class ModeradorState extends UsuariState {
    @Override
    public String nombreRol() {
        return "Moderador";
    }

    @Override
    public void cambiarARolEnquestador(Usuari u) {
        u.setRol(new EnquestadorState());
    }

    @Override
    public void cambiarARolAdmin(Usuari u) {
        u.setRol(new AdminState());
    }

    @Override
    public void cambiarARolEnquestat(Usuari u) {
        u.setRol(new EnquestatState());
    }

    @Override
    public void eliminarEnquesta(Usuari contexto, int idEnquesta) {
        contexto.eliminarEnquestaModerada(idEnquesta);
    }

    @Override
    public void afegirEnquesta(Usuari contexto, Enquesta e) {
        contexto.afegirEnquestaModerada(e);
    }

    @Override
    public boolean teEnquesta(Usuari usuari, int idEnquesta) {
        return usuari.enquestaModerada(idEnquesta);
    }

    @Override
    public boolean esModerador() {
        return true;
    }
}
