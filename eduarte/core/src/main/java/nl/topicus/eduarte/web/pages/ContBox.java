package nl.topicus.eduarte.web.pages;

import nl.topicus.cobra.web.components.wiquery.SlidingFeedbackPanel;
import nl.topicus.cobra.web.components.wiquery.resources.ResourceRefUtil;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;

public class ContBox extends WebMarkupContainer implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	private SlidingFeedbackPanel feedback;

	public ContBox(String id, SlidingFeedbackPanel feedback, PageStyle pageStyle)
	{
		super(id);
		this.feedback = feedback;
		add(new SimpleAttributeModifier("class", pageStyle.getCssStyle()));
		setOutputMarkupId(true);
		setMarkupId("contBox");
	}

	@Override
	public boolean isTransparentResolver()
	{
		return true;
	}

	@Override
	public void contribute(WiQueryResourceManager wiQueryResourceManager)
	{
		ResourceRefUtil.addTimers(wiQueryResourceManager);
		wiQueryResourceManager.addJavaScriptResource(new JavascriptResourceReference(ContBox.class,
			"contbox.js"));
	}

	@Override
	public JsStatement statement()
	{
		Options options = new Options();
		options.putLiteral("feedback", feedback.getMarkupId());
		return new JsQuery(this).$().chain("contBox", options.getJavaScriptOptions());
	}
}
