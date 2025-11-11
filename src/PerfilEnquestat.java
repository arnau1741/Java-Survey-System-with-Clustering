import java.util.List;

public class PerfilEnquestat extends Usuari
{
    private List<Enquesta> enquestesRealitzades;
    // Constructor
    public PerfilEnquestat(int idUsuari, String nomUsuari, String contrasenya, String email)  {
        super(idUsuari, nomUsuari,contrasenya, email);
        enquestesRealitzades = new java.util.ArrayList<>();
    }

    public boolean haRealitzatEnquesta(int idEnquesta) {
        for (Enquesta e : enquestesRealitzades) {
            if (e.getId() == idEnquesta) {
                return true;
            }
        }
        return false;
    }

    public void afegirEnquestaRealitzada(Enquesta enquesta) {
        enquestesRealitzades.add(enquesta);
    }

    public void eliminarEnquestaRealitzada(int idEnquesta) {
        enquestesRealitzades.removeIf(e -> e.getId() == idEnquesta);
    }

    public List<Enquesta> getEnquestesRealitzades() {
        return enquestesRealitzades;
    }
}
