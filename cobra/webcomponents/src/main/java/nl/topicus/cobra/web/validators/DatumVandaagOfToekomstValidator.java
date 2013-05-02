package nl.topicus.cobra.web.validators;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.text.DatumField;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Validator welke controleert of de ingevoerde datum vandaag is of in de toekomst ligt.
 * 
 * @author hoeve
 */
public class DatumVandaagOfToekomstValidator extends AbstractValidator<Date>
{
	private static final long serialVersionUID = 1L;

	public DatumVandaagOfToekomstValidator()
	{

	}

	@SuppressWarnings("unused")
	public DatumVandaagOfToekomstValidator(DatumField component)
	{

	}

	@Override
	protected void onValidate(IValidatable<Date> validatable)
	{
		Date nu = TimeUtil.getInstance().currentDate();
		Date datum = validatable.getValue();

		if (datum != null)
		{
			if (datum.before(nu))
			{
				error(validatable, "DatumVandaagOfToekomstValidator.error");
			}

		}

	}
}
