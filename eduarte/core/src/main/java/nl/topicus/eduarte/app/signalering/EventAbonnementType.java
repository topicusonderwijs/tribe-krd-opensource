package nl.topicus.eduarte.app.signalering;

import nl.topicus.cobra.util.StringUtil;

public enum EventAbonnementType
{
	Mentor,
	Docent,
	Uitvoerende,
	Verantwoordelijke,
	GeselecteerdeGroepen,
	GeselecteerdeDeelnemers,
	TaakGerelateerd;

	@Override
	public String toString()
	{
		return StringUtil.convertCamelCase(name());
	}
}
