package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.participatie.helpers.PeriodeIndelingDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.PeriodeIndeling;
import nl.topicus.eduarte.participatie.zoekfilters.PeriodeIndelingZoekFilter;

import org.hibernate.Criteria;

public class PeriodeIndelingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<PeriodeIndeling, PeriodeIndelingZoekFilter> implements
		PeriodeIndelingDataAccessHelper
{
	public PeriodeIndelingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(PeriodeIndelingZoekFilter filter)
	{
		Criteria criteria = createCriteria(PeriodeIndeling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (filter.getOrganisatieEenheid() != null)
			builder.addNullOrIn("organisatieEenheid", filter.getGeselecteerdeOrganisatieEenheden());

		return criteria;
	}

	@Override
	public boolean bestaat(PeriodeIndeling periodeIndeling)
	{
		Asserts.assertNotNull("absentieReden.instelling", periodeIndeling.getOrganisatie());
		Criteria criteria = createCriteria(PeriodeIndeling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("organisatie", periodeIndeling.getOrganisatie());
		builder.addEquals("omschrijving", periodeIndeling.getOmschrijving());

		OrganisatieEenheid organisatieEenheid = periodeIndeling.getOrganisatieEenheid();
		if (organisatieEenheid != null)
		{
			List<OrganisatieEenheid> organisatieEenheidList = organisatieEenheid.getParents();
			organisatieEenheidList.addAll(organisatieEenheid.getActieveChildren(EduArteContext
				.get().getPeildatumOfVandaag()));
			builder.addNullOrIn("organisatieEenheid", organisatieEenheidList);
		}
		List<PeriodeIndeling> periodeIndelingList = cachedList(criteria);
		for (PeriodeIndeling periodeInd : periodeIndelingList)
		{
			if (!periodeInd.equals(periodeIndeling))
				return true;
		}
		return false;
	}
}
