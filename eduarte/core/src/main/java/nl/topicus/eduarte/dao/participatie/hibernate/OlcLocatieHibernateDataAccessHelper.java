package nl.topicus.eduarte.dao.participatie.hibernate;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.hibernate.CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.OlcLocatieDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.olc.OlcLocatie;
import nl.topicus.eduarte.participatie.zoekfilters.OlcLocatieZoekFilter;

import org.hibernate.Criteria;

public class OlcLocatieHibernateDataAccessHelper
		extends
		CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper<OlcLocatie, OlcLocatieZoekFilter>
		implements OlcLocatieDataAccessHelper
{
	public OlcLocatieHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(OlcLocatieZoekFilter filter)
	{
		Criteria criteria = createCriteria(OlcLocatie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		if (!filter.addOrganisatieEenheidLocatieCriteria(criteria))
			return null;

		builder.addEquals("code", filter.getCode());
		builder.addEquals("naam", filter.getNaam());
		builder.addEquals("actief", filter.getActief());

		return criteria;
	}

	@Override
	public OlcLocatie getByOlcCode(String code)
	{
		Criteria criteria = createCriteria(OlcLocatie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("code", code);
		return cachedTypedUnique(criteria);
	}
}
