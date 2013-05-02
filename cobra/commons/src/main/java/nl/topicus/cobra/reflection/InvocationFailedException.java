package nl.topicus.cobra.reflection;

public class InvocationFailedException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public InvocationFailedException()
	{
	}

	public InvocationFailedException(String message)
	{
		super(message);
	}

	public InvocationFailedException(Throwable cause)
	{
		super(cause);
	}

	public InvocationFailedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public boolean isCausedByMethodNotFound()
	{
		return getCause() instanceof MethodNotFoundException;
	}
}
