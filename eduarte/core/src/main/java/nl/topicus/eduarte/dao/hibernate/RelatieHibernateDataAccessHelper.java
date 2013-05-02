/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.RelatieDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.zoekfilters.RelatieZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class RelatieHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Relatie, RelatieZoekFilter> implements
		RelatieDataAccessHelper
{
	public RelatieHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(RelatieZoekFilter filter)
	{
		Criteria hibernateCriteria =
			filter.isZoekPersonen() ? createCriteria(Persoon.class, "verzorger") : createCriteria(
				Relatie.class, "relatie");
		CriteriaBuilder builder = new CriteriaBuilder(hibernateCriteria);

		if (!filter.isZoekPersonen())
			builder.createAlias("relatie.relatie", "verzorger");
		addCriteria(builder, filter, "relatie", "verzorger");

		return hibernateCriteria;
	}

	@Override
	public void addCriteria(CriteriaBuilder builder, RelatieZoekFilter filter, String relatieAlias,
			String persoonAlias)
	{
		builder.addILikeCheckWildcard(persoonAlias + ".roepnaam", filter.getRoepnaam(),
			MatchMode.START);
		builder.addILikeCheckWildcard(persoonAlias + ".achternaam", filter.getAchternaam(),
			MatchMode.START);
		builder.addVolledigeNaamILike(persoonAlias + ".berekendeZoeknaam",
			filter.getVolledigeNaam());
		builder.addEquals(persoonAlias + ".bsn", filter.getBsn());
		builder.addEquals(persoonAlias + ".geslacht", filter.getGeslacht());
		builder.addGreaterOrEquals(persoonAlias + ".geboortedatum", filter.getGeboortedatumVanaf());
		builder.addLessOrEquals(persoonAlias + ".geboortedatum", filter.getGeboortedatumTotEnMet());
		builder.addEquals(persoonAlias + ".geboorteGemeente", filter.getGeboorteGemeente());
		builder.addEquals(persoonAlias + ".geboorteland", filter.getGeboorteland());
		builder.addNotEquals(persoonAlias + ".geboorteland", filter.getGeboortelandOngelijkAan());

		builder.addNotIn(persoonAlias + ".id", filter.getIdNotIn());
		if (filter.isZoekPersonen())
		{
			DetachedCriteria relatieDc = createDetachedCriteria(Relatie.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(relatieDc);
			relatieDc.setProjection(Projections.property("relatie"));
			dcBuilder.addNotEquals("deelnemer", filter.getDeelnemerOngelijkAan());
			Criterion relatieCriterion = Subqueries.propertyIn(persoonAlias + ".id", relatieDc);

			if (filter.isZoekOngekoppeldePersonen())
			{
				DetachedCriteria deelnemerDc = createDetachedCriteria(Deelnemer.class);
				deelnemerDc.setProjection(Projections.property("persoon"));
				DetachedCriteria medewerkerDc = createDetachedCriteria(Medewerker.class);
				medewerkerDc.setProjection(Projections.property("persoon"));
				builder.getCriteria().add(
					Restrictions.or(
						relatieCriterion,
						Restrictions.and(
							Subqueries.propertyNotIn(persoonAlias + ".id", deelnemerDc),
							Subqueries.propertyNotIn(persoonAlias + ".id", medewerkerDc))));
			}
			else
			{
				builder.getCriteria().add(relatieCriterion);
			}
		}
		else
		{
			builder.addNotEquals(relatieAlias + ".deelnemer", filter.getDeelnemerOngelijkAan());
		}

		builder.addEquals(persoonAlias + ".nationaliteit1", filter.getNationaliteit1());
		builder.addNotEquals(persoonAlias + ".nationaliteit1",
			filter.getNationaliteit1OngelijkAan());
		builder.addEquals(persoonAlias + ".nationaliteit2", filter.getNationaliteit2());
		builder.addNotEquals(persoonAlias + ".nationaliteit2",
			filter.getNationaliteit2OngelijkAan());
		builder.addEquals(persoonAlias + ".bsn", filter.getBsn());

		if (filter.heeftAdresCriteria())
		{
			DetachedCriteria dc = createDetachedCriteria(PersoonAdres.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			dcBuilder.createAlias("adres", "adres");
			dcBuilder
				.addILikeCheckWildcard("adres.postcode", filter.getPostcode(), MatchMode.START);
			dcBuilder.addEquals("adres.huisnummer", filter.getHuisnummer());
			if (filter.getHuisnummerToevoeging() == null)
				dcBuilder.addIsNull("adres.huisnummerToevoeging", true);
			else
				dcBuilder.addEquals("adres.huisnummerToevoeging", filter.getHuisnummerToevoeging());
			dcBuilder.addILikeCheckWildcard("adres.straat", filter.getStraat(), MatchMode.START);
			dcBuilder.addGreaterOrEquals("adres.postcode", filter.getPostcodeVanaf());
			dcBuilder.addLessOrEquals("adres.postcode", filter.getPostcodeTotEnMet());
			dcBuilder.addILikeCheckWildcard("adres.plaats", filter.getPlaats(), MatchMode.START);
			dcBuilder.addNotEquals("adres.plaats", filter.getPlaatsOngelijkAan());
			dcBuilder.addEquals("adres.gemeente", filter.getGemeente());
			dcBuilder.addNotEquals("adres.gemeente", filter.getGemeenteOngelijkAan());
			dcBuilder.addEquals("adres.provincie", filter.getProvincie());
			dcBuilder.addNotEquals("adres.provincie", filter.getProvincieOngelijkAan());
			dcBuilder.addEquals("adres.land", filter.getLand());
			dcBuilder.addNotEquals("adres.land", filter.getLandOngelijkAan());
			dc.setProjection(Projections.property("persoon"));
			builder.propertyIn(persoonAlias + ".id", dc);
		}
	}

	@Override
	public Relatie get(Long id)
	{
		return get(Relatie.class, id);
	}
}
