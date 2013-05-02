package nl.topicus.cobra.web.components.wiquery;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;
import org.odlabs.wiquery.ui.commons.WiQueryUIPlugin;
import org.odlabs.wiquery.ui.progressbar.ProgressBarJavaScriptResourceReference;
import org.odlabs.wiquery.ui.widget.WidgetJavascriptResourceReference;

/**
 * @author Henzen
 */
@WiQueryUIPlugin
public class Progressbar extends WebMarkupContainer implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	private Options options = new Options();

	public Progressbar(String id)
	{
		super(id);
	}

	public Progressbar(String id, IModel< ? > progressModel)
	{
		super(id, progressModel);
		add(new AttributeModifier("title", true, progressModel));
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(WidgetJavascriptResourceReference.get());
		resourceManager.addJavaScriptResource(ProgressBarJavaScriptResourceReference.get());
	}

	public JsStatement statement()
	{
		options.put("value", getDefaultModelObjectAsString());
		return new JsQuery(this).$().chain("progressbar", options.getJavaScriptOptions());
	}

}
