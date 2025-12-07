package prop.enquestes.domini;
import java.util.List;

public class RespostaUnica extends Resposta {
    private Integer resposta; //ha de ser entre 0 i numOpcions -1

    /**
     * Constructor de la classe RespostaUnica
     */
    public RespostaUnica() {
        super();
        this.resposta = null; // Indica que no s'ha contestat encara
    }

    /**
     * Estableix la resposta seleccionada
     * @param resposta opció seleccionada
     * @return 1 si l'operació és correcta, 0 si l'opció és invàlida
     */
    public Integer setResposta(Integer resposta) {
        this.resposta = resposta;
        setContestat(true);
        return 1;
    }

    /**
     * Getter de la resposta seleccionada
     * @return resposta seleccionada
     */
    public Integer getResposta() {
        return resposta;
    }

    /**
     * Getter del text de la resposta única
     * @param opcions Llista d'opcions de la pregunta
     * @return Text de la resposta única
     */
    @Override
    public String getText(List<String> opcions) {
        if (resposta == null) {
            return "";
        }
        return opcions.get(resposta);
    }
}