package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.hogeronderwijs.Fase;
import nl.topicus.eduarte.entities.hogeronderwijs.Hoofdfase;
import nl.topicus.eduarte.zoekfilters.FaseZoekFilter;

public interface FaseDataAccessHelper extends BatchZoekFilterDataAccessHelper<Fase, FaseZoekFilter>
{
	public Fase getEersteFaseVanHoofdfase(Hoofdfase hoofdfase);
}
