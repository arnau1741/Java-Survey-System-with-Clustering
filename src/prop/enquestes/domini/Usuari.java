package prop.enquestes.domini;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Usuari {
    private String nomUsuari;
    private final int idUsuari;
    private String contrasenya;
    private String email;
    private boolean blocked;

    //enquestador
    //private List<Enquesta> enquestesAssignades;
    private Map<Integer, Enquesta> enquestesAssignades;

    //admin i enquestat
    //private List<Enquesta> enquestesRealitzades;
    private Map<Integer, Enquesta> enquestesRealitzades;

    //admin
    //private List<Enquesta> enquestesAdministrades;
    private Map<Integer, Enquesta> enquestesAdministrades;

    private UsuariState rol;

    /**
     * Funcio constructora de la classe Usuari
     * @param idUsuari de l'usuari
     * @param nomUsuari de l'usuari
     * @param contrasenya de l'usuari
     * @param email de l'usuari
     */
    public Usuari(int idUsuari, String nomUsuari, String contrasenya, String email, UsuariState rol) {
        this.idUsuari = idUsuari;
        this.nomUsuari = nomUsuari;
        this.contrasenya = contrasenya;
        this.email = email;
        this.blocked = false;

        this.enquestesAdministrades = new HashMap<>();
        this.enquestesAssignades = new HashMap<>();
        this.enquestesRealitzades = new HashMap<>();
        this.rol = rol;
    }

    // Getters
    /**
     * Retorna l'identificador de l'usuari
     * @return idUsuari
     */
    public int getId() { return idUsuari; }

    /**
     * Retorna el nom de l'usuari
     * @return nomUsuari
     */
    public String getUsuari() { return nomUsuari; }

    /**
     * Retorna la contrasenya de l'usuari
     * @return contrasenya
     */
    public String getContrasenya() { return contrasenya; }

    /**
     * Retorna l'email de l'usuari
     * @return email
     */
    public String getEmail() { return email; }

    /**
     * Retorna les enquestes assignades a l'usuari enquestador
     * @return enquestesAssignades
     */
    public Map<Integer, Enquesta> getEnquestesAssignades() { return enquestesAssignades; }

    /**
     * Retorna les enquestes realitzades per l'usuari enquestat
     * @return enquestesRealitzades
     */
    public Map<Integer, Enquesta> getEnquestesRealitzades() { return enquestesRealitzades; }

    /**
     * Retorna les enquestes administrades per l'usuari administrador
     * @return enquestesAdministrades
     */
    public Map<Integer, Enquesta> getEnquestesAdministrades() { return enquestesAdministrades; }

    /**
     * Retorna el rol de l'usuari
     * @return rol
     */
    public UsuariState getRol() { return rol; }

    /**
     * Retorna true si l'usuari esta bloquejat, false en cas contrari
     * @return blocked
     */
    public boolean isBlocked() { return blocked; }

    // Setters
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

    /**
     * Modifica les enquestes assignades a l'usuari enquestador
     * @param enquestesAssignades
     */
    public void setEnquestesAssignades(Map<Integer, Enquesta> enquestesAssignades) {this.enquestesAssignades = enquestesAssignades;}

    /**
     * Modifica les enquestes realitzades per l'usuari enquestat
     * @param enquestesRealitzades
     */
    public void setEnquestesRealitzades(Map<Integer, Enquesta> enquestesRealitzades) {this.enquestesRealitzades = enquestesRealitzades;}

    /**
     * Modifica les enquestes administrades per l'usuari administrador
     * @param enquestesAdministrades
     */
    public void setEnquestesAdministrades(Map<Integer, Enquesta> enquestesAdministrades) {this.enquestesAdministrades = enquestesAdministrades;}

    /**
     * Modifica el rol de l'usuari
     * @param rol nou rol de l'usuari
     */
    protected void setRol(UsuariState rol) { this.rol = rol; }

    public void cambiarARolEnquestador(){
        rol.cambiarARolEnquestador(this);
    }

    public void cambiarARolAdmin(){
        rol.cambiarARolAdmin(this);
    }

    public void cambiarARolEnquestat(){
        rol.cambiarARolEnquestat(this);
    }

    /**
     * Afegeix una enquesta a les enquestes assignades de l'usuari enquestador
     * @param enquesta
     */
    protected void afegirEnquestaAssignada(Enquesta enquesta) {enquestesAssignades.put(enquesta.getId(), enquesta);}

    /**
     * Afegeix una enquesta a les enquestes realitzades de l'usuari enquestat
     * @param enquesta
     */
    protected void afegirEnquestaRealitzada(Enquesta enquesta) {enquestesRealitzades.put(enquesta.getId(), enquesta);}

    /**
     * Afegeix una enquesta a les enquestes administrades de l'usuari administrador
     * @param enquesta
     */
    protected void afegirEnquestaAdministrada(Enquesta enquesta) {enquestesAdministrades.put(enquesta.getId(), enquesta);}


    public void demanarAfegirEnquestaRealitzada(Enquesta e){rol.afegirEnquestaRealitzada(this, e);}

    public void demanarAfegirEnquestaAssignada(Enquesta e){
        rol.afegirEnquestaAssignada(this, e);
    }

    public void demanarAfegirEnquestaAdministrada(Enquesta e){rol.afegirEnquestaAdministrada(this, e);}

    /**
     * Elimina una enquesta de les enquestes assignades de l'usuari enquestador
     * @param idEnquesta
     */
    protected void eliminarEnquestaAssignada(int idEnquesta) {enquestesAssignades.remove(idEnquesta);}

    /**
     * Elimina una enquesta de les enquestes realitzades de l'usuari enquestat
     * @param idEnquesta
     */
    protected void eliminarEnquestaRealitzada(int idEnquesta) {enquestesRealitzades.remove(idEnquesta);}

    /**
     * Elimina una enquesta de les enquestes administrades de l'usuari administrador
     * @param idEnquesta
     */
    protected void eliminarEnquestaAdministrada(int idEnquesta) {enquestesAdministrades.remove(idEnquesta);}

    public void demanarEliminarAssignada(int idEnquesta){
        rol.eliminarEnquestaAssignada(this, idEnquesta);
    }

    public void demanarEliminarRealitzada(int idEnquesta){rol.eliminarEnquestaRealitzada(this, idEnquesta);}

    public void demanarEliminarAdministrada(int idEnquesta){
        rol.eliminarEnquestaAdministrada(this, idEnquesta);
    }

    /**
     * Comprova si una enquesta esta assignada a l'usuari enquestador
     * @param idEnquesta
     * @return true si l'enquesta esta assignada, false en cas contrari
     */
    protected boolean enquestaAssignada(int idEnquesta) {
        return enquestesAssignades.containsKey(idEnquesta);
    }

    /**
     * Comprova si una enquesta ha estat realitzada per l'usuari enquestat
     * @param idEnquesta
     * @return true si l'enquesta ha estat realitzada, false en cas contrari
     */
    protected boolean haRealitzatEnquesta(int idEnquesta) {
        return enquestesRealitzades.containsKey(idEnquesta);
    }

    /**
     * Comprova si una enquesta esta administrada per l'usuari administrador
     * @param idEnquesta
     * @return true si l'enquesta esta administrada, false en cas contrari
     */
    protected boolean enquestaAdministrada(int idEnquesta) {
        return enquestesAdministrades.containsKey(idEnquesta);
    }

    public boolean teEnquestaAssignada(int idEnquesta){
        return rol.enquestaAssignada(this, idEnquesta);
    }

    public boolean teEnquestaAdministrada(int idEnquesta){
        return rol.enquestaAdministrada(this, idEnquesta);
    }

    public boolean teEnquestaRealitzada(int idEnquesta){
        return rol.enquestaRealitzada(this, idEnquesta);
    }

    public boolean esAdmin(){
        return rol.esAdmin();
    }

    public boolean esModerador(){
        return rol.esModerador();
    }

    public boolean esEnquestador(){
        return rol.esEnquestador();
    }

    public boolean esEnquestat(){
        return rol.esEnquestat();
    }
}