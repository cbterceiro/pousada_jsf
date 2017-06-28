package validadores;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author caiod
 */
@FacesValidator(value = "numeroPositivoValidator")
public class NumeroPositivoValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value.toString() != null && !value.toString().isEmpty()) {
            Integer i = null;
            try {
                i = Integer.parseInt(value.toString());
                if (i < 0) {
                    throw new ValidatorException(new FacesMessage("Número deve ser positivo!"));
                }
            } catch (NumberFormatException ex) {
                throw new ValidatorException(new FacesMessage("Formato de número inválido!"));
            }
        }
    }
}
