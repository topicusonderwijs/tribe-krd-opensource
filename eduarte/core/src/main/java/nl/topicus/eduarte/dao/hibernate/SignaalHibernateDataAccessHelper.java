package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.zoekfilters.NullFilter;
import nl.topicus.eduarte.dao.helpers.SignaalDataAccessHelper;
import nl.topicus.eduarte.entities.signalering.Signaal;
import nl.topicus.eduarte.zoekfilters.SignaalZoekFilter;

import org.hibernate.Criteria;

public class SignaalHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Signaal, SignaalZoekFilter> implements
		SignaalDataAccessHelper
{

	public SignaalHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(SignaalZoekFilter filter)
	{
		Criteria criteria = createCriteria(Signaal.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		switch (filter.getStatus())
		{
			case NieuweSignalen:
				builder.addNullFilterExpression("datumGelezen", NullFilter.IsNull);
			case OpenstaandeSignalen:
				builder.addEquals("gearchiveerd", false);
			case AlleSignalen:
		}
		builder.addEquals("ontvanger", filter.getOntvanger());
		return criteria;
	}
}
