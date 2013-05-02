package nl.topicus.cobra.web.components.wiquery;

import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;
import org.odlabs.wiquery.ui.commons.WiQueryUIPlugin;
import org.odlabs.wiquery.ui.draggable.DraggableJavaScriptResourceReference;

@WiQueryUIPlugin
public class Draggable<T> extends TypedPanel<T> implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	private Options options = new Options();

	public Draggable(String id, String label)
	{
		this(id, null, label);
	}

	public Draggable(String id, IModel<T> model, String label)
	{
		super(id, model);
		add(new Label("label", label));
	}

	@Override
	public void contribute(WiQueryResourceManager wiQueryResourceManager)
	{
		wiQueryResourceManager.addJavaScriptResource(DraggableJavaScriptResourceReference.get());

	}

	@Override
	public JsStatement statement()
	{
		return new JsQuery(this).$().chain("draggable", options.getJavaScriptOptions());
	}
}
