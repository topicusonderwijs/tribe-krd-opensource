package nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.dao.helpers.CriteriumDataAccessHelper;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.krd.web.components.panels.filter.CriteriumZoekFilterPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.EduArteDatabaseSelectiePanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CriteriumTable;
import nl.topicus.eduarte.zoekfilters.CriteriumZoekFilter;

import org.apache.wicket.markup.html.panel.Panel;

public class CriteriumSelectiePanel extends
		EduArteDatabaseSelectiePanel<Criterium, Criterium, CriteriumZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public CriteriumSelectiePanel(String id, CriteriumZoekFilter filter,
			DatabaseSelection<Criterium, Criterium> selection)
	{
		super(id, filter, CriteriumDataAccessHelper.class, selection);
	}

	@Override
	protected CustomDataPanelContentDescription<Criterium> createContentDescription()
	{
		return new CriteriumTable();
	}

	@Override
	protected Panel createZoekFilterPanel(String id, CriteriumZoekFilter filter,
			CustomDataPanel<Criterium> customDataPanel)
	{
		return new CriteriumZoekFilterPanel(id, filter, customDataPanel);
	}

	@Override
	protected String getEntityName()
	{
		return "criteria";
	}
}
