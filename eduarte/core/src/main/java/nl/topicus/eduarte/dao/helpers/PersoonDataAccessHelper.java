package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.zoekfilters.PersoonZoekFilter;

/**
 * @author idserda
 */
public interface PersoonDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Persoon, PersoonZoekFilter<Persoon>>
{
	public Persoon get(Long id);

	public Persoon getPersoonByIdInOudPakket(Long id);

	public List<Persoon> getByBSN(Long bsn);

	public List<Persoon> getKinderen(Long verzorgerId);
}
