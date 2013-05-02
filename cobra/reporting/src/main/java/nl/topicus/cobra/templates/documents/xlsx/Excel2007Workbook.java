package nl.topicus.cobra.templates.documents.xlsx;

import java.io.IOException;
import java.io.InputStream;

import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.documents.xls.ExcelWorkbook;
import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel2007Workbook extends ExcelWorkbook
{
	@SuppressWarnings("hiding")
	public static String MIME_TYPE =
		"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	@Override
	public String getContentType()
	{
		return MIME_TYPE;
	}

	@Override
	public DocumentTemplateType getType()
	{
		return DocumentTemplateType.XLSX;
	}

	/**
	 * Leest een Excel-bestand in.
	 * 
	 * @see nl.topicus.cobra.templates.documents.DocumentTemplate#read(java.io.InputStream)
	 */
	private void read(InputStream stream) throws IOException
	{
		workbook = new XSSFWorkbook(stream);
		currentPart = new SheetRange(0, workbook.getNumberOfSheets() - 1);
	}

	public static ExcelWorkbook createDocument(InputStream inStream) throws TemplateException
	{
		Excel2007Workbook workbook = new Excel2007Workbook();

		try
		{
			workbook.read(inStream);
		}
		catch (IOException e)
		{
			throw new TemplateCreationException("Excel2007 template kon niet worden gemaakt.", e);
		}

		return workbook;
	}
}
