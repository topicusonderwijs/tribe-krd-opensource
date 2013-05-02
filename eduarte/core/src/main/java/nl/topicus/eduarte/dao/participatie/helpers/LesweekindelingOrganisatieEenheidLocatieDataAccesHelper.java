package nl.topicus.eduarte.dao.participatie.helpers;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.LesweekIndelingOrganisatieEenheidLocatie;

public interface LesweekindelingOrganisatieEenheidLocatieDataAccesHelper extends
		BatchDataAccessHelper<LesweekIndelingOrganisatieEenheidLocatie>
{
	public LesweekIndelingOrganisatieEenheidLocatie getOrganisatieEenheidLocatie(
			OrganisatieEenheid organisatieEenheid, Locatie locatie);
}
