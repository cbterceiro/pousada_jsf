package controle;

import dao.DAOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Chale;
import modelo.Cliente;
import modelo.Hospedagem;
import modelo.ReciboServicoItem;
import modelo.Servico;

/**
 *
 * @author caiod
 */
@ManagedBean(name = "hospedagemBean")
@ViewScoped
public class HospedagemBean {
    private List<Hospedagem> listaHospedagens;
    private List<Cliente> listaClientes;
    private List<Chale> listaChales;
    private List<Servico> listaServicos;
    private List<ReciboServicoItem> listaServicosSelecionados = new ArrayList<ReciboServicoItem>();
    private String servicoSelecionado;
    private String clienteSelecionado;
    private String chaleSelecionado;
    private String qntdAcompSelecionada;
    private String quantidadeServicos;
    private Hospedagem hospedagemNova = new Hospedagem();
        
    @ManagedProperty(value = "#{gerenciadorDao}")
    private GerenciadorDAO gerenciadorDao;  
    
    public HospedagemBean() {}
    
    @PostConstruct
    private void buscarListas(){
        try {
            listaHospedagens = gerenciadorDao.getHospedagemDao().listarTodos();  
            listaClientes = gerenciadorDao.getClienteDao().listarTodos();
            listaChales = gerenciadorDao.getChaleDao().listarTodos();
            listaServicos = gerenciadorDao.gerServicoDao().listarTodos();
            System.out.println(listaClientes);
        } catch (DAOException e) {
            MensagensRedirect.redirecionarErro();
        }
    }

    public List<Hospedagem> getListaHospedagens() {
        return listaHospedagens;
    }

    public void setListaHospedagens(List<Hospedagem> listaHospedagens) {
        this.listaHospedagens = listaHospedagens;
    }

    public GerenciadorDAO getGerenciadorDao() {
        return gerenciadorDao;
    }

    public void setGerenciadorDao(GerenciadorDAO gerenciadorDao) {
        this.gerenciadorDao = gerenciadorDao;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public List<Chale> getListaChales() {
        return listaChales;
    }

    public void setListaChales(List<Chale> listaChales) {
        this.listaChales = listaChales;
    }

    public Hospedagem getHospedagemNova() {
        return hospedagemNova;
    }

    public void setHospedagemNova(Hospedagem hospedagemNova) {
        this.hospedagemNova = hospedagemNova;
    }

    public List<Servico> getListaServicos() {
        return listaServicos;
    }

    public void setListaServicos(List<Servico> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public String getQuantidadeServicos() {
        return quantidadeServicos;
    }

    public void setQuantidadeServicos(String quantidadeServicos) {
        this.quantidadeServicos = quantidadeServicos;
    }

    public List<ReciboServicoItem> getListaServicosSelecionados() {
        return listaServicosSelecionados;
    }

    public void setListaServicosSelecionados(List<ReciboServicoItem> listaServicosSelecionados) {
        this.listaServicosSelecionados = listaServicosSelecionados;
    }

    public String getServicoSelecionado() {
        return servicoSelecionado;
    }

    public void setServicoSelecionado(String servicoSelecionado) {
        this.servicoSelecionado = servicoSelecionado;
    }

    public String getClienteSelecionado() {
        return clienteSelecionado;
    }

    public void setClienteSelecionado(String clienteSelecionado) {
        this.clienteSelecionado = clienteSelecionado;
    }

    public String getChaleSelecionado() {
        return chaleSelecionado;
    }

    public void setChaleSelecionado(String chaleSelecionado) {
        this.chaleSelecionado = chaleSelecionado;
    }

    public String getQntdAcompSelecionada() {
        return qntdAcompSelecionada;
    }

    public void setQntdAcompSelecionada(String qntdAcompSelecionada) {
        this.qntdAcompSelecionada = qntdAcompSelecionada;
    }
    
    
    
    public void adicionarServico(){
        Servico serv = null;
        
        for(Servico s : this.listaServicos){
            if(s.getNome().equals(this.servicoSelecionado)){
                serv = s;
                break;
            }
        }
        
        this.listaServicosSelecionados.add(new ReciboServicoItem(Integer.parseInt(this.quantidadeServicos), serv));
    }
    
    public void removerServico(){
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        
        Integer idServico = Integer.parseInt(params.get("servico"));
        
        for(ReciboServicoItem s : this.listaServicosSelecionados){
            if(s.getId() == idServico){
                this.listaServicosSelecionados.remove(s);
                break;
            }
        }
    }
    
    public void efetuarHospedagem(){
        
    }

}
