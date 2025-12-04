package prop.enquestes.domini;

public class EnquestadorState extends UsuariState{

    @Override
    public String nombreRol() {
        return "Enquestador";
    }

    @Override
    public void cambiarARolEnquestador(Usuari u) {
        //no fa res
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
    public void afegirEnquestaAssignada(Usuari contexto, Enquesta e) {
        contexto.afegirEnquestaAssignada(e);
    }

    @Override
    public void afegirEnquestaAdministrada(Usuari contexto, Enquesta e) {
        //no fa res
    }

    @Override
    public void afegirEnquestaModerada(Usuari contexto, Enquesta e) {
        //no fa res

    }

    @Override
    public void afegirEnquestaRealitzada(Usuari contexto, Enquesta e) {
        //no fa res
    }

    @Override
    public void eliminarEnquestaAdministrada(Usuari contexto, int idEnquesta) {
        //no fa res
    }

    @Override
    public void eliminarEnquestaRealitzada(Usuari contexto, int idEnquesta) {
        //no fa res
    }

    @Override
    public void eliminarEnquestaModerada(Usuari contexto, int idEnquesta) {
        //no fa res
    }

    @Override
    public void eliminarEnquestaAssignada(Usuari contexto, int idEnquesta) {
        contexto.eliminarEnquestaAssignada(idEnquesta);
    }

    @Override
    public boolean enquestaAssignada(Usuari contexto, int idEnquesta){
        return contexto.enquestaAssignada(idEnquesta);
    }

    @Override
    public boolean esEnquestador() {
        return true;
    }
}
