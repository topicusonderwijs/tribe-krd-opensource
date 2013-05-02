package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.AjaxRadioColumn;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;

public class BekostigingsperiodeSelecteerTable extends
		CustomDataPanelContentDescription<Bekostigingsperiode>
{
	private static final long serialVersionUID = 1L;

	public BekostigingsperiodeSelecteerTable()
	{
		super("Bekostigingsperiodes");

		addColumn(new AjaxRadioColumn<Bekostigingsperiode>("selectie", "Selecteer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass()
			{
				return "unit_60";
			}
		});
		addColumn(new CustomPropertyColumn<Bekostigingsperiode>("Van", "Van", "begindatum"));
		addColumn(new CustomPropertyColumn<Bekostigingsperiode>("Tot", "Tot", "einddatum"));
		addColumn(new BooleanPropertyColumn<Bekostigingsperiode>("Bekostigd", "Bekostigd",
			"begindatum", "bekostigd"));
	}
}
