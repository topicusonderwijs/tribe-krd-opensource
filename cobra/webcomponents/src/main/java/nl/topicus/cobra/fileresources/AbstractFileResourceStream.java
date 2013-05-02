/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.fileresources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author loite
 */
public abstract class AbstractFileResourceStream implements IResourceStream
{
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = LoggerFactory.getLogger(AbstractFileResourceStream.class);

	public static final String UTF_8 = "UTF-8";

	public static final String ISO_8859_1 = "ISO-8859-1";

	private final Date exportDatumTijd;

	private final String entiteit;

	private final File file;

	private Long medewerkerId;

	private transient FileInputStream inputStream;

	private Locale locale;

	private String exportCharsetFormat = ISO_8859_1;

	public AbstractFileResourceStream(File file, Date exportDatumTijd, String entiteit)
	{
		this.file = file;
		this.exportDatumTijd = exportDatumTijd;
		this.entiteit = entiteit;
		this.medewerkerId = 0L;
	}

	public AbstractFileResourceStream(File file, Long medewerkerId, Date exportDatumTijd,
			String entiteit)
	{
		this(file, exportDatumTijd, entiteit);
		this.medewerkerId = medewerkerId == null ? 0L : medewerkerId;
	}

	/**
	 * @see org.apache.wicket.util.resource.IResourceStream#getContentType()
	 */
	public abstract String getContentType();

	/**
	 * @see org.apache.wicket.util.resource.IResourceStream#length()
	 */
	public long length()
	{
		return file.length();
	}

	/**
	 * @see org.apache.wicket.util.resource.IResourceStream#getInputStream()
	 */
	public InputStream getInputStream() throws ResourceStreamNotFoundException
	{
		if (inputStream == null)
		{
			try
			{
				inputStream = new FileInputStream(file);
			}
			catch (FileNotFoundException e)
			{
				LOG.error(e.getMessage(), e);
				throw new ResourceStreamNotFoundException(e);
			}
		}
		return inputStream;
	}

	/**
	 * @see org.apache.wicket.util.resource.IResourceStream#close()
	 */
	public void close() throws IOException
	{
		if (inputStream != null)
		{
			inputStream.close();
			inputStream = null;
		}
	}

	/**
	 * @see org.apache.wicket.util.resource.IResourceStream#getLocale()
	 */
	public Locale getLocale()
	{
		return locale;
	}

	/**
	 * @see org.apache.wicket.util.resource.IResourceStream#setLocale(java.util.Locale)
	 */
	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	/**
	 * @see org.apache.wicket.util.watch.IModifiable#lastModifiedTime()
	 */
	public Time lastModifiedTime()
	{
		return Time.valueOf(file.lastModified());
	}

	/**
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		// Sluit de inputstream als deze onverhoopt nog open zou zijn.
		close();
		// Verwijder het tijdelijke bestand.
		if (file != null)
		{
			file.delete();
		}
	}

	/**
	 * @return Bestandsnaam van de resource stream (het temp-bestand)
	 */
	public String getFileName()
	{
		return file.getName();
	}

	/**
	 * @return De datum/tijd waarop de export is begonnen.
	 */
	public Date getExportDatumTijd()
	{
		return exportDatumTijd;
	}

	/**
	 * @return De naam van de entiteit die geexporteerd wordt.
	 */
	public String getEntiteit()
	{
		return entiteit;
	}

	public Long getMedewerkerId()
	{
		return medewerkerId;
	}

	public void setMedewerkerId(Long medewerkerId)
	{
		this.medewerkerId = medewerkerId;
	}

	public String getExportCharsetFormat()
	{
		return exportCharsetFormat;
	}

	public void setExportCharsetFormat(String exportCharsetFormat)
	{
		this.exportCharsetFormat = exportCharsetFormat;
	}
}
