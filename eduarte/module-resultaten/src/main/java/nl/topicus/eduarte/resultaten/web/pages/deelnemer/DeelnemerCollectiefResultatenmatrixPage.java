package nl.topicus.eduarte.resultaten.web.pages.deelnemer;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsCodeFilterDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.resultaten.principals.deelnemer.DeelnemerCollectiefResultatenmatrix;
import nl.topicus.eduarte.resultaten.web.components.factory.ToetscodesBeherenButtonFactory;
import nl.topicus.eduarte.resultaten.web.components.resultaat.GroepResultatenmatrixPanel;
import nl.topicus.eduarte.web.components.resultaat.DisplayResultatenUIFactory;
import nl.topicus.eduarte.web.components.resultaat.ResultatenModel;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.PageContext;
import nl.topicus.eduarte.web.pages.home.ToetsFiltersPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

@PageInfo(title = "Resultatenmatrix", menu = "Deelnemer > Resultaten")
@InPrincipal(DeelnemerCollectiefResultatenmatrix.class)
public class DeelnemerCollectiefResultatenmatrixPage extends AbstractDynamicContextPage<Void>
{
	private ToetsZoekFilter toetsFilter;

	private IModel<List<Deelnemer>> selectedDeelnemers;

	@SpringBean
	private ToetsCodeFilterDataAccessHelper toetsFilterHelper;

	public DeelnemerCollectiefResultatenmatrixPage(PageContext context,
			List<Deelnemer> selectedDeelnemers)
	{
		super(context);

		this.selectedDeelnemers = ModelFactory.getListModel(selectedDeelnemers);
		toetsFilter = new ToetsZoekFilter(new ResultaatstructuurZoekFilter());
		toetsFilter.getResultaatstructuurFilter().setAuthorizationContext(
			new OrganisatieEenheidLocatieAuthorizationContext(this));
		toetsFilter.getResultaatstructuurFilter().setDeelnemers(selectedDeelnemers);
		toetsFilter.getResultaatstructuurFilter().setType(Type.SUMMATIEF);
		Resultaatstructuur meestWaarschijnlijk =
			DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class)
				.getMeestWaarschijnlijkeStructuur(toetsFilter.getResultaatstructuurFilter());
		if (meestWaarschijnlijk != null)
		{
			toetsFilter.getResultaatstructuurFilter().setCohort(meestWaarschijnlijk.getCohort());
			toetsFilter.getResultaatstructuurFilter().setOnderwijsproduct(
				meestWaarschijnlijk.getOnderwijsproduct());
		}
		createComponents();
	}

	@Override
	protected void onBeforeRender()
	{
		List<Deelnemer> deelnemers = new ArrayList<Deelnemer>(getSelectedDeelnemers());
		if (!hasBeenRendered())
			toetsFilter.setToetsCodeFilter(toetsFilterHelper.getStandaardFilter(deelnemers,
				toetsFilter.getResultaatstructuurFilter().getCohort()));
		addOrReplace(new GroepResultatenmatrixPanel<ResultatenModel>("resultaten",
			new DisplayResultatenUIFactory<Deelnemer>(), toetsFilter, deelnemers));
		super.onBeforeRender();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new BewerkenButton<Void>(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new DeelnemerCollectiefResultatenmatrixEditPage(
					DeelnemerCollectiefResultatenmatrixPage.this);
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return DeelnemerCollectiefResultatenmatrixEditPage.class;
			}
		}));
		panel.addButton(new PageLinkButton(panel, "Filters beheren", ToetsFiltersPage.class)
			.setAlignment(ButtonAlignment.LEFT));
		EduArteApp.get().getFirstPanelFactory(ToetscodesBeherenButtonFactory.class,
			EduArteContext.get().getOrganisatie()).addToetscodesBeherenButton(panel,
			new LoadableDetachableModel<List<Toets>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Toets> load()
				{
					return DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).list(
						getToetsFilter());
				}
			}, this);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		selectedDeelnemers.detach();
	}

	public List<Deelnemer> getSelectedDeelnemers()
	{
		return selectedDeelnemers.getObject();
	}

	public ToetsZoekFilter getToetsFilter()
	{
		return toetsFilter;
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(PageContext.class);
		ctorArgTypes.add(List.class);
		ctorArgValues.add(getPageContext());
		ctorArgValues.add(selectedDeelnemers);
	}
}
