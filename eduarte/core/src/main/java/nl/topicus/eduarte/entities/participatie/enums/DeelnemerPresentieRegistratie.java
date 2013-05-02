package nl.topicus.eduarte.entities.participatie.enums;

public enum DeelnemerPresentieRegistratie
{
	NIET("Niet toegestaan"),
	STANDAARD_UIT("Wel toegestaan - standaard uit"),
	STANDAARD_AAN("Wel toegestaan - standaard aan");

	private String naam;

	DeelnemerPresentieRegistratie(String naam)
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
