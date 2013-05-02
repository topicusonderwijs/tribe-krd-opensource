package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.CriteriumDataAccessHelper;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.Opleidingsvariant;
import nl.topicus.eduarte.zoekfilters.CriteriumZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;

public class CriteriumHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Criterium, CriteriumZoekFilter> implements
		CriteriumDataAccessHelper
{

	public CriteriumHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(CriteriumZoekFilter filter)
	{
		Criteria criteria = createCriteria(Criterium.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("cohort", "cohort");
		builder.createAlias("opleiding", "opleiding", JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("verbintenisgebied", "verbintenisgebied");
		builder.addEquals("cohort", filter.getCohort());
		builder.addNullFilterExpression("organisatie", filter.getOrganisatieFilter());
		if (filter.getOpleiding() != null && filter.getVerbintenisgebied() == null)
		{
			Criterion opleidingEqual = Restrictions.eq("opleiding", filter.getOpleiding());
			if (filter.getOpleiding() instanceof Opleidingsvariant)
			{
				opleidingEqual =
					Restrictions.or(
						opleidingEqual,
						Restrictions.eq("opleiding",
							((Opleidingsvariant) filter.getOpleiding()).getParent()));
			}
			// opleiding==filteropleiding or opleiding is null and
			// verbintenisgebied==filteropleiding.verbintenisgebied.
			if (filter.getOpleiding().isNegeerLandelijkeCriteria())
			{
				criteria.add(opleidingEqual);
			}
			else
			{
				criteria.add(Restrictions.or(opleidingEqual, Restrictions.and(Restrictions
					.isNull("opleiding"), Restrictions.eq("verbintenisgebied", filter
					.getOpleiding().getVerbintenisgebied()))));
			}
		}
		else if (filter.getVerbintenisgebied() != null && filter.getOpleiding() == null)
		{
			// Haal alle landelijke productregels op.
			builder.addEquals("verbintenisgebied", filter.getVerbintenisgebied());
			builder.addIsNull("opleiding", Boolean.TRUE);
			builder.addIsNull("organisatie", Boolean.TRUE);
		}
		else
		{
			throw new IllegalArgumentException(
				"Op het filter moet een van opleiding of verbintenisgebied ingesteld zijn.");
		}

		return criteria;
	}

	@Override
	public List<Criterium> getCriteria(Opleiding opleiding)
	{
		Criteria criteria = createCriteria(Criterium.class);
		criteria.add(Restrictions.isNotNull("opleiding"));
		criteria.add(Restrictions.eq("opleiding", opleiding));
		return cachedList(criteria);
	}

	@Override
	public Criterium getCriterium(Opleiding opleiding, Cohort cohort, String naam)
	{
		Criteria criteria = createCriteria(Criterium.class);
		criteria.add(Restrictions.isNotNull("opleiding"));
		criteria.add(Restrictions.eq("opleiding", opleiding));
		criteria.add(Restrictions.eq("cohort", cohort));
		criteria.add(Restrictions.eq("naam", naam));
		return cachedUnique(criteria);
	}
}
