package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateRecht;

public class DocumentTemplateRechtTable extends
		CustomDataPanelContentDescription<DocumentTemplateRecht>
{
	private static final long serialVersionUID = 1L;

	public DocumentTemplateRechtTable()
	{
		super("Publiceren voor rollen");
		addColumn(new CustomPropertyColumn<DocumentTemplateRecht>("Rol", "Rol", "rol.naam",
			"rol.naam"));
		addColumn(new CustomPropertyColumn<DocumentTemplateRecht>("Niveau", "Niveau", "actionName"));
	}
}
