package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.fileresources.PdfFileResourceStream;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekIndelingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.entities.participatie.enums.AbsentiePresentieEnum;
import nl.topicus.eduarte.entities.participatie.enums.Schooljaar;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingWeergaveEnum;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerJaarOverzichtRapportage;
import nl.topicus.eduarte.krdparticipatie.rapportage.JaaroverzichtRapportageUtil;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.filter.WaarnemingOverzichtZoekFilterPanel;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.waarneming.WaarnemingDagenHeaderPanel;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.waarneming.WaarnemingTotalenPanel;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.waarneming.WaarnemingUrenHeaderPanel;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.waarneming.WaarnemingWeekPanel;
import nl.topicus.eduarte.participatie.zoekfilters.LesweekindelingZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingOverzichtZoekFilter;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pagina met het waarnemingen jaaroverzicht van 1 deelnemer
 * 
 * @author vandekamp
 */
@PageInfo(title = "Jaaroverzicht", menu = {"Deelnemer > [deelnemer] > Participatie > Jaaroverzicht"})
@InPrincipal(DeelnemerJaarOverzichtRapportage.class)
public class DeelnemerJaarOverzichtPage extends AbstractDeelnemerPage implements
		WaarnemingOverzichtInterface
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(DeelnemerJaarOverzichtPage.class);

	private static final WaarnemingOverzichtZoekFilter getDefaultFilter(Deelnemer deelnemer)
	{
		WaarnemingOverzichtZoekFilter filter = new WaarnemingOverzichtZoekFilter(deelnemer);
		filter.setBeginDatum(Schooljaar.getHuidigSchooljaar().getVanafDatum());
		filter.setEindDatum(TimeUtil.getInstance().currentDate());
		filter.setAbsentieOfPresentie(AbsentiePresentieEnum.Absentie_en_Presentie);
		filter.setWaarnemingWeergave(WaarnemingWeergaveEnum.AbsentieMeldingOfWaarneming);
		filter.setToonTotalenKolommen(true);
		filter.setToonLegeRegels(true);
		return filter;
	}

	private WaarnemingOverzichtZoekFilterPanel filterPanel = null;

	private IModel<Locatie> locatieModel;

	private IModel<OrganisatieEenheid> organisatieEenheidModel;

	public DeelnemerJaarOverzichtPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer());
	}

	public DeelnemerJaarOverzichtPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum());
	}

	public DeelnemerJaarOverzichtPage(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis);
	}

	public DeelnemerJaarOverzichtPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		this(deelnemer, verbintenis, getDefaultFilter(deelnemer));
	}

	public DeelnemerJaarOverzichtPage(Deelnemer deelnemer, Verbintenis verbintenis,
			WaarnemingOverzichtZoekFilter filter)
	{
		super(ParticipatieDeelnemerMenuItem.Jaaroverzicht, deelnemer, verbintenis);

		organisatieEenheidModel = ModelFactory.getModel(verbintenis.getOrganisatieEenheid());
		locatieModel = ModelFactory.getModel(verbintenis.getLocatie());

		if (getOrganisatieEenheid() == null)
		{
			error("Er is voor deze deelnemer geen organisatie-eenheid gevonden.");
			createEmptyContent();
		}
		else if (getLesweekIndelingModel() == null || getLesweekIndelingModel().getObject() == null)
		{
			error("Er is voor deze deelnemer geen gekoppelde lesweek indeling gevonden.");
			createEmptyContent();
		}
		else
		{
			filter.setLesweekIndeling(getLesweekIndelingModel().getObject());
			filterPanel =
				new WaarnemingOverzichtZoekFilterPanel("filter", filter, this, true, verbintenis
					.getOrganisatieEenheid(), null);

			add(filterPanel);
			createContent();

		}
		createComponents();
	}

	private void createEmptyContent()
	{
		addOrReplace(new WebMarkupContainer("filter"));
		WebMarkupContainer jaaroverzichtTable = new WebMarkupContainer("jaaroverzichtTable");
		addOrReplace(jaaroverzichtTable);
		jaaroverzichtTable.addOrReplace(new WebMarkupContainer("dagenHeaderPanel"));
		jaaroverzichtTable.addOrReplace(new WebMarkupContainer("urenHeaderPanel"));
		jaaroverzichtTable.addOrReplace(new WebMarkupContainer("weken"));
		addOrReplace(new WebMarkupContainer("totalen"));

	}

	private void createContent()
	{
		WebMarkupContainer jaaroverzichtTable = new WebMarkupContainer("jaaroverzichtTable");
		addOrReplace(jaaroverzichtTable);
		jaaroverzichtTable.addOrReplace(new WaarnemingDagenHeaderPanel("dagenHeaderPanel", this,
			false));
		jaaroverzichtTable.addOrReplace(new WaarnemingUrenHeaderPanel("urenHeaderPanel", this));
		addOrReplace(new WaarnemingTotalenPanel("totalen", this));
		RepeatingView weken = new RepeatingView("weken");
		jaaroverzichtTable.addOrReplace(weken);

		WaarnemingOverzichtZoekFilter filter = getFilterModel().getObject();
		int week = TimeUtil.getInstance().getWeekOfYear(filter.getBeginDatum());
		int jaar = TimeUtil.getInstance().getYear(filter.getBeginDatum());
		Date datum = TimeUtil.getInstance().getWeekBeginEnEindDatum(jaar, week)[0];
		WaarnemingWeekPanel waarnemingWeekPanel = null;
		for (; !datum.after(filter.getEindDatum());)
		{
			waarnemingWeekPanel =
				new WaarnemingWeekPanel("week" + datum.getTime(), getContextDeelnemer(), datum,
					this);
			weken.add(waarnemingWeekPanel);
			datum = TimeUtil.getInstance().addWeeks(datum, 1);
		}
		if (waarnemingWeekPanel != null)
		{
			int width = 120 + 21 * waarnemingWeekPanel.getColumnCount();
			System.out.println("width = " + width);
			jaaroverzichtTable.add(new AttributeAppender("style", new Model<String>("width: "
				+ width + "px"), " "));
		}
	}

	/**
	 * submit het filter
	 */
	public void submitFilter()
	{
		createContent();
	}

	public IModel<WaarnemingOverzichtZoekFilter> getFilterModel()
	{
		return filterPanel.getFilterModel();
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		if (getContextVerbintenis() != null)
			return getContextVerbintenis().getOrganisatieEenheid();
		return null;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		if (getLesweekIndelingModel() != null && getLesweekIndelingModel().getObject() != null)
		{
			panel.addButton(new AbstractLinkButton(panel, "PDF genereren",
				CobraKeyAction.LINKKNOP1, ButtonAlignment.RIGHT)
			{

				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick()
				{
					try
					{
						byte[] bestand =
							ResourceUtil.readClassPathFileAsBytes(DeelnemerJaarOverzichtPage.class,
								"DeelnemerWaarnemingenOverzicht.jrxml");
						JaaroverzichtRapportageUtil rapport =
							new JaaroverzichtRapportageUtil(getFilterModel().getObject());
						JasperPrint print =
							rapport.generateDocuments(Arrays.asList(getContextVerbintenis()),
								bestand);
						File file = File.createTempFile("lijst", ".pdf");
						JasperExportManager.exportReportToPdfFile(print, file.getPath());
						PdfFileResourceStream stream =
							new PdfFileResourceStream(file, TimeUtil.getInstance()
								.currentDateTime(), "Lijst afdrukken");
						((WebResponse) getRequestCycle().getResponse()).setHeader(
							"Content-Disposition", "attachment; filename=\"" + "Jaaroverzicht.pdf"
								+ "\"");
						getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(stream));
						getRequestCycle().setRedirect(false);
					}
					catch (IOException e)
					{
						log.error(e.toString(), e);
					}
					catch (JRException e)
					{
						log.error(e.toString(), e);
					}
				}

			});
		}
	}

	private Map<Integer, Integer> totLesuren;

	public int getTotLesuur(int dagVanWeek)
	{
		if (totLesuren == null)
			initTotLesuur();
		return totLesuren.get(Integer.valueOf(dagVanWeek));
	}

	private void initTotLesuur()
	{
		totLesuren = new HashMap<Integer, Integer>();
		for (LesdagIndeling lesdag : (getLesweekIndelingModel().getObject())
			.getLesdagIndelingenOrderedByDay())
		{
			int dagVanWeek = lesdag.getDagNummer();
			int totLesuur = getTotLesuurVanDB(dagVanWeek);
			totLesuren.put(Integer.valueOf(dagVanWeek), Integer.valueOf(totLesuur));
		}
	}

	private int getTotLesuurVanDB(int dagVanWeek)
	{
		int tot = 0;
		LesdagIndeling lesdagIndeling = getLesdagIndeling(dagVanWeek);
		if (lesdagIndeling != null)
			tot = lesdagIndeling.getLesuurIndeling().size();
		WaarnemingOverzichtZoekFilter filter = getFilterModel().getObject();
		if (filter.getTotLesuur() != null && filter.getTotLesuur().getLesuur() < tot)
			tot = filter.getTotLesuur().getLesuur();
		return tot;

	}

	private LesdagIndeling getLesdagIndeling(int dagVanWeek)
	{
		for (LesdagIndeling lesdagIndeling : (getLesweekIndelingModel().getObject())
			.getLesdagIndelingenOrderedByDay())
		{
			if (lesdagIndeling.getDagNummer() == dagVanWeek)
				return lesdagIndeling;
		}
		return null;
	}

	/**
	 * @param dagVanWeek
	 * @return vanaf lesuur nummer
	 */
	public int getVanLesuur(int dagVanWeek)
	{
		WaarnemingOverzichtZoekFilter filter = getFilterModel().getObject();
		if (filter.getVanafLesuur() != null)
			return filter.getVanafLesuur().getLesuur();
		return 1;

	}

	@Override
	public IModel<LesweekIndeling> getLesweekIndelingModel()
	{
		return ModelFactory.getModel(DataAccessRegistry.getHelper(
			LesweekIndelingDataAccessHelper.class).getlesweekIndeling(getDefaultLesweekFilter()));
	}

	private LesweekindelingZoekFilter getDefaultLesweekFilter()
	{
		LesweekindelingZoekFilter filter = new LesweekindelingZoekFilter();
		filter.setLocatieModel(locatieModel);
		filter.setOrganisatieEenheidModel(organisatieEenheidModel);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		return filter;
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(locatieModel);
		ComponentUtil.detachQuietly(organisatieEenheidModel);
	}

}
