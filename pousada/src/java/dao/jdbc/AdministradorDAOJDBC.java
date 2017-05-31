package dao.jdbc;

import dao.AdministradorDAO;
import dao.DAOException;

import java.sql.*;
import java.util.Calendar;
import java.util.ArrayList;

import modelo.Administrador;
import modelo.Sexo;

public class AdministradorDAOJDBC implements AdministradorDAO {
    private DAOBaseJDBC daoBase;
    private Connection conn;
    
    public AdministradorDAOJDBC(DAOBaseJDBC daoBase) {
        this.daoBase = daoBase;
        this.conn = daoBase.getConnection();
    }
    
    @Override
    public Integer inserir(Administrador a) throws DAOException {
        PreparedStatement stmt;
        ResultSet generatedKeys;
        int idAdministrador;
        
        idAdministrador = -1;
        try {
            daoBase.iniciarTransacao();
            
            stmt = conn.prepareStatement("INSERT INTO Pessoa (tipo, login, senha, nome, sexo, dataNascimento) VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, "A");
            stmt.setString(2, a.getLogin());
            stmt.setString(3, a.getSenha());
            stmt.setString(4, a.getNome());
            stmt.setString(5, String.valueOf(a.getSexo().getChar()));
            stmt.setDate(6, new java.sql.Date(a.getDataNascimento().getTimeInMillis()));
            stmt.executeUpdate();
            
            generatedKeys = stmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new DAOException("Erro ao pegar a chave gerada pelo banco de dados no Administrador!");
            }
            idAdministrador = generatedKeys.getInt(1);
            a.setId(idAdministrador);
            
            stmt.close();
            daoBase.finalizarTransacao();
        }
        catch (SQLException ex) {
            daoBase.descartarTransacao();
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return idAdministrador;
    }
    
    @Override
    public Administrador consultarPorID(Integer id) throws DAOException {
        Administrador a;
        PreparedStatement stmt;
        ResultSet rs;
        
        a = null;
        try {
            stmt = conn.prepareStatement("SELECT idPessoa, login, senha, nome, sexo, dataNascimento FROM Pessoa WHERE tipo = 'A' AND idPessoa = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                a = criarAdministrador(rs);
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return a;
    }

    @Override
    public void alterar(Administrador a) throws DAOException {
        PreparedStatement stmt;    
        
        try {
            stmt = conn.prepareStatement("UPDATE Pessoa SET tipo = ?, login = ?, senha = ?, nome = ?, sexo = ?, dataNascimento = ? WHERE idPessoa = ?");
            stmt.setString(1, "A");
            stmt.setString(2, a.getLogin());
            stmt.setString(3, a.getSenha());
            stmt.setString(4, a.getNome());
            stmt.setString(5, String.valueOf(a.getSexo().getChar()));
            stmt.setDate(6, new java.sql.Date(a.getDataNascimento().getTimeInMillis()));
            stmt.setInt(7, a.getId());
            
            stmt.executeUpdate();
            stmt.close();
        } 
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void remover(Administrador a) throws DAOException {
        PreparedStatement stmt;
                
        try {
            stmt = conn.prepareStatement("DELETE FROM Pessoa WHERE idPessoa = ?");
            stmt.setInt(1, a.getId());
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public ArrayList<Administrador> listarTodos() throws DAOException {
        ArrayList<Administrador> administradores;
        PreparedStatement stmt;
        ResultSet rs;
        
        administradores = null;
        try {
            stmt = conn.prepareStatement("SELECT P.idPessoa, P.login, P.senha, P.nome, P.sexo, P.dataNascimento, P.idEndereco, E.rua, E.numero, E.cidade, E.estado, E.pais FROM Pessoa P INNER JOIN Endereco E ON P.idEndereco = E.idEndereco WHERE P.tipo = 'A'");
            rs = stmt.executeQuery();
            administradores = new ArrayList<>();
            while (rs.next()) {
                administradores.add(criarAdministrador(rs));
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return administradores;
    }
    
    @Override
    public Administrador consultarPorNome(String nome) throws DAOException {
        Administrador a;
        PreparedStatement stmt;
        ResultSet rs;
        
        a = null;
        try {
            stmt = conn.prepareStatement("SELECT idPessoa, login, senha, nome, sexo, dataNascimento FROM Pessoa WHERE tipo = 'A' AND nome = ?");
            stmt.setString(1, nome);
            rs = stmt.executeQuery();
            if (rs.next()) {
                a = criarAdministrador(rs);
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return a;
    }
    
    @Override
    public Administrador consultarPorLoginSenha(String login, String senha) throws DAOException {
        Administrador a;
        PreparedStatement stmt;
        ResultSet rs;
        
        a = null;
        try {
            stmt = conn.prepareStatement("SELECT idPessoa, login, senha, nome, sexo, dataNascimento FROM Pessoa WHERE tipo = 'A' AND login = ? AND senha = ?");
            stmt.setString(1, login);
            stmt.setString(2, senha);
            rs = stmt.executeQuery();
            if (rs.next()) {
                a = criarAdministrador(rs);
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return a;
    }
    
    private Administrador criarAdministrador(ResultSet rs) throws SQLException {
        Administrador a;
        String sexo;
        Calendar dataNascimento;
        
        a = new Administrador();
        
        a.setId(rs.getInt("idPessoa"));
        a.setLogin(rs.getString("login"));
        a.setSenha(rs.getString("senha"));
        a.setNome(rs.getString("nome"));
        
        sexo = rs.getString("sexo");
        if ("M".equals(sexo))
            a.setSexo(Sexo.MASCULINO);
        if ("F".equals(sexo))
            a.setSexo(Sexo.FEMININO);
        if ("O".equals(sexo))
            a.setSexo(Sexo.OUTROS);
        
        dataNascimento = Calendar.getInstance();
        dataNascimento.setTime(rs.getDate("dataNascimento"));
        a.setDataNascimento(dataNascimento);
        
        return a;
    }
}
