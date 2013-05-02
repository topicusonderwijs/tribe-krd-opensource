package nl.topicus.eduarte.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.core.principals.onderwijs.CurriculumInzien;
import nl.topicus.eduarte.dao.helpers.CurriculumDataAccessHelper;
import nl.topicus.eduarte.entities.curriculum.Curriculum;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.providers.OpleidingProvider;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CurriculumTable;
import nl.topicus.eduarte.zoekfilters.CurriculumZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;

@PageInfo(title = "Curriculum Overzicht", menu = {"Onderwijs > [opleiding] > Curriculum"})
@InPrincipal(CurriculumInzien.class)
public class CurriculumOverzichtPage extends AbstractOpleidingPage
{
	private static final long serialVersionUID = 1L;

	public CurriculumOverzichtPage(OpleidingProvider provider)
	{
		this(provider.getOpleiding());
	}

	public CurriculumOverzichtPage(Opleiding opleiding)
	{
		super(OpleidingMenuItem.Curriculum, opleiding);

		CurriculumZoekFilter filter = new CurriculumZoekFilter(getContextOpleiding());
		filter.addOrderByProperty("cohort");
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));

		EduArteDataPanel<Curriculum> datapanel =
			new EduArteDataPanel<Curriculum>("datapanel", GeneralFilteredSortableDataProvider.of(
				filter, CurriculumDataAccessHelper.class), new CurriculumTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Curriculum>(
			CurriculumOnderwijsproductOpleidingOverzichtPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<Curriculum> item)
			{
				setResponsePage(new CurriculumOnderwijsproductOpleidingOverzichtPage(item
					.getModelObject(), new IPageLink()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Class< ? extends Page> getPageIdentity()
					{
						return CurriculumOverzichtPage.this.getPageClass();
					}

					@Override
					public Page getPage()
					{
						return CurriculumOverzichtPage.this;
					}
				}));
			}
		});

		add(datapanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new BewerkenButton<Curriculum>(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return CurriculumEditPage.class;
			}

			@Override
			public Page getPage()
			{
				return new CurriculumEditPage(getContextOpleiding(), CurriculumOverzichtPage.this);
			}
		}));
	}
}
