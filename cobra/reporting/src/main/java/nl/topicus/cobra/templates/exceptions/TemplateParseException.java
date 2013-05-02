package nl.topicus.cobra.templates.exceptions;

/**
 * Exceptie als het parsen van een templatedocument mislukt.
 * 
 * @author boschman
 */
public class TemplateParseException extends TemplateException
{
	private static final long serialVersionUID = 1L;

	public TemplateParseException()
	{
		super();
	}

	public TemplateParseException(String message)
	{
		super(message);
	}

	public TemplateParseException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
