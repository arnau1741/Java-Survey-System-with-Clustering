import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SplittableRandom;
import java.util.*;

public class GestorEnquesta {
    // gestiona totes les enquestes creades
    private List<Enquesta> enquestes;
    private int ultimIdEnquesta = 0;

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

    //Consultar enquesta per ID
    public Enquesta getEnquestaPerID(int id) {
        for (Enquesta e : enquestes) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    //Devolver el siguiente ID disponible para una nueva enquesta y actualizar el contador
    public int returnIdEnquesta(){
        int tmp = ultimIdEnquesta;
        ultimIdEnquesta++;
        return tmp;
    }

    //Caso de uso importar enquesta
    public void importarEnquesta(String titol, String descripcio, int idCreador, List<Pregunta> preguntes) {
        int id = returnIdEnquesta();
        Enquesta novaEnquesta = new Enquesta(id, titol, descripcio, idCreador, preguntes);
        afegirEnquesta(novaEnquesta);
    }


    //Caso de uso exportar enquesta
    public List<String> exportarEnquesta(int id) {
        Enquesta e = getEnquestaPerID(id);
        if (e == null) {
            System.out.println("No s'ha trobat cap enquesta amb ID " + id + ".");
            return null;
        }
        List<String> out = new ArrayList<>();
        List<Pregunta> pre = e.getPreguntes();

        out.add(e.getTitol() + ";" + e.getDescripcio() + ";" + pre.size());
        for (Pregunta p : pre) {
            out.add(p.getText() + ";" + p.getTipus());
        }
        return out;
    }

    public void importarRespostas(int idEnquesta, int idUsuari, List<String> preguntesTxt, List<String> respostesTxt) {
        Enquesta enq = getEnquestaPerID(idEnquesta);
        if (enq == null) {
            System.out.println("No s'ha trobat cap enquesta amb ID " + idEnquesta + ".");
            return;
        }

        List<Resposta> respostesUsuari = new ArrayList<>();
        for (int i = 0; i < preguntesTxt.size(); i++) {
            String tipus = preguntesTxt.get(i).split(";")[1];
            String valor = respostesTxt.get(i);

            Resposta resposta;
            switch (tipus) {
                case "LLIURE":
                    resposta = new RespostaLliure(valor);
                    break;
                case "ORDENADA":
                    List<String> opcions = List.of(valor.split(","));
                    resposta = new RespostaOrdenada(opcions);
                    break;
                case "MULTIPLE":
                    List<String> opcionsMultiple = List.of(valor.split(","));
                    resposta = new RespostaMultiple(opcionsMultiple);
                    break;
                case "NUMERICA":
                    resposta = new RespostaNumerica(Double.parseDouble(valor));
                    break;
                case "UNICA":
                    List<String> unica = List.of(valor.split(","));
                    resposta = new RespostaUnica(unica);
                    break;
                default:
                    System.out.println("Tipus de pregunta desconegut: " + tipus);
                    return;
            }
            respostesUsuari.add(resposta);
        }
        enq.setResposta(idUsuari, respostesUsuari);
    }

    public List<String> exportarRespostes(int idEnquesta, int idUsuari) {
        Enquesta e = getEnquestaPerID(idEnquesta);
        if (e == null) {
            System.out.println("No s'ha trobat cap enquesta amb ID " + idEnquesta + ".");
            return null;
        }

        List<String> out = new ArrayList<>();
        out.add("Nombre de preguntes: " + e.getPreguntes().size());
        Integer filaUsuari = e.obtenFilaIdUsuari(idUsuari);
        if (filaUsuari == null) {
            out.add("L'usuari amb ID " + idUsuari + " no ha respost aquesta enquesta.");
            return out;
        }

        List<Resposta> filaRespostes = e.respostes.get(filaUsuari);
        List<Pregunta> preguntes = e.getPreguntes();

        out.add("Respostes de l'usuari " + idUsuari + ":");
        out.add("------------------------------------");

        // Combinar pregunta i resposta
        for (int i = 0; i < preguntes.size(); i++) {
            Pregunta p = preguntes.get(i);
            Resposta r = filaRespostes.get(i);
            String valor = (r == null) ? "No contestada" : r.getValorString();
            out.add("P" + p.getId() + ": " + p.getText() + " → " + valor);
        }

        out.add("------------------------------------");
        return out;
    }

    //Cas d'us consultar respostes (necesari) en cas que necessitem l'estadistica sera un altre cas
    public List<Resposta> consultarRespostes(int idEnquesta, int idUsuari) {
        Enquesta enq = getEnquestaPerID(idEnquesta);
        if (enq == null) {
            System.out.println("No s'ha trobat cap enquesta amb ID " + idEnquesta + ".");
            return Collections.emptyList();
        }
        Integer filamatriu = enq.obtenFilaIdUsuari(idUsuari);
        return enq.getRespostesUsuari(filamatriu);
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
                    ", Creador: " + enq.getCreador()
            );
        }
    }
}