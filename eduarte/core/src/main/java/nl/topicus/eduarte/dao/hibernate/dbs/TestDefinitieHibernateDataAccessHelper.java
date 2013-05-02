package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.TestDefinitieDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.testen.TestDefinitie;
import nl.topicus.eduarte.zoekfilters.dbs.TestDefinitieZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class TestDefinitieHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<TestDefinitie, TestDefinitieZoekFilter> implements
		TestDefinitieDataAccessHelper
{
	public TestDefinitieHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(TestDefinitieZoekFilter filter)
	{
		Criteria criteria = createCriteria(TestDefinitie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("categorie", filter.getTestCategorie());
		builder.addEquals("actief", filter.isActief());

		return criteria;
	}
}
