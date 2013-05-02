package nl.topicus.eduarte.entities.participatie.enums;

/**
 * @author N. Henzen
 */
public enum AgendaPrintOpmaak
{
	KLEUR("Printen in kleur"),
	GRIJS("Printen in grijswaarden");

	private String naam;

	AgendaPrintOpmaak(String naam)
	{
		this.naam = naam;
	}

	@Override
	public String toString()
	{
		return naam;
	}

}
