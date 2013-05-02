package nl.topicus.eduarte.krd.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.krd.dao.helpers.BronCfiTerugmeldingDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiTerugmelding;
import nl.topicus.eduarte.krd.zoekfilters.BronCfiTerugmeldingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class BronCfiTerugmeldingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<BronCfiTerugmelding, BronCfiTerugmeldingZoekFilter>
		implements BronCfiTerugmeldingDataAccessHelper
{
	public BronCfiTerugmeldingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BronCfiTerugmeldingZoekFilter filter)
	{
		Criteria criteria = createCriteria(BronCfiTerugmelding.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("ingelezenDoor", "ingelezenDoor");
		builder.createAlias("ingelezenDoor.persoon", "persoon");
		builder.addILikeCheckWildcard("bestandsnaam", filter.getBestandsnaam(), MatchMode.ANYWHERE);
		builder.addEquals("status", filter.getStatus());
		builder.addEquals("peildatum", filter.getPeildatumInBestand());
		builder.addEquals("ingelezenDoor", filter.getIngelezenDoor());

		return criteria;
	}
}
