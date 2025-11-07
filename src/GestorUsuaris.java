import java.util.List;
import java.util.ArrayList;

public class GestorUsuaris {
    // List to store users
    private List<Usuari> usuaris;

    // Constructor
    public GestorUsuaris() {
        usuaris = new ArrayList<>();
    }

    // Metode per a crear un perfil
    public void crearPerfil(Usuari usuari) {
        this.usuaris.add(usuari);
    }

    //Metode per a consultar la informacio del perfil
    public void consultarPerfil(int id) {
        for (Usuari u : usuaris) {
            if (u.getId() == id) {
                System.out.println(
                        "ID: " + u.getId()
                                + ", Usuari: " + u.getUsuari()
                                + ", Email: " + u.getEmail()
                                + ", Especialitat: " + (u.getRol())
                );
            }
        }
    }

    // Metode per a llistar tots els usuaris
    public void llistarUsuaris() {
        for (Usuari u : usuaris) {
            System.out.println(
                    "ID: " + u.getId()
                            + ", Usuari: " + u.getUsuari()
                            + ", Email: " + u.getEmail()
                            + ", Especialitat: " + (u.getRol())
            );
        }
    }
}
