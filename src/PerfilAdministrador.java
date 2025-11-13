import java.util.List;

public class PerfilAdministrador extends Usuari
{
    private List<Enquesta> enquestesAdministrades;
    private List<Enquesta> enquestesAssignades;
    private List<Enquesta> enquestesRealitzada;
    // Constructor
    public PerfilAdministrador(int idUsuari, String nomUsuari, String contrasenya, String email)  {
        super(idUsuari, nomUsuari,contrasenya, email);
        enquestesAdministrades = new java.util.ArrayList<>();
    } 

    public boolean enquestaAdministrada(int idEnquesta) {
        for (Enquesta e : enquestesAdministrades) {
            if (e.getId() == idEnquesta) {
                return true;
            }
        }
        return false;
    }


    public List<Enquesta> getEnquestesAdministrades() {
        return enquestesAdministrades;
    }

    public List<Enquesta> getEnquestesAssignades() {
        return enquestesAssignades;
    }

    public List<Enquesta> getEnquestesRealitzada() {
        return enquestesRealitzada;
    }

    public void afegirEnquestaAssignada(Enquesta enquesta) {
        enquestesAssignades.add(enquesta);
    }

    public void afegirEnquestaRealitzada(Enquesta enquesta) {
        enquestesRealitzada.add(enquesta);
    }

    public void afegirEnquestaAdministrada(Enquesta enquesta) {
        enquestesAdministrades.add(enquesta);
    }

    public void eliminarEnquestaAssignada(int idEnquesta) {
        enquestesAssignades.removeIf(e -> e.getId() == idEnquesta);
    }

    public void eliminarEnquestaRealitzada(int idEnquesta) {
        enquestesRealitzada.removeIf(e -> e.getId() == idEnquesta);
    }

    public void eliminarEnquestaAdministrada(int idEnquesta) {
        enquestesAdministrades.removeIf(e -> e.getId() == idEnquesta);
    }

}
