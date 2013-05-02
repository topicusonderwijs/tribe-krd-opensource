package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Tabel met de mogelijke kolommen voor documenttemplates.
 * 
 * @author hoeve
 */
public class DocumentTemplateTable extends CustomDataPanelContentDescription<DocumentTemplate>
{
	private static final long serialVersionUID = 1L;

	public DocumentTemplateTable()
	{
		super("Samenvoegdocumenten");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<DocumentTemplate>("Omschrijving", "Omschrijving",
			"omschrijving", "omschrijving"));
		addColumn(new CustomPropertyColumn<DocumentTemplate>("Bestandsnaam", "Bestandsnaam",
			"bestandsnaam", "bestandsnaam")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(WebMarkupContainer cell, String componentId,
					WebMarkupContainer row, IModel<DocumentTemplate> rowModel, int span)
			{
				DocumentTemplate myTemplate = rowModel.getObject();

				super.populateItem(cell, componentId, row, rowModel, span);

				if (!myTemplate.isValid())
					cell.add(new SimpleAttributeModifier("class", "spanErrorRight"));
			}
		});
		addColumn(new CustomPropertyColumn<DocumentTemplate>("Uitvoerformaat", "Uitvoerformaat",
			"uitvoerFormaat"));
		addColumn(new CustomPropertyColumn<DocumentTemplate>("Context", "Context", "context",
			"context"));
		addColumn(new CustomPropertyColumn<DocumentTemplate>("Categorie", "Categorie", "categorie",
			"categorie"));
		addColumn(new BooleanPropertyColumn<DocumentTemplate>("Valide", "Valide", "valid", "valid"));
		addColumn(new CustomPropertyColumn<DocumentTemplate>("Documenttype", "Documenttype",
			"documentType.naam", "documentType").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<DocumentTemplate>("Kopie bij context",
			"Kopie bij context", "kopieBijContext", "kopieBijContext").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<DocumentTemplate>("Forceer type", "Forceer type",
			"forceerType", "forceerType").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<DocumentTemplate>("Actief", "Actief", "actief",
			"actief"));
	}
}
