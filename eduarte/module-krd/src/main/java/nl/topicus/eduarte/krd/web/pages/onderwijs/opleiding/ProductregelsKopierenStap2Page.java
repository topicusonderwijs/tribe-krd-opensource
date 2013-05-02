package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingProductregelsKopieren;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.OpleidingSelectiePanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectiePage;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

/**
 * @author loite
 */
@PageInfo(title = "Productregels kopieren stap 2", menu = "Onderwijs > Kopiëren productregels")
@InPrincipal(OpleidingProductregelsKopieren.class)
public class ProductregelsKopierenStap2Page extends
		AbstractSelectiePage<Opleiding, Opleiding, OpleidingZoekFilter> implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private IModel<List<Productregel>> productregelsModel;

	private final static OpleidingZoekFilter getDefaultFilter()
	{
		OpleidingZoekFilter ret = new OpleidingZoekFilter();
		ret.addOrderByProperty("naam");
		ret.setCohortModel(EduArteSession.get().getSelectedCohortModel());
		return ret;
	}

	public ProductregelsKopierenStap2Page(List<Productregel> productregels, SecurePage returnPage)
	{
		this(returnPage, getDefaultFilter());
		productregelsModel = ModelFactory.getListModel(productregels);
	}

	public ProductregelsKopierenStap2Page(SecurePage returnPage)
	{
		this(returnPage, getDefaultFilter());
		productregelsModel = new ListModel<Productregel>(null);
	}

	private ProductregelsKopierenStap2Page(SecurePage returnPage, OpleidingZoekFilter filter)
	{
		super(returnPage, filter, new HibernateSelection<Opleiding>(Opleiding.class),
			new ProductregelsKopierenStap2Target(returnPage));
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(productregelsModel);
	}

	@Override
	protected AbstractSelectiePanel<Opleiding, Opleiding, OpleidingZoekFilter> createSelectiePanel(
			String id, OpleidingZoekFilter filter, Selection<Opleiding, Opleiding> selection)
	{
		return new OpleidingSelectiePanel(id, filter,
			(DatabaseSelection<Opleiding, Opleiding>) selection);
	}

	public List<Productregel> getProductregels()
	{
		return productregelsModel.getObject();
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}

}
