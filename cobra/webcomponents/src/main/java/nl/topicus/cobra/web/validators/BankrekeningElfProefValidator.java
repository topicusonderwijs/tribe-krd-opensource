package nl.topicus.cobra.web.validators;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.validation.ElfProef;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Valideert of het rekeningnummer wel aan de 11 proef voldoet.
 */
public class BankrekeningElfProefValidator extends AbstractValidator<String>
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void onValidate(IValidatable<String> validatable)
	{
		String rekening = validatable.getValue();

		if (StringUtil.isEmpty(rekening))
		{
			return;
		}

		if (!StringUtil.isNumeric(rekening))
		{
			error(validatable);
			return;
		}

		// controle van de '11-proef'
		ElfProef proef = new ElfProef();
		if (rekening.length() >= 9
			&& (rekening.length() > 9 || !proef.isGeldigRekeningNummer(rekening)))
		{
			error(validatable);
		}
	}
}
