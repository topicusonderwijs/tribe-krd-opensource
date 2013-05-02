package nl.topicus.eduarte.web.components.panels.datapanel.selectie;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OnderwijsproductTable;
import nl.topicus.eduarte.web.components.panels.filter.OnderwijsproductZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;

import org.apache.wicket.markup.html.panel.Panel;

public class OnderwijsproductSelectiePanel
		extends
		EduArteDatabaseSelectiePanel<Onderwijsproduct, Onderwijsproduct, OnderwijsproductZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductSelectiePanel(String id, OnderwijsproductZoekFilter filter,
			DatabaseSelection<Onderwijsproduct, Onderwijsproduct> selection)
	{
		super(id, filter, OnderwijsproductDataAccessHelper.class, selection);
	}

	@Override
	protected CustomDataPanelContentDescription<Onderwijsproduct> createContentDescription()
	{
		return new OnderwijsproductTable(true);
	}

	@Override
	protected Panel createZoekFilterPanel(String id, OnderwijsproductZoekFilter filter,
			CustomDataPanel<Onderwijsproduct> customDataPanel)
	{
		return new OnderwijsproductZoekFilterPanel(id, filter, customDataPanel);
	}

	@Override
	protected String getEntityName()
	{
		return "onderwijsproducten";
	}
}
