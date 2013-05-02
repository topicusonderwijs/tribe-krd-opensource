package nl.topicus.cobra.web.components.wiquery;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.wiquery.resources.ResourceRefUtil;

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

public abstract class CarouselListPanel<T> extends TypedPanel<List<IModel<T>>> implements
		IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	private Options options = new Options();

	private WebMarkupContainer container;

	private ListView<List<IModel<T>>> listview;

	private int aantalItemsBovenElkaar = 5;

	private AbstractDefaultAjaxBehavior behave;

	private int selectedIndex = 0;

	private IModel<String> scriptModel;

	public CarouselListPanel(String id, IModel<List<IModel<T>>> model)
	{
		super(id, model);
		List<List<IModel<T>>> lists = getLists(model);
		behave = new AbstractDefaultAjaxBehavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void respond(final AjaxRequestTarget target)
			{
				selectedIndex =
					Integer.parseInt(RequestCycle.get().getRequest().getParameter("index"));
			}
		};
		add(behave);
		container = new WebMarkupContainer("carousel");
		listview = new ListView<List<IModel<T>>>("list", lists)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<List<IModel<T>>> item)
			{
				item.add(createContents("contents", item.getModelObject()));
			}
		};
		listview.setReuseItems(true);
		container.add(listview);
		add(container);

		setItemVisibleInCallback("setIndex" + container.getMarkupId());
		scriptModel = ModelFactory.getModel("");
		Label myScript = new Label("script", scriptModel);
		myScript.setEscapeModelStrings(false); // do not HTML escape JavaScript code
		add(myScript);
	}

	private List<List<IModel<T>>> getLists(IModel<List<IModel<T>>> model)
	{
		List<List<IModel<T>>> lists = new ArrayList<List<IModel<T>>>();
		List<IModel<T>> list = new ArrayList<IModel<T>>();
		int counter = 0;
		for (IModel<T> object : model.getObject())
		{
			if (counter < (aantalItemsBovenElkaar - 1))
			{
				list.add(object);
				counter++;
			}
			else
			{
				list.add(object);
				lists.add(list);
				list = new ArrayList<IModel<T>>();
				counter = 0;
			}
		}
		if (!list.isEmpty())
			lists.add(list);
		return lists;
	}

	public void setItemVisibleInCallback(String jsFunctionName)
	{
		options.put("itemVisibleInCallback", jsFunctionName);
	}

	public void removeAllFromListview()
	{
		this.options.put("start", selectedIndex);
		listview.removeAll();
	}

	public CarouselListPanel<T> setVisible(int visible)
	{
		this.options.put("visible", visible);
		return this;
	}

	public int getVisible()
	{
		if (this.options.containsKey("visible"))
		{
			return this.options.getInt("visible");
		}
		return 0;
	}

	public CarouselListPanel<T> setScroll(int scroll)
	{
		this.options.put("scroll", scroll);
		return this;
	}

	public int getScroll()
	{
		if (this.options.containsKey("scroll"))
		{
			return this.options.getInt("scroll");
		}
		return 0;
	}

	public CarouselListPanel<T> setIndex(int start)
	{
		this.options.put("start", start);
		return this;
	}

	public int getIndex()
	{
		return selectedIndex;
	}

	protected abstract WebMarkupContainer createContents(String id, List<IModel<T>> list);

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

	public int getAantalItemsBovenElkaar()
	{
		return aantalItemsBovenElkaar;
	}

	public void setAantalItemsBovenElkaar(int aantalItemsBovenElkaar)
	{
		this.aantalItemsBovenElkaar = aantalItemsBovenElkaar;
	}

	@Override
	protected void onBeforeRender()
	{
		scriptModel.setObject("function setIndex" + container.getMarkupId()
			+ "(carousel, object, index, state) {" + "wicketAjaxGet(\"" + behave.getCallbackUrl()
			+ " + &index=\"+index, function() { }, function() { })" + "}");
		super.onBeforeRender();
	}

}
