package nl.topicus.eduarte.krdparticipatie.web.components.panels.rapportage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JasperPrint;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.ColumnModel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.dataview.ExportableDataView;
import nl.topicus.cobra.web.components.link.CustomDataPanelAfdrukkenLink;
import nl.topicus.eduarte.dao.participatie.helpers.WaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.web.components.tables.ActiviteitTotaalTable;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage.ActiviteitWaarnemingTotaal;
import nl.topicus.eduarte.participatie.zoekfilters.TotalenPerGroepZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vanderkamp
 */
public class TotalenPerGroepPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private CustomDataPanelAfdrukkenLink< ? > printLink;

	public TotalenPerGroepPanel(String id, String header, Groep groep,
			TotalenPerGroepZoekFilter filter)
	{
		super(id);
		CollectionDataProvider<ActiviteitWaarnemingTotaal> provider =
			new CollectionDataProvider<ActiviteitWaarnemingTotaal>(new TotalenPerGroepModel(groep,
				filter));
		CustomDataPanel<ActiviteitWaarnemingTotaal> panel =
			new EduArteDataPanel<ActiviteitWaarnemingTotaal>("datapanel", provider,
				new ActiviteitTotaalTable<ActiviteitWaarnemingTotaal>(header, filter, "Deelnemer",
					false))
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

	private class TotalenPerGroepModel extends
			LoadableDetachableModel<List<ActiviteitWaarnemingTotaal>>
	{
		private static final long serialVersionUID = 1L;

		private TotalenPerGroepZoekFilter filter;

		private IModel<Groep> groepModel;

		private TotalenPerGroepModel(Groep groep, TotalenPerGroepZoekFilter filter)
		{
			this.filter = filter;
			this.groepModel = ModelFactory.getModel(groep);
		}

		@Override
		protected List<ActiviteitWaarnemingTotaal> load()
		{
			Groep groep = groepModel.getObject();
			List<ActiviteitWaarnemingTotaal> totalenList =
				new ArrayList<ActiviteitWaarnemingTotaal>();
			List<Waarneming> alleWaarnemingenList = new ArrayList<Waarneming>();
			for (Deelnemer deelnemer : groep.getDeelnemersOpPeildatumAsDeelnemer())
			{
				WaarnemingZoekFilter waarnemingFilter = new WaarnemingZoekFilter();
				waarnemingFilter.setDeelnemer(deelnemer);
				waarnemingFilter.setBeginDatumTijd(filter.getBeginDatum());
				waarnemingFilter.setEindDatumTijd(filter.getEindDatum());

				WaarnemingDataAccessHelper helper =
					DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class);
				List<Waarneming> waarnemingenList = helper.list(waarnemingFilter);

				totalenList.add(new ActiviteitWaarnemingTotaal(deelnemer.getDeelnemernummer()
					+ " - " + deelnemer.getPersoon().getVolledigeNaam(), waarnemingenList,
					BigDecimal.ZERO));
				alleWaarnemingenList.addAll(waarnemingenList);
			}
			totalenList.add(ActiviteitWaarnemingTotaal.createTotaal(totalenList));
			// totalenList
			// .add(new ActiviteitTotaal(null, "Totaal", alleAfsprakenList,
			// BigDecimal.ZERO));
			return totalenList;
		}

		@Override
		protected void onDetach()
		{
			super.onDetach();
			ComponentUtil.detachQuietly(filter);
			ComponentUtil.detachQuietly(groepModel);
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
