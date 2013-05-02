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
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductVoorwaarde;
import nl.topicus.eduarte.onderwijscatalogus.principals.onderwijs.OnderwijsproductVoorwaardenWrite;
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
 * Pagina voor toevoegen van onderwijsproduct voorwaarden
 * 
 * @author vandekamp
 */
@PageInfo(title = "Onderwijsproduct voorwaarden toevoegen", menu = {"Onderwijs > Onderwijsproducten > [Onderwijsproduct] > Voorwaarden > Toevoegen"})
@InPrincipal(OnderwijsproductVoorwaardenWrite.class)
public class OnderwijsproductVoorwaardenToevoegenPage extends AbstractOnderwijsproductPage
		implements IModuleEditPage<OnderwijsproductVoorwaarde>
{
	private static final long serialVersionUID = 1L;

	private final SecurePage returnToPage;

	private IModel<OnderwijsproductVoorwaarde> onderwijsproductVoorwaardeModel;

	private final OnderwijsproductZoekFilter getDefaultFilter()
	{
		OnderwijsproductZoekFilter filter = new OnderwijsproductZoekFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		filter.addOrderByProperty("titel");
		return filter;
	}

	public OnderwijsproductVoorwaardenToevoegenPage(
			OnderwijsproductVoorwaarde onderwijsproductVoorwaarde, SecurePage returnToPage)
	{
		super(OnderwijsproductMenuItem.Voorwaarden, ModelFactory
			.getModel(onderwijsproductVoorwaarde.getVoorwaardeVoor()));
		onderwijsproductVoorwaardeModel =
			ModelFactory.getModel(onderwijsproductVoorwaarde, new DefaultModelManager(
				OnderwijsproductVoorwaarde.class));
		this.returnToPage = returnToPage;
		OnderwijsproductZoekFilter filter = getDefaultFilter();
		GeneralDataProvider<Onderwijsproduct, OnderwijsproductZoekFilter> provider =
			GeneralDataProvider.of(filter, OnderwijsproductDataAccessHelper.class);
		CustomDataPanel<Onderwijsproduct> datapanel =
			new EduArteDataPanel<Onderwijsproduct>("datapanel", provider,
				new OnderwijsproductTable(true));

		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Onderwijsproduct>(
			OnderwijsproductVoorwaardenToevoegenPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<Onderwijsproduct> item)
			{
				Onderwijsproduct voorwaarde = item.getModelObject();
				OnderwijsproductVoorwaarde onderwijsprVoorw =
					onderwijsproductVoorwaardeModel.getObject();
				if (onderwijsprVoorw.getVoorwaardeVoor().isVoorwaarde(voorwaarde))
				{
					error("Onderwijsproduct " + voorwaarde.getCode()
						+ " is al een voorwaarde van dit, of onderliggende onderwijsproducten");
				}
				else if (voorwaarde.isVoorwaarde(onderwijsprVoorw.getVoorwaardeVoor()))
				{
					error("Onderwijsproduct " + onderwijsprVoorw.getVoorwaardeVoor().getCode()
						+ " is al een voorwaarde van dit, of onderliggende onderwijsproducten");
				}
				else
				{
					onderwijsprVoorw.setVoorwaardelijkProduct(voorwaarde);
					onderwijsprVoorw.saveOrUpdate();
					onderwijsprVoorw.commit();
					setResponsePage(OnderwijsproductVoorwaardenToevoegenPage.this.returnToPage);
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
		ComponentUtil.detachQuietly(onderwijsproductVoorwaardeModel);
		super.onDetach();
	}
}
