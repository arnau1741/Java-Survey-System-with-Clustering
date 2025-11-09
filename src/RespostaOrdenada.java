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



    @Override
    public String getValorString() {
        return Integer.toString(resposta);
    }
}
