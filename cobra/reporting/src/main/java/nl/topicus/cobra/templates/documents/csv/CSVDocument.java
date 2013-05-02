package nl.topicus.cobra.templates.documents.csv;

import java.io.IOException;
import java.io.InputStream;

import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.documents.xls.ExcelWorkbook;
import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;

public class CSVDocument extends ExcelWorkbook
{
	@SuppressWarnings("hiding")
	public static final String MIME_TYPE = "text/csv";

	@Override
	public String getContentType()
	{
		return MIME_TYPE;
	}

	@Override
	public DocumentTemplateType getType()
	{
		return DocumentTemplateType.CSV;
	}

	private void read(InputStream stream) throws IOException
	{
		workbook = new CSVWorkbook(stream);
		currentPart = new SheetRange(0, workbook.getNumberOfSheets() - 1);
	}

	public static ExcelWorkbook createDocument(InputStream inStream) throws TemplateException
	{
		CSVDocument document = new CSVDocument();

		try
		{
			document.read(inStream);
		}
		catch (IOException e)
		{
			throw new TemplateCreationException("CSV template kon niet worden gemaakt.", e);
		}

		return document;
	}

}
