package nl.topicus.eduarte.dao.helpers.bpv;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVKandidaat;
import nl.topicus.eduarte.entities.bpv.BPVMatch;
import nl.topicus.eduarte.zoekfilters.bpv.BPVMatchZoekFilter;

public interface BPVMatchDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<BPVMatch, BPVMatchZoekFilter>
{
	int getHoogsteVolgnummer(BPVKandidaat kandidaat);
}
