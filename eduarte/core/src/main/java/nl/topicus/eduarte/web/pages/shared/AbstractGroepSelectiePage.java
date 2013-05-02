package nl.topicus.eduarte.web.pages.shared;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.GroepSelectiePanel;
import nl.topicus.eduarte.web.pages.PageContext;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;

import org.apache.wicket.Page;

@PageInfo(title = "Groep(en) selecteren", menu = "verschillende paden")
public abstract class AbstractGroepSelectiePage<R extends IdObject> extends
		AbstractSelectiePage<R, Groep, GroepZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public AbstractGroepSelectiePage(SecurePage returnPage, GroepZoekFilter filter,
			DatabaseSelection<R, Groep> selection, SelectieTarget<R, Groep> target)
	{
		super(returnPage, filter, selection, target);
	}

	public AbstractGroepSelectiePage(Class< ? extends Page> returnPage, PageContext context,
			GroepZoekFilter filter, DatabaseSelection<R, Groep> selection,
			SelectieTarget<R, Groep> target)
	{
		super(returnPage, context, filter, selection, target);
	}

	@Override
	protected AbstractSelectiePanel<R, Groep, GroepZoekFilter> createSelectiePanel(String id,
			GroepZoekFilter filter, Selection<R, Groep> selection)
	{
		return new GroepSelectiePanel<R>(id, filter, (DatabaseSelection<R, Groep>) selection);
	}
}
