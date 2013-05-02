package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.DeelnemerTestDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.testen.DeelnemerTest;
import nl.topicus.eduarte.zoekfilters.dbs.DeelnemerTestZoekFilter;

import org.hibernate.Criteria;

public class DeelnemerTestHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<DeelnemerTest, DeelnemerTestZoekFilter> implements
		DeelnemerTestDataAccessHelper
{
	public DeelnemerTestHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(DeelnemerTestZoekFilter filter)
	{
		Criteria criteria = createCriteria(DeelnemerTest.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		filter.addCriteria(builder);
		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("testDefinitie", filter.getTestDefinitie());

		return criteria;
	}
}
