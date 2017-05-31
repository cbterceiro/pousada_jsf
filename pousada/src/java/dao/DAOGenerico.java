package dao;

import java.util.ArrayList;

public interface DAOGenerico<T, PK> {
    PK inserir(T objeto) throws DAOException;
    void alterar(T objeto) throws DAOException;
    T consultarPorID(PK id) throws DAOException;
    void remover(T objeto) throws DAOException;
    ArrayList<T> listarTodos() throws DAOException;
}
