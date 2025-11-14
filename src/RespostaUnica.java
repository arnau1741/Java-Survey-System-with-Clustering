import java.util.List;

public class RespostaUnica extends Resposta {
    private int resposta; //ha de ser entre 0 i numOpcions -1
    private int numOpcions;

    /**
     * Constructor de la classe RespostaUnica
     * @param numOpcions nombre d'opcions disponibles
     */
    public RespostaUnica(int numOpcions) {
        super();
        this.resposta = -1; // Indica que no s'ha contestat encara
        this.numOpcions = numOpcions;
    }

    /**
     * Estableix la resposta seleccionada
     * @param resposta opció seleccionada
     * @return 1 si l'operació és correcta, 0 si l'opció és invàlida
     */
    public int setResposta(int resposta) {
        if (resposta < 0 || resposta >= numOpcions) {
            return 0; // Opció invàlida
        }
        this.resposta = resposta;
        setContestat(true);
        return 1;
    }

    /**
     * Getter del nombre d'opcions
     * @return nombre d'opcions disponibles
     */
    public int getNumOpcions() {
        return numOpcions;
    }

    /**
     * Getter de la resposta seleccionada
     * @return resposta seleccionada
     */
    public int getResposta() {
        return resposta;
    }

    /**
     * Getter del text de la resposta única
     * @param opcions Llista d'opcions de la pregunta
     * @return Text de la resposta única
     */
    @Override
    public String getText(List<String> opcions) {
        System.out.println("ha entrado a unica " + opcions.get(resposta));
        return opcions.get(resposta);
    }
}
