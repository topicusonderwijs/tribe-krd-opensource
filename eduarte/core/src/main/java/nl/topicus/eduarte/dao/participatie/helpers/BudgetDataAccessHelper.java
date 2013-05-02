package nl.topicus.eduarte.dao.participatie.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.participatie.Budget;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.BudgetZoekFilter;

/**
 * @author vandekamp
 */
public interface BudgetDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Budget, BudgetZoekFilter>
{

	/**
	 * @param onderwijsproduct
	 * @param deelnemer
	 * @return het budget voor dit onderwijsproduct van de deelnemer
	 */
	public int getBudget(Onderwijsproduct onderwijsproduct, Deelnemer deelnemer);

	/**
	 * @param deelnemer
	 * @return het totaal van alle budgetten van deze deelnemer
	 */
	public int getBudgettenVanDeelnemer(Deelnemer deelnemer);

}
