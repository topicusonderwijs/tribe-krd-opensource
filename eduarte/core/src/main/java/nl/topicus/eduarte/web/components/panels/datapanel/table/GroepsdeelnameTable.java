package nl.topicus.eduarte.web.components.panels.datapanel.table;

import java.util.Date;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyDateFieldColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.cobra.web.components.panels.datapanel.columns.AjaxDeleteColumn;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.web.components.datapanel.PostcodeWoonplaatsColumn;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class GroepsdeelnameTable extends AbstractVrijVeldableTable<Groepsdeelname>
{

	private static final long serialVersionUID = 1L;

	/**
	 * Maakt een kolommen lijst voor een GroepDocent. Verwijderen, bewerken etc kolom
	 * wordt verborgen.
	 */
	public GroepsdeelnameTable()
	{
		this(false, false, false);
	}

	/**
	 * Maakt een kolommen lijst voor een GroepDocent.
	 * 
	 * @param editMode
	 *            geeft aan of er een verwijderen, bewerken etc kolom bij moet komen.
	 */
	public GroepsdeelnameTable(boolean editMode, boolean showDelete, boolean showGroepProperties)
	{
		super(StringUtil.firstCharUppercase(EduArteApp.get().getDeelnemerTermMeervoud()));
		createColumns(editMode, showDelete);
		createGroupProperties(showGroepProperties);
		createVrijVeldKolommen(VrijVeldCategorie.PLAATSING, "");
	}

	private void createColumns(boolean editMode, boolean showDelete)
	{
		createDeelnemerColumns();
		createGroepColumns();
		createAdresColumns();
		createContactgegevenColumns();
		createKenmerkenColumns();
		createRelatieColumns();

		if (editMode)
		{
			addColumn(new CustomPropertyDateFieldColumn<Groepsdeelname>("Begindatum", "Begindatum",
				"begindatum")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(WebMarkupContainer row, AjaxRequestTarget target,
						Date newValue)
				{
					target.addComponent(row);
				}
			}.setRequired(true));
			addColumn(new CustomPropertyDateFieldColumn<Groepsdeelname>("Einddatum", "Einddatum",
				"einddatum")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(WebMarkupContainer row, AjaxRequestTarget target,
						Date newValue)
				{
					target.addComponent(row);
				}
			});
		}
		else
		{
			addColumn(new DatePropertyColumn<Groepsdeelname>("Begindatum", "Begindatum",
				"begindatum", "begindatum"));
			addColumn(new DatePropertyColumn<Groepsdeelname>("Einddatum", "Einddatum", "einddatum",
				"einddatum"));
		}

		if (showDelete)
		{
			addColumn(new AjaxDeleteColumn<Groepsdeelname>("delete", "Verwijder")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(WebMarkupContainer item, IModel<Groepsdeelname> rowModel,
						AjaxRequestTarget target)
				{
					Groepsdeelname groepsdeelname = rowModel.getObject();
					GroepsdeelnameTable.this.deleteGroepsdeelname(groepsdeelname, target);
				}

			}.setPositioning(Positioning.FIXED_RIGHT));
		}
	}

	private void createDeelnemerColumns()
	{
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Nummer", "Nummer",
			"deelnemer.deelnemernummer", "deelnemer.deelnemernummer", false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Roepnaam", "Roepnaam",
			"persoon.roepnaam", "deelnemer.persoon.roepnaam", false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Voorletters", "Voorletters",
			"persoon.voorletters", "deelnemer.persoon.voorletters", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Voornamen", "Voornamen",
			"persoon.voornamen", "deelnemer.persoon.voornamen", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Voorvoegsel", "Voorvoegsel",
			"persoon.voorvoegsel", "deelnemer.persoon.voorvoegsel", false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Achternaam", "Achternaam",
			"persoon.achternaam", "deelnemer.persoon.achternaam", false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Geboortevoorvoegsel",
			"Geboortevoorvoegsel", "persoon.officieleVoorvoegsel",
			"deelnemer.persoon.officieleVoorvoegsel", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Geboortenaam", "Geboortenaam",
			"persoon.officieleAchternaam", "deelnemer.persoon.officieleAchternaam", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Geslacht", "Geslacht",
			"persoon.geslacht", "deelnemer.persoon.geslacht", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Geboortedatum", "Geboortedatum",
			"persoon.geboortedatum", "deelnemer.persoon.geboortedatum", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Geboortegemeente", "Geboortegemeente",
			"persoon.geboorteGemeente.naam", "deelnemer.persoon.geboorteGemeente.naam", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Geboren te", "Geboren te",
			"persoon.geboorteplaats", "deelnemer.persoon.geboorteplaats", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Geboorteland", "Geboorteland",
			"persoon.geboorteland", "deelnemer.persoon.geboorteland.naam", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Nationaliteit 1", "Nationaliteit 1",
			"persoon.nationaliteit1", "deelnemer.persoon.nationaliteit1.naam", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Nationaliteit 2", "Nationaliteit 2",
			"persoon.nationaliteit2", "deelnemer.persoon.nationaliteit2.naam", false)
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Groepsdeelname>("Burgerservicenummer (sofinummer)",
			"BSN", "persoon.bsn", "deelnemer.persoon.bsn", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Onderwijsnummer", "Onderwijsnummer",
			"deelnemer.onderwijsnummer", "deelnemer.onderwijsnummer", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Debiteurennummer", "Debiteurennummer",
			"deelnemer.persoon.debiteurennummer", "deelnemer.persoon.debiteurennummer", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Datum in Nederland", "Datum in NL",
			"persoon.datumInNederland", "deelnemer.persoon.datumInNederland", false)
			.setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Groepsdeelname>("Allochtoon", "Allochtoon",
			"deelnemer.allochtoon", "deelnemer.allochtoon", false).setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Groepsdeelname>("Leerlinggebonden financiering (LGF)",
			"LGF", "deelnemer.lgf", "deelnemer.lgf", false).setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<Groepsdeelname>("Inburgeraar", "Inburgeraar",
			"persoon.nieuwkomer", "deelnemer.persoon.nieuwkomer", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Leeftijd", "Leeftijd",
			"persoon.geboortedatum", "deelnemer.persoon.leeftijd", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Datum overlijden", "Datum overlijden",
			"persoon.datumOverlijden", "deelnemer.persoon.datumOverlijden", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Registratie datum",
			"Registratie datum", "deelnemer.registratieDatum", "deelnemer.registratieDatum", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Opleiding", "Opleiding",
			"deelnemer.verbintenisOpPeildatumOfToekomst.opleiding", false).setDefaultVisible(false));
	}

	private void createGroepColumns()
	{
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Groepscode", "Groepscode", "groep.code")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Groepsnaam", "Groepsnaam", "groep.naam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Organisatie-eenheid",
			"Organisatie-eenheid", "organisatieEenheid.naam", "groep.organisatieEenheid.naam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Locatie", "Locatie", "locatie.naam",
			"groep.locatie.naam").setDefaultVisible(false));
	}

	private void createAdresColumns()
	{
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Straat en huisnummer (woonadres)",
			"Woonadres", "deelnemer.persoon.fysiekAdres.adres.straatHuisnummerFormatted", false)
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Groepsdeelname>("Postcode en plaats (woonadres)",
			"(W)Postcode en plaats", "deelnemer.persoon.fysiekAdres.adres.postcodePlaatsFormatted",
			false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Straat en huisnummer (postadres)",
			"Postadres", "deelnemer.persoon.postAdres.adres.straatHuisnummerFormatted", false)
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Groepsdeelname>("Postcode en plaats (postadres)",
			"(P)Postcode en plaats", "deelnemer.persoon.postAdres.adres.postcodePlaatsFormatted",
			false).setDefaultVisible(false));
	}

	private void createContactgegevenColumns()
	{
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Telefoonnummer", "Telefoon",
			"deelnemer.persoon.eersteTelefoon.formattedContactgegeven", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Email-adres", "E-mail",
			"deelnemer.persoon.eersteEmailAdres.formattedContactgegeven", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Homepage", "Homepage",
			"deelnemer.persoon.eersteHomepage.formattedContactgegeven", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Telefoonnummer mobiel", "Mobiel",
			"deelnemer.persoon.eersteMobieltelefoon.formattedContactgegeven", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Contactgegeven overig",
			"Contactgegeven", "deelnemer.persoon.eersteOverig.formattedContactgegeven", false)
			.setDefaultVisible(false));
	}

	private void createKenmerkenColumns()
	{
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Kenmerken", "Kenmerken",
			"deelnemer.kenmerkNamen").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Kenmerkcategorien",
			"Kenmerkcategorien", "deelnemer.kenmerkCategorien").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Kenmerktoelichting",
			"Kenmerktoelichting", "deelnemer.kenmerkToelichtingen").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Begindatum kenmerk",
			"Begindatum kenmerk", "deelnemer.begindatumKenmerken").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Einddatum kenmerk",
			"Einddatum kenmerk", "deelnemer.einddatumKenmerken").setDefaultVisible(false));
	}

	private void createRelatieColumns()
	{
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Naam eerste verzorger",
			"Naam eerste verzorger", "deelnemer.eersteVerzorger.relatie.volledigeNaam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("BSN eerste verzorger",
			"BSN eerste verzorger", "deelnemer.eersteVerzorger.relatie.bsn")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Debiteurennummer eerste verzorger",
			"Debiteurennummer eerste verzorger",
			"deelnemer.eersteVerzorger.relatie.debiteurennummer").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Relatiesoort eerste verzorger",
			"Relatiesoort eerste verzorger", "deelnemer.eersteVerzorger.relatieSoort.naam")
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Groepsdeelname>("Naam tweede verzorger",
			"Naam tweede verzorger", "deelnemer.tweedeVerzorger.relatie.volledigeNaam")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("BSN tweede verzorger",
			"BSN tweede verzorger", "deelnemer.tweedeVerzorger.relatie.bsn")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Debiteurennummer tweede verzorger",
			"Debiteurennummer tweede verzorger",
			"deelnemer.tweedeVerzorger.relatie.debiteurennummer").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Relatiesoort tweede verzorger",
			"Relatiesoort tweede verzorger", "deelnemer.tweedeVerzorger.relatieSoort.naam")
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Groepsdeelname>("Woonadres eerste verzorger",
			"Woonadres eerste verzorger",
			"deelnemer.eersteVerzorger.relatie.fysiekAdres.adres.straatHuisnummerFormatted")
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Groepsdeelname>("Woonplaats eerste verzorger",
			"Woonplaats eerste verzorger",
			"deelnemer.eersteVerzorger.relatie.fysiekAdres.adres.postcodePlaatsFormatted", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Postadres eerste verzorger",
			"Postadres eerste verzorger",
			"deelnemer.eersteVerzorger.relatie.postAdres.adres.straatHuisnummerFormatted", false)
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Groepsdeelname>("Postplaats eerste verzorger",
			"Postplaats eerste verzorger",
			"deelnemer.eersteVerzorger.relatie.postAdres.adres.postcodePlaatsFormatted", false)
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Groepsdeelname>("Woonadres tweede verzorger",
			"Woonadres tweede verzorger",
			"deelnemer.tweedeVerzorger.relatie.fysiekAdres.adres.straatHuisnummerFormatted")
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Groepsdeelname>("Woonplaats tweede verzorger",
			"Woonplaats tweede verzorger",
			"deelnemer.tweedeVerzorger.relatie.fysiekAdres.adres.postcodePlaatsFormatted", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groepsdeelname>("Postadres tweede verzorger",
			"Postadres tweede verzorger",
			"deelnemer.tweedeVerzorger.relatie.postAdres.adres.straatHuisnummerFormatted", false)
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<Groepsdeelname>("Postplaats tweede verzorger",
			"Postplaats tweede verzorger",
			"deelnemer.tweedeVerzorger.relatie.postAdres.adres.postcodePlaatsFormatted", false)
			.setDefaultVisible(false));
	}

	private void createGroupProperties(boolean showGroepProperties)
	{
		if (showGroepProperties)
		{
			addGroupProperty(new GroupProperty<Groepsdeelname>("groep", "Groep", "groep.code", true));
		}
	}

	/**
	 * Lege functie om een {@link Groepsdeelname} te verwijderen uit de page model.
	 * 
	 * @param groepsdeelname
	 * @param target
	 */
	public void deleteGroepsdeelname(Groepsdeelname groepsdeelname, AjaxRequestTarget target)
	{

	}
}
