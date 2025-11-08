import java.util.List;
import java.util.ArrayList;

//patron estado para cambiar entre roles

public abstract class Usuari {
    private int id;
    private String usuari;
    private String rol;

    private String email;
    private boolean blocked;

    // Constructor
    public Usuari(int id, String usuari, boolean registrat, String rol) {
        this.id = id;
        this.usuari = usuari;
        this.rol = rol;

        //Si registrat, trucar a funcio per a demanar email
        this.email = registrat ? email : null;
        this.blocked = false;
    }

 

    //editar una enquesta (administrador) (no para primera entrega)

    //eliminar una enquesta (administrador) (no para primera entrega)

    //Gestionar viabilitat de la enquesta (administrador)

    //Afegir respostes a una enquesta assignada (Enquestador)

    //Revisar una enquesta (moderador)

    //Bloquejar una enquesta (moderador)

    //Gestionar usuaris (moderador)



    //Get functions
    public int getId() { return id; }
    public String getUsuari() { return usuari; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
    public boolean isBlocked() { return blocked; }

    public void setId(int id) { this.id = id; }
    public void setUsuari(String usuari) { this.usuari = usuari; }
    public void setEmail(String email) { this.email = email; }
    public void setRol(String rol) { this.rol = rol; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }
}

