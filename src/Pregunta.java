import java.util.*;

public class Pregunta {
    //Necessitem un tipus per la pregunta per aixi definir la resposta
    //En aquest cas es fara l'us de enums pero es pot fer refactoring si cal

    //public enum Tipus {NUMERICA, LLIURE, UNICA, MULTIPLE, ORDENADA}

    private String text;
    private Integer tipus;
    private Map<Integer, Resposta> respostes; 
    private final Integer numOpcions; // Nombre d'opcions per a preguntes UNICA, MULTIPLE, ORDENADA
    private final List<String> opcions;
    private double minValue; // Per a preguntes NUMERICA
    private double maxValue; // Per a preguntes NUMERICA

    //Format Pregunta_qualsevol? -- Defineix tipus de resposta
    public Pregunta(String text, int tipus, List<String> opcions) {
        this.text = text;
        this.tipus = tipus;
        this.respostes = new java.util.HashMap<>();
        if (tipus == 0 || tipus == 4) {
            this.numOpcions = 0; // No s'aplica per a NUMERICA i LLIURE
            this.opcions = null;
        } else if (tipus == 1 || tipus == 2 || tipus == 3) { //UNICA, MULTIPLE, ORDENADA
            this.numOpcions = opcions.size();
            this.opcions = new ArrayList<>(opcions);
        } else {
            throw new IllegalArgumentException("Tipus de pregunta desconegut: " + tipus); 
        }

    }

    // getters
    public Pregunta getPregunta() {
        return this;
    }

    public String getText() {
        return text;
    }

    public int getTipus() {
        return tipus;
    }

    public List<String> getOpcions() {
        return opcions;
    }

    public Map<Integer, Resposta> getRespostes() {
        return Collections.unmodifiableMap(respostes);
    }
    public double getMinValue() {
        if (tipus == 0) { // NUMERICA
            return minValue;
        } else {
            throw new UnsupportedOperationException("No es pot obtenir el valor mínim per a aquest tipus de pregunta.");
        }
    }
    public double getMaxValue() {
        if (tipus == 0) { // NUMERICA
            return maxValue;
        } else {
            throw new UnsupportedOperationException("No es pot obtenir el valor màxim per a aquest tipus de pregunta.");
        }
    }

    public int getNumOpcions() {
        if (tipus == 1 || tipus == 2 || tipus == 3) { //UNICA, MULTIPLE, ORDENADA
            return numOpcions;
        } else {
            throw new UnsupportedOperationException("No es pot obtenir el nombre d'opcions per a aquest tipus de pregunta.");
        }
    }

    public int getNumRespostes() {
        return respostes.size();
    }

    // setters
    public void setText(String newText) {
        text = newText;
    }

    public Integer addResposta(Resposta resposta, int idUsuari) {
        if (respostes.containsKey(idUsuari)) {
            return 0; // Ja existeix una resposta per aquest usuari
        }
        respostes.put(idUsuari, resposta);
        return 1;
    }

    public Resposta getRespostaModa(){
        if (tipus == 1 ||tipus == 2 || tipus==3){
            Map<Resposta, Integer> freqMap = new HashMap<>();
            for (Resposta r : respostes.values()) {
                freqMap.put(r, freqMap.getOrDefault(r, 0) + 1);
            }
            Resposta moda = null;
            int maxFreq = 0;
            for (Map.Entry<Resposta, Integer> entry : freqMap.entrySet()) {
                if (entry.getValue() > maxFreq) {
                    maxFreq = entry.getValue();
                    moda = entry.getKey();
                }
            }
            return moda;
        } else {
            throw new UnsupportedOperationException("No es pot calcular la moda per a aquest tipus de pregunta.");
        }
    }

    @Override
    public String toString() {
        return "Pregunta Text: " + text + ", Tipus: " + opcions.get(tipus);
    }
}