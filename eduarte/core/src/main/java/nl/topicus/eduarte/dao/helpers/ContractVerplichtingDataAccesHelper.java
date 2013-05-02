package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.contract.ContractVerplichting;
import nl.topicus.eduarte.zoekfilters.ContractVerplichtingZoekFilter;

/**
 * 
 * 
 * @author vanharen
 */
public interface ContractVerplichtingDataAccesHelper extends
		BatchZoekFilterDataAccessHelper<ContractVerplichting, ContractVerplichtingZoekFilter>
{
	public ContractVerplichting get(Long id);
}
