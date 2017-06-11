package controle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MensagensErro {
    public static void criarMensagemErroLogin(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Login ou Senha inválidos!"));
    }
}
