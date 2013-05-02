package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.participatie.helpers.OlcWaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.olc.OlcLocatie;
import nl.topicus.eduarte.entities.participatie.olc.OlcWaarneming;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.OlcWaarnemingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class OlcWaarnemingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<OlcWaarneming, OlcWaarnemingZoekFilter> implements
		OlcWaarnemingDataAccessHelper
{
	public OlcWaarnemingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<OlcWaarneming> getWaarnemingen(OrganisatieEenheid organisatieEenheid,
			OlcLocatie locatie)
	{
		Asserts.assertNotNull("organisatieEenheid", organisatieEenheid);
		Asserts.assertNotNull("OlcLocatie", locatie);

		OlcWaarnemingZoekFilter filter = new OlcWaarnemingZoekFilter();
		filter.setOrganisatieEenheid(organisatieEenheid);
		filter.setOlcLocatie(locatie);
		Criteria criteria = createCriteria(filter);

		return cachedTypedList(criteria);
	}

	@Override
	public List<OlcWaarneming> getActieveWaarneming(OlcLocatie locatie, Deelnemer deelnemer)
	{
		Asserts.assertNotNull("OlcLocatie", locatie);

		OlcWaarnemingZoekFilter filter = new OlcWaarnemingZoekFilter();
		filter.setOlcLocatie(locatie);
		filter.setDeelnemer(deelnemer);
		filter.setNietVerwerkteEnVandaag(false);
		filter.setVerwerkt(false);

		Criteria criteria = createCriteria(filter);

		return cachedTypedList(criteria);
	}

	@Override
	public boolean hasOlcWaarnemingen(OlcLocatie locatie)
	{
		Asserts.assertNotNull("OlcLocatie", locatie);

		OlcWaarnemingZoekFilter filter = new OlcWaarnemingZoekFilter();
		filter.setOlcLocatie(locatie);

		Criteria criteria = createCriteria(filter);
		criteria.setProjection(Projections.rowCount());
		return (Long) cachedUnique(criteria) > 0;
	}

	@Override
	public Criteria createCriteria(OlcWaarnemingZoekFilter filter)
	{
		Criteria criteria = createCriteria(OlcWaarneming.class);

		criteria.createAlias("deelnemer", "deelnemer");
		criteria.createAlias("deelnemer.persoon", "persoon");

		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		if (filter.getBeginTijd() != null)
			builder.addEquals("beginTijd", filter.getBeginTijd());

		if (filter.getEindTijd() != null)
			builder.addEquals("eindTijd", filter.getEindTijd());

		if (filter.getOlcLocatie() != null)
			builder.addEquals("olcLocatie", filter.getOlcLocatie());

		if (filter.getDeelnemer() != null)
			builder.addEquals("deelnemer", filter.getDeelnemer());

		if (filter.getMedewerker() != null)
			builder.addEquals("medewerker", filter.getMedewerker());

		if (filter.getOrganisatieEenheid() != null)
			builder.addEquals("organisatieEenheid", filter.getOrganisatieEenheid());

		if (filter.getAfspraakType() != null)
			builder.addEquals("afspraakType", filter.getAfspraakType());

		if (filter.isNietVerwerkteEnVandaag() != null && filter.isNietVerwerkteEnVandaag()
			&& filter.getDatum() != null && filter.getVerwerkt() != null)
		{
			criteria.add(Restrictions.or(Restrictions.eq("verwerkt", filter.getVerwerkt()),
				Restrictions.eq("datum", filter.getDatum())));
		}
		else
		{
			if (filter.getVerwerkt() != null)
				builder.addEquals("verwerkt", filter.getVerwerkt());
		}

		return criteria;
	}

	@Override
	public boolean heeftOlcWaarneming(Deelnemer deelnemer, Date datum, Date tijd)
	{
		Criteria criteria = createCriteria(OlcWaarneming.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addEquals("deelnemer", deelnemer);
		builder.addEquals("datum", datum);
		builder.addLessOrEquals("beginTijd", tijd);
		builder.addNullOrGreaterOrEquals("eindTijd", tijd);
		criteria.setProjection(Projections.rowCount());

		return (Long) cachedUnique(criteria) > 0;
	}
}
