package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.DeelnemerToetsBevriezing;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

public interface ToetsDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Toets, ToetsZoekFilter>
{
	public void addCriteria(CriteriaBuilder builder, ToetsZoekFilter zoekFilter, String alias,
			boolean resultaatQuery);

	public List<Deelnemer> getDeelnemersMetResultaten(Toets toets);

	public Toets getToets(Onderwijsproduct product, Cohort cohort, SoortToets soortToets);

	public boolean heeftResultaten(Toets toets);

	public boolean heeftResultaten(Toets toets, Deelnemer deelnemer);

	public int getAantalResultaten(Toets toets);

	public int getMaximumAantalPogingen(ToetsZoekFilter filter);

	public List<DeelnemerToetsBevriezing> getBevriezingen(List<Toets> toetsen,
			List<Deelnemer> deelnemers);

	public DeelnemerToetsBevriezing getBevriezing(Toets toets, Deelnemer deelnemer);

	public List<Integer> getDeelnemersMetHogereScore(Toets toets, int herkansingsNummer, int score);

	public List<Toets> getToetsenMetSchaal(Schaal schaal);

	public Toets getToets(Onderwijsproduct onderwijsproduct, Cohort cohort, String toetspad);
}
