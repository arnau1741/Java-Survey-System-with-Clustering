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
    public void afegirEnquestaAdministrada(Usuari contexto, Enquesta e) {
        contexto.afegirEnquestaAdministrada(e);
    }

    @Override
    public void afegirEnquestaModerada(Usuari usuari, Enquesta e) {
        // No fa res
    }

    @Override
    public void afegirEnquestaRealitzada(Usuari contexto, Enquesta e) {
        contexto.afegirEnquestaRealitzada(e);
    }

    @Override
    public void afegirEnquestaAssignada(Usuari usuari, Enquesta e) {
        // No fa res
    }

    @Override
    public void eliminarEnquestaAdministrada(Usuari contexto, int idEnquesta) {
        contexto.eliminarEnquestaAdministrada(idEnquesta);
    }

    @Override
    public void eliminarEnquestaModerada(Usuari usuari, int idEnquesta) {
        // No fa res
    }

    @Override
    public void eliminarEnquestaAssignada(Usuari usuari, int idEnquesta) {
        // No fa res
    }

    @Override
    public void eliminarEnquestaRealitzada(Usuari contexto, int idEnquesta) {
        contexto.eliminarEnquestaRealitzada(idEnquesta);
    }

    @Override
    public boolean enquestaAdministrada(Usuari contexto, int idEnquesta){
        return contexto.enquestaAdministrada(idEnquesta);
    }

    @Override
    public boolean enquestaRealitzada(Usuari contexto, int idEnquesta){
        return contexto.haRealitzatEnquesta(idEnquesta);
    }

    @Override
    public boolean esAdmin() {
        return true;
    }
}
