package prop.enquestes.domini;

import java.util.Map;

public class Usuari {
    private String nomUsuari;
    private final int idUsuari;
    private String contrasenya;
    private String email;
    private boolean blocked;
    private UsuariState rol;

    public Usuari(int idUsuari, String nomUsuari, String contrasenya, String email, UsuariState rol) {
        this.idUsuari = idUsuari;
        this.nomUsuari = nomUsuari;
        this.contrasenya = contrasenya;
        this.email = email;
        this.blocked = false;
        this.rol = rol;
    }

    public int getId() { return idUsuari; }

    public String getUsuari() { return nomUsuari; }
    public void setUsuari(String usuari) { this.nomUsuari = usuari; }

    public String getContrasenya() { return contrasenya; }
    public void setContrasenya(String contrasenya) { this.contrasenya = contrasenya; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isBlocked() { return blocked; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }

    public UsuariState getRol() { return rol; }

    // Setter protegit per permetre el canvi d'estat
    protected void setRol(UsuariState rol) { this.rol = rol; }


    public void cambiarARolEnquestador(){ rol.cambiarARolEnquestador(this); }
    public void cambiarARolAdmin(){ rol.cambiarARolAdmin(this); }
    public void cambiarARolEnquestat(){ rol.cambiarARolEnquestat(this); }


    public Map<Integer, Enquesta> getEnquestesAssignades() {
        return rol.getEnquestesAssignades();
    }

    public void demanarAfegirEnquestaAssignada(Enquesta e){
        rol.afegirEnquestaAssignada(e);
    }

    public void demanarEliminarAssignada(int idEnquesta){
        rol.eliminarEnquestaAssignada(idEnquesta);
    }

    public boolean teEnquestaAssignada(int idEnquesta){
        return rol.enquestaAssignada(idEnquesta);
    }


    public Map<Integer, Enquesta> getEnquestesRealitzades() {
        return rol.getEnquestesRealitzades();
    }

    public void demanarAfegirEnquestaRealitzada(Enquesta e){
        rol.afegirEnquestaRealitzada(e);
    }

    public void demanarEliminarRealitzada(int idEnquesta){
        rol.eliminarEnquestaRealitzada(idEnquesta);
    }

    public boolean teEnquestaRealitzada(int idEnquesta){
        return rol.enquestaRealitzada(idEnquesta);
    }


    public Map<Integer, Enquesta> getEnquestesAdministrades() {
        return rol.getEnquestesAdministrades();
    }

    public void demanarAfegirEnquestaAdministrada(Enquesta e){
        rol.afegirEnquestaAdministrada(e);
    }

    public void demanarEliminarAdministrada(int idEnquesta){
        rol.eliminarEnquestaAdministrada(idEnquesta);
    }

    public boolean teEnquestaAdministrada(int idEnquesta){
        return rol.enquestaAdministrada(idEnquesta);
    }

    public boolean esAdmin()       { return rol.esAdmin(); }
    public boolean esModerador()   { return rol.esModerador(); }
    public boolean esEnquestador() { return rol.esEnquestador(); }
    public boolean esEnquestat()   { return rol.esEnquestat(); }
}