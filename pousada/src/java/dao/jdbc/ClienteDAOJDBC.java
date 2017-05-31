package dao.jdbc;

import dao.ClienteDAO;
import dao.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

import modelo.Cliente;
import modelo.Sexo;
import modelo.Endereco;
import modelo.Telefone;

public class ClienteDAOJDBC implements ClienteDAO {
    private DAOBaseJDBC daoBase;
    private Connection conn;
    
    public ClienteDAOJDBC(DAOBaseJDBC daoBase) {
        this.daoBase = daoBase;
        this.conn = daoBase.getConnection();
    }
    
    @Override
    public Integer inserir(Cliente c) throws DAOException {
        PreparedStatement stmt;
        ResultSet generatedKeys;
        int idCliente;
        Endereco end;
        ArrayList<Telefone> telefones;
        
        idCliente = -1;
        try {
            daoBase.iniciarTransacao();
            
            // Inserindo o cliente
            stmt = conn.prepareStatement("INSERT INTO Pessoa (tipo, login, senha, nome, sexo, dataNascimento) VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, "C");
            stmt.setString(2, c.getLogin());
            stmt.setString(3, c.getSenha());
            stmt.setString(4, c.getNome());
            stmt.setString(5, String.valueOf(c.getSexo().getChar()));
            stmt.setDate(6, new java.sql.Date(c.getDataNascimento().getTimeInMillis()));
            stmt.executeUpdate();
            generatedKeys = stmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new DAOException("Erro ao pegar a chave gerada pelo banco de dados no Cliente!");
            }
            idCliente = generatedKeys.getInt(1);
            c.setId(idCliente);
            
            // Inserindo endereço
            stmt = conn.prepareStatement("INSERT INTO Endereco (rua, numero, cidade, estado, pais, idCliente) VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            end = c.getEndereco();
            stmt.setString(1, end.getRua());
            stmt.setInt(2, end.getNumero());
            stmt.setString(3, end.getCidade());
            stmt.setString(4, end.getEstado());
            stmt.setString(5, end.getPais());
            stmt.setInt(6, c.getId());
            stmt.executeUpdate();
            generatedKeys = stmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new DAOException("Erro ao pegar a chave gerada pelo banco de dados no Endereço!");
            }
            end.setId(generatedKeys.getInt(1));
            
            // Inserindo os telefones
            telefones = c.getTelefones();
            stmt = conn.prepareStatement("INSERT INTO Telefone (ddd, numero, idPessoa) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(3, c.getId());
            for (Telefone t : telefones) {
                stmt.setString(1, t.getDdd());
                stmt.setString(2, t.getNumero());
                stmt.executeUpdate();
                generatedKeys = stmt.getGeneratedKeys();
                if (!generatedKeys.next()) {
                    throw new DAOException("Erro ao pegar a chave gerada pelo banco de dados no Cliente!");
                }
                t.setId(generatedKeys.getInt(1));
            }
            
            stmt.close();
            daoBase.finalizarTransacao();
        }
        catch (SQLException ex) {
            daoBase.descartarTransacao();
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        return idCliente;
    }
    
    @Override
    public Cliente consultarPorID(Integer id) throws DAOException {
        Cliente c;
        PreparedStatement stmt;
        ResultSet rs;
        
        c = null;
        try {
            stmt = conn.prepareStatement("SELECT P.idPessoa, P.login, P.senha, P.nome, P.sexo, P.dataNascimento, E.idEndereco, E.rua, E.numero, E.cidade, E.estado, E.pais FROM Pessoa P INNER JOIN Endereco E ON E.idCliente = P.idPessoa WHERE P.tipo = 'C' AND P.idPessoa = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                c = criarCliente(rs); // Passa as informações do cliente no banco para o objeto Cliente
                c.addListaReserva(daoBase.getReservaDAO().listarPorCliente(c.getId()));
                c.addListaHospedagem(daoBase.getHospedagemDAO().listarPorCliente(c.getId()));
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return c;
    }

    @Override
    public void alterar(Cliente c) throws DAOException {
        PreparedStatement stmt;
        Endereco end;
        ArrayList<Telefone> telefones;
        
        try {
            daoBase.iniciarTransacao();
            
            
            // Alterando cliente
            stmt = conn.prepareStatement("UPDATE Pessoa SET tipo = ?, login = ?, senha = ?, nome = ?, sexo = ?, dataNascimento = ? WHERE idPessoa = ?");
            stmt.setString(1, "C");
            stmt.setString(2, c.getLogin());
            stmt.setString(3, c.getSenha());
            stmt.setString(4, c.getNome());
            stmt.setString(5, String.valueOf(c.getSexo().getChar()));
            stmt.setDate(6, new java.sql.Date(c.getDataNascimento().getTimeInMillis()));
            stmt.setInt(7, c.getId());
            stmt.executeUpdate();

            // Alterando endereço
            stmt = conn.prepareStatement("UPDATE Endereco SET rua = ?, numero = ?, cidade = ?, estado = ?, pais = ?  WHERE idEndereco = ?");
            end = c.getEndereco();
            stmt.setString(1, end.getRua());
            stmt.setInt(2, end.getNumero());
            stmt.setString(3, end.getCidade());
            stmt.setString(4, end.getEstado());
            stmt.setString(5, end.getPais());
            stmt.setInt(6, end.getId());
            stmt.executeUpdate();
  
            // Inserindo os telefones
            telefones = c.getTelefones();
            stmt = conn.prepareStatement("UPDATE Telefone SET ddd = ?, numero = ? WHERE idTelefone = ?");
            for (Telefone t : telefones) {
                stmt.setString(1, t.getDdd());
                stmt.setString(2, t.getNumero());
                stmt.setInt(4, t.getId());
                stmt.executeUpdate();
            }
            
            stmt.close();
            daoBase.finalizarTransacao();
        }
        catch (SQLException ex) {
            daoBase.descartarTransacao();
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void remover(Cliente objeto) throws DAOException {
        
    }

    @Override
    public ArrayList<Cliente> listarTodos() throws DAOException {
        ArrayList<Cliente> clientes;
        Cliente c;
        PreparedStatement stmt;
        ResultSet rs;
        
        clientes = null;
        try {
            stmt = conn.prepareStatement("SELECT P.idPessoa, P.login, P.senha, P.nome, P.sexo, P.dataNascimento, E.idEndereco, E.rua, E.numero, E.cidade, E.estado, E.pais FROM Pessoa P INNER JOIN Endereco E ON E.idCliente = P.idPessoa WHERE P.tipo = 'C'");
            rs = stmt.executeQuery();
            clientes = new ArrayList<>();
            while (rs.next()) {
                c = criarCliente(rs);
                clientes.add(c);
                c.addListaReserva(daoBase.getReservaDAO().listarPorCliente(c.getId()));
                c.addListaHospedagem(daoBase.getHospedagemDAO().listarPorCliente(c.getId()));
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return clientes;
    }
    
    @Override
    public Cliente consultarPorNome(String nome) throws DAOException {
        Cliente c;
        PreparedStatement stmt;
        ResultSet rs;
        
        c = null;
        try {
            stmt = conn.prepareStatement("SELECT P.idPessoa, P.login, P.senha, P.nome, P.sexo, P.dataNascimento, E.idEndereco, E.rua, E.numero, E.cidade, E.estado, E.pais FROM Pessoa P INNER JOIN Endereco E ON E.idCliente = P.idPessoa WHERE P.tipo = 'C' AND P.nome = ?");
            stmt.setString(1, nome);
            rs = stmt.executeQuery();
            if (rs.next()) {
                c = criarCliente(rs); // Passa as informações do cliente no banco para o objeto Cliente
                c.addListaReserva(daoBase.getReservaDAO().listarPorCliente(c.getId()));
                c.addListaHospedagem(daoBase.getHospedagemDAO().listarPorCliente(c.getId()));
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return c;
    }
    
    @Override
    public Cliente consultarPorLoginSenha(String login, String senha) throws DAOException {
        Cliente c;
        PreparedStatement stmt;
        ResultSet rs;
        
        c = null;
        try {
            stmt = conn.prepareStatement("SELECT P.idPessoa, P.login, P.senha, P.nome, P.sexo, P.dataNascimento, E.idEndereco, E.rua, E.numero, E.cidade, E.estado, E.pais FROM Pessoa P INNER JOIN Endereco E ON E.idCliente = P.idPessoa WHERE P.tipo = 'C' AND P.login = ? AND P.senha = ?");
            stmt.setString(1, login);
            stmt.setString(2, senha);
            rs = stmt.executeQuery();
            if (rs.next()) {
                c = criarCliente(rs); // Passa as informações do cliente no banco para o objeto Cliente
                c.addListaReserva(daoBase.getReservaDAO().listarPorCliente(c.getId()));
                c.addListaHospedagem(daoBase.getHospedagemDAO().listarPorCliente(c.getId()));
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return c;
    }
    
    @Override
    public Cliente consultarPorHospedagem(int idHospedagem) throws DAOException {
        Cliente c;
        PreparedStatement stmt;
        ResultSet rs;
        
        c = null;
        try {
            stmt = conn.prepareStatement("SELECT P.idPessoa, P.login, P.senha, P.nome, P.sexo, P.dataNascimento, E.idEndereco, E.rua, E.numero, E.cidade, E.estado, E.pais FROM Pessoa P INNER JOIN Endereco E ON E.idCliente = P.idPessoa INNER JOIN Hospedagem H ON H.idCliente = P.idPessoa WHERE P.tipo = 'C' AND H.idHospedagem = ?");
            stmt.setInt(1, idHospedagem);
            rs = stmt.executeQuery();
            if (rs.next()) {
                c = criarCliente(rs); // Passa as informações do cliente no banco para o objeto Cliente
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return c;
    }
    
    private Cliente criarCliente(ResultSet rs) throws SQLException, DAOException {
        Cliente c;
        int idCliente;
        String sexo;
        Calendar dataNascimento;
        
        c = new Cliente();
        
        idCliente = rs.getInt("idPessoa");
        
        c.setId(idCliente);
        c.setLogin(rs.getString("login"));
        c.setSenha(rs.getString("senha"));
        c.setNome(rs.getString("nome"));
        
        sexo = rs.getString("sexo");
        if ("M".equals(sexo))
            c.setSexo(Sexo.MASCULINO);
        if ("F".equals(sexo))
            c.setSexo(Sexo.FEMININO);
        if ("O".equals(sexo))
            c.setSexo(Sexo.OUTROS);
    
        dataNascimento = Calendar.getInstance();
        dataNascimento.setTime(rs.getDate("dataNascimento"));
        c.setDataNascimento(dataNascimento);
        
        c.setEndereco(rs.getString("rua"), rs.getInt("numero"), rs.getString("cidade"), rs.getString("estado"), rs.getString("pais"));

        obterTelefones(c); // Adiciona no cliente os telefones pertencentes a ele
                
        return c;
    }
    
    private void obterTelefones(Cliente c) throws SQLException {     
        PreparedStatement stmt;
        ResultSet rs;
        
        stmt = conn.prepareStatement("SELECT idTelefone, ddd, numero FROM Telefone WHERE idPessoa = ?");
        stmt.setInt(1, c.getId());
        
        rs = stmt.executeQuery();
        
        while (rs.next()) {
            c.addTelefone(rs.getInt(1), rs.getString(2), rs.getString(3));
        }
        
        stmt.close();
    }
}
