package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.CurriculumOnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.curriculum.Curriculum;
import nl.topicus.eduarte.entities.curriculum.CurriculumOnderwijsproduct;
import nl.topicus.eduarte.zoekfilters.CurriculumOnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.CurriculumZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;

public class CurriculumOnderwijsproductHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<CurriculumOnderwijsproduct, CurriculumOnderwijsproductZoekFilter>
		implements CurriculumOnderwijsproductDataAccessHelper
{

	public CurriculumOnderwijsproductHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(CurriculumOnderwijsproductZoekFilter filter)
	{
		Criteria criteria = createCriteria(CurriculumOnderwijsproduct.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addEquals("curriculum", filter.getCurriculum());
		builder.addEquals("onderwijsproduct", filter.getOnderwijsproduct());

		if (filter.getCurriculumZoekFilter() != null)
		{
			CurriculumZoekFilter curriculumFilter = filter.getCurriculumZoekFilter();
			DetachedCriteria dc = createDetachedCriteria(Curriculum.class);
			DetachedCriteriaBuilder dBuilder = new DetachedCriteriaBuilder(dc);

			if (!curriculumFilter.addOrganisatieEenheidLocatieCriteria(dc))
				return null;

			dBuilder.addEquals("opleiding", curriculumFilter.getOpleiding());
			dBuilder.addEquals("cohort", curriculumFilter.getCohort());

			dc.setProjection(Projections.property("id"));
			criteria.add(Subqueries.propertyIn("curriculum", dc));
		}

		return criteria;
	}
}