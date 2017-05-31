package modelo;

import java.util.Objects;

public class Equipamento implements java.io.Serializable {
    private int id = -1;
    private String descricao;
    
    public Equipamento() { }
    public Equipamento(String descricao) {
        try {
            setDescricao(descricao);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    public Equipamento(int id, String descricao) {
        this(descricao);
        try {
            setId(id);
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
    
    @Override
    public String toString() {
        return descricao;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.descricao);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Equipamento other = (Equipamento) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        return true;
    }
    
    
}
