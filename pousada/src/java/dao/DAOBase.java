package dao;

public interface DAOBase {
    void iniciarTransacao() throws DAOException;
    void finalizarTransacao() throws DAOException;
    void descartarTransacao() throws DAOException;
    AdministradorDAO getAdministradorDAO();
    ChaleDAO         getChaleDAO();
    ClienteDAO       getClienteDAO();
    EquipamentoDAO   getEquipamentoDAO();
    HospedagemDAO    getHospedagemDAO();
    ReservaDAO       getReservaDAO();
    ServicoDAO       getServicoDAO();
    TipoPagamentoDAO getTipoPagamentoDAO();
}
