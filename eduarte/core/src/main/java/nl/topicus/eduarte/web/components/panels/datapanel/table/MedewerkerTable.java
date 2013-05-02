package nl.topicus.eduarte.web.components.panels.datapanel.table;

import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.SoortContactgegevenDataAccessHelper;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.adres.StandaardContactgegeven;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.AdresColumn;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.ContactgegevenColumn;

/**
 * Tabel met de mogelijke kolommen voor medewerkers.
 * 
 * @author loite
 */
public class MedewerkerTable extends AbstractVrijVeldableTable<Medewerker>
{
	private static final long serialVersionUID = 1L;

	public MedewerkerTable()
	{
		this(true);
	}

	public MedewerkerTable(boolean wide)
	{
		super("Medewerkers");
		createColumns(wide);
		createVrijVeldKolommen(VrijVeldCategorie.MEDEWERKERPERSONALIA, "persoon");
		createVrijVeldKolommen(VrijVeldCategorie.MEDEWERKERAANSTELLING, "");
	}

	private void createColumns(boolean wide)
	{
		addColumn(new CustomPropertyColumn<Medewerker>("Roepnaam", "Roepnaam", "persoon.roepnaam",
			"persoon.roepnaam"));

		addColumn(new CustomPropertyColumn<Medewerker>("Voorvoegsel", "Voorvoegsel",
			"persoon.voorvoegsel", "persoon.voorvoegsel"));

		addColumn(new CustomPropertyColumn<Medewerker>("Achternaam", "Achternaam",
			"persoon.achternaam", "persoon.achternaam"));

		addColumn(new CustomPropertyColumn<Medewerker>("Afkorting", "Afkorting", "afkorting",
			"afkorting"));

		addColumn(new CustomPropertyColumn<Medewerker>("Voorletters", "Voorletters",
			"persoon.voorletters", "persoon.voorletters").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Medewerker>("Voornamen", "Voornamen",
			"persoon.voornamen", "persoon.voornamen").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Medewerker>("Geslacht", "Geslacht", "persoon.geslacht",
			"persoon.geslacht").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Medewerker>("Gebruikersnaam", "Gebruikersnaam",
			"account.gebruikersnaam").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Medewerker>("Rollen", "Rollen", "account.rolNamen")
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Medewerker>("Organisatieeenheid", "Organisatie-eenheid",
			"actieveOrganisatieEenhedenNamen").setDefaultVisible(wide));

		addColumn(new CustomPropertyColumn<Medewerker>("Locatie", "Locatie", "actieveLocatiesNamen")
			.setDefaultVisible(wide));

		addColumn(new CustomPropertyColumn<Medewerker>("Functie", "Functie", "functie", "functie")
			.setDefaultVisible(wide));

		voegAdresKolommenToe();
		voegContactgegevenKolommenToe();

		addColumn(new CustomPropertyColumn<Medewerker>("Geboortedatum", "Geboortedatum",
			"persoon.geboortedatum", "persoon.geboortedatum").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Medewerker>("Geboortegemeente", "Geboortegemeente",
			"persoon.geboorteGemeente", "persoon.geboorteGemeente.naam").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Medewerker>("Geboorteland", "Geboorteland",
			"persoon.geboorteland.naam").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Medewerker>("Actief", "Actief", "actiefOmschrijving")
			.setDefaultVisible(false));
	}

	private void voegContactgegevenKolommenToe()
	{
		if (EduArteContext.get().getOrganisatie().getRechtenSoort() == RechtenSoort.INSTELLING)
		{
			List<SoortContactgegeven> soorten =
				DataAccessRegistry.getHelper(SoortContactgegevenDataAccessHelper.class).list(
					Arrays.asList(StandaardContactgegeven.StandaardTonenBijPersoon,
						StandaardContactgegeven.StandaardTonen), true);
			for (SoortContactgegeven soort : soorten)
			{
				addColumn(new ContactgegevenColumn<Medewerker>(soort, soort.getNaam(), true)
					.setDefaultVisible(false));
			}
		}
	}

	private void voegAdresKolommenToe()
	{
		if (EduArteContext.get().getOrganisatie().getRechtenSoort() == RechtenSoort.INSTELLING)
		{
			addColumn(new AdresColumn<Medewerker>("Adres", "adres.straatHuisnummer",
				"Straat en huisnummer", true).setDefaultVisible(false));
			addColumn(new AdresColumn<Medewerker>("Plaats", "adres.postcodePlaats",
				"Postcode en plaats", true).setDefaultVisible(false));
		}
	}
}
