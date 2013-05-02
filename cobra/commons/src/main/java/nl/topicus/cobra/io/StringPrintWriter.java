package nl.topicus.cobra.io;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p>
 * A PrintWriter that maintains a String as its backing store.
 * </p>
 * 
 * <p>
 * Usage:
 * 
 * <pre>
 * StringPrintWriter out = new StringPrintWriter();
 * printTo(out);
 * System.out.println(out.getString());
 * </pre>
 * 
 * </p>
 * 
 * @author Alex Chaffee
 * @author Scott Stanchfield
 * @author Gary D. Gregory
 * @since 2.0
 */
public class StringPrintWriter extends PrintWriter
{

	/**
	 * Constructs a new instance.
	 */
	public StringPrintWriter()
	{
		super(new StringWriter());
	}

	/**
	 * Constructs a new instance using the specified initial string-buffer size.
	 * 
	 * @param initialSize
	 *            an int specifying the initial size of the buffer.
	 */
	public StringPrintWriter(int initialSize)
	{
		super(new StringWriter(initialSize));
	}

	/**
	 * <p>
	 * Since toString() returns information *about* this object, we want a separate method
	 * to extract just the contents of the internal buffer as a String.
	 * </p>
	 * 
	 * @return the contents of the internal string buffer
	 */
	public String getString()
	{
		flush();
		return ((StringWriter) this.out).toString();
	}

}
