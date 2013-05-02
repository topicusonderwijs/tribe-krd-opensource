package nl.topicus.eduarte.web.pages.shared;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.eduarte.entities.onderwijsproduct.Verbruiksmiddel;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.VerbruiksmiddelSelectiePanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.VerbruiksmiddelZoekFilter;

@PageInfo(title = "Verbruiksmiddel(en) selecteren", menu = "verschillende paden")
public abstract class AbstractVerbruiksmiddelSelectiePage extends
		AbstractSelectiePage<Verbruiksmiddel, Verbruiksmiddel, VerbruiksmiddelZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public AbstractVerbruiksmiddelSelectiePage(SecurePage returnPage,
			VerbruiksmiddelZoekFilter filter,
			DatabaseSelection<Verbruiksmiddel, Verbruiksmiddel> selection,
			SelectieTarget<Verbruiksmiddel, Verbruiksmiddel> target)
	{
		super(returnPage, filter, selection, target);
	}

	@Override
	protected AbstractSelectiePanel<Verbruiksmiddel, Verbruiksmiddel, VerbruiksmiddelZoekFilter> createSelectiePanel(
			String id, VerbruiksmiddelZoekFilter filter,
			Selection<Verbruiksmiddel, Verbruiksmiddel> selection)
	{
		return new VerbruiksmiddelSelectiePanel(id, filter,
			(DatabaseSelection<Verbruiksmiddel, Verbruiksmiddel>) selection);
	}
}
