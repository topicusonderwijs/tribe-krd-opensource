package nl.topicus.eduarte.entities;

import nl.topicus.cobra.util.StringUtil;

public enum AutomatischeIncasso
{
	Geen,
	/**
	 * Alleen het wettelijk cursusgeld wordt betaald via automatische incasso
	 */
	AlleenCursusgeld,
	/**
	 * Alle kosten worden betaald via automatische incasso
	 */
	Alles;

	@Override
	public String toString()
	{
		return StringUtil.convertCamelCase(name());
	}
}
