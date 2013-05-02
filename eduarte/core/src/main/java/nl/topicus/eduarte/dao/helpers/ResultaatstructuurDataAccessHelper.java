package nl.topicus.eduarte.dao.helpers;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.DeelnemerResultaatVersie;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;

public interface ResultaatstructuurDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Resultaatstructuur, ResultaatstructuurZoekFilter>
{
	public void addCriteria(CriteriaBuilder builder, ResultaatstructuurZoekFilter filter,
			String alias, boolean resultaatQuery);

	public Resultaatstructuur getResultaatstructuur(Onderwijsproduct product, Cohort cohort,
			Type type, String code);

	public List<Resultaatstructuur> getResultaatstructuren(Onderwijsproduct product, Cohort cohort,
			Type type, String code);

	public Resultaatstructuur getSummatieveResultaatstructuur(Onderwijsproduct product,
			Cohort cohort);

	public List<Resultaatstructuur> getResultaatstructuren(Collection<Onderwijsproduct> producten,
			Cohort cohort);

	public List<Long> getResultaatstructuurIds(Collection<Onderwijsproduct> producten, Cohort cohort);

	public Resultaatstructuur get(Long resultaatstructuurId);

	public boolean heeftResultaten(Resultaatstructuur structuur);

	public List<Resultaatstructuur> getStructuren(Collection<Deelnemer> deelnemers);

	public boolean isStructuurAfgenomen(Deelnemer deelnemer, Resultaatstructuur structuur);

	public List<DeelnemerResultaatVersie> getVersies(List<Deelnemer> deelnemers,
			List<Resultaatstructuur> structuren);

	public void incrementVersies(List<DeelnemerResultaatVersie> versies);

	public void verwijderBijbehorendeResultaten(Resultaatstructuur structuur);

	public void verwijderStructuurEnBijbehorendeResultaten(
			IChangeRecordingModel<Resultaatstructuur> resultaatstructuurModel);

	public List<Deelnemer> getDeelnemersMetResultaten(Resultaatstructuur structuur);

	public Resultaatstructuur getMeestWaarschijnlijkeStructuur(ResultaatstructuurZoekFilter filter);
}
