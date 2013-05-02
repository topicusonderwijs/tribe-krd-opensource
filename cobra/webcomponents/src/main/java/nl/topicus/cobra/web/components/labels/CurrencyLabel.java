package nl.topicus.cobra.web.components.labels;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

/**
 * Geeft bigdecimal met 2 decimalen weer en voegt euroteken toe
 * 
 * @author hop
 */
public class CurrencyLabel extends Label
{
	private static final long serialVersionUID = 1L;

	public CurrencyLabel(String id)
	{
		super(id);
	}

	public CurrencyLabel(String id, IModel<BigDecimal> model)
	{
		super(id, model);
	}

	@Override
	public IConverter getConverter(Class< ? > type)
	{
		if (Number.class.isAssignableFrom(type))
			return new CurrencyConverter();

		return super.getConverter(type);
	}

	public static class CurrencyConverter implements IConverter
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Object convertToObject(String value, Locale locale)
		{
			NumberFormat format = NumberFormat.getCurrencyInstance(locale);
			try
			{
				return format.parseObject(value);
			}
			catch (ParseException e)
			{
				return null;
			}
		}

		@Override
		public String convertToString(Object value, Locale locale)
		{
			if (value instanceof BigDecimal)
			{
				NumberFormat format = NumberFormat.getCurrencyInstance(locale);
				return format.format(value);
			}

			return "";
		}

	}
}
