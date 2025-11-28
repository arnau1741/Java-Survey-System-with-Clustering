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
}
