package nl.topicus.eduarte.dao.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.hibernate.criterion.AnyTypeLike;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VrijVeldDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.examen.ExamenWorkflowTaxonomie;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.ToegestaneBeginstatus;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Aanmelding;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.VerbintenisContract;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.opleiding.OpleidingAanbod;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.AbstractRelatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat.Resultaatsoort;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieEnum;
import nl.topicus.eduarte.entities.taxonomie.mbo.AbstractMBOVerbintenisgebied;
import nl.topicus.eduarte.entities.vrijevelden.*;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekFilter.TypeAdres;
import nl.topicus.onderwijs.duo.bron.bve.foto.pve_9_9.BronFotoType;

import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinFragment;

public class VerbintenisHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Verbintenis, VerbintenisZoekFilter> implements
		VerbintenisDataAccessHelper
{
	public VerbintenisHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(VerbintenisZoekFilter filter)
	{
		Criteria criteria = createCriteria(Verbintenis.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("organisatieEenheid", "organisatieEenheid");
		builder.createAlias("locatie", "locatie", JoinFragment.LEFT_OUTER_JOIN);

		// Deze joins worden niet toegevoegd als ze niet nodig zijn: een count query voor
		// de standard deelnemer zoeken pagina waarbij bepaalde velden niet ingevuld zijn.
		// Dit lijkt een specifiek geval, maar dit is de default als je op die pagina
		// komt.
		boolean queryRequiresPersoon =
			!filter.isStandaardDeelnemerZoeken() || !filter.isCountQuery()
				|| !StringUtil.isEmpty(filter.getOfficieelofaanspreek());
		boolean queryRequiresDeelnemer = filter.getGroep() != null;
		if (queryRequiresPersoon || queryRequiresDeelnemer)
		{
			builder.createAlias("deelnemer", "deelnemer");
			builder.addEquals("deelnemer.organisatie", EduArteContext.get().getInstelling());
			builder.addEquals("deelnemer.gearchiveerd", filter.getGearchiveerd());

			if (queryRequiresPersoon)
			{
				builder.createAlias("deelnemer.persoon", "persoon");
				builder.addEquals("persoon.organisatie", EduArteContext.get().getInstelling());
				builder.addEquals("persoon.gearchiveerd", filter.getGearchiveerd());
			}
		}

		builder.addEquals("gearchiveerd", filter.getGearchiveerd());
		builder.addEquals("organisatieEenheid.organisatie", EduArteContext.get().getInstelling());

		if (!filter.addOrganisatieEenheidLocatieCriteria(criteria))
			return null;
		// Voeg ook multi-select criteria toe.
		if (filter.getOrganisatieEenheidList() != null)
		{
			List<OrganisatieEenheid> oeList = new ArrayList<OrganisatieEenheid>();
			for (OrganisatieEenheid organisatieEenheid : filter.getOrganisatieEenheidList())
			{
				oeList.addAll(organisatieEenheid.getActieveChildren(filter.getPeildatum()));
			}
			builder.addIn("organisatieEenheid", oeList);
		}
		builder.addIn("locatie", filter.getLocatieList());

		if (filter.isOpleidingEnCohortVerplicht()
			&& (filter.getCohort() == null || filter.getOpleiding() == null))
			return null;

		if (filter.isVerbergStatusAangemeld())
			builder.addNotEquals("status", VerbintenisStatus.Aangemeld);

		builder.addLessOrEquals("begindatum", filter.getActiefTotEnMet());
		builder.addGreaterOrEquals("einddatumNotNull", filter.getActiefVanaf());
		builder.addLessOrEquals("begindatum", filter.getPeilEindDatum());
		builder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
		builder.addGreaterOrEquals("begindatum", filter.getDatumInschrijvingVanaf());
		builder.addLessOrEquals("begindatum", filter.getDatumInschrijvingTotEnMet());
		builder.addGreaterOrEquals("einddatum", filter.getDatumUitschrijvingVanaf());
		builder.addLessOrEquals("einddatum", filter.getDatumUitschrijvingTotEnMet());
		builder.addIsNull("einddatum", filter.getEinddatumIsNull());
		builder.addGreaterOrEquals("geplandeEinddatum", filter.getDatumGeplandEindeVanaf());
		builder.addLessOrEquals("geplandeEinddatum", filter.getDatumGeplandEindeTotEnMet());
		builder.addEquals("redenUitschrijving", filter.getRedenUitschrijving());
		builder.addIn("redenUitschrijving", filter.getRedenUitschrijvingList());
		builder.addEquals("bekostigd", filter.getBekostigd());
		builder.addEquals("cohort", filter.getCohort());
		builder.addEquals("contacturenPerWeek", filter.getContacturenPerWeek());
		builder.addEquals("vertrekstatus", filter.getVertrekstatus());
		builder.addEquals("status", filter.getVerbintenisStatus());
		builder.addEquals("intensiteit", filter.getIntensiteit());
		builder.addIn("intensiteit", filter.getIntensiteitList());
		builder.addEquals("indicatieGehandicapt", filter.getIndicatieGehandicapt());

		builder.addEquals("redenInburgering", filter.getRedenInburgering());
		builder.addEquals("profielInburgering", filter.getProfielInburgering());
		builder.addIn("profielInburgering", filter.getProfielInburgeringList());
		builder.addEquals("leerprofiel", filter.getLeerprofiel());
		builder.addIn("leerprofiel", filter.getLeerprofielList());
		builder.addEquals("deelcursus", filter.getDeelcursus());
		builder.addEquals("soortPraktijkexamen", filter.getSoortPraktijkexamen());
		builder.addIn("soortPraktijkexamen", filter.getSoortPraktijkexamenList());
		builder.addGreaterOrEquals("datumAanmelden", filter.getDatumAanmeldenVanaf());
		builder.addLessOrEquals("datumAanmelden", filter.getDatumAanmeldenTotEnMet());
		builder.addGreaterOrEquals("datumAkkoord", filter.getDatumAkkoordVanaf());
		builder.addLessOrEquals("datumAkkoord", filter.getDatumAkkoordTotEnMet());
		builder.addGreaterOrEquals("datumEersteActiviteit", filter.getDatumEersteActiviteitVanaf());
		builder.addLessOrEquals("datumEersteActiviteit", filter.getDatumEersteActiviteitTotEnMet());
		builder.addEquals("beginNiveauSchriftelijkeVaardigheden", filter
			.getBeginNiveauSchriftelijkeVaardigheden());
		builder.addIn("beginNiveauSchriftelijkeVaardigheden", filter
			.getBeginNiveauSchriftelijkeVaardighedenList());
		builder.addEquals("eindNiveauSchriftelijkeVaardigheden", filter
			.getEindNiveauSchriftelijkeVaardigheden());
		builder.addIn("eindNiveauSchriftelijkeVaardigheden", filter
			.getEindNiveauSchriftelijkeVaardighedenList());
		builder.addEquals("deelnemer.uitsluitenVanFacturatie", filter.getUitsluitenVanFacturatie());
		if (filter.getRelevanteVooropleidingSoortLeeg() != null
			|| filter.getRelevanteVooropleidingSoort() != null)
		{
			criteria.createAlias("relevanteVooropleidingVooropleiding", "relVooropleiding");
			builder.addEquals("relVooropleiding.soortVooropleiding", filter
				.getRelevanteVooropleidingSoort());
			builder.addIsNull("relVooropleiding.soortVooropleiding", filter
				.getRelevanteVooropleidingSoortLeeg());
		}

		builder.addEquals("relevanteVerbintenis", filter.getRelevanteVerbintenis());

		builder.addIn("bronStatus", filter.getBronStatusList());

		if (filter.heeftExamendeelnameCriteria())
		{
			DetachedCriteria dcStatus = createDetachedCriteria(Examendeelname.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dcStatus);
			dcBuilder.createAlias("examenstatus", "examenstatus");
			dcBuilder.addEquals("examenstatus.examenWorkflow", filter.getExamenWorkflow());
			dcBuilder.addEquals("examenstatus", filter.getExamenstatus());
			dcBuilder.addIn("examenstatus", filter.getExamenstatusList());
			dcBuilder.addGreaterOrEquals("datumUitslag", filter.getDatumUitslagVanaf());
			dcBuilder.addLessOrEquals("datumUitslag", filter.getDatumUitslagTotEnMet());
			dcBuilder.addIsNull("datumUitslag", filter.getDatumUitslagIsLeeg());
			dcBuilder.addEquals("examenjaar", filter.getExamenjaar());
			dcBuilder.addGreaterOrEquals("examennummer", filter.getExamennummerVanaf());
			dcBuilder.addLessOrEquals("examennummer", filter.getExamennummerTotEnMet());
			dcBuilder.addEquals("bekostigd", filter.getExamendeelnameBekostigd());
			dcBuilder.addGreaterOrEquals("tijdvak", filter.getExamendeelnameTijdvakVanaf());
			dcBuilder.addLessOrEquals("tijdvak", filter.getExamendeelnameTijdvakTotEnMet());
			dcBuilder.addIsNull("tijdvak", filter.getExamendeelnameTijdvakIsLeeg());
			dcBuilder.addEquals("tijdvak", filter.getExamendeelnameTijdvakGelijkAan());
			dcStatus.setProjection(Projections.property("verbintenis"));
			criteria.add(Subqueries.propertyIn("id", dcStatus));
		}
		if (filter.heeftBPVCriteria())
		{
			DetachedCriteria dcBpvInschrijving = createDetachedCriteria(BPVInschrijving.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dcBpvInschrijving);
			dcBuilder.createAlias("bedrijfsgegeven", "bedrijfsgegeven");
			dcBuilder.addEquals("status", filter.getBpvStatus());
			dcBuilder.addIn("status", filter.getBpvStatusList());
			dcBuilder.addEquals("bronStatus", filter.getBpvBronStatus());
			dcBuilder.addEquals("redenUitschrijving", filter.getBpvRedenBeeindiging());
			dcBuilder.addNullOrEquals("bedrijfsgegeven.organisatie", EduArteContext.get()
				.getInstelling());
			dcBuilder.addEquals("bedrijfsgegeven.kenniscentrum", filter.getBpvKenniscentrum());
			dcBuilder.addEquals("bpvBedrijf", filter.getBpvBedrijf());
			dcBuilder.addIn("bpvBedrijf", filter.getBpvBedrijfList());
			dcBuilder.addEquals("contractpartner", filter.getBpvContractpartner());
			dcBuilder.addEquals("praktijkbegeleider", filter.getPraktijkopleiderBPVBedrijf());
			dcBuilder.addIsNull("praktijkbegeleider", filter.getPraktijkopleiderBPVBedrijfLeeg());
			dcBuilder.addEquals("praktijkbegeleider", filter.getPraktijkBegeleider());
			dcBuilder.addIsNull("praktijkbegeleider", filter.getPraktijkBegeleiderLeeg());
			dcBuilder.addIsNull("bedrijfsgegeven.kenniscentrum", filter.getBpvKenniscentrumLeeg());
			dcBuilder.addGreaterOrEquals("einddatum", filter.getBpvBeginDatum());
			dcBuilder.addLessOrEquals("begindatum", filter.getBpvEindDatum());
			dcBuilder.addGreaterOrEquals("totaleOmvang", filter.getBpvOmvangVanaf());
			dcBuilder.addLessOrEquals("totaleOmvang", filter.getBpvOmvangTotEnMet());
			dcBuilder.addGreaterOrEquals("urenPerWeek", filter.getBpvUrenPerWeekVanaf());
			dcBuilder.addLessOrEquals("urenPerWeek", filter.getBpvUrenPerWeekTotEnMet());
			dcBuilder.addGreaterOrEquals("dagenPerWeek", filter.getBpvDagenPerWeekVanaf());
			dcBuilder.addLessOrEquals("dagenPerWeek", filter.getBpvDagenPerWeekTotEnMet());
			dcBuilder.addNotIn("status", filter.getBpvStatusOngelijkAan());
			dcBpvInschrijving.setProjection(Projections.property("verbintenis"));
			criteria.add(Subqueries.propertyIn("id", dcBpvInschrijving));
		}
		if (Boolean.TRUE.equals(filter.getStatusIntake()))
		{
			builder.addEquals("status", VerbintenisStatus.Intake);
		}
		builder.addIn("status", filter.getVerbintenisStatusList());
		// als Beeindigd in de statuslijst voorkomt, wordt bedoeld: beeindigd in
		// peilperiode
		if (filter.getVerbintenisStatusList() != null
			&& filter.getVerbintenisStatusList().contains(VerbintenisStatus.Beeindigd)
			&& filter.getPeildatum() != null)
		{
			Criterion nietBeeindigd = Restrictions.ne("status", VerbintenisStatus.Beeindigd);
			LogicalExpression einddatumInPeilperiode =
				Restrictions.and(Restrictions.ge("einddatumNotNull", filter.getPeildatum()),
					Restrictions.le("einddatumNotNull", filter.getPeilEindDatum()));
			builder.addOrs(nietBeeindigd, einddatumInPeilperiode);
		}
		builder.addNotIn("status", filter.getVerbintenisStatusOngelijkAan());

		if (filter.heeftOpleidingCriteria() || filter.getToegestaneExamenstatusOvergang() != null)
		{
			builder.createAlias("opleiding", "opleiding", JoinFragment.LEFT_OUTER_JOIN);
			builder.createAlias("opleiding.verbintenisgebied", "verbintenisgebied",
				JoinFragment.LEFT_OUTER_JOIN);
			builder.addEquals("verbintenisgebied.taxonomie", filter.getTaxonomie());
			builder.addIn("verbintenisgebied.taxonomie", filter.getTaxonomieList());
			builder.addEquals("verbintenisgebied.taxonomieElementType", filter
				.getTaxonomieElementType());
			builder.addILikeCheckWildcard("verbintenisgebied.taxonomiecode", filter
				.getTaxonomiecode(), MatchMode.START);
			// if (filter.getOpleiding() != null || filter.getOpleidingList() != null)
			// builder.createAlias("opleiding.parent", "parent",
			// JoinFragment.LEFT_OUTER_JOIN);
			// {
			if (filter.getOpleiding() != null)
			{
				builder.addOrs(Restrictions.eq("opleiding", filter.getOpleiding()), Restrictions
					.eq("opleiding.parent", filter.getOpleiding()));
			}
			if (filter.getOpleidingList() != null)
			{
				builder.addOrs(builder.getIn("opleiding", filter.getOpleidingList()), builder
					.getIn("opleiding.parent", filter.getOpleidingList()));
			}
			// }
			builder.addNullOrNotEquals("opleiding", filter.getOpleidingOngelijkAan());
			builder.addNullOrNotEquals("opleiding.parent", filter.getOpleidingOngelijkAan());
			builder.addEquals("opleiding.verbintenisgebied", filter.getVerbintenisgebied());
			builder.addIn("opleiding.verbintenisgebied", filter.getVerbintenisgebiedList());
			builder.addNullOrNotEquals("opleiding.verbintenisgebied", filter
				.getVerbintenisgebiedOngelijkAan());
			builder.addEquals("verbintenisgebied.externeCode", filter.getOpleidingsCode());
			builder.addEquals("opleiding.leerweg", filter.getLeerweg());

			if (filter.getNiveau() != null)
			{
				DetachedCriteria dcMBOVerbintenisGebied =
					createDetachedCriteria(AbstractMBOVerbintenisgebied.class);
				DetachedCriteriaBuilder dcBuilder =
					new DetachedCriteriaBuilder(dcMBOVerbintenisGebied);
				dcBuilder.addEquals("niveau", filter.getNiveau());
				dcMBOVerbintenisGebied.setProjection(Projections.property("id"));
				criteria.add(Subqueries.propertyIn("opleiding.verbintenisgebied",
					dcMBOVerbintenisGebied));
			}
			if (filter.getTeamList() != null && !filter.getTeamList().isEmpty())
			{
				Criteria aanbodCrit = createCriteria(OpleidingAanbod.class);
				CriteriaBuilder cb = new CriteriaBuilder(aanbodCrit);
				cb.addIn("team", filter.getTeamList());

				List<OpleidingAanbod> list = cachedList(aanbodCrit);
				if (list.isEmpty())
					return null;
				Disjunction dis = Restrictions.disjunction();
				for (OpleidingAanbod opleidingAanbod : list)
				{
					Conjunction con = Restrictions.conjunction();
					con.add(Restrictions.eq("organisatieEenheid", opleidingAanbod
						.getOrganisatieEenheid()));
					con.add(Restrictions.eq("locatie", opleidingAanbod.getLocatie()));
					con.add(Restrictions.eq("opleiding", opleidingAanbod.getOpleiding()));
					dis.add(con);
				}
				criteria.add(dis);
			}
		}

		if (filter.heeftPlaatsingCriteria())
		{
			DetachedCriteria dc = createDetachedCriteria(Plaatsing.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("gearchiveerd", filter.getGearchiveerd());
			dcBuilder.addNullOrLessOrEquals("begindatum", filter.getPeilEindDatum());
			dcBuilder.addNullOrGreaterOrEquals("einddatum", filter.getPeildatum());
			dcBuilder.addEquals("groep", filter.getBasisgroep());
			dcBuilder.addEquals("leerjaar", filter.getLeerjaar());
			dcBuilder.addEquals("jarenPraktijkonderwijs", filter.getPraktijkjaar());
			dcBuilder.addEquals("lwoo", filter.getIndicatieLWOO());
			dc.setProjection(Projections.property("verbintenis"));
			builder.propertyIn("id", dc);
			filter.setResultCacheable(false);
		}

		if (filter.heeftIntakegesprekCriteria())
		{
			DetachedCriteria dc = createDetachedCriteria(Intakegesprek.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("status", filter.getIntakegesprekStatus());
			dcBuilder.addGreaterOrEquals("datumTijd", filter.getDatumIntakegesprekVanaf());
			dcBuilder.addLessOrEquals("datumTijd", filter.getDatumIntakegesprekTotEnMet());
			dcBuilder.addEquals("intaker", filter.getIntaker());
			dcBuilder.addIn("intaker", filter.getIntakerList());
			dcBuilder.addEquals("gewensteOpleiding", filter.getGewensteOpleiding());
			dcBuilder.addIn("gewensteOpleiding", filter.getGewensteOpleidingList());
			dcBuilder.addEquals("gewensteLocatie", filter.getGewensteLocatie());
			dcBuilder.addIn("gewensteLocatie", filter.getGewensteLocatieList());
			dcBuilder.addGreaterOrEquals("gewensteBegindatum", filter.getGewensteBegindatumVanaf());
			dcBuilder.addLessOrEquals("gewensteBegindatum", filter.getGewensteBegindatumTotEnMet());
			dcBuilder.addGreaterOrEquals("gewensteEinddatum", filter.getGewensteEinddatumVanaf());
			dcBuilder.addLessOrEquals("gewensteEinddatum", filter.getGewensteEinddatumTotEnMet());

			dc.setProjection(Projections.property("verbintenis"));
			builder.propertyIn("id", dc);
			filter.setResultCacheable(false);
		}

		if (filter.heeftContractCriteria())
		{
			DetachedCriteria dc = createDetachedCriteria(VerbintenisContract.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			dcBuilder.createAlias("contract", "contract");
			dcBuilder.createAlias("contract.soortContract", "soortContract");
			dcBuilder.addEquals("gearchiveerd", filter.getGearchiveerd());
			dcBuilder.addEquals("contract.soortContract", filter.getSoortContract());
			dcBuilder.addIn("contract.soortContract", filter.getSoortContractList());
			dcBuilder.addEquals("contract.typeFinanciering", filter.getTypeFinanciering());
			dcBuilder.addEquals("contract", filter.getContract());
			dcBuilder.addIn("contractList", filter.getContractList());
			dcBuilder.addEquals("onderdeel", filter.getContractOnderdeel());
			dcBuilder.addIn("onderdeel", filter.getContractOnderdeelList());
			dcBuilder.addEquals("contract.externeOrganisatie", filter
				.getContractExterneOrganisatie());
			dcBuilder.addIn("contract.externeOrganisatie", filter
				.getContractExterneOrganisatieList());
			dcBuilder.addEquals("contract.contactPersoon", filter.getContractContactpersoon());
			dcBuilder.addEquals("contract.beheerder", filter.getContractBeheerder());
			dcBuilder.addGreaterOrEquals("contract.kostprijs", filter.getContractKostprijsVanaf());
			dcBuilder.addLessOrEquals("contract.kostprijs", filter.getContractKostprijsTotEnMet());
			dcBuilder.addEquals("soortContract.inburgering", filter.getContractInburgering());
			dcBuilder.addEquals("contract.onderaanneming", filter.getContractOnderaanneming());

			dc.setProjection(Projections.property("verbintenis"));
			builder.propertyIn("id", dc);
		}

		if (filter.heeftRelatieCriteria())
		{
			DetachedCriteria dc2 = null;
			DetachedCriteria dc3 = null;
			DetachedCriteria dc4 = null;

			dc2 = createDetachedCriteria(Relatie.class);
			DetachedCriteriaBuilder dcBuilder2 = new DetachedCriteriaBuilder(dc2);
			dcBuilder2.createAlias("relatie", "verzorger");

			dcBuilder2.addEquals("verzorger.bsn", filter.getRelatieBsn());
			dcBuilder2.addILikeCheckWildcard("verzorger.achternaam", filter.getRelatieAchternaam(),
				MatchMode.START);
			dcBuilder2.addEquals("verzorger.geslacht", filter.getRelatieGeslacht());
			dcBuilder2.addEquals("verzorger.debiteurennummer", filter.getRelatieDebiteurenNummer());
			dcBuilder2.addEquals("relatieSoort", filter.getRelatieSoort());

			if (filter.getRelatieGeboortelandOngelijkAanNL() != null)
			{
				if (filter.getRelatieGeboortelandOngelijkAanNL().booleanValue())
					dcBuilder2.addNullOrNotEquals("verzorger.geboorteland", Land.getNederland());
				else
					dcBuilder2.addEquals("verzorger.geboorteland", Land.getNederland());
			}
			if (filter.getRelatieNationaliteitOngelijkAanNL() != null)
			{
				if (filter.getRelatieNationaliteitOngelijkAanNL().booleanValue())
					dcBuilder2.addNullOrNotEquals("verzorger.nationaliteit1", Land.getNederland());
				else
					dcBuilder2.addEquals("verzorger.nationaliteit1", Land.getNederland());
			}
			dc2.setProjection(Projections.property("id"));

			DetachedCriteria dc = createDetachedCriteria(PersoonAdres.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.createAlias("adres", "adres");
			dcBuilder.addEquals("adres.organisatie", EduArteContext.get().getInstelling());
			dcBuilder.addLessOrEquals("begindatum", filter.getPeildatum());
			dcBuilder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
			if (filter.getRelatieTypeAdres() == TypeAdres.Postadres
				|| filter.getRelatieTypeAdres() == TypeAdres.Beide)
			{
				dcBuilder.addEquals("postadres", Boolean.TRUE);
			}
			if (filter.getRelatieTypeAdres() == TypeAdres.Woonadres
				|| filter.getRelatieTypeAdres() == TypeAdres.Beide)
			{
				dcBuilder.addEquals("fysiekadres", Boolean.TRUE);
			}
			dcBuilder.addILikeCheckWildcard("adres.postcode", filter.getRelatiePostcode(),
				MatchMode.START);
			dcBuilder.addEquals("adres.huisnummer", filter.getRelatieHuisnummer());
			dcBuilder.addEquals("adres.huisnummerToevoeging", filter
				.getRelatieHuisnummerToevoeging());
			dcBuilder.addILikeCheckWildcard("adres.straat", filter.getRelatieStraat(),
				MatchMode.START);
			dcBuilder.addILikeCheckWildcard("adres.plaats", filter.getRelatiePlaats(),
				MatchMode.START);
			dc.setProjection(Projections.property("persoon"));
			dc2.add(Subqueries.propertyIn("relatie", dc));

			dc3 = createDetachedCriteria(PersoonExterneOrganisatie.class);
			DetachedCriteriaBuilder dcBuilder3 = new DetachedCriteriaBuilder(dc3);
			dcBuilder3.addEquals("relatie", filter.getRelatieExterneOrganisatie());
			dcBuilder3.addEquals("relatieSoort", filter.getRelatieSoort());
			dc3.setProjection(Projections.property("id"));

			dc4 = createDetachedCriteria(AbstractRelatie.class);
			dc4.setProjection(Projections.property("deelnemer"));

			if (filter.getRelatieExterneOrganisatie() != null)
				dc4.add(Subqueries.propertyIn("id", dc3));
			else
				dc4.add(Restrictions.or(Subqueries.propertyIn("id", dc3), Subqueries.propertyIn(
					"id", dc2)));

			criteria.add(Subqueries.propertyIn("deelnemer.persoon", dc4));
		}

		if (filter.getRelatieZelfdeAdresAlsDeelnemer() != null)
		{
			// Zoek op wel/niet een gedeeld adres tussen deelnemer en relatie.
			String sql =
				"{alias}.deelnemer in (select deelnemer.id " + "from deelnemer "
					+ "inner join persoon on deelnemer.persoon=persoon.id "
					+ "inner join adresentiteit on adresentiteit.persoon = persoon.id "
					+ "inner join abstractrelatie on abstractrelatie.persoon=persoon.id "
					+ "inner join persoon ouder on abstractrelatie.verzorger=ouder.id "
					+ "inner join adresentiteit ouderadres on ouderadres.persoon=ouder.id "
					+ "where adresentiteit.adres=ouderadres.adres)";
			if (filter.getRelatieZelfdeAdresAlsDeelnemer().booleanValue())
			{
				criteria.add(Restrictions.sqlRestriction(sql));
			}
			else
			{
				criteria.add(Restrictions.sqlRestriction(sql.replace("{alias}.deelnemer in",
					"{alias}.deelnemer not in")));
			}
			filter.setResultCacheable(false);
		}
		if (filter.getAfgenomenOnderwijsproduct() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(OnderwijsproductAfname.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("onderwijsproduct", filter.getAfgenomenOnderwijsproduct());
			if (filter.getBeeindigdeOnderwijsproductAfname() != null)
			{
				if (!filter.getBeeindigdeOnderwijsproductAfname())
					dcBuilder.addGreaterThan("einddatumNotNull", filter.getPeilEindDatum());
				else
				{
					dcBuilder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
					dcBuilder.addLessOrEquals("einddatumNotNull", filter.getPeilEindDatum());
				}
			}

			dc.setProjection(Projections.property("deelnemer"));
			criteria.add(Subqueries.propertyIn("deelnemer", dc));
		}
		if (filter.getProductregel() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(OnderwijsproductAfnameContext.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("productregel", filter.getProductregel());
			dc.setProjection(Projections.property("verbintenis"));
			criteria.add(Subqueries.propertyIn("id", dc));
		}

		if (filter.getToegestaneExamenstatusOvergang() != null)
		{
			// De deelnemer moet wel een opleiding hebben die gekoppeld is aan het
			// verbintenisgebied. Dit verbintenisgebiede moet wel vallen onder de
			// taxonomie van de examenworkflow
			List<Taxonomie> taxonomieen = new ArrayList<Taxonomie>();
			for (ExamenWorkflowTaxonomie eWorkTax : filter.getToegestaneExamenstatusOvergang()
				.getExamenWorkflow().getExamenWorflowTaxonomieen())
			{
				taxonomieen.add(eWorkTax.getTaxonomie());
			}
			builder.addIn("verbintenisgebied.taxonomie", taxonomieen);

			// als het een beginstatus is mag de deelnemer geen actieve examendeelname
			// hebben dwz alleen examendeelnames met de examenstatus wat een eindstatus
			// is.
			if (filter.getToegestaneExamenstatusOvergang().getNaarExamenstatus().isBeginstatus())
			{
				// Verbintenis mag niet gekoppeld zijn aan een examendeelname die geen
				// eindstatus heeft
				DetachedCriteria dc = createDetachedCriteria(Examendeelname.class);
				DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

				dcBuilder.createAlias("examenstatus", "examenstatus");
				dcBuilder.addEquals("gearchiveerd", filter.getGearchiveerd());
				dcBuilder.addEquals("examenstatus.eindstatus", Boolean.FALSE);
				dc.setProjection(Projections.property("verbintenis"));
				criteria.add(Subqueries.propertyNotIn("id", dc));
			}
			// Als het geen beginstatus is moet de deelnemer een examendeelname hebben met
			// als examenstatus een van de toegestane beginstatussen
			else
			{
				// Eerst een detached criteria om alle toegestane examenstatussen op te
				// halen
				DetachedCriteria dc2 = createDetachedCriteria(ToegestaneBeginstatus.class);
				dc2.add(Restrictions.eq("toegestaneExamenstatusOvergang", filter
					.getToegestaneExamenstatusOvergang()));
				dc2.setProjection(Projections.property("examenstatus"));

				// Nog een detached criteria voor examendeelname waarin examendeelnames
				// worden
				// opgehaald die de hierboven opgheaalde examenstatussen hebben
				DetachedCriteria dc = createDetachedCriteria(Examendeelname.class);
				DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
				dcBuilder.addEquals("gearchiveerd", filter.getGearchiveerd());
				dc.add(Subqueries.propertyIn("examenstatus", dc2));
				dc.setProjection(Projections.property("verbintenis"));
				criteria.add(Subqueries.propertyIn("id", dc));
			}
			// De verbintenis moet een examendeelname in een lager tijdvak hebben.
			if (filter.getToegestaneExamenstatusOvergang().isTijdvakAangeven())
			{
				DetachedCriteria dc = createDetachedCriteria(Examendeelname.class);
				DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
				dcBuilder.addEquals("gearchiveerd", filter.getGearchiveerd());
				dc.add(Restrictions.lt("tijdvak", filter.getTijdvak()));
				dc.setProjection(Projections.property("verbintenis"));
				criteria.add(Subqueries.propertyIn("id", dc));
			}
			// Resultaat is niet cacheable als een subquery gebruikt wordt.
			filter.setResultCacheable(false);
		}

		if (filter.getVrijVelden().size() > 0)
		{
			DetachedCriteria dc =
				DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).buildCriteria(
					filter.getVrijVelden(), filter.getGearchiveerd(), VerbintenisVrijVeld.class,
					"verbintenis");
			if (dc != null)
			{
				criteria.add(Subqueries.propertyIn("id", dc));
				filter.setResultCacheable(false);
			}
		}

		if (filter.getContractZoekFilter().getVrijVelden().size() > 0)
		{
			DetachedCriteria dc =
				DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).buildCriteria(
					filter.getContractZoekFilter().getVrijVelden(), filter.getGearchiveerd(),
					ContractVrijVeld.class, "contract");
			if (dc != null)
			{
				builder.createAlias("contracten", "verbinteniscontract");
				builder.createAlias("verbinteniscontract.contract", "contract");

				criteria.add(Subqueries.propertyIn("contract.id", dc));
				filter.setResultCacheable(false);
			}
		}

		if (filter.getDeelnemerZoekFilter().getVrijVelden().size() > 0)
		{
			DetachedCriteria dc =
				DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).buildCriteria(
					filter.getDeelnemerZoekFilter().getVrijVelden(), filter.getGearchiveerd(),
					PersoonVrijVeld.class, "persoon");
			if (dc != null)
			{
				criteria.add(Subqueries.propertyIn("persoon.id", dc));
				filter.setResultCacheable(false);
			}
		}

		if (filter.getGroepZoekFilter().getVrijVelden().size() > 0)
		{
			DetachedCriteria dc =
				DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).buildCriteria(
					filter.getGroepZoekFilter().getVrijVelden(), filter.getGearchiveerd(),
					GroepVrijVeld.class, "groep");
			if (dc != null)
			{
				builder.createAlias("plaatsingen", "plaatsing");
				builder.createAlias("plaatsing.groep", "basisgroep");
				criteria.add(Subqueries.propertyIn("basisgroep.id", dc));
				filter.setResultCacheable(false);
			}
		}

		if (filter.getIntakegesprekZoekFilter().getVrijVelden().size() > 0)
		{
			DetachedCriteria dc =
				DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).buildCriteria(
					filter.getIntakegesprekZoekFilter().getVrijVelden(), filter.getGearchiveerd(),
					IntakegesprekVrijVeld.class, "intakegesprek");
			if (dc != null)
			{
				builder.createAlias("intakegesprekken", "intakegesprek");
				criteria.add(Subqueries.propertyIn("intakegesprek.id", dc));
				filter.setResultCacheable(false);
			}
		}

		if (filter.getOnderwijsproductZoekFilter().getVrijVelden().size() > 0)
		{
			DetachedCriteria dc =
				DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).buildCriteria(
					filter.getOnderwijsproductZoekFilter().getVrijVelden(),
					filter.getGearchiveerd(), OnderwijsproductVrijVeld.class, "onderwijsproduct");
			if (dc != null)
			{
				builder.createAlias("afnameContexten", "afnameContexten");
				builder.createAlias("afnameContexten.onderwijsproductAfname",
					"onderwijsproductAfname");
				builder.createAlias("onderwijsproductAfname.onderwijsproduct", "onderwijsproduct");
				criteria.add(Subqueries.propertyIn("onderwijsproduct.id", dc));
				filter.setResultCacheable(false);
			}
		}

		if (filter.getOpleidingZoekFilter().getVrijVelden().size() > 0)
		{
			DetachedCriteria dc =
				DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).buildCriteria(
					filter.getOpleidingZoekFilter().getVrijVelden(), filter.getGearchiveerd(),
					OpleidingVrijVeld.class, "opleiding");
			if (dc != null)
			{
				criteria.add(Subqueries.propertyIn("opleiding.id", dc));
				filter.setResultCacheable(false);
			}
		}

		if (filter.getPlaatsingZoekFilter().getVrijVelden().size() > 0)
		{
			DetachedCriteria dc =
				DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).buildCriteria(
					filter.getPlaatsingZoekFilter().getVrijVelden(), filter.getGearchiveerd(),
					PlaatsingVrijVeld.class, "plaatsing");
			if (dc != null)
			{
				builder.createAlias("plaatsingen", "plaatsing");
				criteria.add(Subqueries.propertyIn("plaatsing.id", dc));
				filter.setResultCacheable(false);
			}
		}

		if (filter.getRelatieZoekFilter().getVrijVelden().size() > 0)
		{
			DetachedCriteria dc =
				DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).buildCriteria(
					filter.getRelatieZoekFilter().getVrijVelden(), filter.getGearchiveerd(),
					PersoonVrijVeld.class, "persoon");
			if (dc != null)
			{
				builder.createAlias("persoon.relaties", "relatie");
				criteria.add(Subqueries.propertyIn("relatie.id", dc));
				filter.setResultCacheable(false);
			}
		}

		if (filter.getVooropleidingZoekFilter().getVrijVelden().size() > 0)
		{
			DetachedCriteria dc =
				DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).buildCriteria(
					filter.getRelatieZoekFilter().getVrijVelden(), filter.getGearchiveerd(),
					VooropleidingVrijVeld.class, "vooropleiding");
			if (dc != null)
			{
				builder.createAlias("deelnemer.vooropleidingen", "vooropleiding");
				criteria.add(Subqueries.propertyIn("vooropleiding.id", dc));
				filter.setResultCacheable(false);
			}
		}

		if (filter.getUitschrijvingZoekFilter().getVrijVelden().size() > 0)
		{
			DetachedCriteria dc =
				DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).buildCriteria(
					filter.getUitschrijvingZoekFilter().getVrijVelden(), filter.getGearchiveerd(),
					VerbintenisVrijVeld.class, "verbintenis");
			if (dc != null)
			{
				criteria.add(Subqueries.propertyIn("id", dc));
				filter.setResultCacheable(false);
			}
		}

		if (filter.getBpvInschrijvingZoekFilter().getVrijVelden().size() > 0)
		{
			DetachedCriteria dc =
				DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).buildCriteria(
					filter.getBpvInschrijvingZoekFilter().getVrijVelden(),
					filter.getGearchiveerd(), BPVInschrijvingVrijVeld.class, "bpvInschrijving");
			if (dc != null)
			{
				builder.createAlias("bpvInschrijvingen", "bpvInschrijving");
				criteria.add(Subqueries.propertyIn("bpvInschrijving.id", dc));
				filter.setResultCacheable(false);
			}
		}

		DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class).addCriteria(builder,
			filter.getDeelnemerZoekFilter(), "deelnemer", "persoon");

		if (filter.getQuickSearchQuery() != null)
			criteria.add(addQuickSearchCriteria(filter.getQuickSearchQuery(), true, filter
				.getGearchiveerd()));

		return criteria;
	}

	@Override
	protected void addAliassesForSelectionCriteria(Criteria criteria, List<String> orderBy)
	{
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("deelnemer", "deelnemer");
		builder.createAlias("deelnemer.persoon", "persoon");
		builder.createAlias("organisatieEenheid", "organisatieEenheid");
		builder.createAlias("locatie", "locatie", JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("opleiding", "opleiding", JoinFragment.LEFT_OUTER_JOIN);
	}

	// TODO - Nick Multizoek Controleren
	@Override
	public Conjunction addQuickSearchCriteria(String quickSearch, boolean checkOnderwijs,
			Boolean gearchiveerd)
	{
		String[] zoekwaardes = quickSearch.split(",");
		if (zoekwaardes.length == 1)
		{
			zoekwaardes = quickSearch.split(" ");
		}
		Conjunction zoekwaardesTotaal = Restrictions.conjunction();
		Disjunction zoekwaardesIteratie = Restrictions.disjunction();
		Date geboorteDatum = null;

		for (int i = 0; i < zoekwaardes.length; i++)
		{
			if (StringUtil.isEmpty(zoekwaardes[i]))
				continue;
			// Verwijder eventuele spaties aan het begin of eind van een zoekstring.
			zoekwaardes[i] = zoekwaardes[i].trim();
			if (StringUtil.isNumeric(zoekwaardes[i]))
			{
				zoekwaardesIteratie.add(new AnyTypeLike("deelnemer.deelnemernummer",
					zoekwaardes[i], MatchMode.START));
			}
			else
			{
				zoekwaardesIteratie.add(Restrictions.ilike("persoon.achternaam", zoekwaardes[i],
					MatchMode.ANYWHERE));
				zoekwaardesIteratie.add(Restrictions.ilike("persoon.officieleAchternaam",
					zoekwaardes[i], MatchMode.ANYWHERE));
				zoekwaardesIteratie.add(Restrictions.ilike("persoon.roepnaam", zoekwaardes[i],
					MatchMode.ANYWHERE));
				zoekwaardesIteratie.add(Restrictions.ilike("persoon.berekendeZoeknaam",
					zoekwaardes[i], MatchMode.ANYWHERE));

				DetachedCriteria dc = createDetachedCriteria(Groepsdeelname.class);
				DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

				dcBuilder.createAlias("groep", "groep");
				dc.setProjection(Projections.property("deelnemer"));
				zoekwaardesIteratie.add(Subqueries.propertyIn("deelnemer.id", dc));
				dcBuilder.addEquals("gearchiveerd", gearchiveerd);
				dcBuilder.addEquals("groep.organisatie", EduArteContext.get().getInstelling());
				dcBuilder.addILikeFixedMatchMode("groep.code", zoekwaardes[i], MatchMode.ANYWHERE);
				Date peilDatum = TimeUtil.getInstance().currentDate();
				dcBuilder.addNullOrLessOrEquals("begindatum", peilDatum);
				dcBuilder.addNullOrGreaterOrEquals("einddatum", peilDatum);

				if (checkOnderwijs)
					zoekwaardesIteratie.add(Restrictions.ilike("opleiding.naam", zoekwaardes[i],
						MatchMode.ANYWHERE));
			}
			// geboortedatum kan zowel numeriek als niet numeriek zijn (streepjes e.d)
			geboorteDatum = TimeUtil.getInstance().parseDateString(zoekwaardes[i]);
			if (geboorteDatum != null)
				zoekwaardesIteratie.add(Restrictions.eq("persoon.geboortedatum", geboorteDatum));

			zoekwaardesTotaal.add(zoekwaardesIteratie);
			zoekwaardesIteratie = Restrictions.disjunction();
		}
		return zoekwaardesTotaal;
	}

	@Override
	public List<Verbintenis> getVerbintenissenByDeelnemer(Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(Verbintenis.class);
		criteria.add(Restrictions.eq("deelnemer", deelnemer));
		return cachedList(criteria);
	}

	@Override
	public Verbintenis getVerbintenisById(Long id)
	{
		Criteria criteria = createCriteria(Verbintenis.class);
		criteria.add(Restrictions.eq("id", id));
		List<Verbintenis> verbintenissen = cachedTypedList(criteria);
		if (!verbintenissen.isEmpty())
			return verbintenissen.get(0);
		return null;
	}

	@Override
	public List<Long> getBRONFotoVerbintenissen(BronFotoType fototype, Date teldatum)
	{
		// BRON-foto's BO bevatten de volgende inschrijvingen:
		// 1: Alle inschrijvingen die actief zijn binnen het schooljaar van de teldatum in
		// het fotobestand.
		// 2: Alle inschrijvingen die beeindigd zijn in het schooljaar voorafgaand van het
		// schooljaar van de teldatum.
		// 3: Alle BO-diploma's die binnen het kalenderjaar van de teldatum vallen,
		// inclusief de inschrijvingen die hierbij horen (voor zover ze niet al bij de
		// overige 2 categorien horen).
		Schooljaar schooljaar = Schooljaar.valueOf(teldatum);
		Schooljaar vorigSchooljaar = schooljaar.getVorigSchooljaar();
		int jaar = TimeUtil.getInstance().getYear(teldatum);
		Date beginKalenderjaar = TimeUtil.getInstance().asDate(jaar, 0, 1);
		Date eindKalenderjaar = TimeUtil.getInstance().asDate(jaar, 11, 31);
		Criteria criteria = createCriteria(Verbintenis.class);
		criteria.setProjection(Projections.property("id"));
		criteria.createAlias("opleiding", "opleiding");
		criteria.createAlias("opleiding.verbintenisgebied", "verbintenisgebied");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (fototype == BronFotoType.BO)
		{
			Taxonomie cgo = Taxonomie.getLandelijkeTaxonomie(TaxonomieEnum.CGO.getCode());
			Taxonomie mbo = Taxonomie.getLandelijkeTaxonomie(TaxonomieEnum.MBO.getCode());
			criteria.add(Restrictions.or(Restrictions.eq("verbintenisgebied.taxonomie", cgo),
				Restrictions.eq("verbintenisgebied.taxonomie", mbo)));

			Criterion actiefInSchooljaar =
				Restrictions.and(Restrictions.le("begindatum", schooljaar.getEinddatum()),
					Restrictions.ge("einddatumNotNull", schooljaar.getBegindatum()));

			Criterion uitgeschrevenInVorigJaar =
				Restrictions.and(Restrictions.ge("einddatum", vorigSchooljaar.getBegindatum()),
					Restrictions.le("einddatum", vorigSchooljaar.getEinddatum()));

			DetachedCriteria dc = createDetachedCriteria(Examendeelname.class);
			dc.setProjection(Projections.property("verbintenis"));
			dc.add(Restrictions.and(Restrictions.ge("datumUitslag", beginKalenderjaar),
				Restrictions.le("datumUitslag", eindKalenderjaar)));
			dc.add(Restrictions.eq("examenstatus", cgo.getTaxonomieExamenWorkflows().get(0)
				.getExamenWorkflow().getGeslaagdStatus()));
			Criterion examenInKalenderjaar = Subqueries.propertyIn("id", dc);

			builder.addOrs(Arrays.asList(actiefInSchooljaar, uitgeschrevenInVorigJaar,
				examenInKalenderjaar));

			return uncachedList(criteria);
		}
		else if (fototype == BronFotoType.ED_VAVO)
		{
			Taxonomie ed = Taxonomie.getLandelijkeTaxonomie(TaxonomieEnum.Educatie.getCode());
			Taxonomie vavo = Taxonomie.getLandelijkeTaxonomie(TaxonomieEnum.VO.getCode());
			criteria.add(Restrictions.or(Restrictions.eq("verbintenisgebied.taxonomie", ed),
				Restrictions.and(Restrictions.eq("verbintenisgebied.taxonomie", vavo), Restrictions
					.ilike("verbintenisgebied.externeCode", "5%"))));

			Criterion actiefOpTeldatum =
				Restrictions.and(Restrictions.le("begindatum", teldatum), Restrictions.ge(
					"einddatumNotNull", teldatum));

			Date vorigeTeldatum = TimeUtil.getInstance().addYears(teldatum, -1);
			Criterion uitgeschrevenSindsVorigeTeldatum =
				Restrictions.and(Restrictions.ge("einddatum", vorigeTeldatum), Restrictions.lt(
					"einddatum", vorigeTeldatum));

			DetachedCriteria dc = createDetachedCriteria(Examendeelname.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			dcBuilder.createAlias("verbintenis", "verbintenis");
			dcBuilder.createAlias("verbintenis.afnameContexten", "context");
			dcBuilder.createAlias("context.onderwijsproductAfname", "afname");
			dcBuilder.createAlias("afname.onderwijsproduct", "onderwijsproduct");
			dcBuilder.createAlias("onderwijsproduct.resultaatstructuren", "resultaatstructuur");
			dcBuilder.createAlias("resultaatstructuur.eindresultaat", "eindresultaat");
			dcBuilder.createAlias("eindresultaat.resultaten", "resultaat");
			dc.add(Restrictions.eqProperty("resultaatstructuur.cohort", "afname.cohort"));
			dc.add(Restrictions.eqProperty("resultaat.deelnemer", "verbintenis.deelnemer"));
			dc.add(Restrictions.eq("resultaat.geldend", Boolean.TRUE));
			dc.add(Restrictions.ne("resultaat.soort", Resultaatsoort.Tijdelijk));
			dc.add(Restrictions.eq("examenjaar", jaar));
			dc.setProjection(Projections.property("verbintenis"));
			Criterion examenInKalenderjaar = Subqueries.propertyIn("id", dc);

			builder.addOrs(Arrays.asList(actiefOpTeldatum, uitgeschrevenSindsVorigeTeldatum,
				examenInKalenderjaar));

			return uncachedList(criteria);
		}

		return Collections.emptyList();
	}

	@Override
	public Long getNumberOfBronCommuniceerbareVerbintenissen(Vooropleiding vooropleiding)
	{
		Criteria criteria = createCriteria(Verbintenis.class);

		criteria.add(Restrictions.eq("relevanteVooropleiding", vooropleiding));
		criteria.setProjection(Projections.rowCount());
		Long count = cachedUnique(criteria);
		if (count == 0)
		{
			return 1L;
		}
		else
		{
			VerbintenisStatus[] arr =
				{VerbintenisStatus.Voorlopig, VerbintenisStatus.Intake, VerbintenisStatus.Afgemeld,
					VerbintenisStatus.Afgewezen};
			criteria.add(Restrictions.in("status", arr));
			criteria.setProjection(Projections.rowCount());
			return cachedUnique(criteria);
		}
	}

	@Override
	public int listCount(VerbintenisZoekFilter filter)
	{
		try
		{
			filter.setCountQuery(true);
			return super.listCount(filter);
		}
		finally
		{
			filter.setCountQuery(false);
		}
	}

	@Override
	public long getDeelnemerCount(VerbintenisZoekFilter zoekFilter)
	{
		Criteria criteria;
		try
		{
			zoekFilter.setCountQuery(true);
			criteria = createCriteria(zoekFilter);
		}
		finally
		{
			zoekFilter.setCountQuery(false);
		}
		if (criteria == null)
			return 0;
		criteria.setProjection(Projections.countDistinct("deelnemer"));
		return (Long) unique(criteria, zoekFilter.isResultCacheable());
	}

	@Override
	public DeelnemerVerbintenisCount getDeelnemerVerbintenisCount(VerbintenisZoekFilter zoekFilter)
	{
		Criteria criteria;
		try
		{
			zoekFilter.setCountQuery(true);
			criteria = createCriteria(zoekFilter);
		}
		finally
		{
			zoekFilter.setCountQuery(false);
		}
		if (criteria == null)
			return new DeelnemerVerbintenisCount(0, 0);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()).add(
			Projections.countDistinct("deelnemer")));
		Object[] ret = unique(criteria, zoekFilter.isResultCacheable());
		return new DeelnemerVerbintenisCount(((Number) ret[1]).intValue(), ((Number) ret[0])
			.intValue());
	}

	@Override
	public List<Long> getIds(VerbintenisZoekFilter zoekfilter)
	{
		Criteria criteria = createCriteria(zoekfilter);
		if (criteria == null)
			return new ArrayList<Long>();
		criteria.setProjection(Projections.property("id"));
		return list(criteria, zoekfilter.isResultCacheable());
	}

	@Override
	public List<Long> getDeelnemerIds(VerbintenisZoekFilter verbintenisZoekFilter)
	{
		Criteria criteria = createCriteria(verbintenisZoekFilter);
		if (criteria == null)
			return new ArrayList<Long>();
		criteria.setProjection(Projections.distinct(Projections.property("deelnemer.id")));
		return uncachedList(criteria);
	}

	@Override
	public Aanmelding getAanmelding(Verbintenis verbintenis)
	{
		if (!verbintenis.isSaved())
			return null;

		Criteria criteria = createCriteria(Aanmelding.class);
		criteria.add(Restrictions.eq("verbintenis", verbintenis));
		return cachedUnique(criteria);
	}

	@Override
	public Verbintenis getVerbintenisByIdInOudPakket(Long id)
	{
		Criteria criteria = createCriteria(Verbintenis.class);
		criteria.add(Restrictions.eq("idInOudPakket", id));
		criteria.add(Restrictions.eq("organisatie", EduArteContext.get().getInstelling()));
		List<Verbintenis> verbintenissen = cachedTypedList(criteria);
		if (!verbintenissen.isEmpty())
			return verbintenissen.get(0);
		return null;
	}
}
