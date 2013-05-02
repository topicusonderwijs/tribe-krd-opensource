package nl.topicus.cobra.web.validators;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.StringUtil;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

public class CommaSeparatedStringValidator extends AbstractValidator<String>
{
	private static final long serialVersionUID = 1L;

	private AbstractValidator<String> innerValidator;

	public CommaSeparatedStringValidator(AbstractValidator<String> innerValidator)
	{
		this.innerValidator = innerValidator;
	}

	@Override
	protected void onValidate(final IValidatable<String> validatable)
	{
		Asserts.assertNotNull(
			"Bij gebruik van CommaSeparatedStringValidator mag innerValidator niet null zijn",
			innerValidator);

		String valueString = validatable.getValue().toString();

		if (StringUtil.isEmpty(valueString))
		{
			return;
		}

		String[] values = valueString.split(",", -1);

		for (final String value : values)
		{
			innerValidator.validate(new IValidatable<String>()
			{
				@Override
				public void error(IValidationError error)
				{
					innerValidator.error(validatable);
				}

				@Override
				public String getValue()
				{
					return value.trim();
				}

				@Override
				public boolean isValid()
				{
					return validatable.isValid();
				}
			});
		}

	}
}
