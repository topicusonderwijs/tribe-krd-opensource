package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.participatie.helpers.MaatregelDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.Maatregel;
import nl.topicus.eduarte.participatie.zoekfilters.MaatregelZoekFilter;

import org.hibernate.Criteria;

public class MaatregelHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Maatregel, MaatregelZoekFilter> implements
		MaatregelDataAccessHelper
{
	public MaatregelHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(MaatregelZoekFilter filter)
	{
		Criteria criteria = createCriteria(Maatregel.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("actief", filter.getActief());
		if (filter.getOrganisatieEenheid() != null)
			builder.addNullOrIn("organisatieEenheid", filter.getGeselecteerdeOrganisatieEenheden());

		return criteria;
	}

	@Override
	public boolean bestaatEnActief(Maatregel maatregel)
	{
		Asserts.assertNotNull("maatregel.instelling", maatregel.getOrganisatie());
		Criteria criteria = createCriteria(Maatregel.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("organisatie", maatregel.getOrganisatie());
		builder.addEquals("actief", Boolean.TRUE);
		builder.addEquals("omschrijving", maatregel.getOmschrijving());
		OrganisatieEenheid organisatieEenheid = maatregel.getOrganisatieEenheid();
		if (organisatieEenheid != null)
		{
			List<OrganisatieEenheid> organisatieEenheidList = organisatieEenheid.getParents();
			organisatieEenheidList.addAll(organisatieEenheid.getActieveChildren(EduArteContext
				.get().getPeildatumOfVandaag()));
			builder.addNullOrIn("organisatieEenheid", organisatieEenheidList);
		}
		List<Maatregel> maatregelList = cachedList(criteria);
		for (Maatregel maatr : maatregelList)
		{
			if (!maatr.equals(maatregel))
				return true;
		}
		return false;
	}
}
