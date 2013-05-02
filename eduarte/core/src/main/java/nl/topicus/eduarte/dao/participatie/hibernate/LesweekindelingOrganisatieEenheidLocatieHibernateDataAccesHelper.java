package nl.topicus.eduarte.dao.participatie.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekindelingOrganisatieEenheidLocatieDataAccesHelper;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.LesweekIndelingOrganisatieEenheidLocatie;
import nl.topicus.eduarte.participatie.zoekfilters.LesweekindelingOrganisatieEenheidLocatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter.SelectieMode;

import org.hibernate.Criteria;

public class LesweekindelingOrganisatieEenheidLocatieHibernateDataAccesHelper
		extends
		AbstractZoekFilterDataAccessHelper<LesweekIndelingOrganisatieEenheidLocatie, LesweekindelingOrganisatieEenheidLocatieZoekFilter>
		implements LesweekindelingOrganisatieEenheidLocatieDataAccesHelper
{
	public LesweekindelingOrganisatieEenheidLocatieHibernateDataAccesHelper(
			HibernateSessionProvider provider, QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public LesweekIndelingOrganisatieEenheidLocatie getOrganisatieEenheidLocatie(
			OrganisatieEenheid organisatieEenheid, Locatie locatie)
	{
		Criteria criteria = createCriteria(LesweekIndelingOrganisatieEenheidLocatie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("organisatieEenheid", organisatieEenheid);
		if (locatie == null)
			builder.addIsNull("locatie", true);
		else
			builder.addEquals("locatie", locatie);
		return (LesweekIndelingOrganisatieEenheidLocatie) uncachedUnique(criteria);
	}

	@Override
	protected Criteria createCriteria(LesweekindelingOrganisatieEenheidLocatieZoekFilter filter)
	{
		Criteria criteria = createCriteria(LesweekIndelingOrganisatieEenheidLocatie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		filter.setOrganisatieEenheidSelectie(SelectieMode.PARENTS);

		builder.addEquals("organisatieEenheid", filter.getOrganisatieEenheid());

		if (filter.getLocatie() == null)
			builder.addIsNull("locatie", true);
		else
			builder.addEquals("locatie", filter.getLocatie());

		if (filter.getLesweekIndeling() != null)
			builder.addEquals("LesweekIndeling", filter.getLesweekIndeling());

		return criteria;
	}
}
