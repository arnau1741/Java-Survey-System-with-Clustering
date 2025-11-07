
import java.util.List;

public class RespostaUnica extends Resposta {
    private final List<String> opcions;
    private String seleccionada;

    public RespostaUnica(Pregunta idPregunta, List<String> opcions) {
        super(idPregunta);
        this.opcions = opcions;
        this.seleccionada = null;
    }

    //Selecciona una de les opcions possibles
    public void seleccionar(String opcio) {
        if (opcions.contains(opcio)) {
            this.seleccionada = opcio;
            setContestat(true);
        } else {
            System.err.println("Opció '" + opcio + "' no existeix per aquesta pregunta.");
        }
    }

    // Retorna totes les opcions
    public List<String> getOpcions() {
        return opcions;
    }

    //Retorna l'opció seleccionada
    public String getSeleccionada() {
        return seleccionada;
    }

    @Override
    public String getValorString() {
        if(seleccionada == null) return "No contestada";
        else return seleccionada;

    }
}
