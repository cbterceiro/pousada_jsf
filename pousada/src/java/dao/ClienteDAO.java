package dao;

import modelo.Cliente;

public interface ClienteDAO extends DAOGenerico<Cliente, Integer> {
    Cliente consultarPorNome(String nome) throws DAOException;
    Cliente consultarPorLoginSenha(String login, String senha) throws DAOException;
    Cliente consultarPorHospedagem(int idHospedagem) throws DAOException;
}
