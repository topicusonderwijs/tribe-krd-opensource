package nl.topicus.cobra.app.converters;

import junit.framework.Assert;
import nl.topicus.cobra.converters.PostcodeConverter;

import org.junit.Test;

public class PostcodeConverterTest
{
	private PostcodeConverter converter = new PostcodeConverter();

	@Test
	public void vanStringNaarObject()
	{
		Assert.assertEquals("1234AB", converter.convertToObject("1234AB", null));
		Assert.assertEquals("1234AB", converter.convertToObject("1234 AB", null));
	}

	@Test
	public void vanObjectNaarString()
	{
		Assert.assertEquals("1234 AB", converter.convertToString("1234AB", null));
		Assert.assertEquals("1234 AB", converter.convertToString("1234 AB", null));
	}
}
