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
}
