/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.converters;

import java.util.Locale;

import org.apache.wicket.util.convert.IConverter;

/**
 * Converter die van boolean een string maakt: Actief of Niet actief.
 * 
 * @author marrink
 */
public class ActiefConverter implements IConverter
{

	// Class kan uitgebreid worden om via een property file locale specifieke strings
	// terug te geven
	private static final long serialVersionUID = 1L;

	private static final String TRUE = "Actief";

	private static final String FALSE = "Niet actief";

	/**
	 * 
	 */
	public ActiefConverter()
	{
	}

	/**
	 * @see org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String,
	 *      java.util.Locale)
	 */
	@Override
	public Object convertToObject(String value, Locale locale)
	{
		if (value.equals(TRUE))
			return true;
		return false;
	}

	/**
	 * @see org.apache.wicket.util.convert.IConverter#convertToString(java.lang.Object,
	 *      java.util.Locale)
	 */
	@Override
	public String convertToString(Object value, Locale locale)
	{
		if (value instanceof Boolean && (Boolean) value)
			return TRUE;
		return FALSE;
	}

}
