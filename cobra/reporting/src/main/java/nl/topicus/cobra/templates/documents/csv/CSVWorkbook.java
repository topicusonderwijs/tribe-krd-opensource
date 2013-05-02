package nl.topicus.cobra.templates.documents.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.cobra.util.StringUtil;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

public class CSVWorkbook implements Workbook
{
	private List<Sheet> sheets;

	private List<String> names;

	private Character separator;

	private Charset charset;

	private static final String LINE_SEPARATOR = "line.separator";

	/**
	 * Detecteert het scheidingsteken op basis van de inhoud van de eerste regel. Indien
	 * in de eerste regel minder puntkomma's dan komma's voorkomen, dan is de komma het
	 * scheidingsteken.
	 */
	private char autoDetectSeparator(String line)
	{
		// autodetect
		// probeer eerst ;
		int puntkommas = StringUtil.countOccurances(line, ';');
		int kommas = StringUtil.countOccurances(line, ',');
		if (puntkommas < kommas)
			separator = ',';
		else
			separator = ';';
		return separator;
	}

	public CSVWorkbook()
	{
		sheets = new ArrayList<Sheet>();
		names = new ArrayList<String>();
	}

	public CSVWorkbook(InputStream inStream) throws IOException
	{
		this(inStream, "ISO-8859-1");
	}

	public CSVWorkbook(InputStream inStream, String charset) throws IOException
	{
		this();
		this.charset = Charset.forName(charset);
		read(inStream);
	}

	public void read(InputStream inStream) throws IOException
	{
		InputStreamReader ir = null;
		BufferedReader br = null;
		try
		{
			Asserts.assertNotNull("InputStream", inStream);

			ir = new InputStreamReader(inStream, charset);
			br = new BufferedReader(ir);
			String line;
			Sheet sheet = createSheet();
			int rownum = 0;
			while ((line = br.readLine()) != null)
			{
				if (separator == null)
					autoDetectSeparator(line);
				String[] values = line.split(separator.toString(), -1);
				Row row = sheet.createRow(rownum);
				for (int colnum = 0; colnum < values.length; colnum++)
				{
					Cell cell = row.createCell(colnum);
					cell.setCellValue(values[colnum]);
				}
				rownum++;
			}
		}
		finally
		{
			ResourceUtil.closeQuietly(br);
			ResourceUtil.closeQuietly(ir);
			ResourceUtil.closeQuietly(inStream);
		}
	}

	private String uniqueSheetName()
	{
		int n = 0;
		while (names.contains(Integer.toString(n)))
			n++;
		return Integer.toString(n);
	}

	@Override
	public int addPicture(byte[] pictureData, int format)
	{
		return 0;
	}

	@Override
	public Sheet cloneSheet(int sheetNum)
	{
		CSVSheet original = (CSVSheet) getSheetAt(sheetNum);
		Sheet clone = original.clone();
		sheets.add(clone);
		names.add(uniqueSheetName());
		return clone;
	}

	@Override
	public CellStyle createCellStyle()
	{
		return new CSVCellStyle();
	}

	@Override
	public DataFormat createDataFormat()
	{
		return new CSVDataFormat();
	}

	@Override
	public Font createFont()
	{
		return null;
	}

	@Override
	public Name createName()
	{
		return null;
	}

	@Override
	public Sheet createSheet()
	{
		return createSheet(uniqueSheetName());
	}

	@Override
	public Sheet createSheet(String sheetname)
	{
		Sheet sheet = new CSVSheet(this);
		sheets.add(sheet);
		names.add(sheetname);
		return sheet;
	}

	@Override
	public Font findFont(short boldWeight, short color, short fontHeight, String name,
			boolean italic, boolean strikeout, short typeOffset, byte underline)
	{
		return null;
	}

	@Override
	public int getActiveSheetIndex()
	{
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List getAllPictures()
	{
		return null;
	}

	@Override
	public CellStyle getCellStyleAt(short idx)
	{
		return null;
	}

	@Override
	public CreationHelper getCreationHelper()
	{
		return null;
	}

	@Override
	public int getFirstVisibleTab()
	{
		return 0;
	}

	@Override
	public Font getFontAt(short idx)
	{
		return null;
	}

	@Override
	public MissingCellPolicy getMissingCellPolicy()
	{
		return null;
	}

	@Override
	public Name getName(String name)
	{
		return null;
	}

	@Override
	public Name getNameAt(int nameIndex)
	{
		return null;
	}

	@Override
	public int getNameIndex(String name)
	{
		return 0;
	}

	@Override
	public short getNumCellStyles()
	{
		return 0;
	}

	@Override
	public short getNumberOfFonts()
	{
		return 0;
	}

	@Override
	public int getNumberOfNames()
	{
		return 0;
	}

	@Override
	public int getNumberOfSheets()
	{
		return sheets.size();
	}

	@Override
	public String getPrintArea(int sheetIndex)
	{
		return null;
	}

	@Override
	public Sheet getSheet(String name)
	{
		return getSheetAt(getSheetIndex(name));
	}

	@Override
	public Sheet getSheetAt(int index)
	{
		return sheets.get(index);
	}

	@Override
	public int getSheetIndex(String name)
	{
		return names.indexOf(name);
	}

	@Override
	public int getSheetIndex(Sheet sheet)
	{
		return sheets.indexOf(sheet);
	}

	@Override
	public String getSheetName(int sheet)
	{
		return names.get(sheet);
	}

	@Override
	public boolean isHidden()
	{
		return false;
	}

	@Override
	public boolean isSheetHidden(int sheetIx)
	{
		return false;
	}

	@Override
	public boolean isSheetVeryHidden(int sheetIx)
	{
		return false;
	}

	@Override
	public void removeName(int index)
	{
	}

	@Override
	public void removeName(String name)
	{
	}

	@Override
	public void removePrintArea(int sheetIndex)
	{
	}

	@Override
	public void removeSheetAt(int index)
	{
		sheets.remove(index);
		names.remove(index);
	}

	@Override
	public void setActiveSheet(int sheetIndex)
	{
	}

	@Override
	public void setFirstVisibleTab(int sheetIndex)
	{
	}

	@Override
	public void setHidden(boolean hiddenFlag)
	{
	}

	@Override
	public void setMissingCellPolicy(MissingCellPolicy missingCellPolicy)
	{
	}

	@Override
	public void setPrintArea(int sheetIndex, String reference)
	{
	}

	@Override
	public void setPrintArea(int sheetIndex, int startColumn, int endColumn, int startRow,
			int endRow)
	{
	}

	@Override
	public void setRepeatingRowsAndColumns(int sheetIndex, int startColumn, int endColumn,
			int startRow, int endRow)
	{
	}

	@Override
	public void setSelectedTab(int index)
	{
	}

	@Override
	public void setSheetHidden(int sheetIx, boolean hidden)
	{
	}

	@Override
	public void setSheetHidden(int sheetIx, int hidden)
	{
	}

	@Override
	public void setSheetName(int sheet, String name)
	{
		names.set(sheet, name);
	}

	@Override
	public void setSheetOrder(String sheetname, int pos)
	{
		Sheet sheet = sheets.remove(pos);
		sheets.add(pos, sheet);
		String name = names.remove(pos);
		names.add(pos, name);
	}

	@Override
	public void write(OutputStream stream) throws IOException
	{
		// Stel de gebruikte line separator in op de Windows standaard (CR+LF)
		String oldLineSeparator = System.getProperty(LINE_SEPARATOR);
		System.setProperty(LINE_SEPARATOR, "\r\n");
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(stream, this.charset));
		System.setProperty(LINE_SEPARATOR, oldLineSeparator);

		for (Sheet sheet : sheets)
			for (Row row : sheet)
			{
				StringBuilder builder = new StringBuilder();
				boolean first = true;
				for (Cell cell : row)
				{
					if (!first)
						builder.append(separator);
					first = false;
					String value = cell.getStringCellValue();
					builder.append(value);
				}
				writer.println(builder.toString());
			}
		writer.flush();

		if (writer.checkError())
			throw new IOException();
	}

	public Character getSeparator()
	{
		return separator;
	}

	public void setSeparator(Character separator)
	{
		this.separator = separator;
	}

	public void setCharset(Charset charset)
	{
		this.charset = charset;
	}

}
