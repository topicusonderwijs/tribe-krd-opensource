package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

/**
 * @author loite
 */
public interface OpleidingDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Opleiding, OpleidingZoekFilter>
{
	public Opleiding getEersteOpleiding(OpleidingZoekFilter filter);

	public Opleiding getEersteOpleidingGevolgdDoorAlleDeelnemers(List<Deelnemer> deelnemers);

	public List<Opleiding> getOpleidingen(String code);

}
