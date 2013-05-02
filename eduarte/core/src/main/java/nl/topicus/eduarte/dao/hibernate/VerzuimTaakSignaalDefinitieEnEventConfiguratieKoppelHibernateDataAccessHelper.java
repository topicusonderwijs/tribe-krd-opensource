package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppelDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel;
import nl.topicus.eduarte.zoekfilters.VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppelZoekFilter;

import org.hibernate.Criteria;

public class VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppelHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel, VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppelZoekFilter>
		implements VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppelDataAccessHelper
{

	public VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppelHibernateDataAccessHelper(
			HibernateSessionProvider provider, QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(
			VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppelZoekFilter filter)
	{
		Criteria criteria =
			createCriteria(VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		if (filter.getOrganisatieEenheid() != null)
			builder.addEquals("Organisatie", filter.getOrganisatieEenheid());

		if (filter.getOrganisatieEenheid() != null)
			builder.addEquals("Organisatie", filter.getSignaalDefinitie());

		if (filter.getOrganisatieEenheid() != null)
			builder.addEquals("Organisatie", filter.getOrganisatieEenheid());

		return criteria;
	}
}
