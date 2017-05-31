package modelo;

public class Servico implements java.io.Serializable {
    private int id = -1;
    private String nome;
    private String descricao;
    private double valor;
    
    public Servico() { }
    public Servico(int id, String nome, String descricao, double valor) {
        try {
            this.setId(id);
            this.setNome(nome);
            this.setDescricao(descricao);
            this.setValor(valor);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getDescricao() {
        return descricao;
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
    public void setNome(String nome) {
        if (nome == null || nome.isEmpty())
            throw new IllegalArgumentException("Nome inválido.");
        this.nome = nome;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setValor(double valor) {
        if (valor <= 0)
            throw new IllegalArgumentException("Valor inválido.");
        this.valor = valor;
    }
    
    @Override
    public String toString() {
        return nome;
    }
}
