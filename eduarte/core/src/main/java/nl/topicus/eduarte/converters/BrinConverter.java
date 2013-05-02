package nl.topicus.eduarte.converters;

import java.util.Locale;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.organisatie.Brin;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

/**
 * Converteert een textje naar een Brin object.
 * 
 * @author hoeve
 */
public class BrinConverter implements IConverter
{
	private static final long serialVersionUID = 1L;

	@Override
	public Object convertToObject(String value, Locale locale)
	{
		if (StringUtil.isEmpty(value))
			return null;

		try
		{
			return new Brin(value);
		}
		catch (IllegalArgumentException e)
		{
			throw new ConversionException(e).setResourceKey("BrinConverter").setVariable("input",
				value);
		}
	}

	@Override
	public String convertToString(Object value, Locale locale)
	{
		if (value == null || !(value instanceof Brin))
			return "";

		return ((Brin) value).getCode();
	}

}
