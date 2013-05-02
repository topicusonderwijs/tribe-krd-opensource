package nl.topicus.eduarte.web.pages.beheer.organisatie;

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
import nl.topicus.eduarte.core.principals.beheer.organisatie.ExterneOrganisatieKenmerken;
import nl.topicus.eduarte.entities.kenmerk.ExterneOrganisatieKenmerk;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.providers.ExterneOrganisatieProvider;
import nl.topicus.eduarte.web.components.menu.ExterneOrganisatieMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ExterneOrganisatieKenmerkTable;
import nl.topicus.eduarte.web.components.panels.kenmerk.ExterneOrganisatieKenmerkenPanel;

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
@PageInfo(title = "Kenmerken", menu = {"ExterneOrganisatie > [externeOrganisatie] > Verbintenis > Kenmerken"})
@InPrincipal(ExterneOrganisatieKenmerken.class)
public class ExterneOrganisatieKenmerkenPage extends AbstractExterneOrganisatiePage
{
	private static final long serialVersionUID = 1L;

	private CustomDataPanel<ExterneOrganisatieKenmerk> dataPanel;

	private ExterneOrganisatieKenmerkenPanel detailPanel;

	public ExterneOrganisatieKenmerkenPage(ExterneOrganisatieProvider provider)
	{
		this(provider.getExterneOrganisatie());
	}

	public ExterneOrganisatieKenmerkenPage(ExterneOrganisatie externeOrganisatie)
	{
		super(ExterneOrganisatieMenuItem.Kenmerken, externeOrganisatie);
		detailPanel = new ExterneOrganisatieKenmerkenPanel("detailPanel", getEersteKenmerkModel())
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

	private EduArteDataPanel<ExterneOrganisatieKenmerk> createDataPanel(String id)
	{
		return new EduArteDataPanel<ExterneOrganisatieKenmerk>(id,
			new ListModelDataProvider<ExterneOrganisatieKenmerk>(createKenmerkenListModel()),
			new ExterneOrganisatieKenmerkTable())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> createTitleModel(String title)
			{
				return new Model<String>("Kenmerken");
			}

		};
	}

	private CustomDataPanelAjaxClickableRowFactory<ExterneOrganisatieKenmerk> createRowFactory()
	{
		return new CustomDataPanelAjaxClickableRowFactory<ExterneOrganisatieKenmerk>(detailPanel
			.getExterneOrganisatieKenmerkModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<ExterneOrganisatieKenmerk> item)
			{
				target.addComponent(dataPanel);
				target.addComponent(detailPanel);
			}

		};
	}

	private void addBewerkKenmerkButton(BottomRowPanel panel)
	{
		ModuleEditPageButton<ExterneOrganisatieKenmerk> ret =
			new ModuleEditPageButton<ExterneOrganisatieKenmerk>(panel, "Bewerken",
				CobraKeyAction.BEWERKEN, ExterneOrganisatieKenmerk.class, getSelectedMenuItem(),
				ExterneOrganisatieKenmerkenPage.this, detailPanel
					.getExterneOrganisatieKenmerkModel())
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
		ModuleEditPageButton<ExterneOrganisatieKenmerk> ret =
			new ModuleEditPageButton<ExterneOrganisatieKenmerk>(panel, "Kenmerk toevoegen",
				CobraKeyAction.TOEVOEGEN, ExterneOrganisatieKenmerk.class, getSelectedMenuItem(),
				ExterneOrganisatieKenmerkenPage.this, null)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected ExterneOrganisatieKenmerk getEntity()
				{
					return createNieuwKenmerk();
				}

			};
		panel.addButton(ret);
	}

	private ExterneOrganisatieKenmerk createNieuwKenmerk()
	{
		ExterneOrganisatieKenmerk ret =
			new ExterneOrganisatieKenmerk(getContextExterneOrganisatie());
		return ret;
	}

	public IModel<ExterneOrganisatieKenmerk> getEersteKenmerkModel()
	{
		return new SelectedOrFirstModel<ExterneOrganisatieKenmerk>(ModelFactory
			.getModel((ExterneOrganisatieKenmerk) null),
			new PropertyModel<List<ExterneOrganisatieKenmerk>>(getContextExterneOrganisatieModel(),
				"kenmerken"));
	}

	private LoadableDetachableModel<List<ExterneOrganisatieKenmerk>> createKenmerkenListModel()
	{
		return new LoadableDetachableModel<List<ExterneOrganisatieKenmerk>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ExterneOrganisatieKenmerk> load()
			{
				return getContextExterneOrganisatie().getKenmerken();
			}
		};
	}

}
