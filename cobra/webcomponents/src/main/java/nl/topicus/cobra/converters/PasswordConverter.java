/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.converters;

import java.util.Locale;

import nl.topicus.cobra.util.StringUtil;

import org.apache.wicket.util.convert.IConverter;

/**
 * Converter die een string als *tjes toont. Alleen te bebruiken bij labels aangezien dit
 * een eenrichtingsconversie is.
 * 
 * @author marrink
 */
public class PasswordConverter implements IConverter
{
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public PasswordConverter()
	{
	}

	/**
	 * @see org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String,
	 *      java.util.Locale)
	 */
	@Override
	public Object convertToObject(String value, Locale locale)
	{
		return value;
	}

	/**
	 * @see org.apache.wicket.util.convert.IConverter#convertToString(java.lang.Object,
	 *      java.util.Locale)
	 */
	@Override
	public String convertToString(Object value, Locale locale)
	{
		if (value == null)
			return null;
		int length = value.toString().length();
		return StringUtil.repeatString("*", length);
	}

}
