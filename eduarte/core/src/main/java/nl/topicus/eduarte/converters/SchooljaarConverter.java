package nl.topicus.eduarte.converters;

import java.util.Locale;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;

import org.apache.wicket.util.convert.IConverter;

/**
 * Converteert een <tt>Schooljaar</tt> naar <tt>String</tt> of v.v.
 * 
 * @author dashorst
 */
public class SchooljaarConverter implements IConverter
{
	private static final long serialVersionUID = 1L;

	@Override
	public Object convertToObject(String value, Locale locale)
	{
		if (StringUtil.isEmpty(value))
			return null;
		return Schooljaar.parse(value);
	}

	@Override
	public String convertToString(Object value, Locale locale)
	{
		return ((Schooljaar) value).getOmschrijving();
	}
}
