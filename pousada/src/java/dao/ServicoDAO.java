package dao;

import modelo.Servico;

public interface ServicoDAO extends DAOGenerico<Servico, Integer> {
    Servico consultarPorNome(String nome) throws DAOException;
}
