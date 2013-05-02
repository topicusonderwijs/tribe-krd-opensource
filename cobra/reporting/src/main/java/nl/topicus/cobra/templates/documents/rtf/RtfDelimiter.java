package nl.topicus.cobra.templates.documents.rtf;

import java.io.BufferedWriter;
import java.io.IOException;

import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;

public class RtfDelimiter implements IRtfElement
{
	private final String delimiter;

	public RtfDelimiter(String delimiter)
	{
		this.delimiter = delimiter;
	}

	/**
	 * @return the text
	 */
	public String getDelimiter()
	{
		return delimiter;
	}

	@Override
	public void write(BufferedWriter writer) throws TemplateException
	{
		try
		{
			writer.append(getDelimiter());
		}
		catch (IOException e)
		{
			throw new TemplateCreationException("Cannot write document.", e);
		}
	}

	@Override
	public String toString()
	{
		return getDelimiter();
	}

	@Override
	public RtfDelimiter clone()
	{
		return new RtfDelimiter(delimiter);
	}
}
