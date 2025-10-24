// import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Enquesta {
    private Integer id;
    private String titol;
    private String descripcio;
    private Usuari creador;

    private Integer seguentPreguntaID = 0;

    private List<Pregunta> preguntes;

    // posem les dates com atributs, creem una classe Data i es relaciona?
    // private LocalDateTime dataCreacio;
    // private LocalDateTime dataFinalitzacio;

    // Constructora
    public Enquesta(Integer id, String titol, String descripcio, Usuari creador) {
        this.id = id;
        this.titol = titol;
        this.descripcio = descripcio;
        this.creador = creador;
        this.preguntes = new ArrayList<>();
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

    public List<Pregunta> getPreguntes() {
        return Collections.unmodifiableList(preguntes);
    }
}