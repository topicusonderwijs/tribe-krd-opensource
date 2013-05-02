package nl.topicus.eduarte.onderwijscatalogus.jobs;

public class SoortOnderwijsproductImportRegel
{
	String code;

	String naam;

	boolean summatief;

	public SoortOnderwijsproductImportRegel(String code, String naam, Boolean summatief)
	{
		this.code = code;
		this.naam = naam;
		this.summatief = summatief;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public boolean isSummatief()
	{
		return summatief;
	}

	public void setSummatief(boolean summatief)
	{
		this.summatief = summatief;
	}
}