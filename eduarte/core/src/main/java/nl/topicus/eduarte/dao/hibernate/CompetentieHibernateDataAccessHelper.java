package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.CompetentieDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Competentie;
import nl.topicus.eduarte.zoekfilters.CompetentieZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

public class CompetentieHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Competentie, CompetentieZoekFilter> implements
		CompetentieDataAccessHelper
{
	public CompetentieHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<Competentie> getCompetenties()
	{
		Criteria criteria = createCriteria(Competentie.class);
		criteria.addOrder(Order.asc("code"));
		return cachedList(criteria);
	}

	@Override
	protected Criteria createCriteria(CompetentieZoekFilter filter)
	{
		return createCriteria(Competentie.class);
	}
}
