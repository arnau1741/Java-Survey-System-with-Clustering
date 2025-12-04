package prop.enquestes.domini;
import java.util.ArrayList;
import java.util.List;

public class Usuari {
    private String nomUsuari;
    private final int idUsuari;
    private String contrasenya;
    private String email;
    private boolean blocked;

    //enquestador
    private List<Enquesta> enquestesAssignades;

    //admin i enquestat
    private List<Enquesta> enquestesRealitzades;

    //admin
    private List<Enquesta> enquestesAdministrades;

    //moderador
    private List<Enquesta> enquestesModerades;

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

        this.enquestesAdministrades = new ArrayList<>();
        this.enquestesAssignades = new ArrayList<>();
        this.enquestesRealitzades = new ArrayList<>();
        this.enquestesModerades = new ArrayList<>();
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
    public List<Enquesta> getEnquestesAssignades() { return enquestesAssignades; }

    /**
     * Retorna les enquestes realitzades per l'usuari enquestat
     * @return enquestesRealitzades
     */
    public List<Enquesta> getEnquestesRealitzades() { return enquestesRealitzades; }

    /**
     * Retorna les enquestes administrades per l'usuari administrador
     * @return enquestesAdministrades
     */
    public List<Enquesta> getEnquestesAdministrades() { return enquestesAdministrades; }

    /**
     * Retorna les enquestes moderades per l'usuari moderador
     * @return enquestesModerades
     */
    public List<Enquesta> getEnquestesModerades() { return enquestesModerades; }

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
    public void setEnquestesAssignades(List<Enquesta> enquestesAssignades) {this.enquestesAssignades = enquestesAssignades;}

    /**
     * Modifica les enquestes realitzades per l'usuari enquestat
     * @param enquestesRealitzades
     */
    public void setEnquestesRealitzades(List<Enquesta> enquestesRealitzades) {this.enquestesRealitzades = enquestesRealitzades;}

    /**
     * Modifica les enquestes administrades per l'usuari administrador
     * @param enquestesAdministrades
     */
    public void setEnquestesAdministrades(List<Enquesta> enquestesAdministrades) {this.enquestesAdministrades = enquestesAdministrades;}

    /**
     * Modifica les enquestes moderades per l'usuari moderador
     * @param enquestesModerades
     */
    public void setEnquestesModerades(List<Enquesta> enquestesModerades) {this.enquestesModerades = enquestesModerades;}

    /**
     * Modifica el rol de l'usuari
     * @param rol nou rol de l'usuari
     */
    public void setRol(UsuariState rol) { this.rol = rol; }

    /**
     * Afegeix una enquesta a les enquestes assignades de l'usuari enquestador
     * @param enquesta
     */
    protected void afegirEnquestaAssignada(Enquesta enquesta) {enquestesAssignades.add(enquesta);}

    /**
     * Afegeix una enquesta a les enquestes realitzades de l'usuari enquestat
     * @param enquesta
     */
    protected void afegirEnquestaRealitzada(Enquesta enquesta) {enquestesRealitzades.add(enquesta);}

    /**
     * Afegeix una enquesta a les enquestes administrades de l'usuari administrador
     * @param enquesta
     */
    protected void afegirEnquestaAdministrada(Enquesta enquesta) {enquestesAdministrades.add(enquesta);}

    /**
     * Afegeix una enquesta a les enquestes moderades de l'usuari moderador
     * @param enquesta
     */
    protected void afegirEnquestaModerada(Enquesta enquesta) {enquestesModerades.add(enquesta);}

    public void checkAfegirEnquestaRealitzada(Enquesta e){
        rol.afegirEnquestaRealitzada(this, e);
    }

    public void checkAfegirEnquestaAssignada(Enquesta e){
        rol.afegirEnquestaAssignada(this, e);
    }

    public void checkAfegirEnquestaAdministrada(Enquesta e){
        rol.afegirEnquestaAdministrada(this, e);
    }

    public void checkAfegirEnquestaModerada(Enquesta e){
        rol.afegirEnquestaModerada(this, e);
    }

    /**
     * Elimina una enquesta de les enquestes assignades de l'usuari enquestador
     * @param idEnquesta
     */
    protected void eliminarEnquestaAssignada(int idEnquesta) {enquestesAssignades.removeIf(e -> e.getId() == idEnquesta);}

    /**
     * Elimina una enquesta de les enquestes realitzades de l'usuari enquestat
     * @param idEnquesta
     */
    protected void eliminarEnquestaRealitzada(int idEnquesta) {enquestesRealitzades.removeIf(e -> e.getId() == idEnquesta);}

    /**
     * Elimina una enquesta de les enquestes administrades de l'usuari administrador
     * @param idEnquesta
     */
    protected void eliminarEnquestaAdministrada(int idEnquesta) {enquestesAdministrades.removeIf(e -> e.getId() == idEnquesta);}

    /**
     * Elimina una enquesta de les enquestes moderades de l'usuari moderador
     * @param idEnquesta
     */
    protected void eliminarEnquestaModerada(int idEnquesta) {enquestesModerades.removeIf(e -> e.getId() == idEnquesta);}

    public void demanarEliminarAssignada(int idEnquesta){
        rol.eliminarEnquestaAssignada(this, idEnquesta);
    }

    public void demanarEliminarRealitzada(int idEnquesta){
        rol.eliminarEnquestaRealitzada(this, idEnquesta);
    }

    public void demanarEliminarAdministrada(int idEnquesta){
        rol.eliminarEnquestaAdministrada(this, idEnquesta);
    }

    public void demanarEliminarModerada(int idEnquesta){
        rol.eliminarEnquestaModerada(this, idEnquesta);
    }

    /**
     * Comprova si una enquesta esta assignada a l'usuari enquestador
     * @param idEnquesta
     * @return true si l'enquesta esta assignada, false en cas contrari
     */
    protected boolean enquestaAssignada(int idEnquesta) {
        for (Enquesta enquesta : enquestesAssignades) {
            if (enquesta.getId() == idEnquesta) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprova si una enquesta ha estat realitzada per l'usuari enquestat
     * @param idEnquesta
     * @return true si l'enquesta ha estat realitzada, false en cas contrari
     */
    protected boolean haRealitzatEnquesta(int idEnquesta) {
        for (Enquesta enquesta : enquestesRealitzades) {
            if (enquesta.getId() == idEnquesta) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprova si una enquesta esta administrada per l'usuari administrador
     * @param idEnquesta
     * @return true si l'enquesta esta administrada, false en cas contrari
     */
    protected boolean enquestaAdministrada(int idEnquesta) {
        for (Enquesta enquesta : enquestesAdministrades) {
            if (enquesta.getId() == idEnquesta) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprova si una enquesta esta moderada per l'usuari moderador
     * @param idEnquesta
     * @return true si l'enquesta esta moderada, false en cas contrari
     */
    protected boolean enquestaModerada(int idEnquesta) {
        for (Enquesta enquesta : enquestesModerades) {
            if (enquesta.getId() == idEnquesta) {
                return true;
            }
        }
        return false;
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

    public boolean teEnquestaModerada(int idEnquesta){
        return rol.enquestaModerada(this, idEnquesta);
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