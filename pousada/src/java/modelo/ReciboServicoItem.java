package modelo;

/**
 *
 * @author caiod
 */
public class ReciboServicoItem {
    private int id;
    private String descricao;
    private double valorUnitario;
    private int quantidade;
    private double valorTotal;
    private static int controleId = 1;

    public ReciboServicoItem() {
    }

    public ReciboServicoItem(int quantidade, Servico servico) {
        this.quantidade = quantidade;
        this.descricao = servico.getNome();
        this.valorUnitario = servico.getValor();
        this.valorTotal = quantidade * valorUnitario;
        this.id = controleId ++;
    }    

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    
}
