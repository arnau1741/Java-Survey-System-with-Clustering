
public class Pregunta {
    //Necessitem un tipus per la pregunta per aixi definir la resposta
    //En aquest cas es fara l'us de enums pero es pot fer refactoring si cal

    public enum Tipus {NUMERICA, LLIURE, UNICA, MULTIPLE, ORDENADA}

    private Integer id;
    private String text;
    private Tipus tipus;
    //Falta hacer esto
    private Resposta resposta;

    //Format Pregunta_qualsevol? -- Defineix tipus de resposta
    public Pregunta(Integer id, String text, Tipus tipus) {
        this.id = id;
        this.text = text;
        this.tipus = tipus;
        resposta = null;
    }

    //Si eliminem el gestor llavors aqui assignem la resposta
    public void assignarResposta(Resposta resposta) {
        this.resposta = resposta;
    }


    // getters
    public Integer getId() {
        return id;
    }

    public Pregunta getPregunta() {
        return this;
    }

    public Resposta getResposta() {
        return resposta;
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

    public void setResposta(Resposta res) {
        this.resposta = res;
    }

    public void  setText(String newText) {
        text = newText;
    }

    @Override
    public String toString() {
        String respostaText = (resposta != null)
                ? resposta.getValorString()
                : "No contestada";
        return "P" + id + '\'' + text + '\'' + respostaText;
    }
}
