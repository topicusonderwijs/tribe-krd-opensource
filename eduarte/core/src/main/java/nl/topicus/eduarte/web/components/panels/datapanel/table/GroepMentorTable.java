package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyDateFieldColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.cobra.web.components.panels.datapanel.columns.AjaxDeleteColumn;
import nl.topicus.eduarte.entities.groep.GroepMentor;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class GroepMentorTable extends CustomDataPanelContentDescription<GroepMentor>
{

	private static final long serialVersionUID = 1L;

	/**
	 * Maakt een kolommen lijst voor een GroepDocent.
	 * 
	 * @param editMode
	 *            geeft aan of er een verwijderen, bewerken etc kolom bij moet komen.
	 */
	public GroepMentorTable(boolean editMode)
	{
		super("Mentor(en)");
		createColumns(editMode);
	}

	/**
	 * Maakt een kolommen lijst voor een GroepDocent. Verwijderen, bewerken etc kolom
	 * wordt verborgen.
	 */
	public GroepMentorTable()
	{
		this(false);
	}

	private void createColumns(boolean editMode)
	{
		addColumn(new CustomPropertyColumn<GroepMentor>("Afkorting", "Afkorting",
			"medewerker.afkorting", "medewerker.afkorting"));
		addColumn(new CustomPropertyColumn<GroepMentor>("Naam", "Naam",
			"medewerker.persoon.volledigeNaam", "medewerker.persoon.volledigeNaam"));
		if (editMode)
		{
			addColumn(new CustomPropertyDateFieldColumn<GroepMentor>("Begindatum", "Begindatum",
				"begindatum", "begindatum").setRequired(true));
			addColumn(new CustomPropertyDateFieldColumn<GroepMentor>("Einddatum", "Einddatum",
				"einddatum", "einddatum"));
			addColumn(new AjaxDeleteColumn<GroepMentor>("delete", "Verwijder")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(WebMarkupContainer item, IModel<GroepMentor> rowModel,
						AjaxRequestTarget target)
				{
					GroepMentor groepMentor = rowModel.getObject();
					GroepMentorTable.this.deleteGroepMentor(groepMentor, target);
				}

			}.setPositioning(Positioning.FIXED_RIGHT));
		}
		else
		{
			addColumn(new DatePropertyColumn<GroepMentor>("begindatum", "Begindatum", "begindatum",
				"begindatum"));
			addColumn(new DatePropertyColumn<GroepMentor>("einddatum", "Einddatum", "einddatum",
				"einddatum"));
		}
	}

	/**
	 * Lege functie om een {@link GroepMentor} te verwijderen uit de page model.
	 * 
	 * @param groepMentor
	 * @param target
	 */
	public void deleteGroepMentor(GroepMentor groepMentor, AjaxRequestTarget target)
	{

	}
}
