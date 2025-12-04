package prop.enquestes.domini;

public class EnquestatState extends UsuariState {

    @Override
    public String nombreRol() {
        return "Enquestat";
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
        //no fa res
    }

    @Override
    public void eliminarEnquesta(Usuari contexto, int idEnquesta) {
        contexto.eliminarEnquestaRealitzada(idEnquesta);
    }

    @Override
    public void afegirEnquesta(Usuari contexto, Enquesta e) {
        contexto.afegirEnquestaRealitzada(e);
    }

    @Override
    public boolean teEnquesta(Usuari usuari, int idEnquesta) {
        return usuari.haRealitzatEnquesta(idEnquesta);
    }

    @Override
    public boolean esEnquestat() {
        return true;
    }
}
