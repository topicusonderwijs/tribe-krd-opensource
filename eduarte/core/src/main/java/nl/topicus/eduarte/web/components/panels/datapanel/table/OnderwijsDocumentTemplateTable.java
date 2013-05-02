package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.rapportage.OnderwijsDocumentTemplate;

/**
 * Tabel met de mogelijke kolommen voor documenttemplates.
 * 
 * @author hoeve
 */
public class OnderwijsDocumentTemplateTable extends
		CustomDataPanelContentDescription<OnderwijsDocumentTemplate>
{
	private static final long serialVersionUID = 1L;

	public OnderwijsDocumentTemplateTable()
	{
		super("Onderwijs samenvoegdocumenten");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<OnderwijsDocumentTemplate>("Omschrijving",
			"Omschrijving", "omschrijving", "omschrijving"));
		addColumn(new CustomPropertyColumn<OnderwijsDocumentTemplate>("Bestandsnaam",
			"Bestandsnaam", "bestandsnaam", "bestandsnaam"));
		addColumn(new CustomPropertyColumn<OnderwijsDocumentTemplate>("Uitvoerformaat",
			"Uitvoerformaat", "uitvoerFormaat"));
		addColumn(new CustomPropertyColumn<OnderwijsDocumentTemplate>("Categorie", "Categorie",
			"categorie", "categorie"));
		addColumn(new CustomPropertyColumn<OnderwijsDocumentTemplate>("Taxonomie", "Taxonomie",
			"taxonomie", "taxonomie"));
		addColumn(new CustomPropertyColumn<OnderwijsDocumentTemplate>("Documenttype",
			"Documenttype", "examenDocumentType", "examenDocumentType"));
		addColumn(new BooleanPropertyColumn<OnderwijsDocumentTemplate>("Valide", "Valide", "valid",
			"valid"));
	}
}
