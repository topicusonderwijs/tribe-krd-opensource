package nl.topicus.eduarte.util.criteriumbank;

import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;

/**
 * Class voor het kunnen testen van criteria van een opleiding. Deze class kan
 * bijvoorbeeld aangemaakt worden vanuit de opleidingpagina om de daar gedefinieerde
 * criteria te testen.
 * 
 * @author loite
 */
public class CriteriumbankControleTest
{
	private final CriteriumbankControle criteriumbank;

	/**
	 * Constructor
	 * 
	 * @param opleiding
	 * @param cohort
	 */
	public CriteriumbankControleTest(Opleiding opleiding, Cohort cohort)
	{
		this.criteriumbank = new CriteriumbankControle(opleiding, cohort);
	}

	/**
	 * @return true indien de criteriumbankcontrole met succes uitgevoerd kon worden, en
	 *         anders false.
	 */
	public boolean testCriteria()
	{
		// Voer een criteriumbankcontrole uit (resultaat maakt niet uit).
		criteriumbank.voldoetAanCriteria();
		return !criteriumbank.isHeeftFouten();
	}

	/**
	 * @param criterium
	 * @return true indien de criteriumbankcontrole met succes uitgevoerd kon worden voor
	 *         het gegeven criterium, en anders false.
	 */
	public boolean testCriterium(Criterium criterium)
	{
		criteriumbank.voldoetAanCriterium(criterium);
		return !criteriumbank.isHeeftFouten();
	}

	/**
	 * @return Returns the criteriumbank.
	 */
	public CriteriumbankControle getCriteriumbank()
	{
		return criteriumbank;
	}

}
