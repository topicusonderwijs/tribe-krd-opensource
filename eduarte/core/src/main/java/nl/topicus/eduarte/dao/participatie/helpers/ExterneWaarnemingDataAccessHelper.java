package nl.topicus.eduarte.dao.participatie.helpers;

import java.io.Serializable;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.ExterneWaarneming;
import nl.topicus.eduarte.participatie.zoekfilters.ExterneWaarnemingZoekFilter;

/**
 * @author vandekamp
 */
public interface ExterneWaarnemingDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<ExterneWaarneming, ExterneWaarnemingZoekFilter>
{

	/**
	 * @param id
	 * @return de externewaarneming
	 */
	public ExterneWaarneming getById(Serializable id);

	/**
	 * @return Het aantal externe waarnemingen dat in de wachtrij staat om geimporteerd te
	 *         worden.
	 */
	public Long getAantalExterneWaarnemingenInWachtrij();
}
