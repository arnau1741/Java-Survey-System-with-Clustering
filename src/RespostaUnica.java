import java.util.List;

public class RespostaUnica extends Resposta {
    private int resposta; //ha de ser entre 0 i numOpcions -1
    private int numOpcions;

    public RespostaUnica(int numOpcions) {
        super();
        this.numOpcions = numOpcions;
    }

    public int setResposta(int resposta) {
        if (resposta < 0 || resposta >= numOpcions) {
            return 0; // Opció invàlida
        }
        this.resposta = resposta;
        setContestat(true);
        return 1;
    }


    // Retorna totes les opcions
    public int getNumOpcions() {
        return numOpcions;
    }

    //Retorna l'opció seleccionada
    public int getResposta() {
        return resposta;
    }

    @Override
    public String getText(List<String> opcions) {
        System.out.println("ha entrado a unica " + opcions.get(resposta));
        return opcions.get(resposta);
    }
}
