package nl.topicus.eduarte.web.pages.onderwijs.curriculum;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.core.principals.onderwijs.CurriculumInzien;
import nl.topicus.eduarte.dao.helpers.CurriculumOnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.curriculum.CurriculumOnderwijsproduct;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CurriculumOnderwijsproductTable;
import nl.topicus.eduarte.web.components.panels.filter.CurriculumOnderwijsproductZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.CurriculumOnderwijsproductOpleidingOverzichtPage;
import nl.topicus.eduarte.zoekfilters.CurriculumOnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;

@PageInfo(title = "Curriculumoverzicht", menu = "Onderwijs > Curriculum")
@InPrincipal(CurriculumInzien.class)
public class CurriculumOnderwijsproductOverzichtPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private CurriculumOnderwijsproductZoekFilterPanel filterPanel;

	public CurriculumOnderwijsproductOverzichtPage()
	{
		super(CoreMainMenuItem.Onderwijs);

		CurriculumOnderwijsproductZoekFilter filter = new CurriculumOnderwijsproductZoekFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));

		filter.addOrderByProperty("periode");
		filter.addOrderByProperty("leerjaar");
		filter.addOrderByProperty("curriculum.opleiding");
		filter.addOrderByProperty("curriculum.cohort");

		GeneralFilteredSortableDataProvider<CurriculumOnderwijsproduct, CurriculumOnderwijsproductZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				CurriculumOnderwijsproductDataAccessHelper.class);

		EduArteDataPanel<CurriculumOnderwijsproduct> datapanel =
			new EduArteDataPanel<CurriculumOnderwijsproduct>("datapanel", provider,
				new CurriculumOnderwijsproductTable());

		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<CurriculumOnderwijsproduct>(
			CurriculumOnderwijsproductOpleidingOverzichtPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<CurriculumOnderwijsproduct> item)
			{
				setResponsePage(new CurriculumOnderwijsproductOpleidingOverzichtPage(item
					.getModelObject().getCurriculum(), new IPageLink()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Page getPage()
					{
						return CurriculumOnderwijsproductOverzichtPage.this;
					}

					@Override
					public Class< ? extends Page> getPageIdentity()
					{
						return CurriculumOnderwijsproductOverzichtPage.this.getPageClass();
					}
				}));
			}
		});

		add(datapanel);

		datapanel.setItemsPerPage(10);
		add(datapanel);
		filterPanel = new CurriculumOnderwijsproductZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.Curriculum);
	}
}