package nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.krd.dao.helpers.ExamendeelnameDataAccessHelper;
import nl.topicus.eduarte.krd.web.components.panels.filter.ExamendeelnameZoekFilterPanel;
import nl.topicus.eduarte.krd.zoekfilters.ExamendeelnameZoekFilter;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.EduArteDatabaseSelectiePanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerExamendeelnameTable;

import org.apache.wicket.markup.html.panel.Panel;

public class ExamendeelnameSelectiePanel extends
		EduArteDatabaseSelectiePanel<Examendeelname, Examendeelname, ExamendeelnameZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public ExamendeelnameSelectiePanel(String id, ExamendeelnameZoekFilter filter,
			DatabaseSelection<Examendeelname, Examendeelname> selection)
	{
		super(id, filter, ExamendeelnameDataAccessHelper.class, selection);
	}

	@Override
	protected CustomDataPanelContentDescription<Examendeelname> createContentDescription()
	{
		return new DeelnemerExamendeelnameTable();
	}

	@Override
	protected Panel createZoekFilterPanel(String id, ExamendeelnameZoekFilter filter,
			CustomDataPanel<Examendeelname> customDataPanel)
	{
		return new ExamendeelnameZoekFilterPanel(id, filter, customDataPanel);
	}

	@Override
	protected String getEntityName()
	{
		return "examendeelnames";
	}
}
