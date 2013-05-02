package nl.topicus.eduarte.krdparticipatie.web.components.panels.filter;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import nl.topicus.eduarte.dao.participatie.helpers.BudgetDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.WaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.krdparticipatie.web.components.tables.ActiviteitTotaalTable;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage.ActiviteitWaarnemingTotaal;
import nl.topicus.eduarte.participatie.zoekfilters.DeelnemerActiviteitTotalenZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vanderkamp
 */
public class DeelnemerActiviteitWaarnemingTotalenPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private CustomDataPanelAfdrukkenLink< ? > printLink;

	public DeelnemerActiviteitWaarnemingTotalenPanel(String id, Verbintenis verbintenis,
			DeelnemerActiviteitTotalenZoekFilter filter)
	{
		super(id);
		CollectionDataProvider<ActiviteitWaarnemingTotaal> provider =
			new CollectionDataProvider<ActiviteitWaarnemingTotaal>(new ActiviteitTotaalModel(
				verbintenis, filter));
		CustomDataPanel<ActiviteitWaarnemingTotaal> panel =
			new EduArteDataPanel<ActiviteitWaarnemingTotaal>("datapanel", provider,
				new ActiviteitTotaalTable<ActiviteitWaarnemingTotaal>(
					"Totalen per onderwijsproduct van "
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
						ColumnModel<ActiviteitWaarnemingTotaal> columns,
						ExportableDataView<ActiviteitWaarnemingTotaal> dataz)
				{
					printLink =
						(CustomDataPanelAfdrukkenLink< ? >) super.createAfdrukkenLink(id, columns,
							dataz);
					return printLink;
				}
			};
		add(panel);
	}

	private class ActiviteitTotaalModel extends
			LoadableDetachableModel<List<ActiviteitWaarnemingTotaal>>
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
		protected List<ActiviteitWaarnemingTotaal> load()
		{
			Verbintenis verbintenis = inschrijvingModel.getObject();
			filter.setEindDatum(TimeUtil.getInstance().maakEindeVanDagVanDatum(
				filter.getEindDatum()));
			WaarnemingZoekFilter waarnemingFilter = new WaarnemingZoekFilter();
			waarnemingFilter.setDeelnemer(verbintenis.getDeelnemer());
			waarnemingFilter.setBeginDatumTijd(filter.getBeginDatum());
			waarnemingFilter.setEindDatumTijd(filter.getEindDatum());

			WaarnemingDataAccessHelper helper =
				DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class);
			List<Waarneming> waarnemingenList = helper.list(waarnemingFilter);
			List<ActiviteitWaarnemingTotaal> totalenList =
				new ArrayList<ActiviteitWaarnemingTotaal>();
			Map<Onderwijsproduct, List<Waarneming>> waarnemingenMap =
				new HashMap<Onderwijsproduct, List<Waarneming>>();
			List<Waarneming> waarnemingenZonderActiviteit = new ArrayList<Waarneming>();
			for (Waarneming waarneming : waarnemingenList)
			{
				if (waarneming.getOnderwijsproduct() != null)
				{
					if (!waarnemingenMap.containsKey(waarneming.getOnderwijsproduct()))
					{
						waarnemingenMap.put(waarneming.getOnderwijsproduct(),
							new ArrayList<Waarneming>());
					}
					waarnemingenMap.get(waarneming.getOnderwijsproduct()).add(waarneming);
				}
				else
				{
					waarnemingenZonderActiviteit.add(waarneming);
				}
			}

			if (filter.isActiviteitenTonen() == null || filter.isActiviteitenTonen())
			{
				for (Onderwijsproduct odwpr : waarnemingenMap.keySet())
				{
					BudgetDataAccessHelper budgetHelper =
						DataAccessRegistry.getHelper(BudgetDataAccessHelper.class);
					BigDecimal budget =
						new BigDecimal(budgetHelper.getBudget(odwpr, verbintenis.getDeelnemer()));
					totalenList.add(new ActiviteitWaarnemingTotaal(odwpr.getCode(), waarnemingenMap
						.get(odwpr), budget));
				}
				if (!waarnemingenZonderActiviteit.isEmpty())
					totalenList.add(new ActiviteitWaarnemingTotaal("Geen onderwijsproduct",
						waarnemingenZonderActiviteit, BigDecimal.ZERO));
			}
			totalenList.add(new ActiviteitWaarnemingTotaal("Totaal", waarnemingenList,
				BigDecimal.ZERO));
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
