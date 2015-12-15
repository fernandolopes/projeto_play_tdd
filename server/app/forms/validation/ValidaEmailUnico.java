package forms.validation;

import models.Usuario;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.libs.F;

/**
 * Created by fernando on 14/12/15.
 */
public class ValidaEmailUnico extends Constraints.Validator<String> {

    @Override
    public boolean isValid(String s) {
        return Usuario.isEmailUnique(s);
    }

    @Override
    public F.Tuple<String, Object[]> getErrorMessageKey() {
        return F.Tuple(Messages.get("error.uniqueEmail"),new Object[]{});
    }

}