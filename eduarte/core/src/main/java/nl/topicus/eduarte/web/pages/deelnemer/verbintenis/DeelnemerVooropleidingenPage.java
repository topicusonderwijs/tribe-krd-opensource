/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.deelnemer.verbintenis;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.datapanel.SelectedOrFirstModel;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemerVooropleidingen;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.VooropleidingTable;
import nl.topicus.eduarte.web.components.panels.verbintenis.DeelnemerVooropleidingenPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * @author idserda
 */
@PageInfo(title = "Vooropleidingen", menu = {"Deelnemer > [deelnemer] > Verbintenis > Vooropleidingen"})
@InPrincipal(DeelnemerVooropleidingen.class)
public class DeelnemerVooropleidingenPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	private CustomDataPanel<Vooropleiding> dataPanel;

	private DeelnemerVooropleidingenPanel detailPanel;

	public DeelnemerVooropleidingenPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer()
			.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerVooropleidingenPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerVooropleidingenPage(Verbintenis inschrijving)
	{
		this(inschrijving.getDeelnemer(), inschrijving);
	}

	public DeelnemerVooropleidingenPage(Deelnemer deelnemer, Verbintenis inschrijving)
	{
		super(DeelnemerMenuItem.Vooropleidingen, deelnemer, inschrijving);

		detailPanel =
			new DeelnemerVooropleidingenPanel("detailPanel", getEersteVooropleidingModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && getDefaultModelObject() != null;
				}

			};

		detailPanel.setOutputMarkupId(true);

		dataPanel = createDataPanel("dataPanel");
		dataPanel.setRowFactory(createRowFactory());

		add(detailPanel);
		add(dataPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		addNieuweVooropleidingButton(panel);
		addBewerkenButton(panel);
	}

	private EduArteDataPanel<Vooropleiding> createDataPanel(String id)
	{
		return new EduArteDataPanel<Vooropleiding>(id, new ListModelDataProvider<Vooropleiding>(
			createVooropleidingenListModel()), new VooropleidingTable())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> createTitleModel(String title)
			{
				return new Model<String>("Vooropleidingen");
			}

		};
	}

	private CustomDataPanelAjaxClickableRowFactory<Vooropleiding> createRowFactory()
	{
		return new CustomDataPanelAjaxClickableRowFactory<Vooropleiding>(detailPanel
			.getVooropleidingModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<Vooropleiding> item)
			{
				target.addComponent(dataPanel);
				target.addComponent(detailPanel);
			}

		};
	}

	private void addBewerkenButton(BottomRowPanel panel)
	{
		ModuleEditPageButton<Vooropleiding> ret =
			new ModuleEditPageButton<Vooropleiding>(panel, "Bewerken", CobraKeyAction.BEWERKEN,
				Vooropleiding.class, getSelectedMenuItem(), DeelnemerVooropleidingenPage.this,
				detailPanel.getVooropleidingModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && getDefaultModelObject() != null;
				}

			};
		panel.addButton(ret);
	}

	private void addNieuweVooropleidingButton(BottomRowPanel panel)
	{
		ModuleEditPageButton<Vooropleiding> ret =
			new ModuleEditPageButton<Vooropleiding>(panel, "Vooropleiding toevoegen",
				CobraKeyAction.TOEVOEGEN, Vooropleiding.class, getSelectedMenuItem(),
				DeelnemerVooropleidingenPage.this, null)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Vooropleiding getEntity()
				{
					return createNieuweVooropleiding();
				}

			};
		panel.addButton(ret);
	}

	private Vooropleiding createNieuweVooropleiding()
	{
		return new Vooropleiding(getContextDeelnemer());
	}

	public IModel<Vooropleiding> getEersteVooropleidingModel()
	{
		return new SelectedOrFirstModel<Vooropleiding>(ModelFactory.getModel((Vooropleiding) null),
			new PropertyModel<List<Vooropleiding>>(getContextDeelnemerModel(), "vooropleidingen"));
	}

	private LoadableDetachableModel<List<Vooropleiding>> createVooropleidingenListModel()
	{
		return new LoadableDetachableModel<List<Vooropleiding>>()
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected List<Vooropleiding> load()
			{
				return getContextDeelnemer().getVooropleidingen();
			}
		};
	}

}
