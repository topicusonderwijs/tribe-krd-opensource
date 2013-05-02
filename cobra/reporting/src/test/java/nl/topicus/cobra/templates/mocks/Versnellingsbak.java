package nl.topicus.cobra.templates.mocks;

import nl.topicus.cobra.templates.annotations.Exportable;

@Exportable
public enum Versnellingsbak
{

	HANDMATIG("Handmatig", "H"),
	AUTOMAAT("Automaat", "A");

	private String afkorting;

	private String naam;

	Versnellingsbak(String naam, String afkorting)
	{
		this.naam = naam;
		this.afkorting = afkorting;
	}

	@Exportable
	public String getAfkorting()
	{
		return afkorting;
	}

	@Override
	public String toString()
	{
		return naam;
	}
}
