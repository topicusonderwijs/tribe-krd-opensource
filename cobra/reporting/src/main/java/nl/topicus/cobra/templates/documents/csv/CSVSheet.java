package nl.topicus.cobra.templates.documents.csv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.util.PaneInformation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

public class CSVSheet implements Sheet, Cloneable
{
	List<Row> rows;

	private CSVWorkbook workbook;

	public CSVSheet(CSVWorkbook workbook)
	{
		this.workbook = workbook;
		rows = new ArrayList<Row>();
	}

	@Override
	public int addMergedRegion(CellRangeAddress region)
	{
		return 0;
	}

	@Override
	public void autoSizeColumn(int column)
	{
	}

	@Override
	public void autoSizeColumn(int column, boolean useMergedCells)
	{
	}

	@Override
	public Drawing createDrawingPatriarch()
	{
		return null;
	}

	@Override
	public void createFreezePane(int colSplit, int rowSplit)
	{
	}

	@Override
	public void createFreezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow)
	{
	}

	@Override
	public Row createRow(int rownum)
	{
		while (rownum >= rows.size())
			rows.add(new CSVRow(this));
		return rows.get(rownum);
	}

	@Override
	public void createSplitPane(int splitPos, int splitPos2, int leftmostColumn, int topRow,
			int activePane)
	{
	}

	@Override
	public boolean getAutobreaks()
	{
		return false;
	}

	@Override
	public Comment getCellComment(int row, int column)
	{
		return null;
	}

	@Override
	public int[] getColumnBreaks()
	{
		return null;
	}

	@Override
	public CellStyle getColumnStyle(int column)
	{
		return null;
	}

	@Override
	public int getColumnWidth(int columnIndex)
	{
		return 0;
	}

	@Override
	public int getDefaultColumnWidth()
	{
		return 0;
	}

	@Override
	public short getDefaultRowHeight()
	{
		return 0;
	}

	@Override
	public float getDefaultRowHeightInPoints()
	{
		return 0;
	}

	@Override
	public boolean getDisplayGuts()
	{
		return false;
	}

	@Override
	public int getFirstRowNum()
	{
		return 0;
	}

	@Override
	public boolean getFitToPage()
	{
		return false;
	}

	@Override
	public Footer getFooter()
	{
		return null;
	}

	@Override
	public Header getHeader()
	{
		return null;
	}

	@Override
	public boolean getHorizontallyCenter()
	{
		return false;
	}

	@Override
	public int getLastRowNum()
	{
		return rows.size() - 1;
	}

	@Override
	public short getLeftCol()
	{
		return 0;
	}

	@Override
	public double getMargin(short margin)
	{
		return 0;
	}

	@Override
	public CellRangeAddress getMergedRegion(int index)
	{
		return null;
	}

	@Override
	public int getNumMergedRegions()
	{
		return 0;
	}

	@Override
	public PaneInformation getPaneInformation()
	{
		return null;
	}

	@Override
	public int getPhysicalNumberOfRows()
	{
		return rows.size();
	}

	@Override
	public PrintSetup getPrintSetup()
	{
		return null;
	}

	@Override
	public boolean getProtect()
	{
		return false;
	}

	@Override
	public Row getRow(int rownum)
	{
		if (rownum < rows.size())
			return rows.get(rownum);

		return null;
	}

	@Override
	public int[] getRowBreaks()
	{
		return null;
	}

	@Override
	public boolean getRowSumsBelow()
	{
		return false;
	}

	@Override
	public boolean getRowSumsRight()
	{
		return false;
	}

	@Override
	public boolean getScenarioProtect()
	{
		return false;
	}

	@Override
	public String getSheetName()
	{
		return null;
	}

	@Override
	public short getTopRow()
	{
		return 0;
	}

	@Override
	public boolean getVerticallyCenter()
	{
		return false;
	}

	@Override
	public Workbook getWorkbook()
	{
		return workbook;
	}

	@Override
	public void groupColumn(int fromColumn, int toColumn)
	{
	}

	@Override
	public void groupRow(int fromRow, int toRow)
	{
	}

	@Override
	public boolean isColumnBroken(int column)
	{
		return false;
	}

	@Override
	public boolean isColumnHidden(int columnIndex)
	{
		return false;
	}

	@Override
	public boolean isDisplayFormulas()
	{
		return false;
	}

	@Override
	public boolean isDisplayGridlines()
	{
		return false;
	}

	@Override
	public boolean isDisplayRowColHeadings()
	{
		return false;
	}

	@Override
	public boolean isDisplayZeros()
	{
		return false;
	}

	@Override
	public boolean isPrintGridlines()
	{
		return false;
	}

	@Override
	public boolean isRowBroken(int row)
	{
		return false;
	}

	@Override
	public boolean isSelected()
	{
		return true;
	}

	@Override
	public void removeColumnBreak(int column)
	{
	}

	@Override
	public void removeMergedRegion(int index)
	{
	}

	@Override
	public void removeRow(Row row)
	{
		rows.remove(row);
	}

	@Override
	public void removeRowBreak(int row)
	{
	}

	@Override
	public Iterator<Row> rowIterator()
	{
		return rows.iterator();
	}

	@Override
	public void setAutobreaks(boolean value)
	{
	}

	@Override
	public void setColumnBreak(int column)
	{
	}

	@Override
	public void setColumnGroupCollapsed(int columnNumber, boolean collapsed)
	{
	}

	@Override
	public void setColumnHidden(int columnIndex, boolean hidden)
	{
	}

	@Override
	public void setColumnWidth(int columnIndex, int width)
	{
	}

	@Override
	public void setDefaultColumnStyle(int column, CellStyle style)
	{
	}

	@Override
	public void setDefaultColumnWidth(int width)
	{
	}

	@Override
	public void setDefaultRowHeight(short height)
	{
	}

	@Override
	public void setDefaultRowHeightInPoints(float height)
	{
	}

	@Override
	public void setDisplayFormulas(boolean show)
	{
	}

	@Override
	public void setDisplayGridlines(boolean show)
	{
	}

	@Override
	public void setDisplayGuts(boolean value)
	{
	}

	@Override
	public void setDisplayRowColHeadings(boolean show)
	{
	}

	@Override
	public void setDisplayZeros(boolean value)
	{
	}

	@Override
	public void setFitToPage(boolean value)
	{
	}

	@Override
	public void setHorizontallyCenter(boolean value)
	{
	}

	@Override
	public void setMargin(short margin, double size)
	{
	}

	@Override
	public void setPrintGridlines(boolean show)
	{
	}

	@Override
	public void setRowBreak(int row)
	{
	}

	@Override
	public void setRowGroupCollapsed(int row, boolean collapse)
	{
	}

	@Override
	public void setRowSumsBelow(boolean value)
	{
	}

	@Override
	public void setRowSumsRight(boolean value)
	{
	}

	@Override
	public void setSelected(boolean value)
	{
	}

	@Override
	public void setVerticallyCenter(boolean value)
	{
	}

	@Override
	public void setZoom(int numerator, int denominator)
	{
	}

	@Override
	public void shiftRows(int startRow, int endRow, int n)
	{
		shiftRows(startRow, endRow, n, false, false);
	}

	@Override
	public void shiftRows(int startRow, int endRow, int n, boolean copyRowHeight,
			boolean resetOriginalRowHeight)
	{
		if (n == 0)
			return;
		// vooruit of achteruit schuiven?
		else if (n > 0)
			for (int i = endRow; i >= startRow; i--)
				moveRow(i, i + n);
		else
			for (int i = startRow; i <= endRow; i++)
				moveRow(i, i + n);
	}

	/**
	 * Zorg dat rij bestaat, verplaats rij en maak oude rij leeg.
	 */
	private void moveRow(int from, int to)
	{
		createRow(to);
		rows.set(to, rows.get(from));
		rows.set(from, new CSVRow(this));
	}

	@Override
	public void showInPane(short toprow, short leftcol)
	{
	}

	@Override
	public void ungroupColumn(int fromColumn, int toColumn)
	{
	}

	@Override
	public void ungroupRow(int fromRow, int toRow)
	{
	}

	@Override
	public Iterator<Row> iterator()
	{
		return rows.iterator();
	}

	@Override
	public CSVSheet clone()
	{
		CSVSheet newSheet = new CSVSheet(workbook);
		for (Row row : rows)
		{
			CSVRow newRow = ((CSVRow) row).clone();
			newRow.sheet = newSheet;
			newSheet.rows.add(newRow);
		}

		return newSheet;
	}
}
