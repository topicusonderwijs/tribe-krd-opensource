package nl.topicus.cobra.web.validators;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.validation.ElfProef;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Valideert of het BSN wel aan de 11 proef voor BSN's.
 */
public class BsnValidator extends AbstractValidator<Long>
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void onValidate(IValidatable<Long> validatable)
	{
		String sofinummer = validatable.getValue().toString();

		if (StringUtil.isEmpty(sofinummer))
		{
			return;
		}

		// een 0 voorop de string plaatsen indien sofinr 8 lang is
		if (sofinummer.length() == 8)
		{
			sofinummer = "0".concat(sofinummer);
		}

		// sofinummer.length() != 2 &&
		if (sofinummer.length() != 9)
		{
			error(validatable);
			return;
		}
		// controle van de '11-proef'
		ElfProef proef = new ElfProef();
		if (!proef.isGeldigSofiNummer(sofinummer))
		{
			error(validatable);
		}
	}
}