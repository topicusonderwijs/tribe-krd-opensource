package nl.topicus.cobra.web.validators;

import junit.framework.Assert;

import org.apache.wicket.validation.Validatable;
import org.junit.Test;

public class BsnValidatorTest
{
	@Test
	public void testFouteBsn()
	{
		BsnValidator v = new BsnValidator();
		Validatable<Long> validatable = new Validatable<Long>(new Long(1));
		v.validate(validatable);
		Assert.assertEquals(1, validatable.getErrors().size());
	}

	@Test
	public void testGoedeBsn()
	{
		BsnValidator v = new BsnValidator();
		Validatable<Long> validatable = new Validatable<Long>(new Long(182071649));
		v.validate(validatable);
		Assert.assertEquals(0, validatable.getErrors().size());
	}
}
