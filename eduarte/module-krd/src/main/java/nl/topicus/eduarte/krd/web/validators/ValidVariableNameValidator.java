package nl.topicus.eduarte.krd.web.validators;

import net.sourceforge.jeval.Evaluator;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Valideert of een tekstwaarde een geldige variabelenaam is.
 */
public class ValidVariableNameValidator extends AbstractValidator<String>
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void onValidate(IValidatable<String> validatable)
	{
		String waarde = validatable.getValue().toString();
		Evaluator eval = new Evaluator();
		try
		{
			eval.isValidName(waarde);
		}
		catch (IllegalArgumentException e)
		{
			error(validatable);
		}
	}
}
