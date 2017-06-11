package listener;

import controle.GerenciadorDAO;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import modelo.Administrador;

/**
 *
 * @author caiod
 */
public class AutorizaListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext contexto = event.getFacesContext();
        String paginaAtual = contexto.getViewRoot().getViewId();
        boolean isLoginPage = paginaAtual.lastIndexOf("index.xhtml") > -1;
        
        GerenciadorDAO gerenciadorDao = (GerenciadorDAO) contexto.getApplication().evaluateExpressionGet(contexto, "#{gerenciadorDao}", GerenciadorDAO.class);
        Administrador adm = gerenciadorDao.getAdmLogado();
                
        if (!isLoginPage && adm == null) {
            NavigationHandler nh = contexto.getApplication().getNavigationHandler();
            nh.handleNavigation(contexto, null, "index.xhtml");
        } else if (isLoginPage && adm != null) {
            NavigationHandler nh = contexto.getApplication().getNavigationHandler();
            nh.handleNavigation(contexto, null, "principal.xhtml");
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
    
    
}
