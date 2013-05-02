package nl.topicus.cobra.templates.documents.rtf;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.util.StringUtil;

public class RtfText implements IRtfElement
{
	private String text;

	public RtfText(String text)
	{
		this.text = text;
	}

	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}

	public String getTextAsUnicode()
	{
		if (StringUtil.isEmpty(text))
			return text;

		Pattern UNICODE_PATTERN = Pattern.compile("\\'(\\S+)");
		Matcher matcher = UNICODE_PATTERN.matcher(text);
		StringBuffer sb = new StringBuffer();
		while (matcher.find())
		{
			matcher.appendReplacement(sb, Character.toString((char) Integer.parseInt(matcher
				.group(1), 16)));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public void setText(String text)
	{
		this.text = text;
	}

	@Override
	public void write(BufferedWriter writer) throws TemplateException
	{
		try
		{
			writer.append(getText());
		}
		catch (IOException e)
		{
			throw new TemplateCreationException("Cannot write document", e);
		}
	}

	@Override
	public String toString()
	{
		return getText();
	}

	@Override
	public RtfText clone()
	{
		return new RtfText(text);
	}
}
