package nl.topicus.cobra.web.validators;

import nl.topicus.cobra.util.StringUtil;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.EmailAddressValidator;

/**
 * @author hoeve
 * 
 *         Validator which will only check the email pattern when there is a value or the
 *         function {@link #validateOnNullValue()} returns true (default: false).
 */
public class NullableEmailAddressValidator extends EmailAddressValidator
{
	private static final long serialVersionUID = 1L;

	/** singleton instance */
	private static final NullableEmailAddressValidator INSTANCE =
		new NullableEmailAddressValidator();

	/**
	 * Retrieves the singleton instance of <code>NullableEmailAddressValidator</code>.
	 * 
	 * @return the singleton instance of <code>NullableEmailAddressValidator</code>
	 */
	public static NullableEmailAddressValidator getInstance()
	{
		return INSTANCE;
	}

	@Override
	protected void onValidate(IValidatable<String> validatable)
	{
		if (!StringUtil.isEmpty(validatable.getValue()) || validateOnNullValue())
		{
			super.onValidate(validatable);
		}
	}
}
