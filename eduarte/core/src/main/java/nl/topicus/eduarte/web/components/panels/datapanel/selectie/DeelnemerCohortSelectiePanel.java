package nl.topicus.eduarte.web.components.panels.datapanel.selectie;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.panels.filter.DeelnemerCohortSelectieZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.panel.Panel;

public class DeelnemerCohortSelectiePanel<R extends IdObject> extends DeelnemerSelectiePanel<R>
{
	private static final long serialVersionUID = 1L;

	private boolean cohortEnOpleidingRequired;

	public DeelnemerCohortSelectiePanel(String id, VerbintenisZoekFilter filter,
			DatabaseSelection<R, Verbintenis> selection, boolean cohortEnOpleidingRequired)
	{
		super(id, filter, selection);
		this.cohortEnOpleidingRequired = cohortEnOpleidingRequired;
	}

	@Override
	protected Panel createZoekFilterPanel(String id, VerbintenisZoekFilter filter,
			CustomDataPanel<Verbintenis> customDataPanel)
	{
		return new DeelnemerCohortSelectieZoekFilterPanel(id, filter, customDataPanel,
			cohortEnOpleidingRequired);
	}
}
