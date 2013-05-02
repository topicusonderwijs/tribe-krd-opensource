package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.ButtonColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.web.components.datapanel.PostcodeWoonplaatsColumn;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Tabel met de mogelijkheid om een ExterneOrgansatie aan een Persoon te koppelen.
 * 
 * @author idserda
 */
public abstract class PersoonExterneOrganisatieKoppelTable extends
		CustomDataPanelContentDescription<PersoonExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	public PersoonExterneOrganisatieKoppelTable()
	{
		super("Externe organisaties");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<PersoonExterneOrganisatie>("Naam", "Naam", "naam"));
		addColumn(new CustomPropertyColumn<PersoonExterneOrganisatie>("Relatie", "Relatie", "soort"));
		addColumn(new CustomPropertyColumn<PersoonExterneOrganisatie>("Adres", "Adres",
			"getEersteAdresOpPeildatum.adres.straatHuisnummer"));
		addColumn(new PostcodeWoonplaatsColumn<PersoonExterneOrganisatie>("Plaats", "Plaats",
			"getEersteAdresOpPeildatum.adres.postcodePlaats"));

		addColumn(new ButtonColumn<PersoonExterneOrganisatie>("koppel", "Koppelen")
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
			public void onClick(WebMarkupContainer item, IModel<PersoonExterneOrganisatie> rowModel)
			{
				PersoonExterneOrganisatie persoonExterneOrganisatie = rowModel.getObject();
				onClickKoppelen(persoonExterneOrganisatie.getRelatie());
			}

		});

	}

	public abstract void onClickKoppelen(ExterneOrganisatie ExterneOrganisatie);

}
