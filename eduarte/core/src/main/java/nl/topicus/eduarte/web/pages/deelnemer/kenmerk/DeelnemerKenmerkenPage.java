package nl.topicus.eduarte.web.pages.deelnemer.kenmerk;

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
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemerKenmerken;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.kenmerk.DeelnemerKenmerk;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerKenmerkTable;
import nl.topicus.eduarte.web.components.panels.kenmerk.DeelnemerKenmerkenPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * 
 * 
 * @author loite
 */
@PageInfo(title = "Kenmerken", menu = {"Deelnemer > [deelnemer] > Verbintenis > Kenmerken"})
@InPrincipal(DeelnemerKenmerken.class)
public class DeelnemerKenmerkenPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	private CustomDataPanel<DeelnemerKenmerk> dataPanel;

	private DeelnemerKenmerkenPanel detailPanel;

	public DeelnemerKenmerkenPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer());
	}

	public DeelnemerKenmerkenPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerKenmerkenPage(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis);
	}

	public DeelnemerKenmerkenPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		super(DeelnemerMenuItem.Kenmerken, deelnemer, verbintenis);
		detailPanel = new DeelnemerKenmerkenPanel("detailPanel", getEersteKenmerkModel())
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
		addNieuwKenmerkButton(panel);
		addBewerkKenmerkButton(panel);
	}

	private EduArteDataPanel<DeelnemerKenmerk> createDataPanel(String id)
	{
		return new EduArteDataPanel<DeelnemerKenmerk>(id,
			new ListModelDataProvider<DeelnemerKenmerk>(createKenmerkenListModel()),
			new DeelnemerKenmerkTable())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> createTitleModel(String title)
			{
				return new Model<String>("Kenmerken");
			}

		};
	}

	private CustomDataPanelAjaxClickableRowFactory<DeelnemerKenmerk> createRowFactory()
	{
		return new CustomDataPanelAjaxClickableRowFactory<DeelnemerKenmerk>(detailPanel
			.getDeelnemerKenmerkModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<DeelnemerKenmerk> item)
			{
				target.addComponent(dataPanel);
				target.addComponent(detailPanel);
			}

		};
	}

	private void addBewerkKenmerkButton(BottomRowPanel panel)
	{
		ModuleEditPageButton<DeelnemerKenmerk> ret =
			new ModuleEditPageButton<DeelnemerKenmerk>(panel, "Bewerken", CobraKeyAction.BEWERKEN,
				DeelnemerKenmerk.class, getSelectedMenuItem(), DeelnemerKenmerkenPage.this,
				detailPanel.getDeelnemerKenmerkModel())
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

	private void addNieuwKenmerkButton(BottomRowPanel panel)
	{
		ModuleEditPageButton<DeelnemerKenmerk> ret =
			new ModuleEditPageButton<DeelnemerKenmerk>(panel, "Kenmerk toevoegen",
				CobraKeyAction.TOEVOEGEN, DeelnemerKenmerk.class, getSelectedMenuItem(),
				DeelnemerKenmerkenPage.this, null)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected DeelnemerKenmerk getEntity()
				{
					return createNieuwKenmerk();
				}

			};
		panel.addButton(ret);
	}

	private DeelnemerKenmerk createNieuwKenmerk()
	{
		DeelnemerKenmerk ret = new DeelnemerKenmerk(getContextDeelnemer());
		return ret;
	}

	public IModel<DeelnemerKenmerk> getEersteKenmerkModel()
	{
		return new SelectedOrFirstModel<DeelnemerKenmerk>(ModelFactory
			.getModel((DeelnemerKenmerk) null), new PropertyModel<List<DeelnemerKenmerk>>(
			getContextDeelnemerModel(), "kenmerken"));
	}

	private LoadableDetachableModel<List<DeelnemerKenmerk>> createKenmerkenListModel()
	{
		return new LoadableDetachableModel<List<DeelnemerKenmerk>>()
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected List<DeelnemerKenmerk> load()
			{
				return getContextDeelnemer().getKenmerken();
			}
		};
	}

}
