package nl.topicus.eduarte.krd.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.krd.dao.helpers.BronFotobestandDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;
import nl.topicus.eduarte.krd.zoekfilters.BronFotobestandZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class BronFotobestandHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<BronFotobestand, BronFotobestandZoekFilter> implements
		BronFotobestandDataAccessHelper
{
	public BronFotobestandHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BronFotobestandZoekFilter filter)
	{
		Criteria criteria = createCriteria(BronFotobestand.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("ingelezenDoor", "ingelezenDoor");
		builder.createAlias("ingelezenDoor.persoon", "persoon");
		builder.addILikeCheckWildcard("bestandsnaam", filter.getBestandsnaam(), MatchMode.ANYWHERE);
		builder.addEquals("status", filter.getStatus());
		builder.addEquals("verwerkingsstatus", filter.getVerwerkingsstatus());
		builder.addEquals("type", filter.getType());
		builder.addEquals("peildatum", filter.getPeildatumInFoto());
		builder.addEquals("ingelezenDoor", filter.getIngelezenDoor());

		return criteria;
	}
}
