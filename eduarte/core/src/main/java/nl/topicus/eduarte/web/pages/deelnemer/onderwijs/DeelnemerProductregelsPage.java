package nl.topicus.eduarte.web.pages.deelnemer.onderwijs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.IModelDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.datapanel.columns.PanelColumn;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.deelnemer.onderwijsproducten.DeelnemerOnderwijsproductenInzien;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameContextDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.columns.OndPrAfnKeuzeColumnPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ProductregelTable;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
@PageInfo(title = "Deelnemer Productregels", menu = {"Deelnemer > [deelnemer] > Onderwijs > Productregels"})
@InPrincipal(DeelnemerOnderwijsproductenInzien.class)
public class DeelnemerProductregelsPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	public DeelnemerProductregelsPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer()
			.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerProductregelsPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerProductregelsPage(Verbintenis inschrijving)
	{
		this(inschrijving.getDeelnemer(), inschrijving);
	}

	public DeelnemerProductregelsPage(Deelnemer deelnemer, Verbintenis inschrijving)
	{
		super(DeelnemerMenuItem.Productregels, deelnemer, inschrijving);
		IModel<List<Productregel>> productRegels = new ProductregelsModel();
		IModelDataProvider<Productregel> provider =
			new IModelDataProvider<Productregel>(productRegels);
		ProductregelTable table = new ProductregelTable();
		table.addColumn(new PanelColumn<Productregel>("Keuze", "Keuze")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Panel getPanel(String componentId, WebMarkupContainer row,
					IModel<Productregel> model)
			{
				return new OndPrAfnKeuzeColumnPanel(componentId, model,
					getContextVerbintenisModel());
			}
		});
		table.addColumn(new AbstractCustomColumn<Productregel>("Werkstuktitel", "Werkstuktitel")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(WebMarkupContainer cell, String componentId,
					WebMarkupContainer row, IModel<Productregel> rowModel, int span)
			{
				Productregel regel = rowModel.getObject();
				Map<Productregel, OnderwijsproductAfnameContext> keuzes =
					DataAccessRegistry.getHelper(
						OnderwijsproductAfnameContextDataAccessHelper.class).list(
						getContextVerbintenis());
				OnderwijsproductAfnameContext keuze = keuzes.get(regel);
				if (keuze != null && keuze.getOnderwijsproductAfname() != null
					&& StringUtil.isNotEmpty(keuze.getOnderwijsproductAfname().getWerkstuktitel()))
				{
					cell.add(new Label(componentId, keuze.getOnderwijsproductAfname()
						.getWerkstuktitel()));
				}
				else
				{
					cell.add(new Label(componentId, ""));
				}
			}

		}.setDefaultVisible(false));

		if (inschrijving.isVAVOVerbintenis() || inschrijving.isVOVerbintenis())
		{
			table.addColumn(new AbstractCustomColumn<Productregel>("Diplomavak", "Diplomavak")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void populateItem(WebMarkupContainer cell, String componentId,
						WebMarkupContainer row, IModel<Productregel> rowModel, int span)
				{
					Productregel regel = rowModel.getObject();
					Map<Productregel, OnderwijsproductAfnameContext> keuzes =
						DataAccessRegistry.getHelper(
							OnderwijsproductAfnameContextDataAccessHelper.class).list(
							getContextVerbintenis());
					OnderwijsproductAfnameContext keuze = keuzes.get(regel);
					if (keuze != null && keuze.isDiplomavak())
					{
						cell.add(new Label(componentId, "Ja"));
					}
					else
					{
						cell.add(new Label(componentId, "Nee"));
					}
				}

			}.setDefaultVisible(false));

			table
				.addColumn(new AbstractCustomColumn<Productregel>("Volg. tijdvak", "Volg. tijdvak")
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void populateItem(WebMarkupContainer cell, String componentId,
							WebMarkupContainer row, IModel<Productregel> rowModel, int span)
					{
						Productregel regel = rowModel.getObject();
						Map<Productregel, OnderwijsproductAfnameContext> keuzes =
							DataAccessRegistry.getHelper(
								OnderwijsproductAfnameContextDataAccessHelper.class).list(
								getContextVerbintenis());
						OnderwijsproductAfnameContext keuze = keuzes.get(regel);
						if (keuze != null && keuze.isVerwezenNaarVolgendTijdvak())
						{
							cell.add(new Label(componentId, "Ja"));
						}
						else
						{
							cell.add(new Label(componentId, "Nee"));
						}
					}

				}.setDefaultVisible(false));

			if (inschrijving.isHavoVwoVerbintenis())
			{
				table.addColumn(new AbstractCustomColumn<Productregel>("indWerkstuk",
					"Ind. werkstuk")
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void populateItem(WebMarkupContainer cell, String componentId,
							WebMarkupContainer row, IModel<Productregel> rowModel, int span)
					{
						Productregel regel = rowModel.getObject();
						Map<Productregel, OnderwijsproductAfnameContext> keuzes =
							DataAccessRegistry.getHelper(
								OnderwijsproductAfnameContextDataAccessHelper.class).list(
								getContextVerbintenis());
						OnderwijsproductAfnameContext keuze = keuzes.get(regel);
						if (keuze != null && keuze.isWerkstukHoortBijProduct())
						{
							cell.add(new Label(componentId, "Ja"));
						}
						else
						{
							cell.add(new Label(componentId, "Nee"));
						}
					}

				}.setDefaultVisible(false));
			}

			table.addColumn(new AbstractCustomColumn<Productregel>("Toepassingresultaat",
				"Toepassingresultaat")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void populateItem(WebMarkupContainer cell, String componentId,
						WebMarkupContainer row, IModel<Productregel> rowModel, int span)
				{
					Productregel regel = rowModel.getObject();
					Map<Productregel, OnderwijsproductAfnameContext> keuzes =
						DataAccessRegistry.getHelper(
							OnderwijsproductAfnameContextDataAccessHelper.class).list(
							getContextVerbintenis());
					OnderwijsproductAfnameContext keuze = keuzes.get(regel);
					if (keuze != null)
					{
						cell.add(new Label(componentId, keuze.getToepassingResultaat().toString()));
					}
					else
					{
						cell.add(new Label(componentId, ""));
					}
				}

			}.setDefaultVisible(false));
		}

		CustomDataPanel<Productregel> datapanel =
			new EduArteDataPanel<Productregel>("datapanel", provider, table);
		datapanel.setItemsPerPage(Integer.MAX_VALUE);
		form = new Form<Void>("form");
		add(form);
		form.add(datapanel);
		createComponents();
	}

	private final class ProductregelsModel extends LoadableDetachableModel<List<Productregel>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Productregel> load()
		{
			List<Productregel> regels = new ArrayList<Productregel>();

			if (getContextVerbintenis().getOpleiding() != null
				&& getContextVerbintenis().getCohort() != null)
			{
				ProductregelDataAccessHelper helper =
					DataAccessRegistry.getHelper(ProductregelDataAccessHelper.class);
				ProductregelZoekFilter filter =
					new ProductregelZoekFilter(getContextVerbintenis().getOpleiding(),
						getContextVerbintenis().getCohort());
				filter.addOrderByProperty("soortProductregel");
				regels = helper.list(filter);
			}

			return regels;
		}
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		ModuleEditPageButton<Verbintenis> bewerken =
			new ModuleEditPageButton<Verbintenis>(panel, "Bewerken", CobraKeyAction.BEWERKEN,
				Verbintenis.class, getSelectedMenuItem(), DeelnemerProductregelsPage.this,
				getContextVerbintenisModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					// zichtbaar indien de verbintenis nog niet afgesloten is (Beeindigd,
					// Afgewezen, Afgemeld).
					return super.isVisible() && !getContextVerbintenis().getStatus().isAfgesloten();
				}
			};
		panel.addButton(bewerken);
	}

	/*
	 * Niet bookmarkable omdat deze pagina niet relevant is als de deelnemer geen
	 * opleiding heeft.
	 */
	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}
}
