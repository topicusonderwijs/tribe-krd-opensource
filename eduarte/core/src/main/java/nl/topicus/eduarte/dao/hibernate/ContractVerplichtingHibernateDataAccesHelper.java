package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.ContractVerplichtingDataAccesHelper;
import nl.topicus.eduarte.entities.contract.ContractVerplichting;
import nl.topicus.eduarte.zoekfilters.ContractVerplichtingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class ContractVerplichtingHibernateDataAccesHelper extends
		AbstractZoekFilterDataAccessHelper<ContractVerplichting, ContractVerplichtingZoekFilter>
		implements ContractVerplichtingDataAccesHelper
{

	public ContractVerplichtingHibernateDataAccesHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ContractVerplichtingZoekFilter filter)
	{
		Criteria criteria = createCriteria(ContractVerplichting.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("contract", "contract");
		builder.addILikeCheckWildcard("contract.code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("contract.naam", filter.getNaam(), MatchMode.START);
		builder.addEquals("medewerker", filter.getMedewerker());
		builder.addEquals("omschrijving", filter.getOmschrijving());
		builder.addEquals("contract.externeOrganisatie", filter.getExterneOrganisatie());
		builder.addGreaterOrEquals("begindatum", filter.getBeginDatum());
		builder.addLessOrEquals("deadline", filter.getDeadline());
		builder.addEquals("uitgevoerd", filter.getUitgevoerd());

		return criteria;
	}

	@Override
	public ContractVerplichting get(Long id)
	{
		return get(ContractVerplichting.class, id);
	}
}
