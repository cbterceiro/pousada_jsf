package modelo;

public class Telefone implements java.io.Serializable {
    private int id = -1;
    private String ddd;
    private String numero;
    
    public Telefone() { }
    public Telefone(int id, String ddd, String numero) {
        this(ddd, numero);
        try {
            this.setId(id);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    public Telefone(String ddd, String numero) {
        try {
            this.setDdd(ddd);
            this.setNumero(numero);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    
    public int getId() {
        return id;
    }
    public String getDdd() {
        return ddd;
    }
    public String getNumero() {
        return numero;
    }
    
    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("ID inválido.");
        this.id = id;
    }
    public void setDdd(String ddd) {
        try {
            Integer.parseInt(ddd, 10);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("DDD inválido.");
        }
        this.ddd = ddd;
    }
    public void setNumero(String numero) {
        try {
            Integer.parseInt(numero, 10);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Número inválido.");
        }
        this.numero = numero;
    }
    
    @Override
    public String toString() {
        return "(" + ddd + ") " + numero;
    }
}
