package controle;

public class MensagensRedirect {
    public static String redirecionarLogin(){
        return "principal";
    }
    
    public static String redirecionarErro(){
        return "erro.xhtml";
    }
    
    public static String redirecionarMensagemSucesso() {
        return "hospedagemEfetuada";
    }
}
