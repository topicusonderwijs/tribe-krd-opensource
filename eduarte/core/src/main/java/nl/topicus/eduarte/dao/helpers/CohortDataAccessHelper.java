package nl.topicus.eduarte.dao.helpers;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;

/**
 * @author loite
 */
public interface CohortDataAccessHelper extends BatchDataAccessHelper<Cohort>
{

	/**
	 * @return Een lijst met alle landelijk gedefinieerde cohorten.
	 */
	public List<Cohort> list();

	public List<Cohort> list(Date beginDatum, Date eindDatum);

	/**
	 * @param peildatum
	 * @return Het cohort dat actief is op de gegeven peildatum.
	 */
	public Cohort getCohortOpDatum(Date peildatum);

	public Cohort getVolgendeCohort(Cohort bronCohort);

}
