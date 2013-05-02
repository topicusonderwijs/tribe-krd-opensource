package nl.topicus.cobra.templates.documents.rtf;

import java.io.BufferedWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.exceptions.TemplateFormattingException;
import nl.topicus.cobra.templates.resolvers.FieldResolver;
import nl.topicus.cobra.util.StringUtil;

public class RtfField extends RtfControlGroup
{
	public RtfField()
	{
		super();
	}

	public RtfField(AbstractRtfGroup parent)
	{
		super(parent);
	}

	public RtfFieldInstruction getInstruction()
	{
		for (IRtfElement element : getElements())
		{
			if (element instanceof RtfFieldInstruction)
				return (RtfFieldInstruction) element;
		}

		throw new RuntimeException("Field zonder FieldInstruction?");
	}

	public RtfFieldResult getResult()
	{
		for (IRtfElement element : getElements())
		{
			if (element instanceof RtfFieldResult)
				return (RtfFieldResult) element;
		}

		throw new RuntimeException("Field zonder FieldResult?");
	}

	public void merge(FieldResolver resolver) throws TemplateException
	{
		if (getInstruction().getType().equals("MERGEFIELD") && !getResult().isMerged())
		{
			String formattedValue = "";

			Object value = resolver.resolve(getInstruction().getName());
			String format = getInstruction().getFormat();

			if (value == null)
				return;

			if (value instanceof Date)
			{
				if (StringUtil.isEmpty(format))
					format = "dd-MM-yyyy";

				try
				{
					Locale loc = new Locale("nl", "NL");
					DateFormat formatter = new SimpleDateFormat(format, loc);

					formattedValue = formatter.format(value);
				}
				catch (IllegalArgumentException e)
				{
					throw new TemplateFormattingException("Ongeldige format optie '" + format
						+ "' voor een datum veld.", e);
				}
			}
			else if (value instanceof Boolean && format != null)
			{
				Pattern pattern = Pattern.compile("(.*)\\|(.*)");

				Matcher matcher = pattern.matcher(format);

				if (!matcher.matches())
					throw new TemplateFormattingException("Ongeldige format optie '" + format
						+ "' voor een boolean veld.");

				if (((Boolean) value))
					formattedValue = matcher.group(1);
				else
					formattedValue = matcher.group(2);
			}
			else if (value instanceof Number && format != null)
			{
				Locale loc = new Locale("nl", "NL");
				try
				{
					DecimalFormat formatter =
						new DecimalFormat(format, new DecimalFormatSymbols(loc));
					formattedValue = formatter.format(value);
				}
				catch (IllegalArgumentException e)
				{
					throw new TemplateFormattingException("Ongeldige format optie '" + format
						+ "' voor een numeriek veld.", e);
				}
			}
			else
			{
				formattedValue = formatUnicodeToRtf(value.toString());
			}

			getResult().setText(formattedValue);
		}

	}

	public void clear()
	{
		getResult().setText("");
	}

	@Override
	public RtfField clone() throws CloneNotSupportedException
	{
		RtfField clone = new RtfField();
		for (IRtfElement element : getElements())
		{
			IRtfElement cloneElement = element.clone();
			if (cloneElement instanceof AbstractRtfGroup)
				((AbstractRtfGroup) cloneElement).setParent(clone);

			clone.addElement(cloneElement);
		}
		return clone;
	}

	/**
	 * @param resolver
	 *            gaat de type info opzoeken
	 * @return fieldinfo behorende bij dit veld
	 */
	public FieldInfo getFieldInfo(FieldResolver resolver)
	{
		FieldInfo info = null;
		if ("MERGEFIELD".equals(getInstruction().getType()))
		{
			String name = getInstruction().getName();
			info = resolver.getInfo(name);

			if (StringUtil.isNotEmpty(getInstruction().getFormat()))
			{
				info.setFormat(getInstruction().getFormat());
			}
		}

		return info;
	}

	public static String formatUnicodeToRtf(String value)
	{
		StringBuilder sb = new StringBuilder();
		// deze replace vervangt de end of line karakters door de \par tag die in het rtf
		// formaat gebruikt wordt voor een nieuwe regel
		String rtfvalue = value.replaceAll("\r\n", "\\\\par ");
		char[] characters = rtfvalue.toCharArray();
		for (int i = 0; i < characters.length; i++)
		{
			int characterValue = characters[i];
			if (characterValue > 128)
			{
				sb.append("\\u" + characterValue + "?");

			}
			else
			{
				sb.append(characters[i]);
			}
		}

		return sb.toString();
	}

	/**
	 * @see nl.topicus.cobra.templates.documents.rtf.RtfControlGroup#write(java.io.BufferedWriter)
	 *      Schrijf de body van dit element weg. Tenzij men
	 *      {@link RtfDocument#setKeepMergeFields(boolean)} = false heeft geset, dan slaan
	 *      we dit element over en gaan we meteen naar het resultaat.
	 */
	@Override
	public void write(BufferedWriter writer) throws TemplateException
	{
		// Non-mergefield velden zoals checkboxes (FORMCHECKBOX) altijd as-is overnemen
		if (isKeepMergeFields() || !getInstruction().getType().equals("MERGEFIELD"))
			super.write(writer);
		else
			getResult().write(writer);
	}
}
