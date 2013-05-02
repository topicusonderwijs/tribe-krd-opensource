package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier;

import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingOverzichtZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vanderkamp
 */
public interface WaarnemingOverzichtInterface
{
	/**
	 * submit het filter
	 */
	public void submitFilter();

	public IModel<LesweekIndeling> getLesweekIndelingModel();

	public IModel<WaarnemingOverzichtZoekFilter> getFilterModel();

	public OrganisatieEenheid getOrganisatieEenheid();

	/**
	 * @param dagVanWeek
	 * @return tot het lesuur nummer
	 */
	public int getTotLesuur(int dagVanWeek);

	/**
	 * @param dagVanWeek
	 * @return vanaf lesuur nummer
	 */
	public int getVanLesuur(int dagVanWeek);
}
