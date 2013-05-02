package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.participatie.AfspraakDeelnemer;
import nl.topicus.eduarte.entities.participatie.AfspraakParticipant;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakParticipantMultiZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakParticipantZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakZoekFilter;

/**
 * @author vandekamp
 */
public interface AfspraakParticpantDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<AfspraakParticipant, AfspraakParticipantZoekFilter>
{
	public List< ? extends IdObject> getMultiZoekEntiteiten(
			AfspraakParticipantMultiZoekFilter filter);

	public String getRenderString(IdObject deelnemerObject);

	public String getImageName(IdObject deelnemerObject);

	/**
	 * Geeft alle afspraakdeelnemers van de afspraken die overeenkomen met het gegeven
	 * filter.
	 * 
	 * @param filter
	 * @return een lijst met afspraakdeelnemers
	 */
	public List<AfspraakDeelnemer> getAfspraakDeelnemers(AfspraakZoekFilter filter);

	public List<IdObject> findPossibleParticipanten(Persoon persoon);
}
