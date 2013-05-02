package nl.topicus.eduarte.krd.web.validators;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

public class MinimumDateValidator extends AbstractValidator<Date>
{

	private static final long serialVersionUID = 1L;

	private final Date minimum;

	public MinimumDateValidator(Date minimum)
	{
		this.minimum = minimum;
	}

	@Override
	protected Map<String, Object> variablesMap(IValidatable<Date> validatable)
	{
		final Map<String, Object> map = super.variablesMap(validatable);
		map.put("minimum", new SimpleDateFormat("dd-MM-yyyy").format(minimum));
		return map;
	}

	@Override
	protected String resourceKey()
	{
		return "DateValidator.minimum";
	}

	@Override
	protected void onValidate(IValidatable<Date> validatable)
	{
		Date value = validatable.getValue();
		if (value.before(minimum))
		{
			error(validatable);
		}
	}
}
