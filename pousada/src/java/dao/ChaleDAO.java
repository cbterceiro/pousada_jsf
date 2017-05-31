package dao;

import modelo.Chale;
import java.util.ArrayList;

public interface ChaleDAO extends DAOGenerico<Chale, Integer> {
    Chale consultarPorNumero(int numero) throws DAOException;
    ArrayList<Chale> listarDesocupados() throws DAOException;
}
