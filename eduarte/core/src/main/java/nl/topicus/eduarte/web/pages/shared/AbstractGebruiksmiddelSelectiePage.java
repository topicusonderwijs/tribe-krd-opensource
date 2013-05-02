package nl.topicus.eduarte.web.pages.shared;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.eduarte.entities.onderwijsproduct.Gebruiksmiddel;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.GebruiksmiddelSelectiePanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.GebruiksmiddelZoekFilter;

@PageInfo(title = "Verbruiksmiddel(en) selecteren", menu = "verschillende paden")
public abstract class AbstractGebruiksmiddelSelectiePage extends
		AbstractSelectiePage<Gebruiksmiddel, Gebruiksmiddel, GebruiksmiddelZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public AbstractGebruiksmiddelSelectiePage(SecurePage returnPage,
			GebruiksmiddelZoekFilter filter,
			DatabaseSelection<Gebruiksmiddel, Gebruiksmiddel> selection,
			SelectieTarget<Gebruiksmiddel, Gebruiksmiddel> target)
	{
		super(returnPage, filter, selection, target);
	}

	@Override
	protected AbstractSelectiePanel<Gebruiksmiddel, Gebruiksmiddel, GebruiksmiddelZoekFilter> createSelectiePanel(
			String id, GebruiksmiddelZoekFilter filter,
			Selection<Gebruiksmiddel, Gebruiksmiddel> selection)
	{
		return new GebruiksmiddelSelectiePanel(id, filter,
			(DatabaseSelection<Gebruiksmiddel, Gebruiksmiddel>) selection);
	}
}
