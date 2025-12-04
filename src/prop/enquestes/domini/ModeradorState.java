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
    public void eliminarEnquestaAssignada(Usuari usuari, int idEnquesta) {
        // No fa res
    }

    @Override
    public void eliminarEnquestaRealitzada(Usuari usuari, int idEnquesta) {
        // No fa res
    }

    @Override
    public void eliminarEnquestaAdministrada(Usuari usuari, int idEnquesta) {
        // No fa res
    }

    @Override
    public void afegirEnquestaRealitzada(Usuari usuari, Enquesta e) {
        // No fa res
    }

    @Override
    public void afegirEnquestaAssignada(Usuari usuari, Enquesta e) {
        // No fa res
    }

    @Override
    public void afegirEnquestaAdministrada(Usuari usuari, Enquesta e) {
        // No fa res
    }

    @Override
    public boolean esModerador() {
        return true;
    }
}
