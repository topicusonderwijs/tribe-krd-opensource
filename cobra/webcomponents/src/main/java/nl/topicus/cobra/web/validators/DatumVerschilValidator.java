package nl.topicus.cobra.web.validators;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * 
 * 
 * @author vanharen
 */
public class DatumVerschilValidator extends AbstractValidator<Integer>
{

	private static final long serialVersionUID = 1L;

	private Date datum;

	public DatumVerschilValidator(Date datum)
	{
		this.datum = datum;
	}

	@Override
	public void onValidate(IValidatable<Integer> validatable)
	{
		if (datum != null)
		{
			Integer verschilInJaren = validatable.getValue();
			Integer maxVerschilInJaren =
				TimeUtil.getInstance().getDifferenceInYears(new Date(), datum);

			ValidationError error = new ValidationError();
			error
				.setMessage("verschil tussen begin en einddatum mag niet groter zijn dan de leeftijd");

			if (verschilInJaren > maxVerschilInJaren)
				validatable.error(error);
		}
	}
}
