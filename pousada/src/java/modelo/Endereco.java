package modelo;

public class Endereco implements java.io.Serializable {
    private int id = -1;
    private String rua;
    private int numero;
    private String cidade;
    private String estado;
    private String pais;
    
    public Endereco() { }
    public Endereco(int id, String rua, int numero, String cidade, String estado, String pais) {
        this(rua, numero, cidade, estado, pais);
        try {
            setId(id);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    public Endereco(String rua, int numero, String cidade, String estado, String pais) {
        try {
            setRua(rua);
            setNumero(numero);
            setCidade(cidade);
            setEstado(estado);
            setPais(pais);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public int getId() {
        return id;
    }
    public String getRua() {
        return rua;
    }
    public int getNumero() {
        return numero;
    }
    public String getCidade() {
        return cidade;
    }
    public String getEstado() {
        return estado;
    }
    public String getPais() {
        return pais;
    }
    
    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("ID inválido.");
        this.id = id;
    }
    public void setRua(String rua) {
        if (rua == null || rua.isEmpty()) 
            throw new IllegalArgumentException("Rua inválida.");
        this.rua = rua;
    }
    public void setNumero(int numero) {
        if (numero <= 0)
            throw new IllegalArgumentException("Número inválido.");
        this.numero = numero;
    }
    public void setCidade(String cidade) {
        if (cidade == null || cidade.isEmpty()) 
            throw new IllegalArgumentException("Cidade inválida.");
        this.cidade = cidade;
    }
    public void setEstado(String estado) {
        if (estado == null || estado.isEmpty()) 
            throw new IllegalArgumentException("Estado inválido.");
        this.estado = estado;
    }
    public void setPais(String pais) {
        if (pais == null || pais.isEmpty()) 
            throw new IllegalArgumentException("País inválido.");
        this.pais = pais;
    }
    
    @Override
    public String toString() {
        return "Rua " + rua + ", " + numero + ", " + cidade + ", " + estado + ", " + pais;
    }
}
