package nl.topicus.eduarte.dao.helpers.dbs;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.GeplandeBegeleidingsHandeling;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.entities.dbs.trajecten.TrajectSoort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.dbs.TrajectZoekFilter;

public interface TrajectDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Traject, TrajectZoekFilter>
{
	public Traject getTrajectByDeelnemer(Deelnemer deelnemer, TrajectSoort soort);

	public Traject getTrajectById(Long id);

	public List<GeplandeBegeleidingsHandeling> getRecenteHandelingen(Traject traject, int aantal);
}
