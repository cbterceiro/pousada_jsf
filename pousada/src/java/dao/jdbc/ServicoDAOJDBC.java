package dao.jdbc;

import dao.ServicoDAO;
import dao.DAOException;

import java.sql.*;
import java.util.ArrayList;

import modelo.Servico;

public class ServicoDAOJDBC implements ServicoDAO {
    private DAOBaseJDBC daoBase;
    private Connection conn;
    
    public ServicoDAOJDBC(DAOBaseJDBC daoBase) {
        this.daoBase = daoBase;
        this.conn = daoBase.getConnection();
    }
    
    @Override
    public Integer inserir(Servico s) throws DAOException {
        PreparedStatement stmt;
        ResultSet generatedKeys;
        int idServico;
        
        idServico = -1;
        try {
            daoBase.iniciarTransacao();
            
            stmt = conn.prepareStatement("INSERT INTO Servico (nome, descricao, valor) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, s.getNome());
            stmt.setString(2, s.getDescricao());
            stmt.setDouble(3, s.getValor());
            stmt.executeUpdate();

            generatedKeys = stmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new DAOException("Erro ao pegar a chave gerada pelo banco de dados no Serviço!");
            }
            idServico = generatedKeys.getInt(1);
            s.setId(idServico);

            stmt.close();
            daoBase.finalizarTransacao();
        }
        catch (SQLException ex) {
            daoBase.descartarTransacao();
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        return idServico;
    }
    

    @Override
    public Servico consultarPorID(Integer idServico) throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        Servico servico;
        
        servico = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM Servico WHERE idServico = ?");
            stmt.setInt(1, idServico);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                servico = new Servico();
                servico.setId(rs.getInt("idServico"));
                servico.setNome(rs.getString("nome"));
                servico.setDescricao(rs.getString("descricao"));
                servico.setValor(rs.getDouble("valor"));
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return servico;
    }

    @Override
    public void alterar(Servico s) throws DAOException {
        PreparedStatement stmt;
        
        try {
            stmt = conn.prepareStatement("UPDATE Servico SET nome = ?, descricao = ?, valor = ? WHERE idServico = ?");
            stmt.setString(1, s.getNome());
            stmt.setString(2, s.getDescricao());
            stmt.setDouble(3, s.getValor());
            stmt.setInt(4, s.getId());
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
    public void remover(Servico s) throws DAOException {
        PreparedStatement stmt;
                
        try {
            daoBase.iniciarTransacao();
            
            this.removerServico(s.getId());
            stmt = conn.prepareStatement("DELETE FROM Servico WHERE idServico = ?");
            stmt.setInt(1, s.getId());
            stmt.executeUpdate();
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
    public ArrayList<Servico> listarTodos() throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        Servico servicoLido;
        ArrayList<Servico> servicos;
        
        servicos = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM Servico");
            rs = stmt.executeQuery();
            
            servicos = new ArrayList<>();
            while(rs.next()) {
                servicoLido = new Servico();
                servicoLido.setId(rs.getInt("idServico"));
                servicoLido.setNome(rs.getString("nome"));
                servicoLido.setDescricao(rs.getString("descricao"));
                servicoLido.setValor(rs.getDouble("valor"));
                servicos.add(servicoLido);
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        return servicos;
    }
    
    @Override
    public Servico consultarPorNome(String nome) throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        Servico servico;
        
        servico = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM Servico WHERE nome = ?");
            stmt.setString(1, nome);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                servico = new Servico();
                servico.setId(rs.getInt("idServico"));
                servico.setNome(rs.getString("nome"));
                servico.setDescricao(rs.getString("descricao"));
                servico.setValor(rs.getDouble("valor"));
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        return servico;
    }
    
    void removerServico(int idServico) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM ReciboServico WHERE idServico = ?");
        stmt.setInt(1, idServico);
        stmt.executeUpdate();
        stmt.close();
    }
    
}
