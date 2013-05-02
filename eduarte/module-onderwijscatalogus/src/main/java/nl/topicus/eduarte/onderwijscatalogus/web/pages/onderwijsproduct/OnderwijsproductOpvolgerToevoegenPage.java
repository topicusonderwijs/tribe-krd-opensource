package nl.topicus.eduarte.onderwijscatalogus.web.pages.onderwijsproduct;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductOpvolger;
import nl.topicus.eduarte.onderwijscatalogus.principals.onderwijs.OnderwijsproductOpvolgersWrite;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OnderwijsproductTable;
import nl.topicus.eduarte.web.components.panels.filter.OnderwijsproductZoekFilterPanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.AbstractOnderwijsproductPage;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Pagina voor toevoegen van onderwijsproductopvolger
 * 
 * @author loite
 */
@PageInfo(title = "Opvolger van onderwijsproduct toevoegen", menu = {"Onderwijs > Onderwijsproducten > [Onderwijsproduct] > Opvolgers > Toevoegen"})
@InPrincipal(OnderwijsproductOpvolgersWrite.class)
public class OnderwijsproductOpvolgerToevoegenPage extends AbstractOnderwijsproductPage implements
		IModuleEditPage<OnderwijsproductOpvolger>
{
	private static final long serialVersionUID = 1L;

	private final SecurePage returnToPage;

	private IModel<OnderwijsproductOpvolger> onderwijsproductOpvolgerModel;

	private final OnderwijsproductZoekFilter getDefaultFilter(Onderwijsproduct nietOpvolgerVan)
	{
		OnderwijsproductZoekFilter filter = new OnderwijsproductZoekFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		filter.setNietOpvolgerVan(nietOpvolgerVan);
		filter.addOrderByProperty("titel");
		return filter;
	}

	public OnderwijsproductOpvolgerToevoegenPage(OnderwijsproductOpvolger onderwijsproductOpvolger,
			SecurePage returnToPage)
	{
		super(OnderwijsproductMenuItem.Voorwaarden, ModelFactory.getModel(onderwijsproductOpvolger
			.getOudProduct()));
		onderwijsproductOpvolgerModel =
			ModelFactory.getModel(onderwijsproductOpvolger, new DefaultModelManager(
				OnderwijsproductOpvolger.class));
		this.returnToPage = returnToPage;
		OnderwijsproductZoekFilter filter =
			getDefaultFilter(onderwijsproductOpvolger.getOudProduct());
		GeneralDataProvider<Onderwijsproduct, OnderwijsproductZoekFilter> provider =
			GeneralDataProvider.of(filter, OnderwijsproductDataAccessHelper.class);
		CustomDataPanel<Onderwijsproduct> datapanel =
			new EduArteDataPanel<Onderwijsproduct>("datapanel", provider,
				new OnderwijsproductTable(true));

		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Onderwijsproduct>(
			OnderwijsproductOpvolgerToevoegenPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<Onderwijsproduct> item)
			{
				Onderwijsproduct opvolger = item.getModelObject();
				OnderwijsproductOpvolger onderwijsprOpv = onderwijsproductOpvolgerModel.getObject();
				onderwijsprOpv.setNieuwProduct(opvolger);
				onderwijsprOpv.saveOrUpdate();
				onderwijsprOpv.commit();
				onderwijsprOpv.getOudProduct().getOpvolgers().add(onderwijsprOpv);
				setResponsePage(OnderwijsproductOpvolgerToevoegenPage.this.returnToPage);
			}
		});
		add(datapanel);
		add(new OnderwijsproductZoekFilterPanel("filter", filter, datapanel));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new AnnulerenButton(panel, returnToPage));
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(onderwijsproductOpvolgerModel);
		super.onDetach();
	}
}
