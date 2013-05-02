package nl.topicus.cobra.templates.documents.csv;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;

import org.apache.poi.ss.usermodel.*;

public class CSVCell implements Cell, Cloneable
{
	private String value;

	CSVRow row;

	private CellStyle cellStyle;

	public CSVCell(CSVRow row)
	{
		this.row = row;
	}

	@Override
	public boolean getBooleanCellValue()
	{
		return "true".equals(value.toLowerCase()) || "j".equals(value.toLowerCase())
			|| "ja".equals(value.toLowerCase());
	}

	@Override
	public int getCachedFormulaResultType()
	{
		return 0;
	}

	@Override
	public Comment getCellComment()
	{
		return null;
	}

	@Override
	public String getCellFormula()
	{
		return null;
	}

	@Override
	public CellStyle getCellStyle()
	{
		return cellStyle;
	}

	@Override
	public int getCellType()
	{
		if (value == null)
			return Cell.CELL_TYPE_BLANK;
		return Cell.CELL_TYPE_STRING;
	}

	@Override
	public int getColumnIndex()
	{
		return row.cells.indexOf(this);
	}

	@Override
	public Date getDateCellValue()
	{
		return TimeUtil.getInstance().parseDateString(value);
	}

	@Override
	public byte getErrorCellValue()
	{
		return 0;
	}

	@Override
	public Hyperlink getHyperlink()
	{
		return null;
	}

	@Override
	public double getNumericCellValue()
	{
		try
		{
			return NumberFormat.getInstance().parse(value).doubleValue();
		}
		catch (ParseException e)
		{
			return 0.0;
		}
	}

	@Override
	public RichTextString getRichStringCellValue()
	{
		return new RichTextString()
		{

			@Override
			public int numFormattingRuns()
			{
				return 0;
			}

			@Override
			public int length()
			{
				return value.length();
			}

			@Override
			public String getString()
			{
				return value;
			}

			@Override
			public int getIndexOfFormattingRun(int arg0)
			{
				return 0;
			}

			@Override
			public void clearFormatting()
			{
			}

			@Override
			public void applyFont(int arg0, int arg1, Font arg2)
			{
			}

			@Override
			public void applyFont(int arg0, int arg1, short arg2)
			{
			}

			@Override
			public void applyFont(short arg0)
			{
			}

			@Override
			public void applyFont(Font arg0)
			{
			}
		};
	}

	@Override
	public Row getRow()
	{
		return row;
	}

	@Override
	public int getRowIndex()
	{
		return row.getRowNum();
	}

	@Override
	public Sheet getSheet()
	{
		return row.getSheet();
	}

	@Override
	public String getStringCellValue()
	{
		return StringUtil.valueOrEmptyIfNull(value);
	}

	@Override
	public void setAsActiveCell()
	{
	}

	@Override
	public void setCellComment(Comment arg0)
	{
	}

	@Override
	public void setCellErrorValue(byte arg0)
	{
	}

	@Override
	public void setCellFormula(String arg0)
	{
	}

	@Override
	public void setCellStyle(CellStyle cellStyle)
	{
		this.cellStyle = cellStyle;
	}

	@Override
	public void setCellType(int arg0)
	{
		if (arg0 == Cell.CELL_TYPE_BLANK)
			value = null;
	}

	@Override
	public void setCellValue(double number)
	{
		// default format
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();

		// als de CSV de komma als scheidingsteken gebruikt, dan de punt als
		// decimaalteken gebruiken, onafhankelijk van de locale.
		if (getWorkbook().getSeparator().equals(','))
			symbols.setDecimalSeparator('.');

		DecimalFormat format = new DecimalFormat("#0.###", symbols);

		if (cellStyle != null && cellStyle.getDataFormatString() != null)
		{
			String pattern = cellStyle.getDataFormatString();
			if (pattern != null)
			{
				try
				{
					format = new DecimalFormat(pattern, symbols);
				}
				catch (IllegalArgumentException e)
				{
					// negeer fouten in het pattern (format met default pattern)
				}
			}
		}
		value = format.format(number);

	}

	@Override
	public void setCellValue(Date date)
	{
		// default formatting dd-MM-yyyy
		value = TimeUtil.getInstance().formatDate(date);

		if (cellStyle != null && cellStyle.getDataFormatString() != null)
		{
			String pattern = cellStyle.getDataFormatString();
			if (pattern != null)
			{
				try
				{
					DateFormat formatter = new SimpleDateFormat(pattern);
					value = formatter.format(date);
				}
				catch (IllegalArgumentException e)
				{
					// negeer fouten in het pattern (format met default pattern)
				}
			}
		}
	}

	@Override
	public void setCellValue(Calendar arg0)
	{
		value = TimeUtil.getInstance().formatDate(arg0.getTime());
	}

	@Override
	public void setCellValue(RichTextString arg0)
	{
		value = arg0.getString();
	}

	@Override
	public void setCellValue(String arg0)
	{
		value = arg0;
	}

	@Override
	public void setCellValue(boolean arg0)
	{
		value = arg0 ? "J" : "N";
	}

	@Override
	public void setHyperlink(Hyperlink arg0)
	{
		value = arg0.getAddress();
	}

	@Override
	public CSVCell clone()
	{
		CSVCell clone = new CSVCell(row);
		clone.value = value;
		return clone;
	}

	private CSVWorkbook getWorkbook()
	{
		return row.getWorkbook();
	}

	@Override
	public String toString()
	{
		return getStringCellValue();
	}
}
