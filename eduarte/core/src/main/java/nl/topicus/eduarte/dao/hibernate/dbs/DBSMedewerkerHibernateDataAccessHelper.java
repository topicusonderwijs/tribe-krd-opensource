package nl.topicus.eduarte.dao.hibernate.dbs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.DBSMedewerkerDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.entities.dbs.trajecten.TrajectUitvoerder;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.zoekfilters.IVerantwoordelijkeUitvoerendeZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class DBSMedewerkerHibernateDataAccessHelper extends HibernateDataAccessHelper<Medewerker>
		implements DBSMedewerkerDataAccessHelper
{
	public DBSMedewerkerHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<Medewerker> getUitvoerendenVan(Deelnemer deelnemer, Date peilDatum)
	{
		Criteria criteria = createCriteria(TrajectUitvoerder.class, "uitvoerder");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("traject", "traject");
		builder.addEquals("traject.deelnemer", deelnemer);
		builder.addLessOrEquals("traject.begindatum", peilDatum);
		builder.addGreaterOrEquals("traject.einddatumNotNull", peilDatum);
		criteria.setProjection(Projections.distinct(Projections.property("medewerker")));
		return cachedTypedList(criteria);
	}

	@Override
	public List<Medewerker> getVerantwoordelijkenVan(Deelnemer deelnemer, Date peilDatum)
	{
		Criteria criteria = createCriteria(Traject.class, "traject");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemer", deelnemer);
		builder.addLessOrEquals("begindatum", peilDatum);
		builder.addGreaterOrEquals("einddatumNotNull", peilDatum);
		criteria.setProjection(Projections.distinct(Projections.property("verantwoordelijke")));
		return cachedTypedList(criteria);
	}

	@Override
	public boolean isUitvoerendeVan(Medewerker medewerker, Deelnemer deelnemer, Date peilDatum)
	{
		Criteria criteria = createCriteria(TrajectUitvoerder.class, "uitvoerder");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("traject", "traject");
		builder.addEquals("traject.deelnemer", deelnemer);
		builder.addLessOrEquals("traject.begindatum", peilDatum);
		builder.addGreaterOrEquals("traject.einddatumNotNull", peilDatum);
		builder.addEquals("medewerker", medewerker);
		criteria.setProjection(Projections.rowCount());
		return ((Long) uncachedUnique(criteria)) > 0;
	}

	@Override
	public boolean isVerantwoordelijkeVan(Medewerker medewerker, Deelnemer deelnemer, Date peilDatum)
	{
		Criteria criteria = createCriteria(Traject.class, "traject");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemer", deelnemer);
		builder.addLessOrEquals("begindatum", peilDatum);
		builder.addGreaterOrEquals("einddatumNotNull", peilDatum);
		builder.addEquals("verantwoordelijke", medewerker);
		criteria.setProjection(Projections.rowCount());
		return ((Long) uncachedUnique(criteria)) > 0;
	}

	@Override
	public List<Criterion> createVerantwoordelijkeUitvoerende(
			IVerantwoordelijkeUitvoerendeZoekFilter< ? > filter, String deelnemerAlias)
	{
		List<Criterion> ret = new ArrayList<Criterion>();
		if (filter.getVerantwoordelijke() != null)
		{
			DetachedCriteria dc =
				createVerantwoordelijkeCriteria(filter.getVerantwoordelijke(), filter
					.getPeildatum(), filter.getPeilEindDatum());
			ret.add(Subqueries.propertyIn(deelnemerAlias + ".id", dc));
			filter.setResultCacheable(false);
		}
		if (filter.getUitvoerende() != null)
		{
			DetachedCriteria dc =
				createUitvoerendeCriteria(filter.getUitvoerende(), filter.getPeildatum(), filter
					.getPeilEindDatum());
			ret.add(Subqueries.propertyIn(deelnemerAlias + ".id", dc));
			filter.setResultCacheable(false);
		}
		if (filter.getVerantwoordelijkeOfUitvoerende() != null)
		{
			DetachedCriteria dcVerantwoordelijke =
				createVerantwoordelijkeCriteria(filter.getVerantwoordelijkeOfUitvoerende(), filter
					.getPeildatum(), filter.getPeilEindDatum());
			DetachedCriteria dcUitvoerende =
				createUitvoerendeCriteria(filter.getVerantwoordelijkeOfUitvoerende(), filter
					.getPeildatum(), filter.getPeilEindDatum());
			ret
				.add(Restrictions.or(Subqueries.propertyIn(deelnemerAlias + ".id",
					dcVerantwoordelijke), Subqueries.propertyIn(deelnemerAlias + ".id",
					dcUitvoerende)));
			filter.setResultCacheable(false);
		}
		return ret;
	}

	private DetachedCriteria createVerantwoordelijkeCriteria(Medewerker verantwoordelijke,
			Date peildatum, Date peilEinddatum)
	{
		DetachedCriteria ret = createDetachedCriteria(Traject.class, "traject");
		DetachedCriteriaBuilder verantwoordelijkeBuilder = new DetachedCriteriaBuilder(ret);
		verantwoordelijkeBuilder.addLessOrEquals("begindatum", peilEinddatum);
		verantwoordelijkeBuilder.addGreaterOrEquals("einddatumNotNull", peildatum);
		verantwoordelijkeBuilder.addEquals("verantwoordelijke", verantwoordelijke);
		ret.setProjection(Projections.property("deelnemer"));
		return ret;
	}

	private DetachedCriteria createUitvoerendeCriteria(Medewerker uitvoerende, Date peildatum,
			Date peilEinddatum)
	{
		DetachedCriteria ret = createDetachedCriteria(TrajectUitvoerder.class, "uitvoerder");
		DetachedCriteriaBuilder uitvoerendeBuilder = new DetachedCriteriaBuilder(ret);
		uitvoerendeBuilder.createAlias("traject", "traject");
		uitvoerendeBuilder.addLessOrEquals("traject.begindatum", peilEinddatum);
		uitvoerendeBuilder.addGreaterOrEquals("traject.einddatumNotNull", peildatum);
		uitvoerendeBuilder.addEquals("medewerker", uitvoerende);
		ret.setProjection(Projections.property("traject.deelnemer"));
		return ret;
	}
}
