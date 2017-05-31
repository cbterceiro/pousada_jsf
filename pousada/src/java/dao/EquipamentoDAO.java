package dao;

import java.util.ArrayList;
import modelo.Equipamento;

public interface EquipamentoDAO extends DAOGenerico<Equipamento, Integer> {
    Equipamento consultarPorDescricao(String descricao) throws DAOException;
    ArrayList<Equipamento> listarPorChale(Integer idChale) throws DAOException;
}
