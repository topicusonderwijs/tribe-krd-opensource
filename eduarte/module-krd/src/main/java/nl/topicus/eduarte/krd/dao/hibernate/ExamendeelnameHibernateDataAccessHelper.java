/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.opleiding.SoortOnderwijsTax;
import nl.topicus.eduarte.krd.dao.helpers.ExamendeelnameDataAccessHelper;
import nl.topicus.eduarte.krd.zoekfilters.ExamendeelnameZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class ExamendeelnameHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Examendeelname, ExamendeelnameZoekFilter> implements
		ExamendeelnameDataAccessHelper
{
	public ExamendeelnameHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ExamendeelnameZoekFilter filter)
	{
		Criteria criteria = createCriteria(Examendeelname.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		addAliassesForSelectionCriteria(criteria, filter.getOrderByList());
		builder.addEquals("verbintenis", filter.getVerbintenis());
		builder.addEquals("verbintenis.deelnemer", filter.getDeelnemer());
		builder.addEquals("verbintenis.cohort", filter.getCohort());
		builder.addEquals("examenstatus", filter.getExamenstatus());
		builder.addIn("examenstatus", filter.getExamenstatusList());
		builder.addNotEquals("examenstatus", filter.getExamenstatusOngelijkAan());
		builder.addEquals("examenstatus.examenWorkflow", filter.getExamenworkflow());
		builder.addEquals("verbintenisgebied.taxonomie", filter.getTaxonomie());
		builder.addGreaterOrEquals("verbintenis.einddatumNotNull", filter.getPeildatum());
		if (!filter.addOrganisatieEenheidLocatieCriteria("verbintenis", criteria))
			return null;

		if (StringUtil.isNotEmpty(filter.getOfficieelofaanspreek()))
		{
			// officiele achternaam of aanspreekachternaam
			List<Criterion> whereList = new ArrayList<Criterion>();
			whereList.add(builder.createILike("persoon.officieleAchternaam",
				filter.getOfficieelofaanspreek(), MatchMode.START));
			whereList.add(builder.createILike("persoon.achternaam",
				filter.getOfficieelofaanspreek(), MatchMode.START));
			builder.addOrs(whereList);
		}

		builder.addEquals("verbintenis.opleiding", filter.getOpleiding());
		if (filter.getGroep() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(Groepsdeelname.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			dcBuilder.createAlias("groep", "groep");
			dcBuilder.addEquals("groep", filter.getGroep());
			dcBuilder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
			dcBuilder.addGreaterOrEquals("groep.einddatumNotNull", filter.getPeildatum());
			dc.setProjection(Projections.property("deelnemer"));
			builder.propertyIn("deelnemer.id", dc);
			filter.setResultCacheable(false);
		}
		if (filter.getBronOnderwijssoort() != null)
		{
			switch (filter.getBronOnderwijssoort())
			{
				case BEROEPSONDERWIJS:
					builder
						.addOrs(Restrictions.ilike("verbintenisgebied.taxonomiecode", "1",
							MatchMode.START), Restrictions.ilike("verbintenisgebied.taxonomiecode",
							"2", MatchMode.START));
					break;
				case EDUCATIE:
					builder.addILikeFixedMatchMode("verbintenisgebied.taxonomiecode", "4",
						MatchMode.START);
					break;
				case VAVO:
					builder.addILikeFixedMatchMode("verbintenisgebied.taxonomiecode", "3",
						MatchMode.START);
					builder.addILikeFixedMatchMode("verbintenisgebied.externeCode", "5",
						MatchMode.START);
					break;
				case VOORTGEZETONDERWIJS:
					builder.addILikeFixedMatchMode("verbintenisgebied.taxonomiecode", "3",
						MatchMode.START);
					builder.addNotILikeFixedMatchMode("verbintenisgebied.externeCode", "5",
						MatchMode.START);
					break;
				default:
					break;
			}
		}

		if (filter.isAlleenGewijzigde())
			builder.addEquals("gewijzigd", true);

		for (SoortOnderwijsTax soortOnderwijs : filter.getSoortOnderwijsTax())
		{
			builder.addILikeFixedMatchMode("verbintenisgebied.taxonomiecode",
				soortOnderwijs.getTaxCode(), MatchMode.START);
		}

		if (filter.getSchooljaar() != null)
		{
			Criterion datumUitslagInSchooljaar =
				Restrictions.and(
					Restrictions.le("datumUitslag", filter.getSchooljaar().getEinddatumNotNull()),
					Restrictions.ge("datumUitslag", filter.getSchooljaar().getBegindatum()));
			Criterion verwachteEinddatumInSchooljaar =
				Restrictions.and(Restrictions.and(Restrictions.le("verbintenis.geplandeEinddatum",
					filter.getSchooljaar().getEinddatumNotNull()), Restrictions.ge(
					"verbintenis.geplandeEinddatum", filter.getSchooljaar().getBegindatum())),
					Restrictions.isNull("datumUitslag"));
			Criterion examenjaarGelijkAanEindVanSchooljaar =
				Restrictions.and(
					Restrictions.and(Restrictions.isNull("datumUitslag"),
						Restrictions.isNull("verbintenis.geplandeEinddatum")),
					Restrictions.eq("examenjaar", filter.getSchooljaar().getEindJaar()));
			builder.addOrs(datumUitslagInSchooljaar, verwachteEinddatumInSchooljaar,
				examenjaarGelijkAanEindVanSchooljaar);
		}

		return criteria;
	}

	@Override
	public List<Long> getExamenDeelnameIds(ExamendeelnameZoekFilter filter)
	{
		Asserts.assertNotNull("filter.schooljaar", filter.getSchooljaar());
		Criteria criteria = createCriteria(filter);
		criteria.add(Restrictions.eq("meenemenInVolgendeBronBatch", true));
		criteria.setProjection(Projections.property("id"));
		return cachedList(criteria);
	}

	@Override
	public long getAantalExamendeelnamesInWachtrij(ExamendeelnameZoekFilter filter)
	{
		Criteria criteria = createCriteria(filter);
		criteria.add(Restrictions.eq("meenemenInVolgendeBronBatch", true));
		criteria.setProjection(Projections.rowCount());
		return (Long) cachedUnique(criteria);
	}

	@Override
	protected void addAliassesForSelectionCriteria(Criteria criteria, List<String> orderBy)
	{
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("verbintenis", "verbintenis");
		builder.createAlias("verbintenis.opleiding", "opleiding");
		builder.createAlias("opleiding.verbintenisgebied", "verbintenisgebied");
		builder.createAlias("verbintenis.deelnemer", "deelnemer");
		builder.createAlias("deelnemer.persoon", "persoon");
		builder.createAlias("examenstatus", "examenstatus");
	}
}