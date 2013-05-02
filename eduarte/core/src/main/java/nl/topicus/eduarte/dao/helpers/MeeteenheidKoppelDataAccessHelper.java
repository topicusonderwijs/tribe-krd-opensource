package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.MeeteenheidKoppel;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.MeeteenheidKoppelType;
import nl.topicus.eduarte.zoekfilters.MeeteenheidKoppelZoekFilter;

public interface MeeteenheidKoppelDataAccessHelper extends
		ZoekFilterDataAccessHelper<MeeteenheidKoppel, MeeteenheidKoppelZoekFilter>
{
	/**
	 * Lijst van MeeteenheidKoppelingen voor de gegeven organisatie-eenheid en cohort.
	 * 
	 * @param organisatieEenheid
	 * @param type
	 * @param automatischAangemaakt
	 * @return list
	 */
	public List<MeeteenheidKoppel> list(OrganisatieEenheid organisatieEenheid,
			MeeteenheidKoppelType type, boolean automatischAangemaakt);

	public void delete(Opleiding opleiding, MeeteenheidKoppelType type);
}
