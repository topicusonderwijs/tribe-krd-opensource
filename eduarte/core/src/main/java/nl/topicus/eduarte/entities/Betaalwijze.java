package nl.topicus.eduarte.entities;

public enum Betaalwijze
{
	OVERIG("Overig"),
	AUTOMATISCHE_INCASSO("Automatische incasso"),
	ACCEPTEMAIL("AcceptEmail");

	private String naam;

	private Betaalwijze(String naam)
	{
		this.naam = naam;
	}

	@Override
	public String toString()
	{
		return naam;
	}
}