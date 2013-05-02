package nl.topicus.eduarte.web.pages.shared;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.MedewerkerSelectiePanel;
import nl.topicus.eduarte.web.pages.PageContext;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;

import org.apache.wicket.Page;

@PageInfo(title = "Medewerker(s) selecteren", menu = "verschillende paden")
public abstract class AbstractMedewerkerSelectiePage<R extends IdObject> extends
		AbstractSelectiePage<R, Medewerker, MedewerkerZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public AbstractMedewerkerSelectiePage(SecurePage returnPage, MedewerkerZoekFilter filter,
			DatabaseSelection<R, Medewerker> selection, SelectieTarget<R, Medewerker> target)
	{
		super(returnPage, filter, selection, target);
	}

	public AbstractMedewerkerSelectiePage(Class< ? extends Page> returnPage, PageContext context,
			MedewerkerZoekFilter filter, DatabaseSelection<R, Medewerker> selection,
			SelectieTarget<R, Medewerker> target)
	{
		super(returnPage, context, filter, selection, target);
	}

	@Override
	protected AbstractSelectiePanel<R, Medewerker, MedewerkerZoekFilter> createSelectiePanel(
			String id, MedewerkerZoekFilter filter, Selection<R, Medewerker> selection)
	{
		return new MedewerkerSelectiePanel<R>(id, filter,
			(DatabaseSelection<R, Medewerker>) selection);
	}
}
