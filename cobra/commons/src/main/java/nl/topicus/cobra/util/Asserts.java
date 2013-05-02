package nl.topicus.cobra.util;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Standaard asserts voor het controleren van parameters en waarden in de webapplicatie.
 * Gebruik deze asserts in plaats van de standaard manier van if then else constructies
 * aan het begin van een methode.
 */
public final class Asserts
{
	private static Logger log = LoggerFactory.getLogger(Asserts.class);

	/**
	 * Controleert of de waarde niet <code>null</code> is.
	 * 
	 * @param parameter
	 *            de naam van de parameter.
	 * @param waarde
	 *            de waarde van de parameter.
	 * @throws IllegalArgumentException
	 *             wanneer de parameter waarde <code>null</code> is.
	 */
	public static void assertNotNull(final String parameter, final Object waarde)
	{
		if (log.isDebugEnabled())
		{
			// trim the value to max 16 characters
			String waardeText = String.valueOf(waarde);
			waardeText = waardeText.substring(0, Math.min(waardeText.length(), 16));
			log.debug("Controleer dat " + parameter + " niet null is, huidige waarde: '"
				+ waardeText + "'");
		}
		if (waarde == null)
		{
			IllegalArgumentException exception =
				new IllegalArgumentException("Parameter " + parameter + " is null");
			throw exception;
		}
	}

	/**
	 * Controleert of de waarde <code>null</code> is.
	 * 
	 * @param parameter
	 *            de naam van de parameter.
	 * @param waarde
	 *            de waarde van de parameter.
	 * @throws IllegalArgumentException
	 *             wanneer de parameter waarde niet <code>null</code> is.
	 */
	public static void assertNull(final String parameter, final Object waarde)
	{
		if (log.isDebugEnabled())
		{
			// trim the value to max 16 characters
			String waardeText = String.valueOf(waarde);
			waardeText = waardeText.substring(0, Math.min(waardeText.length(), 16));
			log.debug("Controleer dat " + parameter + " null is, huidige waarde: '" + waardeText
				+ "'");
		}
		if (waarde != null)
		{
			IllegalArgumentException exception =
				new IllegalArgumentException("Parameter " + parameter + " is niet null");
			throw exception;
		}
	}

	/**
	 * Controleert dat de gegeven waarde niet langer is dan de gegeven lengte
	 * 
	 * @param parameter
	 * @param waarde
	 * @param length
	 * @throws IllegalArgumentException
	 *             wanneer de parameter waarde <code>null</code> is of langer dan de
	 *             gegeven lengte.
	 */
	public static void assertMaxLength(String parameter, String waarde, int length)
	{
		if (waarde != null && waarde.length() > length)
		{
			String message =
				"Parameter " + parameter + " '" + waarde + "' heeft " + waarde.length()
					+ " karakters, maximaal toegestaan: " + length + " karakters";
			IllegalArgumentException exception = new IllegalArgumentException(message);
			throw exception;
		}
	}

	/**
	 * @param parameter
	 * @param waarde
	 */
	public static void assertGreaterThanZero(final String parameter, long waarde)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Controleer dat " + parameter + " groter dan nul is, huidige waarde: "
				+ waarde);
		}
		if (waarde <= 0)
		{
			throw new IllegalArgumentException("Parameter " + parameter
				+ " is not greater than zero");
		}
	}

	/**
	 * @param parameter
	 * @param waarde
	 */
	public static void assertGreaterThanZero(final String parameter, int waarde)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Controleer dat " + parameter + " groter dan nul is, huidige waarde: "
				+ waarde);
		}
		if (waarde <= 0)
		{
			throw new IllegalArgumentException("Parameter " + parameter
				+ " is not greater than zero");
		}
	}

	/**
	 * Controleert of de waarde niet <code>null</code> of leeg is.
	 * 
	 * @param parameter
	 *            de naam van de parameter.
	 * @param waarde
	 *            de waarde van de parameter.
	 * @throws IllegalArgumentException
	 *             wanneer de parameter waarde <code>null</code> of leeg is.
	 */
	public static void assertNotEmpty(String parameter, Object waarde)
	{
		assertNotNull(parameter, waarde);
		if ("".equals(waarde.toString().trim()))
		{
			String bericht = "Parameter " + parameter + " is leeg";
			throw new IllegalArgumentException(bericht);
		}
	}

	/**
	 * Controleert of de waarde niet <code>null</code> of leeg is.
	 * 
	 * @param parameter
	 * @param list
	 * @throws IllegalArgumentException
	 *             wanneer de parameter waarde <code>null</code> of leeg is.
	 */
	public static void assertNotEmpty(String parameter, Collection< ? > list)
	{
		assertNotNull(parameter, list);
		if (list.isEmpty())
		{
			throw new IllegalArgumentException("Parameter " + parameter + " is leeg");
		}
	}

	/**
	 * Controleert of de waarde niet null is, maar dat de collectie wel leeg is.
	 * 
	 * @param parameter
	 * @param list
	 * @throws IllegalArgumentException
	 *             wanneer de parameter waarde <code>null</code> of niet leeg is.
	 */
	public static void assertEmptyCollection(String parameter, Collection< ? > list)
	{
		assertNotNull(parameter, list);
		if (!list.isEmpty())
		{
			throw new IllegalArgumentException("Parameter " + parameter + " is niet leeg");
		}
	}

	/**
	 * Controleert dat de gegeven waarde overeenkomt met de verwachte waarde.
	 * 
	 * @param parameter
	 * @param actual
	 * @param expected
	 * @throws IllegalArgumentException
	 *             wanneer de waarde niet gelijk is aan de verwachte waarde
	 */
	public static void assertEquals(String parameter, Object actual, Object expected)
	{
		if (actual == expected)
			return;
		if (expected != null && expected.equals(actual))
			return;
		if (actual != null && actual.equals(expected))
			return;
		throw new IllegalArgumentException(parameter + " is niet '" + expected + "' maar '"
			+ actual + "'");
	}

	/**
	 * Controleert of de waarde van de parameter voldoet aan het formaat van de reguliere
	 * expressie.
	 * 
	 * @param parameter
	 *            de naam van de parameter
	 * @param value
	 *            de waarde van de parameter
	 * @param regexp
	 *            de reguliere expressie waaraan voldaan moet worden
	 */
	public static void assertMatchesRegExp(String parameter, String value, String regexp)
	{
		assertNotNull(parameter, value);
		assertNotNull("regexp", regexp);

		Pattern pattern = Pattern.compile(regexp);
		String trimmedValue = value.trim();
		Matcher matcher = pattern.matcher(trimmedValue.subSequence(0, trimmedValue.length()));
		if (!matcher.matches())
		{
			if (log.isDebugEnabled())
			{
				log.debug("Matcher waarde: " + matcher.toString());
			}
			throw new IllegalArgumentException("De waarde '" + value + "' van parameter "
				+ parameter + " voldoet niet aan het formaat " + regexp);
		}
	}

	public static void fail(String message)
	{
		throw new IllegalArgumentException(message);
	}

	public static void assertTrue(String message, boolean conditie)
	{
		if (!conditie)
		{
			throw new IllegalStateException(message);
		}
	}

	public static void assertFalse(String message, boolean conditie)
	{
		assertTrue(message, !conditie);
	}
}
