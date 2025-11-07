import java.util.List;
import java.util.ArrayList;

public abstract class Usuari {
    private int id;
    private String usuari;
    private String rol;
    private boolean registrat;

    private String email;
    private boolean blocked;

    // Constructor
    public Usuari(int id, String usuari, boolean registrat, String rol) {
        this.id = id;
        this.usuari = usuari;
        this.registrat = registrat;
        this.rol = rol;

        //Si registrat, trucar a funcio per a demanar email
        this.email = registrat ? email : null;
        this.blocked = false;
    }


    //Responder enquesta

    //Get functions
    public int getId() { return id; }
    public String getUsuari() { return usuari; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
    public boolean isRegistrat() { return registrat; }

    public void setId(int id) { this.id = id; }
    public void setUsuari(String usuari) { this.usuari = usuari; }
    public void setEmail(String email) { this.email = email; }
    public void setRol(String especialitat) { this.rol = rol; }
    public void setRegistrat(boolean registrat) { this.registrat = registrat; }
}

