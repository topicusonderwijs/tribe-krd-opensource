package nl.topicus.cobra.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExceptionUtilTest
{
	private StringWriter sw;

	private PrintWriter pw;

	@Before
	public void setUp()
	{
		sw = new StringWriter();
		pw = new PrintWriter(sw);
	}

	public NullPointerException returnsNullPointerException()
	{
		return new NullPointerException();
	}

	private RuntimeException returnsRuntimeException()
	{
		return new RuntimeException("ROOT Exception");
	}

	private RuntimeException wrapsAndReturnsException()
	{
		return new RuntimeException("WRAPPING EXCEPTION", returnsRuntimeException());
	}

	private void throwsRuntimeException()
	{
		throw new RuntimeException("ROOT Exception");
	}

	private void wrapsAndThrowsException()
	{
		try
		{
			throwsRuntimeException();
		}
		catch (RuntimeException e)
		{
			throw new RuntimeException("WRAPPING EXCEPTION", e);
		}
	}

	private String newLine()
	{
		return System.getProperty("line.separator");
	}

	@Test
	public void enkelvoudigeExceptie()
	{
		NullPointerException e = returnsNullPointerException();
		ExceptionUtil.printStackTrace(pw, e);
		String expected =
			"java.lang.NullPointerException"
				+ newLine()
				+ "\tat nl.topicus.cobra.util.ExceptionUtilTest.returnsNullPointerException(ExceptionUtilTest.java:25)";
		Assert.assertEquals(expected, sw.toString().substring(0, expected.length()));
	}

	@Test
	public void returnedWrappedException()
	{
		ExceptionUtil.printStackTrace(pw, wrapsAndReturnsException());

		String expected =
			"java.lang.RuntimeException: ROOT Exception"
				+ newLine()
				+ "\tat nl.topicus.cobra.util.ExceptionUtilTest.returnsRuntimeException(ExceptionUtilTest.java:30)\n"
				+ "Wrapped by java.lang.RuntimeException: WRAPPING EXCEPTION"
				+ newLine()
				+ "\tat nl.topicus.cobra.util.ExceptionUtilTest.wrapsAndReturnsException(ExceptionUtilTest.java:35)\n"
				+ "\tat nl.topicus.cobra.util.ExceptionUtilTest.returnedWrappedException(ExceptionUtilTest.java:75)";
		Assert.assertEquals(expected, sw.toString().substring(0, expected.length()));
	}

	@Test
	public void caughtAndWrappedException()
	{
		try
		{
			wrapsAndThrowsException();
		}
		catch (RuntimeException e)
		{
			ExceptionUtil.printStackTrace(pw, e);
		}
		String expected =
			"java.lang.RuntimeException: ROOT Exception"
				+ newLine()
				+ "\tat nl.topicus.cobra.util.ExceptionUtilTest.throwsRuntimeException(ExceptionUtilTest.java:40)\n"
				+ "\tat nl.topicus.cobra.util.ExceptionUtilTest.wrapsAndThrowsException(ExceptionUtilTest.java:47)\n"
				+ "Wrapped by java.lang.RuntimeException: WRAPPING EXCEPTION"
				+ newLine()
				+ "\tat nl.topicus.cobra.util.ExceptionUtilTest.wrapsAndThrowsException(ExceptionUtilTest.java:51)\n"
				+ "\tat nl.topicus.cobra.util.ExceptionUtilTest.caughtAndWrappedException(ExceptionUtilTest.java:93)\n";
		Assert.assertEquals(expected, sw.toString().substring(0, expected.length()));
	}
}
