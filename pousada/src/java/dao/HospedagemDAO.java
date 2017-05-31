package dao;

import modelo.Hospedagem;
import java.util.ArrayList;

public interface HospedagemDAO extends DAOGenerico<Hospedagem, Integer> {
    void finalizar(Hospedagem h) throws DAOException;
    ArrayList<Hospedagem> listarPorCliente(int idCliente) throws DAOException;
}
