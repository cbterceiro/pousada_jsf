package dao;

import modelo.TipoPagamento;

public interface TipoPagamentoDAO {
    TipoPagamento consultarPorID(int id) throws DAOException;
    TipoPagamento consultarPorDescricao(String descricao) throws DAOException;
}
