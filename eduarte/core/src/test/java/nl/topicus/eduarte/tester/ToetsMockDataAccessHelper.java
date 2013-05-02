package nl.topicus.eduarte.tester;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.DeelnemerToetsBevriezing;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

public class ToetsMockDataAccessHelper implements ToetsDataAccessHelper
{
	@Override
	public void addCriteria(CriteriaBuilder builder, ToetsZoekFilter zoekFilter, String alias,
			boolean resultaatQuery)
	{
	}

	@Override
	public DeelnemerToetsBevriezing getBevriezing(Toets toets, Deelnemer deelnemer)
	{
		return null;
	}

	@Override
	public List<DeelnemerToetsBevriezing> getBevriezingen(List<Toets> toetsen,
			List<Deelnemer> deelnemers)
	{
		return null;
	}

	@Override
	public List<Deelnemer> getDeelnemersMetResultaten(Toets toets)
	{
		return null;
	}

	@Override
	public int getMaximumAantalPogingen(ToetsZoekFilter filter)
	{
		return 0;
	}

	@Override
	public Toets getToets(Onderwijsproduct product, Cohort cohort, SoortToets soortToets)
	{
		Resultaatstructuur resultaatstructuur = new Resultaatstructuur();
		resultaatstructuur.setType(Type.SUMMATIEF);
		resultaatstructuur.setNaam("Summatief");
		resultaatstructuur.setCode("SUM");
		// resultaatstructuur.setId(1L);
		Toets toets = new Toets();
		// toets.setId(1L);
		toets.save();
		toets.setResultaatstructuur(resultaatstructuur);
		toets.setSoort(soortToets);
		Toets deeltoets1 = new Toets();
		// deeltoets1.setId(2L);
		deeltoets1.setWeging(0);
		deeltoets1.save();
		deeltoets1.setResultaatstructuur(resultaatstructuur);
		deeltoets1.setParent(toets);
		deeltoets1.setSoort(SoortToets.Instroomniveau);
		Toets deeltoets2 = new Toets();
		// deeltoets2.setId(3L);
		deeltoets2.setWeging(1);
		deeltoets2.save();
		deeltoets2.setResultaatstructuur(resultaatstructuur);
		deeltoets2.setParent(toets);
		deeltoets2.setSoort(SoortToets.BehaaldNiveau);
		List<Toets> toetsen = new ArrayList<Toets>();
		toetsen.add(deeltoets1);
		toetsen.add(deeltoets2);
		toets.setChildren(toetsen);
		resultaatstructuur.setToetsen(Arrays.asList(toets));
		resultaatstructuur.save();
		return toets;
	}

	@Override
	public boolean heeftResultaten(Toets toets)
	{
		return false;
	}

	@Override
	public boolean heeftResultaten(Toets toets, Deelnemer deelnemer)
	{
		return false;
	}

	@Override
	public List<Integer> getDeelnemersMetHogereScore(Toets toets, int herkansingsNummer, int score)
	{
		return null;
	}

	@Override
	public int getIndex(ToetsZoekFilter filter, Object object)
	{
		return 0;
	}

	@Override
	public List<Toets> list(ToetsZoekFilter filter)
	{
		return null;
	}

	@Override
	public List<Toets> list(ToetsZoekFilter filter, int firstResult, int maxResults)
	{
		return null;
	}

	@Override
	public int listCount(ToetsZoekFilter filter)
	{
		return 0;
	}

	@Override
	public List<Serializable> listIds(ToetsZoekFilter filter)
	{
		return null;
	}

	@Override
	public List<Serializable> listIds(ToetsZoekFilter filter, int firstResult, int maxResults)
	{
		return null;
	}

	@Deprecated
	@Override
	public void deleteAndCommit(Toets dataObject)
	{
	}

	@Override
	public void evict(Toets dataObject)
	{
	}

	@Override
	public <R extends Toets> R get(Class<R> class1, Serializable id)
	{
		return null;
	}

	@Override
	public <R extends Toets> List<R> list(Class<R> class1, String... orderBy)
	{
		return null;
	}

	@Override
	public <R extends Toets> R load(Class<R> class1, Serializable id)
	{
		return null;
	}

	@Deprecated
	@Override
	public Serializable saveAndCommit(Toets dataObject)
	{
		return null;
	}

	@Deprecated
	@Override
	public void saveOrUpdateAndCommit(Toets dataObject)
	{
	}

	@Deprecated
	@Override
	public void updateAndCommit(Toets dataObject)
	{
	}

	@Deprecated
	@Override
	public void batchDelete(Toets dataObject)
	{
	}

	@Override
	public void batchExecute()
	{
	}

	@Override
	public <R extends Toets> List<R> list(Class<R> class1, Collection< ? extends Serializable> ids)
	{
		return null;
	}

	@Override
	public void batchRollback()
	{
	}

	@Deprecated
	@Override
	public Serializable batchSave(Toets dataObject)
	{
		return null;
	}

	@Deprecated
	@Override
	public void batchSaveOrUpdate(Toets dataObject)
	{
	}

	@Deprecated
	@Override
	public void batchUpdate(Toets dataObject)
	{
	}

	@Override
	public void flush()
	{
	}

	@Override
	public <R> List<R> list(DatabaseSelection<R, Toets> selection, List<String> orderBy,
			boolean ascending)
	{
		return null;
	}

	@Override
	public int getAantalResultaten(Toets toets)
	{
		return 0;
	}

	@Override
	public List<Toets> getToetsenMetSchaal(Schaal schaal)
	{
		return null;
	}

	@Override
	public Toets getToets(Onderwijsproduct onderwijsproduct, Cohort cohort, String toetspad)
	{
		return null;
	}

	@Override
	public <Y> int touch(java.lang.Class< ? > clz, String property, Y van, Y totEnMet)
	{
		return 0;
	}

	@Override
	public void delete(Toets dataObject)
	{
	}

	@Override
	public Serializable save(Toets dataObject)
	{
		return null;
	}

	@Override
	public void saveOrUpdate(Toets dataObject)
	{
	}

	@Override
	public void update(Toets dataObject)
	{
	}
}
