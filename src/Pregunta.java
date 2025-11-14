import java.util.*;

public class Pregunta {
    //Necessitem un tipus per la pregunta per aixi definir la resposta
    //En aquest cas es fara l'us de enums pero es pot fer refactoring si cal

    //public enum Tipus {NUMERICA, LLIURE, UNICA, MULTIPLE, ORDENADA}

    private String text;
    private Integer tipus;
    private Map<Integer, Resposta> respostes; 
    private int minKeyRespostes; // Per a respostes d'usuaris no registrats
    private final Integer numOpcions; // Nombre d'opcions per a preguntes UNICA, MULTIPLE, ORDENADA
    private final List<String> opcions;
    private double minValue; // Per a preguntes NUMERICA
    private double maxValue; // Per a preguntes NUMERICA

    //Format Pregunta_qualsevol? -- Defineix tipus de resposta

    /**
     * Constructor de la classe Pregunta
     * @param text de la pregunta
     * @param tipus de la pregunta
     * @param opcions per a preguntes UNICA, MULTIPLE, ORDENADA
     * @throws IllegalArgumentException si el tipus de pregunta és desconegut
     */
    public Pregunta(String text, int tipus, List<String> opcions) {
        this.text = text;
        this.tipus = tipus;
        this.respostes = new java.util.HashMap<>();
        this.minKeyRespostes = -1;
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

    /**
     * Getter de la pregunta
     * @return la pròpia pregunta
     */
    public Pregunta getPregunta() {
        return this;
    }

    /**
     * Getter del text de la pregunta
     * @return el text de la pregunta
     */
    public String getText() {
        return text;
    }

    /**
     * Getter del tipus de la pregunta
     * @return el tipus de la pregunta
     */
    public int getTipus() {
        return tipus;
    }

    /**
     * Getter de les opcions de la pregunta
     * @return les opcions de la pregunta
     */
    public List<String> getOpcions() {
        return opcions;
    }

    /**
     * Getter del map de respostes de la pregunta
     * @return el map de respostes de la pregunta
     */
    public Map<Integer, Resposta> getRespostes() {
        return Collections.unmodifiableMap(respostes);
    }

    /**
     * Getter del valor mínim per a preguntes numèriques
     * @return el valor mínim
     * @throws UnsupportedOperationException si la pregunta no és numèrica
     */
    public double getMinValue() {
        if (tipus == 0) { // NUMERICA
            return minValue;
        } else {
            throw new UnsupportedOperationException("No es pot obtenir el valor mínim per a aquest tipus de pregunta.");
        }
    }

    /**
     * Getter del valor màxim per a preguntes numèriques
     * @return el valor màxim
     * @throws UnsupportedOperationException si la pregunta no és numèrica
     */
    public double getMaxValue() {
        if (tipus == 0) { // NUMERICA
            return maxValue;
        } else {
            throw new UnsupportedOperationException("No es pot obtenir el valor màxim per a aquest tipus de pregunta.");
        }
    }

    /**
     * Getter del nombre d'opcions per a preguntes UNICA, MULTIPLE, ORDENADA
     * @return el nombre d'opcions
     * @throws UnsupportedOperationException si la pregunta no és d'aquests tipus
     */
    public int getNumOpcions() {
        if (tipus == 1 || tipus == 2 || tipus == 3) { //UNICA, MULTIPLE, ORDENADA
            return numOpcions;
        } else {
            throw new UnsupportedOperationException("No es pot obtenir el nombre d'opcions per a aquest tipus de pregunta.");
        }
    }

    /**
     * Getter del nombre de respostes a la pregunta
     * @return el nombre de respostes
     */
    public int getNumRespostes() {
        return respostes.size();
    }

    // setters
    /**
     * Setter del text de la pregunta
     * @param newText nou text de la pregunta
     */
    public void setText(String newText) {
        text = newText;
    }

    /**
     * Afegeix una resposta a la pregunta
     * @param resposta a afegir
     * @param idUsuari identificador de l'usuari que fa la resposta (-1 si no està registrat)
     * @return 1 si s'ha afegit correctament, 0 si ja existeix una resposta per aquest usuari
     */
    public Integer addResposta(Resposta resposta, int idUsuari) {
        if (idUsuari == -1) { // Usuari no registrat
            respostes.put(minKeyRespostes, resposta);
            minKeyRespostes--;
            return 1;
        }
        if (respostes.containsKey(idUsuari)) {
            return 0; // Ja existeix una resposta per aquest usuari
        }
        respostes.put(idUsuari, resposta);
        return 1;
    }

    /**
     * Calcula la moda de les respostes per a preguntes UNICA, MULTIPLE, ORDENADA
     * @return la resposta moda
     * @throws UnsupportedOperationException si la pregunta no és d'aquests tipus
     */
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

    /**
     * Elimina totes les respostes de la pregunta
     */
    public void eliminarTotesRespostes() {
        respostes.clear();
        minKeyRespostes = -1;
    }

    @Override
    public String toString() {
        return "Pregunta Text: " + text + ", Tipus: " + opcions.get(tipus);
    }
}