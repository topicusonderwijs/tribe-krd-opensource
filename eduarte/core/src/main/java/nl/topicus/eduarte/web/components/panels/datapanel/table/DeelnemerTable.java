/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.datapanel.table;

import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.dao.helpers.DocumentCategorieDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SoortContactgegevenDataAccessHelper;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.adres.StandaardContactgegeven;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.web.components.datapanel.DocumentCategorieAanwezigColumn;
import nl.topicus.eduarte.web.components.datapanel.PostcodeWoonplaatsColumn;
import nl.topicus.eduarte.web.components.factory.BegeleidingModuleComponentFactory;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.ContactgegevenColumn;
import nl.topicus.eduarte.zoekfilters.DocumentCategorieZoekFilter;

/**
 * @author loite
 */
public class DeelnemerTable extends AbstractVrijVeldableTable<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	public static DeelnemerTable createContractDeelnemerTable()
	{
		return new DeelnemerTable(true);
	}

	public DeelnemerTable()
	{
		this(false);
	}

	private DeelnemerTable(boolean toonContract)
	{
		super(StringUtil.firstCharUppercase(EduArteApp.get().getDeelnemerTermMeervoud())
			+ "/Verbintenissen");
		createColumns(toonContract);
		createGroupProperties();
	}

	private void createColumns(boolean toonContract)
	{
		createDeelnemerColumns();
		createAdresColumns();
		createContactgegevenColumns();
		createRelatieColumns();
		createOpleidingColumns();
		createBPVColumns();
		createBPVAdresColumns();
		createPlaatsingColumns();
		createGroepColumns();
		createContractColumns(toonContract);
		createInburgeringColumns();
		createKenmerkenColumns();
		createIntakegesprekColumns();
		createVooropleidingColumns();
		createExamenColumns();
		createDocumentColumns();
		createVrijVeldKolommen(VrijVeldCategorie.DEELNEMERPERSONALIA, "deelnemer.persoon");
		createVrijVeldKolommen(VrijVeldCategorie.VERBINTENIS, "");
		createVrijVeldKolommen(VrijVeldCategorie.UITSCHRIJVING, "");

		createModuleSpecifiekeKolommen();
	}

	private void createDeelnemerColumns()
	{
		addColumn(new CustomPropertyColumn<Verbintenis>("Nummer", "Nummer",
			"deelnemer.deelnemernummer", "deelnemer.deelnemernummer", false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Roepnaam", "Roepnaam", "persoon.roepnaam",
			"deelnemer.persoon.roepnaam", false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Voorletters", "Voorletters",
			"persoon.voorletters", "deelnemer.persoon.voorletters", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Voornamen", "Voornamen",
			"persoon.voornamen", "deelnemer.persoon.voornamen", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Voorvoegsel", "Voorvoegsel",
			"persoon.voorvoegsel", "deelnemer.persoon.voorvoegsel", false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Achternaam", "Achternaam",
			"persoon.achternaam", "deelnemer.persoon.achternaam", false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Geboortevoorvoegsel",
			"Geboortevoorvoegsel", "persoon.officieleVoorvoegsel",
			"deelnemer.persoon.officieleVoorvoegsel", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Geboortenaam", "Geboortenaam",
			"persoon.officieleAchternaam", "deelnemer.persoon.officieleAchternaam", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Volledige naam", "Volledige naam",
			"persoon.achternaam", "deelnemer.persoon.volledigeNaam", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Volledige naam officieel",
			"Volledige naam officieel", "persoon.officieleAchternaam",
			"deelnemer.persoon.volledigeNaamOfficieel", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Geslacht", "Geslacht", "persoon.geslacht",
			"deelnemer.persoon.geslacht", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Geboortedatum", "Geboortedatum",
			"persoon.geboortedatum", "deelnemer.persoon.geboortedatum", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Geboortegemeente", "Geboortegemeente",
			"persoon.geboorteGemeente.naam", "deelnemer.persoon.geboorteGemeente.naam", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Geboren te", "Geboren te",
			"persoon.geboorteplaats", "deelnemer.persoon.geboorteplaats", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Geboorteland", "Geboorteland",
			"persoon.geboorteland", "deelnemer.persoon.geboorteland.naam", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Nationaliteit 1", "Nationaliteit 1",
			"persoon.nationaliteit1", "deelnemer.persoon.nationaliteit1.naam", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Nationaliteit 2", "Nationaliteit 2",
			"persoon.nationaliteit2", "deelnemer.persoon.nationaliteit2.naam", false)
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Verbintenis>("Burgerservicenummer (sofinummer)", "BSN",
			"persoon.bsn", "deelnemer.persoon.bsn", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Onderwijsnummer", "Onderwijsnummer",
			"deelnemer.onderwijsnummer", "deelnemer.onderwijsnummer", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Debiteurennummer", "Debiteurennummer",
			"persoon.debiteurennummer", "deelnemer.persoon.debiteurennummer", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Datum in Nederland", "Datum in NL",
			"persoon.datumInNederland", "deelnemer.persoon.datumInNederland", false)
			.setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Verbintenis>("Allochtoon", "Allochtoon",
			"deelnemer.allochtoon", "deelnemer.allochtoon", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Startkwalificatieplichtig tot",
			"Startkwalificatieplichtig tot", "deelnemer.startkwalificatieplichtigTot",
			"deelnemer.startkwalificatieplichtigTot", false).setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Verbintenis>("Leerlinggebonden financiering (LGF)",
			"LGF", "deelnemer.lgf", "deelnemer.lgf", false).setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Verbintenis>("Inburgeraar", "Inburgeraar",
			"persoon.nieuwkomer", "deelnemer.persoon.nieuwkomer", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Leeftijd", "Leeftijd",
			"persoon.geboortedatum", "deelnemer.persoon.leeftijd", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Datum overlijden", "Datum overlijden",
			"persoon.datumOverlijden", "deelnemer.persoon.datumOverlijden", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Registratie datum", "Registratie datum",
			"deelnemer.registratieDatum", "deelnemer.registratieDatum", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Wachtwoord", "Wachtwoord",
			"deelnemer.persoon.wachtwoord", "deelnemer.persoon.wachtwoord", false)
			.setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Verbintenis>("Indicatie betalingsplichtige",
			"Betalingsplichtige", "deelnemer.persoon.persoonBetalingsplichtige")
			.setDefaultVisible(false));
	}

	private void createBPVColumns()
	{
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Bedrijven", "BPV Bedrijven",
			"BPVBedrijvenOpPeildatum", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Afsluitdata", "BPV Afsluitdata",
			"BPVAfsluitdataOpPeildatum", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Begindata", "BPV Begindata",
			"BPVBegindataOpPeildatum", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Verwachte Einddata",
			"BPV Verwachte Einddata", "BPVVerwachteEinddataOpPeildatum", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Einddata", "BPV Einddata",
			"BPVEinddataOpPeildatum", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Codes leerbedrijf",
			"BPV Codes leerbedrijf", "BPVCodesLeerbedrijfOpPeildatum", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Contractpartners",
			"BPV Contractpartners", "BPVContractpartnersOpPeildatum", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Contactpersonen contractpartner",
			"BPV Contactpersonen contractpartner", "BPVContactpersonenContractpartnerOpPeildatum",
			false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Praktijkopleiders bpv-bedrijf",
			"BPV Praktijkopleiders bpv-bedrijf", "BPVPraktijkopleidersBPVBedrijfOpPeildatum", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Statussen", "BPV Statussen",
			"BPVStatussenOpPeildatum", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Opnemen in BRON",
			"BPV Opnemen in BRON", null, "BPVOpnemenInBronOpPeildatum", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Praktijkbiedende organisatie",
			"BPV Praktijkbiedende org.", "BPVPraktijkbiedendeOrganisatieOpPeildatum", false)
			.setDefaultVisible(false));
	}

	private void createBPVAdresColumns()
	{
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Bedrijf bezoekadres",
			"BPV Bezoekadres", "BPVBedrijvenBezoekadresOpPeildatumFormatted", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>(
			"BPV Bedrijf straat en huisnummer (bezoekadres)", "BPV Bezoekadres",
			"BPVBedrijvenBezoekStraatHuisnummerOpPeildatumFormatted", false)
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Verbintenis>(
			"BPV Bedrijf postcode en plaats (bezoekadres)", "BPV (B)Postcode en plaats",
			"BPVBedrijvenBezoekPostcodePlaatsOpPeildatumFormatted", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Bedrijf gemeente (bezoekadres)",
			"BPV Gemeente (bezoekadres)", "BPVBedrijvenBezoekGemeenteOpPeildatumFormatted", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Bedrijf provincie (bezoekadres)",
			"BPV Provincie (bezoekadres)", "BPVBedrijvenBezoekProvincieOpPeildatumFormatted", false)
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Bedrijf postadres", "BPV Postadres",
			"BPVBedrijvenPostadresOpPeildatumFormatted", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>(
			"BPV Bedrijf straat en huisnummer (postadres)", "BPV Postadres",
			"BPVBedrijvenPostStraatHuisnummerOpPeildatumFormatted", false).setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Verbintenis>(
			"BPV Bedrijf postcode en plaats (postadres)", "BPV (P)Postcode en plaats",
			"BPVBedrijvenPostPostcodePlaatsOpPeildatumFormatted", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Bedrijf gemeente (postadres)",
			"BPV Gemeente (postadres)", "BPVBedrijvenPostGemeenteOpPeildatumFormatted", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Bedrijf provincie (postadres)",
			"BPV Provincie (postadres)", "BPVBedrijvenPostProvincieOpPeildatumFormatted", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BPV Bedrijf telefoonnummers",
			"BPV Telefoonnummers", "BPVTelefoonnummersOpPeildatum", false).setDefaultVisible(false));
	}

	private void createAdresColumns()
	{
		addColumn(new CustomPropertyColumn<Verbintenis>("Straat en huisnummer (woonadres)",
			"Woonadres", "deelnemer.persoon.fysiekAdres.adres.straatHuisnummerFormatted", false)
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Verbintenis>("Postcode en plaats (woonadres)",
			"(W)Postcode en plaats", "deelnemer.persoon.fysiekAdres.adres.postcodePlaatsFormatted",
			false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Gemeente (woonadres)",
			"Gemeente (woonadres)", "deelnemer.persoon.fysiekAdres.adres.gemeenteFormatted", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Provincie (woonadres)",
			"Provincie (woonadres)", "deelnemer.persoon.fysiekAdres.adres.provincieFormatted",
			false).setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Verbintenis>("Straat en huisnummer (postadres)",
			"Postadres", "deelnemer.persoon.postAdres.adres.straatHuisnummerFormatted", false)
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Verbintenis>("Postcode en plaats (postadres)",
			"(P)Postcode en plaats", "deelnemer.persoon.postAdres.adres.postcodePlaatsFormatted",
			false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Gemeente (postadres)",
			"Gemeente (postadres)", "deelnemer.persoon.postAdres.adres.gemeenteFormatted", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Provincie (postadres)",
			"Provincie (postadres)", "deelnemer.persoon.postAdres.adres.provincieFormatted", false)
			.setDefaultVisible(false));

		addColumn(new BooleanPropertyColumn<Verbintenis>("Geheim (woonadres)", "Geheim woonadres",
			null, "deelnemer.persoon.fysiekAdres.adres.geheim", false).setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Verbintenis>("Geheim (postadres)", "Geheim postadres",
			null, "deelnemer.persoon.postAdres.adres.geheim", false).setDefaultVisible(false));
	}

	private void createContactgegevenColumns()
	{
		addColumn(new CustomPropertyColumn<Verbintenis>("Telefoonnummer", "Telefoon",
			"deelnemer.persoon.eersteTelefoon.formattedContactgegeven", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Email-adres", "E-mail",
			"deelnemer.persoon.eersteEmailAdres.formattedContactgegeven", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Homepage", "Homepage",
			"deelnemer.persoon.eersteHomepage.formattedContactgegeven", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Telefoonnummer mobiel", "Mobiel",
			"deelnemer.persoon.eersteMobieltelefoon.formattedContactgegeven", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Contactgegeven overig", "Contactgegeven",
			"deelnemer.persoon.eersteOverig.formattedContactgegeven", false)
			.setDefaultVisible(false));
		List<SoortContactgegeven> soorten =
			DataAccessRegistry.getHelper(SoortContactgegevenDataAccessHelper.class).list(
				Arrays.asList(StandaardContactgegeven.StandaardTonenBijPersoon,
					StandaardContactgegeven.StandaardTonen), true);
		for (SoortContactgegeven soort : soorten)
		{
			addColumn(new ContactgegevenColumn<Verbintenis>(soort, soort.getNaam(), true)
				.setDefaultVisible(false));
		}
	}

	private void createOpleidingColumns()
	{
		addColumn(new CustomPropertyColumn<Verbintenis>("Opleiding", "Opleiding", "opleiding.naam",
			"opleiding.naam"));
		addColumn(new CustomPropertyColumn<Verbintenis>("Opleidingscode", "Opleidingscode",
			"opleiding.code", "opleiding.code").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Leerweg", "Leerweg", "opleiding.leerweg",
			"opleiding.leerweg").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Taxonomiecode", "Taxonomie",
			"verbintenisgebied.sorteercode", "opleiding.verbintenisgebied.taxonomiecode")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Verbintenisgebied", "Verbintenisgebied",
			"verbintenisgebied.naam", "opleiding.verbintenisgebied.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Crebo- / elementcode",
			"Crebo- / elementcode", "verbintenisgebied.externeCode", "externeCode")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Soort verbintenisgebied",
			"Soort verb.geb.", "verbintenisgebied.taxonomieElementType",
			"opleiding.verbintenisgebied.taxonomieElementType.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Niveau", "Niveau",
			"verbintenisgebied.niveau", "opleiding.verbintenisgebied.niveauNaam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Prijsfactor", "Prijsfactor",
			"verbintenisgebied.prijsfactor", "opleiding.verbintenisgebied.prijsfactor")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Organisatie-eenheid", "Organisatie",
			"organisatieEenheid.naam", "organisatieEenheid.naam"));
		addColumn(new CustomPropertyColumn<Verbintenis>("Locatie", "Locatie", "locatie.naam",
			"locatie.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Team", "Team", "team")
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Verbintenis>("Begindatum", "Begindatum", "begindatum",
			"begindatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Einddatum", "Einddatum", "einddatum",
			"einddatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Geplande einddatum", "Geplande einddatum",
			"geplandeEinddatum", "geplandeEinddatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Bekostigd", "Bekostigd", "bekostigd",
			"bekostigd").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("LWOO", "LWOO",
			"plaatsingOpPeildatum.lwooOmschrijving").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Toelichting", "Toelichting", "toelichting")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Intensiteit", "Intensiteit",
			"intensiteit", "intensiteit").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("SBU's geplande duur",
			"SBU's geplande duur", "studiebelastingsurenGeplandeDuur",
			"studiebelastingsurenGeplandeDuur").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Geplande studiemaanden",
			"Geplande studiemaanden", "geplandeDuurInStudiemaanden", "geplandeDuurInStudiemaanden")
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Verbintenis>("Cohort", "Cohort", "cohort", "cohort.naam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Reden beëindigen", "Reden beëindigen",
			"redenUitschrijving", "redenUitschrijving.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Status", "Status", "status", "status")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Volgnummer", "Vlgnr", "volgnummer",
			"volgnummer").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Overeenkomstnummer", "Overeenkomstnr",
			"overeenkomstnummer", "overeenkomstnummer").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Contacturen per week", "Contacturen",
			"contacturenPerWeek", "contacturenPerWeek").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Vertrekstatus", "Vertrekstatus",
			"vertrekstatus", "vertrekstatus").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>(
			"Afgenomen onderwijsproducten binnen verbintenis", "Onderwijsproducten",
			"contextOnderwijsproductCodes").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BRON status", "BRON status", "bronStatus",
			"bronStatus").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BRON datum", "BRON datum", "bronDatum",
			"bronDatum").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Verbintenis>("Indicatie gehandicapt", "Gehandicapt",
			"indicatieGehandicapt", "indicatieGehandicapt").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Sectornamen TL", "Sectornamen TL",
			"alleSectorNamen").setDefaultVisible(false));

	}

	private void createPlaatsingColumns()
	{
		addColumn(new CustomPropertyColumn<Verbintenis>("Leerjaar", "Lj",
			"plaatsingOpPeildatum.leerjaar").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Praktijkjaar", "Pj",
			"plaatsingOpPeildatum.jarenPraktijkonderwijs").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Plaatsingsgroep (code)",
			"Plaatsingsgroep", "plaatsingOpPeildatum.groep.code").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Plaatsingsgroep (naam)",
			"Plaatsingsgroep", "plaatsingOpPeildatum.groep.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Type plaatsingsgroep",
			"Type plaatsingsgroep", "plaatsingOpPeildatum.groep.groepstype.naam")
			.setDefaultVisible(false));
	}

	private void createGroepColumns()
	{
		addColumn(new CustomPropertyColumn<Verbintenis>("Groepen (code)", "Groepen",
			"deelnemer.groepscodesOpPeildatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Groepen (naam)", "Groepen",
			"deelnemer.groepsnamenOpPeildatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Groepstypes", "Groepstypes",
			"deelnemer.groepstypesOpPeildatum").setDefaultVisible(false));
	}

	private void createContractColumns(boolean toonContract)
	{
		addColumn(new CustomPropertyColumn<Verbintenis>("Contracten", "Contracten",
			"contractOmschrijvingOpPeildatum").setDefaultVisible(toonContract)
			.setEscapeModelStrings(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Contractcode(s)", "Contractcode(s)",
			"contractcodesOpPeildatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Contractnamen", "Contractnamen",
			"contractnamenOpPeildatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Contractonderdelen", "Contractonderdelen",
			"contractOnderdeelNamenOpPeildatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Begindata contracten",
			"Begindata contracten", "contractBegindataOpPeildatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Einddata contracten",
			"Einddata contracten", "contractEinddataOpPeildatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Naam externe organisatie contract",
			"Externe org.", "contractExterneOrganisatieNamenOpPeildatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>(
			"Debiteurennummer externe organisatie contract", "Debiteurennummer externe org.",
			"contractExterneOrganisatieDebiteurennummersOpPeildatum").setDefaultVisible(false));
	}

	private void createGroupProperties()
	{
		addGroupProperty(new GroupProperty<Verbintenis>("opleiding.naam", "Opleiding",
			"opleiding.naam"));
		addGroupProperty(new GroupProperty<Verbintenis>(
			"opleiding.verbintenisgebied.taxonomiecode", "Taxonomiecode",
			"verbintenisgebied.sorteercode"));
		addGroupProperty(new GroupProperty<Verbintenis>("opleiding.verbintenisgebied.naam",
			"Verbintenisgebied", "verbintenisgebied.naam"));
		addGroupProperty(new GroupProperty<Verbintenis>("opleiding.verbintenisgebied.externeCode",
			"CREBO-code / Elementcode", "verbintenisgebied.externeCode"));
		addGroupProperty(new GroupProperty<Verbintenis>(
			"opleiding.verbintenisgebied.taxonomieElementType", "Soort verbintenisgebied",
			"verbintenisgebied.taxonomieElementType"));
		addGroupProperty(new GroupProperty<Verbintenis>("locatie.naam", "Locatie", "locatie.naam"));
		addGroupProperty(new GroupProperty<Verbintenis>("organisatieEenheid.naam",
			"Organisatie-eenheid", "organisatieEenheid.naam"));
		addGroupProperty(new GroupProperty<Verbintenis>("status", "Status", "status"));
		addGroupProperty(new GroupProperty<Verbintenis>("deelnemer.persoon.geboorteGemeente.naam",
			"Geboortegemeente", "persoon.geboorteGemeente"));
		addGroupProperty(new GroupProperty<Verbintenis>("deelnemer.persoon.geboorteland.naam",
			"Geboorteland", "persoon.geboorteland"));
		addGroupProperty(new GroupProperty<Verbintenis>("cohort.naam", "Cohort", "cohort"));
	}

	private void createInburgeringColumns()
	{
		addColumn(new CustomPropertyColumn<Verbintenis>("Reden inburgering", "Reden inburgering",
			"redenInburgering", "redenInburgering").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Profiel inburgering",
			"Profiel inburgering", "profielInburgering", "profielInburgering")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Leerprofiel", "Leerprofiel",
			"leerprofiel", "leerprofiel").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Verbintenis>("Deelcursus", "Deelcursus", "deelcursus",
			"deelcursus").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Soort praktijkexamen",
			"Soort praktijkexamen", "soortPraktijkexamen", "soortPraktijkexamen")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Datum aanmelden/beschikking",
			"Datum aanmelden/beschikking", "datumAanmelden", "datumAanmelden")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Datum akkoord", "Datum akkoord",
			"datumAkkoord", "datumAkkoord").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Datum eerste activiteit",
			"Datum eerste activiteit", "datumEersteActiviteit", "datumEersteActiviteit")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Beginniveau schriftelijke vaardigheden",
			"Beginniveau schriftelijke vaardigheden", "beginNiveauSchriftelijkeVaardigheden",
			"beginNiveauSchriftelijkeVaardigheden").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Eindniveau schriftelijke vaardigheden",
			"Eindniveau schriftelijke vaardigheden", "eindNiveauSchriftelijkeVaardigheden",
			"eindNiveauSchriftelijkeVaardigheden").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Onderaanneming", "Onderaanneming",
			"inburgeringContractOnderaanneming").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Uitstroomreden WI", "Uitstroomreden WI",
			"redenUitschrijving.uitstroomredenWI").setDefaultVisible(false));
	}

	private void createKenmerkenColumns()
	{
		addColumn(new CustomPropertyColumn<Verbintenis>("Kenmerken", "Kenmerken",
			"deelnemer.kenmerkNamen").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Kenmerkcategorien", "Kenmerkcategorien",
			"deelnemer.kenmerkCategorien").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Kenmerktoelichting", "Kenmerktoelichting",
			"deelnemer.kenmerkToelichtingen").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Begindatum kenmerk", "Begindatum kenmerk",
			"deelnemer.begindatumKenmerken").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Einddatum kenmerk", "Einddatum kenmerk",
			"deelnemer.einddatumKenmerken").setDefaultVisible(false));
	}

	private void createIntakegesprekColumns()
	{
		addColumn(new CustomPropertyColumn<Verbintenis>("Status intakegesprek",
			"Status intakegesprek", "intakegesprekStatussen").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Datum/tijd intakegesprek",
			"Datum/tijd intakegesprek", "intakegesprekkenDatumTijd").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Intaker", "Intaker",
			"intakegesprekkenIntaker").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Gewenste opleiding", "Gewenste opleiding",
			"intakegesprekkenGewensteOpleiding").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Gewenste locatie", "Gewenste locatie",
			"intakegesprekkenGewensteLocatie").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Gewenste begindatum",
			"Gewenste begindatum", "intakegesprekkenGewensteBegindatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Gewenste einddatum", "Gewenste einddatum",
			"intakegesprekkenGewensteEinddatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Gewenste BPV", "Gewenste BPV",
			"intakegesprekkenGewensteBpv").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Intakegesprekopmerking",
			"Intakegesprekopmerking", "intakegesprekkenOpmerking").setDefaultVisible(false));
	}

	private void createVooropleidingColumns()
	{
		addColumn(new CustomPropertyColumn<Verbintenis>("Categorie relevante vooropleiding",
			"Categorie relevante vooropleiding", "relevanteVooropleiding.soortOnderwijs")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Omschrijving vooropleiding",
			"Omschrijving vooropleiding", "relevanteVooropleiding.omschrijving")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Brincode vooropleiding",
			"Brincode vooropleiding", "relevanteVooropleiding.brincode").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Soort vooropleiding",
			"Soort vooropleiding", "relevanteVooropleiding.soortVooropleiding")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("School vooropleiding",
			"School vooropleiding", "relevanteVooropleiding.organisatieOmschrijving")
			.setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Verbintenis>("Vooropleiding diploma behaald",
			"Vooropleiding dipl. behaald", "relevanteVooropleiding.diplomaBehaald")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Einddatum vooropleiding",
			"Einddatum vooropleiding", "relevanteVooropleiding.einddatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Schooladvies", "Schooladvies",
			"relevanteVooropleiding.schooladvies").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Citoscore", "Cito-score",
			"relevanteVooropleiding.citoscore").setDefaultVisible(false));
	}

	private void createExamenColumns()
	{
		addColumn(new CustomPropertyColumn<Verbintenis>("Examenstatus", "Examenstatus",
			"examendeelname.examenstatus.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Datum uitslag examen", "Datum uitslag",
			"examendeelname.datumUitslag").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Examennummer", "Examennummer",
			"examendeelname.examennummerMetPrefix").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Verbintenis>("Examen bekostigd", "Examen bekostigd",
			"examendeelname.bekostigd").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Examen tijdvak", "Tijdvak",
			"examendeelname.tijdvak").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Examenworkflow", "Examenworkflow",
			"examendeelname.examenstatus.examenWorkflow.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Examenjaar", "Examenjaar",
			"examendeelname.examenjaar").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Werkstuktitel", "Werkstuktitel",
			"werkstukTitel").setDefaultVisible(false));
	}

	private void createRelatieColumns()
	{
		addColumn(new CustomPropertyColumn<Verbintenis>("Naam eerste verzorger",
			"Naam eerste verzorger", "deelnemer.eersteVerzorger.relatie.volledigeNaam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BSN eerste verzorger",
			"BSN eerste verzorger", "deelnemer.eersteVerzorger.relatie.bsn")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Debiteurennummer eerste verzorger",
			"Debiteurennummer eerste verzorger",
			"deelnemer.eersteVerzorger.relatie.debiteurennummer").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Relatiesoort eerste verzorger",
			"Relatiesoort eerste verzorger", "deelnemer.eersteVerzorger.relatieSoort.naam")
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Verbintenis>("Naam tweede verzorger",
			"Naam tweede verzorger", "deelnemer.tweedeVerzorger.relatie.volledigeNaam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BSN tweede verzorger",
			"BSN tweede verzorger", "deelnemer.tweedeVerzorger.relatie.bsn")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Debiteurennummer tweede verzorger",
			"Debiteurennummer tweede verzorger",
			"deelnemer.tweedeVerzorger.relatie.debiteurennummer").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Relatiesoort tweede verzorger",
			"Relatiesoort tweede verzorger", "deelnemer.tweedeVerzorger.relatieSoort.naam")
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Verbintenis>("Naam wettelijke vertegenwoordiger",
			"Naam wettelijke vertegenwoordiger",
			"deelnemer.wettelijkeVertegenwoordiger.volledigeNaam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("BSN wettelijke vertegenwoordiger",
			"BSN wettelijke vertegenwoordiger", "deelnemer.wettelijkeVertegenwoordiger.bsn")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>(
			"Debiteurennummer wettelijke vertegenwoordiger",
			"Debiteurennummer wettelijke vertegenwoordiger",
			"deelnemer.wettelijkeVertegenwoordiger.debiteurennummer").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Verbintenis>("Debiteurennummer betalingsplichtige",
			"Debiteurennummer betalingsplichtige",
			"deelnemer.persoon.betalingsplichtige.debiteurennummer").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Bankrekeningnummer betalingsplichtige",
			"Bankrekeningnummer betalingsplichtige",
			"deelnemer.persoon.betalingsplichtige.bankrekeningnummer").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Naam betalingsplichtige",
			"Naam betalingsplichtige", "deelnemer.persoon.betalingsplichtige.formeleNaam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Automatische incasso betalingsplichtige",
			"Automatische incasso betalingsplichtige",
			"deelnemer.persoon.betalingsplichtige.automatischeIncasso").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>(
			"Einddatum automatische incasso betalingsplichtige",
			"Einddatum automatische incasso betalingsplichtige",
			"deelnemer.persoon.betalingsplichtige.automatischeIncassoEinddatum")
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Verbintenis>("Woonadres eerste verzorger",
			"Woonadres eerste verzorger",
			"deelnemer.eersteVerzorger.relatie.fysiekAdres.adres.straatHuisnummerFormatted")
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Verbintenis>("Woonplaats eerste verzorger",
			"Woonplaats eerste verzorger",
			"deelnemer.eersteVerzorger.relatie.fysiekAdres.adres.postcodePlaatsFormatted", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Postadres eerste verzorger",
			"Postadres eerste verzorger",
			"deelnemer.eersteVerzorger.relatie.postAdres.adres.straatHuisnummerFormatted", false)
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Verbintenis>("Postplaats eerste verzorger",
			"Postplaats eerste verzorger",
			"deelnemer.eersteVerzorger.relatie.postAdres.adres.postcodePlaatsFormatted", false)
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Verbintenis>("Woonadres tweede verzorger",
			"Woonadres tweede verzorger",
			"deelnemer.tweedeVerzorger.relatie.fysiekAdres.adres.straatHuisnummerFormatted")
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Verbintenis>("Woonplaats tweede verzorger",
			"Woonplaats tweede verzorger",
			"deelnemer.tweedeVerzorger.relatie.fysiekAdres.adres.postcodePlaatsFormatted", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Postadres tweede verzorger",
			"Postadres tweede verzorger",
			"deelnemer.tweedeVerzorger.relatie.postAdres.adres.straatHuisnummerFormatted", false)
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Verbintenis>("Postplaats tweede verzorger",
			"Postplaats tweede verzorger",
			"deelnemer.tweedeVerzorger.relatie.postAdres.adres.postcodePlaatsFormatted", false)
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Verbintenis>("Woonadres wettelijke vertegenwoordiger",
			"Woonadres wettelijke vertegenwoordiger",
			"deelnemer.wettelijkeVertegenwoordiger.fysiekAdres.adres.straatHuisnummerFormatted")
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Verbintenis>(
			"Woonplaats wettelijke vertegenwoordiger", "Woonplaats wettelijke vertegenwoordiger",
			"deelnemer.wettelijkeVertegenwoordiger.fysiekAdres.adres.postcodePlaatsFormatted",
			false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Postadres wettelijke vertegenwoordiger",
			"Postadres wettelijke vertegenwoordiger",
			"deelnemer.wettelijkeVertegenwoordiger.postAdres.adres.straatHuisnummerFormatted",
			false).setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Verbintenis>(
			"Postplaats wettelijke vertegenwoordiger", "Postplaats wettelijke vertegenwoordiger",
			"deelnemer.wettelijkeVertegenwoordiger.postAdres.adres.postcodePlaatsFormatted", false)
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Verbintenis>("Woonadres betalingsplichtige",
			"Woonadres betalingsplichtige",
			"deelnemer.persoon.betalingsplichtige.fysiekAdres.adres.straatHuisnummerFormatted")
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Verbintenis>("Woonplaats betalingsplichtige",
			"Woonplaats betalingsplichtige",
			"deelnemer.persoon.betalingsplichtige.fysiekAdres.adres.postcodePlaatsFormatted", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Verbintenis>("Postadres betalingsplichtige",
			"Postadres betalingsplichtige",
			"deelnemer.persoon.betalingsplichtige.postAdres.adres.straatHuisnummerFormatted", false)
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Verbintenis>("Postplaats betalingsplichtige",
			"Postplaats betalingsplichtige",
			"deelnemer.persoon.betalingsplichtige.postAdres.adres.postcodePlaatsFormatted", false)
			.setDefaultVisible(false));
	}

	private void createModuleSpecifiekeKolommen()
	{
		List<BegeleidingModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(BegeleidingModuleComponentFactory.class);
		if (factories.size() > 0)
		{
			for (AbstractCustomColumn<Verbintenis> column : factories.get(0)
				.getDeelnemerTableColumns())
			{
				addColumn(column);
			}
		}

	}

	private void createDocumentColumns()
	{
		DocumentCategorieZoekFilter categorieFilter = new DocumentCategorieZoekFilter();
		categorieFilter.setActief(Boolean.TRUE);
		List<DocumentCategorie> categorien =
			DataAccessRegistry.getHelper(DocumentCategorieDataAccessHelper.class).list(
				categorieFilter);
		for (DocumentCategorie categorie : categorien)
		{
			addColumn(new DocumentCategorieAanwezigColumn<Verbintenis>("Document van categorie "
				+ categorie.getNaam() + " aanwezig", categorie.getNaam(), categorie)
				.setDefaultVisible(false));
		}

	}
}
