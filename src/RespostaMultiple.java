
import java.util.List;
import java.util.ArrayList;

public class RespostaMultiple extends Resposta{
    private final List<Pair<String, Boolean>> opcions;
    private int seleccio = 0;

    public RespostaMultiple(Pregunta idPregunta, List<String> totesOpcions) {
        super(idPregunta);
        this.opcions = new ArrayList<>();
        for (String palabra : totesOpcions)
            opcions.add(new Pair<>(palabra, false));
    }

    //Marca una opció com a seleccionada
    public void seleccionar(String opcio) {
        for (Pair<String, Boolean> pair : opcions) {
            if (pair.getFirst().equalsIgnoreCase(opcio)) {
                pair.setSecond(true);
                if (seleccio == 0) setContestat(true);
                ++seleccio;
            }
        }
    }

    //Desmarca una opció
    public void desseleccionar(String opcio) {
        for (Pair<String, Boolean> pair : opcions) {
            if (pair.getFirst().equalsIgnoreCase(opcio)) {
                pair.setSecond(false);
                --seleccio;
                if(seleccio == 0) setContestat(false);
            }
        }
    }

    // Retorna la llista de totes les opcions amb el seu estat
    public List<Pair<String, Boolean>> getOpcions() {
        return opcions;
    }

    // Retorna només les opcions seleccionades com a String
    @Override
    public String getValorString() {
        List<String> seleccionades = new ArrayList<>();
        for (Pair<String, Boolean> pair : opcions) {
            if (pair.getSecond()) seleccionades.add(pair.getFirst());
        }
        if(seleccionades.isEmpty()) return "No contestada";
        else return String.join(", ", seleccionades);
    }
}
