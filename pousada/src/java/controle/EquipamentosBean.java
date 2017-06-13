
package controle;

import dao.DAOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import modelo.Equipamento;

@ManagedBean(name = "equipamentosBean")
@SessionScoped
public class EquipamentosBean {
    private List<Equipamento> listaEquipamentos;
    private Equipamento equipamentoNovo = new Equipamento();
    
    @ManagedProperty(value = "#{gerenciadorDao}")
    private GerenciadorDAO gerenciadorDao;
    
    public EquipamentosBean(){
        
    }
    
    @PostConstruct
    private void buscarListaEquipamentos(){
        try {
            listaEquipamentos = gerenciadorDao.getEquipamentoDao().listarTodos();            
        } catch (DAOException e){
            MensagensRedirect.redirecionarErro();
        }
    }

    public List<Equipamento> getListaEquipamentos() {
        return listaEquipamentos;
    }

    public void setListaEquipamentos(List<Equipamento> listaEquipamentos) {
        this.listaEquipamentos = listaEquipamentos;
    }

    public GerenciadorDAO getGerenciadorDao() {
        return gerenciadorDao;
    }

    public void setGerenciadorDao(GerenciadorDAO gerenciadorDao) {
        this.gerenciadorDao = gerenciadorDao;
    }

    public Equipamento getEquipamentoNovo() {
        return equipamentoNovo;
    }

    public void setEquipamentoNovo(Equipamento equipamentoNovo) {
        this.equipamentoNovo = equipamentoNovo;
    }
    
    public void incluirEquipamento(){
        try {            
            gerenciadorDao.getEquipamentoDao().inserir(equipamentoNovo);
            listaEquipamentos = gerenciadorDao.getEquipamentoDao().listarTodos();
        } catch (DAOException e){
             MensagensRedirect.redirecionarErro();
        }    
    }
    
    public void buscarEquipamento(){
        Equipamento equip = null;
        
        try {
            equip = gerenciadorDao.getEquipamentoDao().consultarPorDescricao(this.equipamentoNovo.getDescricao());
        } catch(DAOException e){
            MensagensRedirect.redirecionarErro();
        }
        
        if(equip == null){
            MensagensErro.criarMensagemBuscaEquipamento();
        }
        else{
            
        }
    }
    
}
