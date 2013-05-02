package nl.topicus.eduarte.dao.participatie.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.participatie.helpers.DeelnemerPersoonlijkeGroepDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.DeelnemerPersoonlijkeGroep;
import nl.topicus.eduarte.participatie.zoekfilters.DeelnemerPersoonlijkeGroepZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.sql.JoinFragment;

public class DeelnemerPersoonlijkeGroepHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<DeelnemerPersoonlijkeGroep, DeelnemerPersoonlijkeGroepZoekFilter>
		implements DeelnemerPersoonlijkeGroepDataAccessHelper
{
	public DeelnemerPersoonlijkeGroepHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(DeelnemerPersoonlijkeGroepZoekFilter filter)
	{
		Asserts.assertNotNull("persoonlijkeGroep", filter.getPersoonlijkeGroep());

		Criteria criteria = createCriteria(DeelnemerPersoonlijkeGroep.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("deelnemer", "deelnemer", JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("deelnemer.persoon", "persoon", JoinFragment.LEFT_OUTER_JOIN);
		builder.addLessOrEquals("beginDatum", filter.getPeildatum());
		builder.addNullOrGreaterOrEquals("eindDatum", filter.getPeildatum());
		builder.addEquals("groep", filter.getPersoonlijkeGroep());
		return criteria;
	}
}
