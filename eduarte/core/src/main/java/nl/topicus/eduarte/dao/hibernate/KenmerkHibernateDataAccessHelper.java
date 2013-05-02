package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.KenmerkDataAccessHelper;
import nl.topicus.eduarte.entities.kenmerk.Kenmerk;
import nl.topicus.eduarte.zoekfilters.KenmerkZoekFilter;

import org.hibernate.Criteria;

public class KenmerkHibernateDataAccessHelper
		extends
		CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper<Kenmerk, KenmerkZoekFilter>
		implements KenmerkDataAccessHelper
{
	public KenmerkHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(KenmerkZoekFilter filter)
	{
		Criteria criteria = super.createCriteria(filter);
		criteria.createAlias("categorie", "categorie");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("categorie", filter.getCategorie());

		return criteria;
	}
}
