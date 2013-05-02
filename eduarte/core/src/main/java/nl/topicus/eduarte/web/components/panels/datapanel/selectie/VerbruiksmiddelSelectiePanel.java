package nl.topicus.eduarte.web.components.panels.datapanel.selectie;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.dao.helpers.VerbruiksmiddelDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Verbruiksmiddel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.VerbruiksmiddelZoekFilter;

import org.apache.wicket.markup.html.panel.Panel;

public class VerbruiksmiddelSelectiePanel extends
		EduArteDatabaseSelectiePanel<Verbruiksmiddel, Verbruiksmiddel, VerbruiksmiddelZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public VerbruiksmiddelSelectiePanel(String id, VerbruiksmiddelZoekFilter filter,
			DatabaseSelection<Verbruiksmiddel, Verbruiksmiddel> selection)
	{
		super(id, filter, VerbruiksmiddelDataAccessHelper.class, selection);
	}

	@Override
	protected CustomDataPanelContentDescription<Verbruiksmiddel> createContentDescription()
	{
		return new CodeNaamActiefTable<Verbruiksmiddel>("Verbruiksmiddelen");
	}

	@Override
	protected Panel createZoekFilterPanel(String id, VerbruiksmiddelZoekFilter filter,
			CustomDataPanel<Verbruiksmiddel> customDataPanel)
	{
		return new CodeNaamActiefZoekFilterPanel(id, filter, customDataPanel);
	}

	@Override
	protected String getEntityName()
	{
		return "verbruiksmiddelen";
	}
}
