package nl.topicus.eduarte.resultaten.web.pages.onderwijs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.app.resultaat.ResultaatVersionCollection;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Status;
import nl.topicus.eduarte.resultaten.jobs.ResultatenHerberekenJob;
import nl.topicus.eduarte.resultaten.jobs.ResultatenHerberekenJobDataMap;
import nl.topicus.eduarte.resultaten.principals.onderwijs.ToetsNormeringenAanpassen;
import nl.topicus.eduarte.resultaten.web.components.datapanel.table.ToetsNormeringTable;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PageInfo(title = "Normeringen aanpassen", menu = "Onderwijs > Onderwijsproducten > Normeringen")
@InPrincipal(ToetsNormeringenAanpassen.class)
public class ToetsNormeringEditPage extends SecurePage implements IEditPage
{
	private static final Logger log = LoggerFactory.getLogger(ToetsNormeringEditPage.class);

	private ToetsZoekFilter toetsFilter;

	private ResultaatVersionCollection versions;

	private Set<Toets> gewijzigdeToetsen = new HashSet<Toets>();

	private Form<Void> form;

	public ToetsNormeringEditPage(ToetsZoekFilter toetsFilter)
	{
		super(CoreMainMenuItem.Onderwijs);

		this.toetsFilter = toetsFilter;
		versions = new ResultaatVersionCollection(toetsFilter);

		form = new Form<Void>("form");
		add(form);

		ToetsNormeringTable table = new ToetsNormeringTable(true)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void toetsUpdated(Toets toets)
			{
				gewijzigdeToetsen.add(toets);
			}
		};

		EduArteDataPanel<Toets> datapanel =
			new EduArteDataPanel<Toets>("panel", GeneralFilteredSortableDataProvider.of(
				toetsFilter, ToetsDataAccessHelper.class), table);
		datapanel.setItemsPerPage(ToetsNormeringZoekenPage.MAX_TOETSEN);
		datapanel.setReuseItems(true);
		form.add(datapanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				doOpslaan();
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return ToetsNormeringZoekenPage.class;
			}

			@Override
			public Page getPage()
			{
				return new ToetsNormeringZoekenPage(toetsFilter);
			}
		}));
	}

	private void doOpslaan()
	{
		final ResultaatVersionCollection newVersions = new ResultaatVersionCollection(toetsFilter);
		try
		{
			log.info("Entering resultaten mutex for " + newVersions.getLockKeys().size() + " keys");
			EduArteApp.get().getResultaatMutex().execute(newVersions, 5000, new Runnable()
			{
				@Override
				public void run()
				{
					herberekenResultaten(newVersions);
				}
			});
			log.info("Left mutex");
		}
		catch (TimeoutException e)
		{
			log.info("Could not aquire lock: timeout");
			error("De resultaten zijn in gebruik door een andere gebruiker of "
				+ "het systeem. Probeer het later nog eens.");
		}
	}

	private void herberekenResultaten(ResultaatVersionCollection newVersions)
	{
		log.info("Inside lock: verifying versions");
		if (!newVersions.verifyVersions(versions))
		{
			log.info("Inside lock: version mismatch");
			ToetsNormeringZoekenPage returnPage = new ToetsNormeringZoekenPage(toetsFilter);
			returnPage.error("Een andere gebruiker heeft wijzigingen doorgevoerd op deze "
				+ "structuur. Voer uw wijzigingen opnieuw in a.u.b.");
			setResponsePage(returnPage);
		}
		else
		{
			log.info("Inside lock: incrementing versions");
			newVersions.incrementAndSave();

			log.info("Inside lock: triggering job");
			List<ResultatenHerberekenJobDataMap> jobDataMaps =
				new ArrayList<ResultatenHerberekenJobDataMap>();
			Set<Resultaatstructuur> structuren = new HashSet<Resultaatstructuur>();
			for (Toets curToets : gewijzigdeToetsen)
				structuren.add(curToets.getResultaatstructuur());
			for (Resultaatstructuur structuur : structuren)
			{
				boolean maakBeschikbaar = Status.BESCHIKBAAR.equals(structuur.getStatus());
				structuur.touch();
				structuur.setStatus(Status.IN_HERBEREKENING);
				jobDataMaps.add(createDataMap(structuur, maakBeschikbaar));
			}
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
			for (ResultatenHerberekenJobDataMap curDataMap : jobDataMaps)
			{
				try
				{
					EduArteApp.get().getEduarteScheduler().triggerJob(
						ResultatenHerberekenJob.class, curDataMap);
				}
				catch (SchedulerException e)
				{
					log.error(e.toString(), e);
					EduArteSession.get().error(
						"Taak kon niet opgestart worden voor "
							+ curDataMap.getResultaatstructuur().getOnderwijsproduct());
					EduArteSession.get().error(e.getLocalizedMessage());
				}
			}
			setResponsePage(new AlleResultatenHerberekenOverzichtPage());
		}
	}

	private ResultatenHerberekenJobDataMap createDataMap(Resultaatstructuur structuur,
			boolean maakBeschikbaar)
	{
		List<Toets> toetsen = new ArrayList<Toets>();
		for (Toets curToets : structuur.getToetsen())
		{
			if (gewijzigdeToetsen.contains(curToets))
				toetsen.add(curToets);
		}
		return new ResultatenHerberekenJobDataMap(structuur, toetsen, maakBeschikbaar);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		toetsFilter.detach();
		gewijzigdeToetsen = new HashSet<Toets>();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.Normeringen);
	}
}
