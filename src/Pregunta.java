
public class Pregunta {
    //Necessitem un tipus per la pregunta per aixi definir la resposta
    //En aquest cas es fara l'us de enums pero es pot fer refactoring si cal

    public enum Tipus {NUMERICA, LLIURE, UNICA, MULTIPLE, ORDENADA}

    private Integer id;
    private String text;
    private Tipus tipus;

    //Format Pregunta_qualsevol? -- Defineix tipus de resposta
    public Pregunta(Integer id, String text, Tipus tipus) {
        this.id = id;
        this.text = text;
        this.tipus = tipus;
    }

    // getters
    public Integer getId() {
        return id;
    }

    public Pregunta getPregunta() {
        return this;
    }

    public String getText() {
        return text;
    }


    public Tipus getTipus() {
        return tipus;
    }

    // setters

    public void setId(Integer newId) {
        id = newId;
    }

    public void  setText(String newText) {
        text = newText;
    }

    @Override
    public String toString() {
        return "Pregunta ID: " + id + ", Text: " + text + ", Tipus: " + tipus;
    }
}
