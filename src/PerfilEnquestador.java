import java.util.List;

public class PerfilEnquestador extends Usuari
{
    private List<Enquesta> enquestesAssignades;
    private List<Enquesta> enquestesRealitzada;
    // Constructor
    public PerfilEnquestador(int idUsuari, String nomUsuari, String contrasenya, String email)  {
        super(idUsuari, nomUsuari,contrasenya, email);
        enquestesAssignades = new java.util.ArrayList<>();
    }

    public boolean enquestaAssignada(int idEnquesta) {
        for (Enquesta e : enquestesAssignades) {
            if (e.getId() == idEnquesta) {
                return true;
            }
        }
        return false;
    }

    public void afegirEnquestaAssignada(Enquesta enquesta) {
        enquestesAssignades.add(enquesta);
    }

    public void afegirEnquestaRealitzada(Enquesta enquesta) {
        enquestesRealitzada.add(enquesta);
    }

    public void eliminarEnquestaAssignada(int idEnquesta) {
        enquestesAssignades.removeIf(e -> e.getId() == idEnquesta);
    }

    public void eliminarEnquestaRealitzada(int idEnquesta) {
        enquestesRealitzada.removeIf(e -> e.getId() == idEnquesta);
    }

    public List<Enquesta> getEnquestesAssignades() {
        return enquestesAssignades;
    }

    public void eliminarEnquesta(int idEnquesta) {
        eliminarEnquestaAssignada(idEnquesta);
        eliminarEnquestaRealitzada(idEnquesta);
    }
}
