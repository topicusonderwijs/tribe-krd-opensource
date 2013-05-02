package nl.topicus.cobra.templates.exceptions;

/**
 * Exceptie als het maken van een templatedocument faalt.
 * 
 * @author boschman
 */
public class TemplateCreationException extends TemplateException
{
	private static final long serialVersionUID = 1L;

	public TemplateCreationException()
	{
		super();
	}

	public TemplateCreationException(String message)
	{
		super(message);
	}

	public TemplateCreationException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
