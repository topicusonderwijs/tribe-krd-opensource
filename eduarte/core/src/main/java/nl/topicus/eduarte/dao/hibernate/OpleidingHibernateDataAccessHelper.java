package nl.topicus.eduarte.dao.hibernate;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.OpleidingAanbod;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.LokaalCompetentieMaximum;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;

public class OpleidingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Opleiding, OpleidingZoekFilter> implements
		OpleidingDataAccessHelper
{
	public OpleidingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(OpleidingZoekFilter filter)
	{
		Criteria criteria = createCriteria(Opleiding.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("verbintenisgebied", "verbintenisgebied");
		if (!filter.addOrganisatieEenheidLocatieDetachedCriteria(this, criteria,
			OpleidingAanbod.class, "opleiding"))
			return null;
		builder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
		if (filter.getCohort() != null)
		{
			builder.addNullOrGreaterOrEquals("einddatum", filter.getCohort().getBegindatum());
			builder.addNullOrLessOrEquals("begindatum", filter.getCohort().getEinddatum());
		}
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		if (EduArteContext.get().getAccount() != null
			&& RechtenSoort.DIGITAALAANMELDER == EduArteContext.get().getAccount()
				.getRechtenSoort())
			builder.addILikeCheckWildcard("wervingsnaam", filter.getNaam(), MatchMode.ANYWHERE);
		else
			builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("leerweg", filter.getLeerweg());
		builder.addILikeCheckWildcard("verbintenisgebied.taxonomiecode", filter.getTaxonomiecode(),
			MatchMode.START);
		builder.addEquals("verbintenisgebied", filter.getVerbintenisgebied());
		builder.addEquals("verbintenisgebied.niveau", filter.getVerbintenisgebiedniveau());
		if (filter.getGroep() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(Groepsdeelname.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dc.setProjection(Projections.property("deelnemer"));
			dcBuilder.addEquals("groep", filter.getGroep());

			DetachedCriteria dcDeelnemer = createDetachedCriteria(Deelnemer.class);
			dcDeelnemer.setProjection(Projections.property("id"));
			dcDeelnemer.add(Subqueries.propertyIn("id", dc));

			DetachedCriteria dcVerbintenis = createDetachedCriteria(Verbintenis.class);
			dcVerbintenis.setProjection(Projections.property("opleiding"));
			dcVerbintenis.add(Subqueries.propertyIn("deelnemer", dcDeelnemer));

			criteria.add(Subqueries.propertyIn("id", dcVerbintenis));
		}

		filter.addQuickSearchCriteria(builder, "code", "naam", "wervingsnaam");
		if (filter.getMaximumAanwezig() != null)
		{
			if (filter.getMaximumAanwezig())
			{
				DetachedCriteria dcLokaalCompetentieMaximum =
					createDetachedCriteria(LokaalCompetentieMaximum.class);
				dcLokaalCompetentieMaximum.setProjection(Projections.property("opleiding"));
				criteria.add(Subqueries.propertyIn("id", dcLokaalCompetentieMaximum));
			}
			else
			{
				DetachedCriteria dcLokaalCompetentieMaximum =
					createDetachedCriteria(LokaalCompetentieMaximum.class);
				dcLokaalCompetentieMaximum.setProjection(Projections.property("opleiding"));
				criteria.add(Subqueries.propertyNotIn("id", dcLokaalCompetentieMaximum));
			}
		}

		// toon alleen de opleidingen die geen opleidingaanbod hebben (wezen)
		if (!filter.getIsAangeboden())
		{
			builder.createAlias("aanbod", "aanbod", CriteriaSpecification.LEFT_JOIN);
			builder.addIsNull("aanbod.locatie", true);
			builder.addIsNull("aanbod.organisatieEenheid", true);
		}

		builder.addEquals("parent", filter.getVariantVan());

		return criteria;
	}

	@Override
	public Opleiding getEersteOpleiding(OpleidingZoekFilter filter)
	{
		Criteria criteria = createCriteria(filter);
		criteria.setMaxResults(1);
		return unique(criteria, filter.isResultCacheable());
	}

	@Override
	public Opleiding getEersteOpleidingGevolgdDoorAlleDeelnemers(List<Deelnemer> deelnemers)
	{
		if (deelnemers.isEmpty())
			return null;

		Set<Opleiding> result = getOpleidingenVoorDeelnemer(deelnemers.get(0));
		for (Deelnemer curDeelnemer : deelnemers.subList(1, deelnemers.size()))
		{
			result.retainAll(getOpleidingenVoorDeelnemer(curDeelnemer));
			if (result.isEmpty())
				return null;
		}
		return result.iterator().next();
	}

	private Set<Opleiding> getOpleidingenVoorDeelnemer(Deelnemer deelnemer)
	{
		Set<Opleiding> ret = new LinkedHashSet<Opleiding>();
		for (Verbintenis curVerbintenis : deelnemer.getVerbintenissen())
		{
			ret.add(curVerbintenis.getOpleiding());
		}
		return ret;
	}

	@Override
	public List<Opleiding> getOpleidingen(String code)
	{
		Asserts.assertNotNull("code", code);
		Criteria criteria = createCriteria(Opleiding.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("code", code);
		return cachedTypedList(criteria);
	}
}
