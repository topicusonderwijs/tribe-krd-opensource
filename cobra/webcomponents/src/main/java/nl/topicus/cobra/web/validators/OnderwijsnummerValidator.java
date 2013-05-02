package nl.topicus.cobra.web.validators;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.validation.ElfProef;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Valideert of het onderwijsnummer wel aan de 11 proef voldoet.
 */
public class OnderwijsnummerValidator extends AbstractValidator<String>
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void onValidate(IValidatable<String> validatable)
	{
		String onderwijsnummer = validatable.getValue();
		if (StringUtil.isEmpty(onderwijsnummer))
		{
			return;
		}

		// controle van de '11-proef'
		ElfProef proef = new ElfProef();
		if (onderwijsnummer.length() != 9 || !proef.isGeldigOnderwijsNummer(onderwijsnummer))
		{
			error(validatable);
		}
	}
}
