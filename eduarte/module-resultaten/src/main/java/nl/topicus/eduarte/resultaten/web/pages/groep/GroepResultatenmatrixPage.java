package nl.topicus.eduarte.resultaten.web.pages.groep;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsCodeFilterDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.providers.GroepProvider;
import nl.topicus.eduarte.resultaten.principals.groep.GroepResultatenmatrix;
import nl.topicus.eduarte.resultaten.web.components.factory.ToetscodesBeherenButtonFactory;
import nl.topicus.eduarte.resultaten.web.components.resultaat.GroepResultatenmatrixPanel;
import nl.topicus.eduarte.web.components.menu.GroepMenuItem;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.resultaat.DisplayResultatenUIFactory;
import nl.topicus.eduarte.web.components.resultaat.ResultatenModel;
import nl.topicus.eduarte.web.pages.groep.AbstractGroepPage;
import nl.topicus.eduarte.web.pages.home.ToetsFiltersPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.PageParameters;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

@PageInfo(title = "Groep Resultatenmatrix", menu = "Groepen > [Groep] > Resultaten")
@InPrincipal(GroepResultatenmatrix.class)
public class GroepResultatenmatrixPage extends AbstractGroepPage
{
	private ToetsZoekFilter toetsFilter;

	@SpringBean
	private ToetsCodeFilterDataAccessHelper toetsFilterHelper;

	public GroepResultatenmatrixPage(PageParameters parameters)
	{
		this(AbstractGroepPage.getGroepFromPageParameters(parameters));
	}

	public GroepResultatenmatrixPage(GroepProvider provider)
	{
		this(provider.getGroep());
	}

	public GroepResultatenmatrixPage(Groep groep)
	{
		super(GroepMenuItem.Invoeren, groep);

		toetsFilter = new ToetsZoekFilter(new ResultaatstructuurZoekFilter());
		toetsFilter.getResultaatstructuurFilter().setAuthorizationContext(
			new OrganisatieEenheidLocatieAuthorizationContext(this));
		toetsFilter.getResultaatstructuurFilter().setDeelnemers(
			groep.getDeelnemersOpPeildatumAsDeelnemer());
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
		List<Deelnemer> deelnemers = getContextGroep().getDeelnemersOpPeildatumAsDeelnemer();
		toetsFilter.setToetsCodeFilter(toetsFilterHelper.getStandaardFilter(deelnemers, toetsFilter
			.getResultaatstructuurFilter().getCohort()));
		addOrReplace(new GroepResultatenmatrixPanel<ResultatenModel>("resultaten",
			new DisplayResultatenUIFactory<Deelnemer>(), toetsFilter, getContextGroep()));
		super.onBeforeRender();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ModuleEditPageButton<Groep>(panel, Groep.class, GroepMenuItem.Invoeren,
			this, getContextGroepModel()));
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

	public ToetsZoekFilter getToetsFilter()
	{
		return toetsFilter;
	}
}
