package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatZoekFilterInstelling;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

public interface ResultaatZoekFilterInstellingDataAccessHelper extends
		BatchDataAccessHelper<ResultaatZoekFilterInstelling>
{
	public void vulZoekFilter(ToetsZoekFilter filter, Medewerker medewerker, Groep groep,
			boolean vulGekoppeldAanVerbintenis);

	public void saveZoekFilter(ToetsZoekFilter filter, Medewerker medewerker, Groep groep,
			boolean vulGekoppeldAanVerbintenis);
}
