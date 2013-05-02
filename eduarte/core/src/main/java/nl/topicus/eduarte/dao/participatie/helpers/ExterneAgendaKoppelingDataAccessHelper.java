package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.ExterneAgendaKoppeling;
import nl.topicus.eduarte.participatie.zoekfilters.ExterneAgendaKoppelingZoekFilter;

/**
 */
public interface ExterneAgendaKoppelingDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<ExterneAgendaKoppeling, ExterneAgendaKoppelingZoekFilter>
{
	public List<ExterneAgendaKoppeling> list(List<OrganisatieEenheid> eenheden);

	public List<ExterneAgendaKoppeling> listAutomatischeKoppelingen();
}
