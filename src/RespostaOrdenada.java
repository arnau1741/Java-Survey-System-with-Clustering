import java.util.List;
public class RespostaOrdenada extends Resposta {
    private Integer numOpcions;
    private Integer resposta;

    public RespostaOrdenada(int numOpcions) {
        super();
        this.numOpcions = numOpcions;
    }

    public int setResposta(Integer resposta) {
        if (resposta < 0 || resposta >= numOpcions) {
            return 0; // Opció invàlida
        }
        this.resposta = resposta;
        setContestat(true);
        return 1;
    }

    public Integer getResposta() {
        return resposta;
    }

    public Integer getOrdre() {
        return resposta;
    }

    @Override
    public String getText(List<String> opcions) {
        if (resposta == null) {
            return "No contestat";
        }
        return opcions.get(resposta);
    } 
}
