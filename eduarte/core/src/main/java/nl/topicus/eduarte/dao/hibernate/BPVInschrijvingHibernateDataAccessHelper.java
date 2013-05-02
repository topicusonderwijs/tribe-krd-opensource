package nl.topicus.eduarte.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.BPVInschrijvingDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.DBSMedewerkerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.zoekfilters.BPVInschrijvingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

/**
 * @author vandekamp
 */
public class BPVInschrijvingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<BPVInschrijving, BPVInschrijvingZoekFilter> implements
		BPVInschrijvingDataAccessHelper
{
	public BPVInschrijvingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BPVInschrijvingZoekFilter filter)
	{
		Criteria criteria = createCriteria(BPVInschrijving.class);

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addLessOrEquals("begindatum", filter.getPeildatum());
		builder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
		builder.addEquals("verbintenis", filter.getVerbintenis());
		builder.createAlias("verbintenis", "verbintenis");
		builder.addEquals("verbintenis.deelnemer", filter.getDeelnemer());
		builder.addEquals("bpvBedrijf", filter.getBpvBedrijf());
		builder.addEquals("status", filter.getStatus());
		builder.addNotIn("status", filter.getBpvStatusOngelijkAanList());
		builder.addEquals("redenUitschrijving", filter.getRedenUitschrijving());

		if (filter.getBpvBedrijfAlleRelaties() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(ExterneOrganisatie.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("ondertekeningBPVOdoor", filter.getBpvBedrijfAlleRelaties());
			dc.setProjection(Projections.property("id"));

			List<Criterion> ors = new ArrayList<Criterion>(2);
			ors.add(Restrictions.eq("bpvBedrijf", filter.getBpvBedrijfAlleRelaties()));
			ors.add(Restrictions.eq("contractpartner", filter.getBpvBedrijfAlleRelaties()));
			ors.add(Subqueries.propertyIn("bpvBedrijf", dc));
			ors.add(Subqueries.propertyIn("contractpartner", dc));
			builder.addOrs(ors);
		}

		builder.createAlias("verbintenis", "verbintenis");
		String deelnemerAlias = "deelnemer";
		builder.createAlias("verbintenis.deelnemer", deelnemerAlias);
		builder.createAlias("deelnemer.persoon", "persoon");

		List<Criterion> mentorDocent =
			DataAccessRegistry.getHelper(GroepDataAccessHelper.class).createMentorDocent(filter,
				deelnemerAlias);
		List<Criterion> verantwoordelijkeUitvoerende =
			DataAccessRegistry.getHelper(DBSMedewerkerDataAccessHelper.class)
				.createVerantwoordelijkeUitvoerende(filter, deelnemerAlias);
		DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class)
			.addMentorDocentVerantwoordelijkeUitvoerende(builder, mentorDocent,
				verantwoordelijkeUitvoerende);

		if (filter.getVerbintenis() == null)
		{
			DetachedCriteria dc = createDetachedCriteria(Verbintenis.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			dcBuilder.createAlias("deelnemer", "deelnemer");
			dcBuilder.createAlias("deelnemer.persoon", "persoon");
			dcBuilder.addEquals("deelnemer.organisatie", EduArteContext.get().getInstelling());
			dcBuilder.addEquals("persoon.organisatie", EduArteContext.get().getInstelling());
			dcBuilder.addILikeCheckWildcard("persoon.roepnaam", filter.getRoepnaam(),
				MatchMode.START);
			dcBuilder.addILikeCheckWildcard("persoon.achternaam", filter.getAchternaam(),
				MatchMode.START);
			dcBuilder.addILikeCheckWildcard("persoon.officieleAchternaam", filter.getAchternaam(),
				MatchMode.START);
			dcBuilder.addEquals("opleiding", filter.getOpleiding());

			filter.addOrganisatieEenheidLocatieCriteria(dc);

			if (filter.getGroep() != null)
			{
				DetachedCriteria dcGroep = createDetachedCriteria(Groepsdeelname.class);
				DetachedCriteriaBuilder dcGroepBuilder = new DetachedCriteriaBuilder(dcGroep);

				dcGroepBuilder.createAlias("groep", "groep");
				dcGroepBuilder.addEquals("groep", filter.getGroep());
				dcGroepBuilder.addLessOrEquals("begindatum", filter.getPeildatum());
				dcGroepBuilder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
				dcGroepBuilder.addLessOrEquals("groep.begindatum", filter.getPeildatum());
				dcGroepBuilder.addGreaterOrEquals("groep.einddatumNotNull", filter.getPeildatum());
				dcGroep.setProjection(Projections.property("deelnemer"));
				dc.add(Subqueries.propertyIn("deelnemer.id", dcGroep));
			}
			filter.setResultCacheable(false);
			dc.setProjection(Projections.property("id"));
			criteria.add(Subqueries.propertyIn("verbintenis", dc));
		}

		return criteria;
	}
}
