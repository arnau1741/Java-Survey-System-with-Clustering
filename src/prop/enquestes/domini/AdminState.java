package prop.enquestes.domini;

public class AdminState extends UsuariState {

    @Override
    public String nombreRol() {
        return "Admin";
    }

    @Override
    public void cambiarARolEnquestador(Usuari u) {
        u.setRol(new EnquestadorState());
    }

    @Override
    public void cambiarARolAdmin(Usuari u) {
        //no fa res
    }

    @Override
    public void cambiarARolEnquestat(Usuari u) {
        u.setRol(new EnquestatState());
    }

    @Override
    public void eliminarEnquesta(Usuari contexto, int idEnquesta) {
        contexto.eliminarEnquestaAdministrada(idEnquesta);
    }

    @Override
    public void afegirEnquesta(Usuari contexto, Enquesta e) {
        contexto.afegirEnquestaAdministrada(e);
    }

    @Override
    public boolean teEnquesta(Usuari usuari, int idEnquesta) {
        return usuari.enquestaAdministrada(idEnquesta);
    }

    @Override
    public boolean esAdmin() {
        return true;
    }
}
