package prop.enquestes.domini;
import java.util.List;

public class RespostaNumerica extends Resposta {
    private Double valor;

    /**
     * Constructor de la classe RespostaNumerica
     * @param valor de la resposta numèrica
     */
    public RespostaNumerica(Double valor) {
        super();
        this.valor = valor;
        setContestat(valor != null);
    }

    /**
     * Getter del valor de la resposta numèrica
     * @return valor de la resposta numèrica
     */
    public Double getValor() {
        return valor;
    }

    /**
     * Setter del valor de la resposta numèrica
     * @param valor de la resposta numèrica
     */
    public void setValor(Double valor) {
        this.valor = valor;
        setContestat(valor != null);
    }

    /**
     * Getter del text de la resposta numèrica
     * @param opcions no s'utilitza en aquesta classe
     * @return text de la resposta numèrica
     */
    @Override
    public String getText(List<String> opcions) {
        if (this.valor == null) {
            return "NO CONTESTAT";
        }
        return valor.toString();
    }
}