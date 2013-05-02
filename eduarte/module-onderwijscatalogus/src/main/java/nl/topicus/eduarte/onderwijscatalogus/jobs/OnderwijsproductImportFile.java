package nl.topicus.eduarte.onderwijscatalogus.jobs;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.util.StringUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class OnderwijsproductImportFile implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String bestandsnaam;

	private Map<String, String> leerstijlen = new HashMap<String, String>();

	private Map<String, String> soortPraktijklokalen = new HashMap<String, String>();

	private Map<String, String> typeToetsen = new HashMap<String, String>();

	private Map<String, String> typeLocaties = new HashMap<String, String>();

	private Map<String, String> gebruiksmiddelen = new HashMap<String, String>();

	private Map<String, String> verbruiksmiddelen = new HashMap<String, String>();

	private List<SoortOnderwijsproductImportRegel> soortenOnderwijsproducten =
		new ArrayList<SoortOnderwijsproductImportRegel>();

	private List<OnderwijsproductImportRegel> onderwijsproducten =
		new ArrayList<OnderwijsproductImportRegel>();

	private HSSFWorkbook workbook;

	public OnderwijsproductImportFile(String bestandsnaam, InputStream inputStream)
	{
		this.bestandsnaam = bestandsnaam;
		try
		{
			workbook = new HSSFWorkbook(inputStream);
			readCodeNaamSheets("Leerstijl", leerstijlen);
			readCodeNaamSheets("SoortPraktijklokaal", soortPraktijklokalen);
			readCodeNaamSheets("TypeToets", typeToetsen);
			readCodeNaamSheets("TypeLocatie", typeLocaties);
			readCodeNaamSheets("Gebruiksmiddel", gebruiksmiddelen);
			readCodeNaamSheets("Verbruiksmiddel", verbruiksmiddelen);
			readSoortenOnderwijsproducten("SoortOnderwijsproduct", soortenOnderwijsproducten);
			readOnderwijsproducten("Onderwijsproduct", onderwijsproducten);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	private void readOnderwijsproducten(String sheetNaam, List<OnderwijsproductImportRegel> list)
	{
		HSSFSheet sheet = workbook.getSheet(sheetNaam);
		int rowNum = 1;
		HSSFRow row;
		while (true)
		{
			row = sheet.getRow(rowNum);
			if (row == null)
				break;
			String code = getCellValue(row, 0);
			String titel = getCellValue(row, 1);
			String omschrijving = getCellValue(row, 2);
			String soortOnderwijsproduct = getCellValue(row, 3);
			Integer status = getNummericCellValue(row, 5);
			Integer aggregatieniveau = getNummericCellValue(row, 16);

			if (StringUtil.isEmpty(code) || StringUtil.isEmpty(titel)
				|| StringUtil.isEmpty(omschrijving) || StringUtil.isEmpty(soortOnderwijsproduct)
				|| status == null || aggregatieniveau == null)
				break;
			OnderwijsproductImportRegel regel =
				new OnderwijsproductImportRegel(code, titel, omschrijving, soortOnderwijsproduct,
					status, aggregatieniveau);
			regel.setZoektermen(getCellValue(row, 4));
			regel.setTaxonomieCodes(getCellValue(row, 6));
			regel.setOrganisatieEenheden(getCellValue(row, 7));
			regel.setLocaties(getCellValue(row, 8));
			regel.setPaklijst(getCellValue(row, 9));
			regel.setVoorwaarden(getCellValue(row, 10));
			regel.setKostprijs(parseBigDecimal(getCellValue(row, 11)));
			regel.setKalender(getCellValue(row, 12));
			regel.setUitvoeringsfrequentie(getCellValue(row, 13));
			regel.setOmvang(parseBigDecimal(getCellValue(row, 14)));
			regel.setBelasting(parseBigDecimal(getCellValue(row, 15)));
			regel.setStartonderwijsproduct(stringToBoolean(getCellValue(row, 17)));
			regel.setLeerstijl(getCellValue(row, 18));
			regel.setToegankelijkheid(getCellValue(row, 19));
			regel.setMinimumAantalDeelnemers(getNummericCellValue(row, 20));
			regel.setMaximumAantalDeelnemers(getNummericCellValue(row, 21));
			regel.setJuridischEigenaar(getCellValue(row, 22));
			regel.setGebruiksrecht(getCellValue(row, 23));
			regel.setSoortPraktijklokaal(getCellValue(row, 24));
			regel.setTypeToets(getCellValue(row, 25));
			regel.setPersoneelCompetenties(getCellValue(row, 26));
			regel.setPersoneelKennisGebiedNiveau(getCellValue(row, 27));
			regel.setPersoneelWettelijkeVereisten(getCellValue(row, 38));
			regel.setPersoneelBevoegdheid(getCellValue(row, 29));
			regel.setTypeLocatie(getCellValue(row, 30));
			regel.setGebruiksmiddelen(getCellValue(row, 31));
			regel.setVerbruiksmiddelen(getCellValue(row, 32));
			list.add(regel);
			rowNum++;
		}

	}

	private static final BigDecimal parseBigDecimal(String value)
	{
		try
		{
			return value == null ? null : new BigDecimal(value);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	private String getCellValue(HSSFRow row, int cellnum)
	{
		HSSFCell cell = row.getCell(cellnum);
		if (cell != null)
			return cell.toString();
		return null;
	}

	private int getNummericCellValue(HSSFRow row, int cellnum)
	{
		try
		{
			HSSFCell cell = row.getCell(cellnum);
			if (cell != null)
				return (int) cell.getNumericCellValue();
		}
		catch (Exception e)
		{
			// ignore
		}
		return 0;
	}

	private void readSoortenOnderwijsproducten(String sheetNaam,
			List<SoortOnderwijsproductImportRegel> list)
	{
		HSSFSheet sheet = workbook.getSheet(sheetNaam);
		int rowNum = 1;
		HSSFRow row;
		while (true)
		{
			row = sheet.getRow(rowNum);
			if (row == null)
				break;
			String code = getCellValue(row, 0);
			String naam = getCellValue(row, 1);
			Boolean summatief = stringToBoolean(getCellValue(row, 2));
			if (StringUtil.isEmpty(code) || StringUtil.isEmpty(naam) || summatief == null)
				break;
			list.add(new SoortOnderwijsproductImportRegel(code, naam, summatief));
			rowNum++;
		}

	}

	private void readCodeNaamSheets(String sheetNaam, Map<String, String> map)
	{
		HSSFSheet sheet = workbook.getSheet(sheetNaam);
		int rowNum = 1;
		HSSFRow row;
		while (true)
		{
			row = sheet.getRow(rowNum);
			if (row == null)
				break;
			String code = getCellValue(row, 0);
			String naam = getCellValue(row, 1);
			if (StringUtil.isEmpty(code) || StringUtil.isEmpty(naam))
				break;
			map.put(code, naam);
			rowNum++;
		}
	}

	private Boolean stringToBoolean(String string)
	{
		if (string == null)
			return null;
		if (string.equalsIgnoreCase("1"))
			return true;
		if (string.equalsIgnoreCase("0"))
			return false;
		if (string.equalsIgnoreCase("true"))
			return true;
		if (string.equalsIgnoreCase("false"))
			return false;
		if (string.equalsIgnoreCase("ja"))
			return true;
		if (string.equalsIgnoreCase("nee"))
			return false;
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

	public Map<String, String> getLeerstijlen()
	{
		return leerstijlen;
	}

	public void setLeerstijlen(Map<String, String> leerstijlen)
	{
		this.leerstijlen = leerstijlen;
	}

	public Map<String, String> getSoortPraktijklokalen()
	{
		return soortPraktijklokalen;
	}

	public void setSoortPraktijklokalen(Map<String, String> soortPraktijklokalen)
	{
		this.soortPraktijklokalen = soortPraktijklokalen;
	}

	public Map<String, String> getTypeToetsen()
	{
		return typeToetsen;
	}

	public void setTypeToetsen(Map<String, String> typeToetsen)
	{
		this.typeToetsen = typeToetsen;
	}

	public Map<String, String> getTypeLocaties()
	{
		return typeLocaties;
	}

	public void setTypeLocaties(Map<String, String> typeLocaties)
	{
		this.typeLocaties = typeLocaties;
	}

	public Map<String, String> getGebruiksmiddelen()
	{
		return gebruiksmiddelen;
	}

	public void setGebruiksmiddelen(Map<String, String> gebruiksmiddelen)
	{
		this.gebruiksmiddelen = gebruiksmiddelen;
	}

	public Map<String, String> getVerbruiksmiddelen()
	{
		return verbruiksmiddelen;
	}

	public void setVerbruiksmiddelen(Map<String, String> verbruiksmiddelen)
	{
		this.verbruiksmiddelen = verbruiksmiddelen;
	}

	public List<SoortOnderwijsproductImportRegel> getSoortenOnderwijsproducten()
	{
		return soortenOnderwijsproducten;
	}

	public void setSoortenOnderwijsproducten(
			List<SoortOnderwijsproductImportRegel> soortenOnderwijsproducten)
	{
		this.soortenOnderwijsproducten = soortenOnderwijsproducten;
	}

	public List<OnderwijsproductImportRegel> getOnderwijsproducten()
	{
		return onderwijsproducten;
	}

	public void setOnderwijsproducten(List<OnderwijsproductImportRegel> onderwijsproducten)
	{
		this.onderwijsproducten = onderwijsproducten;
	}

	public HSSFWorkbook getWorkbook()
	{
		return workbook;
	}

	public void setWorkbook(HSSFWorkbook workbook)
	{
		this.workbook = workbook;
	}
}