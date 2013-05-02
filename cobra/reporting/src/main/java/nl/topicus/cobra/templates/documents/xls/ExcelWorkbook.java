/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.templates.documents.xls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.documents.DocumentTemplate;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.documents.xls.ExcelField.Mode;
import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.monitors.DocumentTemplateProgressMonitor;
import nl.topicus.cobra.templates.resolvers.FieldResolver;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel-bestand als document template. Bij mergen worden velden zoals "
 * <code>{deelnemer.persoon.volledigeNaam}</code>" gesubstitueerd. Als de fieldresolver
 * een lijst terug geeft, worden ook onder- of naastliggende cellen gevuld.
 * <p>
 * Er is support voor meerdere worksheets. In dat geval moet v&oacute;&oacute;r de eerste
 * merge de <code>newString</code> methode aangeroepen worden.
 * 
 * @author Laurens Hop
 */
public class ExcelWorkbook implements DocumentTemplate
{
	protected Workbook workbook;

	private DocumentTemplateProgressMonitor monitor;

	/**
	 * default = true, in sommige gevallen wilt de gebruiker misschien de mergefield info
	 * niet meer hebben.
	 */
	private boolean keepMergeFields = KEEP_MERGE_FIELDS_DEFAULT;

	public static boolean KEEP_MERGE_FIELDS_DEFAULT = true;

	protected static class SheetRange
	{
		private int first;

		private int last;

		public SheetRange(int f, int l)
		{
			first = f;
			last = l;
		}
	}

	/**
	 * De sheets die met de volgende merge-actie zullen worden gemerged.
	 */
	protected SheetRange currentPart;

	/**
	 * De originele sheets die bij iedere aanroep van newPart() worden gekopieerd.
	 */
	private SheetRange originalPart;

	private OutputStream outStream;

	/**
	 * {
	 */
	public static final String FIELD_BEGIN = "{";

	/**
	 * }
	 */
	public static final String FIELD_END = "}";

	/**
	 * \{([^\{\}]*)\}
	 */
	public static final Pattern FIELD_REGEX =
		Pattern.compile("\\" + FIELD_BEGIN + "([^\\" + FIELD_BEGIN + "\\" + FIELD_END + "]*)\\"
			+ FIELD_END);

	/**
	 * @param sheet
	 * @return true indien sheet alleen lege cellen bevat
	 */
	@SuppressWarnings("unchecked")
	private boolean isSheetEmpty(Sheet sheet)
	{
		boolean empty = true;
		for (Iterator rit = sheet.rowIterator(); empty && rit.hasNext();)
		{
			Row row = (Row) rit.next();
			for (Iterator cit = row.cellIterator(); empty && cit.hasNext();)
			{
				Cell cell = (Cell) cit.next();
				empty &= cell.getCellType() == Cell.CELL_TYPE_BLANK;
			}
		}
		return empty;
	}

	/**
	 * Leest een Excel-bestand in.
	 * 
	 * @see nl.topicus.cobra.templates.documents.DocumentTemplate#read(java.io.InputStream)
	 */
	private void read(InputStream stream) throws IOException
	{
		workbook = new HSSFWorkbook(stream);
		currentPart = new SheetRange(0, workbook.getNumberOfSheets() - 1);
	}

	@Override
	public List<FieldInfo> getFieldInfo(FieldResolver resolver)
	{
		List<FieldInfo> info = new ArrayList<FieldInfo>();
		for (int i = currentPart.first; i <= currentPart.last; i++)
		{
			addFieldInfo(info, workbook.getSheetAt(i), resolver);
		}
		return info;
	}

	private void addFieldInfo(List<FieldInfo> info, Sheet sheet, FieldResolver resolver)
	{
		List<ExcelField> fields = extractFields(sheet);

		for (ExcelField field : fields)
		{
			FieldInfo fieldInfo = resolver.getInfo(field.getName());
			if (fieldInfo != null)
				info.add(fieldInfo);
		}
	}

	private List<ExcelField> extractFields(Sheet sheet)
	{
		List<ExcelField> fields = extractCellFields(sheet);

		// de header en footer
		extractHeaderAndFooter(sheet, fields);

		// de naam van de sheet
		int sheetIndex = workbook.getSheetIndex(sheet);
		// indien kopie, zoek originele sheet voor de originele naam
		if (originalPart != null)
			sheetIndex = sheetIndex - currentPart.first + originalPart.first;
		String sheetName = workbook.getSheetName(sheetIndex);
		Matcher matcher = FIELD_REGEX.matcher(sheetName);
		if (matcher.find())
		{
			// TODO: als in de naam meerdere velden voorkomen, wordt alleen de eerste
			// gemerged. Probleem is dat je pas na het mergen van alle velden weet wat de
			// uiteindelijke sheet naam wordt en die uiteindelijke naam moet uniek zijn.
			ExcelField field = new ExcelField(matcher.group(1), workbook, sheet, sheetName);
			fields.add(field);
		}

		return fields;
	}

	private void extractCellFields(Row row, List<ExcelField> fields)
	{
		for (Cell cell : row)
		{
			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
			{
				RichTextString str = cell.getRichStringCellValue();
				String value = str.getString();
				extractField(value, cell.getSheet(), row, cell, fields);
			}
		}
	}

	private List<ExcelField> extractCellFields(Row row)
	{
		List<ExcelField> fields = new ArrayList<ExcelField>();
		extractCellFields(row, fields);
		return fields;
	}

	private List<ExcelField> extractCellFields(Sheet sheet)
	{
		List<ExcelField> fields = new ArrayList<ExcelField>();

		for (Row row : sheet)
			extractCellFields(row, fields);

		return fields;
	}

	private void extractField(String valueParam, Sheet sheet, Row row, Cell cell,
			List<ExcelField> fields)
	{
		if (valueParam != null)
		{
			String value = valueParam.trim();
			Matcher matcher = FIELD_REGEX.matcher(value);
			while (matcher.find())
			{
				Mode mode = Mode.FILL_EMPTY;
				// Zodra de cel meer tekst bevat dan dit ene veld, dan schakelen we over
				// naar paragraph mode, waarbij een de velden dmv search-and-replace
				// worden gevuld en de formatting opties van de cel niet kunnen worden
				// gebruikt om te formatten.
				if (!matcher.group(0).equals(value))
					mode = Mode.PARAGRAPH;

				fields.add(new ExcelField(matcher.group(1), workbook, sheet, row, cell, mode));
			}
		}
	}

	private void extractHeaderAndFooter(Sheet sheet, List<ExcelField> fields)
	{
		Matcher matcher;
		Header header = sheet.getHeader();
		if (header != null)
		{
			matcher =
				FIELD_REGEX.matcher(header.getLeft() + header.getCenter() + header.getRight());
			while (matcher.find())
			{
				ExcelField field = new ExcelField(matcher.group(1), workbook, sheet, header);
				fields.add(field);
			}
		}
		Footer footer = sheet.getFooter();
		if (footer != null)
		{
			matcher =
				FIELD_REGEX.matcher(footer.getLeft() + footer.getCenter() + footer.getRight());
			while (matcher.find())
			{
				ExcelField field = new ExcelField(matcher.group(1), workbook, sheet, footer);
				fields.add(field);
			}
		}
	}

	/**
	 * Geeft het MIME type van Excel (application/vnd.ms-excel)
	 * 
	 * @see nl.topicus.cobra.templates.documents.DocumentTemplate#getContentType()
	 */
	@Override
	public String getContentType()
	{
		return MIME_TYPE;
	}

	/**
	 * MIME type
	 */
	public static String MIME_TYPE = "application/vnd.ms-excel";

	@Override
	public DocumentTemplateType getType()
	{
		return DocumentTemplateType.XLS;
	}

	@Override
	public void setOutputStream(OutputStream outStream)
	{
		this.outStream = outStream;
	}

	@Override
	public void mergeDocumentFooter(FieldResolver resolver)
	{
		// doet niks
	}

	@Override
	public void mergeDocumentHeader(FieldResolver resolver)
	{
		// doet niks
	}

	@Override
	public void writeDocumentFooter() throws TemplateException
	{
		try
		{
			if (originalPart != null)
			{
				// gooi originele sheets weg
				for (int i = originalPart.last; i >= originalPart.first; i--)
					workbook.removeSheetAt(i);
			}

			workbook.write(outStream);
		}
		catch (IOException e)
		{
			throw new TemplateCreationException("Kan Excel document niet maken", e);
		}
	}

	@Override
	public void writeDocumentHeader()
	{
		// doet niks
	}

	@Override
	public void writeSection(FieldResolver resolver)
	{
		if (originalPart == null)
		{
			/*
			 * eerste keer newPart aangeroepen, verwijder eerst de lege sheets (muv de
			 * eerste)
			 */
			for (int i = workbook.getNumberOfSheets() - 1; i > 0; i--)
				if (isSheetEmpty(workbook.getSheetAt(i)))
					workbook.removeSheetAt(i);
			originalPart = new SheetRange(0, workbook.getNumberOfSheets() - 1);
		}

		// index van de eerste te clonen nieuwe sheet
		currentPart.first = workbook.getNumberOfSheets();
		for (int i = originalPart.first; i <= originalPart.last; i++)
			workbook.cloneSheet(i);
		// index van de laatste
		currentPart.last = workbook.getNumberOfSheets() - 1;

		mergeSection(resolver);

		if (monitor != null)
			monitor.afterWriteSection();
	}

	private void mergeSection(FieldResolver resolver)
	{
		for (int i = currentPart.first; i <= currentPart.last; i++)
		{
			Sheet sheet = workbook.getSheetAt(i);
			expandPropertyLists(resolver, sheet);
			List<ExcelField> fields = extractFields(sheet);

			for (ExcelField field : fields)
			{
				field.merge(resolver);
				if (field.isShifted())
				{
					// veld heeft andere cellen opgescholven. Fields kloppen niet meer.
					// Herstart merge actie
					mergeSection(resolver);
					break;
				}
			}
		}
	}

	private Row insertRow(Sheet sheet, int rowNumber)
	{
		sheet.shiftRows(rowNumber, sheet.getLastRowNum(), 1);
		return sheet.getRow(rowNumber);
	}

	private void expandPropertyLists(FieldResolver resolver, Sheet sheet)
	{
		// Doorloop de sheet net zo lang tot er geen []-velden meer zijn
		boolean nothingFound = false;
		while (!nothingFound)
		{
			// Doorzoek de sheet op []-velden. Indien gevonden, expandeer de lijst
			// en zoek opnieuw naar overgebleven []-velden.
			boolean found = false;
			List<ExcelField> fields = extractCellFields(sheet);
			Iterator<ExcelField> iterator = fields.iterator();
			while (!found && iterator.hasNext())
			{
				ExcelField field = iterator.next();
				if (field.getName().contains("[]"))
				{
					String prop = field.getName().substring(0, field.getName().indexOf("[]"));
					Object v = resolver.resolve(prop);

					if (v != null && v instanceof Collection< ? >)
					{
						Row row = field.getCell().getRow();
						int rowNum = row.getRowNum();
						// n merge field blokken aanmaken
						int i = 0;
						for (; i < ((Collection< ? >) v).size(); i++)
						{
							Row newRow = duplicateRow(sheet, row);
							fillInListIndex(newRow, i);
						}
						// Verwijder de oorspronkelijke regel.
						sheet.removeRow(sheet.getRow(rowNum + i));
						// Omdat we nu 1 of meer regels hebben aangepast, moet de lus
						// worden afgebroken en opnieuw de velden worden bepaald.
						found = true;
					}
				}
			}
			nothingFound = !found;
		}
	}

	private void fillInListIndex(Row row, int index)
	{
		List<ExcelField> fields = extractCellFields(row);
		for (ExcelField field : fields)
		{
			String fieldname = field.getName();
			if (fieldname.contains("[]"))
				field.setName(fieldname.replace("[]", "[" + index + "]"));
		}
	}

	/**
	 * Voegt een duplicaat van deze row in vlak voor de opgegeven row en retourneert de
	 * nieuwe row.
	 */
	private Row duplicateRow(Sheet sheet, Row row)
	{
		int rowNum = row.getRowNum();
		Row newRow = insertRow(sheet, rowNum);
		// Workaround: in POI Excel implementatie verwijst row naar het oude regelnummer
		// (= nieuwe row); bij CSV implementatie schuift row wel mee.
		row = sheet.getRow(rowNum + 1);
		for (Cell cell : row)
		{
			int type = cell.getCellType();
			Cell newCell = newRow.createCell(cell.getColumnIndex(), type);
			if (type != Cell.CELL_TYPE_BLANK)
				newCell.setCellValue(cell.getStringCellValue());
			newCell.setCellStyle(cell.getCellStyle());
		}
		return newRow;
	}

	public static ExcelWorkbook createDocument(InputStream inStream) throws TemplateException
	{
		ExcelWorkbook workbook = new ExcelWorkbook();

		try
		{
			workbook.read(inStream);
		}
		catch (IOException e)
		{
			throw new TemplateCreationException("Excel template kon niet worden gemaakt.", e);
		}

		return workbook;
	}

	@Override
	public void writePageFooter()
	{
		// does nothing in xls
	}

	@Override
	public void writePageHeader()
	{
		// does nothing in xls
	}

	@Override
	public void setProgressMonitor(DocumentTemplateProgressMonitor monitor)
	{
		this.monitor = monitor;
	}

	@Override
	public OutputStream getOutputStream()
	{
		return outStream;
	}

	public boolean isKeepMergeFields()
	{
		return keepMergeFields;
	}

	public void setKeepMergeFields(boolean keepMergeFields)
	{
		this.keepMergeFields = keepMergeFields;
	}

	@Override
	public boolean hasSections()
	{
		return true;
	}
}
