package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.RelatieSoortDataAccesHelper;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.personen.RelatieSoort;
import nl.topicus.eduarte.zoekfilters.RelatieSoortZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class RelatieSoortHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<RelatieSoort, RelatieSoortZoekFilter> implements
		RelatieSoortDataAccesHelper
{
	public RelatieSoortHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(RelatieSoortZoekFilter filter)
	{
		Criteria criteria = createCriteria(RelatieSoort.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addEquals("actief", filter.getActief());
		builder.addEquals("organisatieOpname", filter.getOrganisatieOpname());
		builder.addEquals("persoonOpname", filter.getPersoonOpname());
		return criteria;
	}

	@Override
	public RelatieSoort get(Instelling organisatie, String code)
	{
		Asserts.assertNotEmpty("code", code);
		Asserts.assertNotEmpty("organisatie", organisatie);

		Criteria criteria = createCriteria(RelatieSoort.class);
		criteria.add(Restrictions.eq("code", code));
		criteria.add(Restrictions.eq("organisatie", organisatie));

		return cachedTypedUnique(criteria);
	}

	@Override
	public List<RelatieSoort> list(boolean persoonOpname, boolean organisatieOpname)
	{
		RelatieSoortZoekFilter filter = new RelatieSoortZoekFilter();
		filter.setActief(true);
		filter.setPersoonOpname(persoonOpname);
		filter.setOrganisatieOpname(organisatieOpname);
		return super.list(filter);
	}
}
