package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.BijzonderheidDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.Bijzonderheid;
import nl.topicus.eduarte.zoekfilters.dbs.BijzonderheidZoekFilter;

import org.hibernate.Criteria;

public class BijzonderheidHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Bijzonderheid, BijzonderheidZoekFilter> implements
		BijzonderheidDataAccessHelper
{
	public BijzonderheidHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BijzonderheidZoekFilter filter)
	{
		Criteria criteria = createCriteria(Bijzonderheid.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		filter.addCriteria(builder);
		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("categorie", filter.getCategorie());
		builder.addEquals("tonenAlsWaarschuwing", filter.getTonenAlsWaarschuwing());
		builder.addEquals("tonenOpDeelnemerkaart", filter.getTonenOpDeelnemerkaart());

		return criteria;
	}
}
