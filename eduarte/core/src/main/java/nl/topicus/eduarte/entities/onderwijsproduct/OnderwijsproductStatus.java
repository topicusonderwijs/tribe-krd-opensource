package nl.topicus.eduarte.entities.onderwijsproduct;

import nl.topicus.cobra.util.StringUtil;

/**
 * Mogelijke statussen voor onderwijsproducten.
 * 
 * @author loite
 */
public enum OnderwijsproductStatus
{
	/**
	 * Aangevraagd door organisatie of deelnemer
	 */
	Aangevraagd,
	/**
	 * Wordt door de organisatie gemaakt of aangeschaft
	 */
	InOntwikkeling,
	/**
	 * Kan worden samengebracht met lerenden (in productie)
	 */
	Beschikbaar,
	/**
	 * Product wordt niet meer aangeboden (na in productie te zijn geweest)
	 */
	Vervallen,
	/**
	 * Kan (door welke reden dan ook) niet worden samengebracht met lerenden
	 */
	NietBeschikbaar;

	/**
	 * Camel-case naar normale string
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return StringUtil.convertCamelCase(name());
	}

}
