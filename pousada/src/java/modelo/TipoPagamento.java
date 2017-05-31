package modelo;

public class TipoPagamento implements java.io.Serializable {
    private int id = -1;
    private String descricao;
    
    public TipoPagamento() { }
    public TipoPagamento(int id, String descricao) {
        try {
            this.setId(id);
            this.setDescricao(descricao);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    
    public int getId() {
        return id;
    }
    public String getDescricao() {
        return descricao;
    }
    
    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("ID inválido.");
        this.id = id;
    }
    public void setDescricao(String descricao) {
        if (descricao == null || descricao.isEmpty())
            throw new IllegalArgumentException("Descrição inválida.");
        this.descricao = descricao;
    }
}

