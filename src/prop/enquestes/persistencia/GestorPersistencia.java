package prop.enquestes.persistencia;

import prop.enquestes.domini.Enquesta;
import prop.enquestes.domini.Usuari;

import java.util.Map;

public class GestorPersistencia {
    private GestorUsuari gestorUsuari;
    private GestorEnquesta gestorEnquesta;

    /**
     * Constructor de la classe GestorPersistencia que inicialitza els gestors de
     * usuaris i enquestes.
     */
    public GestorPersistencia() {
        this.gestorUsuari = new GestorUsuari();
        this.gestorEnquesta = new GestorEnquesta();
    }

    /**
     * Guarda els usuaris proporcionats utilitzant el gestor d'usuaris.
     * @param usuaris
     */
    public void guardarUsuaris(Map<Integer, Usuari> usuaris) {
        gestorUsuari.guardarUsuaris(usuaris);
    }

    /**
     * Carrega i retorna els usuaris utilitzant el gestor d'usuaris.
     * @return Map<Integer, Usuari>
     */
    public Map<Integer, Usuari> carregarUsuaris( Map<Integer, Enquesta> totesEnquestes) {
        return gestorUsuari.carregarUsuaris(totesEnquestes);
    }

    /**
     * Guarda les enquestes proporcionades utilitzant el gestor d'enquestes.
     * @param enquestes
     */
    public void guardarEnquestes(Map<Integer, Enquesta> enquestes) {
        gestorEnquesta.guardarEnquestes(enquestes);
    }

    /**
     * Carrega i retorna les enquestes utilitzant el gestor d'enquestes.
     * @return Map<Integer, Enquesta>
     */
    public Map<Integer, Enquesta> carregarEnquestes() {
        return gestorEnquesta.carregarEnquestes();
    }
}
