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
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductSamenstelling;
import nl.topicus.eduarte.onderwijscatalogus.principals.onderwijs.OnderwijsproductPaklijstWrite;
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
 * Pagina voor het toevoegen van de onderwijsproducten die een onderdeel zijn van dit
 * onderwijsproduct
 * 
 * @author vandekamp
 */
@PageInfo(title = "Onderwijsproduct onderdelen toevoegen", menu = {"Onderwijs > Onderwijsproducten > [Onderwijsproduct] > Paklijst > Toevoegen"})
@InPrincipal(OnderwijsproductPaklijstWrite.class)
public class OnderwijsproductPaklijstToevoegenPage extends AbstractOnderwijsproductPage implements
		IModuleEditPage<OnderwijsproductSamenstelling>
{
	private static final long serialVersionUID = 1L;

	private final SecurePage returnToPage;

	private IModel<OnderwijsproductSamenstelling> onderwijsproductPaklijstModel;

	private final OnderwijsproductZoekFilter getDefaultFilter()
	{
		OnderwijsproductZoekFilter filter = new OnderwijsproductZoekFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		filter.addOrderByProperty("titel");
		return filter;
	}

	public OnderwijsproductPaklijstToevoegenPage(
			OnderwijsproductSamenstelling onderwijsproductSamenstelling, SecurePage returnToPage)
	{
		super(OnderwijsproductMenuItem.Paklijst, ModelFactory
			.getModel(onderwijsproductSamenstelling.getParent()));
		onderwijsproductPaklijstModel =
			ModelFactory.getModel(onderwijsproductSamenstelling, new DefaultModelManager(
				OnderwijsproductSamenstelling.class));
		this.returnToPage = returnToPage;
		OnderwijsproductZoekFilter filter = getDefaultFilter();
		GeneralDataProvider<Onderwijsproduct, OnderwijsproductZoekFilter> provider =
			GeneralDataProvider.of(filter, OnderwijsproductDataAccessHelper.class);
		CustomDataPanel<Onderwijsproduct> datapanel =
			new EduArteDataPanel<Onderwijsproduct>("datapanel", provider,
				new OnderwijsproductTable(true));

		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Onderwijsproduct>(
			OnderwijsproductPaklijstToevoegenPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<Onderwijsproduct> item)
			{
				Onderwijsproduct onderdeel = item.getModelObject();
				OnderwijsproductSamenstelling samenstelling =
					onderwijsproductPaklijstModel.getObject();
				if (samenstelling.getParent().isOnderdeel(onderdeel))
				{
					error("Onderwijsproduct " + onderdeel.getCode()
						+ " is al een onderdeel van dit, of onderliggende onderwijsproducten");
				}
				else if (onderdeel.isOnderdeel(samenstelling.getParent()))
				{
					error("Onderwijsproduct " + samenstelling.getParent().getCode()
						+ " is al een onderdeel van dit, of onderliggende onderwijsproducten");
				}
				else
				{
					samenstelling.setChild(onderdeel);
					samenstelling.saveOrUpdate();
					samenstelling.commit();
					setResponsePage(OnderwijsproductPaklijstToevoegenPage.this.returnToPage);
				}
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
		ComponentUtil.detachQuietly(onderwijsproductPaklijstModel);
		super.onDetach();
	}
}
