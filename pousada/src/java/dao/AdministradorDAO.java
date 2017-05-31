package dao;

import modelo.Administrador;

public interface AdministradorDAO extends DAOGenerico<Administrador, Integer> {
    Administrador consultarPorNome(String nome) throws DAOException;
    Administrador consultarPorLoginSenha(String login, String senha) throws DAOException;
}
