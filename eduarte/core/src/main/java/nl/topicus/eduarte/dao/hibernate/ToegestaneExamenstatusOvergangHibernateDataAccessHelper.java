package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.ToegestaneExamenstatusOvergangDataAccessHelper;
import nl.topicus.eduarte.entities.examen.ToegestaneExamenstatusOvergang;
import nl.topicus.eduarte.zoekfilters.ToegestaneExamenstatusOvergangZoekFilter;

import org.hibernate.Criteria;

public class ToegestaneExamenstatusOvergangHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<ToegestaneExamenstatusOvergang, ToegestaneExamenstatusOvergangZoekFilter>
		implements ToegestaneExamenstatusOvergangDataAccessHelper
{
	public ToegestaneExamenstatusOvergangHibernateDataAccessHelper(
			HibernateSessionProvider provider, QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ToegestaneExamenstatusOvergangZoekFilter filter)
	{
		Criteria criteria = createCriteria(ToegestaneExamenstatusOvergang.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addNullFilterExpression("organisatie", filter.getOrganisatieFilter());
		builder.addEquals("examenWorkflow", filter.getExamenWorkflow());
		builder.addEquals("actie", filter.getActie());
		return criteria;
	}
}
