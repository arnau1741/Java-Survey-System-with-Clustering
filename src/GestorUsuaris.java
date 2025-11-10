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
                //parar busqueda
                break;
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

    public Usuari seleccionarUsuari(int idUsuari) {
        for (Usuari u : usuaris) {
            if (u.getId() == idUsuari) {
                return u;
            }
        }
        return null;
    }

    //Falta cas d'us gestionar visibilitat (comprobar que sigui l'administrador)
    /*
    public void gestionarVisibilitat(int idUsuari, boolean visibilitat) {
        for (Usuari u : usuaris) {
            if (u.getId() == idUsuari) {
                u.setVisibilitat(visibilitat);
                System.out.println("La visibilitat de l'usuari " + u.getUsuari() + " s'ha actualitzat a: " + visibilitat);
                return;
            }
        }
        System.out.println("Usuari amb ID " + idUsuari + " no trobat.");
    }
    */

    //Falta la opcio de sortir del sistema
    /*
    public int sortir() {
    //S'ha de veure com farem el main o el driver del menu per tal de sortir del sistema
        int numeroOpcio = -1;
        return numeroOpcio;
    }
     */
}
