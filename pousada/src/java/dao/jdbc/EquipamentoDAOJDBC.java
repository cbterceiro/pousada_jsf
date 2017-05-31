package dao.jdbc;

import dao.EquipamentoDAO;
import dao.DAOException;

import java.sql.*;
import java.util.ArrayList;

import modelo.Equipamento;

public class EquipamentoDAOJDBC implements EquipamentoDAO {
    private DAOBaseJDBC daoBase;
    private Connection conn;
    
    public EquipamentoDAOJDBC(DAOBaseJDBC daoBase) {
        this.daoBase = daoBase;
        this.conn = daoBase.getConnection();
    }
    
    @Override
    public Integer inserir(Equipamento e) throws DAOException {
        PreparedStatement stmt;
        ResultSet generatedKeys;
        int idEquipamento;
        
        idEquipamento = -1;
        try {
            daoBase.iniciarTransacao();
            
            stmt = conn.prepareStatement("INSERT INTO Equipamento (descricao) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, e.getDescricao());
            stmt.executeUpdate();

            generatedKeys = stmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new DAOException("Erro ao pegar a chave gerada pelo banco de dados no Equipamento!");
            }
            idEquipamento = generatedKeys.getInt(1);
            e.setId(idEquipamento);

            stmt.close();
            daoBase.finalizarTransacao();
        }
        catch (SQLException ex) {
            daoBase.descartarTransacao();
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        return idEquipamento;
    }

    @Override
    public Equipamento consultarPorID(Integer idEquipamento) throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        Equipamento equipamento;
        
        equipamento = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM Equipamento WHERE idEquipamento = ?");
            stmt.setInt(1, idEquipamento);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                equipamento = new Equipamento();
                equipamento.setId(rs.getInt("idEquipamento"));
                equipamento.setDescricao(rs.getString("descricao"));
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return equipamento;
    }

    @Override
    public void alterar(Equipamento e) throws DAOException {
        PreparedStatement stmt;
        
        try {
            stmt = conn.prepareStatement("UPDATE Equipamento SET descricao = ? WHERE idEquipamento = ?");
            stmt.setString(1, e.getDescricao());
            stmt.setInt(2, e.getId());
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
    public void remover(Equipamento e) throws DAOException {
        PreparedStatement stmt;
        EquipamentoNxNChaleDAOJDBC eqcDao = new EquipamentoNxNChaleDAOJDBC();
        
        try {
            daoBase.iniciarTransacao();
            
            eqcDao.removerEquipamento(conn, e.getId());
            
            stmt = conn.prepareStatement("DELETE FROM Equipamento WHERE idEquipamento = ?");
            stmt.setInt(1, e.getId());
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
    public ArrayList<Equipamento> listarTodos() throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        Equipamento equipamentoLido;
        ArrayList<Equipamento> equipamentos;
        
        equipamentos = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM Equipamento");
            rs = stmt.executeQuery();
            
            equipamentos = new ArrayList<>();
            while(rs.next()) {
                equipamentoLido = new Equipamento();
                equipamentoLido.setId(rs.getInt("idEquipamento"));
                equipamentoLido.setDescricao(rs.getString("descricao"));
                equipamentos.add(equipamentoLido);
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        return equipamentos;
    }
    
    @Override
    public Equipamento consultarPorDescricao(String descricao) throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        Equipamento equipamento;
        
        equipamento = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM Equipamento WHERE descricao = ?");
            stmt.setString(1, descricao);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                equipamento = new Equipamento();
                equipamento.setId(rs.getInt("idEquipamento"));
                equipamento.setDescricao(rs.getString("descricao"));
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        return equipamento;
    }
    
    @Override
    public ArrayList<Equipamento> listarPorChale(Integer idChale) throws DAOException {
        String sql = "SELECT e.idEquipamento, e.descricao FROM Chale c "
                + "INNER JOIN EquipamentoChale ec on c.idChale = ec.idChale "
                + "INNER JOIN Equipamento e on ec.idEquipamento = e.idEquipamento "
                + "WHERE c.idChale = ?";
        
        PreparedStatement stmt;
        ResultSet rs;
        Equipamento equipamentoLido;
        ArrayList<Equipamento> equipamentos;
        
        equipamentos = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idChale);
            rs = stmt.executeQuery();
            
            equipamentos = new ArrayList<>();
            while (rs.next()) {
                equipamentoLido = new Equipamento();
                equipamentoLido.setId(rs.getInt("idEquipamento"));
                equipamentoLido.setDescricao(rs.getString("descricao"));
                equipamentos.add(equipamentoLido);
            }
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        return equipamentos;
    }
}
