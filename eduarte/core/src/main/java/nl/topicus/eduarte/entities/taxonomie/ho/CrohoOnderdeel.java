package nl.topicus.eduarte.entities.taxonomie.ho;

import nl.topicus.cobra.util.StringUtil;

public enum CrohoOnderdeel
{
	Economie,
	GedragEnMaatschappij,
	Gezondheidszorg,
	LandbouwEnNatuurlijkeOmgeving,
	Natuur,
	Onderwijs,
	Recht,
	Sectoroverstijgend,
	TaalEnCultuur,
	Techniek;

	@Override
	public String toString()
	{
		return StringUtil.convertCamelCase(name());
	}

	public static CrohoOnderdeel getCrohoOderdeel(String name)
	{
		for (CrohoOnderdeel onderdeel : values())
			if (onderdeel.toString().equalsIgnoreCase(name))
				return onderdeel;

		throw new IllegalArgumentException("Ongeldig croho-onderdeel: " + name);
	}
}
