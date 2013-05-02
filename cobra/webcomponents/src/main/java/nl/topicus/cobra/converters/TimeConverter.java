/*
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.converters;

import java.text.SimpleDateFormat;
import java.util.Locale;

import nl.topicus.cobra.entities.Time;

import org.apache.wicket.util.convert.converters.AbstractConverter;

/**
 * @author loite Een converter voor de nl.topicus.iridium.entities.Time class.
 */
public class TimeConverter extends AbstractConverter
{

	private static final long serialVersionUID = 3385319284076009158L;

	private final String pattern;

	/**
	 * Creates a converter with the standard short time pattern 'HH:mm'.
	 */
	public TimeConverter()
	{
		this("HH:mm");
	}

	/**
	 * Creates a converter with a custom pattern.
	 * 
	 * @param pattern
	 * @see SimpleDateFormat for valid patterns
	 */
	public TimeConverter(String pattern)
	{
		this.pattern = pattern;

	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class getTargetType()
	{
		return Time.class;
	}

	public Object convertToObject(final String value, Locale locale)
	{
		try
		{
			Time res = Time.valueOf(value);
			return res;
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}

	@Override
	public String convertToString(Object value, Locale locale)
	{
		return new SimpleDateFormat(pattern, locale).format(value);
		// new instance because class is not thread safe,
		// if to slow we could cache them in threadlocals
	}

	/**
	 * The pattern used to format the time.
	 * 
	 * @return Returns the pattern.
	 */
	public final String getPattern()
	{
		return pattern;
	}
}
