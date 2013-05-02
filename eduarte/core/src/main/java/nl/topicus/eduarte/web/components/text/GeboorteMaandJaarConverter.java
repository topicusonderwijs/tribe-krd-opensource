package nl.topicus.eduarte.web.components.text;

import java.util.Date;
import java.util.Locale;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.personen.Persoon.ToepassingGeboortedatum;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.converters.DateConverter;

public class GeboorteMaandJaarConverter extends DateConverter
{
	private static final long serialVersionUID = 1L;

	private ToepassingGeboortedatum toepassing;

	private TimeUtil timeUtil;

	public GeboorteMaandJaarConverter(ToepassingGeboortedatum toepassing)
	{
		this.toepassing = toepassing;
		this.timeUtil = TimeUtil.getInstance();
	}

	@Override
	public Date convertToObject(String value, Locale locale)
	{
		if (StringUtil.isNotEmpty(value))
		{
			Date date = null;
			if (value.length() == 10)
			{
				date = timeUtil.parseDateString(value);
			}
			else if (value.length() == 7)
			{
				date = timeUtil.parseDateString("01-" + value);
			}
			else if (value.length() == 4)
			{
				date = timeUtil.parseDateString("01-01" + value);
			}

			if (date == null)
				throw new ConversionException(value + " is geen geldige datum");
			return date;
		}
		return null;
	}

	@Override
	public String convertToString(Object value, Locale locale)
	{
		String returnvalue = null;
		if (value != null && value instanceof Date)
		{
			if (toepassing == ToepassingGeboortedatum.GeboortemaandEnJaar)
			{
				returnvalue = timeUtil.formatDateAs_mm_yyyy((Date) value);
			}
			else if (toepassing == ToepassingGeboortedatum.Geboortejaar)
			{
				returnvalue = timeUtil.formatDateAs_yyyy((Date) value);
			}
		}
		return returnvalue;
	}
}
