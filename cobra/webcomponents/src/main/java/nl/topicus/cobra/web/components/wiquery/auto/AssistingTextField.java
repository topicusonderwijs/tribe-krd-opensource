package nl.topicus.cobra.web.components.wiquery.auto;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;
import org.odlabs.wiquery.ui.autocomplete.AutocompleteJavascriptResourceReference;
import org.odlabs.wiquery.ui.position.PositionJavascriptResourceReference;
import org.odlabs.wiquery.ui.widget.WidgetJavascriptResourceReference;

public abstract class AssistingTextField extends TextField<String> implements IWiQueryPlugin,
		IAutoCompleteBase<String>
{
	private static final long serialVersionUID = 1L;

	private JQueryAutoCompleteBehavior<String> behavior;

	private Options options = new Options();

	public AssistingTextField(String id, IModel<String> model)
	{
		super(id, model);
		setMaxResults(30);
		add(behavior = new JQueryAutoCompleteBehavior<String>(this));
	}

	public Options getOptions()
	{
		return options;
	}

	public int getMaxResults()
	{
		return options.getInt("max");
	}

	public void setMaxResults(int maxResults)
	{
		options.put("max", maxResults);
	}

	@Override
	public IAutoCompleteChoiceRenderer<String> getRenderer()
	{
		return new StringRenderer();
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(WidgetJavascriptResourceReference.get());
		resourceManager.addJavaScriptResource(PositionJavascriptResourceReference.get());
		resourceManager.addJavaScriptResource(AutocompleteJavascriptResourceReference.get());
	}

	@Override
	public JsStatement statement()
	{
		options.putLiteral("source", behavior.getCallbackUrl().toString());
		return new JsQuery(this).$().chain("autocomplete", options.getJavaScriptOptions());
	}
}
