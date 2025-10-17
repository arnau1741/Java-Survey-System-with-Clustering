public abstract class Usuari {
    private int id;
    private String usuari;
    private String email;

    // Constructor
    public Usuari(int id, String usuari, String email) {
        this.id = id;
        this.email = email;
        this.usuari = usuari;
    }


    //para el test de pruebas de las subclases, ya te indica el programa que tiene dos implementaciones diferentes
    abstract void setEspecialitat(String especialitat);
    abstract String getEspecialitat();

    //Get functions
    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getUsuari() { return usuari; }

    public void setId(int id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setUsuari(String usuari) { this.usuari = usuari; }
}

