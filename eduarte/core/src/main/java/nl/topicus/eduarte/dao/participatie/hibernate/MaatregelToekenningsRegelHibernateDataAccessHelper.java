package nl.topicus.eduarte.dao.participatie.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.participatie.helpers.MaatregelToekenningsRegelDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.MaatregelToekenningsRegel;
import nl.topicus.eduarte.participatie.zoekfilters.MaatregelToekenningsRegelZoekFilter;

import org.hibernate.Criteria;

public class MaatregelToekenningsRegelHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<MaatregelToekenningsRegel, MaatregelToekenningsRegelZoekFilter>
		implements MaatregelToekenningsRegelDataAccessHelper
{
	public MaatregelToekenningsRegelHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(MaatregelToekenningsRegelZoekFilter filter)
	{
		Criteria criteria = createCriteria(MaatregelToekenningsRegel.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("maatregel", filter.getMaatregel());
		builder.addEquals("absentieReden", filter.getAbsentieReden());
		builder.addEquals("periode", filter.getPeriodeIndeling());
		builder.addNullOrEquals("organisatieEenheid", filter.getOrganisatieEenheid());

		return criteria;
	}
}
