package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.ModuleAfnameDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.ModuleAfname;
import nl.topicus.eduarte.zoekfilters.ModuleAfnameZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class ModuleAfnameHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<ModuleAfname, ModuleAfnameZoekFilter> implements
		ModuleAfnameDataAccessHelper
{
	public ModuleAfnameHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ModuleAfnameZoekFilter filter)
	{
		Criteria criteria = createCriteria(ModuleAfname.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("organisatie", "organisatie");
		builder.addEquals("organisatie", filter.getFilterOrganisatie());
		builder.addILikeCheckWildcard("moduleName", filter.getModuleName(), MatchMode.ANYWHERE);
		return criteria;
	}
}
