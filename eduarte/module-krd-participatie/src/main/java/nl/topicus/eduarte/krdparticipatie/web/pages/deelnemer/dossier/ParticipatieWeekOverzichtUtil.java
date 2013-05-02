/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.wiquery.CollapsablePanel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieMeldingDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekIndelingDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.WaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.IParticipatieBlokObject;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidsWeekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieMeldingZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.LesweekindelingZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidsWeekFilter.Dag;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidsWeekFilter.Week;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;

/**
 * Toont afspraken, waarnemeningen en absentienmeldingen van een leerling uitgesplitst per
 * week.
 * 
 * @author vandekamp
 */
public final class ParticipatieWeekOverzichtUtil
{
	private ParticipatieWeekOverzichtUtil()
	{
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Model voor een lijst met weken. Deze lijst komt overeen met de gekozen weken van
	 * het filter.
	 * 
	 * @author marrink
	 */
	public static final class WeekModel extends LoadableDetachableModel<List<Week>>
	{
		private static final long serialVersionUID = 1L;

		public AanwezigheidsWeekFilter filter;

		public WeekModel(AanwezigheidsWeekFilter filter)
		{
			this.filter = filter;
		}

		@Override
		protected List<Week> load()
		{
			return filter.getWeken();
		}
	}

	public static final class WeekPanel extends CollapsablePanel<Void>
	{
		private static final long serialVersionUID = 1L;

		public WeekPanel(String id, IModel<Week> week, final UrenModel urenModel,
				final int nrBloks, final int startUur, final int eindUur,
				final AfspraakModel afspraakModel, final int blokSize,
				final WaarnemingenModel waarnemingenModel, final AbsentieModel absentieModel,
				Verbintenis verbintenis, String label)
		{
			super(id, label);
			LesweekIndeling indeling =
				getIndeling(verbintenis.getOrganisatieEenheid(), verbintenis.getLocatie())
					.getObject();
			if (indeling == null)
			{
				this.error("Er is geen Lesweek indeling bekend voor deze organisatie eenheid.");
				add(new WebMarkupContainer("dagen").setVisible(false));
			}
			else
			{
				add(new ListView<Dag>("dagen", new DagModel(week.getObject(), verbintenis
					.getOrganisatieEenheid(), verbintenis.getLocatie()))
				{

					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<Dag> item)
					{
						item.setRenderBodyOnly(true);
						item.add(new DagPanel("dag", item.getModel(), urenModel, nrBloks, startUur,
							eindUur, afspraakModel, blokSize, waarnemingenModel, absentieModel));
					}
				});
			}
		}
	}

	public static IModel<LesweekIndeling> getIndeling(OrganisatieEenheid organisatieEenheid,
			Locatie locatie)
	{
		LesweekindelingZoekFilter filter = new LesweekindelingZoekFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));
		filter.setLocatie(locatie);
		filter.setOrganisatieEenheid(organisatieEenheid);

		LesweekIndeling indeling =
			(DataAccessRegistry.getHelper(LesweekIndelingDataAccessHelper.class)
				.getlesweekIndeling(filter));

		return ModelFactory.getModel(indeling, new DefaultModelManager(LesweekIndeling.class));
	}

	/**
	 * Model voor een lijst met dagen uit een week.
	 * 
	 * @author marrink
	 */
	public static final class DagModel extends LoadableDetachableModel<List<Dag>>
	{
		private static final long serialVersionUID = 1L;

		private final Week week;

		private IModel<OrganisatieEenheid> organsatieEenheidModel;

		private IModel<Locatie> locatieModel;

		public DagModel(Week week, OrganisatieEenheid organisatieEenheid, Locatie locatie)
		{
			super();
			this.week = week;
			this.organsatieEenheidModel = ModelFactory.getModel(organisatieEenheid);
			this.locatieModel = ModelFactory.getModel(locatie);
			getObject();
		}

		@Override
		protected List<Dag> load()
		{
			List<Dag> dagen = new ArrayList<Dag>();
			LesweekIndeling indeling =
				(getIndeling(organsatieEenheidModel.getObject(), locatieModel.getObject()))
					.getObject();
			if (indeling != null)
			{
				List<Integer> lesdagen = new ArrayList<Integer>();
				for (LesdagIndeling lesdag : indeling.getLesdagIndelingenOrderedByDay())
				{
					lesdagen.add(lesdag.getDagNummer());
				}
				for (Dag dag : week.getDagen())
				{
					if (lesdagen.contains(dag.getWeekdag()))
						dagen.add(dag);
				}
			}
			return dagen;
		}

		@Override
		protected void onDetach()
		{
			ComponentUtil.detachQuietly(organsatieEenheidModel);
			ComponentUtil.detachQuietly(locatieModel);
			super.onDetach();
		}

	}

	private static final class DagPanel extends TypedPanel<Dag>
	{
		private static final long serialVersionUID = 1L;

		public DagPanel(String id, IModel<Dag> dag, UrenModel urenModel, final int nrBloks,
				int startUur, int eindUur, AfspraakModel afspraakModel, int blokSize,
				WaarnemingenModel waarnemingenModel, AbsentieModel absentieModel)
		{
			super(id, dag);
			setRenderBodyOnly(true);
			add(new Label("title", dag).add(new SimpleAttributeModifier("colspan", urenModel
				.aantalUren()
				* nrBloks + "")));
			add(new ListView<String>("headers", urenModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<String> item)
				{
					item.setRenderBodyOnly(true);
					item.add(new Label("uur", item.getModel()).add(new SimpleAttributeModifier(
						"colspan", "" + nrBloks)));
				}
			});
			WebMarkupContainer afsprakenContainer = new WebMarkupContainer("afsprakenContainer");
			afsprakenContainer.setVisibilityAllowed(EduArteApp.get().isModuleActive(
				EduArteModuleKey.PARTICIPATIE));
			add(afsprakenContainer);
			WebMarkupContainer afspraken = new WebMarkupContainer("afspraken");
			afsprakenContainer.add(afspraken);
			afspraken.setRenderBodyOnly(true);
			WebMarkupContainer waarnemingen = new WebMarkupContainer("waarnemingen");
			add(waarnemingen);
			waarnemingen.setRenderBodyOnly(true);
			WebMarkupContainer absentie = new WebMarkupContainer("absenties");
			add(absentie);
			absentie.setRenderBodyOnly(true);

			Date datum = (dag.getObject()).getDatum();
			BlokkenGenerator blokkenGenerator = new BlokkenGenerator(startUur, eindUur, blokSize);
			List<IParticipatieBlokObject> afspraakElementen = afspraakModel.getObject();
			afspraken.add(new BlokListView("lessen", blokkenGenerator.maakBlokken(datum,
				afspraakElementen)));
			List<IParticipatieBlokObject> waarnemingElementen = waarnemingenModel.getObject();
			waarnemingen.add(new BlokListView("lessen", blokkenGenerator.maakBlokken(datum,
				waarnemingElementen)));
			List<IParticipatieBlokObject> absentieElementen = absentieModel.getObject();
			absentie.add(new BlokListView("lessen", blokkenGenerator.maakBlokken(datum,
				absentieElementen)));
		}
	}

	private static final class BlokListView extends ListView<Blok>
	{
		private static final long serialVersionUID = 1L;

		public BlokListView(String id, List<Blok> blokken)
		{
			super(id, blokken);
			setRenderBodyOnly(true);
		}

		@Override
		protected void populateItem(ListItem<Blok> item)
		{
			Blok blok = item.getModelObject();
			item.setRenderBodyOnly(true);
			WebMarkupContainer les = new WebMarkupContainer("les");
			les.add(new AttributeModifier("class", true, new Model<String>(blok.getCssClass())));
			les.add(new AttributeModifier("colspan", true, new Model<Integer>(blok
				.getAantalBlokken())));
			les.add(new AttributeModifier("title", true, new Model<String>(blok.getTitle())));
			item.add(les);
		}

	}

	/**
	 * Model voor de header van een dag. Bevat de hele uren van de dag waarop les gegeven
	 * wordt (inclusief). Let op hele uren dus komt mogelijk niet overeen met de
	 * lestijden. Standaard van 07:00 tot 23:00
	 * 
	 * @author marrink
	 */
	public static final class UrenModel extends LoadableDetachableModel<List<String>>
	{
		private static final long serialVersionUID = 1L;

		private int startUur;

		private int eindUur;

		public UrenModel(int startUur, int eindUur)
		{
			this.startUur = startUur;
			this.eindUur = eindUur;
		}

		@Override
		protected List<String> load()
		{
			List<String> uren = new ArrayList<String>(aantalUren());
			for (int i = startUur; i < eindUur; i++)
				uren.add((i < 10 ? "0" : "") + i + ":00");
			return uren;
		}

		public int aantalUren()
		{
			return eindUur - startUur;
		}

		public Date getStart()
		{
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			cal.set(Calendar.HOUR_OF_DAY, startUur);
			return cal.getTime();
		}

		public Date getEnd()
		{
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			cal.set(Calendar.HOUR_OF_DAY, eindUur);
			return cal.getTime();
		}
	}

	/**
	 * @author vanderkamp
	 */
	public static final class AfspraakModel extends
			LoadableDetachableModel<List<IParticipatieBlokObject>>
	{
		private static final long serialVersionUID = 1L;

		private AanwezigheidsWeekFilter filter;

		private Component parent;

		public AfspraakModel(AanwezigheidsWeekFilter filter, Component parent)
		{
			this.filter = filter;
			this.parent = parent;
		}

		@Override
		protected List<IParticipatieBlokObject> load()
		{
			AfspraakZoekFilter afspraakZoekFilter = new AfspraakZoekFilter();
			if (parent != null)
				afspraakZoekFilter
					.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
						parent));
			else
				afspraakZoekFilter
					.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
						new AlwaysGrantedSecurityCheck()));
			afspraakZoekFilter.setDeelnemer(filter.getDeelnemer());
			afspraakZoekFilter.setBeginDatumTijd(filter.getBeginDatum());
			afspraakZoekFilter.setEindDatumTijd(filter.getEindDatum());
			AfspraakDataAccessHelper helper =
				DataAccessRegistry.getHelper(AfspraakDataAccessHelper.class);
			List<Afspraak> afspraken = helper.list(afspraakZoekFilter);
			List<IParticipatieBlokObject> res =
				new ArrayList<IParticipatieBlokObject>(afspraken.size());
			res.addAll(afspraken);
			return res;
		}
	}

	/**
	 * @author vanderkamp
	 */
	public static final class WaarnemingenModel extends
			LoadableDetachableModel<List<IParticipatieBlokObject>>
	{
		private static final long serialVersionUID = 1L;

		private AanwezigheidsWeekFilter filter;

		public WaarnemingenModel(AanwezigheidsWeekFilter filter)
		{
			this.filter = filter;
		}

		@Override
		protected List<IParticipatieBlokObject> load()
		{
			WaarnemingZoekFilter waarnemingZoekFilter = new WaarnemingZoekFilter();
			waarnemingZoekFilter.setDeelnemer(filter.getDeelnemer());
			waarnemingZoekFilter.setBeginDatumTijd(filter.getBeginDatum());
			waarnemingZoekFilter.setEindDatumTijd(filter.getEindDatum());
			waarnemingZoekFilter.setZonderNvtWaarnemingen(true);
			WaarnemingDataAccessHelper helper =
				DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class);
			List<Waarneming> waarnemingen =
				helper.getOverlappendeWaarnemingen(waarnemingZoekFilter);
			List<IParticipatieBlokObject> res =
				new ArrayList<IParticipatieBlokObject>(waarnemingen.size());
			res.addAll(waarnemingen);
			return res;
		}
	}

	/**
	 * @author vanderkamp
	 */
	public static final class AbsentieModel extends
			LoadableDetachableModel<List<IParticipatieBlokObject>>
	{
		private static final long serialVersionUID = 1L;

		public AanwezigheidsWeekFilter filter;

		public AbsentieModel(AanwezigheidsWeekFilter filter)
		{
			this.filter = filter;
		}

		@Override
		protected List<IParticipatieBlokObject> load()
		{
			AbsentieMeldingZoekFilter absentieMeldingZoekFilter = new AbsentieMeldingZoekFilter();
			absentieMeldingZoekFilter.setDeelnemer(filter.getDeelnemer());
			absentieMeldingZoekFilter.setBeginDatumTijd(filter.getBeginDatum());
			AbsentieMeldingDataAccessHelper helper =
				DataAccessRegistry.getHelper(AbsentieMeldingDataAccessHelper.class);
			List<AbsentieMelding> absentieMeldingen =
				helper.getOverlappendeMeldingen(absentieMeldingZoekFilter);

			// Medlingen zonder einddatum moeten weergegeven worden tot de huidige
			// datum/tijd
			for (AbsentieMelding melding : absentieMeldingen)
			{
				if (melding.getEindDatumTijd() == null)
					melding.setEindDatumTijd(TimeUtil.getInstance().currentDateTime());
			}

			List<IParticipatieBlokObject> res =
				new ArrayList<IParticipatieBlokObject>(absentieMeldingen.size());
			res.addAll(absentieMeldingen);
			return res;
		}
	}

	/**
	 * @author vanderkamp
	 */
	public static final class Blok implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private final int aantalBlokken;

		private final Date begintijd;

		private final Date eindtijd;

		private final String cssClass;

		private final String title;

		public Blok(int aantalBlokken, Date begintijd, Date eindtijd, String cssClass, String title)
		{
			this.aantalBlokken = aantalBlokken;
			this.begintijd = begintijd;
			this.eindtijd = eindtijd;
			this.cssClass = cssClass;
			this.title = title;
		}

		public Date getBegintijd()
		{
			return begintijd;
		}

		public Date getEindtijd()
		{
			return eindtijd;
		}

		public String getCssClass()
		{
			return cssClass;
		}

		public String getTitle()
		{
			return title;
		}

		public int getAantalBlokken()
		{
			return aantalBlokken;
		}
	}
}
