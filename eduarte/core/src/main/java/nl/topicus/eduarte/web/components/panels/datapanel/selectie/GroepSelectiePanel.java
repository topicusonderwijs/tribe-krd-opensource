package nl.topicus.eduarte.web.components.panels.datapanel.selectie;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.web.components.panels.datapanel.table.GroepTable;
import nl.topicus.eduarte.web.components.panels.filter.GroepZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;

import org.apache.wicket.markup.html.panel.Panel;

public class GroepSelectiePanel<T> extends EduArteDatabaseSelectiePanel<T, Groep, GroepZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public GroepSelectiePanel(String id, GroepZoekFilter filter,
			DatabaseSelection<T, Groep> selection)
	{
		super(id, filter, GroepDataAccessHelper.class, selection);
	}

	@Override
	protected CustomDataPanelContentDescription<Groep> createContentDescription()
	{
		return new GroepTable();
	}

	@Override
	protected Panel createZoekFilterPanel(String id, GroepZoekFilter filter,
			CustomDataPanel<Groep> customDataPanel)
	{
		return new GroepZoekFilterPanel(id, filter, customDataPanel);
	}

	@Override
	protected String getEntityName()
	{
		return "groepen";
	}
}
