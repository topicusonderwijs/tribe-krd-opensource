package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.personen.AbstractRelatie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.web.components.datapanel.PostcodeWoonplaatsColumn;

/**
 * Tabel met de mogelijke kolommen voor een relatie.
 * 
 * @author idserda
 */
public class RelatieTable<T extends AbstractRelatie> extends AbstractVrijVeldableTable<T>
{
	private static final long serialVersionUID = 1L;

	public RelatieTable(boolean meerderjarig)
	{
		super("Relaties");
		createColumns(meerderjarig);
		createContactgegevenColumns();
		createVrijVeldKolommen(VrijVeldCategorie.RELATIE, "relatie");
	}

	private void createColumns(boolean meerderjarig)
	{
		addColumn(new CustomPropertyColumn<T>("Naam", "Naam", "naam"));
		addColumn(new CustomPropertyColumn<T>("Relatie", "Relatie", "relatieSoort"));
		addColumn(new CustomPropertyColumn<T>("Geslacht", "Geslacht", "geslacht"));
		addColumn(new CustomPropertyColumn<T>("Adres", "Adres",
			"eersteAdresOpPeildatum.adres.straatHuisnummerFormatted"));
		addColumn(new PostcodeWoonplaatsColumn<T>("Plaats", "Plaats",
			"eersteAdresOpPeildatum.adres.postcodePlaatsFormatted"));
		if (!meerderjarig)
		{
			addColumn(new CustomPropertyColumn<T>("Wettelijk", "Wettelijk",
				"wettelijkeVertegenwoordigerOmschrijving"));
			addColumn(new CustomPropertyColumn<T>("Betalingsplichtige", "Betalingsplichtige",
				"betalingsplichtigeOmschrijving"));
		}
	}

	private void createContactgegevenColumns()
	{
		addColumn(new CustomPropertyColumn<T>("Telefoonnummer", "Telefoon",
			"relatie.eersteTelefoon.formattedContactgegeven", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("E-mailadres", "E-mail",
			"relatie.eersteEmailAdres.formattedContactgegeven", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Homepage", "Homepage",
			"relatie.eersteHomepage.formattedContactgegeven", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Telefoonnummer mobiel", "Mobiel",
			"relatie.eersteMobieltelefoon.formattedContactgegeven", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Contactgegeven overig", "Contactgegeven",
			"relatie.eersteOverig.formattedContactgegeven", false).setDefaultVisible(false));
	}
}
