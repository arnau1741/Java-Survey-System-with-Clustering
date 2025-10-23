// import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Enquesta {
    private final int id; // és final perquè no canvia mai
    private String titol;
    private String descripcio;
    private Usuari creador;

    private int seguentPreguntaID = 0;

    private List<Pregunta> preguntes;

    // posem les dates com atributs, creem una classe Data i es relaciona?
    // private LocalDateTime dataCreacio;
    // private LocalDateTime dataFinalitzacio;

    // Constructora
    public Enquesta(int id, String titol, String descripcio, Usuari creador) {
        this.id = id;
        this.titol = titol;
        this.descripcio = descripcio;
        this.creador = creador;
        this.preguntes = new ArrayList<>();
        // this.dataCreacio = LocalDateTime.now();
    }

    // Getters
    public int getId() { return id;}
    public String getTitol() { return titol;}
    public String getDescripcio() { return descripcio;}
    public Usuari getCreador() { return creador;}
    // public LocalDateTime getDataCreacio() { return dataCreacio; }
    // public LocalDateTime getDataFinalitzacio() { return dataFinalitzacio; }

    public void afegirPregunta(Pregunta p) {
        p.setID(seguentPreguntaID++);
        preguntes.add(p);
    }

    public void eliminarPregunta(int preguntaID) {
        if (preguntaID >= 0 && preguntaID < preguntes.size()) {
            preguntes.removeIf(p -> p.getID() == preguntaID);
        }
    }

    public void modificarPregunta(Integer preguntaID, Pregunta novaPregunta) {
        for (int i = 0; i < preguntes.size(); i++) {
            Pregunta p = preguntes.get(i);
            if (p.getID() == preguntaID) {
                novaPregunta.setID(preguntaID);
                preguntes.set(i, novaPregunta);
                break;
            }
        }
    }

    public List<Pregunta> getPreguntes() {
        return Collections.unmodifiableList(preguntes);
    }
}