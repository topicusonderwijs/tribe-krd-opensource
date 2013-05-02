package nl.topicus.cobra.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Utility methoden en interfaces om met ZIP files te kunnen werken zonder zelf voor
 * correcte resource allocatie te moeten zorgen.
 */
public class ZipUtils
{
	/**
	 * Iterator interface voor {@link ZipUtils#itereer(InputStream, ZipIterator)}.
	 * Implementeer deze interface om voor elk element in een ZIP bestand aangeroepen te
	 * worden.
	 */
	public interface ZipIterator
	{
		public void process(ZipInputStream stream, ZipEntry entry) throws IOException,
				InterruptedException;
	}

	/**
	 * Itereert over een ZIP file gerepresenteerd door <tt>is</tt>. Voert voor elk element
	 * (ook directories) de iterator uit.
	 */
	public static void itereer(InputStream is, ZipIterator iterator) throws IOException,
			InterruptedException
	{
		ZipInputStream zip = null;
		try
		{
			zip = new ZipInputStream(is);
			ZipEntry entry;
			while ((entry = zip.getNextEntry()) != null)
			{
				iterator.process(zip, entry);
			}
		}
		finally
		{
			ResourceUtil.closeQuietly(zip);
		}
	}

	/**
	 * Leest de entry in een byte array in. Directories worden als <code>null</code>
	 * teruggegeven.
	 */
	public static byte[] getEntry(ZipInputStream zip, ZipEntry entry) throws IOException
	{
		if (entry.isDirectory())
		{
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] buffer = new byte[32768];
		int bytesRead = 0;

		while (zip.available() > 0 && (bytesRead = zip.read(buffer, 0, buffer.length)) > 0)
		{
			baos.write(buffer, 0, bytesRead);
		}
		return baos.toByteArray();
	}
}
