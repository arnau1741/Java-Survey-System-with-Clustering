import java.util.*;

public class Pregunta {
    //Necessitem un tipus per la pregunta per aixi definir la resposta
    //En aquest cas es fara l'us de enums pero es pot fer refactoring si cal

    //public enum Tipus {NUMERICA, LLIURE, UNICA, MULTIPLE, ORDENADA}

    private final List<String> opcions = Arrays.asList("NUMERICA", "LLIURE", "UNICA", "MULTIPLE", "ORDENADA");
    private String text;
    private Integer tipus;
    private Map<Integer, Resposta> respostes; 
    private Integer numOpcions; // Nombre d'opcions per a preguntes UNICA, MULTIPLE, ORDENADA

    //Format Pregunta_qualsevol? -- Defineix tipus de resposta
    public Pregunta(String text, int tipus) {
        this.text = text;
        this.tipus = tipus;
        if (tipus < 0 || tipus >= opcions.size()) {
            throw new IllegalArgumentException("Tipus de pregunta invàlid: " + tipus);
        }
        this.respostes = new java.util.HashMap<>();
        if (tipus == 0 || tipus == 1) this.numOpcions = 0; // No s'aplica per a NUMERICA i LLIURE
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

    public int getNumOpcions() {
        if (tipus == 2 || tipus == 3 || tipus == 4) {
            return numOpcions;
        } else {
            throw new UnsupportedOperationException("No es pot obtenir el nombre d'opcions per a aquest tipus de pregunta.");
        }
    }

    // setters

    public void setText(String newText) {
        text = newText;
    }

    public void setNumOpcions(int numOpcions) {
        if (tipus == 2 || tipus == 3 || tipus == 4) {
            this.numOpcions = numOpcions;
        } else {
            throw new UnsupportedOperationException("No es pot establir el nombre d'opcions per a aquest tipus de pregunta.");
        }
    }

    public Integer addResposta(Resposta resposta, int idUsuari) {
        if (respostes.containsKey(idUsuari)) {
            return 0; // Ja existeix una resposta per aquest usuari
        }
        respostes.put(idUsuari, resposta);
        return 1;
    }

    @Override
    public String toString() {
        return "Pregunta Text: " + text + ", Tipus: " + opcions.get(tipus);
    }
}
