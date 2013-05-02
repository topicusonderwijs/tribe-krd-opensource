package nl.topicus.eduarte.tester;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.ResultaatHibernateDataAccessHelper.TypeResultaat;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.eduarte.zoekfilters.ResultaatZoekFilter;

public class ResultaatMockDataAccessHelper implements ResultaatDataAccessHelper
{
	@Override
	public List<Resultaat> getActueleResultaten(Toets toets, Deelnemer deelnemer)
	{
		return null;
	}

	@Override
	public Map<Onderwijsproduct, List<Resultaat>> getDefinitieveCentraalExamenResultaten(
			Deelnemer deelnemer, Collection<OnderwijsproductAfname> onderwijsproductAfnames)
	{
		return null;
	}

	@Override
	public Map<Onderwijsproduct, Resultaat> getDefinitieveSchoolexamenResultaten(
			Deelnemer deelnemer, Collection<OnderwijsproductAfname> onderwijsproductAfnames,
			TypeResultaat typeResultaat)
	{
		return null;
	}

	@Override
	public Map<OnderwijsproductAfname, Resultaat> getEindresultaten(Deelnemer deelnemer,
			Collection<OnderwijsproductAfname> onderwijsproductAfnames)
	{
		return null;
	}

	private Resultaat getResultaat(Long id, String interneWaarde, String externeWaarde)
	{
		Resultaat resultaat = new Resultaat();
		resultaat.setGeldend(true);
		resultaat.setId(id);
		Schaalwaarde schaalwaarde = new Schaalwaarde();
		schaalwaarde.setInterneWaarde(interneWaarde);
		schaalwaarde.setExterneWaarde(externeWaarde);
		resultaat.setWaarde(schaalwaarde);
		return resultaat;
	}

	@Override
	public Resultaat getGeldendeResultaat(Toets toets, Deelnemer deelnemer)
	{
		if (toets.getSoort().equals(SoortToets.Instroomniveau))
		{
			if (toets.getParent().getSoort().equals(SoortToets.Luisteren))
			{
				if (deelnemer.getId() == null)
				{
					if ((deelnemer.getDeelnemernummer() == 3008)
						|| (deelnemer.getDeelnemernummer() == 3027))
						return getResultaat(1L, "A1", "1");
				}
				else
				{
					if (deelnemer.getDeelnemernummer() == 3019)
						return getResultaat(1L, "A1", "1");
				}
			}
			if (toets.getParent().getSoort().equals(SoortToets.Spreken))
			{
				if (deelnemer.getId() == null)
				{
					if ((deelnemer.getDeelnemernummer() == 3018)
						|| (deelnemer.getDeelnemernummer() == 3019)
						|| (deelnemer.getDeelnemernummer() == 3022)
						|| (deelnemer.getDeelnemernummer() == 3023)
						|| (deelnemer.getDeelnemernummer() == 3024)
						|| (deelnemer.getDeelnemernummer() == 3026)
						|| (deelnemer.getDeelnemernummer() == 3027))
						return getResultaat(1L, "A1", "1");
				}
				else
				{
					if (deelnemer.getDeelnemernummer() == 3023)
						return getResultaat(1L, "A2", "2");
					if ((deelnemer.getDeelnemernummer() == 3022)
						|| (deelnemer.getDeelnemernummer() == 3024)
						|| (deelnemer.getDeelnemernummer() == 3027))
						return getResultaat(1L, "A1", "1");
				}
			}
		}
		else if (toets.getSoort().equals(SoortToets.BehaaldNiveau))
		{
			if (toets.getParent().getSoort().equals(SoortToets.Spreken))
			{
				// Om onderscheid te kunnen maken tussen verschillende testen
				if (deelnemer.getId() == null)
				{
					if (deelnemer.getDeelnemernummer() == 3024)
						return getResultaat(1L, "A2", "2");
				}
				else
				{
					if (deelnemer.getDeelnemernummer() == 3022)
						return getResultaat(1L, "A2", "2");
					if (deelnemer.getDeelnemernummer() == 3024)
						return getResultaat(1L, "B1", "3");
				}
			}
		}
		return null;
	}

	@Override
	public void verifyResultaten(ResultaatZoekFilter filter)
	{
	}

	@Override
	public int getIndex(ResultaatZoekFilter filter, Object object)
	{
		return 0;
	}

	@Override
	public List<Resultaat> list(ResultaatZoekFilter filter)
	{
		return null;
	}

	@Override
	public List<Resultaat> list(ResultaatZoekFilter filter, int firstResult, int maxResults)
	{
		return null;
	}

	@Override
	public int listCount(ResultaatZoekFilter filter)
	{
		return 0;
	}

	@Override
	public List<Serializable> listIds(ResultaatZoekFilter filter)
	{
		return null;
	}

	@Override
	public List<Serializable> listIds(ResultaatZoekFilter filter, int firstResult, int maxResults)
	{
		return null;
	}

	@Deprecated
	@Override
	public void deleteAndCommit(Resultaat dataObject)
	{
	}

	@Override
	public void evict(Resultaat dataObject)
	{
	}

	@Override
	public <R extends Resultaat> R get(Class<R> class1, Serializable id)
	{
		return null;
	}

	@Override
	public <R extends Resultaat> List<R> list(Class<R> class1, String... orderBy)
	{
		return null;
	}

	@Override
	public <R extends Resultaat> R load(Class<R> class1, Serializable id)
	{
		return null;
	}

	@Deprecated
	@Override
	public Serializable saveAndCommit(Resultaat dataObject)
	{
		return null;
	}

	@Deprecated
	@Override
	public void saveOrUpdateAndCommit(Resultaat dataObject)
	{
	}

	@Deprecated
	@Override
	public void updateAndCommit(Resultaat dataObject)
	{
	}

	@Deprecated
	@Override
	public void batchDelete(Resultaat dataObject)
	{
	}

	@Override
	public void batchExecute()
	{
	}

	@Override
	public <R extends Resultaat> List<R> list(Class<R> class1,
			Collection< ? extends Serializable> ids)
	{
		return null;
	}

	@Override
	public void batchRollback()
	{
	}

	@Deprecated
	@Override
	public Serializable batchSave(Resultaat dataObject)
	{
		return null;
	}

	@Deprecated
	@Override
	public void batchSaveOrUpdate(Resultaat dataObject)
	{
	}

	@Deprecated
	@Override
	public void batchUpdate(Resultaat dataObject)
	{
	}

	@Override
	public void flush()
	{
	}

	@Override
	public <R> List<R> list(DatabaseSelection<R, Resultaat> selection, List<String> orderBy,
			boolean ascending)
	{
		return null;
	}

	@Override
	public boolean heeftResultaten(Deelnemer deelnemer, Onderwijsproduct onderwijsproduct,
			Cohort cohort)
	{
		return false;
	}

	@Override
	public <Y> int touch(java.lang.Class< ? > clz, String property, Y van, Y totEnMet)
	{
		return 0;
	}

	@Override
	public void delete(Resultaat dataObject)
	{
	}

	@Override
	public Serializable save(Resultaat dataObject)
	{
		return null;
	}

	@Override
	public void saveOrUpdate(Resultaat dataObject)
	{
	}

	@Override
	public void update(Resultaat dataObject)
	{
	}
}
