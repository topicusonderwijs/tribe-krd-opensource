/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.fileresources.PdfFileResourceStream;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerWeekOverzichtRapportage;
import nl.topicus.eduarte.krdparticipatie.rapportage.WeekoverzichtRapportageUtil;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.models.LegendaItem;
import nl.topicus.eduarte.krdparticipatie.web.components.models.LegendaModel;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieWeekOverzichtUtil.AbsentieModel;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieWeekOverzichtUtil.AfspraakModel;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieWeekOverzichtUtil.UrenModel;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieWeekOverzichtUtil.WaarnemingenModel;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieWeekOverzichtUtil.WeekModel;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieWeekOverzichtUtil.WeekPanel;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidsWeekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidsWeekZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidsWeekFilter.Week;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Toont afspraken, waarnemeningen en absentienmeldingen van een leerling uitgesplitst per
 * week.
 * 
 * @author vandekamp
 */
@PageInfo(title = "Weekoverzicht", menu = "Deelnemer > [deelnemer] > Aanwezigheid > Weekoverzicht")
@InPrincipal(DeelnemerWeekOverzichtRapportage.class)
public class ParticipatieWeekOverzichtPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(ParticipatieWeekOverzichtPage.class);

	/**
	 * Starttijd van een dag
	 */
	private int startUur = 7;

	/**
	 * Eindtijd van een dag
	 */
	private int eindUur = 23;

	/**
	 * Tijdsduur van elk blok in minuten.
	 */
	private static final int blokSize = 15;

	/**
	 * Aantal blokken in een uur gegeven de blokSize.
	 */
	private static final int nrBloks = 60 / blokSize;

	private final AanwezigheidsWeekFilter filter;

	private UrenModel urenModel;

	private final AfspraakModel afspraakModel;

	private final WaarnemingenModel waarnemingenModel;

	private final AbsentieModel absentieModel;

	private IModel<LesweekIndeling> lesweekModel;

	private WeekAanwezigheidZoekFilterPanel filterPanel;

	public ParticipatieWeekOverzichtPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer().getEersteInschrijvingOpPeildatum());
	}

	public ParticipatieWeekOverzichtPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum());
	}

	public ParticipatieWeekOverzichtPage(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis);
	}

	public ParticipatieWeekOverzichtPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		this(deelnemer, verbintenis, new AanwezigheidsWeekZoekFilter());
	}

	public ParticipatieWeekOverzichtPage(Deelnemer deelnemer, Verbintenis verbintenis,
			final AanwezigheidsWeekFilter filter)
	{
		super(ParticipatieDeelnemerMenuItem.Weekoverzicht, deelnemer, verbintenis);
		this.filter = filter;
		filter.setDeelnemer(deelnemer);

		this.afspraakModel = new AfspraakModel(filter, this);
		this.waarnemingenModel = new WaarnemingenModel(filter);
		this.absentieModel = new AbsentieModel(filter);
		lesweekModel =
			ParticipatieWeekOverzichtUtil.getIndeling(verbintenis.getOrganisatieEenheid(),
				verbintenis.getLocatie());

		if (lesweekModel.getObject() == null)
			geefFoutmelding();
		else
			geefWeekOverzicht(lesweekModel.getObject());

		createComponents();
	}

	private void geefFoutmelding()
	{
		add(new WebMarkupContainer("filter").setVisible(false));
		add(new WebMarkupContainer("beginweek").setVisible(false));
		add(new WebMarkupContainer("eindweek").setVisible(false));
		add(new WebMarkupContainer("weken").setVisible(false));
		add(new WebMarkupContainer("legenda").setVisible(false));

		error("Er is voor deze deelnemer geen gekoppelde lesweek indeling gevonden.");
	}

	private void geefWeekOverzicht(LesweekIndeling weekindeling)
	{
		filterPanel = new WeekAanwezigheidZoekFilterPanel("filter", filter);
		add(filterPanel);
		// Bepaal begin- en eind.
		int minBeginuur = -1;
		int maxEinduur = -1;
		for (LesdagIndeling indeling : weekindeling.getLesdagIndelingenOrderedByDay())
		{
			for (LesuurIndeling lesuur : indeling.getLesuurIndeling())
			{
				int beginuur = TimeUtil.getInstance().getHour(lesuur.getBeginTijd());
				int einduur = TimeUtil.getInstance().getHour(lesuur.getEindTijd());
				int eindminuten = TimeUtil.getInstance().getMinutes(lesuur.getEindTijd());
				if (eindminuten > 0)
					einduur = einduur + 1;
				if (minBeginuur == -1 || beginuur < minBeginuur)
					minBeginuur = beginuur;
				if (einduur > maxEinduur)
					maxEinduur = einduur;
			}
		}
		if (minBeginuur > -1)
			startUur = minBeginuur;
		if (maxEinduur > -1)
			eindUur = maxEinduur;
		this.urenModel = new UrenModel(startUur, eindUur);
		add(ComponentFactory.getDataLabel("beginweek", new PropertyModel<Integer>(filter,
			"beginWeek")));
		add(ComponentFactory.getDataLabel("eindweek",
			new PropertyModel<Integer>(filter, "eindWeek")));
		add(new ListView<Week>("weken", new WeekModel(filter))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Week> item)
			{
				Week week = item.getModelObject();
				int jaar = TimeUtil.getInstance().getYear(week.getStart());
				String dataString =
					" (" + TimeUtil.getInstance().formatDate(week.getStart()) + " - "
						+ TimeUtil.getInstance().formatDate(week.getEnd()) + ")";

				WeekPanel panel =
					new WeekPanel("week", item.getModel(), urenModel, nrBloks, startUur, eindUur,
						afspraakModel, blokSize, waarnemingenModel, absentieModel,
						getContextVerbintenis(), "Aanwezigheid week " + week.getWeekNr() + "-"
							+ jaar + dataString);
				panel.open();
				item.add(panel);
				item.setRenderBodyOnly(true);
			}
		});
		WebMarkupContainer legenda = new WebMarkupContainer("legenda");
		legenda.add(new ListView<LegendaItem>("legendaItem", new LegendaModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<LegendaItem> item)
			{
				LegendaItem legendaItem = item.getModelObject();
				Label kleur =
					new Label("kleur", new Model<String>("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
				kleur.setEscapeModelStrings(false);
				kleur.add(new AttributeAppender("class", new Model<String>(legendaItem.getColor()),
					","));
				item.add(kleur);
				item
					.add(new Label("omschrijving", new Model<String>(legendaItem.getOmschrijving())));
			}
		});
		add(legenda);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{

		if (lesweekModel.getObject() != null)
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
							ResourceUtil.readClassPathFileAsBytes(
								ParticipatieWeekOverzichtPage.class,
								"ParticipatieLandscapeWeekView.jrxml");
						WeekoverzichtRapportageUtil rapport =
							new WeekoverzichtRapportageUtil(filter);
						JasperPrint print =
							rapport.generateDocuments(Arrays.asList(getContextVerbintenis()),
								bestand);
						File file = File.createTempFile("lijst", ".pdf");
						JasperExportManager.exportReportToPdfFile(print, file.getPath());
						PdfFileResourceStream stream =
							new PdfFileResourceStream(file, TimeUtil.getInstance()
								.currentDateTime(), "Lijst afdrukken");
						((WebResponse) getRequestCycle().getResponse()).setHeader(
							"Content-Disposition", "attachment; filename=\"" + "Weekoverzicht.pdf"
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

	@Override
	public void detachModels()
	{
		super.detachModels();
		afspraakModel.detach();
		waarnemingenModel.detach();
		absentieModel.detach();
		lesweekModel.detach();
		filter.detach();
	}

	public IModel<AanwezigheidsWeekFilter> getFilterModel()
	{
		return filterPanel.getFilterModel();
	}

	public AanwezigheidsWeekFilter getFilter()
	{
		return filter;
	}

}
