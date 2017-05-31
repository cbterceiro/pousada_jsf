package modelo;

public class Proprietario extends Administrador {
    public Proprietario() { }
    public Proprietario(int id, String login, String senha, String nome) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.nome = nome;
    }
}
