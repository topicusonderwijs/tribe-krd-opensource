package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdrachtRecht;

public class DeelnemerZoekOpdrachtRechtTable extends
		CustomDataPanelContentDescription<DeelnemerZoekOpdrachtRecht>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerZoekOpdrachtRechtTable()
	{
		super("Publiceren voor rollen");
		addColumn(new CustomPropertyColumn<DeelnemerZoekOpdrachtRecht>("Rol", "Rol", "rol.naam",
			"rol.naam"));
	}
}
