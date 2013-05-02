package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.HulpmiddelDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.Hulpmiddel;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.ToegestaanHulpmiddel;
import nl.topicus.eduarte.zoekfilters.dbs.HulpmiddelZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;

public class HulpmiddelHibernateDataAccessHelper
		extends
		CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper<Hulpmiddel, HulpmiddelZoekFilter>
		implements HulpmiddelDataAccessHelper
{
	public HulpmiddelHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(HulpmiddelZoekFilter filter)
	{
		Criteria criteria = super.createCriteria(filter);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		if (filter.getBijzonderheidCategorie() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(ToegestaanHulpmiddel.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("bijzonderheidCategorie", filter.getBijzonderheidCategorie());
			dc.setProjection(Projections.property("hulpmiddel"));
			builder.propertyIn("id", dc);
		}

		return criteria;
	}
}
