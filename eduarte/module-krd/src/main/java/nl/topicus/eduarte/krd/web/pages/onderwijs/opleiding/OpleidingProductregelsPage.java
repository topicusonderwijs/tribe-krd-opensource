package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.core.principals.onderwijs.OpleidingInzien;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.Productregel.TypeProductregel;
import nl.topicus.eduarte.krd.web.components.panels.filter.ProductregelZoekFilterPanel;
import nl.topicus.eduarte.providers.OpleidingProvider;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ProductregelTable;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.AbstractOpleidingPage;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Pagina met de productregels van een opleiding. Een productregel kan zowel landelijk
 * (voor het verbintenisgebied) als lokaal direct aan de opleiding gekoppeld zijn. Zowel
 * de landelijke als de lokale productregels worden op deze pagina getoond.
 * 
 * @author loite
 */
@PageInfo(title = "Onderwijs Productregels", menu = {"Onderwijs > [opleiding] > Productregels"})
@InPrincipal(OpleidingInzien.class)
@RechtenSoorten( {RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class OpleidingProductregelsPage extends AbstractOpleidingPage
{
	private static final long serialVersionUID = 1L;

	private final ProductregelZoekFilterPanel filterPanel;

	private static final ProductregelZoekFilter getDefaultFilter(Opleiding opleiding,
			IModel<Cohort> cohortModel)
	{
		ProductregelZoekFilter filter = new ProductregelZoekFilter(opleiding, cohortModel);
		return filter;
	}

	public OpleidingProductregelsPage(OpleidingProvider provider)
	{
		this(provider.getOpleiding());
	}

	public OpleidingProductregelsPage(Opleiding opleiding)
	{
		this(opleiding, getDefaultFilter(opleiding, EduArteSession.get().getSelectedCohortModel()));
	}

	public OpleidingProductregelsPage(Opleiding opleiding, ProductregelZoekFilter filter)
	{
		super(OpleidingMenuItem.Productregels, opleiding);
		GeneralDataProvider<Productregel, ProductregelZoekFilter> provider =
			GeneralDataProvider.of(filter,  ProductregelDataAccessHelper.class);
		CustomDataPanel<Productregel> datapanel =
			new EduArteDataPanel<Productregel>("datapanel", provider, new ProductregelTable(
				getContextOpleidingModel()));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Productregel>(
			ProductregelPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<Productregel> item)
			{
				Productregel productregel = item.getModelObject();
				setResponsePage(new ProductregelPage(getContextOpleiding(), productregel,
					OpleidingProductregelsPage.this));
			}
		});
		datapanel.setItemsPerPage(Integer.MAX_VALUE);
		add(datapanel);
		filterPanel = new ProductregelZoekFilterPanel("filter", filter, datapanel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onZoek(IPageable pageable, AjaxRequestTarget target)
			{
				super.onZoek(pageable, target);
				refreshBottomRow(target);
			}

		};
		add(filterPanel);
		createComponents();
	}

	/**
	 * @see nl.topicus.eduarte.web.pages.SecurePage#fillBottomRow(nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel)
	 */
	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				Productregel productregel = new Productregel(EntiteitContext.INSTELLING);
				productregel.setTypeProductregel(TypeProductregel.Productregel);
				productregel.setOpleiding(getContextOpleiding());
				productregel.setVerbintenisgebied(getContextOpleiding().getVerbintenisgebied());
				productregel.setCohort(filterPanel.getZoekfilter().getCohort());
				productregel.setVolgnummer(getContextOpleiding().getMaxProductregelVolgnummer(
					productregel.getCohort()) + 1);
				return new EditProductregelPage(productregel, OpleidingProductregelsPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return EditProductregelPage.class;
			}

		}, "Productregel toevoegen"));
		panel.addButton(new PageLinkButton(panel, "Productregels genereren", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new OpleidingProductregelsGenererenPage(getContextOpleiding(),
					EduArteSession.get().getSelectedCohortModel());
			}

			@Override
			public Class<OpleidingProductregelsGenererenPage> getPageIdentity()
			{
				return OpleidingProductregelsGenererenPage.class;
			}

		})
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return getContextOpleiding().getVerbintenisgebied().getTaxonomieElementType()
					.isDiplomeerbaar()
					&& getContextOpleiding().getLandelijkeEnLokaleProductregels(
						filterPanel.getZoekfilter().getCohort()).isEmpty();
			}
		});
		panel.addButton(new PageLinkButton(panel, "Kopiëren", ButtonAlignment.LEFT, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new ProductregelsKopierenStap1Page(getContextOpleiding(),
					OpleidingProductregelsPage.this);
			}

			@Override
			public Class<ProductregelsKopierenStap1Page> getPageIdentity()
			{
				return ProductregelsKopierenStap1Page.class;
			}

		}));
	}

}
