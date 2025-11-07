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

    public List<List<Pregunta>> respostes; // Matriu de respostes per a cada pregunta

    //Par usuari <> Fila Matriu que li correspon
    public List<Pair<Usuari, Integer>> participants;

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

    public boolean participa(Usuari usuari) {
        for(int i = 0; i < participants.size(); i++){
            if(participants.get(i).getFirst().getId() == usuari.getId()){
                return true;
            }
        }
        return false;
    }

    public void mostrarPreguntes(){
        //metode per mostrar les preguntes de l'enquesta
        for(int j = 0; j < respostes.getFirst().size(); j++){
            System.out.println("Pregunta " + respostes.getFirst().get(j).getText());
        }
    }

    public void afegirParticipant(Usuari usuari, Integer filaMatriu){
        participants.add(new Pair<>(usuari, filaMatriu));
    }

    public int afegirFilaRespostes() {
        respostes.add(new ArrayList<>());
        return respostes.size() - 1;
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

    public List<Pair<Usuari, Integer>> getParticipants() {
        return Collections.unmodifiableList(participants);
    }

    public Pregunta[] getPreguntes(int x) {
        return respostes.get(x).toArray(new Pregunta[0]);
    }

    public void setPreguntaResposta(int x, int i, Pregunta pregunta) {
        respostes.get(x).set(i, pregunta);
    }
}