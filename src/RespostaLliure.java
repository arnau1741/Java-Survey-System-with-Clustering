
public class RespostaLliure extends Resposta{
    private String text;

    public RespostaLliure(Integer idPregunta, String text) {
        super(idPregunta);
        this.text = text;
        setContestat(text != null && !text.isBlank());
    }

    public void setText(String text) {
        this.text = text;
        setContestat(text != null && !text.isBlank());
    }

    @Override
    public String getValorString() {
        if(text == null || text.isBlank()) return "No contestada";
        else return text;
    }
}
