package nl.topicus.eduarte.entities.participatie.enums;

public enum OnderwijsproductGebruik
{
	ONGEBRUIKT("Ongebruikt"),
	OPTIONEEL("Optioneel"),
	VERPLICHT("Verplicht");

	private String naam;

	OnderwijsproductGebruik(String naam)
	{
		this.naam = naam;
	}

	public String getNaam()
	{
		return naam;
	}

	@Override
	public String toString()
	{
		return naam;
	}
}
