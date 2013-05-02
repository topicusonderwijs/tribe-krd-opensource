package nl.topicus.eduarte.krd.web.pages.shared;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie.ProductregelSelectiePanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectiePage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;

@PageInfo(title = "Productregel(s) selecteren", menu = "verschillende paden")
public abstract class AbstractProductregelSelectiePage extends
		AbstractSelectiePage<Productregel, Productregel, ProductregelZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public AbstractProductregelSelectiePage(SecurePage returnPage, ProductregelZoekFilter filter,
			DatabaseSelection<Productregel, Productregel> selection,
			SelectieTarget<Productregel, Productregel> target)
	{
		super(returnPage, filter, selection, target);
	}

	@Override
	protected AbstractSelectiePanel<Productregel, Productregel, ProductregelZoekFilter> createSelectiePanel(
			String id, ProductregelZoekFilter filter,
			Selection<Productregel, Productregel> selection)
	{
		return new ProductregelSelectiePanel(id, filter,
			(DatabaseSelection<Productregel, Productregel>) selection);
	}
}
