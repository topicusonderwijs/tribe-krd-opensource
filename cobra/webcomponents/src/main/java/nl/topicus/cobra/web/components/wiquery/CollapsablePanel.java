package nl.topicus.cobra.web.components.wiquery;

import nl.topicus.cobra.web.behaviors.ClientSideCallable;
import nl.topicus.cobra.web.behaviors.ServerCallAjaxBehaviour;
import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;
import org.odlabs.wiquery.ui.accordion.AccordionJavaScriptResourceReference;
import org.odlabs.wiquery.ui.commons.WiQueryUIPlugin;

@WiQueryUIPlugin
public class CollapsablePanel<T> extends TypedPanel<T> implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	private Options options = new Options();

	private WebMarkupContainer content;

	private boolean contentsAdded = false;

	public CollapsablePanel(String id, String label)
	{
		this(id, null, label);
	}

	public CollapsablePanel(String id, IModel<T> model, String label)
	{
		super(id, model);

		add(new Label("label", label));
		options.putLiteral("header", "h5");
		options.put("alwaysOpen", false);
		options.put("collapsible", true);
		options.put("active", false);
		options.put("autoHeight", false);
		options.put("animated", new IEDisabledAnimation());

		content = new WebMarkupContainer("content")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isTransparentResolver()
			{
				return true;
			}
		};
		add(content);
		content.setVisible(false);
		content.setOutputMarkupPlaceholderTag(true);
		add(new ServerCallAjaxBehaviour());
	}

	protected void createContents()
	{
	}

	public CollapsablePanel<T> open()
	{
		options.removeOption("active");
		content.setVisible(true);
		return this;
	}

	public CollapsablePanel<T> close()
	{
		options.put("active", false);
		content.setVisible(false);
		return this;
	}

	public CollapsablePanel<T> setLoadAjax(boolean loadAjax)
	{
		content.setVisible(!loadAjax);
		return this;
	}

	@Override
	protected void onBeforeRender()
	{
		if (content.isVisible() && !contentsAdded)
		{
			createContents();
			contentsAdded = true;
		}
		super.onBeforeRender();
	}

	@ClientSideCallable
	public void onOpen(AjaxRequestTarget target)
	{
		if (!content.isVisible())
		{
			open();
			if (!contentsAdded)
			{
				createContents();
				contentsAdded = true;
			}
			target.addComponent(this);
		}
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(AccordionJavaScriptResourceReference.get());
	}

	@Override
	public JsStatement statement()
	{
		return new JsQuery(this).$().chain("accordion", options.getJavaScriptOptions()).chain(
			"bind",
			"'accordionchangestart.CollapsablePanel'",
			"function() {$('#" + getMarkupId()
				+ "').unbind('accordionchangestart.CollapsablePanel').serverCall('onOpen');}");

	}
}
