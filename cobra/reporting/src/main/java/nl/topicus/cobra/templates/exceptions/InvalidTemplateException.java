package nl.topicus.cobra.templates.exceptions;

/**
 * Exceptie als het maken van een templatedocument faalt.
 * 
 * @author boschman
 */
public class InvalidTemplateException extends TemplateException
{
	private static final long serialVersionUID = 1L;

	public InvalidTemplateException()
	{
		super();
	}

	public InvalidTemplateException(String message)
	{
		super(message);
	}

	public InvalidTemplateException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
