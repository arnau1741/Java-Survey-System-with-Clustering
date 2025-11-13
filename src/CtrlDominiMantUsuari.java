import java.util.*;

public class CtrlDominiMantUsuari {
    private Map<Integer, Usuari> usuaris;

    public CtrlDominiMantUsuari() {
        usuaris = new HashMap<>();
    }

    public void afegirUsuari(Usuari usuari) {
        usuaris.put(usuari.getId(), usuari);
    }

    public int getNumUsuaris() {
        return usuaris.size();
    }

    public Usuari getUsuari(int idUsuari) {
        return usuaris.get(idUsuari);
    }

    public boolean existeixUsuari(String nomUsuari) {
        for (Usuari u : usuaris.values()) {
            if (u.getUsuari().equals(nomUsuari)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeixUsuariId(int IdUsuari){
        return usuaris.containsKey(IdUsuari);
    }

    public Usuari getUsuariPerNom(String nomUsuari) {
        for (Usuari u : usuaris.values()) {
            if (u.getUsuari().equals(nomUsuari)) {
                return u;
            }
        }
        return null;
    }

    public void reemplacarUsuari(Usuari usuariAntic, Usuari usuariNou) {
        usuaris.put(usuariAntic.getId(), usuariNou);
    }
    
}
