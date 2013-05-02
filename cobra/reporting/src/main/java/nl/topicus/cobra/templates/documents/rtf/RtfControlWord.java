package nl.topicus.cobra.templates.documents.rtf;

import java.io.BufferedWriter;
import java.io.IOException;

import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;

/**
 * Een RTF control word.
 * 
 * @author boschman
 */
public class RtfControlWord implements IRtfElement
{
	private final String name;

	private final Integer value;

	public RtfControlWord(String name)
	{
		this(name, null);
	}

	public RtfControlWord(String name, int value)
	{
		this(name, Integer.valueOf(value));
	}

	private RtfControlWord(String name, Integer value)
	{
		this.name = name;
		this.value = value;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public void write(BufferedWriter writer) throws TemplateException
	{
		try
		{
			writer.append(toString());
		}
		catch (IOException e)
		{
			throw new TemplateCreationException("Cannot write document", e);
		}
	}

	@Override
	public String toString()
	{
		return name + (value != null ? String.valueOf(value) : "");
	}

	@Override
	public RtfControlWord clone()
	{
		return new RtfControlWord(name, value);
	}
}
