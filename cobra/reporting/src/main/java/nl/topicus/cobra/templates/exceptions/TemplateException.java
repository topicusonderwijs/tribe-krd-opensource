package nl.topicus.cobra.templates.exceptions;

/**
 * Base class voor alle excepties uit de template engine.
 * 
 * @author boschman
 */
public abstract class TemplateException extends Exception
{
	private static final long serialVersionUID = 1L;

	public TemplateException()
	{
		super();
	}

	public TemplateException(String message)
	{
		super(message);
	}

	public TemplateException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
