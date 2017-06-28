package controle;

import dao.DAOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private String qntdAcompSelecionada = "0";
    private String quantidadeServicos;
    private String dataInicioString;
    private String dataFimString;
    
    @ManagedProperty(value = "#{hospedagemEfetuadaBean}")
    private HospedagemEfetuadaBean hospedagemEfetuadaBean;
    
    @ManagedProperty(value = "#{gerenciadorDao}")
    private GerenciadorDAO gerenciadorDao;  
    
    public HospedagemBean() {}
    
    @PostConstruct
    private void buscarListas(){
        this.setDatasIniciais();
        try {
            listaHospedagens = gerenciadorDao.getHospedagemDao().listarTodos();  
            listaClientes = gerenciadorDao.getClienteDao().listarTodos();
            listaChales = gerenciadorDao.getChaleDao().listarTodos();
            listaServicos = gerenciadorDao.gerServicoDao().listarTodos();
            
        } catch (DAOException e) {
            MensagensRedirect.redirecionarErro();
        }
    }
    
    private void setDatasIniciais() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar hoje = Calendar.getInstance();
        Calendar amanha = Calendar.getInstance();
        amanha.add(Calendar.DAY_OF_MONTH, 1);
        this.dataInicioString = sdf.format(hoje.getTime());
        this.dataFimString = sdf.format(amanha.getTime());
    }

    public void adicionarServico(){
        Servico serv = this.servicoPorNome(this.servicoSelecionado);
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
    
    public String efetuarHospedagem() {
        Hospedagem h = new Hospedagem();
        h.setCliente(this.clientePorNome(this.clienteSelecionado));
        h.setChale(this.chalePorNome(this.chaleSelecionado));
        h.setQntdAcomp(Integer.parseInt(this.qntdAcompSelecionada));
        h.setDataInicioString(this.dataInicioString);
        h.setDataFimString(this.dataFimString);
                
        try {
            h.checkDatas();
            h.setStatus("Hospedado");
        
            for (ReciboServicoItem s: this.listaServicosSelecionados) {
                h.addRecibo(this.servicoPorNome(s.getDescricao()), s.getQuantidade());
            }

            h.calcValorTotal();
            
            this.gerenciadorDao.getHospedagemDao().inserir(h);
            this.hospedagemEfetuadaBean.setHospedagem(h);
        }
        catch (IllegalArgumentException ex) {
            MensagensErro.criarMensagemDatasInvalidas();
        } catch (DAOException ex) {
            return MensagensRedirect.redirecionarErro();
        }
        
        return MensagensRedirect.redirecionarMensagemSucesso();
    }
    
    private Cliente clientePorNome(String nomeCliente) {
        if (nomeCliente == null)
            return null;
        
        for (Cliente c : this.listaClientes) {
            if (nomeCliente.equals(c.getNome()))
                return c;
        }
        return null;
    }
    
    private Servico servicoPorNome(String nomeServico) {
        if (nomeServico == null)
            return null;
        
        for (Servico s : this.listaServicos) {
            if (nomeServico.equals(s.getNome()))
                return s;
        }
        return null;
    }
    
    private Chale chalePorNome(String nomeChale) {
        if (nomeChale == null)
            return null;
        
        for (Chale c : this.listaChales) {
            if (nomeChale.equals(c.toString()))
                return c;
        }
        return null;
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

    public String getDataInicioString() {
        return dataInicioString;
    }

    public void setDataInicioString(String dataInicioString) {
        this.dataInicioString = dataInicioString;
    }

    public String getDataFimString() {
        return dataFimString;
    }

    public void setDataFimString(String dataFimString) {
        this.dataFimString = dataFimString;
    }

    public HospedagemEfetuadaBean getHospedagemEfetuadaBean() {
        return hospedagemEfetuadaBean;
    }

    public void setHospedagemEfetuadaBean(HospedagemEfetuadaBean hospedagemEfetuadaBean) {
        this.hospedagemEfetuadaBean = hospedagemEfetuadaBean;
    }
}
