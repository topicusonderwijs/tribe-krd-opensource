package nl.topicus.eduarte.web.components.panels;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.datapanel.ColumnModel;
import nl.topicus.cobra.web.components.datapanel.CriteriaBean;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelId;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.cobra.web.components.datapanel.IContextProvider;
import nl.topicus.cobra.web.components.dataview.ExportableDataView;
import nl.topicus.cobra.web.components.link.CustomDataPanelAfdrukkenLink;
import nl.topicus.cobra.web.components.link.CustomDataPanelExportLink;
import nl.topicus.cobra.web.components.wiquery.OpenInNewWindowAttributeModifier;
import nl.topicus.cobra.web.pages.ContextPage;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.InstellingsLogoDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.InstellingsLogo;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.jobs.rapportage.DataPanelExportJob;
import nl.topicus.eduarte.web.components.datapanel.ActiefRowFactoryDecorator;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EduArteDataPanel<T> extends CustomDataPanel<T>
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(EduArteDataPanel.class);

	private AutoZoekFilterPanel< ? > zoekFilterPanel;

	private static final class EduArteContextProvider implements IContextProvider
	{
		private static final long serialVersionUID = 1L;

		private final String contextOmschrijving;

		private final String gebruikersnaam;

		private final String organisatienaam;

		private List<CriteriaBean> zoekCriteria;

		private EduArteContextProvider(String contextOmschrijving, String gebruikersnaam,
				String organisatienaam, List<CriteriaBean> zoekCriteria)
		{
			this.contextOmschrijving = contextOmschrijving;
			this.gebruikersnaam = gebruikersnaam;
			this.organisatienaam = organisatienaam;
			this.zoekCriteria = zoekCriteria;
		}

		@Override
		public String getContextOmschrijving()
		{
			return contextOmschrijving;
		}

		@Override
		public String getIngelogdeGebruikersNaam()
		{
			return gebruikersnaam;
		}

		@Override
		public String getOrganisatieNaam()
		{
			return organisatienaam;
		}

		@Override
		public List<CriteriaBean> getZoekCriteria()
		{
			return zoekCriteria;
		}

		@Override
		public Image getInstellingsLogo()
		{
			Image curFoto = null;
			try
			{
				InstellingsLogo logo =
					DataAccessRegistry.getHelper(InstellingsLogoDataAccessHelper.class)
						.getInstellingsLogo();
				if (logo != null)
				{
					byte[] afbeelding = logo.getLogo().getBestand();
					if (afbeelding != null && afbeelding.length > 0)
					{
						curFoto = ImageIO.read(new ByteArrayInputStream(afbeelding));
					}
				}
			}
			catch (IOException e)
			{
				// ignore, we kunnen hier toch niets doen.
			}
			return curFoto;
		}
	}

	private IContextProvider provider = null;

	public EduArteDataPanel(String id, CustomDataPanelId panelId, IDataProvider<T> provider,
			CustomDataPanelContentDescription<T> contentDescription)
	{
		super(id, panelId, provider, contentDescription);
		setRowFactory(new ActiefRowFactoryDecorator<T>(new CustomDataPanelRowFactory<T>()));
	}

	public EduArteDataPanel(String id, IDataProvider<T> provider,
			CustomDataPanelContentDescription<T> contentDescription)
	{
		super(id, provider, contentDescription);
		setRowFactory(new ActiefRowFactoryDecorator<T>(new CustomDataPanelRowFactory<T>()));
	}

	@Override
	public boolean supportsExportJobs()
	{
		return true;
	}

	@Override
	public void triggerExportJob(CustomDataPanelAfdrukkenLink<T> link)
	{
		triggerExportJob(link, true);
	}

	@Override
	public void triggerExportJob(CustomDataPanelExportLink<T> link)
	{
		triggerExportJob(link, false);
	}

	@Override
	protected IContextProvider getContextProvider()
	{
		return provider;
	}

	@Override
	public void createContextProvider()
	{
		Account account = EduArteContext.get().getAccount();
		provider =
			new EduArteContextProvider(((ContextPage) getPage()).getContextOmschrijving(),
				account == null ? null : account.getGebruikersnaam(), EduArteContext.get()
					.getInstelling().getNaam(), zoekFilterPanel == null ? null
					: zoekFilterPanel.getZoekCriteria());
	}

	private void triggerExportJob(Link<T> link, boolean pdf)
	{
		if (provider == null)
			createContextProvider();
		JobDataMap datamap = new JobDataMap();
		datamap.put("link", link);
		datamap.put("application", EduArteApp.get().getApplicationKey());
		datamap.put("session", Session.get());
		datamap.put("omschrijving", provider.getContextOmschrijving());
		datamap.put("pdf", Boolean.valueOf(pdf));
		try
		{
			EduArteApp.get().getEduarteScheduler().triggerJob(DataPanelExportJob.class, datamap);
		}
		catch (SchedulerException e)
		{
			log.error("Export kon niet in de achtergrond opgestart worden");
		}
	}

	@Override
	protected Component createAfdrukkenLink(String id, ColumnModel<T> columns,
			ExportableDataView<T> dataz)
	{
		Component link = super.createAfdrukkenLink(id, columns, dataz);
		link.add(new OpenInNewWindowAttributeModifier());
		return link;
	}

	public void setZoekFilterPanel(AutoZoekFilterPanel< ? > autoZoekFilterPanel)
	{
		this.zoekFilterPanel = autoZoekFilterPanel;
	}

	public AutoZoekFilterPanel< ? > getZoekFilterPanel()
	{
		return zoekFilterPanel;
	}

}
