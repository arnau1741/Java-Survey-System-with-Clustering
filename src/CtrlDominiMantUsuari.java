import java.util.*;

public class CtrlDominiMantUsuari {
    private Map<Integer, Usuari> usuaris;

    /**
     * Constructor de la classe CtrlDominiMantUsuari
     */
    public CtrlDominiMantUsuari() {
        usuaris = new HashMap<>();
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
     * Retorna l'usuari amb l'id especificat
     * @param idUsuari de l'usuari a retornar
     * @return usuari amb l'id especificat
     */
    public Usuari getUsuari(int idUsuari) {
        return usuaris.get(idUsuari);
    }

    /**
     * Comprova si existeix un usuari amb el nom especificat
     * @param nomUsuari del usuari a cercar
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
     * Comprova si existeix un usuari amb l'id especificat
     * @param IdUsuari de l'usuari a cercar
     * @return true si existeix, false en cas contrari
     */
    public boolean existeixUsuariId(int IdUsuari){
        return usuaris.containsKey(IdUsuari);
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
     * Reemplaca un usuari antic per un usuari nou
     * Aquesta funcio cambiara quan s'apliqui el patro estat en futures entregues
     * @param usuariAntic a ser reemplaçat
     * @param usuariNou a reemplaçar
     */
    public void reemplacarUsuari(Usuari usuariAntic, Usuari usuariNou) {
        usuaris.put(usuariAntic.getId(), usuariNou);
    }
    
}
