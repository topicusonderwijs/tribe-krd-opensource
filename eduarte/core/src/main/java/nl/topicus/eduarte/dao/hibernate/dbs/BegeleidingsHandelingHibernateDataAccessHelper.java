package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.entities.dbs.trajecten.BegeleidingsHandeling;
import nl.topicus.eduarte.entities.dbs.trajecten.BegeleidingsHandelingsStatussoort;
import nl.topicus.eduarte.zoekfilters.dbs.BegeleidingsHandelingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

public class BegeleidingsHandelingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<BegeleidingsHandeling, BegeleidingsHandelingZoekFilter>
		implements nl.topicus.eduarte.dao.helpers.dbs.BegeleidingsHandelingDataAccessHelper
{
	public BegeleidingsHandelingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BegeleidingsHandelingZoekFilter filter)
	{
		Criteria criteria = createCriteria(BegeleidingsHandeling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("traject", "traject");

		filter.addCriteria(builder);

		if (filter.getMijnActiesPage() != null && filter.getMijnActiesPage().booleanValue())
		{
			if (filter.getEigenaar() != null && filter.getToegekendAan() != null)
			{
				SimpleExpression zelf = Restrictions.eq("eigenaar", filter.getEigenaar());
				SimpleExpression geweigerd = Restrictions.eq("geweigerd", Boolean.TRUE);
				SimpleExpression toegekend =
					Restrictions.eq("toegekendAan", filter.getToegekendAan());
				criteria.add(Restrictions.or(Restrictions.and(zelf, geweigerd),
					Restrictions.and(toegekend, Restrictions.not(geweigerd))));

				// deadline ligt voor bepaalde datum, of is null
				criteria.add(Restrictions.or(
					Restrictions.le("deadlineStatusovergang", filter.getDatumDeadlineTot()),
					Restrictions.isNull("deadlineStatusovergang")));
				// Als handeling al uitgevoerd of geannuleerd is, wordt deze ook niet meer
				// getoond.
				criteria.add(Restrictions.not(Restrictions.in("status",
					BegeleidingsHandelingsStatussoort.getEindStatussen())));
			}
			return criteria;
		}

		builder.addEquals("traject", filter.getTraject());
		builder.addEquals("traject.deelnemer", filter.getDeelnemer());

		builder.addNotEquals("toegekendAan", filter.getToegekendAanOngelijkAan());
		builder.addEquals("eigenaar", filter.getEigenaar());
		builder.addEquals("geweigerd", filter.getGeweigerd());
		builder.addGreaterOrEquals("deadlineStatusovergang", filter.getDatumDeadlineVanaf());
		builder.addLessOrEquals("deadlineStatusovergang", filter.getDatumDeadlineTot());

		if (filter.getActief() != null)
		{
			if (filter.getActief().booleanValue())
			{
				criteria.add(Restrictions.not(Restrictions.in("status",
					BegeleidingsHandelingsStatussoort.getEindStatussen())));
			}
			else
			{
				criteria.add(Restrictions.in("status",
					BegeleidingsHandelingsStatussoort.getEindStatussen()));
			}
		}
		if (filter.getToegekendAanNotNull() != null)
		{
			if (filter.getToegekendAanNotNull().booleanValue())
				criteria.add(Restrictions.isNotNull("toegekendAan"));
			else
				criteria.add(Restrictions.isNull("toegekendAan"));
		}
		if (filter.getEigenaarOfToegekendAan() != null)
		{
			criteria.add(Restrictions.or(
				Restrictions.eq("eigenaar", filter.getEigenaarOfToegekendAan()),
				Restrictions.eq("toegekendAan", filter.getEigenaarOfToegekendAan())));
		}

		return criteria;
	}
}
