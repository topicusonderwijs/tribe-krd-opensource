package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.Date;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;
import nl.topicus.eduarte.entities.personen.Functie;
import nl.topicus.eduarte.zoekfilters.DeelnemerVerzuimloketZoekfilter;
import nl.topicus.onderwijs.ibgverzuimloket.model.IbgEnums;

public interface VerzuimmeldingDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<IbgVerzuimmelding, DeelnemerVerzuimloketZoekfilter>
{
	public Functie get(Integer meldingsnummer, Date vanafDatum, Date tmDatum, String status,
			IbgEnums.Verzuimsoort verzuimsoort);
}
