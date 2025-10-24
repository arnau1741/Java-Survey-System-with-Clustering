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
}