package nl.topicus.cobra.web.validators;

import nl.topicus.cobra.util.StringUtil;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Valideert of het IP adres geld is (0.0.0.0 t/m 255.255.255.255)
 */
public class IpAdresValidator extends AbstractValidator<String>
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void onValidate(IValidatable<String> validatable)
	{
		String ipAdres = validatable.getValue();

		if (StringUtil.isEmpty(ipAdres))
		{
			return;
		}

		if (!StringUtil.isIpAdres(ipAdres))
			error(validatable);
	}
}
