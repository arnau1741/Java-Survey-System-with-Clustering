
public class Pregunta {
    //Necessitem un tipus per la pregunta per aixi definir la resposta
    //En aquest cas es fara l'us de enums pero es pot fer refactoring si cal

    public enum Tipus {NUMERICA, LLIURE, UNICA, MULTIPLE, ORDENADA}

    private Integer id;
    private String text;
    private Tipus tipus;

    private Resposta resposta;

    //Format Pregunta_qualsevol? -- Defineix tipus de resposta
    public Pregunta(Integer id, String text, Tipus tipus) {
        this.id = id;
        this.text = text;
        this.tipus = tipus;
        resposta = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer newId) {
        id = newId;
    }

    public void setResposta(Resposta res) {
        this.resposta = res;
    }

    public String getText() {
        return text;
    }

    public Tipus getTipus() {
        return tipus;
    }

    @Override
    public String toString() {
        return "Pregunta{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", tipus=" + tipus +
                '}';
    }
}
