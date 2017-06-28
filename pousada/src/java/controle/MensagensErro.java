package controle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MensagensErro {
    public static void criarMensagemErroLogin(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Login ou Senha inválidos!"));
    }
    
    public static void criarMensagemBuscaEquipamento(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ops!", "Produto não encontrado!"));
    }
    
    public static void criarMensagemDatasInvalidas() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "A data inicial deve ser anterior à data final!"));
    }
}
