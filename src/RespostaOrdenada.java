import java.util.List;
public class RespostaOrdenada extends Resposta {
    private Integer numOpcions;
    private Integer resposta;

    /**
     * Constructor de la classe RespostaOrdenada
     * @param numOpcions nombre d'opcions disponibles
     */
    public RespostaOrdenada(int numOpcions) {
        super();
        this.numOpcions = numOpcions;
    }

    /**
     * Estableix la resposta seleccionada
     * @param resposta opció seleccionada
     * @return 1 si l'operació és correcta, 0 si l'opció és invàlida
     */
    public int setResposta(Integer resposta) {
        if (resposta < 0 || resposta >= numOpcions) {
            return 0; // Opció invàlida
        }
        this.resposta = resposta;
        setContestat(true);
        return 1;
    }

    /**
     * Getter de la resposta
     * @return resposta seleccionada
     */
    public Integer getResposta() {
        return resposta;
    }

    /**
     * Getter de l'ordre de la resposta
     * @return ordre de la resposta
     */
    public Integer getOrdre() {
        return resposta;
    }

    /**
     * Getter del text de la resposta ordenada
     * @param opcions Llista d'opcions de la pregunta
     * @return Text de la resposta ordenada
     */
    @Override
    public String getText(List<String> opcions) {
        if (resposta == null) {
            return "No contestat";
        }
        return opcions.get(resposta);
    } 
}
