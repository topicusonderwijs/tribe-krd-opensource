package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.participatie.helpers.BasisroosterDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.Basisrooster;
import nl.topicus.eduarte.entities.participatie.ExternSysteem;
import nl.topicus.eduarte.participatie.zoekfilters.BasisroosterZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;

public class BasisroosterHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Basisrooster, BasisroosterZoekFilter> implements
		BasisroosterDataAccessHelper
{
	public BasisroosterHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public Basisrooster getBasisrooster(String naam, OrganisatieEenheid organisatieEenheid,
			ExternSysteem externSysteem)
	{
		Asserts.assertNotNull("naam", naam);

		Criteria criteria = createCriteria(Basisrooster.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("naam", naam);
		builder.addEquals("organisatieEenheid", organisatieEenheid);
		builder.addEquals("externSysteem", externSysteem);

		return cachedTypedUnique(criteria);
	}

	@Override
	public List<Basisrooster> list()
	{
		Criteria criteria = createCriteria(Basisrooster.class);
		return cachedTypedList(criteria);
	}

	@Override
	protected Criteria createCriteria(BasisroosterZoekFilter filter)
	{
		Criteria criteria = createCriteria(Basisrooster.class);
		criteria.createAlias("organisatieEenheid", "organisatieEenheid",
			JoinFragment.LEFT_OUTER_JOIN);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (filter.getOrganisatieEenheid() != null)
		{
			criteria.add(Restrictions.or(Restrictions.isNull("organisatieEenheid"), Restrictions
				.in("organisatieEenheid",
					filter.getOrganisatieEenheid().getActieveChildren(filter.getPeildatum()))));
		}
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		return criteria;
	}
}
