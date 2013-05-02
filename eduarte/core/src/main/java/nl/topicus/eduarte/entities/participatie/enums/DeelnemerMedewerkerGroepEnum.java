package nl.topicus.eduarte.entities.participatie.enums;

public enum DeelnemerMedewerkerGroepEnum
{
	deelnemer("Deelnemer"),

	medewerker("Medewerker"),

	groep("Groep");

	private String naam;

	DeelnemerMedewerkerGroepEnum(String naam)
	{
		this.naam = naam;
	}

	@Override
	public String toString()
	{
		return naam;
	}
}
