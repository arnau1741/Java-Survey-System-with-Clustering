package prop.enquestes.domini;
import java.util.List;
public class RespostaOrdenada extends Resposta {
    private Integer resposta;

    /**
     * Constructor de la classe RespostaOrdenada
     */
    public RespostaOrdenada() {
        super();
        resposta = null;
    }

    /**
     * Estableix la resposta seleccionada
     * @param resposta opció seleccionada
     * @return 1 si l'operació és correcta, 0 si l'opció és invàlida
     */
    public int setResposta(Integer resposta) {
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
            return "NO CONTESTAT";
        }
        return opcions.get(resposta);
    } 
}
