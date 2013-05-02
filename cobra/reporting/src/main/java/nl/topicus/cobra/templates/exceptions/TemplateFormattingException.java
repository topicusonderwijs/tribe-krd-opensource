package nl.topicus.cobra.templates.exceptions;

/**
 * Exceptie als een waarde niet geformatteerd kan worden met het opgegegeven patroon.
 * 
 * @author boschman
 */
public class TemplateFormattingException extends TemplateException
{
	private static final long serialVersionUID = 1L;

	public TemplateFormattingException()
	{
		super();
	}

	public TemplateFormattingException(String message)
	{
		super(message);
	}

	public TemplateFormattingException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
