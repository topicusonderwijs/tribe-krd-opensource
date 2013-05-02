package nl.topicus.eduarte.web.pages.shared;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.OnderwijsproductSelectiePanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;

@PageInfo(title = "Onderwijsproduct(en) selecteren", menu = "verschillende paden")
public abstract class AbstractOnderwijsproductSelectiePage extends
		AbstractSelectiePage<Onderwijsproduct, Onderwijsproduct, OnderwijsproductZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public AbstractOnderwijsproductSelectiePage(SecurePage returnPage,
			OnderwijsproductZoekFilter filter,
			DatabaseSelection<Onderwijsproduct, Onderwijsproduct> selection,
			SelectieTarget<Onderwijsproduct, Onderwijsproduct> target)
	{
		super(returnPage, filter, selection, target);
	}

	@Override
	protected AbstractSelectiePanel<Onderwijsproduct, Onderwijsproduct, OnderwijsproductZoekFilter> createSelectiePanel(
			String id, OnderwijsproductZoekFilter filter,
			Selection<Onderwijsproduct, Onderwijsproduct> selection)
	{
		return new OnderwijsproductSelectiePanel(id, filter,
			(DatabaseSelection<Onderwijsproduct, Onderwijsproduct>) selection);
	}
}
