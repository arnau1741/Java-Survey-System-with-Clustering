// import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Enquesta {
    // Representa una enquesta concreta: id, titol, descripcio, creador, preguntes, respostes, participants
    private Integer id;
    private String titol;
    private String descripcio;
    private Usuari creador;

    private Integer seguentPreguntaID = 0;

    private List<List<Pregunta>> respostes; // Matriu de respostes per a cada pregunta

    //si i = 0 de respostes es del usuari id = 130, j = 0 participants = 130
    private List<Usuari> participants;

    // posem les dates com atributs, creem una classe Data i es relaciona?
    // private LocalDateTime dataCreacio;
    // private LocalDateTime dataFinalitzacio;

    // Constructora
    public Enquesta(Integer id, String titol, String descripcio, Usuari creador) {
        this.id = id;
        this.titol = titol;
        this.descripcio = descripcio;
        this.creador = creador;

        this.respostes = new ArrayList<>();
        this.participants = new ArrayList<>();
        // this.dataCreacio = LocalDateTime.now();
    }

    // Getters
    public Integer getId() { return id;}
    public String getTitol() { return titol;}
    public String getDescripcio() { return descripcio;}
    public Usuari getCreador() { return creador;}

    // Setters
    public void setTitol(String titol) { this.titol = titol; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }
    public void setCreador(Usuari creador) { this.creador = creador; }
    public void setId(int id) { this.id = id; }

    // public LocalDateTime getDataCreacio() { return dataCreacio; }
    // public LocalDateTime getDataFinalitzacio() { return dataFinalitzacio; }


    public List<Usuari> getParticipants() {
        return Collections.unmodifiableList(participants);
    }
}