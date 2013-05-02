package nl.topicus.eduarte.web.pages.medewerker;

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
import nl.topicus.eduarte.core.principals.deelnemer.MedewerkerKenmerken;
import nl.topicus.eduarte.entities.kenmerk.MedewerkerKenmerk;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.providers.MedewerkerProvider;
import nl.topicus.eduarte.web.components.menu.MedewerkerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.MedewerkerKenmerkTable;
import nl.topicus.eduarte.web.components.panels.kenmerk.MedewerkerKenmerkenPanel;

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
@PageInfo(title = "Kenmerken", menu = {"Medewerker > [medewerker] > Verbintenis > Kenmerken"})
@InPrincipal(MedewerkerKenmerken.class)
public class MedewerkerKenmerkenPage extends AbstractMedewerkerPage
{
	private static final long serialVersionUID = 1L;

	private CustomDataPanel<MedewerkerKenmerk> dataPanel;

	private MedewerkerKenmerkenPanel detailPanel;

	public MedewerkerKenmerkenPage(MedewerkerProvider provider)
	{
		this(provider.getMedewerker());
	}

	public MedewerkerKenmerkenPage(Medewerker medewerker)
	{
		super(MedewerkerMenuItem.Kenmerken, medewerker);
		detailPanel = new MedewerkerKenmerkenPanel("detailPanel", getEersteKenmerkModel())
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

	private EduArteDataPanel<MedewerkerKenmerk> createDataPanel(String id)
	{
		return new EduArteDataPanel<MedewerkerKenmerk>(id,
			new ListModelDataProvider<MedewerkerKenmerk>(createKenmerkenListModel()),
			new MedewerkerKenmerkTable())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> createTitleModel(String title)
			{
				return new Model<String>("Kenmerken");
			}

		};
	}

	private CustomDataPanelAjaxClickableRowFactory<MedewerkerKenmerk> createRowFactory()
	{
		return new CustomDataPanelAjaxClickableRowFactory<MedewerkerKenmerk>(detailPanel
			.getMedewerkerKenmerkModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<MedewerkerKenmerk> item)
			{
				target.addComponent(dataPanel);
				target.addComponent(detailPanel);
			}

		};
	}

	private void addBewerkKenmerkButton(BottomRowPanel panel)
	{
		ModuleEditPageButton<MedewerkerKenmerk> ret =
			new ModuleEditPageButton<MedewerkerKenmerk>(panel, "Bewerken", CobraKeyAction.BEWERKEN,
				MedewerkerKenmerk.class, getSelectedMenuItem(), MedewerkerKenmerkenPage.this,
				detailPanel.getMedewerkerKenmerkModel())
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
		ModuleEditPageButton<MedewerkerKenmerk> ret =
			new ModuleEditPageButton<MedewerkerKenmerk>(panel, "Kenmerk toevoegen",
				CobraKeyAction.TOEVOEGEN, MedewerkerKenmerk.class, getSelectedMenuItem(),
				MedewerkerKenmerkenPage.this, null)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected MedewerkerKenmerk getEntity()
				{
					return createNieuwKenmerk();
				}

			};
		panel.addButton(ret);
	}

	private MedewerkerKenmerk createNieuwKenmerk()
	{
		MedewerkerKenmerk ret = new MedewerkerKenmerk(getContextMedewerker());
		return ret;
	}

	public IModel<MedewerkerKenmerk> getEersteKenmerkModel()
	{
		return new SelectedOrFirstModel<MedewerkerKenmerk>(ModelFactory
			.getModel((MedewerkerKenmerk) null), new PropertyModel<List<MedewerkerKenmerk>>(
			getContextMedewerkerModel(), "kenmerken"));
	}

	private LoadableDetachableModel<List<MedewerkerKenmerk>> createKenmerkenListModel()
	{
		return new LoadableDetachableModel<List<MedewerkerKenmerk>>()
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected List<MedewerkerKenmerk> load()
			{
				return getContextMedewerker().getKenmerken();
			}
		};
	}

}
