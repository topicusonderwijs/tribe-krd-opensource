package nl.topicus.eduarte.web.components.panels.datapanel.selectie;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.dao.helpers.GebruiksmiddelDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Gebruiksmiddel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.GebruiksmiddelZoekFilter;

import org.apache.wicket.markup.html.panel.Panel;

public class GebruiksmiddelSelectiePanel extends
		EduArteDatabaseSelectiePanel<Gebruiksmiddel, Gebruiksmiddel, GebruiksmiddelZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public GebruiksmiddelSelectiePanel(String id, GebruiksmiddelZoekFilter filter,
			DatabaseSelection<Gebruiksmiddel, Gebruiksmiddel> selection)
	{
		super(id, filter, GebruiksmiddelDataAccessHelper.class, selection);
	}

	@Override
	protected CustomDataPanelContentDescription<Gebruiksmiddel> createContentDescription()
	{
		return new CodeNaamActiefTable<Gebruiksmiddel>("Gebruiksmiddelen");
	}

	@Override
	protected Panel createZoekFilterPanel(String id, GebruiksmiddelZoekFilter filter,
			CustomDataPanel<Gebruiksmiddel> customDataPanel)
	{
		return new CodeNaamActiefZoekFilterPanel(id, filter, customDataPanel);
	}

	@Override
	protected String getEntityName()
	{
		return "gebruiksmiddelen";
	}
}
