package dao.jdbc;

import java.sql.*;

public class EquipamentoNxNChaleDAOJDBC {
    public void inserir(Connection conn, int idChale, int idEquipamento) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO EquipamentoChale (idChale, idEquipamento) VALUES (?, ?)");
        stmt.setInt(1, idChale);
        stmt.setInt(2, idEquipamento);
        stmt.executeUpdate();
        stmt.close();
    }
    
    public void remover(Connection conn, int idChale, int idEquipamento) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM EquipamentoChale WHERE idChale = ? AND idEquipamento = ?");
        stmt.setInt(1, idChale);
        stmt.setInt(2, idEquipamento);
        stmt.executeUpdate();
        stmt.close();
    }

    void removerEquipamento(Connection conn, int idEquipamento) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM EquipamentoChale WHERE idEquipamento = ?");
        stmt.setInt(1, idEquipamento);
        stmt.executeUpdate();
        stmt.close();
    }
    
    void removerChale(Connection conn, int idChale) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM EquipamentoChale WHERE idChale = ?");
        stmt.setInt(1, idChale);
        stmt.executeUpdate();
        stmt.close();
    }
}
