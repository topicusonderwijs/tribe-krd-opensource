package nl.topicus.eduarte.krd.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzameling;
import nl.topicus.eduarte.krd.zoekfilters.BronExamenverzamelingZoekFilter;
import nl.topicus.onderwijs.duo.bron.IBronBatch;

public interface BronExamenverzamenlingDataAccessHelper extends
		ZoekFilterDataAccessHelper<BronExamenverzameling, BronExamenverzamelingZoekFilter>
{
	long getAantalExVerzGereed(BronExamenverzamelingZoekFilter filter);

	List<BronExamenverzameling> getExVerzGekoppeldAanBatch(IBronBatch bronBatch);

	public BronExamenverzameling getLaatstAangemaakteVerzameling();
}
