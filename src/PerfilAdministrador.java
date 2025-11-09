import java.util.List;

public class PerfilAdministrador extends Usuari
{
    private List<Enquesta> enquestesAdministrades;
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

    public void afegirEnquestaAdministrada(Enquesta enquesta) {
        enquestesAdministrades.add(enquesta);
    }

    public List<Enquesta> getEnquestesAdministrades() {
        return enquestesAdministrades;
    }
    
}
