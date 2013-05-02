/*
 * $Id: DoubleConverter.java,v 1.2 2007-07-26 05:03:41 loite Exp $
 * $Revision: 1.2 $
 * $Date: 2007-07-26 05:03:41 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.converters;

import java.util.Locale;

import nl.topicus.cobra.util.StringUtil;

import org.apache.wicket.util.convert.converters.AbstractDecimalConverter;

/**
 * Double converter die alles naar nederlandse standaard omzet.
 * <table>
 * <tr>
 * <th>van</th>
 * <th>naar</th>
 * </tr>
 * <tr>
 * <td>6.5</td>
 * <td>6,5</td>
 * </tr>
 * <tr>
 * <td>6,5</td>
 * <td>6,5</td>
 * </tr>
 * <tr>
 * <td>1.000,50</td>
 * <td>1000,50</td>
 * </tr>
 * <tr>
 * <td>1,000.50</td>
 * <td>1000,50</td>
 * </tr>
 * <tr>
 * <td>1.000.000</td>
 * <td>1000000</td>
 * </tr>
 * <tr>
 * <td>1,000,000</td>
 * <td>1000000</td>
 * </tr>
 * <tr>
 * <td>1,000,00,000</td>
 * <td>error</td>
 * </tr>
 * <tr>
 * <td>1.00.00</td>
 * <td>error</td>
 * </tr>
 * <tr>
 * <td>1.000.000,000,000</td>
 * <td>error</td>
 * </tr>
 * </table>
 * 
 * @author marrink
 */
public final class DoubleConverter extends AbstractDecimalConverter
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Locale NL = new Locale("nl", "NL");

	/**
	 * 
	 */
	public DoubleConverter()
	{
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class getTargetType()
	{
		return Double.class;
	}

	/**
	 * @see org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String,
	 *      java.util.Locale)
	 */
	public Object convertToObject(String value, Locale locale)
	{
		if (value == null)
			return null;
		Number number;
		String localizedString = localizeString(String.valueOf(value));
		// if(localizedString.length()>0 && localizedString.charAt(0)==',')
		// localizedString="0"+localizedString;
		number = parse(localizedString, -Double.MAX_VALUE, Double.MAX_VALUE, NL);
		if (number == null)
			return null;
		return Double.valueOf(number.doubleValue());
	}

	private String localizeString(String org)
	{
		int punten = StringUtil.countOccurances(org, '.');
		int comma = StringUtil.countOccurances(org, ',');
		if (comma == 1 && punten == 0)
			return org;
		else if (punten == 1 && comma == 0)
			return org.replace('.', ',');
		else if (punten > 1 && comma > 1)
			throw newConversionException("Ongeldig aantal punten en comma's", org, NL);
		int puntIndex = punten > 0 ? org.indexOf('.') : -1;
		int commaIndex = comma > 0 ? org.indexOf(',') : -1;
		if (before(puntIndex, commaIndex))
		{
			if (comma > punten)
				throw newConversionException(
					"Er zijn meer comma's dan punten maar de punt komt eerst", org, NL);
			String[] tokens = org.split("\\.", -1);
			return handleParts(org, tokens, ',', false);
		}
		else if (before(commaIndex, puntIndex))
		{
			if (punten > comma)
				throw newConversionException(
					"Er zijn meer punten dan comma's maar de comma komt eerst", org, NL);
			String[] tokens = org.split(",", -1);
			return handleParts(org, tokens, '.', true);
		}
		else
			// geen punten of comma's
			return org;
	}

	private boolean before(int index1, int index2)
	{
		return (index2 == -1 && index1 >= 0) || (index1 >= 0 && index1 < index2);
	}

	/**
	 * @param org
	 * @param tokens
	 * @return
	 */
	private String handleParts(String org, String[] tokens, char decimalSeparator,
			boolean replaceDot)
	{
		String result = validateString(tokens, decimalSeparator);
		if (result != null)
			throw newConversionException("\"" + result + "\" is geen geldig duizendtal.", org, NL);
		if (replaceDot)
			tokens[tokens.length - 1] = tokens[tokens.length - 1].replace('.', ',');
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
	private String validateString(String[] split, char decimalSeparator)
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

}
