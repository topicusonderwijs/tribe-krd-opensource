package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.GesprekSamenvattingZinDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.GesprekSamenvattingZin;
import nl.topicus.eduarte.zoekfilters.dbs.GesprekSamenvattingZinZoekFilter;

import org.hibernate.Criteria;

public class GesprekSamenvattingZinHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<GesprekSamenvattingZin, GesprekSamenvattingZinZoekFilter>
		implements GesprekSamenvattingZinDataAccessHelper
{
	public GesprekSamenvattingZinHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(GesprekSamenvattingZinZoekFilter filter)
	{
		return createCriteria(GesprekSamenvattingZin.class);
	}
}
