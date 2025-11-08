// import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

/*
 * Classe Enquesta
 * Es una Lista de preguntes i una Matriu de respostes 
 * Las respuestas de los usuarios no registrados es -1 y 
 * las filas que usa en la matriz de respuestas
 * se guardan en una lista aparte (noRegistratAnswers)
 * Las filas de usuarios registrados se guardan en un mapa
 * userToAnswerId
 */



public class Enquesta {
    // Representa una enquesta concreta: id, titol, descripcio, creador, preguntes, respostes, participants
    private Integer id;
    private String titol;
    private String descripcio;
    private Integer idCreador;


    public List<List<Resposta>> respostes; // Matriu de respostes per a cada pregunta
    public List<Pregunta> preguntes;

    //Par usuari <> Fila Matriu que li correspon
    private Map<Integer, Integer> userToAnswerId; // Map d'usuari a fila de respostes

    private List<Integer> noRegistratAnswers; // Llista de files de respostes per usuaris no registrats

    // posem les dates com atributs, creem una classe Data i es relaciona?
    // private LocalDateTime dataCreacio;
    // private LocalDateTime dataFinalitzacio;

    // Constructora
    public Enquesta(Integer id, String titol, String descripcio, Integer idCreador) {
        this.id = id;
        this.titol = titol;
        this.descripcio = descripcio;
        this.idCreador = idCreador;

        this.respostes = new ArrayList<>();
        this.userToAnswerId = new HashMap<>();
        this.noRegistratAnswers = new ArrayList<>();
        // this.dataCreacio = LocalDateTime.now();
        //this.visible = false;
    }

    public boolean participa(int id) {
        if (id < 0) throw new IllegalArgumentException("L'id de l'usuari no pot ser negatiu.");

        for (Integer userId : userToAnswerId.keySet()) {
            if (userId.equals(id)) {
                return true;
            }
        }
        return false;
    }



    public List<Pregunta> getPreguntes(){
        return Collections.unmodifiableList(preguntes);
    }

    public void setResposta(int idUsuari, List<Resposta> respostesUsuari){
        Integer filaMatriu = userToAnswerId.size();
        respostes.add(respostesUsuari);
        if (idUsuari >= 0) userToAnswerId.put(idUsuari, filaMatriu);
        else if (idUsuari == -1) noRegistratAnswers.add(filaMatriu);
        else throw new IllegalArgumentException("L'id de l'usuari no pot ser menor que -1.");
    }

    // Getters
    public Integer getId() { return id;}
    public String getTitol() { return titol;}
    public String getDescripcio() { return descripcio;}
    public Integer getCreador() { return idCreador;}

    // Setters
    public void setTitol(String titol) { this.titol = titol; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }
    public void setCreador(Integer idCreador) { this.idCreador = idCreador; }
    public void setId(int id) { this.id = id; }
    public void setPreguntes(List<Pregunta> preguntes) { this.preguntes = preguntes; }

    // public LocalDateTime getDataCreacio() { return dataCreacio; }
    // public LocalDateTime getDataFinalitzacio() { return dataFinalitzacio; }

    public List<Integer> getParticipants() {
        return new ArrayList<>(userToAnswerId.keySet());
    }



}