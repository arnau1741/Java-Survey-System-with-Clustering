import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestorEnquesta {
    // gestiona totes les enquestes creades
    private List<Enquesta> enquestes;

    //Cas d'us crear enquesta
    public GestorEnquesta() {
        enquestes = new ArrayList<>();
    }

    public void afegirEnquesta(Enquesta enquesta) {
        if (getEnquestaPerID(enquesta.getId()) != null) {
            System.out.println("Ja existeix una enquesta amb aquest ID: " + enquesta.getId());
        }
        else enquestes.add(enquesta);;
        //añadiria el añadir ya participante, respuestas, etc.
    }

    public Enquesta getEnquestaPerID(int id) {
        for (Enquesta e : enquestes) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    public int returnSize(){
        return enquestes.size();
    }
    /// S'HA DE PENSAR EN AQUESTES DUES SI ES NECESARI QUE EL FEM
    /// Exportar enquesta?
    /// importar enquesta?
    ///
    /// Crear enquesta? -----
    /// Modificar enquesta? -----
    /// Guardar enquesta? -----
    /// Administrar enquestes? Es un conjunt per tant es tot el que fa el gestor
    ///
    /// Importar respostes???
    /// Gestionar respostes???
    ///
    /*
    //Cas d'us modificar enquesta (necesari)
    public void modificarEnquesta() {
        // Implementació de la gestió d'enquestes
        Haura de cridar a guardar enquesta
    }


    public void guardarEnquesta(Enquesta enquesta) {
        for (int i = 0; i < enquestes.size(); i++) {
            if (enquestes.get(i).getId().equals(enquesta.getId())) {
                enquestes.set(i, enquesta);
                System.out.println("Encuesta modificada: " + enquesta.getTitol());
                return;
            }
        }
    enq.add(enquesta);
    }


    public void respondreEnquesta(int idEnquesta, Usuari usuari) {
        // Implementació de la gestió d'enquestes
    }
     */

    /*
    Es necessari que el gestor d'enquestes gestioni les respostes?
    O necessitem que un gestor de respostes o preguntes ho faci?
    public void respondreResposta() {
    }

    //Cas d'us consultar respostes (necesari) en cas que necessitem l'estadistica sera un altre cas
    public void consultarRespostes() {
    }
     */



    //Cas d'us esborrar enquesta (necesari)
    //Com ho farem? es possible que l'usuari coneix l'id de l'enquesta?
    public void esborrarEnquesta(int id) {
        Enquesta enq = getEnquestaPerID(id);
        if (enq != null) {
            enquestes.remove(enq);
            System.out.println("Enquesta amb ID " + id + " eliminada correctament.");
        } else {
            System.out.println("No s'ha trobat cap enquesta amb ID " + id + ".");
        }
    }


    //Cas d'us consultar enquestes
    public void llistarEnquestes() {
        for (Enquesta enq : enquestes) {
            System.out.println(
                    "Enquesta ID: " + enq.getId() +
                    ", Títol: " + enq.getTitol() +
                    ", Descripció: " + enq.getDescripcio() +
                    ", Creador: " + enq.getCreador().getUsuari()
            );
        }
    }
}