package nl.topicus.eduarte.dao.helpers.bpv;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVKandidaat;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.zoekfilters.bpv.BPVKandidaatZoekFilter;

public interface BPVKandidaatDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<BPVKandidaat, BPVKandidaatZoekFilter>
{
	boolean exist(Verbintenis verbintenis);

	BPVKandidaat getByVerbintenis(Verbintenis verbintenis);
}