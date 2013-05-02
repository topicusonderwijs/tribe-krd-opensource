/*
 * Copyright (c) 2005-2008, Topicus B.V.
 * All rights reserved
 */
package nl.topicus.cobra.fileresources;

import java.io.File;
import java.util.Date;

/**
 * @author roeloffzen
 */
public class Excel2007FileResourceStream extends AbstractFileResourceStream
{
	private static final long serialVersionUID = 1L;

	public Excel2007FileResourceStream(File file, Date exportDatumTijd, String entiteit)
	{
		super(file, exportDatumTijd, entiteit);
	}

	/**
	 * @see org.apache.wicket.util.resource.IResourceStream#getContentType()
	 */
	@Override
	public String getContentType()
	{
		return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	}

}
