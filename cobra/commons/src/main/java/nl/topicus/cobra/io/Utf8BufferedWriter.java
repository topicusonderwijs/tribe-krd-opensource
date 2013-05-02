/*
 * Copyright (c) 2005-2008, Topicus B.V.
 * All rights reserved
 */
package nl.topicus.cobra.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Een UTF-8 encoded outputstreamwriter.
 * 
 * @author boschman
 */
public class Utf8BufferedWriter extends BufferedWriter
{
	public static byte[] BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

	/**
	 * Maakt een UTF-8 encoded buffered outputstreamwriter.
	 * 
	 * @param file
	 * @throws java.io.UnsupportedEncodingException
	 */
	public Utf8BufferedWriter(File file) throws IOException
	{
		super(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	}

	/**
	 * Maakt een UTF-8 encoded buffered outputstreamwriter met een buffer van de opgegeven
	 * lengte.
	 * 
	 * @param file
	 * @param size
	 * @throws IOException
	 */
	public Utf8BufferedWriter(File file, int size) throws IOException
	{
		super(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"), size);
	}

	/**
	 * Schrijft de unicode byte-order mark (0xEF 0xBB 0xBF), dit moet aan het begin van de
	 * stream geschreven worden!
	 * 
	 * @throws IOException
	 */
	public void writeByteOrderMark() throws IOException
	{
		write(new String(BOM, "UTF-8"));
	}
}
