package nl.topicus.cobra.web.components.wiquery;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.wiquery.resources.ResourceRefUtil;

import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;

public abstract class CarouselPanel<T> extends TypedPanel<List<T>> implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	private Options options = new Options();

	private WebMarkupContainer container;

	private ListView<T> listview;

	int selectedIndex = 0;

	private IModel<String> scriptModel;

	private AbstractDefaultAjaxBehavior behave;

	private Component refreshComponent;

	private Label defaultContent;

	/**
	 * CarouselPanel is gemaakt om een carousel te creeren welke 1 item per keer laat zien
	 * en 1 item per keer scrollt. Het is natuurlijk mogelijk om meer te laten zien, maar
	 * de selectedIndex is dan <b>NIET</b> meer supported.
	 */
	public CarouselPanel(String id, final IModel<List<T>> listModel, Component refreshComponent)
	{
		super(id, listModel);
		options.put("visible", getItemsPerPage());
		options.put("scroll", getItemsPerScroll());

		this.refreshComponent = refreshComponent;

		behave = new AbstractDefaultAjaxBehavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void respond(final AjaxRequestTarget target)
			{
				selectedIndex =
					Integer.parseInt(RequestCycle.get().getRequest().getParameter("index"));
				CarouselPanel.this.refreshParenData();
				target.addComponent(CarouselPanel.this.refreshComponent);
			}
		};
		add(behave);

		container = new WebMarkupContainer("carousel")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return listModel.getObject().size() > 0;
			}
		};

		listview = new ListView<T>("list", listModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<T> item)
			{
				item.add(createContents("contents", item.getModel(), item.getIndex(), listModel
					.getObject().size()));
			}
		};
		listview.setReuseItems(true);
		container.add(listview);
		add(container);

		defaultContent = new Label("defaultContent", getDefaultContent())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return listview.getModelObject().size() == 0;
			}
		};

		add(defaultContent);

		setItemVisibleInCallback("setIndex" + container.getMarkupId());

		// this contains the id
		scriptModel = ModelFactory.getModel("");
		Label myScript = new Label("script", scriptModel);
		myScript.setEscapeModelStrings(false); // do not HTML escape JavaScript code
		add(myScript);

	}

	public abstract void refreshParenData();

	public abstract int getItemsPerScroll();

	public abstract int getItemsPerPage();

	public void removeAllFromListview()
	{
		listview.removeAll();
	}

	protected abstract WebMarkupContainer createContents(String id, IModel<T> model, int index,
			int size);

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		ResourceRefUtil.addCarousel(resourceManager);
	}

	@Override
	public JsStatement statement()
	{
		return new JsQuery(container).$().chain("jcarousel", options.getJavaScriptOptions());
	}

	public void setItemVisibleInCallback(String jsFunctionName)
	{
		options.put("itemVisibleInCallback", jsFunctionName);
	}

	public T getSelectedItem()
	{
		if (selectedIndex != 0 && listview.getModelObject().size() > 0)
		{
			return listview.getModelObject().get(selectedIndex - 1);
		}
		return null;
	}

	@Override
	protected void onBeforeRender()
	{
		scriptModel.setObject("function setIndex" + container.getMarkupId()
			+ "(carousel, object, index, state) {" + "wicketAjaxGet(\"" + behave.getCallbackUrl()
			+ " + &index=\"+index, function() { }, function() { })" + "}");
		super.onBeforeRender();
	}

	public abstract String getDefaultContent();

}
