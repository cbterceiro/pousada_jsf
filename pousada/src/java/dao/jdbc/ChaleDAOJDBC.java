package dao.jdbc;

import dao.ChaleDAO;
import dao.DAOException;

import java.sql.*;
import java.util.ArrayList;

import modelo.Chale;
import modelo.Equipamento;

public class ChaleDAOJDBC implements ChaleDAO {
    private DAOBaseJDBC daoBase;
    private Connection conn;
    
    public ChaleDAOJDBC(DAOBaseJDBC daoBase) {
        this.daoBase = daoBase;
        this.conn = daoBase.getConnection();
    }
    
    @Override
    public Integer inserir(Chale c) throws DAOException {
        PreparedStatement stmt;
        ResultSet generatedKeys;
        int idChale;
        
        idChale = -1;
        try {
            daoBase.iniciarTransacao();
            
            stmt = conn.prepareStatement("INSERT INTO Chale (numero, diaria, estaOcupado) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            
            stmt.setInt(1, c.getNumero());
            stmt.setDouble(2, c.getDiaria());
            stmt.setBoolean(3, c.getEstaOcupado());
            stmt.executeUpdate();
            
            generatedKeys = stmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new DAOException("Erro ao pegar a chave gerada pelo banco de dados no Chalé!");
            }
            idChale = generatedKeys.getInt(1);
            c.setId(idChale);
            
            salvarEquipamentos(c);
            
            stmt.close();
            daoBase.finalizarTransacao();
        }
        catch (SQLException ex) {
            daoBase.descartarTransacao();
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return idChale;
    }
    
    @Override
    public Chale consultarPorID(Integer idChale) throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        Chale chale;
        
        chale = null;
        try{
            stmt = conn.prepareStatement("SELECT * FROM Chale c WHERE c.idChale = ?");
            stmt.setInt(1, idChale);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                chale = new Chale();
                chale.setId(rs.getInt("idChale"));
                chale.setNumero(rs.getInt("numero"));
                chale.setDiaria(rs.getDouble("diaria"));
                chale.setEstaOcupado(rs.getBoolean("estaOcupado"));
                chale.setEquipamentos(obterEquipamentos(idChale));
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        return chale;
    }

    @Override
    public void alterar(Chale c) throws DAOException {
        PreparedStatement stmt;
        try {
            daoBase.iniciarTransacao();
            
            stmt = conn.prepareStatement("UPDATE Chale SET numero = ?, diaria = ?, estaOcupado = ? WHERE idChale = ?");
            stmt.setInt(1, c.getNumero());
            stmt.setDouble(2, c.getDiaria());
            stmt.setBoolean(3, c.getEstaOcupado());
            stmt.setInt(4, c.getId());
            stmt.executeUpdate();
            
            salvarEquipamentos(c);
            
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
    public void remover(Chale c) throws DAOException {
        PreparedStatement stmt;
        EquipamentoNxNChaleDAOJDBC eqcDao = new EquipamentoNxNChaleDAOJDBC();
        
        try {
            daoBase.iniciarTransacao();
            
            eqcDao.removerChale(conn, c.getId());
            stmt = conn.prepareStatement("DELETE FROM Chale WHERE idChale = ? ");

            stmt.setInt(1, c.getId());
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
    public ArrayList<Chale> listarTodos() throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        Chale chaleLido;
        ArrayList<Chale> chales;
        
        chales = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM Chale");
            rs = stmt.executeQuery();
            
            chales = new ArrayList<>();
            while (rs.next()) {
                chaleLido = new Chale();
                chaleLido.setId(rs.getInt("idChale"));
                chaleLido.setNumero(rs.getInt("numero"));
                chaleLido.setDiaria(rs.getDouble("diaria"));
                chaleLido.setEstaOcupado(rs.getBoolean("estaOcupado"));
                chaleLido.setEquipamentos(obterEquipamentos(rs.getInt("idChale")));
                chales.add(chaleLido);
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        return chales;
    }
    
    @Override
    public Chale consultarPorNumero(int numero) throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        Chale chale;
        
        chale = null;
        try{
            stmt = conn.prepareStatement("SELECT * FROM Chale WHERE numero = ?");
            stmt.setInt(1, numero);
            rs = stmt.executeQuery();
            if (rs.next()) {
                chale = new Chale();
                chale.setId(rs.getInt("idChale"));
                chale.setNumero(rs.getInt("numero"));
                chale.setDiaria(rs.getDouble("diaria"));
                chale.setEstaOcupado(rs.getBoolean("estaOcupado"));
                chale.setEquipamentos(obterEquipamentos(rs.getInt("idchale")));
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        return chale;
    }

    @Override
    public ArrayList<Chale> listarDesocupados() throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        Chale chaleLido;
        ArrayList<Chale> chales;
        
        chales = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM Chale WHERE estaOcupado = false");
            rs = stmt.executeQuery();
            
            chales = new ArrayList<>();
            while (rs.next()) {
                chaleLido = new Chale();
                chaleLido.setId(rs.getInt("idChale"));
                chaleLido.setNumero(rs.getInt("numero"));
                chaleLido.setDiaria(rs.getDouble("diaria"));
                chaleLido.setEstaOcupado(rs.getBoolean("estaOcupado"));
                chaleLido.setEquipamentos(obterEquipamentos(rs.getInt("idChale")));
                chales.add(chaleLido);
            }
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        return chales;
    }
    
    private ArrayList<Equipamento> obterEquipamentos(Integer idChale) throws DAOException {
        return daoBase.getEquipamentoDAO().listarPorChale(idChale);
    }
    
    private void salvarEquipamentos(Chale c) throws SQLException, DAOException {
        int idChale = c.getId();
        ArrayList<Equipamento> equipamentosBanco  = obterEquipamentos(idChale);
        ArrayList<Equipamento> equipamentosObjeto = c.getEquipamentos();
        EquipamentoNxNChaleDAOJDBC eqcDao = new EquipamentoNxNChaleDAOJDBC();
        
        for (Equipamento equipamento : equipamentosBanco) {
            // Se o equipamento que está no banco não estiver no
            // objeto então remove a conexão entre os dois do banco
            if (!equipamentosObjeto.contains(equipamento)) { 
                eqcDao.remover(conn, idChale, equipamento.getId());
            }   
        }
        for (Equipamento equipamento : equipamentosObjeto) {
            // Se o equipamento que está no objeto não estiver no
            // banco, então insere a conexão entre os dois no banco
            if (!equipamentosBanco.contains(equipamento)) {
                eqcDao.inserir(conn, idChale, equipamento.getId());
            }
        }
    }

}
