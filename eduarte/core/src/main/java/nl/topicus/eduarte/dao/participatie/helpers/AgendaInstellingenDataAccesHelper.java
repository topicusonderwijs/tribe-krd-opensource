package nl.topicus.eduarte.dao.participatie.helpers;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.AgendaInstellingen;
import nl.topicus.eduarte.entities.personen.Persoon;

/**
 * @author N. Henzen
 */
public interface AgendaInstellingenDataAccesHelper extends
		BatchDataAccessHelper<AgendaInstellingen>
{
	public AgendaInstellingen getAgendaInstellingen(Persoon persoon);

}
