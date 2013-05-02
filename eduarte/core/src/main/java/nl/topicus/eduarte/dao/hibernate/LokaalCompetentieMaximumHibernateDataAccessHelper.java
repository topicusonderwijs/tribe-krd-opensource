package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.LokaalCompetentieMaximumDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.LokaalCompetentieMaximum;
import nl.topicus.eduarte.zoekfilters.LokaalCompetentieMaximumZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class LokaalCompetentieMaximumHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<LokaalCompetentieMaximum, LokaalCompetentieMaximumZoekFilter>
		implements LokaalCompetentieMaximumDataAccessHelper
{
	public LokaalCompetentieMaximumHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(LokaalCompetentieMaximumZoekFilter filter)
	{
		Criteria criteria = createCriteria(LokaalCompetentieMaximum.class);

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("opleiding", filter.getOpleiding());
		builder.addEquals("cohort", filter.getCohort());
		builder.addLessOrEquals("datum", filter.getBeginDatum());
		builder.addGreaterOrEquals("datum", filter.getEindDatum());

		return criteria;
	}

	@Override
	public LokaalCompetentieMaximum getLokaalCompetentieMaximum(Opleiding opleiding,
			CompetentieMatrix matrix, Cohort cohort)
	{
		Criteria criteria = createCriteria(LokaalCompetentieMaximum.class);

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("opleiding", opleiding);
		builder.addEquals("cohort", cohort);
		builder.addEquals("matrix", matrix);
		return cachedUnique(criteria);
	}

	@Override
	public LokaalCompetentieMaximum getLokaalCompetentieMaximum(Opleiding opleiding, Cohort cohort)
	{
		Criteria criteria = createCriteria(LokaalCompetentieMaximum.class);

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("opleiding", opleiding);
		builder.addEquals("cohort", cohort);
		return cachedUnique(criteria);
	}
}
