/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.converters;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.topicus.cobra.util.StringUtil;

/**
 * Converts strings to uppercase (both ways) and uses
 * {@link PostcodeConverter#POSTCODE_NL_REGEX} to convert to String or Object.
 */
public class PostcodeConverter extends UppercaseConverter
{
	private static final long serialVersionUID = 1L;

	/**
	 * 4 cijfers, optioneel een spatie en 2 letters.
	 */
	public static final String POSTCODE_NL_REGEX = "([0-9]{4})([\\s]?)([a-zA-Z]{2})";

	private static final Pattern POSTCODE_NL = Pattern.compile(POSTCODE_NL_REGEX);

	@Override
	protected Class< ? > getTargetType()
	{
		return String.class;
	}

	@Override
	public String convertToString(Object value, Locale locale)
	{
		if (value == null)
			return null;

		String postcodeString = value.toString().toUpperCase();
		Matcher postcodeMatcher = POSTCODE_NL.matcher(postcodeString);
		if (postcodeMatcher.matches())
		{
			String cijfers = postcodeMatcher.group(1);
			String spatie = postcodeMatcher.group(2);
			String letters = postcodeMatcher.group(3);

			if (StringUtil.isEmpty(spatie))
				spatie = " ";

			return cijfers + spatie + letters;
		}

		return postcodeString;
	}

	@Override
	public Object convertToObject(String value, Locale locale)
	{
		if (value == null)
			return null;

		String postcodeString = value.toString().toUpperCase();
		Matcher postcodeMatcher = POSTCODE_NL.matcher(postcodeString);
		if (postcodeMatcher.matches())
		{
			String cijfers = postcodeMatcher.group(1);
			String letters = postcodeMatcher.group(3);

			return cijfers + letters;
		}

		return postcodeString;
	}
}
