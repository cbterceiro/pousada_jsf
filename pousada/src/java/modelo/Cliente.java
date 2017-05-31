package modelo;

import java.util.ArrayList;
import java.util.Calendar;

public class Cliente extends Pessoa {
    Endereco endereco;
    ArrayList<Telefone> telefones;
    
    ArrayList<Reserva> reservas;
    ArrayList<Hospedagem> hospedagens;
   
    public Cliente() {
        telefones = new ArrayList<>();
        reservas = new ArrayList<>();
        hospedagens = new ArrayList<>();
        endereco = new Endereco();
    }
    public Cliente(int id, String nome, Sexo sexo, String dataNascimento) {
        super(id, nome, sexo, dataNascimento);
        telefones = new ArrayList<>();
        reservas = new ArrayList<>();
        hospedagens = new ArrayList<>();
        endereco = new Endereco();
    }
    public Cliente(int id, String nome, Sexo sexo, String dataNascimento, String rua, int numero,String cidade, String estado, String pais, String referencia) {
        this(id, nome, sexo, dataNascimento);
        try {
            this.setEndereco(rua, numero, cidade, estado, pais);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public Endereco getEndereco() {
        return endereco;
    }
    public ArrayList<Telefone> getTelefones() {
        return telefones;
    }
    public ArrayList<Reserva> getReservas() {
        return reservas;
    }
    public ArrayList<Hospedagem> getHospedagens() {
        return hospedagens;
    }
        
    public void setEndereco(String rua, int numero, String cidade, String estado, String pais) {
        this.endereco = new Endereco();
        this.endereco.setRua(rua);
        this.endereco.setNumero(numero);
        this.endereco.setCidade(cidade);
        this.endereco.setEstado(estado);
        this.endereco.setPais(pais);
    }
    
    public void setTelefones(ArrayList<Telefone> telefones) {
        this.telefones = telefones;
    }
    
    public void addTelefone(int idTelefone, String ddd, String numero) {
        this.telefones.add(new Telefone(idTelefone, ddd, numero));
    }
    
    public void addTelefone(String ddd, String numero) {
        this.telefones.add(new Telefone(ddd, numero));
    }
    
    public void addReserva(int idReserva, Chale chale, Calendar dataInicio, Calendar dataFim) {
        this.reservas.add(new Reserva(idReserva, chale, dataInicio, dataFim));
    }
    
    public void addReserva(Chale chale, Calendar dataInicio, Calendar dataFim) {
        this.reservas.add(new Reserva(chale, dataInicio, dataFim));
    }
    
    public void addReserva(int idReserva, Chale chale, String dataInicio, String dataFim) {
        this.reservas.add(new Reserva(idReserva, chale, dataInicio, dataFim));
    }
    
    public void addReserva(Chale chale, String dataInicio, String dataFim) {
        this.reservas.add(new Reserva(chale, dataInicio, dataFim));
    }
    
    public void addListaReserva(ArrayList<Reserva> reservas) {
        this.reservas.addAll(reservas);
    }
    
    public void addHospedagem(Hospedagem h) {
        this.hospedagens.add(h);
    }
    
    public void addListaHospedagem(ArrayList<Hospedagem> hospedagens) {
        this.hospedagens.addAll(hospedagens);
    }
    
    public void removeHospedagem(Hospedagem h) {
        this.hospedagens.remove(h);
    }
    
    public Reserva getUltimaReservaAdicionada() {
        return this.reservas.get(reservas.size() - 1);
    }
    
    @Override
    public String createLogin() {
        return super.createLogin();
    }
    
    public String info() {
        String s = "";
        
        s += "Nome: " + nome + "\n";
        s += "Login: " + login + "\n";
        s += "Sexo: " + sexo.toString() + "\n";
        s += "Data de nascimento: " + getDataNascimentoString() + "\n";
        s += "Endereço: " + endereco.toString() + "\n";
        
        s += "Telefones:\n";
        if (telefones.isEmpty())
            s += "    Nenhum.\n";
        else
            for (Telefone t : telefones)
                s += "    " + t.toString() + "\n";
        
        s += "Reservas:\n";
        if (reservas.isEmpty())
            s += "    Nenhuma.\n";
        else
            for (Reserva r : reservas)
                s += "   " + r.toString() + "\n";
        
        s += "Hospedagens:\n";
        if (hospedagens.isEmpty())
            s += "    Nenhuma.\n";
        else
            for (Hospedagem h : hospedagens)
                s += "    " + h.toString() + "\n";
           
        return s;
    }
}
