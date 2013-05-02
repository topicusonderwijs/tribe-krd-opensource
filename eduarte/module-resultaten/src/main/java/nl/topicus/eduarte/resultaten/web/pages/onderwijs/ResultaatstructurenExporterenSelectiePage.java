package nl.topicus.eduarte.resultaten.web.pages.onderwijs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.resultaten.principals.onderwijs.OnderwijsproductResultaatstructuurExporteren;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.OnderwijsproductSelectiePanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectiePage;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;

/**
 * @author papegaaij
 */
@PageInfo(title = "Resultaatstructuren exporteren", menu = "Onderwijs > Onderwijsproducten > Exporteren resultaatstructuren")
@InPrincipal(OnderwijsproductResultaatstructuurExporteren.class)
public class ResultaatstructurenExporterenSelectiePage extends
		AbstractSelectiePage<Onderwijsproduct, Onderwijsproduct, OnderwijsproductZoekFilter>
		implements IModuleEditPage<Resultaatstructuur>
{
	private static final long serialVersionUID = 1L;

	private final static OnderwijsproductZoekFilter getDefaultFilter()
	{
		OnderwijsproductZoekFilter ret = new OnderwijsproductZoekFilter();
		ret.addOrderByProperty("titel");
		ret.setCohortModel(EduArteSession.get().getSelectedCohortModel());
		return ret;
	}

	public ResultaatstructurenExporterenSelectiePage(SecurePage returnPage)
	{
		super(returnPage, getDefaultFilter(), new HibernateSelection<Onderwijsproduct>(
			Onderwijsproduct.class), new ResultaatstructurenExporterenTarget());
	}

	@Override
	protected AbstractSelectiePanel<Onderwijsproduct, Onderwijsproduct, OnderwijsproductZoekFilter> createSelectiePanel(
			String id, OnderwijsproductZoekFilter filter,
			Selection<Onderwijsproduct, Onderwijsproduct> selection)
	{
		return new OnderwijsproductSelectiePanel(id, filter,
			(DatabaseSelection<Onderwijsproduct, Onderwijsproduct>) selection);
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}
}
