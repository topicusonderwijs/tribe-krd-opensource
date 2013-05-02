package nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.wiquery.CollapsablePanel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.dao.helpers.BPVInschrijvingDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.GroepsdeelnameDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.rapportage.model.DeelnemerResultatenRapportageModel;
import nl.topicus.eduarte.web.components.factory.BegeleidingModuleComponentFactory;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVInschrijvingTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerGroepsdeelnameTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OnderwijsproductAfnameTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.VerbintenisTable;
import nl.topicus.eduarte.web.components.resultaat.ResultatenModel;
import nl.topicus.eduarte.zoekfilters.BPVInschrijvingZoekFilter;
import nl.topicus.eduarte.zoekfilters.GroepsdeelnameZoekFilter;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductAfnameZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class DeelnemerkaartDetailPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public DeelnemerkaartDetailPanel(String id, IModel<Deelnemer> deelnemerModel,
			IModel<Verbintenis> verbintenisModel)
	{
		super(id);

		add(new AfgenomenOnderwijsproductenPanel("onderwijsproducten", verbintenisModel));
		add(new VerbintenissenPanel("verbintenissen", deelnemerModel));
		if (verbintenisModel.getObject() == null)
			add(new EmptyPanel("bpvs"));
		else
			add(new BPVsPanel("bpvs", verbintenisModel));
		add(new GroepenPanel("groepen", deelnemerModel));
		if (verbintenisModel.getObject() == null)
			add(new EmptyPanel("resultaten"));
		else
			add(new ResultatenPanel("resultaten", verbintenisModel));

		addBegeleidingPanel(deelnemerModel);
	}

	private void addBegeleidingPanel(IModel<Deelnemer> deelnemerModel)
	{
		List<BegeleidingModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(BegeleidingModuleComponentFactory.class);
		if (factories.size() > 0)
			add(factories.get(0).getDeelnemerkaartPanel("begeleiding", deelnemerModel));
		else
			add(new WebMarkupContainer("begeleiding").setVisible(false));
	}

	private class AfgenomenOnderwijsproductenPanel extends CollapsablePanel<Verbintenis>
	{
		private static final long serialVersionUID = 1L;

		public AfgenomenOnderwijsproductenPanel(String id, IModel<Verbintenis> verbintenisModel)
		{
			super(id, verbintenisModel, "Afgenomen onderwijsproducten");
		}

		@Override
		protected void createContents()
		{
			OnderwijsproductAfnameZoekFilter filter =
				new OnderwijsproductAfnameZoekFilter(getModelObject().getDeelnemer());
			IDataProvider<OnderwijsproductAfname> provider =
				GeneralFilteredSortableDataProvider.of(filter,
					OnderwijsproductAfnameDataAccessHelper.class);
			filter.addOrderByProperty("begindatum");
			filter.setAscending(false);
			CustomDataPanel<OnderwijsproductAfname> datapanel =
				new EduArteDataPanel<OnderwijsproductAfname>("datapanel", provider,
					new OnderwijsproductAfnameTable(false, getModel()));
			datapanel.setItemsPerPage(20);
			add(datapanel);
		}
	}

	private class VerbintenissenPanel extends CollapsablePanel<Deelnemer>
	{
		private static final long serialVersionUID = 1L;

		public VerbintenissenPanel(String id, IModel<Deelnemer> deelnemerModel)
		{
			super(id, deelnemerModel, "Actieve verbintenissen");
		}

		@Override
		protected void createContents()
		{
			VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
			filter.setDeelnemernummer(getModelObject().getDeelnemernummer());
			List<VerbintenisStatus> statusOngelijkAan = new ArrayList<VerbintenisStatus>();
			statusOngelijkAan.add(VerbintenisStatus.Afgemeld);
			statusOngelijkAan.add(VerbintenisStatus.Afgewezen);
			statusOngelijkAan.add(VerbintenisStatus.Beeindigd);
			filter.setVerbintenisStatusOngelijkAan(statusOngelijkAan);

			IDataProvider<Verbintenis> provider =
				GeneralFilteredSortableDataProvider.of(filter, VerbintenisDataAccessHelper.class);

			filter.addOrderByProperty("begindatum");
			filter.setAscending(false);
			CustomDataPanel<Verbintenis> datapanel =
				new EduArteDataPanel<Verbintenis>("datapanel", provider,
					new VerbintenisTable<Verbintenis>());
			datapanel.setItemsPerPage(5);
			add(datapanel);
		}
	}

	private class BPVsPanel extends CollapsablePanel<Verbintenis>
	{
		private static final long serialVersionUID = 1L;

		public BPVsPanel(String id, IModel<Verbintenis> verbintenisModel)
		{
			super(id, verbintenisModel, "Actieve BPV's");
		}

		@Override
		protected void createContents()
		{
			BPVInschrijvingZoekFilter filter = new BPVInschrijvingZoekFilter();
			filter.setDeelnemer(getModelObject().getDeelnemer());
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
			filter.setPeildatum(null);
			List<BPVStatus> statusOngelijkAan = new ArrayList<BPVStatus>();
			statusOngelijkAan.add(BPVStatus.Afgemeld);
			statusOngelijkAan.add(BPVStatus.Afgewezen);
			statusOngelijkAan.add(BPVStatus.Beëindigd);
			filter.setBpvStatusOngelijkAanList(statusOngelijkAan);

			IDataProvider<BPVInschrijving> provider =
				GeneralFilteredSortableDataProvider.of(filter,
					BPVInschrijvingDataAccessHelper.class);

			filter.addOrderByProperty("begindatum");
			filter.setAscending(false);
			CustomDataPanel<BPVInschrijving> datapanel =
				new EduArteDataPanel<BPVInschrijving>("datapanel", provider,
					new BPVInschrijvingTable());
			datapanel.setItemsPerPage(5);
			add(datapanel);
		}
	}

	private class GroepenPanel extends CollapsablePanel<Deelnemer>
	{
		private static final long serialVersionUID = 1L;

		public GroepenPanel(String id, IModel<Deelnemer> deelnemerModel)
		{
			super(id, deelnemerModel, "Actieve groepen");
		}

		@Override
		protected void createContents()
		{
			GroepsdeelnameZoekFilter filter = new GroepsdeelnameZoekFilter(getModelObject());
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
			filter.addOrderByProperty("groep.code");
			filter.addOrderByProperty("begindatum");
			filter.setAscending(false);

			IDataProvider<Groepsdeelname> provider =
				GeneralFilteredSortableDataProvider
					.of(filter, GroepsdeelnameDataAccessHelper.class);

			CustomDataPanel<Groepsdeelname> datapanel =
				new EduArteDataPanel<Groepsdeelname>("datapanel", provider,
					new DeelnemerGroepsdeelnameTable());
			datapanel.setItemsPerPage(20);
			add(datapanel);
		}
	}

	private class ResultatenPanel extends CollapsablePanel<Verbintenis>
	{
		private static final long serialVersionUID = 1L;

		public ResultatenPanel(String id, IModel<Verbintenis> verbintenisModel)
		{
			super(id, verbintenisModel, "Resultaten");
		}

		@Override
		protected void createContents()
		{
			DeelnemerResultatenRapportageModel deelnemerResultatenRapportageModel =
				new DeelnemerResultatenRapportageModel(getModel(),
					new OrganisatieEenheidLocatieAuthorizationContext(this));

			ListModelDataProvider<Toets> provider =
				new ListModelDataProvider<Toets>(new PropertyModel<List<Toets>>(
					deelnemerResultatenRapportageModel, "toetsen"));

			CustomDataPanel<Toets> datapanel =
				new EduArteDataPanel<Toets>("datapanel", provider, new ToetsResultaatTable(
					deelnemerResultatenRapportageModel.getResultatenModel()));
			datapanel.setItemsPerPage(20);
			add(datapanel);
		}
	}

	private class ToetsResultaatTable extends CustomDataPanelContentDescription<Toets>
	{
		private static final long serialVersionUID = 1L;

		private ResultatenModel resultatenModel;

		public ToetsResultaatTable(ResultatenModel resultatenModel)
		{
			super("Resultaten");
			this.resultatenModel = resultatenModel;

			createColumns();
		}

		private void createColumns()
		{
			addColumn(new CustomPropertyColumn<Toets>("Naam", "Naam", "naam", "naam"));

			addColumn(new CustomPropertyColumn<Toets>("Datum", "Datum", "datum")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void populateItem(WebMarkupContainer cell, String componentId,
						WebMarkupContainer row, IModel<Toets> rowModel, int span)
				{
					Resultaat resultaat =
						resultatenModel.getResultaat(rowModel, new Model<Deelnemer>(resultatenModel
							.getDeelnemers().get(0)), ResultatenModel.RESULTAAT_IDX);
					if (resultaat != null)
						cell.add(new Label(componentId, TimeUtil.getInstance().formatDate(
							resultaat.getCreatedAt())));
					else
						cell.add(new WebMarkupContainer(componentId));
				}
			});

			addColumn(new CustomPropertyColumn<Toets>("Onderwijsproduct", "Onderwijsproduct",
				"onderwijsproduct")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void populateItem(WebMarkupContainer cell, String componentId,
						WebMarkupContainer row, IModel<Toets> rowModel, int span)
				{
					Toets toets = rowModel.getObject();

					Onderwijsproduct onderwijsproduct =
						toets.getResultaatstructuur().getOnderwijsproduct();

					cell.add(new Label(componentId, onderwijsproduct.toString()));
				}
			});
			addColumn(new CustomPropertyColumn<Toets>("Cijfer", "Cijfer", "cijfer")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void populateItem(WebMarkupContainer cell, String componentId,
						WebMarkupContainer row, IModel<Toets> rowModel, int span)
				{
					Resultaat resultaat =
						resultatenModel.getResultaat(rowModel, new Model<Deelnemer>(resultatenModel
							.getDeelnemers().get(0)), ResultatenModel.RESULTAAT_IDX);
					if (resultaat != null)
						cell.add(new Label(componentId, resultaat.getFormattedDisplayWaarde()));
					else
						cell.add(new WebMarkupContainer(componentId));
				}
			});
		}

		@Override
		public void detach()
		{
			ComponentUtil.detachQuietly(resultatenModel);
		}
	}
}
