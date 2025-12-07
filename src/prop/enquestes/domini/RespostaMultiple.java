package prop.enquestes.domini;
import java.util.List;
import java.util.ArrayList;

public class RespostaMultiple extends Resposta{
    private List<Integer> respostes; //valors entre 0 i numOpcions -1

    /**
     * Constructor de la classe RespostaMultiple
     * @param numOpcions nombre d'opcions disponibles
     */
    public RespostaMultiple() {
        super();
    }

    /**
     * Marca una opcio com a seleccionada
     * @param seleccionat llista d'opcions seleccionades
     * @return 1 si l'operació és correcta, 0 si hi ha una opció invàlida
     */
    public int selecciona(List<Integer> seleccionat) {
        this.respostes = new ArrayList<>();
        for (Integer opcio : seleccionat) {
            this.respostes.add(opcio);
        }
        if(!this.respostes.isEmpty()) setContestat(true);
        return 1;
    }

     /**
     * Getter de les respostes seleccionades
     * @return llista d'opcions seleccionades
     */
    public List<Integer> getRespostes() {
        return respostes;
    }

    /**
     * Getter del text de les opcions seleccionades
     * @param opcions Llista d'opcions de la pregunta
     * @return Text de les opcions seleccionades
     */
    @Override
    public String getText(List<String> opcions) {
        StringBuilder sb = new StringBuilder();
        for (Integer opcio : respostes) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(opcions.get(opcio));
        }
        return sb.toString();
    }
}