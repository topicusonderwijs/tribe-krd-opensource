package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.DecimalUtil;
import nl.topicus.cobra.web.components.datapanel.CustomColumn;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.GroupProperty.GroupPropertyList;
import nl.topicus.cobra.web.components.datapanel.columns.ClickableCustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DateTimeColumn;
import nl.topicus.cobra.web.components.datapanel.columns.TimePropertyColumn;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.dao.participatie.helpers.WaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerWaarnemingenInzien;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.filter.DeelnemerWaarnemingenZoekFilterPanel;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingZoekFilter;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.factory.ParticipatieModuleComponentFactory;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Page;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Pagina met alle waarnemingen van 1 deelnemer
 * 
 * @author vandekamp
 * @author loite
 */
@PageInfo(title = "Waarnemingen", menu = {"Deelnemer > [deelnemer] > Aanwezigheid > Waarnemingen"})
@InPrincipal(DeelnemerWaarnemingenInzien.class)
public class DeelnemerWaarnemingenPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	private final List<CustomColumn<Waarneming>> getColumns()
	{
		List<CustomColumn<Waarneming>> cols = new ArrayList<CustomColumn<Waarneming>>(10);

		cols.add(new CustomPropertyColumn<Waarneming>("Soort", "Soort", "waarnemingSoort",
			"waarnemingSoort"));
		cols.add(new CustomPropertyColumn<Waarneming>("Datum/tijd", "Datum/tijd", "beginDatumTijd",
			"beginEind"));
		cols.add(new DateTimeColumn<Waarneming>("Begindatum/tijd", "Begindatum/tijd",
			"beginDatumTijd", "beginDatumTijd").setDefaultVisible(false));
		cols.add(new DateTimeColumn<Waarneming>("Einddatum/tijd", "Einddatum/Tijd",
			"eindDatumTijd", "eindDatumTijd").setDefaultVisible(false));
		cols.add(new DatePropertyColumn<Waarneming>("Begindatum", "Begindatum", "beginDatumTijd",
			"beginDatumTijd").setDefaultVisible(false));
		cols.add(new DatePropertyColumn<Waarneming>("Einddatum", "Einddatum", "eindDatumTijd",
			"eindDatumTijd").setDefaultVisible(false));
		cols.add(new TimePropertyColumn<Waarneming>("Begintijd", "Begintijd", "beginDatumTijd",
			"beginDatumTijd").setDefaultVisible(false));
		cols.add(new TimePropertyColumn<Waarneming>("Eindtijd", "EindTijd", "eindDatumTijd",
			"eindDatumTijd").setDefaultVisible(false));
		cols.add(new CustomPropertyColumn<Waarneming>("Beginlesuur", "Beginlesuur", "beginLesuur",
			"beginLesuur").setDefaultVisible(false));
		cols.add(new CustomPropertyColumn<Waarneming>("Eindlesuur", "Eindlesuur", "eindLesuur",
			"eindLesuur").setDefaultVisible(false));
		cols.add(new CustomPropertyColumn<Waarneming>("Afgehandeld", "Afgehandeld", "afgehandeld",
			"afgehandeldOmschrijving"));
		cols.add(new ClickableCustomPropertyColumn<Waarneming>("Absentiemelding",
			"Absentiemelding", "absentieMelding.absentieReden.omschrijving")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(IModel<Waarneming> model)
			{
				Waarneming waarneming = model.getObject();
				List<ParticipatieModuleComponentFactory> factories =
					EduArteApp.get().getPanelFactories(ParticipatieModuleComponentFactory.class);
				if (factories.size() == 1)
				{
					setResponsePage(factories.get(0).newAbsentieMeldingToevoegenPage(
						getContextDeelnemer(), getContextVerbintenis(),
						waarneming.getAbsentieMelding(), getPage()));
				}
			}
		});
		if (EduArteApp.get().isModuleActive(EduArteModuleKey.PARTICIPATIE))
		{
			cols.add(new CustomPropertyColumn<Waarneming>("Afspraak", "Afspraak", "afspraak.titel"));
			cols.add(new CustomPropertyColumn<Waarneming>("Onderwijsproduct", "Onderwijsproduct",
				"afspraak.onderwijsproduct"));
			cols.add(new CustomPropertyColumn<Waarneming>("Locatie", "Locatie", "afspraak.locatie")
				.setDefaultVisible(false));
		}
		cols.add(new CustomPropertyColumn<Waarneming>("Laatst gewijzigd door",
			"Laatst gewijzigd door", "lastModifiedBy.eigenaar.volledigeNaam"));
		cols.add(new CustomPropertyColumn<Waarneming>("Aangemaakt door", "Aangemaakt door",
			"createdBy.eigenaar.volledigeNaam").setDefaultVisible(false));
		cols.add(new DateTimeColumn<Waarneming>("Datum laatst gewijzigd", "Datum laatst gewijzigd",
			"lastModifiedAt").setDefaultVisible(false));
		cols.add(new DateTimeColumn<Waarneming>("Datum aangemaakt", "Datum aangemaakt", "createdAt")
			.setDefaultVisible(false));

		return cols;
	}

	private static final GroupPropertyList<Waarneming> getGroupProperties()
	{
		GroupPropertyList<Waarneming> res = new GroupPropertyList<Waarneming>(5);
		res.add(new GroupProperty<Waarneming>("waarnemingSoort", "Soort", "waarnemingSoort"));
		res.add(new GroupProperty<Waarneming>("groepeerDatumOmschrijving", "Datum/tijd",
			"beginDatumTijd"));
		res.add(new GroupProperty<Waarneming>("afgehandeldOmschrijving", "Afgehandeld",
			"afgehandeld"));
		return res;
	}

	private static final WaarnemingZoekFilter getDefaultFilter(Deelnemer deelnemer)
	{
		WaarnemingZoekFilter filter = new WaarnemingZoekFilter();
		filter.setDeelnemer(deelnemer);
		filter.addOrderByProperty("beginDatumTijd");
		filter.setAscending(false);
		filter.setDatumTijdExactGelijk(false);
		return filter;
	}

	private final DeelnemerWaarnemingenZoekFilterPanel filterPanel;

	private final Page returnPage;

	public DeelnemerWaarnemingenPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer());
	}

	public DeelnemerWaarnemingenPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum());
	}

	public DeelnemerWaarnemingenPage(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis);
	}

	public DeelnemerWaarnemingenPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		this(deelnemer, verbintenis, getDefaultFilter(deelnemer));
	}

	public DeelnemerWaarnemingenPage(Deelnemer deelnemer, Verbintenis verbintenis, Page returnPage)
	{
		this(deelnemer, verbintenis, getDefaultFilter(deelnemer), returnPage);
	}

	public DeelnemerWaarnemingenPage(Deelnemer deelnemer, Verbintenis verbintenis,
			WaarnemingZoekFilter filter)
	{
		this(deelnemer, verbintenis, filter, null);
	}

	public DeelnemerWaarnemingenPage(Deelnemer deelnemer, Verbintenis verbintenis,
			WaarnemingZoekFilter filter, Page returnPage)
	{
		super(ParticipatieDeelnemerMenuItem.Waarnemingen, deelnemer, verbintenis);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		this.returnPage = returnPage;
		IDataProvider<Waarneming> provider =
			GeneralDataProvider.of(filter, WaarnemingDataAccessHelper.class);

		CustomDataPanelContentDescription<Waarneming> contDesc =
			new CustomDataPanelContentDescription<Waarneming>("Waarnemingen");
		contDesc.setColumns(getColumns());
		contDesc.setGroupProperties(getGroupProperties());

		EduArteDataPanel<Waarneming> datapanel =
			new EduArteDataPanel<Waarneming>("datapanel", provider, contDesc);
		List<ParticipatieModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(ParticipatieModuleComponentFactory.class);
		if (!factories.isEmpty())
			datapanel.setRowFactory(factories.get(0).getEditWaarnemingenPageRowFactory(getPage(),
				getContextDeelnemerModel(), getContextVerbintenisModel()));
		add(datapanel);

		CollectionDataProvider<WaarnemingenTotaalTelling> collectionDataProvider =
			new CollectionDataProvider<WaarnemingenTotaalTelling>(new WaarnemingTotaalTellingModel(
				filter));
		CustomDataPanelContentDescription<WaarnemingenTotaalTelling> totaalPanelDesc =
			new CustomDataPanelContentDescription<WaarnemingenTotaalTelling>("Waarnemingen totalen");

		totaalPanelDesc.setColumns(getTotaalTellingColumns());

		EduArteDataPanel<WaarnemingenTotaalTelling> totaalTellingPanel =
			new EduArteDataPanel<WaarnemingenTotaalTelling>("totaalTellingPanel",
				collectionDataProvider, totaalPanelDesc);

		add(totaalTellingPanel);
		filterPanel =
			new DeelnemerWaarnemingenZoekFilterPanel("filter", filter, datapanel,
				getContextVerbintenis() == null ? null : getContextVerbintenis()
					.getOrganisatieEenheid());
		add(filterPanel);
		createComponents();
	}

	@Override
	public String getWikiName()
	{
		return super.getWikiName() + " voor participatie";
	}

	private static final List<CustomColumn<WaarnemingenTotaalTelling>> getTotaalTellingColumns()
	{
		List<CustomColumn<WaarnemingenTotaalTelling>> cols =
			new ArrayList<CustomColumn<WaarnemingenTotaalTelling>>(10);
		cols.add(new CustomPropertyColumn<WaarnemingenTotaalTelling>("Waarneming", "Waarneming",
			"waarnemingSoort"));
		cols.add(new CustomPropertyColumn<WaarnemingenTotaalTelling>("Reden", "Reden",
			"absentieRedenOmschrijving"));
		cols.add(new CustomPropertyColumn<WaarnemingenTotaalTelling>("Klokuren", "Klokuren",
			"aantalKlokuren"));
		cols.add(new CustomPropertyColumn<WaarnemingenTotaalTelling>("Lesuren", "Lesuren",
			"aantalLesuren"));
		cols.add(new CustomPropertyColumn<WaarnemingenTotaalTelling>("Percentage aanwezig", "%",
			"percentageAanwezig"));
		return cols;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		if (returnPage != null)
			panel.addButton(new TerugButton(panel, returnPage));
		List<ParticipatieModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(ParticipatieModuleComponentFactory.class);
		for (ParticipatieModuleComponentFactory factory : factories)
		{
			factory.newWaarnemingToevoegenKnop(panel, getContextDeelnemerModel(),
				getContextVerbintenisModel(), this);
		}
	}

	private class WaarnemingTotaalTellingModel extends
			LoadableDetachableModel<List<WaarnemingenTotaalTelling>>
	{
		private static final long serialVersionUID = 1L;

		private WaarnemingZoekFilter filter;

		private WaarnemingTotaalTellingModel(WaarnemingZoekFilter filter)
		{
			this.filter = filter;

		}

		@Override
		protected List<WaarnemingenTotaalTelling> load()
		{
			Map<WaarnemingSoort, List<Waarneming>> waarnemingSoortMap =
				new HashMap<WaarnemingSoort, List<Waarneming>>();
			Map<String, List<Waarneming>> absentieRedenMap =
				new HashMap<String, List<Waarneming>>();
			WaarnemingDataAccessHelper helper =
				DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class);
			List<Waarneming> waarnemingenList = helper.list(filter);
			long totaalAanbodSeconden = 0;
			for (Waarneming waarneming : waarnemingenList)
			{
				if (!waarneming.getWaarnemingSoort().equals(WaarnemingSoort.Nvt))
				{
					totaalAanbodSeconden += waarneming.getDuurInSeconds();
					WaarnemingSoort key = waarneming.getWaarnemingSoort();
					if (!waarnemingSoortMap.containsKey(key))
					{
						waarnemingSoortMap.put(key, new ArrayList<Waarneming>());
					}
					waarnemingSoortMap.get(key).add(waarneming);

					if (waarneming.getWaarnemingSoort().equals(WaarnemingSoort.Afwezig))
					{
						String keyAbsentie = "Geen reden";
						if (waarneming.getAbsentieMelding() != null)
							keyAbsentie =
								waarneming.getAbsentieMelding().getAbsentieReden()
									.getOmschrijving();

						if (!absentieRedenMap.containsKey(keyAbsentie))
						{
							absentieRedenMap.put(keyAbsentie, new ArrayList<Waarneming>());
						}
						absentieRedenMap.get(keyAbsentie).add(waarneming);
					}
				}
			}
			List<WaarnemingenTotaalTelling> totalenList =
				new ArrayList<WaarnemingenTotaalTelling>();
			for (WaarnemingSoort soort : WaarnemingSoort.values())
			{
				if (waarnemingSoortMap.containsKey(soort))
					totalenList.add(createWaarnemingenTotaalTelling(soort.toString(),
						waarnemingSoortMap.get(soort), totaalAanbodSeconden, true));
			}
			for (String omschrijving : absentieRedenMap.keySet())
			{
				totalenList.add(createWaarnemingenTotaalTelling(omschrijving,
					absentieRedenMap.get(omschrijving), totaalAanbodSeconden, false));
			}
			return totalenList;
		}

		private WaarnemingenTotaalTelling createWaarnemingenTotaalTelling(String omschrijving,
				List<Waarneming> waarnemingenList, long totaalAanbodSeconden,
				boolean isWaarnemingSoort)
		{
			WaarnemingenTotaalTelling totaalTelling = new WaarnemingenTotaalTelling();
			if (isWaarnemingSoort)
				totaalTelling.setWaarnemingSoort(omschrijving);
			else
				totaalTelling.setAbsentieRedenOmschrijving(omschrijving);
			long klokSeconds = 0;
			int lesuren = 0;
			for (Waarneming waarneming : waarnemingenList)
			{
				klokSeconds += waarneming.getDuurInSeconds();
				lesuren += waarneming.getDuurInLesuren();
			}
			totaalTelling.setAantalKlokuren(getKlokUren(klokSeconds));
			totaalTelling.setAantalLesuren(lesuren);
			totaalTelling
				.setPercentageAanwezig(getProcent(totaalAanbodSeconden, klokSeconds) + "%");

			return totaalTelling;
		}

		private BigDecimal getProcent(long totaal, long gedeelte)
		{
			if (totaal == 0 || gedeelte == 0)
				return BigDecimal.ZERO;
			BigDecimal aanwezigBigd = BigDecimal.valueOf(gedeelte);
			BigDecimal aanbodBigd = BigDecimal.valueOf(totaal);
			return aanwezigBigd.multiply(DecimalUtil.HUNDRED).divide(aanbodBigd,
				RoundingMode.HALF_UP);
		}

		private BigDecimal getKlokUren(long seconds)
		{
			BigDecimal totaal = new BigDecimal(seconds);
			BigDecimal uur = new BigDecimal(3600);
			BigDecimal aantalUren = BigDecimal.ZERO;
			if (seconds > 0)
			{
				aantalUren = (totaal.divide(uur, 1, RoundingMode.HALF_UP));
			}
			return aantalUren;
		}

		@Override
		protected void onDetach()
		{
			super.onDetach();
			ComponentUtil.detachQuietly(filter);
		}
	}
}
