package nl.topicus.eduarte.web.pages.shared;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.OpleidingSelectiePanel;
import nl.topicus.eduarte.web.pages.PageContext;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

import org.apache.wicket.Page;

@PageInfo(title = "Opleiding(en) selecteren", menu = "verschillende paden")
public abstract class AbstractOpleidingSelectiePage extends
		AbstractSelectiePage<Opleiding, Opleiding, OpleidingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public AbstractOpleidingSelectiePage(SecurePage returnPage, OpleidingZoekFilter filter,
			DatabaseSelection<Opleiding, Opleiding> selection,
			SelectieTarget<Opleiding, Opleiding> target)
	{
		super(returnPage, filter, selection, target);
	}

	public AbstractOpleidingSelectiePage(Class< ? extends Page> returnPage, PageContext context,
			OpleidingZoekFilter filter, DatabaseSelection<Opleiding, Opleiding> selection,
			SelectieTarget<Opleiding, Opleiding> target)
	{
		super(returnPage, context, filter, selection, target);
	}

	@Override
	protected AbstractSelectiePanel<Opleiding, Opleiding, OpleidingZoekFilter> createSelectiePanel(
			String id, OpleidingZoekFilter filter, Selection<Opleiding, Opleiding> selection)
	{
		return new OpleidingSelectiePanel(id, filter,
			(DatabaseSelection<Opleiding, Opleiding>) selection);
	}

}
