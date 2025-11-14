//patron estado para cambiar entre roles

public abstract class Usuari {
    private String nomUsuari;
    private final int idUsuari;
    private String contrasenya;
    private String email;
    private boolean blocked;

    // Constructor
    /**
     * Funcio constructora de la classe Usuari
     * @param idUsuari de l'usuari
     * @param nomUsuari de l'usuari
     * @param contrasenya de l'usuari
     * @param email de l'usuari
     */
    public Usuari(int idUsuari, String nomUsuari, String contrasenya, String email) {
        this.idUsuari = idUsuari;
        this.nomUsuari = nomUsuari;
        this.contrasenya = contrasenya;
        this.email = email;
        this.blocked = false;
    }

    //Get functions
    /**
     * Retorna l'id de l'usuari
     * @return int idUsuari
     */
    public int getId() { return idUsuari; }

    /**
     * Retorna el nom de l'usuari
     * @return string nomUsuari
     */
    public String getUsuari() { return nomUsuari; }

    /**
     * Retorna la contrasenya de l'usuari
     * @return string contrasenya
     */
    public String getContrasenya() { return contrasenya; }

    /**
     * Retorna l'email de l'usuari
     * @return string email
     */
    public String getEmail() { return email; }

    /**
     * Retorna si l'usuari esta bloquejat
     * @return boolean blocked
     */
    public boolean isBlocked() { return blocked; }

    //Set functions
    /**
     * Modifica el nom de l'usuari
     * @param usuari nou nom de l'usuari
     */
    public void setUsuari(String usuari) { this.nomUsuari = usuari; }

    /**
     * Modifica la contrasenya de l'usuari
     * @param contrasenya nova contrasenya de l'usuari
     */
    public void setContrasenya(String contrasenya) { this.contrasenya = contrasenya; }

    /**
     * Modifica l'email de l'usuari
     * @param email nou email de l'usuari
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Modifica l'estat de bloqueig de l'usuari
     * @param blocked nou estat de bloqueig de l'usuari
     */
    public void setBlocked(boolean blocked) { this.blocked = blocked; }

    // Metodes abstractes
    /**
     * Metode abstracte per eliminar una enquesta
     * @param idEnquesta de l'enquesta
     */
    public abstract void eliminarEnquesta(int idEnquesta);
}

