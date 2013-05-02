package nl.topicus.eduarte.web.components.panels.datapanel.selectie;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OpleidingTable;
import nl.topicus.eduarte.web.components.panels.filter.OpleidingZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

import org.apache.wicket.markup.html.panel.Panel;

public class OpleidingSelectiePanel extends
		EduArteDatabaseSelectiePanel<Opleiding, Opleiding, OpleidingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public OpleidingSelectiePanel(String id, OpleidingZoekFilter filter,
			DatabaseSelection<Opleiding, Opleiding> selection)
	{
		super(id, filter, OpleidingDataAccessHelper.class, selection);
	}

	@Override
	protected CustomDataPanelContentDescription<Opleiding> createContentDescription()
	{
		return new OpleidingTable();
	}

	@Override
	protected Panel createZoekFilterPanel(String id, OpleidingZoekFilter filter,
			CustomDataPanel<Opleiding> customDataPanel)
	{
		return new OpleidingZoekFilterPanel(id, filter, customDataPanel);
	}

	@Override
	protected String getEntityName()
	{
		return "opleidingen";
	}
}
