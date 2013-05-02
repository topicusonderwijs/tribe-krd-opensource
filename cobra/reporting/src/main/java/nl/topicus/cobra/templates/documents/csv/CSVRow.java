package nl.topicus.cobra.templates.documents.csv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class CSVRow implements Row, Cloneable
{
	List<Cell> cells;

	CSVSheet sheet;

	public CSVRow(CSVSheet sheet)
	{
		this.sheet = sheet;
		cells = new ArrayList<Cell>();
	}

	public Cell getCell(int num)
	{
		if (num < cells.size())
			return cells.get(num);

		return null;
	}

	@Override
	public Iterator<Cell> cellIterator()
	{
		return cells.iterator();
	}

	@Override
	public Cell createCell(int column)
	{
		while (column >= cells.size())
			cells.add(new CSVCell(this));
		return cells.get(column);
	}

	@Override
	public Cell createCell(int column, int type)
	{
		Cell cell = createCell(column);
		cell.setCellType(type);
		return cell;
	}

	@Override
	public Cell getCell(int cellnum, MissingCellPolicy policy)
	{
		return getCell(cellnum);
	}

	@Override
	public short getFirstCellNum()
	{
		return 0;
	}

	@Override
	public short getHeight()
	{
		return 0;
	}

	@Override
	public float getHeightInPoints()
	{
		return 0;
	}

	@Override
	public short getLastCellNum()
	{
		return (short) (cells.size() - 1);
	}

	@Override
	public int getPhysicalNumberOfCells()
	{
		return cells.size();
	}

	@Override
	public int getRowNum()
	{
		return sheet.rows.indexOf(this);
	}

	@Override
	public Sheet getSheet()
	{
		return sheet;
	}

	@Override
	public boolean getZeroHeight()
	{
		return false;
	}

	/**
	 * Verwijder cell; verschuif achterliggende cellen naar links
	 */
	@Override
	public void removeCell(Cell cell)
	{
		cells.remove(cell);
	}

	@Override
	public void setHeight(short height)
	{
	}

	@Override
	public void setHeightInPoints(float height)
	{
	}

	/**
	 * Verplaats deze rij naar rowNum; verschuif tussenliggende rijen naar boven of
	 * beneden
	 */
	@Override
	public void setRowNum(int rowNum)
	{
		sheet.rows.remove(this);
		sheet.rows.add(rowNum, this);
	}

	@Override
	public void setZeroHeight(boolean height)
	{
	}

	@Override
	public Iterator<Cell> iterator()
	{
		return cellIterator();
	}

	@Override
	public CSVRow clone()
	{
		CSVRow newRow = new CSVRow(sheet);
		for (Cell cell : cells)
		{
			CSVCell newCell = ((CSVCell) cell).clone();
			newCell.row = newRow;
			newRow.cells.add(newCell);
		}
		return newRow;
	}

	public CSVWorkbook getWorkbook()
	{
		return (CSVWorkbook) sheet.getWorkbook();
	}

	@Override
	public String toString()
	{
		return cells.toString();
	}
}
