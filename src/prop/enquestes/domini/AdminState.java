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
}
