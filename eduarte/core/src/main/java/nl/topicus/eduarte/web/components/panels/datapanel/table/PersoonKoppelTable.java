package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.ButtonColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.web.components.datapanel.PostcodeWoonplaatsColumn;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Tabel voor het koppelen van personen aan deelnemers.
 * 
 * @author loite
 */
public abstract class PersoonKoppelTable extends CustomDataPanelContentDescription<Persoon>
{
	private static final long serialVersionUID = 1L;

	public PersoonKoppelTable()
	{
		super("Relaties");
		createColumns();
		createContactgegevenColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Persoon>("Naam", "Naam", "achternaam", "formeleNaam"));
		addColumn(new CustomPropertyColumn<Persoon>("Geslacht", "Geslacht", "geslacht", "geslacht"));
		addColumn(new CustomPropertyColumn<Persoon>("Adres", "Adres",
			"eerstePersoonAdresOpPeildatum.adres.straatHuisnummer"));
		addColumn(new PostcodeWoonplaatsColumn<Persoon>("Plaats", "Plaats",
			"eerstePersoonAdresOpPeildatum.adres.postcodePlaats"));
		addColumn(new ButtonColumn<Persoon>("koppel", "Koppelen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected String getCssDisabled()
			{
				return "newItem";
			}

			@Override
			protected String getCssEnabled()
			{
				return "newItem";
			}

			@Override
			public void onClick(WebMarkupContainer item, IModel<Persoon> rowModel)
			{
				Persoon persoon = rowModel.getObject();
				onClickKoppelen(persoon);
			}

		});
	}

	private void createContactgegevenColumns()
	{
		addColumn(new CustomPropertyColumn<Persoon>("Telefoonnummer", "Telefoon",
			"eersteTelefoon.contactgegeven", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Persoon>("E-mailadres", "E-mail",
			"eersteEmailAdres.contactgegeven", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Persoon>("Homepage", "Homepage",
			"eersteHomepage.contactgegeven", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Persoon>("Telefoonnummer mobiel", "Mobiel",
			"eersteMobieltelefoon.contactgegeven", false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Persoon>("Contactgegeven overig", "Contactgegeven",
			"eersteOverig.contactgegeven", false).setDefaultVisible(false));
	}

	public abstract void onClickKoppelen(Persoon persoon);
}
