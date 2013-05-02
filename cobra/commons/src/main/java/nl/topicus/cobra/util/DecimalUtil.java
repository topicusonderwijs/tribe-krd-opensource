/*
 * Copyright (c) 2005-2009, Topicus b.v.
 * All rights reserved
 */
package nl.topicus.cobra.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * Utillity class om makkelijker met bigdecimals te werken.
 * 
 * @author brand
 * @author marrink
 */
public class DecimalUtil
{
	public static final BigDecimal ONE = BigDecimal.ONE;

	public static final BigDecimal TWO = BigDecimal.valueOf(2);

	public static final BigDecimal THREE = BigDecimal.valueOf(3);

	public static final BigDecimal FOUR = BigDecimal.valueOf(4);

	public static final BigDecimal FIVE = BigDecimal.valueOf(5);

	public static final BigDecimal SIX = BigDecimal.valueOf(6);

	public static final BigDecimal SEVEN = BigDecimal.valueOf(7);

	public static final BigDecimal EIGHT = BigDecimal.valueOf(8);

	public static final BigDecimal NINE = BigDecimal.valueOf(9);

	public static final BigDecimal TEN = BigDecimal.TEN;

	public static final BigDecimal ELEVEN = BigDecimal.valueOf(11);

	public static final BigDecimal SIXTEEN = BigDecimal.valueOf(16);

	public static final BigDecimal EIGHTEEN = BigDecimal.valueOf(18);

	public static final BigDecimal TWENTY = BigDecimal.valueOf(20);

	public static final BigDecimal THIRTYSIX = BigDecimal.valueOf(36);

	public static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

	public static final BigDecimal FIVE_POINT_FIVE = new BigDecimal("5.5");

	/**
	 * default precisie voor delingen bij resultaten
	 */
	public static final MathContext RESULTAAT_MATH_CONTEXT =
		new MathContext(10, RoundingMode.HALF_UP);

	/**
	 * @param decimal
	 * @return true als de waarde !=0 en niet null is, anders false
	 */
	public static final boolean nonZero(BigDecimal decimal)
	{
		return decimal != null && decimal.compareTo(BigDecimal.ZERO) != 0;
	}

	/**
	 * @param decimal
	 * @return true als de waarde 0 is
	 */
	public static final boolean isZero(BigDecimal decimal)
	{
		return decimal.compareTo(BigDecimal.ZERO) == 0;
	}

	/**
	 * @param is
	 * @param than
	 * @return true als de waarde van 'is' groter dan 'than' is
	 */
	public static final boolean greaterThan(BigDecimal is, BigDecimal than)
	{
		return is.compareTo(than) > 0;
	}

	/**
	 * @param is
	 * @param than
	 * @return true als de waarde van 'is' groter dan 'than' is
	 */
	public static final boolean greaterThan(BigDecimal is, int than)
	{
		return is.compareTo(BigDecimal.valueOf(than)) > 0;
	}

	/**
	 * @param is
	 * @param than
	 * @return true als de waarde van 'is' groter dan of gelijk aan 'than' is
	 */
	public static final boolean greaterThanOrEqual(BigDecimal is, int than)
	{
		return is.compareTo(BigDecimal.valueOf(than)) >= 0;
	}

	/**
	 * Vergelijkt 2 BigDecimals (nullsafe). returned een negatieve waarde als one < other
	 * is, 0 als beide gelijk zijn, 1 als one > other is. Een null waarde is altijd
	 * kleiner dan een andere waarde
	 * 
	 * @param one
	 * @param other
	 * @return een negatieve waarde als one < other is, 0 als beide gelijk zijn, 1 als one
	 *         > other is
	 */
	public static final int compare(BigDecimal one, BigDecimal other)
	{
		if (one == null && other == null)
			return 0;
		if (one == null)
			return -1;
		else if (other == null)
			return 1;
		return one.compareTo(other);
	}

	/**
	 * @param one
	 * @param other
	 * @return de grootste van beide
	 */
	public static final BigDecimal max(BigDecimal one, BigDecimal other)
	{
		return (one.compareTo(other) > 0 ? one : other);
	}

	/**
	 * @param one
	 * @param other
	 * @return de kleinste van beide
	 */
	public static final BigDecimal min(BigDecimal one, BigDecimal other)
	{
		return (one.compareTo(other) < 0 ? one : other);
	}

	/**
	 * @param is
	 * @return true als de waarde groter is dan BigDecimal.ZERO en !=null
	 */
	public static final boolean greaterThanZero(BigDecimal is)
	{
		return is != null && is.compareTo(BigDecimal.ZERO) > 0;
	}

	/**
	 * Kijkt of een decimal een waarde heeft die op of boven 0.0 ligt.
	 * 
	 * @param decimal
	 * @return true als de waarde groter gelijk aan BigDecimal.ZERO is, false in alle
	 *         andere gevallen, dus ook bij null
	 */
	public static boolean zeroOrMore(BigDecimal decimal)
	{
		return decimal != null && decimal.compareTo(BigDecimal.ZERO) >= 0;
	}

	/**
	 * Is de waarde kleiner dan 0, null is groter dan 0.
	 * 
	 * @param decimal
	 * @return true als de waarde kleiner dan 0 is, anders false
	 */
	public static final boolean lessThanZero(BigDecimal decimal)
	{
		if (decimal == null)
			return false;
		return decimal.compareTo(BigDecimal.ZERO) < 0;
	}

	/**
	 * Is de waarde kleiner gelijk aan 0, null is groter dan 0.
	 * 
	 * @param decimal
	 * @return true als de waarde kleiner gelijk aan 0 is, anders false
	 */
	public static final boolean lessOrEqualtoZero(BigDecimal decimal)
	{
		if (decimal == null)
			return false;
		return decimal.compareTo(BigDecimal.ZERO) <= 0;
	}

	/**
	 * Is de waarde kleiner gelijk dan de andere waarde
	 * 
	 * @param decimal
	 * @param other
	 * @return true als decimal &lt;= other is, anders false
	 */
	public static final boolean lessOrEqual(BigDecimal decimal, int other)
	{
		return decimal.compareTo(BigDecimal.valueOf(other)) <= 0;
	}

	/**
	 * @param decimal
	 * @param by
	 * @return decimal / by
	 */
	public static final BigDecimal divide(BigDecimal decimal, int by)
	{
		return decimal.divide(BigDecimal.valueOf(by));
	}

	/**
	 * @param decimal
	 * @param mc
	 * @param by
	 * @return decimal / by afgerond volgens de context
	 */
	public static final BigDecimal divide(BigDecimal decimal, MathContext mc, int by)
	{
		return decimal.divide(BigDecimal.valueOf(by), mc);
	}

	/**
	 * @param decimal
	 * @param mc
	 * @param by
	 * @return decimal / by afgerond volgens de context
	 */
	public static final BigDecimal divide(BigDecimal decimal, MathContext mc, BigDecimal by)
	{
		return decimal.divide(by, mc);
	}

	/**
	 * @param decimal
	 * @param by
	 * @return decimal / by
	 */
	public static final BigDecimal divide(int decimal, int by)
	{
		return BigDecimal.valueOf(decimal).divide(BigDecimal.valueOf(by));
	}

	/**
	 * @param decimal
	 * @param mc
	 * @param by
	 * @return decimal / by afgerond volgens de context
	 */
	public static final BigDecimal divide(int decimal, MathContext mc, int by)
	{
		return BigDecimal.valueOf(decimal).divide(BigDecimal.valueOf(by), mc);
	}

	/**
	 * @param decimal
	 * @param by
	 * @return decimal * by
	 */
	public static final BigDecimal multiply(BigDecimal decimal, int by)
	{
		return decimal.multiply(BigDecimal.valueOf(by));
	}

	/**
	 * @param decimal
	 * @param by
	 * @return decimal * by
	 */
	public static final BigDecimal multiplyRounded(BigDecimal decimal, int by)
	{
		return decimal.setScale(0, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(by));
	}

	/**
	 * Maakt een BigDecimal van de gegeven string.
	 * 
	 * @param string
	 * @return BigDecimal representatie van de gegeven string.
	 */
	public static final BigDecimal valueOf(String string)
	{
		return new BigDecimal(localizeString(string));
	}

	private static final String localizeString(String org)
	{
		int punten = StringUtil.countOccurances(org, '.');
		int comma = StringUtil.countOccurances(org, ',');
		if (punten == 1 && comma == 0)
			return org;
		else if (comma == 1 && punten == 0)
			return org.replace(',', '.');
		else if (punten > 1 && comma > 1)
			throw new IllegalArgumentException("Ongeldig aantal punten en comma's");

		int puntIndex = punten > 0 ? org.indexOf('.') : -1;
		int commaIndex = comma > 0 ? org.indexOf(',') : -1;
		if (before(puntIndex, commaIndex))
		{
			if (comma > punten)
				throw new IllegalArgumentException(
					"Er zijn meer comma's dan punten maar de punt komt eerst");
			String[] tokens = org.split("\\.", -1);
			return handleParts(org, tokens, ',', true);
		}
		else if (before(commaIndex, puntIndex))
		{
			if (punten > comma)
				throw new IllegalArgumentException(
					"Er zijn meer punten dan comma's maar de comma komt eerst");
			String[] tokens = org.split(",", -1);
			return handleParts(org, tokens, '.', false);
		}
		else
			// geen punten of comma's
			return org;
	}

	private static final boolean before(int index1, int index2)
	{
		return (index2 == -1 && index1 >= 0) || (index1 >= 0 && index1 < index2);
	}

	/**
	 * @param org
	 * @param tokens
	 * @return
	 */
	private static final String handleParts(String org, String[] tokens, char decimalSeparator,
			boolean replaceDot)
	{
		String result = validateString(tokens, decimalSeparator);
		if (result != null)
			throw new IllegalArgumentException("\"" + result + "\" is geen geldig duizendtal.");
		if (replaceDot)
			tokens[tokens.length - 1] = tokens[tokens.length - 1].replace(',', '.');
		StringBuilder builder = new StringBuilder(org.length());
		for (int i = 0; i < tokens.length; i++)
		{
			builder.append(tokens[i]);
		}
		return builder.toString();
	}

	/**
	 * Validates elk onderdeel van de string
	 * 
	 * @param split
	 * @return
	 */
	private static final String validateString(String[] split, char decimalSeparator)
	{
		if (split.length <= 2 && split.length > 0)
			return split[0].length() > 0 ? null : split[0]; // elke lengte is goed behalve
		// 0
		int length = 0;
		for (int i = 0; i < split.length; i++)
		{
			length = split[i].length();
			if (i == 0)
			{
				if (length > 3 || length < 1)
					return split[i];
			}
			else if (i == split.length - 1)
			{
				int index = split[i].indexOf(decimalSeparator);
				if ((index < 0 && length != 3) || (index >= 0 && index != 3))
					return split[i];
			}
			else if (length != 3)
				return split[i];
		}
		return null;
	}

	/**
	 * @param bigDecimalList
	 * @return het gemiddelde als er cijfers in de bigDecimalList zitten, anders -1
	 */
	public static final BigDecimal average(List<BigDecimal> bigDecimalList)
	{
		if (bigDecimalList == null)
		{
			return BigDecimal.valueOf(-1);
		}

		BigDecimal sum = BigDecimal.valueOf(0);
		int aantalCijfers = 0;
		for (BigDecimal bigDecimal : bigDecimalList)
		{
			if (bigDecimal != null)
			{
				sum = sum.add(bigDecimal);
				aantalCijfers++;

			}
		}
		// Bij geen cijfers -1 terug geven
		if (aantalCijfers == 0)
			return BigDecimal.valueOf(-1);

		// return sum.divide(BigDecimal.valueOf(aantalCijfers), 2);
		return divide(sum, new MathContext(2, RoundingMode.HALF_UP), aantalCijfers);
	}
}
