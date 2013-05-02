package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.FaseDataAccessHelper;
import nl.topicus.eduarte.entities.hogeronderwijs.Fase;
import nl.topicus.eduarte.entities.hogeronderwijs.Hoofdfase;
import nl.topicus.eduarte.zoekfilters.FaseZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;

public class FaseHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Fase, FaseZoekFilter> implements FaseDataAccessHelper
{
	public FaseHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(FaseZoekFilter filter)
	{
		Criteria criteria = createCriteria(Fase.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addEquals("actief", filter.getActief());
		builder.addEquals("hoofdfase", filter.getHoofdfase());
		return criteria;
	}

	@Override
	public Fase getEersteFaseVanHoofdfase(Hoofdfase hoofdfase)
	{
		Criteria criteria = createCriteria(Fase.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("hoofdfase", hoofdfase);
		builder.addEquals("actief", Boolean.TRUE);
		DetachedCriteria dc = createDetachedCriteria(Fase.class, "subfase");
		DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
		dcBuilder.addEquals("subfase.hoofdfase", hoofdfase);
		dcBuilder.addEquals("subfase.actief", Boolean.TRUE);
		dc.setProjection(Projections.property("subfase.volgendeFase"));
		criteria.add(Subqueries.propertyNotIn("id", dc));
		criteria.addOrder(Order.asc("code"));
		List<Fase> result = cachedTypedList(criteria);
		if (result.size() >= 1)
			return result.get(0);
		return null;
	}
}
