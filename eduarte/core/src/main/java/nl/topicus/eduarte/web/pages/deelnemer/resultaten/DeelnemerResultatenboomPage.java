package nl.topicus.eduarte.web.pages.deelnemer.resultaten;

import java.util.concurrent.TimeoutException;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractConfirmationLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.resultaat.ResultaatVersionCollection;
import nl.topicus.eduarte.core.principals.deelnemer.resultaten.DeelnemerResultatenboom;
import nl.topicus.eduarte.dao.helpers.ToetsCodeFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.resultaat.DisplayResultatenUIFactory;
import nl.topicus.eduarte.web.components.resultaat.RecalculationManager;
import nl.topicus.eduarte.web.components.resultaat.ResultatenModel;
import nl.topicus.eduarte.web.components.resultaat.ResultatenboomPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.home.ToetsFiltersPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PageInfo(title = "Resultatenboom", menu = "Deelnemer > [Deelnemer] > Resultaten > Resultatenboom")
@InPrincipal(DeelnemerResultatenboom.class)
public class DeelnemerResultatenboomPage extends AbstractDeelnemerPage
{
	private static final Logger log = LoggerFactory.getLogger(DeelnemerResultatenboomPage.class);

	@SpringBean
	private ToetsCodeFilterDataAccessHelper toetsFilterHelper;

	private ResultatenboomPanel<ResultatenModel> resultatenboomPanel;

	private ResultaatVersionCollection versions;

	private boolean structuurOngeldig = false;

	public DeelnemerResultatenboomPage(PageParameters parameters)
	{
		this(getDeelnemerFromPageParameters(DeelnemerResultatenboomPage.class, parameters));
	}

	public DeelnemerResultatenboomPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer()
			.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerResultatenboomPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerResultatenboomPage(Verbintenis inschrijving)
	{
		this(inschrijving.getDeelnemer(), inschrijving);
	}

	public DeelnemerResultatenboomPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		super(DeelnemerMenuItem.Resultatenboom, deelnemer, verbintenis);

		ToetsZoekFilter toetsFilter = new ToetsZoekFilter(new ResultaatstructuurZoekFilter());
		toetsFilter.getResultaatstructuurFilter().setAuthorizationContext(
			new OrganisatieEenheidLocatieAuthorizationContext(this));
		toetsFilter.getResultaatstructuurFilter().setContextVerbintenis(
			getContextVerbintenisModel());
		toetsFilter.getResultaatstructuurFilter().setAlleenGekoppeldAanVerbintenis(true);
		toetsFilter.getResultaatstructuurFilter().setType(Type.SUMMATIEF);
		toetsFilter.setToetsCodeFilter(toetsFilterHelper.getStandaardFilter(verbintenis));
		add(resultatenboomPanel =
			new ResultatenboomPanel<ResultatenModel>("resultaten",
				new DisplayResultatenUIFactory<Toets>(), toetsFilter, deelnemer, verbintenis
					.getCohort()));
		versions = new ResultaatVersionCollection(resultatenboomPanel.getDeelnemers());
		createComponents();
	}

	public void setStructuurOngeldig(boolean structuurOngeldig)
	{
		this.structuurOngeldig = structuurOngeldig;
	}

	public ToetsZoekFilter getToetsFilter()
	{
		return resultatenboomPanel.getToetsFilter();
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
			DeelnemerMenuItem.Resultatenboom, this, getContextDeelnemerModel()));
		panel.addButton(new PageLinkButton(panel, "Filters beheren", ToetsFiltersPage.class)
			.setAlignment(ButtonAlignment.LEFT));

		panel.addButton(new AbstractConfirmationLinkButton(panel, "Structuur repareren",
			CobraKeyAction.GEEN, ButtonAlignment.LEFT,
			"Weet u zeker dat u wilt proberen de structuur te repareren?")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				final ResultaatVersionCollection newVersions =
					new ResultaatVersionCollection(resultatenboomPanel.getDeelnemers());
				try
				{
					log.info("Entering resultaten mutex for " + newVersions.getLockKeys().size()
						+ " keys");
					EduArteApp.get().getResultaatMutex().execute(newVersions, 5000, new Runnable()
					{
						@Override
						public void run()
						{
							log.info("Inside lock: verifying versions");
							if (newVersions.verifyVersions(versions))
							{
								log.info("Inside lock: incrementing versions");
								newVersions.incrementAndSave();
								performRecalc();
							}
							else
							{
								log.info("Inside lock: version mismatch");
								error("Een andere gebruiker heeft resultaten gewijzigd. "
									+ "Voer de resultaten a.u.b. opnieuw in.");
							}
						}
					});
					log.info("Left resultaten mutex");
				}
				catch (TimeoutException ex)
				{
					log.info("Could not aquire lock: timeout");
					error("De resultaten zijn in gebruik door een andere gebruiker of "
						+ "door het systeem. Probeer het later nog eens.");
				}
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && structuurOngeldig;
			}
		});
	}

	private void performRecalc()
	{
		try
		{
			log.info("Inside lock: recalculating");
			BatchDataAccessHelper< ? > batchHelper =
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
			RecalculationManager recalcManager =
				new RecalculationManager(ModelFactory.getModel(getIngelogdeMedewerker()),
					resultatenboomPanel.getResultatenModel());
			for (Toets curToets : resultatenboomPanel.getToetsen())
			{
				recalcManager.addRecalcuation(curToets, getContextDeelnemer());
			}
			recalcManager.recalculateResultaten(resultatenboomPanel.getResultatenModel()
				.getFreshObject().getAllResultaten());
			batchHelper.flush();
			log.info("Inside lock: checking");
			resultatenboomPanel.getResultatenModel().checkResultaten(false);
			batchHelper.batchExecute();
			structuurOngeldig = false;
			info("De reparatie van de structuur is geslaagd.");
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			error("De herberekening is mislukt: " + e.getMessage());
		}
	}
}
