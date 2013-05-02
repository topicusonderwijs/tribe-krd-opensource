package nl.topicus.eduarte.web.components.modalwindow.stagemarktbedrijfsgegevens;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.cobra.web.pages.FeedbackComponent;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.web.components.factory.BPVModuleComponentFactory;
import nl.topicus.eduarte.web.components.factory.StagemarktBedrijfsgegevens;
import nl.topicus.eduarte.web.components.factory.StagemarktBedrijfsgegevensZoekFilter;
import nl.topicus.eduarte.web.components.factory.StagemarktServiceAdapter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.filter.renderer.ZoekFilterMarkupRenderer;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class StagemarktBedrijfsgegevensPanel extends
		CobraModalWindowBasePanel<StagemarktBedrijfsgegevens>
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	private AutoFieldSet<StagemarktBedrijfsgegevensZoekFilter> fieldset;

	private EduArteDataPanel<StagemarktBedrijfsgegevens> dataPanel;

	private StagemarktBedrijfsgegevensZoekFilter zoekfilter;

	public StagemarktBedrijfsgegevensPanel(String id,
			CobraModalWindow<StagemarktBedrijfsgegevens> modalWindow)
	{
		this(id, modalWindow, null);
	}

	public StagemarktBedrijfsgegevensPanel(String id,
			CobraModalWindow<StagemarktBedrijfsgegevens> modalWindow,
			StagemarktBedrijfsgegevensZoekFilter filter)
	{
		super(id, modalWindow);

		if (filter != null)
		{
			zoekfilter = filter;
		}
		else
		{
			zoekfilter = new StagemarktBedrijfsgegevensZoekFilter();
		}

		add(form = new Form<Void>("form"));
		form.add(fieldset =
			new AutoFieldSet<StagemarktBedrijfsgegevensZoekFilter>("inputfields",
				new PropertyModel<StagemarktBedrijfsgegevensZoekFilter>(this, "zoekfilter")));
		fieldset.setPropertyNames("bedrijfsnaam", "postcode", "huisnummer");
		fieldset.setMarkupRendererName(ZoekFilterMarkupRenderer.NAME);
		fieldset.setRenderMode(RenderMode.EDIT);

		AjaxSubmitLink submit = new AjaxSubmitLink("submit", form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > submitForm)
			{
				// refresh triggert webservice lookup
				target.addComponent(dataPanel);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > submitForm)
			{
				FeedbackComponent feedbackComponent = findParent(FeedbackComponent.class);
				if (feedbackComponent != null)
				{
					feedbackComponent.refreshFeedback(target);
				}
				target.addComponent(StagemarktBedrijfsgegevensPanel.this);
			}
		};
		submit.add(new AttributeModifier("src", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return getRequest().getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/searchicon.gif";
			}
		}));
		form.setDefaultButton(submit);
		form.add(submit);

		dataPanel = createDataPanel("dataPanel");
		dataPanel.setRowFactory(createRowFactory());
		add(dataPanel);
	}

	protected EduArteDataPanel<StagemarktBedrijfsgegevens> createDataPanel(String id)
	{
		return new EduArteDataPanel<StagemarktBedrijfsgegevens>(id,
			new ListModelDataProvider<StagemarktBedrijfsgegevens>(
				createStagemarktBedrijfsgegevensListModel()), new StagemarktBedrijfsgegevensTable())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> createTitleModel(String title)
			{
				return new Model<String>("Stagemarkt Bedrijfsgegevens");
			}
		};
	}

	private CustomDataPanelAjaxClickableRowFactory<StagemarktBedrijfsgegevens> createRowFactory()
	{
		return new CustomDataPanelAjaxClickableRowFactory<StagemarktBedrijfsgegevens>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<StagemarktBedrijfsgegevens> item)
			{
				target.addComponent(dataPanel);
			}

		};
	}

	private List<StagemarktBedrijfsgegevens> getBedrijfsgegevens()
	{
		List<StagemarktBedrijfsgegevens> result = new ArrayList<StagemarktBedrijfsgegevens>();
		List<BPVModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(BPVModuleComponentFactory.class);
		StagemarktBedrijfsgegevensZoekFilter filter = getZoekfilter();

		if (factories.size() > 0 && !zoekfilter.isEmpty())
		{
			StagemarktServiceAdapter adapter = factories.get(0).getStagemarktServiceAdapter();
			result =
				adapter.getBedrijfgegevens(filter.getBedrijfsnaam(), filter.getPostcode(), filter
					.getHuisnummer() != null ? filter.getHuisnummer() : 0, null);
			// TODO: Brin kenniscentrum meegeven (is nu null)?
		}
		return result;
	}

	private LoadableDetachableModel<List<StagemarktBedrijfsgegevens>> createStagemarktBedrijfsgegevensListModel()
	{
		return new LoadableDetachableModel<List<StagemarktBedrijfsgegevens>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<StagemarktBedrijfsgegevens> load()
			{
				return getBedrijfsgegevens();
			}
		};
	}

	public StagemarktBedrijfsgegevensZoekFilter getZoekfilter()
	{
		return zoekfilter;
	}

	public void setZoekfilter(StagemarktBedrijfsgegevensZoekFilter zoekfilter)
	{
		this.zoekfilter = zoekfilter;
	}
}
