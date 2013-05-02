package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingsVorm;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOpleiding;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOpleidingAanbod;
import nl.topicus.eduarte.zoekfilters.CrohoOpleidingAanbodZoekFilter;

public interface CrohoOpleidingAanbodDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<CrohoOpleidingAanbod, CrohoOpleidingAanbodZoekFilter>
{
	public CrohoOpleidingAanbod getAanbod(CrohoOpleiding crohoOpleiding, Brin brin,
			OpleidingsVorm opleidingsvorm);

	public List<CrohoOpleidingAanbod> getAanbod(CrohoOpleiding crohoOpleiding, Brin brin);
}
