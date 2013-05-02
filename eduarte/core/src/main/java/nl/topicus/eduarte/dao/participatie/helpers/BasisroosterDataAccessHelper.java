package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.Basisrooster;
import nl.topicus.eduarte.entities.participatie.ExternSysteem;
import nl.topicus.eduarte.participatie.zoekfilters.BasisroosterZoekFilter;

/**
 * @author loite
 */
public interface BasisroosterDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Basisrooster, BasisroosterZoekFilter>
{

	/**
	 * @param naam
	 * @param organisatieEenheid
	 * @param externSysteem
	 * @return Het basisrooster met de gegeven naam bij de gegeven organisatie-eenheid.
	 */
	public Basisrooster getBasisrooster(String naam, OrganisatieEenheid organisatieEenheid,
			ExternSysteem externSysteem);

	/**
	 * @return Alle actieve basisroosters die gedefinieerd zijn.
	 */
	public List<Basisrooster> list();

}
