package nl.topicus.eduarte.entities.resultaatstructuur;

public class ResultaatFormatException extends Exception
{
	private static final long serialVersionUID = 1L;

	public ResultaatFormatException()
	{
	}

	public ResultaatFormatException(String message)
	{
		super(message);
	}

	public ResultaatFormatException(Throwable cause)
	{
		super(cause);
	}

	public ResultaatFormatException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
