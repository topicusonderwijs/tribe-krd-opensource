package nl.topicus.eduarte.web.pages.onderwijs.opleiding;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.core.principals.onderwijs.CurriculumInzien;
import nl.topicus.eduarte.dao.helpers.CurriculumOnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.curriculum.Curriculum;
import nl.topicus.eduarte.entities.curriculum.CurriculumOnderwijsproduct;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CurriculumOnderwijsproductTable;
import nl.topicus.eduarte.zoekfilters.CurriculumOnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

@PageInfo(title = "Curriculum Onderwijsproduct overzicht", menu = {"Onderwijs > [opleiding] > Curriculum > [curriculum]"})
@InPrincipal(CurriculumInzien.class)
public class CurriculumOnderwijsproductOpleidingOverzichtPage extends AbstractOpleidingPage
{
	private static final long serialVersionUID = 1L;

	private IModel<Curriculum> curriculumModel;

	private IPageLink returnPageLink;

	public CurriculumOnderwijsproductOpleidingOverzichtPage(Curriculum curriculum,
			IPageLink returnPageLink)
	{
		super(OpleidingMenuItem.Curriculum, curriculum.getOpleiding());
		curriculumModel = ModelFactory.getModel(curriculum);
		this.returnPageLink = returnPageLink;

		CurriculumOnderwijsproductZoekFilter filter =
			new CurriculumOnderwijsproductZoekFilter(curriculum);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));

		filter.addOrderByProperty("periode");
		filter.addOrderByProperty("leerjaar");

		EduArteDataPanel<CurriculumOnderwijsproduct> datapanel =
			new EduArteDataPanel<CurriculumOnderwijsproduct>("datapanel",
				GeneralFilteredSortableDataProvider.of(filter,
					CurriculumOnderwijsproductDataAccessHelper.class),
				new CurriculumOnderwijsproductTable(curriculum));
		datapanel.setRowFactory(new CustomDataPanelRowFactory<CurriculumOnderwijsproduct>());
		add(datapanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new BewerkenButton<CurriculumOnderwijsproduct>(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return CurriculumOnderwijsproductEditPage.class;
			}

			@Override
			public Page getPage()
			{
				return new CurriculumOnderwijsproductEditPage(getCurriculum(),
					CurriculumOnderwijsproductOpleidingOverzichtPage.this);
			}
		}));

		panel.addButton(new TerugButton(panel, returnPageLink));
	}

	private IModel<Curriculum> getCurriculumModel()
	{
		return curriculumModel;
	}

	private Curriculum getCurriculum()
	{
		return getCurriculumModel().getObject();
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		ctorArgTypes.clear();
		ctorArgValues.clear();

		ctorArgTypes.add(Curriculum.class);
		ctorArgValues.add(curriculumModel);
		ctorArgTypes.add(IPageLink.class);
		ctorArgValues.add(returnPageLink);
	}

	@Override
	protected void detachModel()
	{
		super.detachModel();
		ComponentUtil.detachQuietly(curriculumModel);
	}

}
