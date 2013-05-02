/*
 * Copyright (c) 2005-2009, Topicus b.v.
 * All rights reserved
 */
package nl.topicus.cobra.fileresources;

import java.io.File;
import java.util.Date;

import org.apache.wicket.util.resource.IResourceStream;

/**
 * @author loite
 */
public class CsvFileResourceStream extends AbstractFileResourceStream
{
	private static final long serialVersionUID = 1L;

	public CsvFileResourceStream(File file, Date exportDatumTijd, String entiteit)
	{
		super(file, exportDatumTijd, entiteit);
	}

	/**
	 * @see IResourceStream#getContentType()
	 */
	@Override
	public String getContentType()
	{
		return "text/csv; charset=" + getExportCharsetFormat();
	}
}
