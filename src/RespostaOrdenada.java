import java.util.List;

public class RespostaOrdenada extends Resposta {
    private List<String> ordre;

    public RespostaOrdenada(String idPregunta, List<String> ordre) {
        super(idPregunta);
        this.ordre = ordre;
    }

    public List<String> getOrdre() {
        return ordre;
    }

    public void setOrdre(List<String> ordre) {
        this.ordre = ordre;
        setContestat(ordre != null && !ordre.isEmpty());
    }

    @Override
    public String getValorString() {
        if (ordre == null || ordre.isEmpty()) return "No contestada";
        else return String.join(" > ", ordre); // Example format: "Option1 > Option2 > Option3"
    }
}
