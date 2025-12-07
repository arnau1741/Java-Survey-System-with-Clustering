package prop.enquestes.persistencia;

import prop.enquestes.domini.Enquesta;
import prop.enquestes.domini.Usuari;

import java.util.Map;

public class GestorPersistencia {
    private GestorUsuari gestorUsuari;
    private GestorEnquesta gestorEnquesta;

    public GestorPersistencia() {
        this.gestorUsuari = new GestorUsuari();
        this.gestorEnquesta = new GestorEnquesta();
    }

    public void guardarUsuaris(Map<Integer, Usuari> usuaris) {
        gestorUsuari.guardarUsuaris(usuaris);
    }

    public Map<Integer, Usuari> carregarUsuaris() {
        return gestorUsuari.carregarUsuaris();
    }

    public void guardarEnquestes(Map<Integer, Enquesta> enquestes) {
        gestorEnquesta.guardarEnquestes(enquestes);
    }

    public Map<Integer, Enquesta> carregarEnquestes() {
        return gestorEnquesta.carregarEnquestes();
    }
}
