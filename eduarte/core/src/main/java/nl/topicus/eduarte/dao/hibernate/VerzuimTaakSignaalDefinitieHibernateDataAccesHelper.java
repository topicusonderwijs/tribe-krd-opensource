package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.VerzuimTaakSignaalDefinitieDataAccessHelper;
import nl.topicus.eduarte.entities.signalering.VerzuimTaakSignaalDefinitie;
import nl.topicus.eduarte.zoekfilters.VerzuimTaakSignaalDefinitieZoekFilter;

import org.hibernate.Criteria;

public class VerzuimTaakSignaalDefinitieHibernateDataAccesHelper
		extends
		AbstractZoekFilterDataAccessHelper<VerzuimTaakSignaalDefinitie, VerzuimTaakSignaalDefinitieZoekFilter>
		implements VerzuimTaakSignaalDefinitieDataAccessHelper

{
	public VerzuimTaakSignaalDefinitieHibernateDataAccesHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(VerzuimTaakSignaalDefinitieZoekFilter filter)
	{
		Criteria criteria = createCriteria(VerzuimTaakSignaalDefinitie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		if (filter.getOrganisatieEenheid() != null)
			builder.addEquals("Organisatie", filter.getOrganisatieEenheid());

		return criteria;
	}
}
