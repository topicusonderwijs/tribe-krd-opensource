package nl.topicus.cobra.util;

import org.junit.Test;

public class AssertsTest
{
	/**
	 * Test method for 'nl.topicus.iridium.validation.Asserts.assertNotNull(String,
	 * Object)'
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAssertNotNullMetNull()
	{
		Asserts.assertNotNull("foo", null);
	}

	@Test
	public void testAssertNotNullMetNotNull()
	{
		Asserts.assertNotNull("foo", new Object());
	}

	/**
	 * Test method for 'nl.topicus.iridium.validation.Asserts.assertNotNull(String,
	 * Object)'
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAssertNotEmptyMetEmpty()
	{
		Asserts.assertNotEmpty("foo", "");
	}

	@Test
	public void testAssertNotEmptyMetNotEmpty()
	{
		Asserts.assertNotEmpty("foo", "123");
	}

	/**
	 * Test method for 'nl.topicus.iridium.validation.Asserts.assertMatchesRegExp(String,
	 * String, String)'
	 */
	@Test
	public void testAssertMatchesRegExp()
	{
		Asserts.assertMatchesRegExp("postcode", "1234 Aa", "[1-9][0-9]{3} ?[a-zA-Z]{2}");
		Asserts.assertMatchesRegExp("postcode", "1234Aa", "[1-9][0-9]{3} ?[a-zA-Z]{2}");
		Asserts.assertMatchesRegExp("postcode", "   1234 Aa   ", "[1-9][0-9]{3} ?[a-zA-Z]{2}");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAssertMatchesRegExpFail1()
	{
		Asserts.assertMatchesRegExp("foo", null, "[1-9][0-9]{3} ?[a-zA-Z]{2}");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAssertMatchesRegExpFail2()
	{
		Asserts.assertMatchesRegExp("foo", "", "[1-9][0-9]{3} ?[a-zA-Z]{2}");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAssertMatchesRegExpFail3()
	{
		Asserts.assertMatchesRegExp("foo", "abc", "[1-9][0-9]{3} ?[a-zA-Z]{2}");
	}
}
