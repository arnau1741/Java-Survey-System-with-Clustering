package prop.enquestes.controladors;
import java.io.StringReader;
import java.util.*;

import prop.enquestes.domini.*;
import prop.enquestes.excepcions.UsuariNoValid;

public class CtrlDominiMantUsuari {
    private Map<Integer, Usuari> usuaris;
    private Map<String, Integer> nomUsuariToID;
    private Integer ultimID;

    /**
     * Constructor de la classe CtrlDominiMantUsuari
     */
    public CtrlDominiMantUsuari() {
        usuaris = new HashMap<>();
        nomUsuariToID = new HashMap<>();
    }

    //////////////////////////// Persistencia
    /**
     * Estableix els usuaris del sistema, funcio de persistencia
     * @param usuaris
     */
    public void setUsuaris(Map<Integer, Usuari> usuaris) {
        this.usuaris = usuaris;
        for (Integer id : usuaris.keySet()){
            nomUsuariToID.put(usuaris.get(id).getUsuari(), id);
        }
        ultimID = usuaris.size();
    }

    /**
     * Retorna els usuaris del sistema, funcio de persistencia
     * @return usuaris
     */
    public Map<Integer, Usuari> getUsuaris() {
        return usuaris;
    }
    ////////////////////////////

    /**
     * Vetar un usuari pel seu id
     * @param idUsuari
     */
    public void vetarUsuari(int idUsuari){
        Usuari u = usuaris.get(idUsuari);
        if(u != null){
            u.setBlocked(true);
        }
    }

    /**
     * Desvetar un usuari pel seu id
     * @param idUsuari
     */
    public void desvetarUsuari(int idUsuari){
        Usuari u = usuaris.get(idUsuari);
        if(u != null){
            u.setBlocked(false);
        }
    }


    /**
     * Afegeix un usuari al sistema
     * @param usuari a afegir
     */
    public void afegirUsuari(Usuari usuari) {
        usuaris.put(usuari.getId(), usuari);
    }


    /**
     * Retorna el nombre d'usuaris del sistema
     * @return nombre d'usuaris
     */
    public int getNumUsuaris() {
        return usuaris.size();
    }

    /**
     * Retorna l'usuari amb l'identificador especificat
     * @param idUsuari de l'usuari a retornar
     * @return usuari amb l'identificador especificat
     */
    public Usuari getUsuari(int idUsuari) {
        return usuaris.get(idUsuari);
    }

    public List<String> obtenirNomsUsuaris() {
        return new ArrayList<>(nomUsuariToID.keySet());
    }

    /**
     * Comprova si existeix un usuari amb el nom especificat
     * @param nomUsuari de l'usuari a cercar
     * @return true si existeix, false en cas contrari
     */
    public boolean existeixUsuari(String nomUsuari) {
        for (Usuari u : usuaris.values()) {
            if (u.getUsuari().equals(nomUsuari)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprova si existeix un usuari amb l'identificador especificat
     * @param IdUsuari de l'usuari a cercar
     * @return true si existeix, false en cas contrari
     */
    public boolean existeixUsuariId(int IdUsuari){
        return usuaris.containsKey(IdUsuari);
    }

    /**
     * Comprova si un email ja està en ús per algun usuari
     * @param email
     * @return true si l'email ja està en ús, false en cas contrari
     */
    public boolean emailUsat(String email){
        for (Usuari u : usuaris.values()) {
            if (u.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna l'usuari amb el nom especificat
     * @param nomUsuari del usuari a retornar
     * @return usuari amb el nom especificat
     */
    public Usuari getUsuariPerNom(String nomUsuari) {
        for (Usuari u : usuaris.values()) {
            if (u.getUsuari().equals(nomUsuari)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Retorna el rol de l'usuari amb l'identificador especificat
     * @param idUsuari
     * @return rol de l'usuari
     */
    public String getRolUsuari(int idUsuari) {
        Usuari u = getUsuari(idUsuari);
        UsuariState rol = u.getRol();
        return rol.nombreRol();
    }

    /**
     * Inicia sessió d'un usuari
     * @param nomUsuari
     * @param password
     * @return codi d'error
     */
    public int iniciarSessio(String nomUsuari, String password){
        //comprovem si existeix un usuari amb nomUsuari
        if (!nomUsuariToID.containsKey(nomUsuari)) throw new IllegalArgumentException("L'usuari no existeix.");
        Integer id = nomUsuariToID.get(nomUsuari);
        String correctPassword = usuaris.get(id).getContrasenya();
        Usuari us = usuaris.get(id);
        if(us.isBlocked()) throw new IllegalArgumentException("L'usuari està vetat.");

        //comprovem password
        if(correctPassword.equals(password)) {
            return us.getId();
        }
        else throw new IllegalArgumentException("Contrasenya incorrecta.");
    }

    /**
     * Genera un nou ID per a un usuari
     * @return nou ID
     */
    public int getNouID(){
        int tmp = ultimID;
        ultimID++;
        return tmp;
    }
}
