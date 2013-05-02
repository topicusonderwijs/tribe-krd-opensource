package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.hogeronderwijs.Inschrijvingsverzoek;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.zoekfilters.InschrijvingsverzoekZoekFilter;

public interface InschrijvingsverzoekDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Inschrijvingsverzoek, InschrijvingsverzoekZoekFilter>
{
	public Inschrijvingsverzoek get(Verbintenis verbintenis, int studiejaar,
			String instroommomentCode);

}