package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsCodeFilter;
import nl.topicus.eduarte.zoekfilters.ToetsCodeFilterZoekFilter;

public interface ToetsCodeFilterDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<ToetsCodeFilter, ToetsCodeFilterZoekFilter>
{
	public ToetsCodeFilter getStandaardFilter(Verbintenis verbintenis);

	public ToetsCodeFilter getStandaardFilter(List<Deelnemer> deelnemers, Cohort cohort);

	public ToetsCodeFilter getStandaardFilter(Opleiding opleiding, Cohort cohort);
}
