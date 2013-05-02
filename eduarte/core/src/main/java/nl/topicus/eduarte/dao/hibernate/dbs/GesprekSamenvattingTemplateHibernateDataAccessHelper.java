package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.GesprekSamenvattingTemplateDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.GesprekSamenvattingTemplate;
import nl.topicus.eduarte.zoekfilters.dbs.GesprekSamenvattingTemplateZoekFilter;

import org.hibernate.Criteria;

public class GesprekSamenvattingTemplateHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<GesprekSamenvattingTemplate, GesprekSamenvattingTemplateZoekFilter>
		implements GesprekSamenvattingTemplateDataAccessHelper
{
	public GesprekSamenvattingTemplateHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(GesprekSamenvattingTemplateZoekFilter filter)
	{
		return createCriteria(GesprekSamenvattingTemplate.class);
	}
}
