public abstract class Usuari {
    private int id;
    private String usuari;
    private String email;
    private String rol;
    private boolean registrat;

    // Constructor
    public Usuari(int id, String usuari, String email) {
        this.id = id;
        this.usuari = usuari;
        this.email = email;
        this.rol = rol;
        this.registrat = registrat;
    }

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

