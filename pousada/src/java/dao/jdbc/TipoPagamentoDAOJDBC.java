package dao.jdbc;

import dao.TipoPagamentoDAO;
import dao.DAOException;

import java.sql.*;

import modelo.TipoPagamento;

public class TipoPagamentoDAOJDBC implements TipoPagamentoDAO {
    private DAOBaseJDBC daoBase;
    private Connection conn;
    
    public TipoPagamentoDAOJDBC(DAOBaseJDBC daoBase) {
        this.daoBase = daoBase;
        this.conn = daoBase.getConnection();
    }

    @Override
    public TipoPagamento consultarPorID(int id) throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        TipoPagamento tp;
        
        tp = null;
        try {
            stmt = conn.prepareStatement("SELECT idTipoPagamento, descricao FROM TipoPagamento WHERE idTipoPagamento = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                tp = new TipoPagamento(rs.getInt(1), rs.getString(2));
            }
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return tp;
    }

    @Override
    public TipoPagamento consultarPorDescricao(String descricao) throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        TipoPagamento tp;
        
        tp = null;
        try {
            stmt = conn.prepareStatement("SELECT idTipoPagamento, descricao FROM TipoPagamento WHERE descricao = ?");
            stmt.setString(1, descricao);
            rs = stmt.executeQuery();
            if (rs.next()) {
                tp = new TipoPagamento(rs.getInt(1), rs.getString(2));
            }
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return tp;
    }
   
}
