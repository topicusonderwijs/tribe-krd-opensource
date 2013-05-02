package nl.topicus.eduarte.krdparticipatie.web.components.panels.filter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.ColumnModel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.dataview.ExportableDataView;
import nl.topicus.cobra.web.components.link.CustomDataPanelAfdrukkenLink;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.BudgetDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.entities.participatie.enums.UitnodigingStatus;
import nl.topicus.eduarte.krdparticipatie.web.components.tables.ActiviteitTotaalTable;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage.ActiviteitTotaal;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakTypeZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.DeelnemerActiviteitTotalenZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vanderkamp
 */
public class DeelnemerActiviteitTotalenPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private CustomDataPanelAfdrukkenLink< ? > printLink;

	public DeelnemerActiviteitTotalenPanel(String id, Verbintenis verbintenis,
			DeelnemerActiviteitTotalenZoekFilter filter)
	{
		super(id);
		CollectionDataProvider<ActiviteitTotaal> provider =
			new CollectionDataProvider<ActiviteitTotaal>(new ActiviteitTotaalModel(verbintenis,
				filter));
		CustomDataPanel<ActiviteitTotaal> panel =
			new EduArteDataPanel<ActiviteitTotaal>("datapanel", provider,
				new ActiviteitTotaalTable<ActiviteitTotaal>("Totalen per onderwijsproduct van "
					+ verbintenis.getDeelnemer().getPersoon().getVolledigeNaam(), filter,
					"Onderwijsproduct", true))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected String getCSVTitle()
				{
					return getContentDescription().getTitle();
				}

				@Override
				protected Component createAfdrukkenLink(@SuppressWarnings("hiding") String id,
						ColumnModel<ActiviteitTotaal> columns,
						ExportableDataView<ActiviteitTotaal> dataz)
				{
					printLink =
						(CustomDataPanelAfdrukkenLink< ? >) super.createAfdrukkenLink(id, columns,
							dataz);
					return printLink;
				}
			};
		add(panel);
	}

	private class ActiviteitTotaalModel extends LoadableDetachableModel<List<ActiviteitTotaal>>
	{
		private static final long serialVersionUID = 1L;

		private DeelnemerActiviteitTotalenZoekFilter filter;

		private IModel<Verbintenis> inschrijvingModel;

		private ActiviteitTotaalModel(Verbintenis verbintenis,
				DeelnemerActiviteitTotalenZoekFilter filter)
		{
			this.filter = filter;
			this.inschrijvingModel = ModelFactory.getModel(verbintenis);
		}

		@Override
		protected List<ActiviteitTotaal> load()
		{
			Verbintenis verbintenis = inschrijvingModel.getObject();
			filter.setEindDatum(TimeUtil.getInstance().maakEindeVanDagVanDatum(
				filter.getEindDatum()));
			AfspraakZoekFilter afspraakZoekFilter = new AfspraakZoekFilter();
			afspraakZoekFilter.setDeelnemer(verbintenis.getDeelnemer());
			afspraakZoekFilter.setBeginDatumTijd(filter.getBeginDatum());
			afspraakZoekFilter.setEindDatumTijd(filter.getEindDatum());
			afspraakZoekFilter.setContract(filter.getContract());
			EnumSet<UitnodigingStatus> uitnStatussen =
				EnumSet.of(UitnodigingStatus.DIRECTE_PLAATSING, UitnodigingStatus.GEACCEPTEERD,
					UitnodigingStatus.INGETEKEND);
			afspraakZoekFilter.setUitnodigingStatussen(uitnStatussen);
			AfspraakTypeZoekFilter afspraakTypeZoekFilter = new AfspraakTypeZoekFilter();
			EnumSet<AfspraakTypeCategory> cats = EnumSet.noneOf(AfspraakTypeCategory.class);
			for (AfspraakTypeCategory category : filter.getAfspraakTypeCategories())
			{
				cats.add(category);
			}
			afspraakTypeZoekFilter.setCategories(cats);
			afspraakZoekFilter.setAfspraakTypeZoekFilter(afspraakTypeZoekFilter);
			AfspraakDataAccessHelper helper =
				DataAccessRegistry.getHelper(AfspraakDataAccessHelper.class);
			List<Afspraak> afsprakenList = new ArrayList<Afspraak>();
			if (!filter.getAfspraakTypeCategories().isEmpty())
				afsprakenList = helper.list(afspraakZoekFilter);

			List<ActiviteitTotaal> totalenList = new ArrayList<ActiviteitTotaal>();
			Map<Onderwijsproduct, List<Afspraak>> afsprakenMap =
				new HashMap<Onderwijsproduct, List<Afspraak>>();
			List<Afspraak> afsprakenZonderActiviteit = new ArrayList<Afspraak>();
			for (Afspraak afspraak : afsprakenList)
			{
				if (afspraak.getOnderwijsproduct() != null)
				{
					if (!afsprakenMap.containsKey(afspraak.getOnderwijsproduct()))
					{
						afsprakenMap.put(afspraak.getOnderwijsproduct(), new ArrayList<Afspraak>());
					}
					afsprakenMap.get(afspraak.getOnderwijsproduct()).add(afspraak);
				}
				else
				{
					afsprakenZonderActiviteit.add(afspraak);
				}
			}

			if (filter.isActiviteitenTonen() == null || filter.isActiviteitenTonen())
			{
				for (Onderwijsproduct odwpr : afsprakenMap.keySet())
				{
					BudgetDataAccessHelper budgetHelper =
						DataAccessRegistry.getHelper(BudgetDataAccessHelper.class);
					BigDecimal budget =
						new BigDecimal(budgetHelper.getBudget(odwpr, verbintenis.getDeelnemer()));
					totalenList.add(new ActiviteitTotaal(verbintenis.getDeelnemer(), odwpr
						.getCode(), afsprakenMap.get(odwpr), budget));
				}
				if (!afsprakenZonderActiviteit.isEmpty())
					totalenList.add(new ActiviteitTotaal(verbintenis.getDeelnemer(),
						"Geen activiteit", afsprakenZonderActiviteit, BigDecimal.ZERO));
			}
			totalenList.add(new ActiviteitTotaal(verbintenis.getDeelnemer(), "Totaal",
				afsprakenList, BigDecimal.ZERO));
			return totalenList;
		}

		@Override
		protected void onDetach()
		{
			super.onDetach();
			ComponentUtil.detachQuietly(filter);
			ComponentUtil.detachQuietly(inschrijvingModel);
		}
	}

	public JasperPrint getJasperPrint()
	{
		try
		{
			return printLink.getJasperPrint(null);
		}
		catch (InterruptedException e)
		{
			// ignore, kan alleen als het in de achtergrond opgestart wordt.
			return null;
		}
	}
}
