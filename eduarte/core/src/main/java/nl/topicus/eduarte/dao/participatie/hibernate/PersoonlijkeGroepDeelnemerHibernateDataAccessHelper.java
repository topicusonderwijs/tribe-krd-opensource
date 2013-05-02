package nl.topicus.eduarte.dao.participatie.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.participatie.helpers.PersoonlijkeGroepDeelnemerDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.PersoonlijkeGroepDeelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.PersoonlijkeGroepDeelnemerZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;

public class PersoonlijkeGroepDeelnemerHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<PersoonlijkeGroepDeelnemer, PersoonlijkeGroepDeelnemerZoekFilter>
		implements PersoonlijkeGroepDeelnemerDataAccessHelper
{
	public PersoonlijkeGroepDeelnemerHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(PersoonlijkeGroepDeelnemerZoekFilter filter)
	{
		Asserts.assertNotNull("persoonlijkeGroep", filter.getPersoonlijkeGroep());
		Criteria criteria = createCriteria(PersoonlijkeGroepDeelnemer.class);
		criteria.createAlias("deelnemer", "deelnemer", JoinFragment.LEFT_OUTER_JOIN);
		criteria.createAlias("deelnemer.persoon", "persoon", JoinFragment.LEFT_OUTER_JOIN);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addLessOrEquals("beginDatum", filter.getPeildatum());
		builder.addNullOrGreaterOrEquals("eindDatum", filter.getPeildatum());
		builder.addEquals("groep", filter.getPersoonlijkeGroep());
		if (filter.isAlleenDeelnemers())
		{
			criteria.add(Restrictions.isNotNull("deelnemer"));
		}

		return criteria;
	}
}
