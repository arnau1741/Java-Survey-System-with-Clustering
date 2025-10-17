import java.util.List;
import java.util.ArrayList;

public class GestorUsuaris {
    // List to store users
    private List<Usuari> usuaris;

    // Constructor
    public GestorUsuaris() {
        usuaris = new ArrayList<>();
    }

    //esto es una prueba de mieirda




    // Method to add a user
    public void afegirUsuari(Usuari usuari) {
        this.usuaris.add(usuari);
    }

    // Method to list all users
    public void llistarUsuaris() {
        for (Usuari u : usuaris) {
            System.out.println(
                    "ID: " + u.getId()
                            + ", Usuari: " + u.getUsuari()
                            + ", Email: " + u.getEmail()
                            + ", Especialitat: " + (u.getEspecialitat())
            );
        }
    }
}
