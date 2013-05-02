package nl.topicus.eduarte.dao.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.DeelnemerToetsBevriezing;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.eduarte.web.components.resultaat.VerbintenisAfhankelijkeToetsComparator;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;

public class ToetsHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Toets, ToetsZoekFilter> implements ToetsDataAccessHelper
{
	public ToetsHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ToetsZoekFilter filter)
	{
		Criteria criteria = createCriteria(Toets.class, "toets");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		addCriteria(builder, filter, "toets", false);

		filter.addQuickSearchCriteria(builder, "code", "naam");

		return criteria;
	}

	@Override
	public void addCriteria(CriteriaBuilder builder, ToetsZoekFilter filter, String alias,
			boolean resultaatQuery)
	{
		if (filter.getResultaatstructuur() != null)
		{
			builder.addEquals("resultaatstructuur", filter.getResultaatstructuur());
		}
		else if (filter.getResultaatstructuurFilter() != null)
		{
			builder.createAlias(alias + ".resultaatstructuur", "resultaatstructuur");
			DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class)
				.addCriteria(builder, filter.getResultaatstructuurFilter(), "resultaatstructuur",
					resultaatQuery);
			filter.setResultCacheable(filter.getResultaatstructuurFilter().isResultCacheable());
		}
		if (!filter.getIgnoreToetsCodeFilter() && filter.getToetsCodeFilter() != null)
		{
			builder.addIn(alias + ".code", filter.getToetsCodeFilter().getToetsCodesAsSet());
		}
		builder.addILikeCheckWildcard(alias + ".code", filter.getCode(), MatchMode.START);
		builder.addILikeFixedMatchMode(alias + ".codePath", filter.getCodePath(), MatchMode.START);
		builder.addILikeCheckWildcard(alias + ".naam", filter.getNaam(), MatchMode.START);
		builder.addEquals(alias + ".scoreschaal", filter.getScoreschaal());
		builder.addEquals("verwijsbaar", filter.getVerwijsbaar());
		builder.addIsNull("parent", filter.isEindResultaat());
		builder.addEquals("samengesteld", filter.isSamengesteldeToets());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <Y> List<Y> criteriaList(Criteria criteria, ToetsZoekFilter filter, int firstResult,
			int maxResults, CacheMode cacheMode)
	{
		List<Y> ret = super.criteriaList(criteria, filter, firstResult, maxResults, cacheMode);
		if ("boom".equals(filter.getOrderBy()))
		{
			ToetsZoekFilter toetsZoekFilter = castZoekFilter(filter, ToetsZoekFilter.class);
			// speciale sortering vanwege de boom structuur
			Collections.sort((List<Toets>) ret, new VerbintenisAfhankelijkeToetsComparator(filter
				.isAscending(), toetsZoekFilter.getResultaatstructuurFilter() == null ? null
				: toetsZoekFilter.getResultaatstructuurFilter().getContextVerbintenis()));
		}
		return ret;
	}

	@Override
	protected void addOrders(Criteria criteria, List<String> orderByList, boolean ascending)
	{
		List<String> orderByListZonderBoom = new ArrayList<String>(orderByList);
		orderByListZonderBoom.remove("boom");
		super.addOrders(criteria, orderByListZonderBoom, ascending);
	}

	@Override
	public List<Deelnemer> getDeelnemersMetResultaten(Toets toets)
	{
		Criteria ret = createCriteria(Resultaat.class);
		CriteriaBuilder builder = new CriteriaBuilder(ret);
		builder.addEquals("toets", toets);
		ret.setProjection(Projections.distinct(Projections.property("deelnemer")));
		return cachedList(ret);
	}

	@Override
	public boolean heeftResultaten(Toets toets)
	{
		return heeftResultaten(toets, null);
	}

	@Override
	public boolean heeftResultaten(Toets toets, Deelnemer deelnemer)
	{
		Criteria ret = createCriteria(Resultaat.class);
		CriteriaBuilder builder = new CriteriaBuilder(ret);
		builder.addEquals("deelnemer", deelnemer);
		List<Toets> descendants = toets.getDescendants();
		Iterator<Toets> it = descendants.iterator();
		while (it.hasNext())
			if (!it.next().isSaved())
				it.remove();
		builder.addIn("toets", descendants);
		ret.setProjection(Projections.rowCount());
		return (Long) cachedUnique(ret) > 0;
	}

	@Override
	public Toets getToets(Onderwijsproduct product, Cohort cohort, SoortToets soortToets)
	{
		Criteria criteria = createCriteria(Toets.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		if (product != null && cohort != null)
		{
			DetachedCriteria dc = createDetachedCriteria(Resultaatstructuur.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("onderwijsproduct", product);
			dcBuilder.addEquals("cohort", cohort);
			dc.setProjection(Projections.property("id"));
			criteria.add(Subqueries.propertyIn("resultaatstructuur", dc));
		}

		builder.addEquals("soort", soortToets);
		return cachedTypedUnique(criteria);
	}

	@Override
	public int getMaximumAantalPogingen(ToetsZoekFilter filter)
	{
		Criteria criteria = createCriteria(Toets.class, "toets");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		addCriteria(builder, filter, "toets", false);
		if (filter.getToetsCodeFilter() != null)
		{
			builder.addIn("code", filter.getToetsCodeFilter().getToetsCodesAsSet());
		}
		// Dit kan niet in de database, want aantalPogingen is een berekende property
		int max = 0;
		for (Toets curToets : cachedTypedList(criteria))
		{
			max = Math.max(max, curToets.getAantalPogingen());
		}
		return max;
	}

	@Override
	public List<DeelnemerToetsBevriezing> getBevriezingen(List<Toets> toetsen,
			List<Deelnemer> deelnemers)
	{
		Criteria criteria = createCriteria(DeelnemerToetsBevriezing.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addIn("toets", toetsen);
		builder.addIn("deelnemer", deelnemers);
		return cachedList(criteria);
	}

	@Override
	public DeelnemerToetsBevriezing getBevriezing(Toets toets, Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(DeelnemerToetsBevriezing.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("toets", toets);
		builder.addEquals("deelnemer", deelnemer);
		return (DeelnemerToetsBevriezing) uncachedUnique(criteria);
	}

	@Override
	public int getAantalResultaten(Toets toets)
	{
		Criteria ret = createCriteria(Resultaat.class);
		CriteriaBuilder builder = new CriteriaBuilder(ret);
		builder.addEquals("toets", toets);
		ret.setProjection(Projections.rowCount());
		return ((Long) cachedUnique(ret)).intValue();
	}

	@Override
	public List<Integer> getDeelnemersMetHogereScore(Toets toets, int herkansingsNummer, int score)
	{
		Criteria ret = createCriteria(Resultaat.class);
		CriteriaBuilder builder = new CriteriaBuilder(ret);
		builder.createAlias("deelnemer", "deelnemer");
		builder.addEquals("toets", toets);
		builder.addEquals("herkansingsnummer", herkansingsNummer);
		builder.addEquals("actueel", true);
		builder.addGreaterThan("score", score);
		ret.setProjection(Projections.distinct(Projections.property("deelnemer.deelnemernummer")));
		ret.setMaxResults(10);
		return cachedList(ret);
	}

	@Override
	public List<Toets> getToetsenMetSchaal(Schaal schaal)
	{
		Criteria criteria = createCriteria(Toets.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("schaal", schaal);
		return cachedTypedList(criteria);
	}

	@Override
	public Toets getToets(Onderwijsproduct onderwijsproduct, Cohort cohort, String toetspad)
	{
		Criteria criteria = createCriteria(Toets.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		criteria.createAlias("resultaatstructuur", "resultaatstructuur");
		builder.addEquals("resultaatstructuur.onderwijsproduct", onderwijsproduct);
		builder.addEquals("resultaatstructuur.cohort", cohort);
		builder.addEquals("codePath", toetspad);
		builder.addEquals("resultaatstructuur.type", Type.SUMMATIEF);

		return cachedTypedUnique(criteria);
	}

}
