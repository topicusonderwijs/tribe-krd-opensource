package nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.web.components.panels.filter.ProductregelZoekFilterPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.EduArteDatabaseSelectiePanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ProductregelTable;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;

import org.apache.wicket.markup.html.panel.Panel;

public class ProductregelSelectiePanel extends
		EduArteDatabaseSelectiePanel<Productregel, Productregel, ProductregelZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public ProductregelSelectiePanel(String id, ProductregelZoekFilter filter,
			DatabaseSelection<Productregel, Productregel> selection)
	{
		super(id, filter, ProductregelDataAccessHelper.class, selection);
	}

	@Override
	protected CustomDataPanelContentDescription<Productregel> createContentDescription()
	{
		return new ProductregelTable();
	}

	@Override
	protected Panel createZoekFilterPanel(String id, ProductregelZoekFilter filter,
			CustomDataPanel<Productregel> customDataPanel)
	{
		return new ProductregelZoekFilterPanel(id, filter, customDataPanel);
	}

	@Override
	protected String getEntityName()
	{
		return "productregels";
	}
}
