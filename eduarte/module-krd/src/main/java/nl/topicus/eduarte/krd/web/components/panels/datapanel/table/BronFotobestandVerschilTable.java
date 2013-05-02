package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.ClickableCustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestandVerschil;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestandVerschil.FotoVerschil;
import nl.topicus.eduarte.krd.web.pages.deelnemer.EditDeelnemerPersonaliaPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.individueel.DeelnemerExamenEditPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.individueel.DeelnemerExamenPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.EditVerbintenisPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv.EditBPVInschrijvingPage;
import nl.topicus.eduarte.web.components.datapanel.PostcodeWoonplaatsColumn;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.DeelnemerVerbintenisPage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.bpv.DeelnemerBPVPage;

import org.apache.wicket.Page;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author loite
 */
public class BronFotobestandVerschilTable extends
		CustomDataPanelContentDescription<BronFotobestandVerschil>
{
	private static final long serialVersionUID = 1L;

	public BronFotobestandVerschilTable()
	{
		super("Verschillen tussen fotobestand en database");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Verschil", "Verschil",
			"verschil", "verschil.korteOmschrijving"));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("PGN", "PGN", "fotoRecord.pgn",
			"fotoRecord.pgn"));
		addColumn(new ClickableCustomPropertyColumn<BronFotobestandVerschil>("Nummer", "Nummer",
			"deelnemer.deelnemernummer", "deelnemer.deelnemernummer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(IModel<BronFotobestandVerschil> model)
			{
				SecurePage returnTo = (SecurePage) RequestCycle.get().getResponsePage();

				BronFotobestandVerschil container = model.getObject();
				Verbintenis verbintenis = container.getVerbintenis();
				Deelnemer deelnemer = container.getDeelnemer();

				Page response = null;
				FotoVerschil verschil = container.getVerschil();
				if (verschil.isBpvVerschil() && container.getBpvInschrijving() == null)
				{
					response = new DeelnemerBPVPage(deelnemer, verbintenis);
				}
				else if (verschil.isBpvVerschil() && container.getBpvInschrijving() != null)
				{
					response =
						new EditBPVInschrijvingPage(container.getBpvInschrijving(), returnTo);
				}
				else if (verschil.isInschrijvingsVerschil() && verbintenis != null)
				{
					response = new EditVerbintenisPage(verbintenis, returnTo);
				}
				else if (verschil.isInschrijvingsVerschil() && deelnemer != null)
				{
					response = new DeelnemerVerbintenisPage(deelnemer);
				}
				else if (verschil.isDiplomaVerschil() && container.getExamendeelname() != null)
				{
					response = new DeelnemerExamenEditPage(container.getExamendeelname());
				}
				else if (verschil.isDiplomaVerschil())
				{
					response =
						new DeelnemerExamenPage(deelnemer, verbintenis, container
							.getExamendeelname());
				}
				else if (verschil.isPersoonsgegevensVerschil())
				{
					response = new EditDeelnemerPersonaliaPage(deelnemer, returnTo);
				}
				else if (verbintenis != null)
				{
					response = new DeelnemerkaartPage(verbintenis);
				}
				else
				{
					response = new DeelnemerkaartPage(deelnemer);
				}
				String database = StringUtil.emptyOrStringValue(container.getWaardeInDb());
				String bron = StringUtil.emptyOrStringValue(container.getWaardeInBron());

				response.warn(String.format("%s. In database: %s In BRON: %s", verschil
					.getOmschrijving(), database, bron));
				RequestCycle.get().setResponsePage(response);
			}
		});
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Naam", "Naam",
			"persoon.achternaam", "deelnemer.persoon.volledigeNaam"));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("BRON", "BRON", "waardeInBron"));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Database", "Database",
			"waardeInDb"));

		createDeelnemerColumns();
		createAdresColumns();
		createContactgegevenColumns();
		createOpleidingColumns();
		createPlaatsingColumns();
		createContractColumns();
		createBPVColumns();
		createGroupProperties();
	}

	private void createDeelnemerColumns()
	{
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Roepnaam", "Roepnaam",
			"persoon.roepnaam", "deelnemer.persoon.roepnaam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Voorletters", "Voorletters",
			"persoon.voorletters", "deelnemer.persoon.voorletters").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Voornamen", "Voornamen",
			"persoon.voornamen", "deelnemer.persoon.voornamen").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Voorvoegsel", "Voorvoegsel",
			"persoon.voorvoegsel", "deelnemer.persoon.voorvoegsel").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Achternaam", "Achternaam",
			"persoon.achternaam", "deelnemer.persoon.achternaam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Geboortevoorvoegsel",
			"Geboortevoorvoegsel", "persoon.officieleVoorvoegsel",
			"deelnemer.persoon.officieleVoorvoegsel").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Geboortenaam", "Geboortenaam",
			"persoon.officieleAchternaam", "deelnemer.persoon.officieleAchternaam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Geslacht", "Geslacht",
			"persoon.geslacht", "deelnemer.persoon.geslacht").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Geboortedatum",
			"Geboortedatum", "persoon.geboortedatum", "deelnemer.persoon.geboortedatum")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("BPV Bedrijf", "BPV Bedrijf",
			"verbintenis.BPVInschrijvingOpPeildatum.bpvBedrijf.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Geboortegemeente",
			"Geboortegemeente", "persoon.geboorteGemeente.naam",
			"deelnemer.persoon.geboorteGemeente.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Geboren te", "Geboren te",
			"persoon.geboorteplaats", "deelnemer.persoon.geboorteplaats").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Geboorteland", "Geboorteland",
			"persoon.geboorteland", "deelnemer.persoon.geboorteland.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Nationaliteit 1",
			"Nationaliteit 1", "persoon.nationaliteit1", "deelnemer.persoon.nationaliteit1.naam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Nationaliteit 2",
			"Nationaliteit 2", "persoon.nationaliteit2", "deelnemer.persoon.nationaliteit2.naam")
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>(
			"Burgerservicenummer (sofinummer)", "BSN", "persoon.bsn", "deelnemer.persoon.bsn")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Onderwijsnummer",
			"Onderwijsnummer", "deelnemer.onderwijsnummer", "deelnemer.onderwijsnummer")
			.setDefaultVisible(false));
	}

	private void createAdresColumns()
	{
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>(
			"Straat en huisnummer (woonadres)", "Adres",
			"deelnemer.persoon.fysiekAdres.adres.straatHuisnummer").setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<BronFotobestandVerschil>(
			"Postcode en plaats (woonadres)", "Postcode en plaats",
			"deelnemer.persoon.fysiekAdres.adres.postcodePlaats").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>(
			"Straat en huisnummer (postadres)", "Adres",
			"deelnemer.persoon.postAdres.adres.straatHuisnummer").setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<BronFotobestandVerschil>(
			"Postcode en plaats (postadres)", "Postcode en plaats",
			"deelnemer.persoon.postAdres.adres.postcodePlaats").setDefaultVisible(false));
	}

	private void createContactgegevenColumns()
	{
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Telefoonnummer", "Telefoon",
			"deelnemer.persoon.eersteTelefoon.contactgegeven").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Email-adres", "E-mail",
			"deelnemer.persoon.eersteEmailAdres.contactgegeven").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Homepage", "Homepage",
			"deelnemer.persoon.eersteHomepage.contactgegeven").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Telefoonnummer mobiel",
			"Mobiel", "deelnemer.persoon.eersteMobieltelefoon.contactgegeven")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Contactgegeven overig",
			"Contactgegeven", "deelnemer.persoon.eersteOverig.contactgegeven")
			.setDefaultVisible(false));
	}

	private void createOpleidingColumns()
	{
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Opleiding", "Opleiding",
			"opleiding.naam", "verbintenis.opleiding.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Taxonomiecode", "Taxonomie",
			"verbintenisgebied.sorteercode",
			"verbintenis.opleiding.verbintenisgebied.taxonomiecode").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Verbintenisgebied",
			"Verbintenisgebied", "verbintenisgebied.naam",
			"verbintenis.opleiding.verbintenisgebied.naam").setDefaultVisible(false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Crebo- / elementcode",
			"Crebo- / elementcode", "verbintenis.opleiding.verbintenisgebied.externeCode")
			.setDefaultVisible(false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Soort verbintenisgebied",
			"Soort verb.geb.", "verbintenisgebied.taxonomieElementType",
			"verbintenis.opleiding.verbintenisgebied.taxonomieElementType.naam").setDefaultVisible(
			false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Organisatie-eenheid",
			"Organisatie", "organisatieEenheid.naam", "verbintenis.organisatieEenheid.naam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Locatie", "Locatie",
			"locatie.naam", "verbintenis.locatie.naam").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Begindatum", "Begindatum",
			"verbintenis.begindatum", "verbintenis.begindatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Einddatum", "Einddatum",
			"verbintenis.einddatum", "verbintenis.einddatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Geplande einddatum",
			"Geplande einddatum", "verbintenis.geplandeEinddatum", "verbintenis.geplandeEinddatum")
			.setDefaultVisible(false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Bekostigd", "Bekostigd",
			"verbintenis.bekostigd", "verbintenis.bekostigd").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("LWOO", "LWOO",
			"verbintenis.plaatsingOpPeildatum.lwooOmschrijving").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Toelichting", "Toelichting",
			"verbintenis.toelichting").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Intensiteit", "Intensiteit",
			"verbintenis.intensiteit", "verbintenis.intensiteit").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Cohort", "Cohort",
			"verbintenis.cohort", "verbintenis.cohort.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Reden uitschrijving",
			"Reden uitschrijving", "verbintenis.redenUitschrijving",
			"verbintenis.redenUitschrijving.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Status", "Status",
			"verbintenis.status", "verbintenis.status").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Volgnummer", "Vlgnr",
			"verbintenis.volgnummer", "verbintenis.volgnummer").setDefaultVisible(false));
	}

	private void createPlaatsingColumns()
	{
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Leerjaar", "Lj",
			"verbintenis.plaatsingOpPeildatum.leerjaar").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Basisgroep (code)",
			"Basisgroep", "verbintenis.plaatsingOpPeildatum.basisgroep.code").setDefaultVisible(
			false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Basisgroep (naam)",
			"Basisgroep", "verbintenis.plaatsingOpPeildatum.basisgroep.naam").setDefaultVisible(
			false).setDefaultVisible(false));
	}

	private void createContractColumns()
	{
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Contractcode(s)",
			"Contractcode(s)", "verbintenis.contractcodesOpPeildatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("Contractnamen",
			"Contractnamen", "verbintenis.contractnamenOpPeildatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>(
			"Naam externe organisatie contract", "Externe org.",
			"verbintenis.contractExterneOrganisatieNamenOpPeildatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>(
			"Debiteurennummer externe organisatie contract", "Debiteurennummer externe org.",
			"verbintenis.contractExterneOrganisatieDebiteurennummersOpPeildatum")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>(
			"Ocw-code externe organisatie contract", "Ocw-code externe org.",
			"verbintenis.contractExterneOrganisatieOcwCodesOpPeildatum").setDefaultVisible(false));
	}

	private void createBPVColumns()
	{
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("BPV Afsluitdatum",
			"BPV Afsluitdatum", "bpvInschrijving.afsluitdatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("BPV Begindatum",
			"BPV Begindatum", "bpvInschrijving.begindatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("BPV Geplande einddatum",
			"BPV Geplande einddatum", "bpvInschrijving.verwachteEinddatum")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("BPV Werkelijke einddatum",
			"BPV Werkelijke einddatum", "bpvInschrijving.einddatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("BPV Omvang", "BPV Omvang",
			"bpvInschrijving.totaleOmvang").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BronFotobestandVerschil>("BPV Code leerbedrijf",
			"BPV Code leerbedrijf", "bpvInschrijving.codeLeerbedrijf").setDefaultVisible(false));
	}

	private void createGroupProperties()
	{
		addGroupProperty(new GroupProperty<BronFotobestandVerschil>("verbintenis.opleiding.naam",
			"Opleiding", "opleiding.naam"));
		addGroupProperty(new GroupProperty<BronFotobestandVerschil>(
			"verbintenis.opleiding.verbintenisgebied.taxonomiecode", "Taxonomiecode",
			"verbintenisgebied.sorteercode"));
		addGroupProperty(new GroupProperty<BronFotobestandVerschil>(
			"verbintenis.opleiding.verbintenisgebied.naam", "Verbintenisgebied",
			"verbintenisgebied.naam"));
		addGroupProperty(new GroupProperty<BronFotobestandVerschil>(
			"verbintenis.opleiding.verbintenisgebied.externeCode", "CREBO-code / Elementcode",
			"verbintenisgebied.externeCode"));
		addGroupProperty(new GroupProperty<BronFotobestandVerschil>(
			"verbintenis.opleiding.verbintenisgebied.taxonomieElementType",
			"Soort verbintenisgebied", "verbintenisgebied.taxonomieElementType"));
		addGroupProperty(new GroupProperty<BronFotobestandVerschil>("verbintenis.locatie.naam",
			"Locatie", "locatie.naam"));
		addGroupProperty(new GroupProperty<BronFotobestandVerschil>(
			"verbintenis.organisatieEenheid.naam", "Organisatie-eenheid", "organisatieEenheid.naam"));
		addGroupProperty(new GroupProperty<BronFotobestandVerschil>("verbintenis.status", "Status",
			"status"));
		addGroupProperty(new GroupProperty<BronFotobestandVerschil>(
			"deelnemer.persoon.geboorteGemeente.naam", "Geboortegemeente",
			"persoon.geboorteGemeente"));
		addGroupProperty(new GroupProperty<BronFotobestandVerschil>(
			"deelnemer.persoon.geboorteland.naam", "Geboorteland", "persoon.geboorteland"));
	}
}
