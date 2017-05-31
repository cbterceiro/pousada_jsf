package dao.jdbc;

import dao.HospedagemDAO;
import dao.ChaleDAO;
import dao.ClienteDAO;
import dao.ServicoDAO;
import dao.TipoPagamentoDAO;
import dao.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

import modelo.Hospedagem;
import modelo.Chale;
import modelo.ReciboServico;
import modelo.ParcelaPagamento;
import modelo.excecoes.PagamentoOverflowException;

public class HospedagemDAOJDBC implements HospedagemDAO {
    private DAOBaseJDBC daoBase;
    private Connection conn;
    
    public HospedagemDAOJDBC(DAOBaseJDBC daoBase) {
        this.daoBase = daoBase;
        this.conn = daoBase.getConnection();
    }
    
    @Override
    public Integer inserir(Hospedagem h) throws DAOException {
        PreparedStatement stmt;
        ResultSet generatedKeys;
        int idHospedagem;
        Chale chale;
        ChaleDAO chaleDao;
        
        idHospedagem = -1;
        try {
            daoBase.iniciarTransacao();
            
            chale = h.getChale();
            chale.setEstaOcupado(true);
            daoBase.getChaleDAO().alterar(chale);
            
            stmt = conn.prepareStatement("INSERT INTO Hospedagem (quantidadeAcompanhantes, dataInicio, dataFim, valor, pago, idChale, idCliente) VALUES (?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, h.getQntdAcomp());
            stmt.setDate(2, new java.sql.Date(h.getDataInicio().getTimeInMillis()));
            stmt.setDate(3, new java.sql.Date(h.getDataFim().getTimeInMillis()));
            stmt.setDouble(4, h.getValorTotal());
            stmt.setBoolean(5, false);
            stmt.setInt(6, chale.getId());
            stmt.setInt(7, h.getCliente().getId());
            stmt.executeUpdate();
            
            generatedKeys = stmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new DAOException("Erro ao pegar a chave gerada pelo banco de dados na Hospedagem!");
            }
            idHospedagem = generatedKeys.getInt(1);
            h.setId(idHospedagem);
            
            salvarRecibos(h);
            
            stmt.close();
            daoBase.finalizarTransacao();
        }
        catch (SQLException ex) {
            daoBase.descartarTransacao();
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return idHospedagem;
    }
    
    @Override
    public Hospedagem consultarPorID(Integer id) throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        Hospedagem hospedagem;
        
        hospedagem = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM Hospedagem WHERE idHospedagem = ?");
            stmt.setInt(1, id);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                hospedagem = criarHospedagem(rs);
            }
            
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return hospedagem;
    }

    @Override
    public void alterar(Hospedagem objeto) throws DAOException {

    }

    @Override
    public void remover(Hospedagem objeto) throws DAOException {
        
    }

    @Override
    public ArrayList<Hospedagem> listarTodos() throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        ArrayList<Hospedagem> hospedagens;
        
        hospedagens = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM Hospedagem");
            rs = stmt.executeQuery();
            
            hospedagens = new ArrayList<>();
            while (rs.next()) {
                hospedagens.add(criarHospedagem(rs));
            }
            
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return hospedagens;
    }

    @Override
    public ArrayList<Hospedagem> listarPorCliente(int idCliente) throws DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        ArrayList<Hospedagem> hospedagens;
        
        hospedagens = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM Hospedagem WHERE idCliente = ?");
            stmt.setInt(1, idCliente);
            rs = stmt.executeQuery();
            
            hospedagens = new ArrayList<>();
            while (rs.next()) {
                hospedagens.add(criarHospedagem(rs));
            }
            
            stmt.close();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
        
        return hospedagens;
    }

    @Override
    public void finalizar(Hospedagem h) throws DAOException {
        PreparedStatement stmt;
        Chale chale;
       
        try {
            daoBase.iniciarTransacao();
            
            chale = h.getChale();
            chale.setEstaOcupado(false);
            daoBase.getChaleDAO().alterar(chale);
            
            salvarParcelas(h);
            
            stmt = conn.prepareStatement("UPDATE Hospedagem SET pago = true WHERE idHospedagem = ?");
            stmt.setInt(1, h.getId());
            stmt.executeUpdate();
            
            daoBase.finalizarTransacao();
        }
        catch (SQLException ex) {
            daoBase.descartarTransacao();
            System.out.println("Erro no banco de dados!");
            System.out.println(ex);
            throw new DAOException(ex);
        }
    }
    
    public Hospedagem criarHospedagem(ResultSet rs) throws SQLException, DAOException {
        Hospedagem h;
        Calendar dataInicio, dataFim;
        ChaleDAO chaleDao;
        ClienteDAO clienteDao;        
        
        h = new Hospedagem();
        h.setId(rs.getInt("idHospedagem"));
        h.setQntdAcomp(rs.getInt("quantidadeAcompanhantes"));
        
        dataInicio = Calendar.getInstance();
        dataInicio.setTime(rs.getDate("dataInicio"));
        h.setDataInicio(dataInicio);
        
        dataFim = Calendar.getInstance();
        dataFim.setTime(rs.getDate("dataFim"));
        h.setDataFim(dataFim);
        
        chaleDao = daoBase.getChaleDAO();
        h.setChale(chaleDao.consultarPorID(rs.getInt("idChale")));
        
        clienteDao = daoBase.getClienteDAO();
        h.setCliente(clienteDao.consultarPorHospedagem(h.getId()));
        
        h.setStatus(rs.getBoolean("pago") ? "Finalizado" : "Hospedado");
        
        obterRecibos(h);
        
        h.calcValorTotal();
        
        if ("Finalizado".equals(h.getStatus())) {
            obterParcelas(h);
        }

        return h;
    }
        
    public void salvarRecibos(Hospedagem h) throws SQLException  {
        PreparedStatement stmt;
        ArrayList<ReciboServico> recibos;
        
        stmt = conn.prepareStatement("INSERT INTO ReciboServico (idHospedagem, idServico, quantidade, valor, dataHora) VALUES (?, ?, ?, ?, NOW())");
        stmt.setInt(1, h.getId());
        
        recibos = h.getRecibos();
        for (ReciboServico rs : recibos) {
            stmt.setInt(2, rs.getServico().getId());
            stmt.setInt(3, rs.getQntd());
            stmt.setDouble(4, rs.getValorTotal());
            stmt.executeUpdate();
        }
        stmt.close();
    }
    
    public void salvarParcelas(Hospedagem h) throws SQLException {
        PreparedStatement stmt;
        ArrayList<ParcelaPagamento> parcelas;
        
        stmt = conn.prepareStatement("INSERT INTO ParcelaPagamento (valor, idTipoPagamento, idHospedagem) VALUES (?, ?, ?)");
        stmt.setInt(3, h.getId());
        
        parcelas = h.getPagamento().getParcelas();
        for (ParcelaPagamento p : parcelas) {
            stmt.setDouble(1, p.getValor());
            stmt.setInt(2, p.getTipoPagamento().getId());
            stmt.executeUpdate();
        }
        stmt.close();
    }
    
    public void obterRecibos(Hospedagem h) throws SQLException, DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        ServicoDAO servicoDao;
        
        stmt = conn.prepareStatement("SELECT * FROM ReciboServico WHERE idHospedagem = ?");
        stmt.setInt(1, h.getId());
        
        servicoDao = daoBase.getServicoDAO();
        
        rs = stmt.executeQuery();
        while (rs.next()) {
            h.addRecibo(servicoDao.consultarPorID(rs.getInt("idServico")), rs.getInt("quantidade"));
        }
        stmt.close();
    }
    
    public void obterParcelas(Hospedagem h) throws SQLException, DAOException {
        PreparedStatement stmt;
        ResultSet rs;
        TipoPagamentoDAO tpDao;
        
        stmt = conn.prepareStatement("SELECT * FROM ParcelaPagamento WHERE idHospedagem = ?");
        stmt.setInt(1, h.getId());
        
        tpDao = daoBase.getTipoPagamentoDAO();
        
        rs = stmt.executeQuery();
        while (rs.next()) {
            try {
                h.addParcelaPagamento(tpDao.consultarPorID(rs.getInt("idTipoPagamento")), rs.getDouble("valor"));
            }
            catch (PagamentoOverflowException ex) {
                System.out.println("Algo de errado não está certo.");
                System.out.println(ex);
                System.exit(1);
            }
        }
        
        stmt.close();
    }

}
