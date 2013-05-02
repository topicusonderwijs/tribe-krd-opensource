package nl.topicus.cobra.web.components.wiquery.indicator;

import org.apache.wicket.ajax.WicketAjaxReference;
import org.apache.wicket.markup.html.WicketEventReference;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class AjaxIndicator extends Panel implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	public AjaxIndicator(String id)
	{
		super(id);
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		resourceManager
			.addJavaScriptResource((JavascriptResourceReference) WicketEventReference.INSTANCE);
		resourceManager
			.addJavaScriptResource((JavascriptResourceReference) WicketAjaxReference.INSTANCE);
		resourceManager.addJavaScriptResource(AjaxIndicator.class, "ajaxindicator.js");
	}

	@Override
	public JsStatement statement()
	{
		return new JsQuery(this).$().chain("ajaxIndicator");
	}
}
