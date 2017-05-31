package modelo;

public class ParcelaPagamento implements java.io.Serializable {
    private int id = -1;
    private TipoPagamento tipoPagamento;
    private double valor;
    
    public ParcelaPagamento() { }
    public ParcelaPagamento(TipoPagamento tipo, double valor) {
        try {
            setTipoPagamento(tipo);
            setValor(valor);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }        

    public int getId() {
        return id;
    }
    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }
    public double getValor() {
        return valor;
    }
    public String getValorString() {
        return java.text.NumberFormat.getCurrencyInstance().format(valor);
    }

    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("ID inválido.");
        this.id = id;
    }
    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }
    public void setValor(double valor) {
        if (valor < 0)
            throw new IllegalArgumentException("Valor inválido.");
        this.valor = valor;
    }
    
}
