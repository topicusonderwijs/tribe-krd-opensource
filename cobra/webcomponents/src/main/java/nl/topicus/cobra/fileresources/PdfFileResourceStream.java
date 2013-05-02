/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.fileresources;

import java.io.File;
import java.util.Date;

/**
 * @author loite
 */
public class PdfFileResourceStream extends AbstractFileResourceStream
{
	private static final long serialVersionUID = 1L;

	public PdfFileResourceStream(File file, Date exportDatumTijd, String entiteit)
	{
		super(file, exportDatumTijd, entiteit);
	}

	/**
	 * @see nl.topicus.cobra.fileresources.AbstractFileResourceStream#getContentType()
	 */
	@Override
	public String getContentType()
	{
		return "application/pdf";
	}

}
