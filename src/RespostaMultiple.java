
import java.util.List;
import java.util.ArrayList;

public class RespostaMultiple extends Resposta{
    private Integer numOpcions;
    private List<Integer> respostes; //valors entre 0 i numOpcions -1

    public RespostaMultiple(Integer numOpcions) {
        super();
        this.numOpcions = numOpcions;
    }

    //Marca una opció com a seleccionada
    public int selecciona (List<Integer> seleccionat) {
        this.respostes = new ArrayList<>();
        for (Integer opcio : seleccionat) {
            if (opcio < 0 || opcio >= numOpcions) {
                return 0; // Opció invàlida
            }
            this.respostes.add(opcio);
        }
        if(!this.respostes.isEmpty()) setContestat(true);
        return 1;
    }

    public int getNumOpcions() {
        return numOpcions;
    }

    // Retorna només les opcions seleccionades com a String
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
