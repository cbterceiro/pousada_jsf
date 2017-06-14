package controle;

import dao.AdministradorDAO;
import dao.ChaleDAO;
import dao.ClienteDAO;
import dao.DAOException;
import dao.EquipamentoDAO;
import dao.HospedagemDAO;
import dao.ServicoDAO;
import dao.jdbc.DAOBaseJDBC;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import modelo.Administrador;
import modelo.Equipamento;

/**
 *
 * @author caiod
 */
@ManagedBean(name = "gerenciadorDao")
@SessionScoped
public class GerenciadorDAO {
    
    private DAOBaseJDBC daoBase = new DAOBaseJDBC();
    
    private Administrador admLogado;

    public GerenciadorDAO() {
    }
        
    public Administrador getAdmLogado() {
        return admLogado;
    }

    public void setAdmLogado(Administrador admLogado) {
        this.admLogado = admLogado;
    }
    
    public HospedagemDAO getHospedagemDao(){
        return daoBase.getHospedagemDAO();
    }
    
    public AdministradorDAO getAdministradorDAO() {
        return daoBase.getAdministradorDAO();
    }
    
    public ClienteDAO getClienteDao(){
        return daoBase.getClienteDAO();
    }
    
    public ChaleDAO getChaleDao(){
        return daoBase.getChaleDAO();
    }
    
    public ServicoDAO gerServicoDao(){
        return daoBase.getServicoDAO();
    }
    
    public EquipamentoDAO getEquipamentoDao(){
        return daoBase.getEquipamentoDAO();
    }
    
    public void setEquipamentoDAO(Equipamento equipamento){
        try {
            daoBase.getEquipamentoDAO().alterar(equipamento);
        } catch(DAOException e){
            MensagensRedirect.redirecionarErro();
        }
    }
}
