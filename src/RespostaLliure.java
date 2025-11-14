import java.util.List;

public class RespostaLliure extends Resposta{
    private String text;

    /**
     * Constructor de la classe RespostaLliure
     * @param text de la resposta lliure
     */
    public RespostaLliure(String text) {
        super();
        this.text = text;
        setContestat(text != null && !text.isBlank());
    }

    /**
     * Getter del text de la resposta lliure
      * @param text de la resposta lliure
     */
    public void setText(String text) {
        this.text = text;
        setContestat(text != null && !text.isBlank());
    }

    /**
     * Getter del text de la resposta lliure
     * @return text de la resposta lliure
     */
    @Override
    public String getText(List<String> opcions) {
        if(text == null || text.isBlank()) return "No contestada";
        else return text;
    }
}
