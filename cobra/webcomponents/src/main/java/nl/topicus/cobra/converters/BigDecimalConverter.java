package nl.topicus.cobra.converters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

/**
 * @author hoeve
 */
public class BigDecimalConverter implements IConverter
{
	private static final long serialVersionUID = 1L;

	private final int fractionDigits;

	/**
	 * insertDot is een grapje van onze matty Emond. Dit is speciaal voor cijfers, waar
	 * luie docenten graag 75 invullen en eigenlijk 7,5 bedoelen. Stel dit in op true als
	 * je ook lui bent.
	 */
	private final boolean insertDot;

	public BigDecimalConverter()
	{
		fractionDigits = -1;
		insertDot = false;
	}

	public BigDecimalConverter(int fractionDigits)
	{
		this.fractionDigits = fractionDigits;
		insertDot = false;
	}

	public BigDecimalConverter(int fractionDigits, boolean insertDot)
	{
		this.fractionDigits = fractionDigits;
		this.insertDot = insertDot;
	}

	@Override
	public BigDecimal convertToObject(String value, Locale locale)
	{
		if (value == null || value.trim().equals(""))
			return null;

		NumberFormat numberFormat = getNumberFormat(locale, null, fractionDigits);
		value = detectSymbolErrors(value, numberFormat);

		BigDecimal returnValue = convertToObject(value, locale, numberFormat);

		if (insertDot && returnValue.scale() == 0)
			returnValue = returnValue.movePointLeft(fractionDigits);
		else if (fractionDigits > -1)
			returnValue = returnValue.setScale(fractionDigits, RoundingMode.HALF_UP);

		return returnValue;
	}

	private BigDecimal convertToObject(String value, Locale locale, NumberFormat numberFormat)
	{
		if (numberFormat == null || value == null)
		{
			return null;
		}
		else
		{
			// Convert spaces to no-break space (U+00A0) to fix problems with
			// browser conversions.
			// Space is not valid thousands-separator, but no-br space is.
			value = value.replace(' ', '\u00A0');
		}

		final ParsePosition position = new ParsePosition(0);
		final String stringValue = value.toString();
		final Object result = numberFormat.parseObject(stringValue, position);
		if (position.getIndex() != stringValue.length())
		{
			throw new ConversionException("Cannot parse '" + value + "' using format "
				+ numberFormat).setSourceValue(value).setTargetType(BigDecimal.class).setConverter(
				this).setLocale(locale);
		}

		return (BigDecimal) result;
	}

	@Override
	public String convertToString(Object value, Locale locale)
	{
		if (value == null)
			return null;

		BigDecimal bigDecimalValue = (BigDecimal) value;

		NumberFormat fmt =
			getNumberFormat(locale, bigDecimalValue, (fractionDigits > -1) ? fractionDigits
				: bigDecimalValue.scale());
		if (fmt != null)
			return fmt.format(bigDecimalValue);

		return value.toString();
	}

	private String detectSymbolErrors(String value, NumberFormat fmt)
	{
		if (fmt instanceof DecimalFormat)
		{
			DecimalFormat numberDecimalFormat = (DecimalFormat) fmt;
			char groupSep = numberDecimalFormat.getDecimalFormatSymbols().getGroupingSeparator();
			char decSep = numberDecimalFormat.getDecimalFormatSymbols().getDecimalSeparator();
			int firstGroupingSepIndex = value.indexOf(groupSep);
			int lastGroupingSepIndex = value.lastIndexOf(groupSep);
			int firstDecimalSepIndex = value.indexOf(decSep);
			int lastDecimalSepIndex = value.lastIndexOf(decSep);

			/**
			 * We hebben de grouping separator per ongeluk als decimal separator gebruikt.
			 * (er is geen decimal index en er is max. 1 grouping index)
			 */
			if (firstDecimalSepIndex == -1 && firstGroupingSepIndex != -1
				&& firstGroupingSepIndex == lastGroupingSepIndex)
				value = value.replace(groupSep, decSep);

			/**
			 * We hebben de decimal separator per ongeluk als grouping separator gebruikt.
			 * (er is geen grouping index en er zijn min. 2 decimal indices)
			 */
			else if (firstGroupingSepIndex == -1 && firstDecimalSepIndex != -1
				&& firstDecimalSepIndex != lastDecimalSepIndex)
				value = value.replace(decSep, groupSep);

			/**
			 * We hebben de decimal separator per ongeluk als grouping gebruikt en
			 * anderson. (er is 1 grouping index en min. 2 decimal indices)
			 */
			else if (firstGroupingSepIndex != -1 && firstGroupingSepIndex == lastGroupingSepIndex
				&& firstDecimalSepIndex != -1 && firstDecimalSepIndex != lastDecimalSepIndex)
			{
				value = value.replace(decSep, '@');
				value = value.replace(groupSep, decSep);
				value = value.replace('@', groupSep);
			}
		}

		return value;
	}

	public NumberFormat getNumberFormat(Locale locale, BigDecimal bigDecimalValue, int numDigits)
	{
		NumberFormat numberFormat = NumberFormat.getInstance(locale);

		if (numberFormat instanceof DecimalFormat)
			((DecimalFormat) numberFormat).setParseBigDecimal(true);

		if (numDigits > -1)
		{
			numberFormat.setMaximumFractionDigits(numDigits);
			numberFormat.setMinimumFractionDigits(numDigits);
		}
		numberFormat.setRoundingMode(RoundingMode.HALF_UP);

		numberFormat.setGroupingUsed(bigDecimalValue == null
			|| bigDecimalValue.compareTo(new BigDecimal(1000000)) >= 0);

		return (NumberFormat) numberFormat.clone();
	}

}
