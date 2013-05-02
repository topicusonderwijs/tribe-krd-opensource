package nl.topicus.eduarte.onderwijscatalogus.jobs;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.util.StringUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

public class ToegestaanOndProdImportFile implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String bestandsnaam;

	private List<ToegestaanOndProdImportRegel> toegestaneOnderwijsproducten =
		new ArrayList<ToegestaanOndProdImportRegel>();

	private HSSFWorkbook workbook;

	private final NumberFormat format = NumberFormat.getIntegerInstance();

	public ToegestaanOndProdImportFile(String bestandsnaam, InputStream inputStream)
	{
		format.setGroupingUsed(false);
		this.bestandsnaam = bestandsnaam;
		try
		{
			workbook = new HSSFWorkbook(inputStream);
			readOnderwijsproducten(toegestaneOnderwijsproducten);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	private void readOnderwijsproducten(List<ToegestaanOndProdImportRegel> list)
	{
		HSSFSheet sheet = workbook.getSheetAt(0);
		int rowNum = 1;
		HSSFRow row;
		while (true)
		{
			row = sheet.getRow(rowNum);
			if (row == null)
				break;
			String opleidingscode = getCellValue(row, 0);
			String cohort = getCellValue(row, 1);
			String productregelAfkorting = getCellValue(row, 2);
			String onderwijsproductCode = getCellValue(row, 3);

			if (StringUtil.isEmpty(opleidingscode) || StringUtil.isEmpty(cohort)
				|| StringUtil.isEmpty(productregelAfkorting)
				|| StringUtil.isEmpty(onderwijsproductCode))
				break;
			ToegestaanOndProdImportRegel regel =
				new ToegestaanOndProdImportRegel(opleidingscode, cohort, productregelAfkorting,
					onderwijsproductCode);
			list.add(regel);
			rowNum++;
		}

	}

	private String getCellValue(HSSFRow row, int cellnum)
	{
		HSSFCell cell = row.getCell(cellnum);
		if (cell != null)
		{
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
			{
				return format.format(cell.getNumericCellValue());
			}
			return cell.toString();
		}
		return null;
	}

	public void setBestandsnaam(String bestandsnaam)
	{
		this.bestandsnaam = bestandsnaam;
	}

	public String getBestandsnaam()
	{
		return bestandsnaam;
	}

	public HSSFWorkbook getWorkbook()
	{
		return workbook;
	}

	public void setWorkbook(HSSFWorkbook workbook)
	{
		this.workbook = workbook;
	}

	public List<ToegestaanOndProdImportRegel> getToegestaneOnderwijsproducten()
	{
		return toegestaneOnderwijsproducten;
	}

	public void setToegestaneOnderwijsproducten(
			List<ToegestaanOndProdImportRegel> toegestaneOnderwijsproducten)
	{
		this.toegestaneOnderwijsproducten = toegestaneOnderwijsproducten;
	}
}