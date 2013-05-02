package nl.topicus.eduarte.resultaten.jobs;

public class RecalculationInterruptedException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public RecalculationInterruptedException(InterruptedException cause)
	{
		super(cause);
	}

	@Override
	public InterruptedException getCause()
	{
		return (InterruptedException) super.getCause();
	}
}
