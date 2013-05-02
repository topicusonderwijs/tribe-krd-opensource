/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.templates.documents.xls;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.documents.Field;
import nl.topicus.cobra.templates.resolvers.FieldResolver;
import nl.topicus.cobra.util.StringUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.*;

/**
 * Excel field. Houdt een referentie bij naar de worksheet, rij en cel waar het veld
 * stond, zodat bij mergen de cel gevuld kan worden. Bij lijsten worden ook onder- of
 * naastliggende velden gevuld.
 * 
 * @author Laurens Hop
 */
public class ExcelField implements Field
{
	private String name;

	private String numberFormat;

	private String dateFormat;

	private String textFormat;

	private Object result;

	private Workbook workbook;

	private Sheet sheet;

	private Header header;

	private Footer footer;

	private String sheetName;

	private Row row;

	private Cell cell;

	private CellStyle style;

	private boolean shifted = false;

	/**
	 * Orientatie voor het vullen van lijsten: onder elkaar of naast elkaar
	 */
	private enum Orientation
	{
		/**
		 * Onder elkaar
		 */
		VERTICAL,
		/**
		 * Naast elkaar
		 */
		HORIZONTAL;
	}

	private Orientation orientation = Orientation.VERTICAL;

	/**
	 * Mode voor het vullen van lijsten: vullen (default), overschrijven of invoegen
	 */
	public enum Mode
	{
		/**
		 * Overschrijven
		 */
		OVERWRITE,
		/**
		 * Invoegen
		 */
		INSERT,
		/**
		 * Lege cellen vullen
		 */
		FILL_EMPTY,
		/**
		 * Find-and-replace van veld binnen de cel-tekst
		 */
		PARAGRAPH,
		/**
		 * Is gelijk aan OVERWRITE, maar bij toevoegen van nieuwe rij of kolom: dupliceer
		 * de gehele rij of kolom (inclusief vaste velden)
		 */
		DUPLICATE_ROWS;
	}

	private Mode mode = Mode.FILL_EMPTY;

	/**
	 * Maximaal aantal cellen die overschreven mogen worden (0 = onbeperkt)
	 */
	private int maxOverwriteVertical = 0;

	private int maxOverwriteHorizontal = 0;

	/**
	 * Vult een cel met een waarde
	 * 
	 * @param sheet
	 *            huidige worksheet
	 * @param row
	 *            rij van de huidige cel
	 * @param currentCell
	 *            huidige cel
	 * @param value
	 *            waarde (indien Iterable, worden de onderliggende of naastliggende cellen
	 *            ook gevuld)
	 * @param orientation
	 *            als value Iterable is, geeft deze parameter aan of de onderliggende
	 *            (VERTICAL) of de naastliggende (HORIZONTAL) cellen zullen worden gevuld
	 */
	@SuppressWarnings("unchecked")
	private void setCellValue(Row currentRow, Cell currentCell, Object value,
			Orientation orientation)
	{
		if (mode.equals(Mode.PARAGRAPH) && !(value instanceof String))
		{
			setCellValue(currentRow, currentCell, formatAsString(value), orientation);
		}
		else if (value instanceof Number)
		{
			if (style != null)
				currentCell.setCellStyle(style);
			currentCell.setCellValue(((Number) value).doubleValue());
		}
		else if (value instanceof Date)
		{
			if (style != null)
				currentCell.setCellStyle(style);
			currentCell.setCellValue((Date) value);
		}
		else if (value instanceof Boolean)
		{
			setCellValue(currentRow, currentCell, ((Boolean) value).booleanValue() ? "J" : "N",
				orientation);
		}
		else if (value instanceof Iterable)
		{
			fillCells(currentRow, currentCell, (Iterable) value, orientation);
		}
		else if (value instanceof Object[])
		{
			fillCells(currentRow, currentCell, Arrays.asList((Object[]) value), orientation);
		}
		else if (value == null)
		{
			currentCell.setCellType(Cell.CELL_TYPE_BLANK);
		}
		else
		{
			String text = formatAsString(value);
			if (mode == Mode.PARAGRAPH)
			{
				String current = currentCell.getRichStringCellValue().getString();
				String fieldText = ExcelWorkbook.FIELD_BEGIN + name + ExcelWorkbook.FIELD_END;
				currentCell.setCellValue(current.replace(fieldText, text));
			}
			else
				currentCell.setCellValue(text);
		}
	}

	/**
	 * Wijzigt de naam van dit veld. Als er nog geen resultaat is berekend, wordt de
	 * inhoud van de aan dit veld gekoppelde cell, header of footer eveneens gewijzigd.
	 */
	public void setName(String name)
	{
		if (result == null)
		{
			if (header != null)
				replaceHeader(this.name, name);
			else if (footer != null)
				replaceFooter(this.name, name);
			else
			{
				String current = cell.getStringCellValue();
				cell.setCellValue(current.replace(this.name, name));
			}
		}
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	private String formatAsString(Object value)
	{
		if (value instanceof Number)
		{
			return value.toString();
		}
		else if (value instanceof Date)
		{
			return new SimpleDateFormat("dd-MM-yyyy").format((Date) value);
		}
		else if (value instanceof Iterable)
		{
			StringBuilder builder = new StringBuilder();
			Iterator<Object> it = ((Iterable) value).iterator();
			boolean first = true;
			while (it.hasNext())
			{
				if (!first)
					builder.append(", ");
				else
					first = false;
				builder.append(formatAsString(it.next()));
			}
			return builder.toString();
		}
		else if (value instanceof Object[])
		{
			return formatAsString(Arrays.asList((Object[]) value));
		}
		else if (value == null)
		{
			return "";
		}
		else
		{
			String text = value.toString();
			if (StringUtil.isNotEmpty(textFormat))
				for (String format : textFormat.split("\\s"))
				{
					if ("Upper".equalsIgnoreCase(format))
						text = text.toUpperCase();
					if ("Lower".equalsIgnoreCase(format))
						text = text.toLowerCase();
					if ("FirstCap".equalsIgnoreCase(format))
						text = StringUtil.firstCharUppercase(text);
					if ("Caps".equalsIgnoreCase(format))
						text = StringUtil.firstCharUppercaseOfEachWord(text);
					if (StringUtil.isNumeric(format))
					{
						int maxlength = Integer.parseInt(format);
						if (text.length() > maxlength)
							text = text.substring(0, maxlength);
					}
				}
			return text;
		}
	}

	/**
	 * Vult een of meer cellen met waarden uit een lijst
	 * 
	 * @param values
	 *            lijst met waarden. Indien een waarde zelf ook weer een lijst (Iterable)
	 *            is, zal het resultaat een 2D-matrix zijn.
	 * @param currentOrientation
	 *            horizontaal of verticaal
	 */
	private void fillCells(Row currentRowParam, Cell currentCellParam, Iterable<Object> values,
			Orientation currentOrientation)
	{
		Row currentRow = currentRowParam;
		Cell currentCell = currentCellParam;
		int columnNumber = currentCell.getColumnIndex();
		int rowNumber = currentRow.getRowNum();

		int startColumnNumber = columnNumber;
		int startRowNumber = rowNumber;

		boolean first = true;
		CellStyle firstCellStyle = null;

		// workaround om bug in Row.addCell heen (lastCellNum wordt niet geupdate bij
		// schuiven)
		int currentLastCellNum = currentRow.getLastCellNum() - 1;

		for (Object value : values)
		{
			// In insert mode rijen shiften
			if (mode == Mode.INSERT && !first && orientation == Orientation.VERTICAL)
			{
				currentRow = insertRow(rowNumber);
			}
			else
			{
				Row original = currentRow;
				currentRow = sheet.getRow(rowNumber);
				if (currentRow == null)
				{
					currentRow = sheet.createRow(rowNumber);
					if (mode == Mode.DUPLICATE_ROWS)
						duplicateRow(currentRow, original);
				}

			}

			// In insert mode kolommen shiften
			// TODO: stijlen overnemen van eerste kolom
			// FIXME: workaround werkt nog niet
			if (mode == Mode.INSERT && !first && orientation == Orientation.HORIZONTAL)
			{
				for (int i = currentLastCellNum; i >= columnNumber; i--)
				{
					Cell shiftCell = currentRow.getCell(i);
					if (shiftCell != null && currentRow instanceof HSSFRow)
						((HSSFRow) currentRow).moveCell((HSSFCell) shiftCell, (short) (i + 1));
				}
				shifted = true;
				currentLastCellNum++;
				currentCell = currentRow.createCell(columnNumber);
			}
			else
			{
				// Hier is alvast het kopieren van de cell style toegevoegd
				currentCell = currentRow.getCell(columnNumber);
				if (currentCell != null && first)
					firstCellStyle = currentCell.getCellStyle();
				if (currentCell == null)
				{
					currentCell = currentRow.createCell(columnNumber);
					if (firstCellStyle != null)
						currentCell.setCellStyle(firstCellStyle);
				}

			}

			// Switch orientatie zodat lijsten van lijsten kunnen worden gevuld
			// (2D-matrix)
			Orientation newOrientation;
			if (currentOrientation == Orientation.HORIZONTAL)
				newOrientation = Orientation.VERTICAL;
			else
				newOrientation = Orientation.HORIZONTAL;

			boolean blank = currentCell.getCellType() == Cell.CELL_TYPE_BLANK;

			// in vulmodus: stoppen zodra we de eerste niet-lege cel tegenkomen (muv
			// eerste, die bevat de tag)
			if (mode == Mode.FILL_EMPTY && !blank && !first)
				break;

			setCellValue(currentRow, currentCell, value, newOrientation);

			if (currentOrientation == Orientation.VERTICAL)
				rowNumber++;
			else
				columnNumber++;

			if (maxOverwriteVertical > 0 && rowNumber > startRowNumber + maxOverwriteVertical - 1)
				break;
			if (maxOverwriteHorizontal > 0
				&& columnNumber > startColumnNumber + maxOverwriteHorizontal - 1)
				break;

			first = false;
		}

	}

	private void duplicateRow(Row newRow, Row originalRow)
	{
		for (Cell originalCell : originalRow)
		{
			Cell copy = newRow.createCell(originalCell.getColumnIndex());
			if (!ExcelWorkbook.FIELD_REGEX.matcher(copy.getStringCellValue()).matches())
			{
				copy.setCellValue(originalCell.getStringCellValue());
				copy.setCellStyle(originalCell.getCellStyle());
				copy.setCellType(originalCell.getCellType());
			}
		}
	}

	public Cell getCell()
	{
		return cell;
	}

	private Row insertRow(int rowNumber)
	{
		Row currentRow;
		sheet.shiftRows(rowNumber, sheet.getLastRowNum(), 1);
		currentRow = sheet.createRow(rowNumber);
		// stijlen overnemen van eerste rij
		currentRow.setHeight(row.getHeight());
		for (Iterator<Cell> it = row.cellIterator(); it.hasNext();)
		{
			Cell origCell = it.next();
			Cell newCell = currentRow.createCell(origCell.getColumnIndex());
			newCell.setCellStyle(origCell.getCellStyle());
		}
		shifted = true;
		return currentRow;
	}

	@Override
	public Object getMergedResult()
	{
		return result;
	}

	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Wordt niet gebruikt
	 * 
	 * @return null
	 * @see nl.topicus.cobra.templates.documents.Field#getType()
	 */
	@Override
	public String getType()
	{
		return null;
	}

	/**
	 * Vult de cel, header, footer of sheetname met de waarde volgens de fieldresolver.
	 * 
	 * @see nl.topicus.cobra.templates.documents.Field#merge(nl.topicus.cobra.templates.resolvers.FieldResolver)
	 */
	@Override
	public void merge(FieldResolver resolver)
	{
		result = resolver.resolve(name);

		if (cell != null)
			mergeCell();
		else if (header != null)
			mergeHeader();
		else if (footer != null)
			mergeFooter();
		else if (sheetName != null)
			mergeSheetName();
	}

	/**
	 * Vult de cel met het resultaat. Als het resultaat een lijst is, kunnen onder- en/of
	 * naastliggende cellen ook worden gevuld, afhankelijk van de vulmodus.
	 */
	private void mergeCell()
	{
		setCellValue(row, cell, result, orientation);
	}

	/**
	 * Vult de sheet naam met het resultaat. Als dit geen unieke tekst oplevert, wordt (n)
	 * achter de naam gezet (n oplopend vanaf 1).
	 */
	private void mergeSheetName()
	{
		String fieldText = ExcelWorkbook.FIELD_BEGIN + name + ExcelWorkbook.FIELD_END;
		String text = formatAsString(result);
		int sheetIndex = workbook.getSheetIndex(sheet);
		String nieuweNaam = sheetName.replace(fieldText, text);
		// haal niet toegestane tekens eruit / \ ? * [ ]
		nieuweNaam = nieuweNaam.replaceAll("[/\\\\\\?\\*\\[\\]]", "");

		// maak uniek
		String uniek = nieuweNaam;
		if (uniek.length() > 31)
			uniek = uniek.substring(0, 31);

		int i = 1;
		while (workbook.getSheetIndex(uniek) >= 0)
		{
			String index = " (" + (i++) + ")";
			if (nieuweNaam.length() + index.length() < 31)
				uniek = nieuweNaam + index;
			else
				uniek = nieuweNaam.substring(0, 31 - index.length()) + index;
		}

		workbook.setSheetName(sheetIndex, uniek);
	}

	private void mergeFooter()
	{
		String fieldText = ExcelWorkbook.FIELD_BEGIN + name + ExcelWorkbook.FIELD_END;
		String text = formatAsString(result);
		replaceFooter(fieldText, text);
	}

	private void replaceFooter(String oud, String nieuw)
	{
		String left = footer.getLeft();
		if (left != null)
			footer.setLeft(left.replace(oud, nieuw));
		String center = footer.getCenter();
		if (center != null)
			footer.setCenter(center.replace(oud, nieuw));
		String right = footer.getRight();
		if (right != null)
			footer.setRight(right.replace(oud, nieuw));
	}

	private void mergeHeader()
	{
		String fieldText = ExcelWorkbook.FIELD_BEGIN + name + ExcelWorkbook.FIELD_END;
		String text = formatAsString(result);
		replaceHeader(fieldText, text);
	}

	private void replaceHeader(String oud, String nieuw)
	{
		String left = header.getLeft();
		if (left != null)
			header.setLeft(left.replace(oud, nieuw));
		String center = header.getCenter();
		if (center != null)
			header.setCenter(center.replace(oud, nieuw));
		String right = header.getRight();
		if (right != null)
			header.setRight(right.replace(oud, nieuw));
	}

	/**
	 * Constructor voor een veld dat met een Excel-cel verbonden is.
	 * 
	 * @param field
	 *            Veldnaam
	 * @param workbook
	 * @param sheet
	 * @param row
	 * @param cell
	 * @param mode
	 *            Default vulmodus voor het verwerken van lijsten (kan worden veranderd
	 *            door \xx modifiers in het template)
	 */
	public ExcelField(String field, Workbook workbook, Sheet sheet, Row row, Cell cell, Mode mode)
	{
		this.workbook = workbook;
		this.sheet = sheet;
		this.row = row;
		this.cell = cell;
		this.mode = mode;

		parse(field);
	}

	/**
	 * Constructor voor een veld dat met een Excel-cel verbonden is. Default vulmodus voor
	 * lijsten is FILL_EMPTY (bij een lijst worden de lege cellen direct onder de
	 * gekoppelde Excel-cel gevuld). Dmv \xx modifiers kan dit gedrag vanuit het template
	 * worden veranderd.
	 * 
	 * @param field
	 * @param workbook
	 * @param sheet
	 * @param row
	 * @param cell
	 */
	public ExcelField(String field, Workbook workbook, Sheet sheet, Row row, Cell cell)
	{
		this(field, workbook, sheet, row, cell, Mode.FILL_EMPTY);
	}

	/**
	 * Constructor voor een veld dat met de header van een sheet verbonden is.
	 * 
	 * @param field
	 * @param workbook
	 * @param sheet
	 * @param header
	 */
	public ExcelField(String field, Workbook workbook, Sheet sheet, Header header)
	{
		this.name = field;
		this.workbook = workbook;
		this.sheet = sheet;
		this.header = header;
		this.mode = Mode.PARAGRAPH;
	}

	public ExcelField(String field, Workbook workbook, Sheet sheet, Footer footer)
	{
		this.name = field;
		this.workbook = workbook;
		this.sheet = sheet;
		this.footer = footer;
		this.mode = Mode.PARAGRAPH;
	}

	public ExcelField(String field, Workbook workbook, Sheet sheet, String sheetName)
	{
		this.name = field;
		this.workbook = workbook;
		this.sheet = sheet;
		this.mode = Mode.PARAGRAPH;
		this.sheetName = sheetName;
	}

	/**
	 * @param field
	 */
	private void parse(String field)
	{
		// Extra properties:
		// \@ datumformaat
		// \# currency-formaat
		// \* tekstformaat
		// \t (transpose) switchen van verticaal naar horizontaal voor lijsten
		// \i invoegen
		// \d dupliceer
		// \o [nn [mm]] overschijven max. nn x mm cellen

		String[] parts = field.split("\\\\");
		name = parts[0].trim();

		for (int i = 1; i < parts.length; i++)
		{
			if (parts[i].startsWith("@"))
			{
				setDateFormat(parts[i].substring(1).trim());
			}
			else if (parts[i].startsWith("#"))
			{
				setNumberFormat(parts[i].substring(1).trim());
			}
			else if (parts[i].startsWith("*"))
			{
				setTextFormat(parts[i].substring(1).trim());
			}
			else if (parts[i].startsWith("t"))
			{
				orientation = Orientation.HORIZONTAL;
				// wissel de maxima
				int h = maxOverwriteHorizontal;
				maxOverwriteHorizontal = maxOverwriteVertical;
				maxOverwriteVertical = h;
			}
			else if (parts[i].startsWith("i"))
			{
				mode = Mode.INSERT;
			}
			else if (parts[i].startsWith("d"))
			{
				mode = Mode.DUPLICATE_ROWS;
			}
			else if (parts[i].startsWith("o"))
			{
				mode = Mode.OVERWRITE;
				String[] args = parts[i].substring(1).trim().split(" ");
				if (args.length >= 2)
				{
					if (orientation == Orientation.VERTICAL)
					{
						maxOverwriteVertical = Integer.parseInt(args[0]);
						maxOverwriteHorizontal = Integer.parseInt(args[1]);
					}
					else
					{
						maxOverwriteVertical = Integer.parseInt(args[1]);
						maxOverwriteHorizontal = Integer.parseInt(args[0]);
					}
				}
				else if (args.length == 1 && args[0].length() > 0)
				{
					maxOverwriteVertical = maxOverwriteHorizontal = Integer.parseInt(args[0]);
				}
			}
		}
	}

	/**
	 * @see nl.topicus.cobra.templates.documents.Field#getFieldInfo(nl.topicus.cobra.templates.resolvers.FieldResolver)
	 */
	@Override
	public FieldInfo getFieldInfo(FieldResolver resolver)
	{
		return resolver.getInfo(getName());
	}

	public String getNumberFormat()
	{
		return numberFormat;
	}

	public void setNumberFormat(String numberFormat)
	{
		this.numberFormat = numberFormat;
		DataFormat format = workbook.createDataFormat();
		if (style == null)
			style = workbook.createCellStyle();
		short fmt = format.getFormat(numberFormat);
		style.setDataFormat(fmt);
	}

	public String getDateFormat()
	{
		return dateFormat;
	}

	public void setDateFormat(String dateFormat)
	{
		this.dateFormat = dateFormat;
		DataFormat format = workbook.createDataFormat();
		if (style == null)
			style = workbook.createCellStyle();

		short fmt = format.getFormat(dateFormat);
		style.setDataFormat(fmt);
	}

	public String getTextFormat()
	{
		return textFormat;
	}

	public void setTextFormat(String textFormat)
	{
		this.textFormat = textFormat;
	}

	public boolean isShifted()
	{
		return shifted;
	}

	@Override
	public String toString()
	{
		return name;
	}

}
