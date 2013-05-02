package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.GesprekDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.Gesprek;
import nl.topicus.eduarte.zoekfilters.dbs.GesprekZoekFilter;

import org.hibernate.Criteria;

public class GesprekHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Gesprek, GesprekZoekFilter> implements
		GesprekDataAccessHelper
{
	public GesprekHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(GesprekZoekFilter filter)
	{
		Criteria criteria = createCriteria(Gesprek.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("traject", "traject");
		builder.createAlias("afspraak", "afspraak");
		builder.createAlias("gesprekSoort", "gesprekSoort");

		filter.addCriteria(builder);
		builder.addEquals("traject.deelnemer", filter.getDeelnemer());

		return criteria;
	}
}
