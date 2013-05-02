package nl.topicus.cobra.web.validators;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

public class DatumInVerledenOfVandaagValidator extends AbstractValidator<Date>
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void onValidate(IValidatable<Date> validatable)
	{
		Date nu = TimeUtil.getInstance().currentDate();
		Date datum = validatable.getValue();

		if (datum != null)
		{
			if (datum.after(nu))
			{
				error(validatable, "DatumInVerledenOfVandaagValidator.error");
			}

		}

	}
}
