package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.TaalkeuzeDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.ModerneTaal;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.TaalType;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Taalkeuze;
import nl.topicus.eduarte.zoekfilters.TaalkeuzeZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class TaalkeuzeHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Taalkeuze, TaalkeuzeZoekFilter> implements
		TaalkeuzeDataAccessHelper
{
	public TaalkeuzeHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(TaalkeuzeZoekFilter filter)
	{
		Criteria criteria = createCriteria(Taalkeuze.class);

		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addEquals("verbintenis", filter.getVerbintenis());
		builder.addEquals("taal", filter.getTaal());

		if (filter.getVoorgedefinieerd() != null || filter.getTaalTitel() != null)
		{
			criteria.createAlias("taal", "taal");
			builder.addEquals("taal.voorgedefinieerd", filter.getVoorgedefinieerd());
			builder.addEquals("taal.omschrijving", filter.getTaalTitel());
		}

		return criteria;
	}

	@Override
	public boolean isTaalInGebruik(ModerneTaal taal)
	{
		Criteria criteria = createCriteria(Taalkeuze.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("taal", taal);
		criteria.setProjection(Projections.rowCount());
		return (Long) cachedUnique(criteria) > 0;
	}

	@Override
	public List<Taalkeuze> getTaalkeuzes(Verbintenis verbintenis)
	{
		Criteria criteria = createCriteria(Taalkeuze.class);
		criteria.add(Restrictions.eq("verbintenis", verbintenis));
		return cachedList(criteria);
	}

	@Override
	public List<ModerneTaal> getTaalkeuzes(Verbintenis verbintenis, TaalType taaltype)
	{
		Criteria criteria = createCriteria(Taalkeuze.class);
		criteria.add(Restrictions.eq("verbintenis", verbintenis));
		criteria.createAlias("taal", "taal");
		criteria.createAlias("taal.gekoppeldeTaalTypes", "gekoppeldeTaalTypes");
		criteria.add(Restrictions.eq("gekoppeldeTaalTypes.type", taaltype));
		criteria.setProjection(Projections.property("taal"));
		return cachedList(criteria);
	}
}
