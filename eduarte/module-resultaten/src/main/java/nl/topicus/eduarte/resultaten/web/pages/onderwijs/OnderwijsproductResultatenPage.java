package nl.topicus.eduarte.resultaten.web.pages.onderwijs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Status;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.Herkansingsscore;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.Scoreschaal;
import nl.topicus.eduarte.resultaten.principals.onderwijs.OnderwijsproductResultaatstructuur;
import nl.topicus.eduarte.resultaten.web.components.structuur.ResultaatstructuurPageUIFactory;
import nl.topicus.eduarte.resultaten.web.pages.shared.ResultaatstructuurReturnPage;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.filter.ToetsZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.AbstractOnderwijsproductPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Pagina voor het tonen van de resultaatstructuur voor dit onderwijsproduct
 * 
 * @author papegaaij
 */
@PageInfo(title = "Onderwijsproduct resultaatstructuur", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct] > Resultaten"})
@InPrincipal(OnderwijsproductResultaatstructuur.class)
public class OnderwijsproductResultatenPage extends AbstractOnderwijsproductPage implements
		ResultaatstructuurReturnPage
{
	private static final long serialVersionUID = 1L;

	private ResultaatstructuurPageUIFactory uiFactory;

	public OnderwijsproductResultatenPage(Onderwijsproduct onderwijsproduct)
	{
		super(OnderwijsproductMenuItem.SummatieveStructuur, ModelFactory.getModel(onderwijsproduct));
		uiFactory =
			new ResultaatstructuurPageUIFactory(this,
				new LoadableDetachableModel<Resultaatstructuur>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected Resultaatstructuur load()
					{
						return DataAccessRegistry.getHelper(
							ResultaatstructuurDataAccessHelper.class)
							.getSummatieveResultaatstructuur(getContextOnderwijsproduct(),
								getCohort());
					}
				}, getDefaultFilter(onderwijsproduct))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Resultaatstructuur createEmptyStructuur()
				{
					Resultaatstructuur structuur =
						new Resultaatstructuur(getContextOnderwijsproduct(), getCohort());
					structuur.setNaam("Summatief");
					structuur.setCode("SUM");
					structuur.setType(Type.SUMMATIEF);
					structuur.setStatus(Status.IN_ONDERHOUD);
					Toets eindresultaat = new Toets(structuur);
					eindresultaat.setCode("EIND");
					eindresultaat.setNaam("Eindresultaat");
					eindresultaat.setScoreBijHerkansing(Herkansingsscore.Hoogste);
					eindresultaat.setScoreschaal(Scoreschaal.Geen);
					structuur.setEindresultaat(eindresultaat);
					structuur.getToetsen().add(eindresultaat);
					return structuur;
				}
			};

		EduArteDataPanel<Toets> datapanel = uiFactory.createDataPanel();
		add(datapanel);

		add(new ToetsZoekFilterPanel("filter", uiFactory.getFilter(), datapanel, onderwijsproduct)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onZoek(IPageable pageable, AjaxRequestTarget target)
			{
				super.onZoek(pageable, target);
				target.addComponent(recreateBottomRow());
				target.addComponent(getTitle());
			}
		});
		uiFactory.setMessages();
		createComponents();
		getTitle().setOutputMarkupId(true);
	}

	private ToetsZoekFilter getDefaultFilter(Onderwijsproduct onderwijsproduct)
	{
		ResultaatstructuurZoekFilter resultaatstructuurFilter =
			new ResultaatstructuurZoekFilter(onderwijsproduct);
		resultaatstructuurFilter
			.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		resultaatstructuurFilter.setCohortModel(EduArteSession.get().getSelectedCohortModel());
		resultaatstructuurFilter.setType(Type.SUMMATIEF);
		if (resultaatstructuurFilter.getCohort() == null)
			resultaatstructuurFilter.setCohort(Cohort.getHuidigCohort());
		ToetsZoekFilter ret = new ToetsZoekFilter(resultaatstructuurFilter);
		ret.addOrderByProperty("boom");
		return ret;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		uiFactory.createBottomRow(panel);
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		recreateBottomRow();
	}

	public Cohort getCohort()
	{
		return uiFactory.getFilter().getResultaatstructuurFilter().getCohort();
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(uiFactory);
	}

	@Override
	protected String getTitlePostfix()
	{
		return " (" + getCohort() + ")";
	}

	@Override
	public SecurePage getReturnPage()
	{
		return this;
	}

	@Override
	public SecurePage getReturnPageAfterDelete()
	{
		return this;
	}
}
