package nl.topicus.eduarte.resultaten.web.pages.deelnemer;

import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.selection.CheckboxSelectionColumn;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.EduArteDatabaseSelectiePanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ResultaatstructuurTable;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

public class ResultaatstructuurSelectiePanel
		extends
		EduArteDatabaseSelectiePanel<Resultaatstructuur, Resultaatstructuur, ResultaatstructuurZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public ResultaatstructuurSelectiePanel(String id, ResultaatstructuurZoekFilter filter,
			HibernateSelection<Resultaatstructuur> selection)
	{
		super(id, filter, ResultaatstructuurDataAccessHelper.class, selection);
	}

	@Override
	protected CustomDataPanelContentDescription<Resultaatstructuur> createContentDescription()
	{
		return new ResultaatstructuurTable();
	}

	@Override
	protected CheckboxSelectionColumn<Resultaatstructuur, Resultaatstructuur> createSelectionColumn()
	{
		return new CheckboxSelectionColumn<Resultaatstructuur, Resultaatstructuur>("Bevroren",
			"Bevroren", getSelection())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass()
			{
				return "unit_40";
			}
		};
	}

	@Override
	protected Panel createZoekFilterPanel(String id, ResultaatstructuurZoekFilter filter,
			CustomDataPanel<Resultaatstructuur> customDataPanel)
	{
		Panel ret = new EmptyPanel(id);
		ret.setVisible(false);
		return ret;
	}

	@Override
	protected String getEntityName()
	{
		return "resultaatstructuren";
	}
}
