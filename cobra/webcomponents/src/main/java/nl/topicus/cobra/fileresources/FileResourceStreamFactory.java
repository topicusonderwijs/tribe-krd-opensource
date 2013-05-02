/*
 * Copyright (c) 2005-2008, Topicus B.V.
 * All rights reserved
 */
package nl.topicus.cobra.fileresources;

import java.io.File;
import java.util.Date;

/**
 * Factory welke een juiste subclass terug geeft aan de hand van de bestandsnaam.
 * 
 * @author roeloffzen (vocus)
 * @uathor hoeve
 */
public class FileResourceStreamFactory
{
	/**
	 * @param file
	 * @param lastModified
	 * @param entiteit
	 * @return The correct subclass of AbstractFileResourceStream for the provided file
	 */
	public static AbstractFileResourceStream getFileResourceStream(File file, Long medewerkerId,
			Date lastModified, String entiteit)
	{
		AbstractFileResourceStream stream = null;

		if (file.getName().endsWith(".pdf"))
			stream = new PdfFileResourceStream(file, lastModified, entiteit);
		else if (file.getName().endsWith(".csv"))
			stream = new CsvFileResourceStream(file, lastModified, entiteit);
		else if (file.getName().endsWith(".txt"))
			stream = new TextFileResourceStream(file, lastModified, entiteit);
		else if (file.getName().endsWith(".rtf"))
			stream = new RTFFileResourceStream(file, lastModified, entiteit);
		else if (file.getName().endsWith(".xlsx"))
			stream = new Excel2007FileResourceStream(file, lastModified, entiteit);
		else if (file.getName().endsWith(".xls"))
			stream = new ExcelFileResourceStream(file, lastModified, entiteit);
		else if (file.getName().endsWith(".docx"))
			stream = new Word2007FileResourceStream(file, lastModified, entiteit);

		if (stream != null)
			stream.setMedewerkerId(medewerkerId);

		return stream;
	}

	/**
	 * @param file
	 * @param lastModified
	 * @param entiteit
	 * @return The correct subclass of AbstractFileResourceStream for the provided file
	 */
	public static AbstractFileResourceStream getFileResourceStream(File file, Date lastModified,
			String entiteit)
	{
		return FileResourceStreamFactory.getFileResourceStream(file, null, lastModified, entiteit);
	}
}
