package nl.topicus.eduarte.web.components.resultaat;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;

public class ResultaatEditorPanel extends Panel implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	private Component table;

	private ResultatenModel resultatenModel;

	private int aantalPogingen;

	private boolean multiToets;

	private EditorJavascriptRenderer< ? > javascriptRenderer;

	private boolean editable;

	public ResultaatEditorPanel(String id, Component table, ResultatenModel resultatenModel,
			boolean multiToets, final boolean editable,
			EditorJavascriptRenderer< ? > javascriptRenderer)
	{
		super(id);

		this.table = table;
		this.resultatenModel = resultatenModel;
		this.aantalPogingen = resultatenModel.getAbsoluutMaximumAantalPogingen();
		this.multiToets = multiToets;
		this.editable = editable;
		this.javascriptRenderer = javascriptRenderer;
		javascriptRenderer.setResultaatEditorPanel(this);

		add(new ResultaatEditorSliderPanel("leftSlider", aantalPogingen, true));
		add(new ResultaatEditorSliderPanel("rightSlider", aantalPogingen, false));

		add(new AttributeAppender("class", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return editable ? "readwrite" : "readonly";
			}
		}, " "));
	}

	public boolean isEditable()
	{
		return editable;
	}

	public void setEditable(boolean editable)
	{
		this.editable = editable;
	}

	@Override
	public void contribute(WiQueryResourceManager wiQueryResourceManager)
	{
		wiQueryResourceManager.addJavaScriptResource(new JavascriptResourceReference(
			ResultaatEditorPanel.class, "resultaateditor.js"));
	}

	@Override
	public JsStatement statement()
	{
		JsStatement statement =
			new JsStatement().append(javascriptRenderer.renderJavascript(aantalPogingen,
				resultatenModel.isToetsMetAlternatiefAanwezig(), multiToets, editable));
		Options jsOptions = new Options();
		jsOptions.putLiteral("table", "#" + table.getMarkupId());
		statement.append(new JsQuery(this).$().chain("resultaatEditor",
			jsOptions.getJavaScriptOptions()).render());
		return statement;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		javascriptRenderer.detach();
	}
}
