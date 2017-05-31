package modelo;

import java.text.Normalizer;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public abstract class Pessoa implements java.io.Serializable {
    protected int id = -1;
    protected String login;
    protected String senha;
    protected String nome;
    protected Sexo sexo;
    protected Calendar dataNascimento;
    
    protected SimpleDateFormat formatData;
    
    public Pessoa() {
        dataNascimento = Calendar.getInstance();
        formatData = new SimpleDateFormat("dd/MM/yyyy");
        dataNascimento.setLenient(false);
        formatData.setLenient(false);
    }
    public Pessoa(int id, String nome, Sexo sexo, Calendar dataNascimento) {
        this();
        try {
            this.setId(id);
            this.setNome(nome);
            this.setSexo(sexo);
            this.setDataNascimento(dataNascimento);
            this.setLogin( this.createLogin() );
            this.setSenha( this.createSenha() );
        }
        catch(IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    public Pessoa(int id, String nome, Sexo sexo, String dataNascimento) {
        this();
        try {
            this.setId(id);
            this.setNome(nome);
            this.setSexo(sexo);
            this.setDataNascimentoString(dataNascimento);
            this.setLogin( this.createLogin() );
            this.setSenha( this.createSenha() );
        }
        catch(IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public int getId() {
        return id;
    }
    public String getLogin() {
        return login;
    }
    public String getSenha() {
        return senha;
    }
    public String getNome() {
        return nome;
    }
    public Sexo getSexo() {
        return sexo;
    }
    public Calendar getDataNascimento() {
        return dataNascimento;
    }
    public String getDataNascimentoString() {
        return formatData.format(dataNascimento.getTime());
    }

    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("ID inválido.");
        this.id = id;
    }
    public void setNome(String nome) {
        String[] nomeCompleto;
        if (nome == null || nome.isEmpty())
            throw new IllegalArgumentException("Nome inválido.");
        
        nomeCompleto = nome.split(" ");
        
        if (nomeCompleto.length <= 1)
            throw new IllegalArgumentException("Nome incompleto.");
        
        nome = "";
        for (String n : nomeCompleto) {
            try {
                nome += Character.toUpperCase(n.charAt(0)) + n.substring(1).toLowerCase() + " ";
            }
            catch (StringIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Nome inválido.");
            }
        }
        
        this.nome = nome.trim();
        this.createLogin();
        this.createSenha();
    }
    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }
    public void setDataNascimento(Calendar dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    public void setDataNascimentoString(String dataNascimento) {
        try {
            this.dataNascimento.setTime(formatData.parse(dataNascimento));
        }
        catch (ParseException e) {
            throw new IllegalArgumentException("Data inválida: " + dataNascimento);
        }
    }
    public void setLogin(String login) {
        if (login == null || login.isEmpty())
            throw new IllegalArgumentException("Login inválido.");
        this.login = login;
    }
    public void setSenha(String senha) {
        if (senha == null || senha.isEmpty())
            throw new IllegalArgumentException("Senha inválida.");
        this.senha = senha;
    }
    
    public String createLogin() {
        String[] nomeCompleto = nome.split(" ");
        int n = nomeCompleto.length - 1;

        return (java.text.Normalizer
            .normalize(nomeCompleto[0].toLowerCase() + Character.toUpperCase(nomeCompleto[n].charAt(0)), Normalizer.Form.NFD)
            .replaceAll("[^\\p{ASCII}]", ""));
    }
    public String createSenha() {
        return "123456";
    }
    
    @Override
    public String toString() {
        return nome;
    }
}
