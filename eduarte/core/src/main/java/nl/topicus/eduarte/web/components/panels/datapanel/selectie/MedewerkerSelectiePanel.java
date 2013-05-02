package nl.topicus.eduarte.web.components.panels.datapanel.selectie;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.components.panels.datapanel.table.MedewerkerTable;
import nl.topicus.eduarte.web.components.panels.filter.MedewerkerZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;

import org.apache.wicket.markup.html.panel.Panel;

public class MedewerkerSelectiePanel<T> extends
		EduArteDatabaseSelectiePanel<T, Medewerker, MedewerkerZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public MedewerkerSelectiePanel(String id, MedewerkerZoekFilter filter,
			DatabaseSelection<T, Medewerker> selection)
	{
		super(id, filter, MedewerkerDataAccessHelper.class, selection);
	}

	@Override
	protected CustomDataPanelContentDescription<Medewerker> createContentDescription()
	{
		return new MedewerkerTable();
	}

	@Override
	protected Panel createZoekFilterPanel(String id, MedewerkerZoekFilter filter,
			CustomDataPanel<Medewerker> customDataPanel)
	{
		return new MedewerkerZoekFilterPanel(id, filter, customDataPanel, true);
	}

	@Override
	protected String getEntityName()
	{
		return "medewerkers";
	}
}
