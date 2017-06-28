package controle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import modelo.Hospedagem;

/**
 *
 * @author cbterceiro
 */
@ManagedBean(name = "hospedagemEfetuadaBean")
@SessionScoped
public class HospedagemEfetuadaBean {
    private Hospedagem hospedagem;
    private String hospedagemInfo;

    public HospedagemEfetuadaBean() {
    }

    public Hospedagem getHospedagem() {
        return hospedagem;
    }

    public void setHospedagem(Hospedagem hospedagem) {
        this.hospedagem = hospedagem;
        this.setHospedagemInfo(hospedagem.info());
    }

    public String getHospedagemInfo() {
        return hospedagemInfo;
    }

    public void setHospedagemInfo(String hospedagemInfo) {
        this.hospedagemInfo = hospedagemInfo;
    }
}
