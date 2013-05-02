package nl.topicus.eduarte.web.components.panels.datapanel.table;

import java.util.Date;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyDateFieldColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.cobra.web.components.panels.datapanel.columns.AjaxDeleteColumn;
import nl.topicus.eduarte.entities.groep.GroepDocent;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class GroepDocentTable extends CustomDataPanelContentDescription<GroepDocent>
{

	private static final long serialVersionUID = 1L;

	/**
	 * Maakt een kolommen lijst voor een GroepDocent. Verwijderen, bewerken etc kolom
	 * wordt verborgen.
	 */
	public GroepDocentTable()
	{
		this(false);
	}

	/**
	 * Maakt een kolommen lijst voor een GroepDocent.
	 * 
	 * @param editMode
	 *            geeft aan of er een verwijderen, bewerken etc kolom bij moet komen.
	 */
	public GroepDocentTable(boolean editMode)
	{
		super("Docent(en)");
		createColumns(editMode);
	}

	private void createColumns(boolean editMode)
	{
		addColumn(new CustomPropertyColumn<GroepDocent>("Afkorting", "Afkorting",
			"medewerker.afkorting", "medewerker.afkorting"));
		addColumn(new CustomPropertyColumn<GroepDocent>("Naam", "Naam",
			"medewerker.persoon.volledigeNaam", "medewerker.persoon.volledigeNaam"));
		if (editMode)
		{
			addColumn(new CustomPropertyDateFieldColumn<GroepDocent>("Begindatum", "Begindatum",
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
			addColumn(new CustomPropertyDateFieldColumn<GroepDocent>("Einddatum", "Einddatum",
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
			addColumn(new AjaxDeleteColumn<GroepDocent>("delete", "Verwijder")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(WebMarkupContainer item, IModel<GroepDocent> rowModel,
						AjaxRequestTarget target)
				{
					GroepDocent groepDocent = rowModel.getObject();
					GroepDocentTable.this.deleteGroepDocent(groepDocent, target);
				}

			}.setPositioning(Positioning.FIXED_RIGHT));
		}
		else
		{
			addColumn(new DatePropertyColumn<GroepDocent>("begindatum", "Begindatum", "begindatum",
				"begindatum"));
			addColumn(new DatePropertyColumn<GroepDocent>("einddatum", "Einddatum", "einddatum",
				"einddatum"));
		}

	}

	/**
	 * Lege functie om een {@link GroepDocent} te verwijderen uit de page model.
	 * 
	 * @param groepDocent
	 * @param target
	 */
	public void deleteGroepDocent(GroepDocent groepDocent, AjaxRequestTarget target)
	{

	}
}
