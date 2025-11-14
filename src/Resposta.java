import java.util.List;
//Pre: idPregunta és un identificador vàlid de pregunta
//Post: Crea una resposta associada a la classe
//resposta i es guarda en els diferents tipus de classe Resposta
public abstract class Resposta {
    //Atributs de la classe Resposta
    private boolean contestat;  //Sera true si la resposta està contestada

    /**
     * Constructor de la classe Resposta
     */
    public Resposta() {
        this.contestat = false;
    }

    /**
     * Mètode per saber si la resposta està contestada
     * @return true si està contestada, false en cas contrari
     */
    public boolean EsContestat() {
        return contestat;
    }

    /**
     * Mètode per establir si la resposta està contestada
     * @param contestat true si està contestada, false en cas contrari
     */
    public void setContestat(boolean contestat) {
        this.contestat = contestat;
    }

    /**
     * Mètode per obtenir el text de la resposta
     * @param opcions Llista d'opcions de la pregunta
     * @return Text de la resposta
     */
    public abstract String getText(List<String> opcions);
    //Excepcio per validar els caracters i altres coses
    //public abstract void validar() throws RespostaInvalida;
    //public abstract boolean comparaRes(Resposta res2);
}
