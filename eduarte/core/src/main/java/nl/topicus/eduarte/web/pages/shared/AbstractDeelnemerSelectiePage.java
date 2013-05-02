package nl.topicus.eduarte.web.pages.shared;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.DeelnemerSelectiePanel;
import nl.topicus.eduarte.web.pages.PageContext;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Page;

@PageInfo(title = "Deelnemer(s) selecteren", menu = "verschillende paden")
public abstract class AbstractDeelnemerSelectiePage<R extends IdObject> extends
		AbstractSelectiePage<R, Verbintenis, VerbintenisZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public AbstractDeelnemerSelectiePage(SecurePage returnPage, VerbintenisZoekFilter filter,
			DatabaseSelection<R, Verbintenis> selection, SelectieTarget<R, Verbintenis> target)
	{
		super(returnPage, filter, selection, target);
	}

	public AbstractDeelnemerSelectiePage(Class< ? extends Page> returnPage, PageContext context,
			VerbintenisZoekFilter filter, DatabaseSelection<R, Verbintenis> selection,
			SelectieTarget<R, Verbintenis> target)
	{
		super(returnPage, context, filter, selection, target);
	}

	@Override
	protected AbstractSelectiePanel<R, Verbintenis, VerbintenisZoekFilter> createSelectiePanel(
			String id, VerbintenisZoekFilter filter, Selection<R, Verbintenis> selection)
	{
		return new DeelnemerSelectiePanel<R>(id, filter,
			(DatabaseSelection<R, Verbintenis>) selection);
	}

}
