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

    private List<List<Resposta>> respostes; // Matriu de respostes per a cada pregunta
    private List<Pregunta> preguntes;
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
        this.preguntes = new ArrayList<>();
        this.participants = new ArrayList<>();
        // this.dataCreacio = LocalDateTime.now();
    }

    // Getters
    public Integer getId() { return id;}
    public String getTitol() { return titol;}
    public String getDescripcio() { return descripcio;}
    public Usuari getCreador() { return creador;}
    // public LocalDateTime getDataCreacio() { return dataCreacio; }
    // public LocalDateTime getDataFinalitzacio() { return dataFinalitzacio; }

    public void afegirPregunta(Pregunta p) {
        p.setId(seguentPreguntaID++);
        preguntes.add(p);

        for (List<Resposta> fila : respostes) {
            fila.add(null); // Afegeix una columna per a la nova pregunta
        }
    }

    public void eliminarPregunta(int preguntaID) {
        if (preguntaID >= 0 && preguntaID < preguntes.size()) {
            preguntes.removeIf(p -> p.getId() == preguntaID);
        }
    }

    public void modificarPregunta(Integer preguntaID, Pregunta novaPregunta) {
        for (int i = 0; i < preguntes.size(); i++) {
            Pregunta p = preguntes.get(i);
            if (p.getId().equals(preguntaID)) {
                novaPregunta.setId(preguntaID);
                preguntes.set(i, novaPregunta);
                break;
            }
        }
    }

    public void afegirParticipant(Usuari u) {
        // Comprova si l'usuari ja és participant
        if (!participants.contains(u)) {
            participants.add(u);
            // Afegeix una nova fila de respostes per al nou participant
            List<Resposta> novaFila = new ArrayList<>();
            for (int i = 0; i < preguntes.size(); i++) {
                novaFila.add(null); // Inicialitza les respostes com a null
            }
            respostes.add(novaFila);
        }
    }

    public void afegirResposta(Usuari u, Pregunta p, Resposta r) {
        int participantIndex = participants.indexOf(u);
        int preguntaIndex = preguntes.indexOf(p);
        if (participantIndex != -1 && preguntaIndex != -1) {
            respostes.get(participantIndex).set(preguntaIndex, r);
        }
    }

    public List<Pregunta> getPreguntes() {
        // tornar una llista immutable per evitar modificacions externes
        return Collections.unmodifiableList(preguntes);
    }

    public Resposta getResposta(Usuari u, Pregunta p) {
        int participantIndex = participants.indexOf(u);
        int preguntaIndex = preguntes.indexOf(p);

        if (participantIndex != -1 && preguntaIndex != -1) {
            return respostes.get(participantIndex).get(preguntaIndex);
        }
        return null;
    }

    public List<Usuari> getParticipants() {
        return Collections.unmodifiableList(participants);
    }
}