package nl.topicus.cobra.converters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.converters.DateConverter;

/**
 * Eigen date converter voor Nederlandse datums.
 */
public final class DutchDateConverter extends DateConverter
{
	private static final long serialVersionUID = 1L;

	@Override
	public DateFormat getDateFormat(Locale locale)
	{
		if (new Locale("nl", "NL").equals(locale))
			return new SimpleDateFormat("dd-MM-yyyy");

		return super.getDateFormat(locale);
	}

	@Override
	public Date convertToObject(String value, Locale locale)
	{
		if (StringUtil.isNotEmpty(value))
		{
			Date date = TimeUtil.getInstance().parseDateString(value);
			if (date == null)
				throw new ConversionException(value + " is geen geldige datum");
			return date;
		}
		return null;
	}

	@Override
	public String convertToString(Object value, Locale locale)
	{
		if (value == null)
			return null;
		return TimeUtil.getInstance().formatDate((Date) value);
	}
}