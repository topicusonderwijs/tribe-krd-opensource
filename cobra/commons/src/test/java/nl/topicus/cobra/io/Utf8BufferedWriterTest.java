package nl.topicus.cobra.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class Utf8BufferedWriterTest
{
	@Test
	public void testWrite() throws IOException
	{
		String filename = "target/Utf8BufferedWriterTest.txt";

		String text = "UTF-8 Test zonder byte order mark";

		writeString(createWriter(filename, 512), false, text);

		FileInputStream in = new FileInputStream(filename);
		byte[] actual = new byte[text.length()];
		in.read(actual);
		in.close();

		Assert.assertArrayEquals(text.getBytes(), actual);
	}

	@Test
	public void testWriteByteOrderMark() throws IOException
	{
		String filename = "target/Utf8BufferedWriterTest_bom.txt";

		String text = "UTF-8 Test met byte order mark";

		writeString(createWriter(filename), true, text);

		FileInputStream in = new FileInputStream(filename);
		byte[] actual = new byte[3];
		in.read(actual, 0, 3);
		byte[] actualText = new byte[text.length()];
		in.read(actualText);
		in.close();

		Assert.assertArrayEquals(Utf8BufferedWriter.BOM, actual);

		Assert.assertArrayEquals(text.getBytes(), actualText);
	}

	private Utf8BufferedWriter createWriter(String filename) throws IOException
	{
		File file = new File(filename);
		return new Utf8BufferedWriter(file);
	}

	private Utf8BufferedWriter createWriter(String filename, int buffersize) throws IOException
	{
		File file = new File(filename);
		return new Utf8BufferedWriter(file, buffersize);
	}

	private void writeString(Utf8BufferedWriter writer, boolean bom, String line)
			throws IOException
	{
		if (bom)
			writer.writeByteOrderMark();
		writer.write(line);
		writer.close();
	}
}
