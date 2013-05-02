package nl.topicus.eduarte.krd.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.krd.dao.helpers.BronCfiTerugmeldingRegelDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiTerugmeldingRegel;
import nl.topicus.eduarte.krd.zoekfilters.BronCfiTerugmeldingRegelZoekFilter;

import org.hibernate.Criteria;

public class BronCfiTerugmeldingRegelHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<BronCfiTerugmeldingRegel, BronCfiTerugmeldingRegelZoekFilter>
		implements BronCfiTerugmeldingRegelDataAccessHelper
{
	public BronCfiTerugmeldingRegelHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BronCfiTerugmeldingRegelZoekFilter filter)
	{
		Criteria criteria = createCriteria(BronCfiTerugmeldingRegel.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("cfiTerugmelding", filter.getCfiTerugmelding());
		builder.addEquals("regelType", filter.getRegelType());
		return criteria;
	}
}
