package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.OpleidingAanbodDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.OpleidingAanbod;
import nl.topicus.eduarte.zoekfilters.OpleidingAanbodZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

public class OpleidingAanbodHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<OpleidingAanbod, OpleidingAanbodZoekFilter> implements
		OpleidingAanbodDataAccessHelper
{
	public OpleidingAanbodHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(OpleidingAanbodZoekFilter filter)
	{
		Criteria criteria = createCriteria(OpleidingAanbod.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("opleiding", "opleiding");
		builder.createAlias("opleiding.verbintenisgebied", "verbintenisgebied");
		builder.addGreaterOrEquals("opleiding.einddatumNotNull", filter.getPeildatum());
		builder.addILikeCheckWildcard("opleiding.code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("opleiding.naam", filter.getOpleidingNaam(),
			MatchMode.ANYWHERE);
		builder.addEquals("opleiding.leerweg", filter.getLeerweg());
		builder.addILikeCheckWildcard("verbintenisgebied.taxonomiecode", filter.getTaxonomiecode(),
			MatchMode.START);
		builder.addEquals("opleiding.verbintenisgebied", filter.getVerbintenisgebied());
		builder.addEquals("team", filter.getTeam());

		builder.addEquals("opleiding.parent", filter.getVariantVan());

		if (filter.getOrderByList().contains("team.naam"))
			builder.createAlias("team", "team", CriteriaSpecification.LEFT_JOIN);
		return criteria;
	}

	@Override
	public long getOpleidingCount(OpleidingAanbodZoekFilter zoekFilter)
	{
		Criteria criteria = createCriteria(zoekFilter);
		criteria.setProjection(Projections.countDistinct("opleiding"));
		return (Long) cachedUnique(criteria);
	}
}
