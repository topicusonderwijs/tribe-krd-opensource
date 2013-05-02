package nl.topicus.eduarte.dao.hibernate.bpv;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.eduarte.dao.helpers.bpv.BPVCriteriaDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVCriteria;
import nl.topicus.eduarte.zoekfilters.bpv.BPVCriteriaZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class BPVCriteriaHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<BPVCriteria, BPVCriteriaZoekFilter> implements
		BPVCriteriaDataAccessHelper
{
	public BPVCriteriaHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BPVCriteriaZoekFilter filter)
	{
		Criteria criteria = createCriteria(BPVCriteria.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("actief", filter.isActief());
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("status", filter.getStatus());
		builder.addEquals("toegestaanKoppelenMetExterneOrganisatie", filter
			.isToegestaanKoppelenMetExterneOrganisatie());
		builder.addEquals("toegestaanKoppelenMetStageKandidaat", filter
			.isToegestaanKoppelenMetStageKandidaat());
		builder.addEquals("toegestaanKoppelenMetStagePlaats", filter
			.isToegestaanKoppelenMetStagePlaats());
		builder.addEquals("toegestaanKoppelenMetOnderwijsproduct", filter
			.isToegestaanKoppelenMetOnderwijsproduct());
		builder.addEquals("toegestaanKoppelenMetStageProfiel", filter
			.isToegestaanKoppelenMetStageProfiel());
		return criteria;
	}

}