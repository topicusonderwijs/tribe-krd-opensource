package nl.topicus.eduarte.web.pages.deelnemer.resultaten;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.core.principals.deelnemer.resultaten.DeelnemerResultatenmatrix;
import nl.topicus.eduarte.dao.helpers.ToetsCodeFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.resultaat.DisplayResultatenUIFactory;
import nl.topicus.eduarte.web.components.resultaat.ResultatenModel;
import nl.topicus.eduarte.web.components.resultaat.ResultatenmatrixPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.home.ToetsFiltersPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

@PageInfo(title = "Deelnemer Resultatenmatrix", menu = "Deelnemer > [Deelnemer] > Resultaten > Resultatenmatrix")
@InPrincipal(DeelnemerResultatenmatrix.class)
public class DeelnemerResultatenmatrixPage extends AbstractDeelnemerPage
{
	private ToetsZoekFilter toetsFilter;

	@SpringBean
	private ToetsCodeFilterDataAccessHelper toetsFilterHelper;

	public DeelnemerResultatenmatrixPage(PageParameters parameters)
	{
		this(getDeelnemerFromPageParameters(DeelnemerResultatenmatrixPage.class, parameters));
	}

	public DeelnemerResultatenmatrixPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer()
			.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerResultatenmatrixPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerResultatenmatrixPage(Verbintenis inschrijving)
	{
		this(inschrijving.getDeelnemer(), inschrijving);
	}

	public DeelnemerResultatenmatrixPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		super(DeelnemerMenuItem.Resultatenmatrix, deelnemer, verbintenis);

		toetsFilter = new ToetsZoekFilter(new ResultaatstructuurZoekFilter());
		toetsFilter.getResultaatstructuurFilter().setAuthorizationContext(
			new OrganisatieEenheidLocatieAuthorizationContext(this));
		toetsFilter.getResultaatstructuurFilter().setContextVerbintenis(
			getContextVerbintenisModel());
		toetsFilter.getResultaatstructuurFilter().setType(Type.SUMMATIEF);
		toetsFilter.setToetsCodeFilter(toetsFilterHelper.getStandaardFilter(verbintenis));
		if (verbintenis != null)
		{
			toetsFilter.getResultaatstructuurFilter().setCohort(verbintenis.getCohort());
			createComponents();
			toetsFilter.getResultaatstructuurFilter().setAlleenGekoppeldAanVerbintenis(true);
		}
	}

	@Override
	protected void onBeforeRender()
	{
		addOrReplace(new ResultatenmatrixPanel<ResultatenModel>("resultaten",
			new DisplayResultatenUIFactory<Resultaatstructuur>(), toetsFilter,
			getContextDeelnemer(), toetsFilter.getResultaatstructuurFilter().getCohort()));
		super.onBeforeRender();
	}

	@Override
	protected void onSelectionChanged(Verbintenis verbintenis)
	{
		getToetsFilter().getResultaatstructuurFilter().setCohort(verbintenis.getCohort());
		getToetsFilter().setToetsCodeFilter(toetsFilterHelper.getStandaardFilter(verbintenis));
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ModuleEditPageButton<Deelnemer>(panel, Deelnemer.class,
			DeelnemerMenuItem.Resultatenmatrix, this, getContextDeelnemerModel()));
		panel.addButton(new PageLinkButton(panel, "Filters beheren", ToetsFiltersPage.class)
			.setAlignment(ButtonAlignment.LEFT));
	}

	public ToetsZoekFilter getToetsFilter()
	{
		return toetsFilter;
	}
}
