
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
    }

    //Falta revisar el valor
    @Override
    public String getValorString() {
        if(valor == null) return "No contestada";
        else return String.valueOf(valor);
    }
}
