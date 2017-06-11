/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import dao.AdministradorDAO;
import dao.jdbc.DAOBaseJDBC;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author caiod
 */
@ManagedBean(name = "gerenciadorDao")
@ApplicationScoped
public class GerenciadorDAO {
    
    private DAOBaseJDBC daoBase = new DAOBaseJDBC();

    public GerenciadorDAO() {
    }
        
    public AdministradorDAO getAdministradorDAO() {
        return daoBase.getAdministradorDAO();
    }
    
    
}
