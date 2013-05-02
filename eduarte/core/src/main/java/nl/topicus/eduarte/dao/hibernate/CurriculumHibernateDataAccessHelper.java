package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.zoekfilters.NullFilter;
import nl.topicus.eduarte.dao.helpers.CurriculumDataAccessHelper;
import nl.topicus.eduarte.entities.curriculum.Curriculum;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.zoekfilters.CurriculumZoekFilter;

import org.hibernate.Criteria;

public class CurriculumHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Curriculum, CurriculumZoekFilter> implements
		CurriculumDataAccessHelper
{

	public CurriculumHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(CurriculumZoekFilter filter)
	{
		Criteria criteria = createCriteria(Curriculum.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addEquals("opleiding", filter.getOpleiding());
		builder.addEquals("cohort", filter.getCohort());

		if (!filter.addOrganisatieEenheidLocatieCriteria(criteria))
			return null;

		return criteria;
	}

	@Override
	public Curriculum get(OrganisatieEenheid organisatieEenheid, Locatie locatie,
			Opleiding opleiding, Cohort cohort)
	{
		Criteria criteria = createCriteria(Curriculum.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addEquals("organisatieEenheid", organisatieEenheid);
		if (locatie == null)
			builder.addNullFilterExpression("locatie", NullFilter.IsNull);
		else
			builder.addEquals("locatie", locatie);
		builder.addEquals("opleiding", opleiding);
		builder.addEquals("cohort", cohort);

		return cachedUnique(criteria);
	}
}