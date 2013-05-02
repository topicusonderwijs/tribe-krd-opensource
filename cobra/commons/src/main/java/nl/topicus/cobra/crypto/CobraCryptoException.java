package nl.topicus.cobra.crypto;

public class CobraCryptoException extends Exception
{

	private static final long serialVersionUID = 1L;

	public CobraCryptoException()
	{
	}

	public CobraCryptoException(String message)
	{
		super(message);
	}

	public CobraCryptoException(Throwable cause)
	{
		super(cause);
	}

	public CobraCryptoException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
