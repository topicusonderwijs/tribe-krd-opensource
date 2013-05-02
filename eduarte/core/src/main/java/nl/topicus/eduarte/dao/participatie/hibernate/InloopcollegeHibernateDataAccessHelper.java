package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.GroepsdeelnameDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.InloopcollegeDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.AfspraakDeelnemer;
import nl.topicus.eduarte.entities.participatie.InloopCollege;
import nl.topicus.eduarte.entities.participatie.InloopCollegeGroep;
import nl.topicus.eduarte.entities.participatie.InloopCollegeOpleiding;
import nl.topicus.eduarte.participatie.zoekfilters.InloopcollegeZoekFilter;
import nl.topicus.eduarte.zoekfilters.GroepsdeelnameZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class InloopcollegeHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<InloopCollege, InloopcollegeZoekFilter> implements
		InloopcollegeDataAccessHelper
{

	public InloopcollegeHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(InloopcollegeZoekFilter filter)
	{
		Criteria criteria = createCriteria(InloopCollege.class, "inloopcollege");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addNullOrGreaterOrEquals("inschrijfEindDatum", filter.getInschrijfBeginDatum());
		builder.addLessOrEquals("inschrijfBeginDatum", filter.getInschrijfEindDatum());

		builder.addILikeCheckWildcard("omschrijving", filter.getOmschrijving(), MatchMode.START);

		if (filter.getOpleiding() != null)
		{
			DetachedCriteria dcInloopCollege = createDetachedCriteria(InloopCollegeOpleiding.class);
			dcInloopCollege.setProjection(Projections.property("inloopCollege"));
			DetachedCriteriaBuilder dcInloopCollegeBuilder =
				new DetachedCriteriaBuilder(dcInloopCollege);
			dcInloopCollegeBuilder.addEquals("opleiding", filter.getOpleiding());
			criteria.add(Restrictions.or(Restrictions.isEmpty("opleidingen"),
				Subqueries.propertyIn("id", dcInloopCollege)));
			filter.setResultCacheable(false);
		}

		// groepen van deze deelnemer ophalen en op het filter setten
		if (filter.getDeelnemer() != null && filter.getGroepen() == null)
		{
			List<Groep> groepen = new ArrayList<Groep>();
			GroepsdeelnameZoekFilter deelnameFilter =
				new GroepsdeelnameZoekFilter(filter.getDeelnemer());
			deelnameFilter.setAuthorizationContext(filter.getAuthorizationContext());
			deelnameFilter.setPeildatum(TimeUtil.getInstance().currentDate());
			for (Groepsdeelname curDeelname : DataAccessRegistry.getHelper(
				GroepsdeelnameDataAccessHelper.class).list(deelnameFilter))
			{
				groepen.add(curDeelname.getGroep());
			}
			filter.setGroepen(groepen);
		}

		if (filter.getGroepen() != null)
		{
			if (filter.getGroepen().isEmpty())
			{
				criteria.add(Restrictions.isEmpty("groepen"));
			}
			else
			{
				DetachedCriteria dcInloopCollege = createDetachedCriteria(InloopCollegeGroep.class);
				dcInloopCollege.setProjection(Projections.property("inloopCollege"));
				DetachedCriteriaBuilder dcInloopCollegeBuilder =
					new DetachedCriteriaBuilder(dcInloopCollege);
				dcInloopCollegeBuilder.addIn("groep", filter.getGroepen());
				criteria.add(Restrictions.or(Restrictions.isEmpty("groepen"),
					Subqueries.propertyIn("id", dcInloopCollege)));
				filter.setResultCacheable(false);
			}
		}

		// Als het maximum aantal deelnemers is bereikt worden de colleges niet getoond
		if (!filter.isToonVolleColleges())
		{
			// Maak detached criteria.
			DetachedCriteria dcInloopCollege = createDetachedCriteria(Afspraak.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dcInloopCollege);

			dcBuilder.createAlias("inloopCollege", "inloopCollege");
			dcInloopCollege.setProjection(Projections.property("inloopCollege"));

			String sql =
				"inloopCollege in (select af.inloopcollege from afspraak af inner join inloopcollege inloop on af.inloopcollege=inloop.id "
					+ " where af.inloopcollege = {alias}.inloopCollege and "
					+ " ((inloop.maxDeelnemers is null) or "
					+ "(inloop.maxDeelnemers > (select count(*) from afspraakdeelnemer ad where ad.afspraak = af.id ))))";

			dcInloopCollege.add(Restrictions.sqlRestriction(sql));

			criteria.add(Subqueries.propertyIn("id", dcInloopCollege));
			filter.setResultCacheable(false);
		}
		// Als de deelnemer is ingevuld ToonIngeschrevenColleges is false, dan mogen
		// alleen de colleges getoond worden waarvoor deze deelnemer zich kan inschrijven
		// en waarvoor hij of zij nog niet ingeschreven is. Als ToonIngeschrevenColleges
		// true is, mogen alleen de colleges getoond worden waarvoor deze deelnemer zich
		// heeft ingeschreven
		if (filter.getDeelnemer() != null)
		{
			if (filter.isToonIngeschrevenColleges() != null)
			{
				DetachedCriteria dcAfspraak = createDetachedCriteria(AfspraakDeelnemer.class);
				dcAfspraak.setProjection(Projections.property("afspraak"));
				DetachedCriteriaBuilder dcAfspraakBuilder = new DetachedCriteriaBuilder(dcAfspraak);
				dcAfspraakBuilder.addEquals("deelnemer", filter.getDeelnemer());
				// if (filter.isToonIngeschrevenColleges())
				// dcAfspraakBuilder.addEquals("uitnodigingStatus",
				// UitnodigingStatus.INGETEKEND);

				DetachedCriteria dcInloopCollege = createDetachedCriteria(Afspraak.class);
				dcInloopCollege.setProjection(Projections.property("inloopCollege"));
				DetachedCriteriaBuilder dcInloopCollegeBuilder =
					new DetachedCriteriaBuilder(dcInloopCollege);
				// Zonder deze addIsNull check gaat de NotIn fout, omdat deze dan een
				// vergelijking moet maken met Null, wat resulteerd in UNKNOWN en daardoor
				// blijft de lijst altijd leeg.
				dcInloopCollegeBuilder.addIsNull("inloopCollege", false);
				dcInloopCollege.add(Subqueries.propertyIn("id", dcAfspraak));
				if (filter.isToonIngeschrevenColleges())
					criteria.add(Subqueries.propertyIn("id", dcInloopCollege));
				else
					criteria.add(Subqueries.propertyNotIn("id", dcInloopCollege));
				filter.setResultCacheable(false);
			}
		}

		criteria.addOrder(Order.asc("inschrijfBeginDatum"));
		return criteria;
	}
}
