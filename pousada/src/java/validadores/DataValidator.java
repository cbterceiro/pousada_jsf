package validadores;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@FacesValidator(value = "dataValidator")
public class DataValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            sdf.parse(value.toString());
        } catch (ParseException ex) {
            throw new ValidatorException(new FacesMessage("Formato de data inválido! [dd/mm/yyyy]"));
        }
    }

}
