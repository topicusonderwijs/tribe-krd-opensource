package nl.topicus.eduarte.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.StandaardToetsCodeFilterDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsCodeFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.StandaardToetsCodeFilter;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsCodeFilter;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsCodeFilterOrganisatieEenheidLocatie;
import nl.topicus.eduarte.zoekfilters.ToetsCodeFilterZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;

public class ToetsCodeFilterHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<ToetsCodeFilter, ToetsCodeFilterZoekFilter> implements
		ToetsCodeFilterDataAccessHelper
{
	public ToetsCodeFilterHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ToetsCodeFilterZoekFilter filter)
	{
		Criteria criteria = createCriteria(ToetsCodeFilter.class, "toets");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (!filter.addOrganisatieEenheidLocatieDetachedCriteria(this, criteria,
			ToetsCodeFilterOrganisatieEenheidLocatie.class, "toetsCodeFilter"))
			return null;

		if (filter.getOrganisatieEenheidLocatieList() != null
			&& !filter.getOrganisatieEenheidLocatieList().isEmpty())
		{
			List<Criterion> ors =
				new ArrayList<Criterion>(filter.getOrganisatieEenheidLocatieList().size());
			for (IOrganisatieEenheidLocatieKoppelEntiteit< ? > koppel : filter
				.getOrganisatieEenheidLocatieList())
			{
				DetachedCriteria dc =
					createDetachedCriteria(ToetsCodeFilterOrganisatieEenheidLocatie.class);
				DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
				dcBuilder.addEquals("locatie", koppel.getLocatie());
				dcBuilder.addIn("organisatieEenheid", koppel.getOrganisatieEenheid()
					.getActieveChildren(filter.getPeildatum()));
				dc.setProjection(Projections.property("toetsCodeFilter"));
				ors.add(Subqueries.propertyIn("id", dc));
			}
			builder.addOrs(ors);
			filter.setResultCacheable(false);
		}

		if (filter.getPersoonlijk() == null)
			builder.addNullOrEquals("medewerker", filter.getMedewerker());
		else if (filter.getPersoonlijk())
			builder.addEquals("medewerker", filter.getMedewerker());
		else
			builder.addIsNull("medewerker", true);
		return criteria;
	}

	@Override
	public ToetsCodeFilter getStandaardFilter(Verbintenis verbintenis)
	{
		return verbintenis == null ? null : getStandaardFilter(verbintenis.getOpleiding(),
			verbintenis.getCohort());
	}

	@Override
	public ToetsCodeFilter getStandaardFilter(List<Deelnemer> deelnemers, Cohort cohort)
	{
		Opleiding opleiding =
			DataAccessRegistry.getHelper(OpleidingDataAccessHelper.class)
				.getEersteOpleidingGevolgdDoorAlleDeelnemers(deelnemers);
		return opleiding == null ? null : getStandaardFilter(opleiding, cohort);
	}

	@Override
	public ToetsCodeFilter getStandaardFilter(Opleiding opleiding, Cohort cohort)
	{
		StandaardToetsCodeFilter filter =
			DataAccessRegistry.getHelper(StandaardToetsCodeFilterDataAccessHelper.class)
				.getStandaardFilter(opleiding, cohort);
		return filter == null ? null : filter.getToetsCodeFilter();
	}
}
