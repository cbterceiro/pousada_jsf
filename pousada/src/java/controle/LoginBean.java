package controle;

import dao.DAOException;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import modelo.Administrador;

/**
 *
 * @author caiod
 */
@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean {
    private String login;
    private String senha;
    
    @ManagedProperty(value = "#{gerenciadorDao}")
    private GerenciadorDAO gerenciadorDao;    
    

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public GerenciadorDAO getGerenciadorDao() {
        return gerenciadorDao;
    }

    public void setGerenciadorDao(GerenciadorDAO gerenciadorDao) {
        this.gerenciadorDao = gerenciadorDao;
    }
    
    public String logar() {
        try {
            Administrador adm = gerenciadorDao.getAdministradorDAO().consultarPorLoginSenha(login, senha);
            if (adm == null) {
                MensagensErro.criarMensagemErroLogin();
            } else {
                gerenciadorDao.setAdmLogado(adm);
                return MensagensRedirect.redirecionarLogin();
            }
        } catch (DAOException ex) {
            return MensagensRedirect.redirecionarErro();
        }
        return null;
    }
    
    public String logout() {
        gerenciadorDao.setAdmLogado(null);
        return "index.xhtml";
    }
}
