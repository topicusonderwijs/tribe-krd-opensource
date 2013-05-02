package nl.topicus.cobra.util;

import java.io.PrintWriter;

/**
 * Utility methods voor het parsen van chained exceptions.
 * 
 * @author dashorst
 */
public class ExceptionUtil
{
	public static String getDescription(Exception exception)
	{
		Throwable wortel = getRootCause(exception);
		if (wortel instanceof NullPointerException)
		{
			return wortel.getClass().getSimpleName();
		}
		return wortel.getClass().getSimpleName() + ": " + wortel.getMessage();
	}

	/**
	 * @return <code>true</code> wanneer de exception veroorzaakt is door een exception
	 *         van type cause.
	 */
	public static boolean isCausedBy(Throwable exception, Class< ? extends Throwable> cause)
	{
		if (cause.isInstance(exception))
			return true;
		// om oneindige recursie te voorkomen...
		if (exception.getCause() == exception)
			return false;
		if (exception.getCause() != null)
			return isCausedBy(exception.getCause(), cause);
		return false;
	}

	/**
	 * Deze schrijft de inverse van wat {@link Throwable#printStackTrace()} doet: deze
	 * methode zet de belangrijkste informatie namelijk bovenaan.
	 */
	public static void printStackTrace(PrintWriter pw, Throwable t)
	{
		printStackTrace(pw, t, new StackTraceElement[] {});
	}

	private static void printStackTrace(PrintWriter pw, Throwable t, StackTraceElement[] wrapped)
	{
		String message;
		if (t.getCause() != t && t.getCause() != null)
		{
			printStackTrace(pw, t.getCause(), t.getStackTrace());
			message = "Wrapped by ";
		}
		else
		{
			message = "";
		}

		pw.println(message + t);
		StackTraceElement[] stackTrace = t.getStackTrace();
		for (StackTraceElement element : stackTrace)
		{
			boolean containedInWrapper = false;
			int i = 0;
			while (i < wrapped.length && !containedInWrapper)
			{
				containedInWrapper = element.equals(wrapped[i]);
				i++;
			}
			if (!containedInWrapper)
				pw.printf("\tat %s.%s(%s:%d)\n", element.getClassName(), element.getMethodName(),
					element.getFileName(), element.getLineNumber());
		}
	}

	/**
	 * @return de exceptie van het exceptionType uit de cause hierarchie.
	 */
	public static <T extends Throwable> T getCause(Throwable exception, Class<T> exceptionType)
	{
		if (exception.getClass().isAssignableFrom(exceptionType))
			return exceptionType.cast(exception);
		// om oneindige recursie te voorkomen...
		if (exception.getCause() == exception)
			return null;
		if (exception.getCause() != null)
			return getCause(exception.getCause(), exceptionType);
		return null;
	}

	public static Throwable getRootCause(Throwable exception)
	{
		// om oneindige recursie te voorkomen...
		if (exception.getCause() == exception)
			return exception;
		if (exception.getCause() == null)
			return exception;
		return getRootCause(exception.getCause());
	}
}
