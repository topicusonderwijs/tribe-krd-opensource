package nl.topicus.eduarte.web.components.panels.sidebar.rapportage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.link.ConfirmationAjaxLink;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.sidebar.datastores.AbstractSideBarDataStore;
import nl.topicus.eduarte.dao.helpers.JobRunDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.logging.RapportageJobRun;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.MedewerkerAccount;
import nl.topicus.eduarte.jobs.rapportage.RapportageDownloadLink;
import nl.topicus.eduarte.web.components.panels.sidebar.AbstractSideBar;
import nl.topicus.eduarte.web.components.panels.sidebar.GeenElementenPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.JobRunZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

/**
 * Sidebar component waarin persoonlijke rapportages getoond worden. We doen hier geen
 * auto updater want dat is duur! Dit doen we wel via de interface welke wordt gebruikt
 * bij de genereer link.
 * 
 * @author hoeve
 */
public class RapportageSideBar extends AbstractSideBar
{
	private static final long serialVersionUID = 1L;

	/**
	 * Exception die gegooid wordt bij een fout bij het klikken op een rapportage.
	 * 
	 * @author loite
	 */
	public static final class GotoRapportageException extends Exception
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor
		 * 
		 * @param message
		 * @param cause
		 */
		public GotoRapportageException(String message, Throwable cause)
		{
			super(message, cause);
		}
	}

	private static final class RapportageModel extends
			LoadableDetachableModel<List<RapportageJobRun>>
	{
		private JobRunZoekFilter<RapportageJobRun> getDefaultFinishedZoekFilter()
		{
			JobRunZoekFilter<RapportageJobRun> filter =
				new JobRunZoekFilter<RapportageJobRun>(RapportageJobRun.class);
			Account account = EduArteContext.get().getAccount();
			Medewerker medewerker = null;
			if (account instanceof MedewerkerAccount)
				medewerker = EduArteContext.get().getMedewerker();
			filter.setMedewerker(medewerker);
			filter.setAccount(account);

			// rapportages van de laatste twee dagen. (Zonder tijdinformatie, omdat dit de
			// caching omzeilt!)
			Date datenow = TimeUtil.getInstance().currentDate();
			datenow = TimeUtil.getInstance().addDays(datenow, -2);
			filter.setBeginDatum(datenow);
			filter.addOrderByProperty("runStart");
			filter.setAscending(false);

			return filter;
		}

		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		protected List<RapportageJobRun> load()
		{
			Account account = EduArteContext.get().getAccount();
			Medewerker medewerker = null;
			if (account instanceof MedewerkerAccount)
				medewerker = EduArteContext.get().getMedewerker();

			if ((account == null && medewerker == null)
				|| EduArteContext.get().getInstelling() == null)
				return new ArrayList<RapportageJobRun>();

			JobRunZoekFilter<RapportageJobRun> filter = getDefaultFinishedZoekFilter();

			return DataAccessRegistry.getHelper(JobRunDataAccessHelper.class).list(filter, 0, 5);
		}
	}

	private WebMarkupContainer listcontainer;

	final ListView<RapportageJobRun> listview;

	public RapportageSideBar(String id, final SecurePage page)
	{
		super(id, page);

		listview = new ListView<RapportageJobRun>("listview", new RapportageModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<RapportageJobRun> item)
			{
				Link<RapportageJobRun> link = new RapportageDownloadLink("link", item.getModel());
				RapportageJobRun jobRun = item.getModelObject();
				link.add(new Label("samenvatting", new Model<String>((jobRun.getSamenvatting()
					+ " " + TimeUtil.getInstance().formatDateTime(jobRun.getCreatedAt())))));
				item.add(link);

				ConfirmationAjaxLink<RapportageJobRun> verwijderen =
					new ConfirmationAjaxLink<RapportageJobRun>("verwijderen",
						"Weet u zeker dat u deze rapportage wilt verwijderen?", item.getModel())
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick(AjaxRequestTarget target)
						{
							RapportageJobRun run = getModelObject();
							run.delete();
							run.commit();

							RapportageSideBar.this.refresh(target);
						}
					};
				item.add(verwijderen);
			}
		};

		listcontainer = new WebMarkupContainer("listcontainer");
		listcontainer.setOutputMarkupId(true);
		listcontainer.add(listview);
		add(listcontainer);

		GeenElementenPanel geenElementen =
			new GeenElementenPanel("geen", "Geen rapportages gevonden", 2)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return listview.size() == 0;
				}
			};
		listcontainer.add(geenElementen);
	}

	protected void refresh(AjaxRequestTarget target)
	{
		target.addComponent(listcontainer);
		listview.getModel().detach();
	}

	@Override
	public Component add(IBehavior... behavior)
	{
		listcontainer.add(behavior);
		listview.getModel().detach();

		return this;
	}

	/**
	 * @see nl.topicus.eduarte.web.components.panels.sidebar.AbstractSideBar#getDataStoreClass()
	 */
	@Override
	protected Class< ? extends AbstractSideBarDataStore> getDataStoreClass()
	{
		return null;
	}
}
