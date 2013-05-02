package nl.topicus.eduarte.web.pages;

import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.menu.main.MainMenuItem;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class CenterContainer extends WebMarkupContainer implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	public CenterContainer(String id, MainMenuItem menuItem)
	{
		super(id);
		add(new AppendingAttributeModifier("class", menuItem.getLabel().toLowerCase(), " "));
	}

	@Override
	public boolean isTransparentResolver()
	{
		return true;
	}

	@Override
	public void contribute(WiQueryResourceManager wiQueryResourceManager)
	{
		wiQueryResourceManager.addJavaScriptResource(CenterContainer.class, "center.js");
	}

	@Override
	public JsStatement statement()
	{
		return new JsQuery(this).$().chain("center");
	}
}
