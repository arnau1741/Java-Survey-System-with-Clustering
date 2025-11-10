import java.util.List;

public class RespostaNumerica extends Resposta {
    private Double valor;

    public RespostaNumerica(Double valor) {
        super();
        this.valor = valor;
        setContestat(valor != null);
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
        setContestat(valor != null);
    }

    @Override
    public String getText(List<String> opcions) {
        if (valor == null) {
            return "No contestat";
        }
        return valor.toString();
    }

}
