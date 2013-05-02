package nl.topicus.eduarte.entities.participatie;

public class ExterneAgendaException extends Exception
{
	private static final long serialVersionUID = 1L;

	public ExterneAgendaException()
	{
	}

	public ExterneAgendaException(String message)
	{
		super(message);
	}

	public ExterneAgendaException(Throwable cause)
	{
		super(cause);
	}

	public ExterneAgendaException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
