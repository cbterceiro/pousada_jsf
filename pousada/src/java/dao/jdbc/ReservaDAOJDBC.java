package dao.jdbc;

import modelo.Reserva;
import modelo.Chale;

import dao.ReservaDAO;
import dao.ChaleDAO;
import dao.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class ReservaDAOJDBC implements ReservaDAO {
    private DAOBaseJDBC daoBase;
    private Connection conn;
    
    public ReservaDAOJDBC(DAOBaseJDBC daoBase) {
        this.daoBase = daoBase;
        this.conn = daoBase.getConnection();
    }
    
    @Override
    public Integer inserir(Reserva r, int idCliente) throws DAOException {
        PreparedStatement stmt;
        ResultSet generatedKeys;
        Chale chale;
        int idReserva;
        
        idReserva = -1;
        try {
            daoBase.iniciarTransacao();
            
            chale = r.getChale();
            chale.setEstaOcupado(true);
            daoBase.getChaleDAO().alterar(chale);
            
            stmt = conn.prepareStatement("INSERT INTO Reserva (dataInicio, dataFim, idCliente, idChale) VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setDate(1, new java.sql.Date(r.getDataInicio().getTimeInMillis()));
            stmt.setDate(2, new java.sql.Date(r.getDataFim().getTimeInMillis()));
            stmt.setInt(3, idCliente);
            stmt.setInt(4, chale.getId());
            stmt.executeUpdate();
            
            generatedKeys = stmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new DAOException("Erro ao pegar a chave gerada pelo banco de dados na Reserva!");
            }
            idReserva = generatedKeys.getInt(1);
            r.setId(idReserva);
            
            stmt.close();
            daoBase.finalizarTransacao();
        }
        catch (SQLException ex) {
            daoBase.descartarTransacao();
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return idReserva;
    }
 
    @Override
    public ArrayList<Reserva> listarPorCliente(int idCliente) throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        ArrayList<Reserva> reservas;
        int idReserva;
        Chale chale;
        Calendar dataInicio, dataFim;
        
        reservas = null;
        try {
            stmt = conn.prepareStatement("SELECT idReserva, dataInicio, dataFim, idChale FROM Reserva WHERE idCliente = ?");
            stmt.setInt(1, idCliente);
        
            rs = stmt.executeQuery();
            
            reservas = new ArrayList<>();
            
            while (rs.next()) {
                idReserva = rs.getInt("idReserva");

                dataInicio = Calendar.getInstance();
                dataFim = Calendar.getInstance();
                dataInicio.setTime(rs.getDate("dataInicio"));
                dataFim.setTime(rs.getDate("dataFim"));
                
                chale = daoBase.getChaleDAO().consultarPorID( rs.getInt("idChale") );
                
                reservas.add(new Reserva(idReserva, chale, dataInicio, dataFim));
            }
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            System.exit(1);
        }
        
        return reservas;
    }
    
}
