package nl.topicus.eduarte.entities.settings;

import nl.topicus.cobra.util.StringUtil;

/**
 * Manieren van aanmelden voor digitaal aanmelden, afhankelijk van de setting kan een
 * groep opleiding gekozen worden, of er kan niet aangemeld worden bij die
 * organisatie-eenheid
 * 
 * @author vandekamp
 */
public enum ManierVanAanmelden
{
	NietGebruiken,
	AanmeldenViaGroep,
	AanmeldenViaOpleiding;

	@Override
	public String toString()
	{
		return StringUtil.convertCamelCase(super.toString());
	}
}
