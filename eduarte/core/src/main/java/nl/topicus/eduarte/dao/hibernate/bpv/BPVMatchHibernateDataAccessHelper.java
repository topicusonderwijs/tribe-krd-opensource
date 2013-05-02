package nl.topicus.eduarte.dao.hibernate.bpv;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.bpv.BPVMatchDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVKandidaat;
import nl.topicus.eduarte.entities.bpv.BPVMatch;
import nl.topicus.eduarte.zoekfilters.bpv.BPVMatchZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;

public class BPVMatchHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<BPVMatch, BPVMatchZoekFilter> implements
		BPVMatchDataAccessHelper
{
	public BPVMatchHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BPVMatchZoekFilter filter)
	{
		Criteria criteria = createCriteria(BPVMatch.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("bpvColoPlaats", "bpvColoPlaats", CriteriaSpecification.LEFT_JOIN);
		builder.addEquals("bpvKandidaat", filter.getBpvKandidaat());
		builder.addEquals("codeLeerbedrijf", filter.getCodeLeerbedrijf());
		return criteria;
	}

	@Override
	public int getHoogsteVolgnummer(BPVKandidaat kandidaat)
	{
		Asserts.assertNotNull("bpvKandidaat", kandidaat);
		Criteria ret = createCriteria(BPVMatch.class);
		CriteriaBuilder builder = new CriteriaBuilder(ret);
		builder.addEquals("bpvKandidaat", kandidaat);
		ret.setProjection(Projections.max("keuzeVolgnummer"));
		Integer max = cachedUnique(ret);
		return max == null ? 0 : max;
	}
}
