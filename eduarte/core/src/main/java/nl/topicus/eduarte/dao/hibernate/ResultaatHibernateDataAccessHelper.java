package nl.topicus.eduarte.dao.hibernate;

import java.util.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat.Resultaatsoort;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.eduarte.web.components.resultaat.ResultaatKey;
import nl.topicus.eduarte.web.components.resultaat.ResultaatVerifier;
import nl.topicus.eduarte.zoekfilters.ResultaatZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class ResultaatHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Resultaat, ResultaatZoekFilter> implements
		ResultaatDataAccessHelper
{
	public ResultaatHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ResultaatZoekFilter filter)
	{
		Criteria criteria = createCriteria(Resultaat.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		criteria.addOrder(Order.desc("createdAt"));
		if (filter.getToetsZoekFilter() != null)
		{
			builder.createAlias("toets", "toets");
			DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).addCriteria(builder,
				filter.getToetsZoekFilter(), "toets", true);
			filter.setResultCacheable(filter.getToetsZoekFilter().isResultCacheable());
			if (filter.getToetsZoekFilter().getResultaatstructuurFilter() != null)
			{
				if (filter.getToetsZoekFilter().getResultaatstructuurFilter().getDeelnemers() != null)
				{
					builder.addPropertyEquals("deelnemer", "afname.deelnemer");
				}
			}
		}
		builder.addIn("toets", filter.getToetsen());
		builder.addIn("deelnemer", filter.getDeelnemers());
		builder.addEquals("actueel", filter.getActueel());
		return criteria;
	}

	@Override
	public List<Resultaat> getActueleResultaten(Toets toets, Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(Resultaat.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("actueel", true);
		builder.addEquals("toets", toets);
		builder.addEquals("deelnemer", deelnemer);
		return cachedTypedList(criteria);
	}

	@Override
	public Resultaat getGeldendeResultaat(Toets toets, Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(Resultaat.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("geldend", true);
		builder.addEquals("toets", toets);
		builder.addEquals("deelnemer", deelnemer);
		return cachedTypedUnique(criteria);
	}

	@Override
	public Map<OnderwijsproductAfname, Resultaat> getEindresultaten(Deelnemer deelnemer,
			Collection<OnderwijsproductAfname> onderwijsproductAfnames)
	{
		Asserts.assertNotNull("onderwijsproductAfnames", onderwijsproductAfnames);
		if (onderwijsproductAfnames.isEmpty())
			return Collections.emptyMap();
		Criteria criteria = createCriteria(Resultaat.class);
		criteria.createAlias("toets", "toets");
		criteria.createAlias("toets.resultaatstructuur", "resultaatstructuur");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemer", deelnemer);

		// Voeg ors toe voor de combinaties onderwijsproduct/cohort
		List<Criterion> ors = new ArrayList<Criterion>(onderwijsproductAfnames.size());
		for (OnderwijsproductAfname afname : onderwijsproductAfnames)
		{
			ors.add(Restrictions.and(Restrictions.eq("resultaatstructuur.cohort", afname
				.getCohort()), Restrictions.eq("resultaatstructuur.onderwijsproduct", afname
				.getOnderwijsproduct())));
		}
		builder.addOrs(ors);

		builder.addEquals("geldend", Boolean.TRUE);
		builder.addEquals("toets.eindtoets", Boolean.TRUE);

		List<Resultaat> list = cachedTypedList(criteria);
		Map<OnderwijsproductAfname, Resultaat> resultaten =
			new HashMap<OnderwijsproductAfname, Resultaat>(list.size());
		for (Resultaat res : list)
		{
			for (OnderwijsproductAfname afname : onderwijsproductAfnames)
			{
				if (afname.getOnderwijsproduct().equals(
					res.getToets().getResultaatstructuur().getOnderwijsproduct())
					&& afname.getCohort()
						.equals(res.getToets().getResultaatstructuur().getCohort()))
				{
					resultaten.put(afname, res);
					break;
				}
			}
		}

		return resultaten;
	}

	@Override
	public void verifyResultaten(ResultaatZoekFilter filter)
	{
		if (!filter.isBeperkt())
			throw new IllegalArgumentException(
				"Kan niet alle resultaten verifieeren: zet een beperking op het filter");

		Map<ResultaatKey, List<Resultaat>> resultatenPerDeelnemerToets =
			splitResultaten(list(filter));
		for (Map.Entry<ResultaatKey, List<Resultaat>> curEntry : resultatenPerDeelnemerToets
			.entrySet())
		{
			verifyResultaten(curEntry.getKey().getToets(), curEntry.getKey().getDeelnemer(),
				curEntry.getValue());
		}
		new ResultaatVerifier().verify(resultatenPerDeelnemerToets);
	}

	/**
	 * Voort de controle uit op de resultaten
	 * 
	 * @param toets
	 * @param deelnemer
	 * @param resultaten
	 */
	private void verifyResultaten(Toets toets, Deelnemer deelnemer, List<Resultaat> resultaten)
	{
		Resultaat geldend = null;
		Resultaat inSamengesteld = null;
		BitSet herkansingenMetActueel = new BitSet();
		BitSet actueelNietNull = new BitSet();
		for (Resultaat curResultaat : resultaten)
		{
			if (curResultaat.isGeldend())
			{
				if (geldend != null)
					throw new IllegalResultaatException(curResultaat,
						"Meerdere geldende resultaten");
				if (!curResultaat.isActueel())
					throw new IllegalResultaatException(curResultaat,
						"Het geldende resultaat is niet actueel");
				geldend = curResultaat;
			}
			if (curResultaat.isActueel())
			{
				if (herkansingenMetActueel.get(getHerkansingsnummer(curResultaat)))
					throw new IllegalResultaatException(curResultaat,
						"Meerdere actuele resultaten voor dezelfde herkansing");
				herkansingenMetActueel.set(getHerkansingsnummer(curResultaat));
				if (!curResultaat.getSoort().equals(Resultaatsoort.Alternatief))
					actueelNietNull.set(getHerkansingsnummer(curResultaat), !curResultaat
						.isNullResultaat());
			}
			if (curResultaat.getOverschrijft() != null)
			{
				if (curResultaat.getOverschrijft().isActueel())
					throw new IllegalResultaatException(curResultaat,
						"Een actueel resultaat is overschreven");
			}
			if (curResultaat.isActueel() && curResultaat.isCijferUpdateRequired())
				throw new IllegalResultaatException(curResultaat, "Het cijfer is niet berekend");
			if (curResultaat.isNullResultaat())
			{
				if (curResultaat.getOverschrijft() == null)
					throw new IllegalResultaatException(curResultaat,
						"Een null-resultaat moet altijd een ander resultaat overschrijven");
				if (curResultaat.getOverschrijft().isNullResultaat())
					throw new IllegalResultaatException(curResultaat,
						"Een null-resultaat mag geen ander null-resultaat overschrijven");
			}
			if (curResultaat.isInSamengesteld() && curResultaat.isActueel())
			{
				if (inSamengesteld != null)
					throw new IllegalResultaatException(curResultaat,
						"Meerdere resultaten gemarkeerd als 'in samengesteld'");
				inSamengesteld = curResultaat;
			}
		}
		if (geldend != null && geldend.isNullResultaat() && !actueelNietNull.isEmpty())
			throw new IllegalResultaatException(geldend,
				"Een null-resultaat kan alleen geldend zijn als alle actuele resultaten null zijn");
		if (geldend == null)
		{
			for (Resultaat curResultaat : resultaten)
			{
				if (!curResultaat.getSoort().equals(Resultaatsoort.Alternatief))
					throw new IllegalResultaatException("Er is geen geldend resultaat voor '"
						+ toets + "', terwijl er wel resultaten zijn");
			}
		}
	}

	private int getHerkansingsnummer(Resultaat resultaat)
	{
		if (resultaat.getSoort().equals(Resultaatsoort.Alternatief))
			return 0;
		return resultaat.getHerkansingsnummer() + 2;
	}

	private Map<ResultaatKey, List<Resultaat>> splitResultaten(List<Resultaat> list)
	{
		Map<ResultaatKey, List<Resultaat>> ret = new HashMap<ResultaatKey, List<Resultaat>>();
		for (Resultaat curResultaat : list)
		{
			ResultaatKey curKey = new ResultaatKey(curResultaat);
			List<Resultaat> curResultatenList = ret.get(curKey);
			if (curResultatenList == null)
			{
				curResultatenList = new ArrayList<Resultaat>();
				ret.put(curKey, curResultatenList);
			}
			curResultatenList.add(curResultaat);
		}
		return ret;
	}

	public static enum TypeResultaat
	{
		/**
		 * Naar BRON moet altijd het geldende SE-resultaat gestuurd worden (dus nooit het
		 * alternatieve resultaat).
		 */
		Geldend,
		/**
		 * Op de correctiestaat moet altijd het hoogste van de SE-cijfers getoond worden
		 * (dus het hoogste van het geldende en het alternatieve resultaat).
		 */
		Hoogste,
		/**
		 * Voor de rest moet het alternatief of geldende resultaat gebruikt worden welke
		 * inSamengesteld is
		 */
		InSamengesteld;
	}

	@Override
	public Map<Onderwijsproduct, Resultaat> getDefinitieveSchoolexamenResultaten(
			Deelnemer deelnemer, Collection<OnderwijsproductAfname> onderwijsproductAfnames,
			TypeResultaat typeResultaat)
	{
		Asserts.assertNotNull("onderwijsproductAfnames", onderwijsproductAfnames);
		Asserts.assertNotNull("typeResultaat", typeResultaat);
		if (onderwijsproductAfnames.isEmpty())
			return Collections.emptyMap();
		Criteria criteria = createCriteria(Resultaat.class);
		criteria.createAlias("toets", "toets");
		criteria.createAlias("toets.resultaatstructuur", "resultaatstructuur");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemer", deelnemer);

		// Voeg ors toe voor de combinaties onderwijsproduct/cohort
		List<Criterion> ors = new ArrayList<Criterion>(onderwijsproductAfnames.size());
		for (OnderwijsproductAfname afname : onderwijsproductAfnames)
		{
			ors.add(Restrictions.and(Restrictions.eq("resultaatstructuur.cohort", afname
				.getCohort()), Restrictions.eq("resultaatstructuur.onderwijsproduct", afname
				.getOnderwijsproduct())));
		}
		builder.addOrs(ors);

		builder.addEquals("actueel", Boolean.TRUE);
		builder.addEquals("toets.soort", SoortToets.Schoolexamen);
		builder.addNotEquals("soort", Resultaatsoort.Tijdelijk);

		if (TypeResultaat.Geldend == typeResultaat)
			builder.addEquals("geldend", Boolean.TRUE);
		else
			builder.addOrs(Restrictions.eq("geldend", Boolean.TRUE), Restrictions.eq("soort",
				Resultaatsoort.Alternatief));

		List<Resultaat> list = cachedTypedList(criteria);
		Map<Onderwijsproduct, Resultaat> resultaten =
			new HashMap<Onderwijsproduct, Resultaat>(list.size());
		for (Resultaat res : list)
		{
			if (res.getCijfer() != null)
			{
				Resultaat bestaand =
					resultaten.get(res.getToets().getResultaatstructuur().getOnderwijsproduct());
				if (bestaand != null && TypeResultaat.Geldend != typeResultaat)
				{
					// als het bestaande cijfer kleinder is dan res -> zet res in de map
					if (TypeResultaat.Hoogste == typeResultaat
						&& bestaand.getCijfer().compareTo(res.getCijfer()) < 0)
					{
						resultaten.put(
							res.getToets().getResultaatstructuur().getOnderwijsproduct(), res);
					}
					if (TypeResultaat.InSamengesteld == typeResultaat && res.isInSamengesteld())
					{
						resultaten.put(
							res.getToets().getResultaatstructuur().getOnderwijsproduct(), res);
					}
				}
				else
				{
					resultaten.put(res.getToets().getResultaatstructuur().getOnderwijsproduct(),
						res);
				}
			}
		}

		return resultaten;
	}

	@Override
	public Map<Onderwijsproduct, List<Resultaat>> getDefinitieveCentraalExamenResultaten(
			Deelnemer deelnemer, Collection<OnderwijsproductAfname> onderwijsproductAfnames)
	{
		Asserts.assertNotNull("onderwijsproductAfnames", onderwijsproductAfnames);
		if (onderwijsproductAfnames.isEmpty())
			return Collections.emptyMap();
		Criteria criteria = createCriteria(Resultaat.class);
		criteria.createAlias("toets", "toets");
		criteria.createAlias("toets.resultaatstructuur", "resultaatstructuur");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemer", deelnemer);

		// Voeg ors toe voor de combinaties onderwijsproduct/cohort
		List<Criterion> ors = new ArrayList<Criterion>(onderwijsproductAfnames.size());
		for (OnderwijsproductAfname afname : onderwijsproductAfnames)
		{
			ors.add(Restrictions.and(Restrictions.eq("resultaatstructuur.cohort", afname
				.getCohort()), Restrictions.eq("resultaatstructuur.onderwijsproduct", afname
				.getOnderwijsproduct())));
		}
		builder.addOrs(ors);

		builder.addEquals("actueel", Boolean.TRUE);
		builder.addEquals("toets.soort", SoortToets.CentraalExamen);
		builder.addNotEquals("soort", Resultaatsoort.Tijdelijk);

		List<Resultaat> list = cachedTypedList(criteria);
		Map<Onderwijsproduct, List<Resultaat>> resultaten =
			new HashMap<Onderwijsproduct, List<Resultaat>>(list.size());
		for (Resultaat res : list)
		{
			List<Resultaat> resList =
				resultaten.get(res.getToets().getResultaatstructuur().getOnderwijsproduct());
			if (resList == null)
			{
				resList = new ArrayList<Resultaat>();
				resultaten.put(res.getToets().getResultaatstructuur().getOnderwijsproduct(),
					resList);
			}
			resList.add(res);
		}
		for (List<Resultaat> resList : resultaten.values())
		{
			Collections.sort(resList, new Comparator<Resultaat>()
			{

				@Override
				public int compare(Resultaat o1, Resultaat o2)
				{
					return o1.getHerkansingsnummer() - o2.getHerkansingsnummer();
				}

			});
		}
		for (List<Resultaat> resList : resultaten.values())
		{
			if (resList.size() > 1)
			{
				// Onderstaande is mogelijk als er zowel een poging 0 is als een
				// alternatief resultaat.
				if (resList.get(0).getHerkansingsnummer() == 0
					&& resList.get(1).getHerkansingsnummer() == 0)
				{
					// Verwijder het resultaat dat niet gebruikt is in het samengestelde
					// resultaat.
					if (resList.get(0).isInSamengesteld() && !resList.get(1).isInSamengesteld())
					{
						resList.remove(1);
					}
					else if (!resList.get(0).isInSamengesteld()
						&& resList.get(1).isInSamengesteld())
					{
						resList.remove(0);
					}
					else if (resList.get(1).getSoort() == Resultaatsoort.Alternatief)
					{
						resList.remove(1);
					}
					else if (resList.get(0).getSoort() == Resultaatsoort.Alternatief)
					{
						resList.remove(0);
					}
					else
					{
						resList.remove(0);
					}
				}
			}
		}

		return resultaten;
	}

	@Override
	public boolean heeftResultaten(Deelnemer deelnemer, Onderwijsproduct onderwijsproduct,
			Cohort cohort)
	{
		Asserts.assertNotNull("deelnemer", deelnemer);
		Asserts.assertNotNull("onderwijsproduct", onderwijsproduct);
		Asserts.assertNotNull("cohort", cohort);
		Criteria criteria = createCriteria(Resultaat.class);
		criteria.createAlias("toets", "toets");
		criteria.createAlias("toets.resultaatstructuur", "resultaatstructuur");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemer", deelnemer);
		builder.addEquals("resultaatstructuur.cohort", cohort);
		builder.addEquals("resultaatstructuur.onderwijsproduct", onderwijsproduct);
		builder.addEquals("geldend", Boolean.TRUE);
		criteria.add(Restrictions.isNotNull("cijfer"));
		criteria.setProjection(Projections.rowCount());

		Long count = (Long) uncachedUnique(criteria);
		return count != null && count.intValue() > 0;
	}
}
