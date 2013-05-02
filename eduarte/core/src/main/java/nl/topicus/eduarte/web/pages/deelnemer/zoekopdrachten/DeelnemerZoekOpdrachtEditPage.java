package nl.topicus.eduarte.web.pages.deelnemer.zoekopdrachten;

import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.ColumnModel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelId;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelIdSource;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelService;
import nl.topicus.cobra.web.components.datapanel.DefaultCustomDataPanelId;
import nl.topicus.cobra.web.components.datapanel.settings.SelecteerKolommenModalWindow;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxActieButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.app.security.IgnoreEditPageWithoutWrite;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemersZoekenUitgebreid;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdracht;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdrachtRecht;
import nl.topicus.eduarte.util.XmlSerializer;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.zoekopdracht.DeelnemerZoekOpdrachtRechtEditPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerTable;
import nl.topicus.eduarte.web.components.panels.filter.uitgebreid.DeelnemerUitgebreidZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.security.actions.Render;
import org.apache.wicket.security.components.SecureComponentHelper;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;
import org.apache.wicket.spring.injection.annot.SpringBean;

@PageInfo(title = "Opgeslagen zoekopdracht bewerken", menu = "Deelnemer > Opgeslagen zoekopdrachten > bewerken")
@InPrincipal(DeelnemersZoekenUitgebreid.class)
@IgnoreEditPageWithoutWrite
public class DeelnemerZoekOpdrachtEditPage extends SecurePage implements IEditPage
{
	private static class ZoekOpdrachtColumnModel extends ColumnModel<Verbintenis>
	{
		private static final long serialVersionUID = 1L;

		private ZoekOpdrachtColumnModel(CustomDataPanelIdSource panelIdSource,
				CustomDataPanelContentDescription<Verbintenis> content,
				CustomDataPanelService<Verbintenis> service)
		{
			super(panelIdSource, content, service);
		}

		@Override
		public void resetToDefaults()
		{
			this.columnModel.setObject(getService().getColumns(null, getContent()));
		}

		@Override
		public void saveColumns()
		{
		}

		public void submit()
		{
			super.saveColumns();
		}
	}

	public static final String PUBLICEREN = "PUBLICEREN";

	private DeelnemerUitgebreidZoekFilterPanel filterPanel;

	private Form<Void> form;

	private SecurePage returnPage;

	private SelecteerKolommenModalWindow<Verbintenis> kolomKiezer;

	@SpringBean
	private CustomDataPanelService<Verbintenis> customDataPanelService;

	private ZoekOpdrachtColumnModel columnModel;

	public DeelnemerZoekOpdrachtEditPage(SecurePage returnPage, DeelnemerZoekOpdracht zoekOpdracht)
	{
		this(returnPage, zoekOpdracht, zoekOpdracht.deserializeFilter());
	}

	public DeelnemerZoekOpdrachtEditPage(SecurePage returnPage, DeelnemerZoekOpdracht zoekOpdracht,
			VerbintenisZoekFilter filter)
	{
		super(CoreMainMenuItem.Deelnemer);
		this.returnPage = returnPage;
		boolean publicerenRecht =
			new DataSecurityCheck(SecureComponentHelper.alias(DeelnemerZoekOpdrachtEditPage.class)
				+ PUBLICEREN).isActionAuthorized(Render.class);

		ModelManager manager =
			new DefaultModelManager(DeelnemerZoekOpdrachtRecht.class, DeelnemerZoekOpdracht.class);
		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(zoekOpdracht, manager));

		DeelnemerTable table = new DeelnemerTable();
		columnModel = new ZoekOpdrachtColumnModel(new CustomDataPanelIdSource()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public CustomDataPanelId getPanelId()
			{
				if (getZoekOpdracht().isSaved())
					return new DefaultCustomDataPanelId(DeelnemerZoekOpdracht.class,
						getZoekOpdracht().getId().toString(), getZoekOpdracht().isPersoonlijk());
				return null;
			}
		}, table, customDataPanelService);
		add(kolomKiezer =
			new SelecteerKolommenModalWindow<Verbintenis>("kolomKiezer", table, columnModel));

		add(form = new Form<Void>("form"));

		WebMarkupContainer rollenContainer = new WebMarkupContainer("rollenContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && !getZoekOpdracht().isPersoonlijk();
			}
		};
		rollenContainer.setOutputMarkupPlaceholderTag(true);
		form.add(rollenContainer);

		rollenContainer.add(new DeelnemerZoekOpdrachtRechtEditPanel("rollen",
			new PropertyModel<List<DeelnemerZoekOpdrachtRecht>>(getZoekOpdrachtModel(), "rechten"),
			manager)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public DeelnemerZoekOpdrachtRecht createNewT()
			{
				return new DeelnemerZoekOpdrachtRecht(getZoekOpdracht());
			}
		});

		AutoFieldSet<DeelnemerZoekOpdracht> zoekopdrachtFields =
			new AutoFieldSet<DeelnemerZoekOpdracht>("zoekopdracht", getZoekOpdrachtModel(),
				"Zoekopdracht");
		zoekopdrachtFields.setPropertyNames("omschrijving", "persoonlijk", "kolommenVastzetten",
			"peildatumVastzetten");
		zoekopdrachtFields.addFieldModifier(new EnableModifier(publicerenRecht, "persoonlijk"));
		zoekopdrachtFields.setRenderMode(RenderMode.EDIT);
		zoekopdrachtFields.addFieldModifier(new EduArteAjaxRefreshModifier("persoonlijk",
			rollenContainer));
		zoekopdrachtFields.addFieldModifier(new EduArteAjaxRefreshModifier("kolommenVastzetten")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				refreshBottomRow(target);
			}
		});
		form.add(zoekopdrachtFields);

		form.add(filterPanel = new DeelnemerUitgebreidZoekFilterPanel("filter", filter, true));

		createComponents();
	}

	@SuppressWarnings("unchecked")
	private IChangeRecordingModel<DeelnemerZoekOpdracht> getZoekOpdrachtModel()
	{
		return (IChangeRecordingModel<DeelnemerZoekOpdracht>) getDefaultModel();
	}

	private DeelnemerZoekOpdracht getZoekOpdracht()
	{
		return getZoekOpdrachtModel().getObject();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				DeelnemerZoekOpdracht opdracht = getZoekOpdracht();
				opdracht.setFilter(XmlSerializer.serializeValues(
					Arrays.asList(filterPanel.getFilter())).get(0));
				getZoekOpdrachtModel().saveObject();
				opdracht.commit();
				if (opdracht.isKolommenVastzetten())
				{
					columnModel.submit();
				}
				setResponsePage(OpgeslagenZoekOpdrachtenPage.class);
			}
		});
		panel.addButton(new AjaxActieButton(panel, "Kolommen kiezen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				kolomKiezer.show(target);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && getZoekOpdracht().isKolommenVastzetten();
			}
		}.setAlignment(ButtonAlignment.LEFT));
		panel.addButton(new AnnulerenButton(panel, returnPage));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.OpgeslagenZoekopdrachten);
	}
}
