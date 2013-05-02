package nl.topicus.eduarte.dao.hibernate.bpv;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.eduarte.dao.helpers.bpv.BPVPlaatsDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVMatch;
import nl.topicus.eduarte.entities.bpv.BPVPlaats;
import nl.topicus.eduarte.entities.bpv.BPVKandidaat.MatchingType;
import nl.topicus.eduarte.zoekfilters.bpv.BPVPlaatsZoekFilter;
import nl.topicus.eduarte.zoekfilters.bpv.BPVPlaatsZoekFilter.BPVPlaatsStatus;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;

public class BPVPlaatsHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<BPVPlaats, BPVPlaatsZoekFilter> implements
		BPVPlaatsDataAccessHelper
{
	public BPVPlaatsHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BPVPlaatsZoekFilter filter)
	{
		Criteria criteria = createCriteria(BPVPlaats.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("type", filter.getType());

		// TODO filter op bpvCriteria?
		builder.addGreaterOrEquals("aantalPlaatsen", filter.getAantalPlaatsen());
		builder.addGreaterOrEquals("aantalStudenten", filter.getAantalStudenten());
		builder.addLessOrEquals("begeleidingsUren", filter.getBegeleidingsUren());
		builder.addEquals("bedrijfsContactPersoon", filter.getBedrijfsContactPersoon());
		// vanaf
		builder.addGreaterOrEquals("begindatum", filter.getBegindatum());
		// tot max
		builder.addLessOrEquals("einddatum", filter.getEinddatum());
		builder.addEquals("matchingDoorInstelling", filter.isMatchingDoorInstelling());
		builder.addEquals("matchingDoorStudenten", filter.isMatchingDoorStudenten());
		builder.addILikeCheckWildcard("opdrachtOmschrijving", filter.getOpdrachtOmschrijving(),
			MatchMode.ANYWHERE);
		// minimaal
		builder.addGreaterOrEquals("vergoeding", filter.getVergoeding());
		// minimaal
		builder.addGreaterOrEquals("urenPerWeek", filter.getUrenPerWeek());
		builder.addGreaterOrEquals("dagenPerWeek", filter.getDagenPerWeek());
		builder.addEquals("bedrijf", filter.getBedrijf());
		builder.addIn("opleiding", filter.getGeschikteOpleidingen());

		if (filter.getBpvPlaatsStatus() != null
			&& filter.getBpvPlaatsStatus() != BPVPlaatsStatus.ALLES)
		{
			if (filter.getBpvPlaatsStatus() == BPVPlaatsStatus.NIET_GEACCORDEERD)
			{
				// alleen stageplaasten die geen BPVMatch hebben die geaccordeerd is
				DetachedCriteria dcStatus = createDetachedCriteria(BPVMatch.class);
				DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dcStatus);
				dcBuilder.addEquals("matchAkkoord", true);
				dcStatus.setProjection(Projections.property("bpvPlaats"));
				criteria.add(Subqueries.propertyNotIn("id", dcStatus));
			}
			else if (filter.getBpvPlaatsStatus() == BPVPlaatsStatus.NIET_GEMATCHED_DOOR_INSTELLING)
			{
				// alleen stageplaasten die nog geen BPVMatch hebben die door de
				// instelling is aangemaakt
				DetachedCriteria dcStatus = createDetachedCriteria(BPVMatch.class);
				DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dcStatus);
				dcStatus.createAlias("bpvKandidaat", "bpvKandidaat");
				dcBuilder.addEquals("bpvKandidaat.matchingType", MatchingType.Instelling);
				dcStatus.setProjection(Projections.property("bpvPlaats"));
				criteria.add(Subqueries.propertyNotIn("id", dcStatus));
			}
			else if (filter.getBpvPlaatsStatus() == BPVPlaatsStatus.NIET_GEMATCHED)
			{
				// alleen stageplaasten die helemaal nog geen BPVMatch hebben
				DetachedCriteria dcStatus = createDetachedCriteria(BPVMatch.class);
				dcStatus.setProjection(Projections.property("bpvPlaats"));
				criteria.add(Subqueries.propertyNotIn("id", dcStatus));
			}
		}
		return criteria;
	}
}