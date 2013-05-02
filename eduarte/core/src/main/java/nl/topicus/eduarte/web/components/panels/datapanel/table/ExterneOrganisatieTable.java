package nl.topicus.eduarte.web.components.panels.datapanel.table;

import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.dao.helpers.SoortContactgegevenDataAccessHelper;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.adres.StandaardContactgegeven;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.ContactgegevenColumn;

/**
 * @author vandekamp
 */
public class ExterneOrganisatieTable extends AbstractVrijVeldableTable<ExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	public ExterneOrganisatieTable()
	{
		super("Externe organisaties");
		createColumns();
		createVrijVeldKolommen(VrijVeldCategorie.EXTERNEORGANISATIE, "");
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<ExterneOrganisatie>("Naam", "Naam", "naam", "naam"));

		addColumn(new CustomPropertyColumn<ExterneOrganisatie>("Verkorte naam", "Verkorte naam",
			"verkorteNaam", "verkorteNaam").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<ExterneOrganisatie>("Debiteurennummer",
			"Debiteurennummer", "debiteurennummer", "debiteurennummer").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<ExterneOrganisatie>("Soort", "Soort",
			"soortExterneOrganisatie", "soortExterneOrganisatie"));

		addColumn(new CustomPropertyColumn<ExterneOrganisatie>("Straat en huisnummer", "Adres",
			"fysiekAdres.adres.straatHuisnummer"));
		addColumn(new CustomPropertyColumn<ExterneOrganisatie>("Postcode", "Postcode",
			"fysiekAdres.adres.postcode").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<ExterneOrganisatie>("Plaats", "Plaats",
			"fysiekAdres.adres.plaats"));

		addColumn(new BooleanPropertyColumn<ExterneOrganisatie>("BPV Bedrijf", "BPV Bedrijf",
			"bpvBedrijf", "bpvBedrijf").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<ExterneOrganisatie>("BPV Bedrijf kenniscentrum",
			"BPV Bedrijf kenniscentrum", "BPVBedrijvenKenniscentrumFormatted")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<ExterneOrganisatie>("BPV Bedrijf code leerbedrijf",
			"BPV Bedrijf code leerbedrijf", "BPVBedrijvenCodeLeerbedrijfFormatted")
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<ExterneOrganisatie>("Begindatum", "Begindatum",
			"begindatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<ExterneOrganisatie>("Einddatum", "Einddatum",
			"einddatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<ExterneOrganisatie>("Relatienr", "Relatienr",
			"relatienummers").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<ExterneOrganisatie>("Stagemarkt nog controleren",
			"Stagemarkt nog controleren", "nogControleren").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<ExterneOrganisatie>("Stagemarkt controleresultaat",
			"Stagemarkt controleresultaat", "controleResultaat").setDefaultVisible(false));

		List<SoortContactgegeven> soorten =
			DataAccessRegistry.getHelper(SoortContactgegevenDataAccessHelper.class).list(
				Arrays.asList(StandaardContactgegeven.StandaardTonenBijOrganisatie,
					StandaardContactgegeven.StandaardTonen), true);
		for (SoortContactgegeven soort : soorten)
		{
			addColumn(new ContactgegevenColumn<ExterneOrganisatie>(soort, soort.getNaam(), true)
				.setDefaultVisible(false));
		}

	}
}
