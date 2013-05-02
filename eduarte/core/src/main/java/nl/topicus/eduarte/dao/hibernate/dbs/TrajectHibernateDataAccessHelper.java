package nl.topicus.eduarte.dao.hibernate.dbs;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.dbs.TrajectDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.GeplandeBegeleidingsHandeling;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.entities.dbs.trajecten.TrajectSoort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.dbs.TrajectZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class TrajectHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Traject, TrajectZoekFilter> implements
		TrajectDataAccessHelper
{
	public TrajectHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(TrajectZoekFilter filter)
	{
		Criteria criteria = createCriteria(Traject.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		filter.addCriteria(builder);
		builder.addEquals("deelnemer", filter.getDeelnemer());

		builder.createAlias("trajectSoort", "trajectSoort");
		builder.addEquals("trajectSoort.kwadrant", filter.getKwadrant());

		return criteria;
	}

	@Override
	public Traject getTrajectByDeelnemer(Deelnemer deelnemer, TrajectSoort soort)
	{
		Criteria criteria = createCriteria(Traject.class);
		criteria.add(Restrictions.eq("deelnemer", deelnemer));
		criteria.add(Restrictions.eq("trajectSoort", soort));
		return cachedUnique(criteria);
	}

	@Override
	public Traject getTrajectById(Long id)
	{
		Criteria criteria = createCriteria(Traject.class);
		criteria.add(Restrictions.eq("id", id));
		return cachedUnique(criteria);
	}

	public List<GeplandeBegeleidingsHandeling> getRecenteHandelingen(Traject traject, int aantal)
	{
		Criteria criteria = createCriteria(GeplandeBegeleidingsHandeling.class);

		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("afspraak", "afspraak");
		builder.addLessOrEquals("afspraak.beginDatumTijd", TimeUtil.getInstance().currentDate());
		builder.addEquals("traject", traject);

		criteria.addOrder(Order.desc("afspraak.beginDatumTijd"));
		criteria.setMaxResults(aantal);

		return list(criteria, false);
	}
}
