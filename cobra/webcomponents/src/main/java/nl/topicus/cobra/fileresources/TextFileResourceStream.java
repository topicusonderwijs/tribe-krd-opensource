/*
 * Copyright (c) 2005-2008, Topicus B.V.
 * All rights reserved
 */
package nl.topicus.cobra.fileresources;

import java.io.File;
import java.util.Date;

import org.apache.wicket.util.resource.IResourceStream;

/**
 * Resourcestream foor het openen van gewone textbestanden
 * 
 * @author steenbeeke
 */
public class TextFileResourceStream extends AbstractFileResourceStream
{
	private static final long serialVersionUID = 1L;

	public TextFileResourceStream(File file, Long medewerkerId, Date exportDatumTijd,
			String entiteit)
	{
		super(file, medewerkerId, exportDatumTijd, entiteit);
	}

	public TextFileResourceStream(File file, Date exportDatumTijd, String entiteit)
	{
		super(file, exportDatumTijd, entiteit);
	}

	/**
	 * @see IResourceStream#getContentType()
	 */
	@Override
	public String getContentType()
	{
		return "text/plain; charset=" + getExportCharsetFormat();
	}

}
