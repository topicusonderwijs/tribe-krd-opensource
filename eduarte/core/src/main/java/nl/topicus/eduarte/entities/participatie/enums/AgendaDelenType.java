package nl.topicus.eduarte.entities.participatie.enums;

/**
 * @author N. Henzen
 */
public enum AgendaDelenType
{
	VOLLEDIG("Volledige weergave"),

	TIJD_EN_TYPE("Tijd en type weergave"),

	HALF("Alleen tijd weergave"),

	GEEN("Geen weergave");

	private String naam;

	AgendaDelenType(String naam)
	{
		this.naam = naam;
	}

	@Override
	public String toString()
	{
		return naam;
	}

}
