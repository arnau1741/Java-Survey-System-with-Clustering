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
    
}
