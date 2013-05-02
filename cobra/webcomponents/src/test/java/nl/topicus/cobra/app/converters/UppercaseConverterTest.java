package nl.topicus.cobra.app.converters;

import junit.framework.Assert;
import nl.topicus.cobra.converters.UppercaseConverter;

import org.junit.Test;

public class UppercaseConverterTest
{
	private UppercaseConverter converter = new UppercaseConverter();

	@Test
	public void vanStringNaarObject()
	{
		Assert.assertEquals("ABCDEF12", converter.convertToObject("abcdEF12", null));
	}

	@Test
	public void vanObjectNaarString()
	{
		Assert.assertEquals("ABCDEF12", converter.convertToString("abcdEF12", null));
	}
}
