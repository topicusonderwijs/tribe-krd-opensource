package nl.topicus.cobra.util;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Klasse voor het beheren van resources zoals
 * 
 * @link{java.io.Reader of
 * @link{java.io.Writer . Voorbeelden van gebruik zijn:
 * 
 *                      <pre>
 * Reader reader = null;
 * try
 * {
 * 	reader = new FileReader(&quot;file.txt&quot;);
 * 	// doe iets
 * }
 * finally
 * {
 * 	ResourceUtil.closeQuietly(reader);
 * }
 * </pre>
 * 
 * @see java.io.Closeable
 */
public final class ResourceUtil
{
	/**
	 * Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(ResourceUtil.class);

	/**
	 * Sluit de closeable resource netjes af, en logt een eventuele exceptie maar gooit
	 * deze exceptie niet verder. Gebruik deze methode binnen try - finally blokken voor
	 * het afsluiten van bestanden en andere resources.
	 * 
	 * @param closeable
	 *            het ding dat afgesloten moet worden (een Writer, Reader of zo).
	 */
	public static void closeQuietly(Closeable closeable)
	{
		// doe een null check, om zo resources die niet aangemaakt konden worden toch op
		// een standaard manier te kunnen sluiten in een finally blok.
		if (closeable == null)
		{
			log.debug("Poging tot sluiten van een null resource");
			return;
		}
		try
		{
			log.debug("Closing resource " + closeable.getClass());
			closeable.close();
		}
		catch (IOException e)
		{
			log.warn("Exception bij het sluiten van " + closeable.getClass(), e);
		}
	}

	/**
	 * Utility constructor.
	 */
	private ResourceUtil()
	{
		// doe niets
	}

	public static void closeQuietly(ResultSet closeable)
	{
		// doe een null check, om zo resources die niet aangemaakt konden worden toch op
		// een standaard manier te kunnen sluiten in een finally blok.
		if (closeable == null)
		{
			log.debug("Poging tot sluiten van een null resource");
			return;
		}
		try
		{
			log.debug("Closing resultset");
			closeable.close();
		}
		catch (SQLException e)
		{
			log.warn("Exception bij het sluiten van " + closeable.getClass(), e);
		}
	}

	/**
	 * Sluit het statement (kan een prepared statement zijn) zonder een exception te
	 * genereren.
	 * 
	 * @param closeable
	 */
	public static void closeQuietly(Statement closeable)
	{
		// doe een null check, om zo resources die niet aangemaakt konden worden toch op
		// een standaard manier te kunnen sluiten in een finally blok.
		if (closeable == null)
		{
			log.debug("Poging tot sluiten van een null resource");
			return;
		}
		try
		{
			log.debug("Closing prepared statement");
			closeable.close();
		}
		catch (SQLException e)
		{
			log.warn("Exception bij het sluiten van " + closeable.getClass(), e);
		}
	}

	public static void closeQuietly(Connection closeable)
	{
		// doe een null check, om zo resources die niet aangemaakt konden worden toch op
		// een standaard manier te kunnen sluiten in een finally blok.
		if (closeable == null)
		{
			log.debug("Poging tot sluiten van een null resource");
			return;
		}
		try
		{
			log.debug("Closing connection");
			closeable.close();
		}
		catch (SQLException e)
		{
			log.warn("Exception bij het sluiten van " + closeable.getClass(), e);
		}
	}

	/**
	 * Rollback van een JDBC connectie, zonder dat dit andere excepties maskeert.
	 * 
	 * @param connection
	 *            de connectie waarop rollback wordt aangeroepen.
	 */
	public static void rollbackQuietly(Connection connection)
	{
		// doe een null check, om zo resources die niet aangemaakt konden worden toch op
		// een standaard manier te kunnen sluiten in een finally blok.
		if (connection == null)
		{
			log.debug("Poging tot rollback van een null resource");
			return;
		}
		try
		{
			log.debug("Rollback van connection");
			connection.rollback();
		}
		catch (SQLException e)
		{
			log.warn("Exception bij rollback van " + connection.getClass(), e);
		}
	}

	/**
	 * Leest het (tekst) bestand met de naam 'filename', te vinden ten opzichte van de
	 * clazz op het classpath. Hierbij wordt de default charset van de jvm gebruikt
	 * 
	 * @return de inhoud van het bestand als string.
	 */
	public static String readClasspathFile(Class< ? > clazz, String filename)
	{
		return readClasspathFile(clazz, filename, null);
	}

	/**
	 * Leest het (tekst) bestand met de naam 'filename', te vinden ten opzichte van de
	 * clazz op het classpath.
	 * 
	 * @return de inhoud van het bestand als string.
	 */
	public static String readClasspathFile(Class< ? > clazz, String filename, String charset)
	{
		InputStream is = null;
		InputStreamReader ir = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		try
		{
			is = clazz.getResourceAsStream(filename);
			Asserts.assertNotNull("InputStream", is);

			if (charset == null)
				ir = new InputStreamReader(is);
			else
				ir = new InputStreamReader(is, Charset.forName(charset));
			br = new BufferedReader(ir);
			int number = -1;
			char[] buffer = new char[1024];
			do
			{
				number = br.read(buffer);
				if (number >= 0)
				{
					sb.append(buffer, 0, number);
				}
			}
			while (number != -1);
		}
		catch (IOException e)
		{
			log.error("Kan " + filename + " niet lezen op het classpath relatief ten opzichte van "
				+ clazz, e);
		}
		finally
		{
			closeQuietly(br);
			closeQuietly(ir);
			closeQuietly(is);
		}
		return sb.toString();
	}

	/**
	 * @param clazz
	 * @param filename
	 * @return de bytes van het bestand
	 * @throws IOException
	 */
	public static byte[] readClassPathFileAsBytes(Class< ? > clazz, String filename)
			throws IOException
	{
		InputStream inputStream = clazz.getResourceAsStream(filename);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
		byte[] bytes = new byte[512];

		// Read bytes from the input stream in bytes.length-sized chunks and write
		// them into the output stream
		int readBytes;
		while ((readBytes = inputStream.read(bytes)) > 0)
		{
			outputStream.write(bytes, 0, readBytes);
		}

		// Convert the contents of the output stream into a byte array
		byte[] byteData = outputStream.toByteArray();

		// Close the streams
		inputStream.close();
		outputStream.close();

		return byteData;
	}

	/**
	 * Writes the input stream to the output stream. Input is done without a Reader
	 * object, meaning that the input is copied in its raw form.
	 * 
	 * @param in
	 *            The input stream
	 * @param out
	 *            The output stream
	 * @return Number of bytes copied from one stream to the other
	 * @throws IOException
	 */
	public static int copy(final InputStream in, final OutputStream out) throws IOException
	{
		final byte[] buffer = new byte[4096];
		int bytesCopied = 0;
		while (true)
		{
			int byteCount = in.read(buffer, 0, buffer.length);
			if (byteCount <= 0)
			{
				break;
			}
			out.write(buffer, 0, byteCount);
			bytesCopied += byteCount;
		}
		return bytesCopied;
	}

	/**
	 * Flushes the objectToFlush if it implements Flushable. Will log exceptions and
	 * rethrow them as RuntimeException, as these things are typically fatal.
	 * 
	 * @param objectToFlush
	 */
	public static void flush(Object objectToFlush)
	{
		if (objectToFlush instanceof Flushable)
		{
			Flushable flushable = (Flushable) objectToFlush;
			try
			{
				flushable.flush();
			}
			catch (IOException e)
			{
				log.error(e.toString(), e);
				throw new RuntimeException(e);
			}
		}
	}
}
