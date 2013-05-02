package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.OrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.ExterneAgendaKoppelingDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.ExterneAgendaKoppeling;
import nl.topicus.eduarte.participatie.zoekfilters.ExterneAgendaKoppelingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class ExterneAgendaKoppelingHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<ExterneAgendaKoppeling, ExterneAgendaKoppelingZoekFilter>
		implements ExterneAgendaKoppelingDataAccessHelper
{
	public ExterneAgendaKoppelingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ExterneAgendaKoppelingZoekFilter filter)
	{
		Criteria criteria = createCriteria(ExterneAgendaKoppeling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (filter.getOrganisatieEenheid() != null)
			builder.addIn("organisatieEenheid", filter.getGeselecteerdeOrganisatieEenheden());
		builder.addEquals("actief", filter.getActief());
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		return criteria;
	}

	@Override
	public List<ExterneAgendaKoppeling> list(List<OrganisatieEenheid> eenheden)
	{
		OrganisatieEenheidDataAccessHelper helper =
			DataAccessRegistry.getHelper(OrganisatieEenheidDataAccessHelper.class);
		List<OrganisatieEenheid> allEenheden = new ArrayList<OrganisatieEenheid>();
		for (OrganisatieEenheid curOE : eenheden)
		{
			allEenheden.addAll(helper.getAncestors(curOE));
		}

		Criteria criteria = createCriteria(ExterneAgendaKoppeling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addIn("organisatieEenheid", allEenheden);
		builder.addEquals("actief", true);
		builder.addEquals("automatisch", false);
		return cachedTypedList(criteria);
	}

	@Override
	public List<ExterneAgendaKoppeling> listAutomatischeKoppelingen()
	{
		OrganisatieEenheid rootEenheid =
			DataAccessRegistry.getHelper(OrganisatieEenheidDataAccessHelper.class)
				.getRootOrganisatieEenheid();
		Criteria criteria = createCriteria(ExterneAgendaKoppeling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("organisatieEenheid", rootEenheid);
		builder.addEquals("actief", true);
		builder.addEquals("automatisch", true);
		return cachedTypedList(criteria);
	}
}
