package modelo;

public enum Sexo implements java.io.Serializable {
    MASCULINO('M', "Masculino"), FEMININO('F', "Feminino"), OUTROS('O', "Outros");
    
    private char ch;
    private String nome;
    
    Sexo(char ch, String nome) {
        this.ch = ch;
        this.nome = nome;
    }
    
    public char getChar() {
        return ch;
    }
    public String getNome() {
        return nome;
    }
    
    @Override
    public String toString() {
        return getNome();
    }   
}
