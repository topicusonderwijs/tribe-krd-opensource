package nl.topicus.eduarte.dao.hibernate.bpv;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.bpv.BPVKandidaatDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVKandidaat;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Aanmelding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;
import nl.topicus.eduarte.zoekfilters.bpv.BPVKandidaatZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class BPVKandidaatHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<BPVKandidaat, BPVKandidaatZoekFilter> implements
		BPVKandidaatDataAccessHelper
{
	public BPVKandidaatHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BPVKandidaatZoekFilter filter)
	{
		Criteria criteria = createCriteria(BPVKandidaat.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("matchingType", filter.getMatchingType());
		builder.addEquals("verbintenis", filter.getVerbintenis());
		builder.addEquals("bpvInschrijving", filter.getBpvInschrijving());
		builder.addIn("matchingStatus", filter.getMatchingStatussen());
		builder.addEquals("matchingStatus", filter.getMatchingStatus());

		if (filter.getOnderwijsproduct() != null)
		{
			builder.createAlias("onderwijsproducten", "onderwijsproducten");
			builder.addEquals("onderwijsproducten.onderwijsproduct", filter.getOnderwijsproduct());
		}

		if (!StringUtil.isEmpty(filter.getLeerbedrijfNaam()))
		{
			builder.createAlias("bpvMatches", "bpvMatches");
			builder.createAlias("bpvMatches.bpvColoPlaats", "bpvColoPlaats");
			builder.addILikeCheckWildcard("bpvColoPlaats.leerbedrijfNaam", filter
				.getLeerbedrijfNaam(), MatchMode.ANYWHERE);
		}

		if (filter.getVerbintenis() == null && filter.getVerbintenisFilter() != null)
		{
			VerbintenisZoekFilter verbFilter = filter.getVerbintenisFilter();
			if (verbFilter.getAchternaam() != null || verbFilter.getOpleiding() != null
				|| verbFilter.getGroep() != null || verbFilter.getOrganisatieEenheid() != null
				|| verbFilter.getLocatie() != null || verbFilter.getOfficieelofaanspreek() != null
				|| verbFilter.getCohort() != null)
			{
				DetachedCriteria dc = createDetachedCriteria(Verbintenis.class);
				DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
				dcBuilder.createAlias("deelnemer", "deelnemer");
				dcBuilder.createAlias("deelnemer.persoon", "persoon");
				dcBuilder.addEquals("deelnemer.organisatie", EduArteContext.get().getInstelling());
				dcBuilder.addEquals("persoon.organisatie", EduArteContext.get().getInstelling());

				dcBuilder.addILikeCheckWildcard("persoon.roepnaam", verbFilter.getRoepnaam(),
					MatchMode.START);

				dcBuilder.addEquals("opleiding", verbFilter.getOpleiding());
				dcBuilder.addEquals("cohort", verbFilter.getCohort());

				if (StringUtil.isNotEmpty(filter.getVerbintenisFilter().getOfficieelofaanspreek()))
				{
					// officiele achternaam of aanspreekachternaam
					List<Criterion> whereList = new ArrayList<Criterion>();
					whereList.add(builder.createILike("persoon.officieleAchternaam", filter
						.getVerbintenisFilter().getOfficieelofaanspreek(), MatchMode.START));
					whereList.add(builder.createILike("persoon.achternaam", filter
						.getVerbintenisFilter().getOfficieelofaanspreek(), MatchMode.START));
					dcBuilder.addOrs(whereList);
				}

				if (!verbFilter.addOrganisatieEenheidLocatieCriteria(dc))
					return null;
				if (verbFilter.getGroep() != null)
				{
					DetachedCriteria dcGroep = createDetachedCriteria(Groepsdeelname.class);
					DetachedCriteriaBuilder dcGroepBuilder = new DetachedCriteriaBuilder(dcGroep);

					dcGroepBuilder.createAlias("groep", "groep");
					dcGroepBuilder.addEquals("groep", verbFilter.getGroep());
					dcGroepBuilder.addLessOrEquals("begindatum", filter.getPeildatum());
					dcGroepBuilder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
					dcGroepBuilder.addLessOrEquals("groep.begindatum", filter.getPeildatum());
					dcGroepBuilder.addGreaterOrEquals("groep.einddatumNotNull", filter
						.getPeildatum());
					dcGroep.setProjection(Projections.property("deelnemer"));
					dc.add(Subqueries.propertyIn("deelnemer.id", dcGroep));
				}
				filter.setResultCacheable(false);
				dc.setProjection(Projections.property("id"));
				criteria.add(Subqueries.propertyIn("verbintenis", dc));
			}
		}
		return criteria;
	}

	@Override
	public boolean exist(Verbintenis verbintenis)
	{
		Asserts.assertNotNull("verbintenis", verbintenis);
		Criteria criteria = createCriteria(BPVKandidaat.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("verbintenis", verbintenis);
		return cachedTypedList(criteria).size() > 0;
	}

	@Override
	public BPVKandidaat getByVerbintenis(Verbintenis verbintenis)
	{
		Asserts.assertNotNull("verbintenis", verbintenis);
		Criteria criteria = createCriteria(Aanmelding.class);
		criteria.add(Restrictions.eq("verbintenis", verbintenis));
		return cachedUnique(criteria);
	}
}
