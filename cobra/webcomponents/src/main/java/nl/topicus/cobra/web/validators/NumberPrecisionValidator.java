package nl.topicus.cobra.web.validators;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * 
 * 
 * @author vanharen
 */
public class NumberPrecisionValidator extends AbstractValidator<BigDecimal>
{

	private static final long serialVersionUID = 1L;

	private int scale;

	public NumberPrecisionValidator(int scale)
	{
		this.scale = scale;
	}

	@Override
	protected void onValidate(IValidatable<BigDecimal> validatable)
	{
		BigDecimal inputValue = validatable.getValue();
		int valuescale = inputValue.precision();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("scale", scale);
		if (valuescale > scale)
			error(validatable, params);
	}
}
