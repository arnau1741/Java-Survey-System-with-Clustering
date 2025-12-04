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
    public void afegirEnquestaRealitzada(Usuari contexto, Enquesta e) {
        contexto.afegirEnquestaRealitzada(e);
    }

    @Override
    public void afegirEnquestaAssignada(Usuari usuari, Enquesta e) {
        //no fa res
    }

    @Override
    public void afegirEnquestaAdministrada(Usuari usuari, Enquesta e) {
        //no fa res
    }

    @Override
    public void afegirEnquestaModerada(Usuari usuari, Enquesta e) {
        //no fa res
    }

    @Override
    public void eliminarEnquestaRealitzada(Usuari contexto, int idEnquesta) {
        contexto.eliminarEnquestaRealitzada(idEnquesta);
    }

    @Override
    public void eliminarEnquestaAdministrada(Usuari usuari, int idEnquesta) {
        //no fa res
    }

    @Override
    public void eliminarEnquestaModerada(Usuari usuari, int idEnquesta) {
        //no fa res
    }

    @Override
    public void eliminarEnquestaAssignada(Usuari usuari, int idEnquesta) {
        //no fa res
    }

    @Override
    public boolean enquestaRealitzada(Usuari contexto, int idEnquesta){
        return contexto.haRealitzatEnquesta(idEnquesta);
    }

    @Override
    public boolean esEnquestat() {
        return true;
    }
}
