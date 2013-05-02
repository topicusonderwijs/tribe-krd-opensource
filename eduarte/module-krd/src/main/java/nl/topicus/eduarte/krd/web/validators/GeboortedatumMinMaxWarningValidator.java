package nl.topicus.eduarte.krd.web.validators;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

public class GeboortedatumMinMaxWarningValidator extends AbstractValidator<Date>
{
	private static final long serialVersionUID = 1L;

	private static final int MAX_LEEFTIJD = 99;

	private static final int MIN_LEEFTIJD = 10;

	private SecurePage page;

	public GeboortedatumMinMaxWarningValidator(SecurePage page)
	{
		this.page = page;
	}

	@Override
	protected void onValidate(IValidatable<Date> validatable)
	{
		Date nu = TimeUtil.getInstance().currentDate();
		Date datum = validatable.getValue();

		int verschil = TimeUtil.getInstance().getDifferenceInYears(nu, datum);

		if (datum != null)
		{
			if (verschil > MAX_LEEFTIJD)
			{
				page.warn("Let op: met ingevoerde geboortedatum is persoon ouder dan "
					+ MAX_LEEFTIJD + " jaar.");
			}

			if (verschil < MIN_LEEFTIJD)
			{
				page.warn("Let op: met ingevoerde geboortedatum is persoon jonger dan "
					+ MIN_LEEFTIJD + " jaar.");
			}
		}
	}
}
