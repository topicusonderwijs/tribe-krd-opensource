package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingProductregelsKopieren;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie.ProductregelSelectiePanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectiePage;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;

/**
 * @author loite
 */
@PageInfo(title = "Productregels kopieren stap 1", menu = "Onderwijs > Kopiëren productregels")
@InPrincipal(OpleidingProductregelsKopieren.class)
public class ProductregelsKopierenStap1Page extends
		AbstractSelectiePage<Productregel, Productregel, ProductregelZoekFilter> implements
		IEditPage
{
	private static final long serialVersionUID = 1L;

	private final static ProductregelZoekFilter getDefaultFilter(Opleiding bronOpleiding)
	{
		ProductregelZoekFilter ret =
			new ProductregelZoekFilter(bronOpleiding, EduArteSession.get().getSelectedCohortModel());
		ret.setLandelijk(false);
		return ret;
	}

	public ProductregelsKopierenStap1Page(Opleiding bronOpleiding, SecurePage returnPage)
	{
		this(returnPage, getDefaultFilter(bronOpleiding));
	}

	private ProductregelsKopierenStap1Page(SecurePage returnPage, ProductregelZoekFilter filter)
	{
		super(returnPage, filter, new HibernateSelection<Productregel>(Productregel.class),
			new ProductregelsKopierenStap1Target());
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
	}

	@Override
	protected AbstractSelectiePanel<Productregel, Productregel, ProductregelZoekFilter> createSelectiePanel(
			String id, ProductregelZoekFilter filter,
			Selection<Productregel, Productregel> selection)
	{
		return new ProductregelSelectiePanel(id, filter,
			(DatabaseSelection<Productregel, Productregel>) selection);
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}
}
