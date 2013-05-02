/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.converters;

import java.util.Locale;

import org.apache.wicket.util.convert.converters.AbstractConverter;

/**
 * Converts strings to uppercase (both ways)
 */
public class UppercaseConverter extends AbstractConverter
{
	private static final long serialVersionUID = 1L;

	@Override
	protected Class< ? > getTargetType()
	{
		return String.class;
	}

	@Override
	public String convertToString(Object value, Locale locale)
	{
		return value != null ? value.toString().toUpperCase() : null;
	}

	@Override
	public Object convertToObject(String value, Locale locale)
	{
		return value != null ? value.toUpperCase() : null;
	}
}
