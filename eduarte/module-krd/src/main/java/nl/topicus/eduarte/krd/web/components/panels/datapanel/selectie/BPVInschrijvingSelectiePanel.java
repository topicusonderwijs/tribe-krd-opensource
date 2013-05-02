package nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.dao.helpers.BPVInschrijvingDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.EduArteDatabaseSelectiePanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVInschrijvingTable;
import nl.topicus.eduarte.web.components.panels.filter.BPVInschrijvingZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.BPVInschrijvingZoekFilter;

import org.apache.wicket.markup.html.panel.Panel;

public class BPVInschrijvingSelectiePanel extends
		EduArteDatabaseSelectiePanel<BPVInschrijving, BPVInschrijving, BPVInschrijvingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public BPVInschrijvingSelectiePanel(String id, BPVInschrijvingZoekFilter filter,
			DatabaseSelection<BPVInschrijving, BPVInschrijving> selection)
	{
		super(id, filter, BPVInschrijvingDataAccessHelper.class, selection);
	}

	@Override
	protected CustomDataPanelContentDescription<BPVInschrijving> createContentDescription()
	{
		return new BPVInschrijvingTable()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean isNaamDefaultVisible()
			{
				return true;
			}
		};
	}

	@Override
	protected Panel createZoekFilterPanel(String id, BPVInschrijvingZoekFilter filter,
			CustomDataPanel<BPVInschrijving> customDataPanel)
	{
		return new BPVInschrijvingZoekFilterPanel(id, filter, customDataPanel);
	}

	@Override
	protected String getEntityName()
	{
		return "examendeelnames";
	}
}
