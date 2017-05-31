package modelo;

import modelo.excecoes.PagamentoOverflowException;
import java.util.ArrayList;

public class Pagamento implements java.io.Serializable {
    private int id = -1;    
    private double valorTotal;
    private double valorAtual;
    private ArrayList<ParcelaPagamento> parcelas;
    
    public Pagamento() {
        valorAtual = 0;
        parcelas = new ArrayList<>();
    }
    public Pagamento(int id) {
        this();
        try {
            this.setId(id);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    public Pagamento(int id, double valorTotal) {
        this(id);
        try {
            this.setValorTotal(valorTotal);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public int getId() {
        return id;
    }
    public double getValorTotal() {
        return valorTotal;
    }
    public double getValorAtual() {
        return valorAtual;
    }
    public ArrayList<ParcelaPagamento> getParcelas() {
        return parcelas;
    }
    public String getValorTotalString() {
        return java.text.NumberFormat.getCurrencyInstance().format(valorTotal);
    }
    public String getValorAtualString() {
        return java.text.NumberFormat.getCurrencyInstance().format(valorAtual);
    }
    
    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("ID inválido.");
        this.id = id;
    }
    public void setValorAtual(double valorAtual) {
        if (valorAtual < 0)
            throw new IllegalArgumentException("Valor inválido.");
        this.valorAtual = valorAtual;
    }
    public void setValorTotal(double valorTotal) {
        if (valorTotal <= 0)
            throw new IllegalArgumentException("Valor inválido.");
        this.valorTotal = valorTotal;
    }
    
    public void addParcelaPagamento(TipoPagamento tipo, double valor) throws PagamentoOverflowException {
        if (valorAtual + valor > valorTotal)
            throw new PagamentoOverflowException("Valor acima do total.");
        this.parcelas.add(new ParcelaPagamento(tipo, valor));
        valorAtual += valor;
    }
    public void removeAllParcelas() {
        this.parcelas = new ArrayList<>();
    }
    
    public boolean estaPago() {
        return (valorAtual == valorTotal);
    }
}
