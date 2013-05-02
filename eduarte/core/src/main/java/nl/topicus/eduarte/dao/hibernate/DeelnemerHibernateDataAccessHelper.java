/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import static nl.topicus.cobra.util.TimeUtil.*;
import static org.hibernate.criterion.Restrictions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.DBSMedewerkerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VrijVeldDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.kenmerk.DeelnemerKenmerk;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.DeelnemerBijlage;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.security.authentication.DeelnemerAccount;
import nl.topicus.eduarte.entities.vrijevelden.PersoonVrijVeld;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekFilter;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekFilter.TypeAdres;

import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinFragment;
import org.hibernate.type.Type;

public class DeelnemerHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Deelnemer, DeelnemerZoekFilter> implements
		DeelnemerDataAccessHelper
{
	public DeelnemerHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(DeelnemerZoekFilter filter)
	{
		Criteria criteria = createCriteria(Deelnemer.class, "deelnemer");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("deelnemer.persoon", "persoon");
		builder.addEquals("persoon.organisatie", EduArteContext.get().getInstelling());
		addCriteria(builder, filter, "deelnemer", "persoon");
		return criteria;
	}

	@Override
	public void addCriteria(CriteriaBuilder builder, DeelnemerZoekFilter filter,
			String deelnemerAlias, String persoonAlias)
	{
		if (filter.getDeelnemerIdsIn() != null && filter.getDeelnemerIdsIn().isEmpty())
		{
			// zorg dat altijd lege lijst wordt geretourneerd
			builder.getCriteria().add(Restrictions.sqlRestriction("0 = 1"));
			return;
		}
		builder.addIn(deelnemerAlias + ".id", filter.getDeelnemerIdsIn());

		if (filter.getHeeftAccount() != null)
		{
			DetachedCriteria dcAccount = createDetachedCriteria(DeelnemerAccount.class);
			dcAccount.setProjection(Projections.property("deelnemer"));
			DetachedCriteriaBuilder dcAccountBuilder = new DetachedCriteriaBuilder(dcAccount);
			dcAccountBuilder.addIsNull("deelnemer", false);

			if (filter.getHeeftAccount())
				builder.propertyIn(deelnemerAlias + ".id", dcAccount);
			else
				builder.propertyNotIn(deelnemerAlias + ".id", dcAccount);
			filter.setResultCacheable(false);
		}

		builder.addEquals(deelnemerAlias + ".deelnemernummer", filter.getDeelnemernummer());
		builder.addGreaterOrEquals(deelnemerAlias + ".deelnemernummer", filter
			.getDeelnemernummerVanaf());
		builder.addLessOrEquals(deelnemerAlias + ".deelnemernummer", filter
			.getDeelnemernummerTotEnMet());
		builder.addILikeCheckWildcard(persoonAlias + ".roepnaam", filter.getRoepnaam(),
			MatchMode.START);
		builder.addIsNull(persoonAlias + ".roepnaam", filter.getRoepnaamIsLeeg());
		builder.addILikeCheckWildcard(persoonAlias + ".voornamen", filter.getVoornamen(),
			MatchMode.START);
		builder.addIsNull(persoonAlias + ".voornamen", filter.getVoornamenIsLeeg());
		builder.addILikeCheckWildcard(persoonAlias + ".voorletters", filter.getVoorletters(),
			MatchMode.START);
		builder.addIsNull(persoonAlias + ".voorletters", filter.getVoorlettersIsLeeg());
		builder.addEquals(persoonAlias + ".debiteurennummer", filter.getDebiteurennummer());

		builder.addILikeCheckWildcard(persoonAlias + ".officieleVoorvoegsel", filter
			.getOfficieleVoorvoegsel(), MatchMode.START);
		builder.addILikeCheckWildcard(persoonAlias + ".voorvoegsel", filter.getVoorvoegsel(),
			MatchMode.START);
		builder.addILikeCheckWildcard(persoonAlias + ".officieleAchternaam",
			filter.getAchternaam(), MatchMode.START);
		builder.addILikeCheckWildcard(persoonAlias + ".achternaam", filter.getAanspreeknaam(),
			MatchMode.START);
		if (filter.getOfficieleNaamWijktAf() != null)
		{
			if (filter.getOfficieleNaamWijktAf().booleanValue())
			{
				builder.getCriteria().add(
					Restrictions.neProperty(persoonAlias + ".achternaam", persoonAlias
						+ ".officieleAchternaam"));
			}
			else
			{
				builder.getCriteria().add(
					Restrictions.eqProperty(persoonAlias + ".achternaam", persoonAlias
						+ ".officieleAchternaam"));
			}
		}

		builder.addEquals(persoonAlias + ".datumInNederland", filter.getDatumInNL());
		builder.addGreaterOrEquals(persoonAlias + ".datumInNederland", filter.getDatumInNLVanaf());
		builder.addLessOrEquals(persoonAlias + ".datumInNederland", filter.getDatumInNLTotEnMet());
		builder.addIsNull(persoonAlias + ".datumInNederland", filter.getDatumInNLIsLeeg());
		builder.addEquals(deelnemerAlias + ".allochtoon", filter.getAllochtoon());
		builder.addEquals(deelnemerAlias + ".lgf", filter.getLgf());
		builder.addEquals(persoonAlias + ".nieuwkomer", filter.getNieuwkomer());
		builder.addVolledigeNaamILike(persoonAlias + ".berekendeZoeknaam", filter
			.getVolledigeNaam());
		builder.addEquals(persoonAlias + ".bsn", filter.getBsn());
		builder.addIsNull(persoonAlias + ".bsn", filter.getBsnIsLeeg());
		builder.addEquals(deelnemerAlias + ".onderwijsnummer", filter.getOnderwijsnummer());
		builder.addIsNull(deelnemerAlias + ".onderwijsnummer", filter.getOnderwijsnummerIsLeeg());
		builder.addEquals(persoonAlias + ".geslacht", filter.getGeslacht());
		builder.addEquals(deelnemerAlias + ".bronStatus", filter.getBronStatus());

		if (filter.getStartkwalificatieplichtigOpDatum() != null)
		{
			Date startkwalificatieOpBasisVanGeboortedatum =
				TimeUtil.getInstance().addYears(filter.getStartkwalificatieplichtigOpDatum(), -24);
			Criterion opBasisVanGeboortedatum =
				Restrictions.and(Restrictions.isNull(deelnemerAlias
					+ ".startkwalificatieplichtigTot"), Restrictions.ge(persoonAlias
					+ ".geboortedatum", startkwalificatieOpBasisVanGeboortedatum));
			Criterion opBasisVanProperty =
				Restrictions.and(Restrictions.isNotNull(deelnemerAlias
					+ ".startkwalificatieplichtigTot"), Restrictions
					.ge(deelnemerAlias + ".startkwalificatieplichtigTot", filter
						.getStartkwalificatieplichtigOpDatum()));
			builder.addOrs(opBasisVanGeboortedatum, opBasisVanProperty);
		}

		if (filter.getGeboortedatum() != null)
		{
			builder.addEquals(persoonAlias + ".geboortedatum", filter.getGeboortedatum());
		}
		else
		{
			builder.addGreaterOrEquals(persoonAlias + ".geboortedatum", filter
				.getGeboortedatumVanaf());
			builder.addLessOrEquals(persoonAlias + ".geboortedatum", filter
				.getGeboortedatumTotEnMet());
		}
		builder.addIsNull(persoonAlias + ".geboortedatum", filter.getGeboortedatumIsLeeg());

		if (filter.getRegistratieDatum() != null)
		{
			builder.addEquals(deelnemerAlias + ".registratieDatum", filter.getRegistratieDatum());
		}
		else
		{
			builder.addGreaterOrEquals(deelnemerAlias + ".registratieDatum", filter
				.getRegistratieDatumVanaf());
			builder.addLessOrEquals(deelnemerAlias + ".registratieDatum", filter
				.getRegistratieDatumTotEnMet());
		}
		builder.addIsNull(deelnemerAlias + ".registratieDatum", filter.getRegistratieDatumIsLeeg());

		if (filter.isMeerderjarig() != null)
		{
			if (filter.isMeerderjarig())
				builder.addLessOrEquals(persoonAlias + ".geboortedatum", TimeUtil.getInstance()
					.addYears(filter.getPeilEindDatum(), -18));
			else
				builder.addGreaterOrEquals(persoonAlias + ".geboortedatum", TimeUtil.getInstance()
					.addYears(filter.getPeilEindDatum(), -18));
		}
		if (filter.getOverleden() != null)
		{
			builder.addIsNull(persoonAlias + ".datumOverlijden", !filter.getOverleden()
				.booleanValue());
		}

		if (filter.getHeeftMeerdereVerbintenissen() != null
			&& filter.getHeeftMeerdereVerbintenissen().booleanValue())
		{
			DetachedCriteria dc = createDetachedCriteria(Verbintenis.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addLessOrEquals("begindatum", filter.getPeilEindDatum());
			dcBuilder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
			// Beetje hacken met sql projections. Het stuk dat je niet begrijpt is de
			// group by, die ook meteen maar een having doet, want dan komt het op de
			// juiste plek te staan.
			dc.setProjection(Projections.sqlGroupProjection("{alias}.deelnemer",
				"{alias}.deelnemer having count({alias}.id) > 1", new String[0], new Type[0]));
			builder.propertyIn("deelnemer", dc);
		}

		// als wettelijkeVertegenwoordiger false is, dan alle deelnemers met relatie en
		// wettelijkeVertegenwoordiger op false, en alle deelnemers zonder relatie tonen.
		if (filter.isVertegenwoordiger() != null)
		{

			if (filter.isVertegenwoordiger())
			{
				DetachedCriteria dc = createDetachedCriteria(Relatie.class);
				DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
				dcBuilder.addEquals("wettelijkeVertegenwoordiger", filter.isVertegenwoordiger());
				dc.setProjection(Projections.property("deelnemer"));
				builder.propertyIn(deelnemerAlias + ".persoon", dc);
			}
			else
			{

				DetachedCriteria dc1 = createDetachedCriteria(Relatie.class);
				DetachedCriteriaBuilder dcBuilder1 = new DetachedCriteriaBuilder(dc1);
				dcBuilder1.addEquals("wettelijkeVertegenwoordiger", filter.isVertegenwoordiger());
				dc1.setProjection(Projections.property("deelnemer"));

				DetachedCriteria dc2 = createDetachedCriteria(Relatie.class);
				dc2.setProjection(Projections.property("deelnemer"));
				builder.addOrs(Subqueries.propertyNotIn(deelnemerAlias + ".persoon", dc2),
					Subqueries.propertyIn(deelnemerAlias + ".persoon", dc1));
			}
		}

		// als betalingsplichtige false is, dan alle deelnemers met relatie en betalings
		// plichtinge op false, en alle deelnemers zonder relatie.
		if (filter.isBetalingsplichtige() != null)
		{
			if (filter.isBetalingsplichtige())
			{
				DetachedCriteria dc = createDetachedCriteria(Relatie.class);
				DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
				dcBuilder.addEquals("betalingsplichtige", filter.isBetalingsplichtige());
				dc.setProjection(Projections.property("deelnemer"));
				builder.propertyIn(deelnemerAlias + ".persoon", dc);
			}
			else
			{
				DetachedCriteria dc1 = createDetachedCriteria(Relatie.class);
				DetachedCriteriaBuilder dcBuilder1 = new DetachedCriteriaBuilder(dc1);
				dcBuilder1.addEquals("betalingsplichtige", filter.isBetalingsplichtige());
				dc1.setProjection(Projections.property("deelnemer"));

				DetachedCriteria dc2 = createDetachedCriteria(Relatie.class);
				dc2.setProjection(Projections.property("deelnemer"));
				builder.addOrs(Subqueries.propertyNotIn(deelnemerAlias + ".persoon", dc2),
					Subqueries.propertyIn(deelnemerAlias + ".persoon", dc1));
			}
		}
		builder.addEquals(deelnemerAlias + ".uitsluitenVanFacturatie", filter
			.getUitsluitenVanFacturatie());

		builder.addEquals(persoonAlias + ".geboorteGemeente", filter.getGeboorteGemeente());
		builder.addEquals(persoonAlias + ".geboorteland", filter.getGeboorteland());
		builder.addIn(persoonAlias + ".geboorteland", filter.getGeboortelandList());
		if (filter.getGeboortelandOngelijkAanNL() != null)
		{
			if (filter.getGeboortelandOngelijkAanNL().booleanValue())
			{
				builder.addNullOrNotEquals(persoonAlias + ".geboorteland", Land.getNederland());
			}
			else
			{
				builder.addEquals(persoonAlias + ".geboorteland", Land.getNederland());
			}
		}
		if (filter.getGeboortelandIsLeeg() != null)
		{
			builder.addIsNull(persoonAlias + ".geboorteland", filter.getGeboortelandIsLeeg()
				.booleanValue());
		}

		builder.addOrEquals(persoonAlias + ".nationaliteit1", persoonAlias + ".nationaliteit2",
			filter.getNationaliteit());
		builder.addOrIn(persoonAlias + ".nationaliteit1", persoonAlias + ".nationaliteit2", filter
			.getNationaliteitList());
		if (filter.getNationaliteitOngelijkAanNL() != null)
		{
			if (filter.getNationaliteitOngelijkAanNL().booleanValue())
			{
				builder.addNullOrNotEquals(persoonAlias + ".nationaliteit1", Nationaliteit
					.getNederlands());
				builder.addNullOrNotEquals(persoonAlias + ".nationaliteit2", Nationaliteit
					.getNederlands());
			}
			else
			{
				builder.addOrEquals(persoonAlias + ".nationaliteit1", persoonAlias
					+ ".nationaliteit2", Nationaliteit.getNederlands());
			}
		}
		if (filter.getNationaliteitIsLeeg() != null)
		{
			if (filter.getNationaliteitIsLeeg().booleanValue())
			{
				// Allebei moeten leeg zijn, we kunnen ze dus veilig achter elkaar
				// toevoegen aan de query.
				builder.addIsNull(persoonAlias + ".nationaliteit1", true);
				builder.addIsNull(persoonAlias + ".nationaliteit2", true);
			}
			else
			{
				// Het is voldoende als 1 van de 2 niet null is.
				builder.getCriteria().add(
					Restrictions.or(Restrictions.isNotNull(persoonAlias + ".nationaliteit1"),
						Restrictions.isNotNull(persoonAlias + ".nationaliteit2")));
			}
		}
		if (filter.getDubbeleNationaliteit() != null)
		{
			if (filter.getDubbeleNationaliteit().booleanValue())
			{
				builder.addIsNull(persoonAlias + ".nationaliteit1", false);
				builder.addIsNull(persoonAlias + ".nationaliteit2", false);
				builder.getCriteria().add(
					Restrictions.neProperty(persoonAlias + ".nationaliteit1", persoonAlias
						+ ".nationaliteit2"));
			}
			else
			{
				builder.getCriteria().add(
					Restrictions.or(Restrictions.isNull(persoonAlias + ".nationaliteit1"),
						Restrictions.isNull(persoonAlias + ".nationaliteit2")));
			}
		}

		if (filter.heeftAdresCriteria())
		{
			DetachedCriteria dc = createDetachedCriteria(PersoonAdres.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			dcBuilder.createAlias("adres", "adres");
			dcBuilder.addEquals("adres.organisatie", EduArteContext.get().getInstelling());
			dcBuilder.addLessOrEquals("begindatum", filter.getPeilEindDatum());
			dcBuilder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
			if (filter.getTypeAdres() == TypeAdres.Postadres
				|| filter.getTypeAdres() == TypeAdres.Beide)
			{
				dcBuilder.addEquals("postadres", Boolean.TRUE);
			}
			if (filter.getTypeAdres() == TypeAdres.Woonadres
				|| filter.getTypeAdres() == TypeAdres.Beide)
			{
				dcBuilder.addEquals("fysiekadres", Boolean.TRUE);
			}
			dcBuilder.addEquals("adres.geheim", filter.getGeheimAdres());
			dcBuilder
				.addILikeCheckWildcard("adres.postcode", filter.getPostcode(), MatchMode.START);
			dcBuilder.addEquals("adres.huisnummer", filter.getHuisnummer());
			dcBuilder.addEquals("adres.huisnummerToevoeging", filter.getHuisnummerToevoeging());
			dcBuilder.addILikeCheckWildcard("adres.straat", filter.getStraat(), MatchMode.START);
			dcBuilder.addGreaterOrEquals("adres.postcode", filter.getPostcodeVanaf());
			dcBuilder.addLessOrEquals("adres.postcode", filter.getPostcodeTotEnMet());
			dcBuilder.addILikeCheckWildcard("adres.plaats", filter.getPlaats(), MatchMode.START);
			dcBuilder.addNotEquals("adres.plaats", filter.getPlaatsOngelijkAan());
			dcBuilder.addEquals("adres.gemeente", filter.getGemeente());
			dcBuilder.addNullOrNotEquals("adres.gemeente", filter.getGemeenteOngelijkAan());
			dcBuilder.addEquals("adres.provincie", filter.getProvincie());
			dcBuilder.addNullOrNotEquals("adres.provincie", filter.getProvincieOngelijkAan());
			dcBuilder.addEquals("adres.land", filter.getLand());
			dcBuilder.addNotEquals("adres.land", filter.getLandOngelijkAan());
			dc.setProjection(Projections.property("persoon"));
			builder.propertyIn(deelnemerAlias + ".persoon", dc);
			filter.setResultCacheable(false);
		}
		if (StringUtil.isNotEmpty(filter.getOfficieelofaanspreek()))
		{
			// officiele achternaam of aanspreekachternaam
			List<Criterion> whereList = new ArrayList<Criterion>();
			whereList.add(builder.createILike(persoonAlias + ".officieleAchternaam", filter
				.getOfficieelofaanspreek(), MatchMode.START));
			whereList.add(builder.createILike(persoonAlias + ".achternaam", filter
				.getOfficieelofaanspreek(), MatchMode.START));
			builder.addOrs(whereList);
		}
		if (filter.getGroep() != null || filter.getGroepstype() != null
			|| (filter.getGroepList() != null && !filter.getGroepList().isEmpty()))
		{
			DetachedCriteria dc = createDetachedCriteria(Groepsdeelname.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			dcBuilder.createAlias("groep", "groep");
			dcBuilder.addEquals("groep", filter.getGroep());
			dcBuilder.addIn("groep", filter.getGroepList());
			dcBuilder.addEquals("groep.groepstype", filter.getGroepstype());
			dcBuilder.addLessOrEquals("begindatum", filter.getPeilEindDatum());
			dcBuilder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
			if (filter.getGroepsdeelnameBeeindigd() != null)
			{
				// alleen groepsdeelnames die (niet) eindigen in de peilperiode
				if (!filter.getGroepsdeelnameBeeindigd())
					dcBuilder.addGreaterThan("einddatumNotNull", filter.getPeilEindDatum());
				else
				{
					dcBuilder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
					dcBuilder.addLessOrEquals("einddatumNotNull", filter.getPeilEindDatum());
				}
			}
			dcBuilder.addLessOrEquals("groep.begindatum", filter.getPeilEindDatum());
			dcBuilder.addGreaterOrEquals("groep.einddatumNotNull", filter.getPeildatum());
			dc.setProjection(Projections.property("deelnemer"));
			builder.propertyIn(deelnemerAlias + ".id", dc);
			filter.setResultCacheable(false);
		}
		if (filter.getKenmerkCategorie() != null || filter.getKenmerk() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(DeelnemerKenmerk.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			dcBuilder.createAlias("kenmerk", "kenmerk");
			dcBuilder.addEquals("kenmerk", filter.getKenmerk());
			dcBuilder.addEquals("kenmerk.categorie", filter.getKenmerkCategorie());
			dcBuilder.addLessOrEquals("begindatum", filter.getPeilEindDatum());
			dcBuilder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
			dc.setProjection(Projections.property("deelnemer"));
			builder.propertyIn(deelnemerAlias + ".id", dc);
			filter.setResultCacheable(false);
		}
		if (filter.getKenmerkenLijst() != null && !filter.getKenmerkenLijst().isEmpty())
		{
			DetachedCriteria dc = createDetachedCriteria(DeelnemerKenmerk.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			dcBuilder.createAlias("kenmerk", "kenmerk");
			dcBuilder.addIn("kenmerk", filter.getKenmerkenLijst());
			dcBuilder.addLessOrEquals("begindatum", filter.getPeildatum());
			dcBuilder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
			dc.setProjection(Projections.property("deelnemer"));
			builder.propertyIn(deelnemerAlias + ".id", dc);
			filter.setResultCacheable(false);
		}
		if (filter.heeftVooropleidingCriteria())
		{
			DetachedCriteria dc = createDetachedCriteria(Vooropleiding.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.createAlias("soortVooropleiding", "soortVooropleiding",
				JoinFragment.LEFT_OUTER_JOIN);
			dcBuilder.addEquals("brincode", filter.getBrinVooropleiding());
			dcBuilder.addIn("brincode", filter.getBrinVooropleidingList());

			if (filter.getVooropleidingExterneOrganisatie() != null)
				dcBuilder.addEquals("externeOrganisatie", filter
					.getVooropleidingExterneOrganisatie());

			if (filter.getCategorieVooropleiding() != null)
			{
				dcBuilder.getCriteria().add(
					Restrictions.or(Restrictions.and(Restrictions.eq(
						"soortVooropleiding.soortOnderwijsMetDiploma", filter
							.getCategorieVooropleiding()), Restrictions.eq("diplomaBehaald",
						Boolean.TRUE)), Restrictions.and(Restrictions.eq(
						"soortVooropleiding.soortOnderwijsZonderDiploma", filter
							.getCategorieVooropleiding()), Restrictions.eq("diplomaBehaald",
						Boolean.FALSE))));
			}
			dcBuilder.addEquals("soortVooropleiding", filter.getSoortVooropleiding());
			dcBuilder.addGreaterOrEquals("begindatum", filter.getVooropleidingBegindatumVanaf());
			dcBuilder.addLessOrEquals("begindatum", filter.getVooropleidingBegindatumTotEnMet());
			dcBuilder.addGreaterOrEquals("einddatum", filter.getVooropleidingEinddatumVanaf());
			dcBuilder.addLessOrEquals("einddatum", filter.getVooropleidingEinddatumTotEnMet());
			dcBuilder.addGreaterOrEquals("aantalJarenOnderwijs", filter
				.getVooropleidingAantalJarenOnderwijsVanaf());
			dcBuilder.addLessOrEquals("aantalJarenOnderwijs", filter
				.getVooropleidingAantalJarenOnderwijsTotEnMet());
			dcBuilder.addIsNull("aantalJarenOnderwijs", filter
				.getVooropleidingAantalJarenOnderwijsIsLeeg());
			dcBuilder.addEquals("diplomaBehaald", filter.getVooropleidingDiplomaBehaald());
			dcBuilder.addILikeCheckWildcard("naam", filter
				.getNaamOnderwijsinstellingVooropleiding(), MatchMode.ANYWHERE);
			dcBuilder.addEquals("schooladvies", filter.getVooropleidingSchooladvies());
			dcBuilder.addEquals("citoscore", filter.getVooropleidingCitoscore());

			dc.setProjection(Projections.property("deelnemer"));
			builder.propertyIn(deelnemerAlias + ".id", dc);
			filter.setResultCacheable(false);
		}

		List<Criterion> mentorDocent =
			DataAccessRegistry.getHelper(GroepDataAccessHelper.class).createMentorDocent(filter,
				deelnemerAlias);
		List<Criterion> verantwoordelijkeUitvoerende =
			DataAccessRegistry.getHelper(DBSMedewerkerDataAccessHelper.class)
				.createVerantwoordelijkeUitvoerende(filter, deelnemerAlias);
		addMentorDocentVerantwoordelijkeUitvoerende(builder, mentorDocent,
			verantwoordelijkeUitvoerende);

		if (filter.getVrijVelden().size() > 0)
		{
			DetachedCriteria dc =
				DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).buildCriteria(
					filter.getVrijVelden(), filter.getGearchiveerd(), PersoonVrijVeld.class,
					"persoon");
			if (dc != null)
			{
				builder.propertyIn(persoonAlias + ".id", dc);
			}
		}

		if (filter.getSnelZoekenString() != null)
		{
			if (filter.getSnelZoekenString().contains(";"))
			{
				String[] subStrings = filter.getSnelZoekenString().split(";");
				for (String snelZoekenSubstring : subStrings)
				{
					List<Criterion> ors =
						getSnelZoekCriteria(snelZoekenSubstring, filter.isExactSnelZoeken(),
							deelnemerAlias, persoonAlias);
					builder.addOrs(ors);
				}

			}
			else
			{
				builder.addOrs(getSnelZoekCriteria(filter.getSnelZoekenString(), filter
					.isExactSnelZoeken(), deelnemerAlias, persoonAlias));
			}
		}
		if (filter.getContactgegeven() != null || filter.getTypeContactgegeven() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(PersoonContactgegeven.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addILikeCheckWildcard("contactgegeven", filter.getContactgegeven(),
				MatchMode.START);
			dcBuilder.createAlias("soortContactgegeven", "soortContactgegeven",
				JoinFragment.LEFT_OUTER_JOIN);
			dcBuilder.addEquals("soortContactgegeven.typeContactgegeven", filter
				.getTypeContactgegeven());
			dc.setProjection(Projections.property("persoon"));
			builder.propertyIn(persoonAlias + ".id", dc);
			filter.setResultCacheable(false);
		}
		if (filter.heeftBijalgeCriteria())
		{
			DetachedCriteria dc = createDetachedCriteria(DeelnemerBijlage.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.createAlias("bijlage", "bijlage");
			dcBuilder.createAlias("bijlage.documentType", "documentType");
			dcBuilder.addIn("bijlage.documentType", filter.getDocumentTypeList());
			dcBuilder.addIn("documentType.categorie", filter.getDocumentCategorieList());
			dc.setProjection(Projections.property("deelnemer"));
			builder.propertyIn(deelnemerAlias + ".id", dc);
			filter.setResultCacheable(false);
		}
		/* Alluris */
		if (filter.getOcwnummer() != null && !filter.getOcwnummer().equals(""))
		{
			builder.addEquals(deelnemerAlias + ".ocwnummer", filter.getOcwnummer());
		}
		builder.addEquals(persoonAlias + ".eersteVoorletter", filter.getEersteVoorletter());

	}

	@Override
	public void addMentorDocentVerantwoordelijkeUitvoerende(CriteriaBuilder builder,
			List<Criterion> mentorDocent, List<Criterion> verantwoordelijkeUitvoerende)
	{
		if (mentorDocent.isEmpty() && verantwoordelijkeUitvoerende.isEmpty())
			return;
		if (mentorDocent.isEmpty())
			builder.getCriteria().add(createAnd(verantwoordelijkeUitvoerende));
		if (verantwoordelijkeUitvoerende.isEmpty())
			builder.getCriteria().add(createAnd(mentorDocent));
		builder.addOrs(createAnd(mentorDocent), createAnd(verantwoordelijkeUitvoerende));
	}

	private Criterion createAnd(List<Criterion> crits)
	{
		Conjunction ands = Restrictions.conjunction();
		for (Criterion curCrit : crits)
		{
			ands.add(curCrit);
		}
		return ands;
	}

	private List<Criterion> getSnelZoekCriteria(String snelZoekenString, boolean exactSnelZoeken,
			String deelnemerAlias, String persoonAlias)
	{
		List<Criterion> whereList = new ArrayList<Criterion>();

		if (StringUtil.isNumeric(snelZoekenString))
		{
			// is het een geldige long?
			try
			{
				Long longQuery = new Long(snelZoekenString);
				whereList.add(Restrictions.eq(persoonAlias + ".bsn", longQuery));
				whereList.add(Restrictions.eq(deelnemerAlias + ".onderwijsnummer", longQuery));

				// is het een geldige int?
				try
				{
					Integer integerQuery = new Integer(snelZoekenString);
					whereList.add(Restrictions
						.eq(deelnemerAlias + ".deelnemernummer", integerQuery));
				}
				catch (NumberFormatException e)
				{
					// geen geldige int
				}
			}
			catch (NumberFormatException e)
			{
				// geen geldige long
			}
		}
		else
		{
			if (exactSnelZoeken)
			{
				whereList.add(Restrictions.eq(persoonAlias + ".berekendeZoeknaam", snelZoekenString
					.toLowerCase()));
				whereList.add(Restrictions.eq(persoonAlias + ".lowercaseAchternaam",
					snelZoekenString.toLowerCase()));
			}
			else
			{
				whereList.add(Restrictions.like(persoonAlias + ".lowercaseAchternaam",
					snelZoekenString.toLowerCase(), MatchMode.ANYWHERE));
				whereList.add(Restrictions.ilike(persoonAlias + ".officieleAchternaam",
					snelZoekenString, MatchMode.ANYWHERE));
				whereList.add(Restrictions.ilike(persoonAlias + ".roepnaam", snelZoekenString,
					MatchMode.ANYWHERE));
				whereList.add(Restrictions.ilike(persoonAlias + ".berekendeZoeknaam",
					snelZoekenString, MatchMode.ANYWHERE));
			}
		}
		Date geboorteDatum = TimeUtil.getInstance().parseDateString(snelZoekenString);
		if (geboorteDatum != null)
			whereList.add(Restrictions.eq(persoonAlias + ".geboortedatum", geboorteDatum));
		return whereList;
	}

	@Override
	public Deelnemer get(Long id)
	{
		return get(Deelnemer.class, id);
	}

	@Override
	public Deelnemer getById(Long id)
	{
		Criteria criteria = createCriteria(Deelnemer.class);
		criteria.add(Restrictions.eq("id", id));
		return cachedUnique(criteria);
	}

	@Override
	public List<Long> getIds(VerbintenisZoekFilter filter)
	{
		Criteria criteria = createCriteria(Verbintenis.class);
		criteria.createAlias("deelnemer", "deelnemer");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemer.organisatie", EduArteContext.get().getInstelling());
		builder.addLessOrEquals("begindatum", filter.getPeilEindDatum());
		builder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
		filter.addOrganisatieEenheidLocatieCriteria(criteria);
		builder.addEquals("opleiding", filter.getOpleiding());
		criteria.setProjection(Projections.distinct(Projections.property("deelnemer.id")));
		return cachedList(criteria);
	}

	@Override
	public Deelnemer getByDeelnemernummer(Integer deelnemernummer)
	{
		Criteria criteria = createCriteria(Deelnemer.class);
		criteria.add(Restrictions.eq("deelnemernummer", deelnemernummer));
		return cachedTypedUnique(criteria);
	}

	@Override
	public Deelnemer getByStudielinknummer(Integer studielinknummer)
	{
		Criteria criteria = createCriteria(Deelnemer.class);
		criteria.add(Restrictions.eq("studielinknummer", studielinknummer));
		return cachedTypedUnique(criteria);
	}

	@Override
	public List<Deelnemer> getByBSN(Long bsn)
	{
		Criteria criteria = createCriteria(Deelnemer.class, "deelnemer");
		criteria.createAlias("deelnemer.persoon", "persoon");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("persoon.organisatie", EduArteContext.get().getInstelling());
		criteria.add(Restrictions.eq("persoon.bsn", bsn));
		return cachedTypedList(criteria);
	}

	@Override
	public List<Deelnemer> getByOnderwijsnummer(Long onderwijsnummer)
	{
		Criteria criteria = createCriteria(Deelnemer.class, "deelnemer");
		criteria.add(Restrictions.eq("onderwijsnummer", onderwijsnummer));
		return cachedTypedList(criteria);
	}

	@Override
	public Deelnemer getByPersoon(Persoon persoon)
	{
		Asserts.assertNotNull("persoon", persoon);
		Criteria criteria = createCriteria(Deelnemer.class);
		criteria.add(Restrictions.eq("persoon", persoon));
		return cachedTypedUnique(criteria);
	}

	@Override
	public List<Deelnemer> getLeerplichtige(VerbintenisZoekFilter filter)
	{
		Criteria criteria = createCriteria(Deelnemer.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		criteria.createAlias("persoon", "persoon");
		builder.addGreaterOrEquals("persoon.geboortedatum", filter.getGeboortedatumVanaf());
		builder.addLessOrEquals("persoon.geboortedatum", filter.getGeboortedatumTotEnMet());

		filter.setResultCacheable(false);
		// filteren op organisatieeenheid.
		DetachedCriteria dc = createDetachedCriteria(Verbintenis.class);
		DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
		dc.setProjection(Projections.property("deelnemer"));
		if (!filter.addOrganisatieEenheidLocatieCriteria(dc))
			return null;

		dcBuilder.addLessOrEquals("begindatum", filter.getPeilEindDatum());
		dcBuilder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
		criteria.add(Subqueries.propertyIn("id", dc));

		return cachedTypedList(criteria);
	}

	@Override
	public Deelnemer getDeelnemerByIdInOudpakket(Long id)
	{
		Criteria criteria = createCriteria(Deelnemer.class);
		criteria.add(Restrictions.eq("idInOudPakket", id));
		return cachedUnique(criteria);
	}

	@Override
	public List<Deelnemer> getDeelnemersDieGisterenInactiefGewordenZijn()
	{
		DetachedCriteria heeftActieveVerbintenissen =
			createActieveVerbintenissenCriteria(vandaag());

		DetachedCriteria gisterenBeeindigd = createBeeindigdeVerbintenissenCriteria(gisteren());

		// dit kan mogelijk gebruik maken van een nog sneller query pad door de
		// gisterenBeeindigd query op te splitsen in een OR van twee exists queries: de
		// verbintenissen met een einddatum gelijk aan gisteren, en de verbintenissen die
		// in het verleden beeindigd zijn met een last_modified_at datum gelijk aan
		// gisteren.

		// Daar het de bedoeling is dat deze query ongeveer één keer per nacht draait,
		// lijkt een wat langer query pad geen problemen te veroorzaken (er zit geen
		// gebruiker op te wachten).

		Criteria criteria = createCriteria(Deelnemer.class, "d");
		criteria.add(Subqueries.notExists(heeftActieveVerbintenissen));
		criteria.add(Subqueries.exists(gisterenBeeindigd));

		// cachen is onwenselijk, aangezien die
		return uncachedList(criteria);
	}

	/**
	 * Creeert een {@link DetachedCriteria} die de actieve verbintenissen op de peildatum
	 * bepaalt.
	 */
	private DetachedCriteria createActieveVerbintenissenCriteria(Date peildatum)
	{
		DetachedCriteria heeftActieveVerbintenissen =
			createDetachedCriteria(Verbintenis.class, "verbintenis");

		DetachedCriteriaBuilder heeftActieveVerbintenissenBuilder =
			new DetachedCriteriaBuilder(heeftActieveVerbintenissen);
		heeftActieveVerbintenissen.setProjection(Projections.property("deelnemer"));

		heeftActieveVerbintenissen.add(eqProperty("verbintenis.deelnemer", "d.id"));
		heeftActieveVerbintenissenBuilder.addLessOrEquals("begindatum", peildatum);
		heeftActieveVerbintenissenBuilder.addGreaterOrEquals("einddatumNotNull", peildatum);
		return heeftActieveVerbintenissen;
	}

	/**
	 * Creeert een {@link DetachedCriteria} die de verbintenissen bepaalt die op de
	 * peildatum beeindigd zijn door ofwel een einddatum te hebben die gelijk is aan
	 * peildatum, ofwel een peildatum in het verleden hebben *en* gisteren gewijzigd zijn.
	 * Dit levert natuurlijk wel false-positives op, maar dat zou niet uit maken.
	 */
	private DetachedCriteria createBeeindigdeVerbintenissenCriteria(Date peildatum)
	{
		DetachedCriteria gisterenBeeindigd =
			createDetachedCriteria(Verbintenis.class, "verbintenis");
		DetachedCriteriaBuilder gisterenBeeindigdBuilder =
			new DetachedCriteriaBuilder(gisterenBeeindigd);
		gisterenBeeindigd.setProjection(Projections.property("deelnemer"));

		gisterenBeeindigd.add(eqProperty("verbintenis.deelnemer", "d.id"));

		// *of* door de gebruiker gisteren in het verleden beeindigd.
		gisterenBeeindigdBuilder.addOrs(eq("einddatumNotNull", peildatum), and(eq("lastModifiedAt",
			peildatum), lt("einddatumNotNull", peildatum)));
		return gisterenBeeindigd;
	}

	@Override
	public List<Long> getDeelnemerIdsMetResultaten()
	{
		Criteria criteria = createCriteria(Resultaat.class);
		criteria.setProjection(Projections.distinct(Projections.property("deelnemer.id")));
		return uncachedList(criteria);
	}

}
