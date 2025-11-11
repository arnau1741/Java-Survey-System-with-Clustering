//patron estado para cambiar entre roles

public abstract class Usuari {
    private String nomUsuari;
    private final int idUsuari;
    private String contrasenya;
    private String email;
    private boolean blocked;

    // Constructor
    public Usuari(int idUsuari, String nomUsuari, String contrasenya, String email) {
        this.idUsuari = idUsuari;
        this.nomUsuari = nomUsuari;
        this.contrasenya = contrasenya;
        this.email = email;
        this.blocked = false;
    }

    //Get functions
    public int getId() { return idUsuari; }
    public String getUsuari() { return nomUsuari; }
    public String getContrasenya() { return contrasenya; }
    public String getEmail() { return email; }
    public boolean isBlocked() { return blocked; }

    //Set functions
    public void setUsuari(String usuari) { this.nomUsuari = usuari; }
    public void setEmail(String email) { this.email = email; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }
}

