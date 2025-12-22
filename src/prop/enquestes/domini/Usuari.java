package prop.enquestes.domini;

import java.util.List;
import java.util.Map;

public class Usuari {
    private String nomUsuari;
    private final int idUsuari;
    private String contrasenya;
    private String email;
    private boolean blocked;
    private UsuariState rol;

    /**
     * Constructora de la classe Usuari
     * @param idUsuari
     * @param nomUsuari
     * @param contrasenya
     * @param email
     * @param rol
     */
    public Usuari(int idUsuari, String nomUsuari, String contrasenya, String email, UsuariState rol) {
        this.idUsuari = idUsuari;
        this.nomUsuari = nomUsuari;
        this.contrasenya = contrasenya;
        this.email = email;
        this.blocked = false;
        this.rol = rol;
    }

    /**
     * Constructora de la classe Usuari
     * @param idUsuari
     * @param nomUsuari
     * @param contrasenya
     * @param email
     * @param blocked
     * @param rol
     */
    public Usuari(int idUsuari, String nomUsuari, String contrasenya, String email, boolean blocked, UsuariState rol) {
        this.idUsuari = idUsuari;
        this.nomUsuari = nomUsuari;
        this.contrasenya = contrasenya;
        this.email = email;
        this.blocked = blocked;
        this.rol = rol;
    }
    /**
     * Getter de l'id de l'usuari
     * @return idUsuari
     */
    public int getId() { return idUsuari; }

    /**
     * Getter del nom d'usuari
     * @return nomUsuari
     */
    public String getUsuari() { return nomUsuari; }

    /**
     * Setter del nom d'usuari
     * @param usuari
     */
    public void setUsuari(String usuari) { this.nomUsuari = usuari; }

    /**
     * Getter de la contrasenya
     * @return contrasenya
     */
    public String getContrasenya() { return contrasenya; }

    /**
     * Setter de la contrasenya
     * @param contrasenya
     */
    public void setContrasenya(String contrasenya) { this.contrasenya = contrasenya; }

    /**
     * Getter de l'email
     * @return email
     */
    public String getEmail() { return email; }

    /**
     * Setter de l'email
     * @param email
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Getter de l'estat de bloqueig
     * @return blocked
     */
    public boolean isBlocked() { return blocked; }

    /**
     * Setter de l'estat de bloqueig
     * @param blocked
     */
    public void setBlocked(boolean blocked) { this.blocked = blocked; }

    /**
     * Getter del rol de l'usuari
     * @return rol
     */
    public UsuariState getRol() { return rol; }

    /**
     * Setter del rol de l'usuari
     * @param rol
     */
    protected void setRol(UsuariState rol) { this.rol = rol; }

    /**
     * Canvia el rol de l'usuari a enquestador
     */
    public void cambiarARolEnquestador(){ rol.cambiarARolEnquestador(this); }

    /**
     * Canvia el rol de l'usuari a moderador
     */
    public void cambiarARolAdmin(){ rol.cambiarARolAdmin(this); }

    /**
     * Canvia el rol de l'usuari a moderador
     */
    public void cambiarARolEnquestat(){ rol.cambiarARolEnquestat(this); }

    /**
     * Canvia el rol de l'usuari a moderador
     * @return Map de les enquestes assignades a l'usuari
     */
    public Map<Integer, Enquesta> getEnquestesAssignades() {
        return rol.getEnquestesAssignades();
    }

    /**
     * Afegeix una enquesta a les enquestes assignades de l'usuari
     * @param e
     */
    public void demanarAfegirEnquestaAssignada(Enquesta e){
        rol.afegirEnquestaAssignada(e);
    }

    /**
     * Elimina una enquesta de les enquestes assignades de l'usuari
     * @param idEnquesta
     */
    public void demanarEliminarAssignada(int idEnquesta){
        rol.eliminarEnquestaAssignada(idEnquesta);
    }

    /**
     * Comprova si l'usuari te una enquesta assignada
     * @param idEnquesta
     * @return true si te l'enquesta assignada, false en cas contrari
     */
    public boolean teEnquestaAssignada(int idEnquesta){
        return rol.enquestaAssignada(idEnquesta);
    }

    /**
     * Getter de les enquestes realitzades per l'usuari
     * @return Map de les enquestes realitzades per l'usuari
     */
    public Map<Integer, Enquesta> getEnquestesRealitzades() {
        return rol.getEnquestesRealitzades();
    }

    /**
     * Afegeix una enquesta a les enquestes realitzades de l'usuari
     * @param e
     */
    public void demanarAfegirEnquestaRealitzada(Enquesta e){
        rol.afegirEnquestaRealitzada(e);
    }

    /**
     * Elimina una enquesta de les enquestes realitzades de l'usuari
     * @param idEnquesta
     */
    public void demanarEliminarRealitzada(int idEnquesta){
        rol.eliminarEnquestaRealitzada(idEnquesta);
    }

    /**
     * Comprova si l'usuari te una enquesta realitzada
     * @param idEnquesta
     * @return true si te l'enquesta realitzada, false en cas contrari
     */
    public boolean teEnquestaRealitzada(int idEnquesta){
        return rol.enquestaRealitzada(idEnquesta);
    }

    /**
     * Getter de les enquestes administrades per l'usuari
     * @return Map de les enquestes administrades per l'usuari
     */
    public Map<Integer, Enquesta> getEnquestesAdministrades() {
        return rol.getEnquestesAdministrades();
    }

    /**
     * Afegeix una enquesta a les enquestes administrades de l'usuari
     * @param e
     */
    public void demanarAfegirEnquestaAdministrada(Enquesta e){
        rol.afegirEnquestaAdministrada(e);
    }

    /**
     * Elimina una enquesta de les enquestes administrades de l'usuari
     * @param idEnquesta
     */
    public void demanarEliminarAdministrada(int idEnquesta){
        rol.eliminarEnquestaAdministrada(idEnquesta);
    }

    /**
     * Comprova si l'usuari te una enquesta administrada
     * @param idEnquesta
     * @return true si te l'enquesta administrada, false en cas contrari
     */
    public boolean teEnquestaAdministrada(int idEnquesta){
        return rol.enquestaAdministrada(idEnquesta);
    }

    /**
     * Comprova si l'usuari es admin
     * @return true si es admin, false en cas contrari
     */
    public boolean esAdmin()       { return rol.esAdmin(); }

    /**
     * Comprova si l'usuari es moderador
     * @return true si es moderador, false en cas contrari
     */
    public boolean esModerador()   { return rol.esModerador(); }

    /**
     * Comprova si l'usuari es enquestador
     * @return true si es enquestador, false en cas contrari
     */
    public boolean esEnquestador() { return rol.esEnquestador(); }

    /**
     * Comprova si l'usuari es enquestat
     * @return true si es enquestat, false en cas contrari
     */
    public boolean esEnquestat()   { return rol.esEnquestat(); }

    /**
     * Obte les enquestes per rol
     * @return Llista de les enquestes per rol
     */
    public List<String> obtenirEnquestesPerRol() {
        return rol.obtenirEnquestesPerRol();
    }
}