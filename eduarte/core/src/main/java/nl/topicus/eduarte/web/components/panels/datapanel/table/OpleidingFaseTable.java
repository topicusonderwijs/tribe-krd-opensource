package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingFase;

public class OpleidingFaseTable extends CustomDataPanelContentDescription<OpleidingFase>
{
	private static final long serialVersionUID = 1L;

	public OpleidingFaseTable()
	{
		this("Fases");
	}

	public OpleidingFaseTable(String title)
	{
		super(title);
		createColumns();
		createGroupProperties();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<OpleidingFase>("Opleidingsvorm", "Opleidingsvorm",
			"opleidingsvorm", "opleidingsvorm").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OpleidingFase>("Hoofdfase", "Hoofdfase", "hoofdfase",
			"hoofdfase"));
		addColumn(new CustomPropertyColumn<OpleidingFase>("Credits", "Credits", "credits",
			"credits"));

	}

	private void createGroupProperties()
	{
		GroupProperty<OpleidingFase> opleidingsvorm =
			new GroupProperty<OpleidingFase>("opleidingsvorm", "Opleidingsvorm", "opleidingsvorm");
		addGroupProperty(opleidingsvorm);
		setDefaultGroupProperty(opleidingsvorm);
		addGroupProperty(new GroupProperty<OpleidingFase>("hoofdfase", "Hoofdfase", "hoofdfase"));
	}
}
