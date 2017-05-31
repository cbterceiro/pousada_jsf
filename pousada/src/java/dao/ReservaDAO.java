package dao;

import modelo.Reserva;
import java.util.ArrayList;

public interface ReservaDAO {
    public Integer inserir(Reserva r, int idCliente) throws DAOException;
    ArrayList<Reserva> listarPorCliente(int idCliente) throws DAOException;
}
