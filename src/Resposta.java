
//Pre: idPregunta és un identificador vàlid de pregunta
//Post: Crea una resposta associada a la classe
//resposta i es guarda en els diferents tipus de classe Resposta
public abstract class Resposta {
    //Atributs de la classe Resposta
    private boolean contestat;  //Sera true si la resposta està contestada

    public Resposta() {
        this.contestat = false;
    }


    public boolean EsContestat() {
        return contestat;
    }

    public void setContestat(boolean contestat) {
        this.contestat = contestat;
    }

    public abstract String getValorString();
    //Excepcio per validar els caracters i altres coses
    //public abstract void validar() throws RespostaInvalida;
    //public abstract boolean comparaRes(Resposta res2);
}
