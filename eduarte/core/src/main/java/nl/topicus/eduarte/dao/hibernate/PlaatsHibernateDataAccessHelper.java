package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.PlaatsDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.entities.landelijk.Plaats;
import nl.topicus.eduarte.entities.landelijk.Provincie;
import nl.topicus.eduarte.zoekfilters.PlaatsZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class PlaatsHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Plaats, PlaatsZoekFilter> implements
		PlaatsDataAccessHelper
{
	public PlaatsHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public Plaats get(String naam)
	{
		Asserts.assertNotEmpty("naam", naam);
		Criteria criteria = createCriteria(Plaats.class);
		criteria.add(Restrictions.eq("naam", naam));

		return cachedTypedUnique(criteria);
	}

	@Override
	public Plaats getPlaatsVanCaseInsensitiveNaam(String naam)
	{
		if (naam.equals(""))
			return null;

		Criteria criteria = createCriteria(Plaats.class);
		criteria.add(Restrictions.ilike("naam", naam, MatchMode.EXACT));

		return cachedTypedUnique(criteria);
	}

	@Override
	public Plaats get(String naam, Provincie provincie, Gemeente gemeente)
	{
		Asserts.assertNotEmpty("naam", naam);
		Criteria criteria = createCriteria(Plaats.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("naam", naam);
		builder.addEquals("provincie", provincie);
		builder.addEquals("gemeente", gemeente);

		return cachedTypedUnique(criteria);
	}

	@Override
	protected Criteria createCriteria(PlaatsZoekFilter filter)
	{
		Criteria criteria = createCriteria(Plaats.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);

		filter.addQuickSearchCriteria(builder, "naam");

		builder.addEquals("gemeente", filter.getGemeente());
		builder.addEquals("provincie", filter.getProvincie());
		builder.addEquals("uniek", filter.getUniek());

		criteria.createAlias("gemeente", "gemeente");
		if (filter.getOrderByList().contains("provincie.naam"))
			criteria.createAlias("provincie", "provincie", CriteriaSpecification.LEFT_JOIN);

		return criteria;
	}

	public void updateUniekePlaatsen()
	{
		createSQLQuery(
			"update plaats set uniek=0 where (select count(*) from plaats p where p.naam=plaats.naam)>1")
			.executeUpdate();
		createSQLQuery(
			"update plaats set uniekmetprovincie=0 where (select count(*) from plaats p where p.naam=plaats.naam and p.provincie=plaats.provincie)>1")
			.executeUpdate();
	}
}
